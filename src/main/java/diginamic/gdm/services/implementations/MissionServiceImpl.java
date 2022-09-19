package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.Manager;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.repository.ManagerRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private NatureService natureService;
    private ManagerRepository managerRepository;

    @Override
    public List<Mission> list() {
        return missionRepository.findAll();
    }

    @Override
    public boolean create(Mission mission, boolean allowWE) {

        mission.setId(0);

        if (!isThisMissionValid(mission, allowWE)) {
            return false;
        }

        mission.setStatus(Status.INIT);

        this.missionRepository.save(mission);

        return true;
    }

    @Override
    public Mission read(int id) {
        return this.missionRepository.findById(id).orElseThrow();
    }

    @Override
    public Mission update(int id, Mission mission, boolean allowWE) {

        //throws exception
        if (id != mission.getId()) {
            return null;
        }
        if (!isThisMissionValid(mission, allowWE)) {
            return null;
        }
        if (!canBeUpdated(mission)) {
            return null;
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
    public void delete(int id) {
        Mission mission = read(id);
        this.missionRepository.delete(mission);
    }

    @Override
    public void updateStatus(int id, Status status) {
        Mission mission = read(id);
        mission.setStatus(status);
        missionRepository.save(mission);
    }

    @Override
    public boolean isThisMissionValid(Mission mission, boolean allowWE) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = mission.getStartDate();
        LocalDateTime endDate = mission.getEndDate();
        // the mission has a collaborator
        boolean hasCollaborator = mission.getCollaborator() != null;

        // dates not null, start before end, start after now
        boolean areDatesValid = ((startDate != null) && (endDate != null) && endDate.isAfter(startDate) && startDate.isAfter(now));

        // start and end not in WE
        DayOfWeek startDay = startDate.getDayOfWeek();
        DayOfWeek endDay = endDate.getDayOfWeek();
        boolean areDatesNotInWE = allowWE || (startDay != DayOfWeek.SATURDAY && startDay != DayOfWeek.SUNDAY && endDay != DayOfWeek.SATURDAY && endDay != DayOfWeek.SUNDAY);

        // nature, start and end cities are mandatory
        boolean requiredDataIsPresent = mission.getNature() != null && mission.getStartCity() != null && mission.getEndCity() != null;

        // if the transport is Flight, the mission must be created at least a week before the start
        boolean flightAdvanceNotice = mission.getMissionTransport() != Transport.Flight || startDate.isAfter(now.plusDays(7));

        // the mission s nature must be active at the date of start
        boolean isNatureActive = natureService.isNatureActive(mission.getNature(), startDate);

        // the mission cant be in the same time as another one
        List<Mission> missions = missionRepository.findByCollaboratorAndEndDateAfterOrderByStartDate(mission.getCollaborator(), startDate);
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
        return status == Status.INIT || status == Status.REJECTED;
    }

    @Override
    public boolean canBeDeleted(Mission mission) {
//        Status status = mission.getStatus();
//        return status == Status.INIT || status == Status.REJECTED;
        return true;
    }

    @Override
    public boolean isMissionDone(int id) {
        Mission mission = missionRepository.findById(id).get();
        return mission.getStatus() == Status.VALIDATED && mission.getEndDate().isBefore(LocalDateTime.now());
    }

    @Override
    public List<Mission> missionsToValidate(int idManager) {
        Manager manager = managerRepository.findById(idManager).orElseThrow();
        List<Mission> missionsToValidate = new ArrayList<>();
        manager.getTeam().stream().forEach(collaborator -> {
            missionsToValidate.addAll(missionRepository.findByCollaboratorAndStatus(collaborator, Status.WAITING_VALIDATION));
        });
        return missionsToValidate;
    }

}
