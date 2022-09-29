package diginamic.gdm.services.implementations;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diginamic.gdm.GDMVars;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.errors.MissionServiceErrors;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
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


	/** delay to respect when selecting flight as transport  */
	
	private static final int minDayBeforeFligthTransport = GDMVars.MIN_DAYS_BEFORE_FLIGHT_TRANSPORT;
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
			throw new BadRequestException(MissionServiceErrors.delete.STATUSERROR + mission.getStatus()
					+ MissionServiceErrors.delete.CANT_BE_DELETED, ErrorCodes.missionInvalid);
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
		Status status = missionIDB.getStatus();
		switch (status) {
		case ENDED:
			throw new BadRequestException(MissionServiceErrors.update.IS_ENDED, ErrorCodes.missionInvalid);
		case WAITING_VALIDATION:
			throw new BadRequestException(MissionServiceErrors.update.IS_WAITING_VALIDATION, ErrorCodes.missionInvalid);
		case VALIDATED:
			throw new BadRequestException(MissionServiceErrors.update.IS_VALIDATED, ErrorCodes.missionInvalid);
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
		String message = new StringBuilder(MissionServiceErrors.delete.STATUSERROR).append(mission.getStatus())
				.append(MissionServiceErrors.delete.CANT_BE_DELETED).toString();
		if (isMissionDone(mission)) {
			throw new BadRequestException(message, ErrorCodes.missionInvalid);
		}
		switch (mission.getStatus()) {
		case VALIDATED:
			throw new BadRequestException(message, ErrorCodes.missionInvalid);
		case ENDED:
			throw new BadRequestException(message, ErrorCodes.missionInvalid);
		default:
			// in all other case the mission is deleted
			this.missionRepository.delete(mission);
		}
	}

	@Override
	public List<Mission> getMissionsOfCollaborator(Collaborator collaborator) {
		return missionRepository.findByCollaborator(collaborator);
	}

	/**
	 * Check if the mission has been completed
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
	 * Check if the mission has been completed
	 *
	 * @param mission
	 * @return true if completed
	 * @throws Exception
	 */
	public boolean isMissionDone(Mission mission) throws Exception {
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
		// TODO change this for exceptions !!!!
		// the mission has a collaborator
		Collaborator collaborator = collaboratorService.read(mission.getCollaborator().getId());

		// dates not null, start before end, start after now
		if (startDate == null) {
			throw new BadRequestException(new StringBuilder(MissionServiceErrors.invalid.START_CANT_BE)
					.append(MissionServiceErrors.NULL).toString(), ErrorCodes.missionInvalid);
		}
		if (endDate == null) {
			throw new BadRequestException(new StringBuilder(MissionServiceErrors.invalid.END_CANT_BE)
					.append(MissionServiceErrors.NULL).toString(), ErrorCodes.missionInvalid);
		}
		if (startDate.isBefore(now)) {
			throw new BadRequestException(
					new StringBuilder(MissionServiceErrors.invalid.START_CANT_BE)
							.append(MissionServiceErrors.invalid.BEFORE).append(MissionServiceErrors.NOW).toString(),
					ErrorCodes.missionInvalid);
		}
		if (endDate.isBefore(startDate)) {
			throw new BadRequestException(
					new StringBuilder(MissionServiceErrors.invalid.END_CANT_BE)
							.append(MissionServiceErrors.invalid.BEFORE).append(MissionServiceErrors.START).toString(),
					ErrorCodes.missionInvalid);
		}

		// start and end not in WE
		DayOfWeek startDay = startDate.getDayOfWeek();
		DayOfWeek endDay = endDate.getDayOfWeek();
		if (!allowWE) {
			switch (startDay) {
			case SATURDAY:
			case SUNDAY:
				throw new BadRequestException(new StringBuilder(MissionServiceErrors.invalid.START_CANT_BE)
						.append(MissionServiceErrors.ON).append(startDay).toString(), ErrorCodes.missionInvalid);
			default:
			}
			switch (endDay) {
			case SATURDAY:
			case SUNDAY:
				throw new BadRequestException(new StringBuilder(MissionServiceErrors.invalid.END_CANT_BE)
						.append(MissionServiceErrors.ON).append(endDay).toString(), ErrorCodes.missionInvalid);
			default:
			}
		}

		// nature, start and end cities are mandatory
		Nature nature = natureService.read(mission.getNature().getId());
		if (mission.getStartCity() == null) {
			throw new BadRequestException(new StringBuilder(MissionServiceErrors.START)
					.append(MissionServiceErrors.invalid.CITY_CANT_BE)
					.append(MissionServiceErrors.NULL)
					.toString(), ErrorCodes.missionInvalid);			
		}
		if (mission.getEndCity() == null) {
			throw new BadRequestException(new StringBuilder(MissionServiceErrors.END)
					.append(MissionServiceErrors.invalid.CITY_CANT_BE)
					.append(MissionServiceErrors.NULL)
					.toString(), ErrorCodes.missionInvalid);
		}
		// if the transport is Flight, the mission must be created at least a week
		// before the start
		System.err.println(mission.getMissionTransport() == Transport.Flight);
		System.err.println(startDate.isBefore(now.plusDays(7)));
		if (mission.getMissionTransport() == Transport.Flight
				&& startDate.isBefore(now.plusDays(this.minDayBeforeFligthTransport))) {
			throw new BadRequestException(new StringBuilder(MissionServiceErrors.invalid.AERIAL_TRANSPORT )
					.append(this.minDayBeforeFligthTransport)
					.append(MissionServiceErrors.FROM_TODAY)
					.toString(), ErrorCodes.missionInvalid);
		}
		// the mission s nature must be active at the date of start
		if (!natureService.isNatureActive(nature, startDate)) {
			throw new BadRequestException(MissionServiceErrors.invalid.INACTIVE_NATURE, ErrorCodes.missionInvalid);
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
				throw new BadRequestException(MissionServiceErrors.invalid.CANT_START_IN_NEXT_MISSION,
						ErrorCodes.missionInvalid);
			}
			if (endDate.isAfter(nextMissionStartDate) && endDate.isBefore(nextMissionEndDate)) {
				throw new BadRequestException(MissionServiceErrors.invalid.CANT_END_IN_NEXT_MISSION,
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
				.orElseThrow(() -> new BadRequestException(MissionServiceErrors.read.NOTFOUND, ErrorCodes.missionNotFound));
	}

	@Override
	public Mission update(int id, Mission mission, boolean allowWE) throws BadRequestException {
		Mission current = read(mission.getId());
		// ckecks of ID's
		if (id != current.getId()) {
			throw new BadRequestException(MissionServiceErrors.update.INCONSISTENT_ID, ErrorCodes.idInconsistent);
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

}
