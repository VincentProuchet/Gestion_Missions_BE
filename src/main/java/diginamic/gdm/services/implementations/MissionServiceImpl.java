package diginamic.gdm.services.implementations;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
import diginamic.gdm.vars.GDMVars;
import diginamic.gdm.vars.errors.ErrorsMessage;
import diginamic.gdm.vars.errors.impl.MissionErrors;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link MissionService}.
 *
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
//@Value("${path.to.your.property.key}") // for injecting properties values 
public class MissionServiceImpl implements MissionService {

	/** delay to respect when selecting flight as transport */
	private final int minDayBeforeFligthTransport = GDMVars.MIN_DAYS_BEFORE_FLIGHT_TRANSPORT;

	/**
	 * The {@link MissionRepository} dependency.
	 */
	private MissionRepository missionRepository;
	/**
	 * The {@link NatureService} dependency.
	 */
	private NatureService natureService;
	/**
	 * The {@link CollaboratorService} dependency.
	 */
	private CollaboratorService collaboratorService;

	/**
	 * returns true, but check if the mission status is INIT or REJECTED
	 * 
	 * @param mission the mission
	 * @return true if deleted
	 * @throws Exception
	 */
	public boolean canBeDeleted(Mission mission) throws BadRequestException {
		switch (mission.getStatus()) {
		case INIT:
		case REJECTED:
			return true;
		case ENDED:
		case VALIDATED:
		case WAITING_VALIDATION:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.delete.CANT_BE_DELETED,
					MissionErrors.delete.STATUSERROR, mission.getStatus().name());
		// any case that is not in the book is considered to allow deletion
		default:
			return true;
		}
	}

	/**
	 * Check if the mission status is INIT or REJECTED
	 *
	 * @param mission the mission
	 * @return true if the status allows the update
	 * @throws Exception
	 */
	public boolean canBeUpdated(Mission mission) throws BadRequestException {
		Mission missionIDB = read(mission.getId());
		switch (missionIDB.getStatus()) {
		case ENDED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_ENDED);
		case WAITING_VALIDATION:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_WAITING_VALIDATION);
		case VALIDATED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_VALIDATED);
		default:
			return true;
		}
	}

	@Override
	public List<Mission> completedMissions() {
		return missionRepository.findByStatusAndEndDateBefore(Status.VALIDATED, LocalDateTime.now());
	}

	@Override
	public List<Mission> completedMissionsToCompute() {
		return missionRepository.findByStatusAndEndDateBeforeAndHasBonusBeenEvaluatedFalse(Status.VALIDATED,
				LocalDateTime.now());
	}

	@Override
	public Mission create(Mission mission, boolean allowWE) throws BadRequestException {

		mission.setId(0);
		isThisMissionValid(mission, allowWE);
		mission.setStatus(Status.INIT);
		return this.missionRepository.save(mission);
	}

	@Override
	public void delete(int id) throws Exception {
		Mission mission = read(id);
		if (isMissionDone(mission)) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.delete.CANT_BE_DELETED,
					MissionErrors.delete.STATUSERROR, mission.getStatus().name());
		}
		if (this.canBeDeleted(mission)) {
			this.missionRepository.delete(mission);
		}
	}

	@Override
	public List<Mission> getMissionsOfCollaborator(Collaborator collaborator) {
		return missionRepository.findByCollaborator(collaborator);
	}

	/**
	 * Check if the mission has been completed make a persistence query using
	 * provided id
	 *
	 * @param id mission id
	 * @return true if completed
	 * @throws Exception
	 */
	@Override
	public boolean isMissionDone(int id) throws Exception {
		Mission mission = read(id);
		return mission.getStatus() == Status.VALIDATED && mission.getEndDate().isBefore(LocalDateTime.now());
	}

	/**
	 * internal useOnly Check if the mission has been completed this override is
	 * made to reduce the database queries quantities if you allready got the JPA
	 * instance of a specific mission you can just pass it here if not then use the
	 * id version
	 *
	 * @param mission
	 * @return true if completed
	 * @throws Exception
	 */
	private boolean isMissionDone(Mission mission) throws Exception {
		return mission.getStatus() == Status.VALIDATED && mission.getEndDate().isBefore(LocalDateTime.now());
	}

	/**
	 * Check the validity of the mission request, allow a date in WE
	 *
	 * @param allowWE allow to work in WE
	 * @param mission the mission
	 * @return true if the mission is correctly formed
	 * @throws BadRequestException
	 */
	public boolean isThisMissionValid(Mission mission, boolean allowWE) throws BadRequestException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = mission.getStartDate();
		LocalDateTime endDate = mission.getEndDate();
		if (mission.getCollaborator() == null) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.NULL_COLLABORATOR,
					MissionErrors.NULL);
		}
		Collaborator  collaborator = this.collaboratorService.read(mission.getCollaborator().getId());
		if (mission.getNature() == null) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.NULL_NATURE,
					MissionErrors.NULL);
		}
		// nature, are mandatory
		Nature nature = natureService.read(mission.getNature().getId());
		// the mission s nature must be active at the date of start
		if (!natureService.isNatureActive(nature, startDate)) {
			throw new BadRequestException(MissionErrors.invalid.INACTIVE_NATURE, ErrorCodes.missionInvalid);
		}

		// the mission has a collaborator
		// dates not null, start before end, start after now
		if (startDate == null) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.START_CANT_BE,
					MissionErrors.NULL);
		}
		if (endDate == null) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.END_CANT_BE,
					MissionErrors.NULL);
		}
		if (startDate.isBefore(now)) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.START_CANT_BE,
					MissionErrors.BEFORE, MissionErrors.NOW);
		}
		if (endDate.isBefore(startDate)) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.END_CANT_BE,
					MissionErrors.BEFORE, MissionErrors.START);
		}

		// start and end not in WE
		DayOfWeek startDay = startDate.getDayOfWeek();
		DayOfWeek endDay = endDate.getDayOfWeek();
		if (!allowWE) {
			switch (startDay) {
			case SATURDAY:
			case SUNDAY:
				throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.START_CANT_BE,
						MissionErrors.ON, startDay.toString());
			default:
			}
			switch (endDay) {
			case SATURDAY:
			case SUNDAY:
				throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.END_CANT_BE,
						MissionErrors.ON, endDay.toString());
			default:
			}
		}
		// start and end cities are mandatory
		if (mission.getStartCity() == null) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.DEPARTURE,
					MissionErrors.invalid.NULL_CITY);
		}
		if (mission.getEndCity() == null) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.ARRIVAL,
					MissionErrors.invalid.NULL_CITY);
		}
		// if the transport is Flight, the mission must be created at least a week
		// before the start
		System.err.println(mission.getMissionTransport() == Transport.Flight);
		System.err.println(startDate.isBefore(now.plusDays(7)));
		if (mission.getMissionTransport() == Transport.Flight
				&& startDate.isBefore(now.plusDays(minDayBeforeFligthTransport))) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.invalid.AERIAL_TRANSPORT,
					String.valueOf(minDayBeforeFligthTransport), MissionErrors.FROM_TODAY);
		}

		// the mission can't be in the same time as another one
		// we get all mission that have an end date before our new misson start date
		// witch rule of every mission that would end after our start date
		List<Mission> missions = missionRepository.findByCollaboratorAndEndDateAfterAndStatusNotOrderByStartDate(
				collaborator, startDate, Status.REJECTED);
		int missionsCount = missions.size();
		boolean isCollaboratorAvailable = true;
		// in case of an update, avoid to compare to self
		// this part if for the case of an update and its to avoid to make it compare to
		// itself
		// (the list is empty OR ( the list as one AND its id is the id of our new
		// mission )
		if (!(missionsCount == 0 || (missionsCount == 1 && missions.get(0).getId() == mission.getId()))) {
			// we get the first one wich is the one rigth after our new mission start date
			// and we are ignoring all the one that can be before
			Mission nextMission = (missions.get(0).getId() == mission.getId()) ? missions.get(1) : missions.get(0);
			LocalDateTime nextMissionStartDate = nextMission.getStartDate();
			LocalDateTime nextMissionEndDate = nextMission.getEndDate();
			if (startDate.isAfter(nextMissionStartDate) && startDate.isBefore(nextMissionEndDate)) {
				throw new BadRequestException(MissionErrors.invalid.CANT_START_IN_NEXT_MISSION,
						ErrorCodes.missionInvalid);
			}
			if (endDate.isAfter(nextMissionStartDate) && endDate.isBefore(nextMissionEndDate)) {
				throw new BadRequestException(MissionErrors.invalid.CANT_END_IN_NEXT_MISSION,
						ErrorCodes.missionInvalid);
			}
		}
		return isCollaboratorAvailable;
	}

	@Override
	public List<Mission> list() {
		return missionRepository.findAll();
	}

	@Override
	public List<Mission> missionsToPutInWaitingValidation() {
		return missionRepository.findByStatus(Status.INIT);
	}

	@Override
	public List<Mission> missionsToValidate(int idManager) throws BadRequestException {
		Collaborator manager = collaboratorService.read(idManager);
		List<Mission> missionsToValidate = new ArrayList<>();
		manager.getTeam().forEach(collaborator -> missionsToValidate
				.addAll(missionRepository.findByCollaboratorAndStatusNot(collaborator, Status.INIT)));
		return missionsToValidate;
	}

	@Override
	public Mission read(int id) throws BadRequestException {
		return this.missionRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(MissionErrors.read.NOT_FOUND, ErrorCodes.missionNotFound));
	}

	@Override
	public Mission update(int id, Mission mission, boolean allowWE) throws BadRequestException {
		Mission current = read(mission.getId());
		// ckecks of ID's
		if (id != current.getId()) {
			throw new BadRequestException(MissionErrors.INCONSISTENT_ID, ErrorCodes.idInconsistent);
		}
		// check database datas status
		canBeUpdated(current);
		// check new datas integrity
		isThisMissionValid(mission, allowWE);
		// because its supposed to be the result of a calculus
		// current.setBonus(mission.getBonus());// Il would not have touched the bonus
		current.setNature(natureService.read(mission.getNature().getId()));
		current.setStartDate(mission.getStartDate());
		current.setEndDate(mission.getEndDate());
		current.setMissionTransport(mission.getMissionTransport());
		current.setStartCity(mission.getStartCity());
		current.setEndCity(mission.getEndCity());
		current.setNature(mission.getNature());
		// a modified mission will ALWAYS get its status to init
		current.setStatus(Status.INIT);
		return this.missionRepository.save(current);
	}

	@Override
	public Mission updateStatus(int id, Status status) throws BadRequestException {
		Mission mission = read(id);
		mission.setStatus(status);
		return missionRepository.save(mission);
	}
	
	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 * 
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission validateMission(int id) throws BadRequestException {
		Mission mission = this.read(id);
		switch (mission.getStatus()) {
		
		case INIT:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_INIT);			
		case REJECTED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_REJECTED);			
		case ENDED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_ENDED);			
		case VALIDATED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_VALIDATED);
		case WAITING_VALIDATION:
		default:
			mission.setStatus(Status.VALIDATED);
			return missionRepository.save(mission);
		}
		
	}
	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 * 
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission RejectMission(int id) throws BadRequestException {
		Mission mission = this.read(id);
		switch (mission.getStatus()) {
		
		case INIT:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_INIT);			
		case REJECTED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_REJECTED);			
		case ENDED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_ENDED);			
		case VALIDATED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_VALIDATED);
		case WAITING_VALIDATION:
		default:
			mission.setStatus(Status.REJECTED);
			return missionRepository.save(mission);
		}
		
	}
	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 * 
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission NightComputing(int id) throws BadRequestException {
		Mission mission = this.read(id);
		switch (mission.getStatus()) {		
		case REJECTED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_REJECTED);			
		case ENDED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_ENDED);			
		case VALIDATED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_VALIDATED);
		case WAITING_VALIDATION:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_WAITING_VALIDATION);
		case INIT:			
		default:
			mission.setStatus(Status.WAITING_VALIDATION);
			return missionRepository.save(mission);
		}
		
	}
	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 * 
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission resetMission(int id) throws BadRequestException {
		Mission mission = this.read(id);
		switch (mission.getStatus()) {
		case INIT:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_INIT);			
		case WAITING_VALIDATION:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_WAITING_VALIDATION);			
		case ENDED:
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.IS_ENDED);			
		case VALIDATED:
		case REJECTED:
		default:
			mission.setStatus(Status.INIT);
			return missionRepository.save(mission);
		}
		
	}
}
