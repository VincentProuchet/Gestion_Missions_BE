package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for {@link NatureService}.
 *
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
public class NatureServiceImpl implements NatureService {

    /**
     * The {@link NatureRepository} dependency.
     */
    private NatureRepository natureRepository;

    private MissionRepository missionRepository;

    @Override
    public List<Nature> list() {
        return this.natureRepository.findAll();
    }

    @Override
    public Nature create(Nature nature) throws BadRequestException {
        if (!canBeAdded(nature)) {
            throw new BadRequestException("A nature with this name already exists", ErrorCodes.natureInvalid);
        }
        if (!isAValidNature(nature)) {
            throw new BadRequestException("This nature is invalid : check the coherence of dates and the description is present", ErrorCodes.natureInvalid);
        }

        return this.natureRepository.save(nature);
    }

    @Override
    public Nature read(int id) throws BadRequestException {
        return this.natureRepository.findById(id).orElseThrow(() -> new BadRequestException("Nature not found", ErrorCodes.natureNotFound));
    }

    @Override
    public Nature update(int id, Nature nature) throws BadRequestException {

        if (id != nature.getId()) {
            throw new BadRequestException("The id is inconsistent with the given nature", ErrorCodes.idInconsistent);
        }
        if (!isAValidNature(nature)) {
            throw new BadRequestException("This nature is invalid : check the coherence of dates and the description is present", ErrorCodes.natureInvalid);
        }
        if (!canBeUpdated(id, nature)) {
            throw new BadRequestException("This nature can't be updated : consider create it instead, or get the latest version of it before updating", ErrorCodes.natureInvalid);
        }

        Nature registeredNature = natureRepository.findById(id).get();
        List<Nature> orderedListOfNatures = natureRepository.findByDescriptionOrderByDateOfValidityDesc(nature.getDescription());
        // the actual update in DB
        Nature activeNature;
        LocalDateTime now = LocalDateTime.now();
        if (isThisNatureInUse(registeredNature)) {

            registeredNature.setEndOfValidity(now);
            nature.setDateOfValidity(now);
            nature.setId(0);
            activeNature = natureRepository.save(nature);

            // update missions that referred to this nature
            List<Mission> futureMissions = missionRepository.findByNatureAndStartDateAfter(registeredNature, now);

            for (Mission mission : futureMissions) {
                mission.setNature(activeNature);
                missionRepository.save(mission);
            }

            natureRepository.save(registeredNature);

        } else {
            // if a nature is not used, it will be updated either way, active or not
            registeredNature.setGivesBonus(nature.isGivesBonus());
            registeredNature.setCharged(nature.isCharged());
            registeredNature.setTjm(nature.getTjm());
            registeredNature.setBonusPercentage(nature.getBonusPercentage());
            registeredNature.setDateOfValidity(now);
            registeredNature.setEndOfValidity(nature.getEndOfValidity());
            registeredNature.setDescription(nature.getDescription());
            this.natureRepository.save(registeredNature);

            activeNature = registeredNature;
        }

        return activeNature;
    }

    @Override
    public void delete(int id) throws BadRequestException {
        // if not used
        // delete from DB
        // return
        //
        // if active ie endDate null
        // set endDate to now
        // else
        // throw exception cant completely delete a used nature
        LocalDateTime now = LocalDateTime.now();
        Nature nature = read(id);

        if (!isThisNatureInUse(nature)) {
            this.natureRepository.delete(nature);
            return;
        }

        List<Mission> missions = missionRepository.findByNatureOrderByStartDateDesc(nature);

        Mission lastMission = missions.get(0);
        if (lastMission.getStartDate().isAfter(now)) {
            throw new BadRequestException("This nature will be used in the future, it can't be deleted, but it can be updated", ErrorCodes.natureCantBeDeleted);
        }

        if (nature.getEndOfValidity() != null) {
            throw new BadRequestException("This nature is in use and is already inactive, or it will be deactivated, it can't be deleted", ErrorCodes.natureCantBeDeleted);
        }
        nature.setEndOfValidity(now);
        natureRepository.save(nature);
    }

    @Override
    public List<Nature> getActiveNatures() {
        return natureRepository.findByEndOfValidityIsNull();
    }

    @Override
    public boolean isNatureActive(Nature nature, LocalDateTime date) {
        LocalDateTime endDateOfValidity = nature.getEndOfValidity();
        LocalDateTime startDateOfValidity = nature.getDateOfValidity();

        boolean currentlyValidIfDateIsNull = date == null && (endDateOfValidity == null || endDateOfValidity.isAfter(LocalDateTime.now()));
        boolean isDateInValidityPeriod = date != null && (endDateOfValidity == null || date.isBefore(endDateOfValidity)) && date.isAfter(startDateOfValidity);
        return currentlyValidIfDateIsNull || isDateInValidityPeriod;
    }

    @Override
    public boolean isAValidNature(Nature nature) {
        LocalDateTime endDateOfValidity = nature.getEndOfValidity();
        LocalDateTime startDateOfValidity = nature.getDateOfValidity();

        boolean areDatesValid = ((startDateOfValidity != null) && ((endDateOfValidity == null) || (endDateOfValidity.compareTo(startDateOfValidity) > 0)));
        boolean requiredDataIsPresent = nature.getDescription() != null && !nature.getDescription().equals("");
        return areDatesValid && requiredDataIsPresent;
    }

    @Override
    public boolean canBeAdded(Nature nature) {
        return this.natureRepository.findByDescription(nature.getDescription()).size() == 0;
    }

    @Override
    public boolean canBeUpdated(int id, Nature nature) {
        if (id != nature.getId()) {
            return false;
        }
        // should throw an exception
        List<Nature> orderedListOfNatures = natureRepository.findByDescriptionOrderByDateOfValidityDesc(nature.getDescription());
        if (orderedListOfNatures.size() == 0) {
            // inform that the right method to call is create
            return false;
        }

        // throws an id not found exception
        Optional<Nature> registeredNatureOptional = natureRepository.findById(id);
        if (registeredNatureOptional.isEmpty()) {
            return false;
        }

        Nature registeredNature = registeredNatureOptional.get();

        // check that the given nature id is the last with the given description
        // both data comes from the DB, so a check on IDs is enough
        return orderedListOfNatures.get(0).getId() == registeredNature.getId();
    }

    @Override
    public boolean isThisNatureInUse(Nature nature) {
        return !missionRepository.findByNatureIs(nature).isEmpty();
    }

}
