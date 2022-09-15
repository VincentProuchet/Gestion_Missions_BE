package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.Nature;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
public class NatureServiceImpl implements NatureService {

    /**
     * The {@link NatureRepository} dependency.
     */
    private NatureRepository natureRepository;

    private MissionService missionService;

    @Override
    public List<Nature> list() {
        return this.natureRepository.findAll();
    }

    @Override
    public void create(Nature nature) {
        if (!isAValidNature(nature) || !canBeAdded(nature)) {
            //should throw an exception
            return;
        }

        this.natureRepository.save(nature);
    }

    @Override
    public Nature read(int id) {
        return this.natureRepository.findById(id).orElseThrow();
    }

    @Override
    public Nature update(int id, Nature nature) {

        // validity checks, needs to throw an exception
        if (!isAValidNature(nature)) {
            return null;
        }
        if (!canBeUpdated(id, nature)) {
            return null;
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
            natureRepository.save(nature);
            natureRepository.save(registeredNature);
            activeNature = nature;
        } else {
            // if a nature is not used but is not active, it will be updated either way
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
    public void delete(int id) {
        // if not used
        // delete from DB
        // return
        //
        // if active ie endDate null
        // set endDate to now
        // else
        // throw exception cant completely delete a used nature
        Nature nature = read(id);

        if (!isThisNatureInUse(nature)) {
            this.natureRepository.delete(nature);
            return;
        }

        if (nature.getEndOfValidity() == null) {

            nature.setEndOfValidity(LocalDateTime.now());
            natureRepository.save(nature);

        } else {
            //throws an exception
            return;
        }
    }

    @Override
    public List<Nature> getActiveNatures() {
        return natureRepository.findByEndOfValidityIsNull();
    }

    @Override
    public boolean isAValidNature(Nature nature) {
        LocalDateTime endDate = nature.getEndOfValidity();
        LocalDateTime startDate = nature.getDateOfValidity();
        boolean areDatesValid = ((startDate != null) && (endDate == null) || (endDate.compareTo(startDate) >= 0));
        boolean requiredDataIsPresent = nature.getDescription() != null && nature.getDescription() != "";
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
        return !missionService.getMissionWhithGivenNature(nature).isEmpty();
    }

}
