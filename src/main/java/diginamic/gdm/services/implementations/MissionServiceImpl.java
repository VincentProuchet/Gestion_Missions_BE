package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.*;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for {@link MissionService}.
 *
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {

	CollaboratorRepository collaboratorRepository;
	/**
	 * The {@link MissionRepository} dependency.
	 */
	private MissionRepository missionRepository;
	private NatureService natureService;
	private NatureRepository natureRepository;
	private CollaboratorRepository managerRepository;

	@Override
	public List<Mission> list() {
		return missionRepository.findAll();
	}

	@Override
	public List<Mission> getMissionsOfCollaborator(Collaborator collaborator) {
		return missionRepository.findByCollaborator(collaborator);
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
	public Mission read(int id) throws BadRequestException {
		return this.missionRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Mission id not found", ErrorCodes.missionNotFound));
	}

	@Override
	public Mission update(int id, Mission mission, boolean allowWE) throws BadRequestException {

		Mission current = read(mission.getId());

		// throws exception
		if (id != current.getId()) {
			throw new BadRequestException("The id is inconsistent with the given mission", ErrorCodes.idInconsistent);
		}
		// current.setBonus(mission.getBonus());// Il would not have touched the bonus
		// because its suposed to be the result of a calculus
		current.setNature(natureService.read(mission.getNature().getId()));		
		current.setStartDate(mission.getStartDate());
		current.setEndDate(mission.getEndDate());
		current.setMissionTransport(mission.getMissionTransport());
		current.setStartCity(mission.getStartCity());
		current.setEndCity(mission.getEndCity());
		current.setNature(mission.getNature());
		current.setStatus(Status.INIT);
		
		
		if (!isThisMissionValid(current, allowWE)) {
			throw new BadRequestException(
					"This mission does not have all the required data, or the dates are not allowed (WE or collaborator already in mission) ",
					ErrorCodes.missionInvalid);
		}
		if (!canBeUpdated(current)) {
			throw new BadRequestException(
					"This mission can't be updated : make sure its status is INIT or REJECTED, or consider create it if it does not exist",
					ErrorCodes.missionInvalid);
		}

		return this.missionRepository.save(current);
	}

	@Override
	public void delete(int id) throws BadRequestException {
		Mission mission = read(id);
		this.missionRepository.delete(mission);
	}

	@Override
	public Mission updateStatus(int id, Status status) throws BadRequestException {
		Mission mission = read(id);
		mission.setStatus(status);
		return missionRepository.save(mission);
	}

	@Override
	public boolean isThisMissionValid(Mission mission, boolean allowWE) throws BadRequestException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = mission.getStartDate();
		LocalDateTime endDate = mission.getEndDate();
		// TODO change this for exceptions !!!!
		// the mission has a collaborator
		Collaborator collaborator = collaboratorRepository.findById(mission.getCollaborator().getId())
				.orElseThrow(() -> new BadRequestException("no collaborator or invallid ", ErrorCodes.missionInvalid));

		// dates not null, start before end, start after now

		if (startDate == null) {
			throw new BadRequestException("Missions start date is null ", ErrorCodes.missionInvalid);
		}
		if (endDate == null) {
			throw new BadRequestException("Missions end date is null ", ErrorCodes.missionInvalid);
		}
		if (!startDate.isAfter(now)) {
			throw new BadRequestException("Missions can't start in the past ", ErrorCodes.missionInvalid);
		}
		if (!endDate.isAfter(startDate)) {
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

		Nature nature = natureRepository.findById(mission.getNature().getId())
				.orElseThrow(() -> new BadRequestException("no nature or invallid ", ErrorCodes.missionInvalid));
		if (mission.getStartCity() == null) {
			throw new BadRequestException("start city invallid ", ErrorCodes.missionInvalid);
		}
		if (mission.getEndCity() == null) {
			throw new BadRequestException("arrival city or invallid ", ErrorCodes.missionInvalid);
		}

		// if the transport is Flight, the mission must be created at least a week
		// before the start
		
				if(mission.getMissionTransport() == Transport.Flight || !startDate.isAfter(now.plusDays(7))) {
					new BadRequestException("with aerial transport start day must be now + 7 days", ErrorCodes.missionInvalid);
				}

		// the mission s nature must be active at the date of start
		if(!natureService.isNatureActive(nature, startDate)) {
			throw	new BadRequestException("nature is invallid ", ErrorCodes.missionInvalid);
		}

		// the mission cant be in the same time as another one
		List<Mission> missions = missionRepository.findByCollaboratorAndEndDateAfterOrderByStartDate(collaborator,
				startDate);
		int missionsCount = missions.size();
		boolean isCollaboratorAvailable = true;
		// in case of an update, avoid to compare to self
		if (!(missionsCount == 0 || (missionsCount == 1 && missions.get(0).getId() == mission.getId()))) {
			
			Mission nextMission = (missions.get(0).getId() == mission.getId()) ? missions.get(1) : missions.get(0);
			LocalDateTime nextMissionStartDate = nextMission.getStartDate();
			if(!startDate.isBefore(nextMissionStartDate)) {
				throw new BadRequestException(" la mission commence après le début de la mission suivante", ErrorCodes.missionInvalid);
			}
			if(!endDate.isBefore(nextMissionStartDate)) {
				throw new BadRequestException("la mission se termine après le début le la mission suivante", ErrorCodes.missionInvalid);
			}
		}
		return   isCollaboratorAvailable;
	}

	@Override
	public boolean canBeUpdated(Mission mission) {
		Status status = mission.getStatus();
		return (status == Status.INIT || status == Status.REJECTED)
				&& missionRepository.findById(mission.getId()).isPresent();
	}

	@Override
	public boolean canBeDeleted(Mission mission) {
//        Status status = mission.getStatus();
//        return status == Status.INIT || status == Status.REJECTED;
		return true;
	}

	/**
	 * needs refactoring, since it will do uneccessary database queries
	 */
	@Override
	public boolean isMissionDone(int id) {
		Optional<Mission> optionalMission = missionRepository.findById(id);
		if (optionalMission.isEmpty()) {
			return false;
		}
		Mission mission = optionalMission.get();
		return mission.getStatus() == Status.VALIDATED && mission.getEndDate().isBefore(LocalDateTime.now());
	}

	
    @Override
    public List<Mission> missionsToValidate(int idManager) throws BadRequestException {
        Collaborator manager = managerRepository.findById(idManager).orElseThrow(() -> new BadRequestException("Manager not found", ErrorCodes.managerNotFound));
        List<Mission> missionsToValidate = new ArrayList<>();
        manager.getTeam().forEach(collaborator -> missionsToValidate.addAll(missionRepository.findByCollaboratorAndStatusNot(collaborator, Status.INIT)));
        return missionsToValidate;
    }

	@Override
	public List<Mission> missionsToPutInWaitingValidation() {
		return missionRepository.findByStatus(Status.INIT);
	}

	@Override
	public List<Mission> completedMissionsToCompute() {
		return missionRepository.findByStatusAndEndDateBeforeAndHasBonusBeenEvaluatedFalse(Status.VALIDATED,
				LocalDateTime.now());
	}

	@Override
	public List<Mission> completedMissions() {
		return missionRepository.findByStatusAndEndDateBefore(Status.VALIDATED, LocalDateTime.now());
	}

}
