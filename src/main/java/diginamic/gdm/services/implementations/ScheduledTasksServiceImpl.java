package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
import diginamic.gdm.services.ScheduledTasksService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ScheduledTasksServiceImpl implements ScheduledTasksService {

    private MissionService missionService;
    private CollaboratorService collaboratorService;
    private NatureService natureService;

    @Override
    public void computeBonusForCompletedMissions() {

        //compute bonuses for completed missions : workedDays*tjm*%bonus/100
        List<Mission> completedMissions = missionService.completedMissionsToCompute();

        for (Mission mission : completedMissions) {

            Nature nature = mission.getNature();

            float bonus = (workedDays(mission) * nature.getBonusPercentage() / 100) *nature.getTjm();
            
            mission.setBonus(bonus);
            mission.setHasBonusBeenEvaluated(true);

        }
        // send a mail to the manager
    }

    @Override
    public void changeMissionStatus() throws BadRequestException {
        //update missions statuses INIT becomes WAITING_VALIDATION
        List<Mission> missionsWithStatusInit = missionService.missionsToPutInWaitingValidation();
        for (Mission mission : missionsWithStatusInit) {
            missionService.NightComputing(mission.getId());
        }

    }

    @Override
    public long workedDays(Mission mission) {
        LocalDateTime start = mission.getStartDate();
        LocalDateTime end = mission.getEndDate();

        long totalDays = start.until(end, ChronoUnit.DAYS);
        long weekendDays = (totalDays / 7) * 2;

        if (start.getDayOfWeek().getValue() > end.getDayOfWeek().getValue()) {
            weekendDays += 2;
        }

        return totalDays - weekendDays;
    }
}
