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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Transactional
public class ScheduledTasksServiceImpl implements ScheduledTasksService {

    private MissionService missionService;
    private CollaboratorService collaboratorService;
    private NatureService natureService;

    @Override
    public void computeBonusForCompletedMissions() {

        //compute bonuses for completed missions : workedDays*tjm*%bonus/100
        List<Mission> completedMissions = missionService.completedMissions();

        for (Mission mission : completedMissions) {

            Nature nature = mission.getNature();

            BigDecimal bonus = new BigDecimal(workedDays(mission.getStartDate(), mission.getEndDate()) * nature.getBonusPercentage() / 100).multiply(nature.getTjm());

            mission.setBonus(bonus);

        }
        //send a mail to the manager
    }

    @Override
    public void changeMissionStatus() throws BadRequestException {
        //update missions statuses INIT becomes WAITING_VALIDATION
        List<Mission> missionsWithStatusInit = missionService.missionsToPutInWaitingValidation();
        for (Mission mission : missionsWithStatusInit) {
            mission.setStatus(Status.WAITING_VALIDATION);
            missionService.update(mission.getId(), mission);
        }
        //maybe VALIDATED and date passed becomes DONE?

    }

    /**
     * Return the number of days between start and end, excluding the WEs
     *
     * @param start must not be a Saturday or a Sunday
     * @param end   idem
     * @return
     */
    private long workedDays(LocalDateTime start, LocalDateTime end) {

        long totalDays = start.until(end, ChronoUnit.DAYS);
        long weekendDays = (totalDays / 7) * 2;

        if (start.getDayOfWeek().getValue() > end.getDayOfWeek().getValue()) {
            weekendDays += 2;
        }

        return weekendDays;
    }
}
