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
	/**
	 * The {@link MissionRepository} dependency. we may change it to MssioService I
	 * need to put more thoughts in that.
	 */
	private MissionRepository missionRepository;

	@Override
	public List<Nature> list() {
		return this.natureRepository.findAll();
	}

	@Override
	public Nature create(Nature nature) throws BadRequestException {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime end = null;
		nature.setEndOfValidity(end);
		nature.setDateOfValidity(now);

		if (!canBeAdded(nature)) {
			throw new BadRequestException(
					"A nature with this name already exists consider modify the existing one instead.",
					ErrorCodes.natureInvalid);
		}
		this.isAValidNature(nature);
		return this.natureRepository.save(nature);
	}

	@Override
	public Nature read(int id) throws BadRequestException {
		return this.natureRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Nature not found", ErrorCodes.natureNotFound));
	}

	@Override
	public List<Nature> read(String description) throws BadRequestException {		
		return this.natureRepository.findByDescription(description.toLowerCase());
	}

	/**
	 * 
	 * Check that the given nature is the last of the natures with the same
	 * description (active or not)
	 *
	 * @param id
	 * @param nature
	 * @return true if the nature can be updated, on condition it is valid
	 */
	public boolean canBeUpdated(Nature nature) throws BadRequestException {
		// should throw an exception
		List<Nature> orderedListOfNatures = natureRepository
				.findByDescriptionOrderByDateOfValidityDesc(nature.getDescription());
		if (orderedListOfNatures.size() == 0) {
			// if list is empty
			// inform that the right method to call is create
			return false;
		}

		// throws an id not found exception
		Optional<Nature> registeredNatureOptional = natureRepository.findById(nature.getId());
		if (registeredNatureOptional.isEmpty()) {
			return false;
		}

		Nature registeredNature = registeredNatureOptional.get();

		// check that the given nature id is the last with the given description
		// both data comes from the DB, so a check on IDs is enough
		return orderedListOfNatures.get(0).getId() == registeredNature.getId();
	}

	@Override
	public Nature update(int id, Nature nature) throws BadRequestException {

		// currently the start date is now and the end date is nul
		// we may decide to allow planning in the future
		LocalDateTime now = LocalDateTime.now();
		// LocalDateTime end = null;

		if (id != nature.getId()) {
			throw new BadRequestException("The id is inconsistent with the given nature", ErrorCodes.idInconsistent);
		}
		Nature registeredNature = this.read(nature.getId());
		if (nature.getDateOfValidity().isBefore(now)) {
			throw new BadRequestException(" updated nature can't start in the past", ErrorCodes.natureInvalid);
		}
		// well searching by label is done
		// as an intEgrity check to prevent creation of nature 
		// if an existing one is active
		// if the name is different of what is registered
		if (!registeredNature.getDescription().equals(nature.getDescription())) {
			// we check for other natures of the same description
			List<Nature> ListOfNaturesOfDescription = natureRepository
					.findByDescriptionOrderByDateOfValidityDesc(nature.getDescription());
			if (ListOfNaturesOfDescription.size() > 0) {
				for (Nature nature2 : ListOfNaturesOfDescription) {
					if (this.isNatureActive(nature2, nature.getDateOfValidity())) {
						throw new BadRequestException(
								" you are trying to change the name of the nature and there is allready an active nature of the name "
										+ nature.getDescription(),
								ErrorCodes.natureInvalid);
					}
				}
			}
		}


		Nature activeNature;
		// if existing nature is active
		if (isThisNatureInUse(registeredNature)) {
			// we create a new one
			// the new values receive data to become a valide new nature
			nature.setEndOfValidity(null);
			nature.setDateOfValidity(now);
			nature.setId(0);
			isAValidNature(nature);
			// and we save the new one
			// we new do save it before updating future missions
			activeNature = natureRepository.save(nature);

			// update future missions that referred to this nature
			List<Mission> futureMissions = missionRepository.findByNatureAndStartDateAfter(registeredNature, now);
			for (Mission mission : futureMissions) {
				mission.setNature(activeNature);
				missionRepository.save(mission);
			}
			// the actual update in DB
			// if everything allright to that point
			// we give it an endDateOfValidity
			registeredNature.setEndOfValidity(now);
			natureRepository.save(registeredNature);

		} else {
			// if a nature is not used, it will be updated either way, active or not
			registeredNature.setGivesBonus(nature.isGivesBonus());
			registeredNature.setCharged(nature.isCharged());
			registeredNature.setTjm(nature.getTjm());
			registeredNature.setBonusPercentage(nature.getBonusPercentage());
			registeredNature.setDateOfValidity(now);
			registeredNature.setEndOfValidity(null);
			// you can't update a nature description
			// registeredNature.setDescription(nature.getDescription());
			// we still check if for data integrity
			isAValidNature(registeredNature);
			// the save allways return the entity saved instance
			activeNature = this.natureRepository.save(registeredNature);
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
		// throw exception can't delete a used nature
		LocalDateTime now = LocalDateTime.now();
		Nature nature = read(id);
		// si nature is used
		// it get an end of validity
		// and is not erased
		if (isThisNatureInUse(nature)) {
			nature.setEndOfValidity(now);
			natureRepository.save(nature);
			return;
		}
		this.isNatureAssigned(nature);
		// if a nature is not used nor assigned to future mission
		// its deleted
		this.natureRepository.delete(nature);
		return;
	}

	/**
	 * test if a nature has an assigned mission this is a data integrity test made
	 * for internal use
	 * 
	 * @param nature to test
	 * @return true if it get to its end
	 * @throws BadRequestException
	 */
	public void isNatureAssigned(Nature nature) throws BadRequestException {
		// we get mission that have This nature assigned
		List<Mission> missions = missionRepository.findByNatureOrderByStartDateDesc(nature);
		// we check only the last mission in chronology
		Mission lastMission = missions.get(0);
		if (lastMission.getStartDate().isAfter(LocalDateTime.now())) {
			throw new BadRequestException("This nature as assigned mission and can't be deleted",
					ErrorCodes.natureCantBeDeleted);
		}
	}

	@Override
	public List<Nature> getActiveNatures() {
		return natureRepository.findByEndOfValidityIsNull();
	}

	@Override
	public boolean isNatureActive(Nature nature, LocalDateTime date) {
		// nature is active if its endofvalidity is null
		if (nature.getEndOfValidity() == null) {
			return true;
		}
		// if given date is null
		if (date == null) {
			// we give it now
			date = LocalDateTime.now();
		}
		// TRUE if the given date is before endDate AND after start date
		return (date.isBefore(nature.getEndOfValidity()) && date.isAfter(nature.getDateOfValidity()));
	}

	/**
	 * Returns true if the nature is currently active this is a data integrity test
	 * made for internal use mainly when creating/updating nature
	 *
	 * @param nature must not be null
	 * @return
	 */
	public boolean isAValidNature(Nature nature) throws BadRequestException {
		LocalDateTime endDateOfValidity = nature.getEndOfValidity();
		LocalDateTime startDateOfValidity = nature.getDateOfValidity();
		if (startDateOfValidity == null) {
			// throw startdateofvalidity can't be null
			throw new BadRequestException("startdateofvalidity can't be null", ErrorCodes.natureInvalid);
		}
		// if endofvalidity not null
		if (endDateOfValidity != null) {
			// if end of validity is before startofvalidity
			if (startDateOfValidity.isAfter(endDateOfValidity)) {
				// throw end of validity can't be after startofvalidity
				throw new BadRequestException("end of validity can't be after startofvalidity",
						ErrorCodes.natureInvalid);
			}
		}
		if (nature.getDescription() == null) {
			// throw description can't be null
			throw new BadRequestException("description can't be null", ErrorCodes.natureInvalid);
		}
		if (nature.getDescription().equals("")) {
			// throw description can't be empty
			throw new BadRequestException("description can't be empty", ErrorCodes.natureInvalid);
		}
		if (nature.getBonusPercentage() < 0) {
			throw new BadRequestException("bonus can't be negative", ErrorCodes.natureInvalid);
		}
		if (nature.getTjm().signum() == -1) {
			throw new BadRequestException("Tjm can't be negative", ErrorCodes.natureInvalid);
		}
		return true;
	}

	/**
	 * Check if no other nature has the same description
	 *
	 * @param nature
	 * @return true if no nature has the same description, false otherwise
	 */
	public boolean canBeAdded(Nature nature) {
		return this.natureRepository.findByDescription(nature.getDescription()).size() == 0;
	}

	/**
	 * For a nature to be deleted, created or modified, this must be false If true,
	 * the nature must be rendered inactive instead
	 *
	 * @param nature
	 * @return true if a mission use this nature
	 */
	public boolean isThisNatureInUse(Nature nature) {
		return !missionRepository.findByNatureIs(nature).isEmpty();
	}

}
