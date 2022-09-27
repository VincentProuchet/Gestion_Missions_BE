package diginamic.gdm.services.implementations;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link MissionService}.
 *
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {

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

	@Override
	public boolean canBeDeleted(Mission mission) throws BadRequestException {
		switch (mission.getStatus()) {
		case INIT:
		case REJECTED:
			return true;
		case ENDED:
		case VALIDATED:
		case WAITING_VALIDATION:
			throw new BadRequestException("The mission as " + mission.getStatus() + " and can't be deleted",
					ErrorCodes.missionInvalid);
		// any case that is not in the book is considered to allow deletion
		default:
			return true;
		}
	}

	@Override
	public boolean canBeUpdated(Mission mission) throws BadRequestException {
		Mission missionIDB = read(mission.getId());
		Status status = missionIDB.getStatus();
		switch (status) {
		case ENDED:
			throw new BadRequestException("la mission est terminée ", ErrorCodes.missionInvalid);
		case WAITING_VALIDATION:
			throw new BadRequestException("la mission est en attente de validation et ne peux être modifiée ",
					ErrorCodes.missionInvalid);
		case VALIDATED:
			throw new BadRequestException("la mission est Validée et ne peux être modifiée ",
					ErrorCodes.missionInvalid);
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

		if (!isThisMissionValid(mission, allowWE)) {
			throw new BadRequestException(
					"This mission does not have all the required data, or the dates are not allowed (WE or collaborator already in mission) ",
					ErrorCodes.missionInvalid);
		}

		mission.setStatus(Status.INIT);

		return this.missionRepository.save(mission);
	}

	@Override
	public void delete(int id) throws BadRequestException {
		Mission mission = read(id);
		this.missionRepository.delete(mission);
	}

	@Override
	public List<Mission> getMissionsOfCollaborator(Collaborator collaborator) {
		return missionRepository.findByCollaborator(collaborator);
	}

	/**
	 * needs refactoring, since it will do uneccessary database queries
	 * 
	 * @throws BadRequestException
	 */
	@Override
	public boolean isMissionDone(int id) throws Exception {
		Mission mission = read(id);

		return mission.getStatus() == Status.VALIDATED && mission.getEndDate().isBefore(LocalDateTime.now());
	}

	@Override
	public boolean isThisMissionValid(Mission mission, boolean allowWE) throws BadRequestException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = mission.getStartDate();
		LocalDateTime endDate = mission.getEndDate();
		// TODO change this for exceptions !!!!
		// the mission has a collaborator
		Collaborator collaborator = collaboratorService.read(mission.getCollaborator().getId());

		// dates not null, start before end, start after now
		if (startDate == null) {
			throw new BadRequestException("Missions start date is null ", ErrorCodes.missionInvalid);
		}
		if (endDate == null) {
			throw new BadRequestException("Missions end date is null ", ErrorCodes.missionInvalid);
		}
		if (startDate.isBefore(now)) {
			throw new BadRequestException("Missions can't start in the past ", ErrorCodes.missionInvalid);
		}
		if (endDate.isBefore(startDate)) {
			throw new BadRequestException("Missions can't start after its end ", ErrorCodes.missionInvalid);
		}

		// start and end not in WE
		DayOfWeek startDay = startDate.getDayOfWeek();
		DayOfWeek endDay = endDate.getDayOfWeek();
		if (!allowWE) {
			if (startDay == DayOfWeek.SATURDAY) {
				throw new BadRequestException("Missions can't start on Saturday ", ErrorCodes.missionInvalid);
			}
			if (startDay == DayOfWeek.SUNDAY) {
				throw new BadRequestException("Missions can't start on  Sunday ", ErrorCodes.missionInvalid);
			}
			if (endDay == DayOfWeek.SATURDAY) {
				throw new BadRequestException("Missions can't end on Saturday  ", ErrorCodes.missionInvalid);
			}
			if (endDay == DayOfWeek.SUNDAY) {
				throw new BadRequestException("Missions can't end on  Sunday ", ErrorCodes.missionInvalid);
			}
		}

		// nature, start and end cities are mandatory
		// TODO change this with exceptions, or else there will be NULLPOINTEREXCEPTIONS
		// !!!

		Nature nature = natureService.read(mission.getNature().getId());
		if (mission.getStartCity() == null) {
			throw new BadRequestException("start city invallid ", ErrorCodes.missionInvalid);
		}
		if (mission.getEndCity() == null) {
			throw new BadRequestException("arrival city or invallid ", ErrorCodes.missionInvalid);
		}

		// if the transport is Flight, the mission must be created at least a week
		// before the start
		System.err.println(mission.getMissionTransport() == Transport.Flight);
		System.err.println(startDate.isBefore(now.plusDays(7)));
		if (mission.getMissionTransport() == Transport.Flight && startDate.isBefore(now.plusDays(7))) {

			new BadRequestException("with aerial transport start day must be now + 7 days", ErrorCodes.missionInvalid);
		}

		// the mission s nature must be active at the date of start
		if (!natureService.isNatureActive(nature, startDate)) {
			throw new BadRequestException("nature is not active ", ErrorCodes.missionInvalid);
		}

		// the mission cant be in the same time as another one
		List<Mission> missions = missionRepository.findByCollaboratorAndEndDateAfterAndStatusNotOrderByStartDate(
				collaborator, startDate, Status.REJECTED);
		// on ne prend que la mission qui vient après la date de la mission courante
		int missionsCount = missions.size();
		boolean isCollaboratorAvailable = true;
		// in case of an update, avoid to compare to self
		if (!(missionsCount == 0 || (missionsCount == 1 && missions.get(0).getId() == mission.getId()))) {

			Mission nextMission = (missions.get(0).getId() == mission.getId()) ? missions.get(1) : missions.get(0);
			LocalDateTime nextMissionStartDate = nextMission.getStartDate();
			if (startDate.isAfter(nextMissionStartDate)) {
				throw new BadRequestException(" la mission commence après le début de la mission suivante",
						ErrorCodes.missionInvalid);
			}
			if (endDate.isAfter(nextMissionStartDate)) {
				throw new BadRequestException("la mission se termine après le début le la mission suivante",
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
				.orElseThrow(() -> new BadRequestException("Mission id not found", ErrorCodes.missionNotFound));
	}

	@Override
	public Mission update(int id, Mission mission, boolean allowWE) throws BadRequestException {
		Mission current = read(mission.getId());
		// ckecks of ID's
		if (id != current.getId()) {
			throw new BadRequestException("The id is inconsistent with the given mission", ErrorCodes.idInconsistent);
		}
		// check new datas integrity
		if (!isThisMissionValid(mission, allowWE)) {
			throw new BadRequestException(
					"This mission does not have all the required data, or the dates are not allowed (WE or collaborator already in mission) ",
					ErrorCodes.missionInvalid);
		}
		// check database datas sttatus 		
		if (!canBeUpdated(current)) {
			throw new BadRequestException(
					"This mission can't be updated : make sure its status is INIT or REJECTED, or consider create it if it does not exist",
					ErrorCodes.missionInvalid);
		}
		// because its suposed to be the result of a calculus
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
