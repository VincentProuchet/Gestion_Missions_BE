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

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
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
    public Mission create(Mission mission, boolean allowWE) throws BadRequestException {

        mission.setId(0);

        if (!isThisMissionValid(mission, allowWE)) {
            throw new BadRequestException("This mission does not have all the required data, or the dates are not allowed (WE or collaborator already in mission) ", ErrorCodes.missionInvalid);
        }

        mission.setStatus(Status.INIT);

        return this.missionRepository.save(mission);
    }

    @Override
    public Mission read(int id) throws BadRequestException {
        return this.missionRepository.findById(id).orElseThrow(() -> new BadRequestException("Mission id not found", ErrorCodes.missionNotFound));
    }

    @Override
    public Mission update(int id, Mission mission, boolean allowWE) throws BadRequestException {

        //throws exception
        if (id != mission.getId()) {
            throw new BadRequestException("The id is inconsistent with the given mission", ErrorCodes.idInconsistent);
        }
        if (!isThisMissionValid(mission, allowWE)) {
            throw new BadRequestException("This mission does not have all the required data, or the dates are not allowed (WE or collaborator already in mission) ", ErrorCodes.missionInvalid);
        }
        if (!canBeUpdated(mission)) {
            throw new BadRequestException("This mission can't be updated : make sure its status is INIT or REJECTED, or consider create it if it does not exist", ErrorCodes.missionInvalid);
        }

        Mission current = read(id);
        current.setBonus(mission.getBonus());
        current.setMissionTransport(mission.getMissionTransport());
        current.setStartDate(mission.getStartDate());
        current.setEndDate(mission.getEndDate());
        current.setStartCity(mission.getStartCity());
        current.setEndCity(mission.getEndCity());
        current.setNature(mission.getNature());
        current.setStatus(Status.INIT);
        Mission updatedMission = this.missionRepository.save(current);
        return updatedMission;
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
    public boolean isThisMissionValid(Mission mission, boolean allowWE) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = mission.getStartDate();
        LocalDateTime endDate = mission.getEndDate();
        // the mission has a collaborator
        // TODO change this for exceptions !!!!
        boolean hasCollaborator = mission.getCollaborator() != null;
        Collaborator collaborator = collaboratorRepository.findById(mission.getCollaborator().getId()).get();

        // dates not null, start before end, start after now
        boolean areDatesValid = ((startDate != null) && (endDate != null) && endDate.isAfter(startDate) && startDate.isAfter(now));

        // start and end not in WE
        DayOfWeek startDay = startDate.getDayOfWeek();
        DayOfWeek endDay = endDate.getDayOfWeek();
        boolean areDatesNotInWE = allowWE || (startDay != DayOfWeek.SATURDAY && startDay != DayOfWeek.SUNDAY && endDay != DayOfWeek.SATURDAY && endDay != DayOfWeek.SUNDAY);

        // nature, start and end cities are mandatory
        // TODO change this with exceptions, or else there will be NULLPOINTEREXCEPTIONS !!!
        boolean requiredDataIsPresent = mission.getNature() != null;
        Optional<Nature> natureOptional = natureRepository.findById(mission.getNature().getId());
        requiredDataIsPresent = requiredDataIsPresent && mission.getStartCity() != null && mission.getEndCity() != null;
        Nature nature = natureOptional.get();

        // if the transport is Flight, the mission must be created at least a week before the start
        boolean flightAdvanceNotice = mission.getMissionTransport() != Transport.Flight || startDate.isAfter(now.plusDays(7));

        // the mission s nature must be active at the date of start
        boolean isNatureActive = natureService.isNatureActive(nature, startDate);

        // the mission cant be in the same time as another one
        List<Mission> missions = missionRepository.findByCollaboratorAndEndDateAfterOrderByStartDate(collaborator, startDate);
        int missionsCount = missions.size();
        boolean isCollaboratorAvailable = true;
        // in case of an update, avoid to compare to self
        if (!(missionsCount == 0 || (missionsCount == 1 && missions.get(0).getId() == mission.getId()))) {
            Mission nextMission = (missions.get(0).getId() == mission.getId()) ? missions.get(1) : missions.get(0);
            LocalDateTime nextMissionStartDate = nextMission.getStartDate();
            isCollaboratorAvailable = startDate.isBefore(nextMissionStartDate) && endDate.isBefore(nextMissionStartDate);
        }
        return areDatesValid && requiredDataIsPresent && areDatesNotInWE && flightAdvanceNotice && isNatureActive && hasCollaborator && isCollaboratorAvailable;
    }

    @Override
    public boolean canBeUpdated(Mission mission) {
        Status status = mission.getStatus();
        return (status == Status.INIT || status == Status.REJECTED) && missionRepository.findById(mission.getId()).isPresent();
    }

    @Override
    public boolean canBeDeleted(Mission mission) {
//        Status status = mission.getStatus();
//        return status == Status.INIT || status == Status.REJECTED;
        return true;
    }

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
        manager.getTeam().stream().forEach(collaborator -> {
            missionsToValidate.addAll(missionRepository.findByCollaboratorAndStatus(collaborator, Status.WAITING_VALIDATION));
        });
        return missionsToValidate;
    }

    @Override
    public List<Mission> missionsToPutInWaitingValidation() {
        return missionRepository.findByStatus(Status.INIT);
    }

    @Override
    public List<Mission> completedMissionsToCompute() {
        return missionRepository.findByStatusAndEndDateBeforeAndHasBonusBeenEvaluatedFalse(Status.VALIDATED, LocalDateTime.now());
    }

    @Override
    public List<Mission> completedMissions() {
        return missionRepository.findByStatusAndEndDateBefore(Status.VALIDATED, LocalDateTime.now());
    }

}
