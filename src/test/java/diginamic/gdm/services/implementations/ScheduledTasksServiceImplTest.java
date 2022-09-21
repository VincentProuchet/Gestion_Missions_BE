package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.ScheduledTasksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests use the data created by InitDataDB
 */
@SpringBootTest
class ScheduledTasksServiceImplTest {

    @Autowired
    private MissionService missionService;
    @Autowired
    private ScheduledTasksService scheduledTasksService;

    @Test
    void workedDays() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 21, 1,1);
        Mission fakeMission = new Mission();
        fakeMission.setStartDate(now);
        fakeMission.setEndDate(now.plusDays(7));
        assertEquals(this.scheduledTasksService.workedDays(fakeMission), 5);
        fakeMission.setEndDate(now.plusDays(8));
        assertEquals(this.scheduledTasksService.workedDays(fakeMission), 6);
        fakeMission.setEndDate(now.plusDays(12));
        assertEquals(this.scheduledTasksService.workedDays(fakeMission), 8);
    }
    @Test
    void computeBonusForCompletedMissions() throws BadRequestException {
        List<Mission> missionsWithBonusesToCompute = missionService.completedMissionsToCompute();
        assertEquals(missionsWithBonusesToCompute.size(), 2);

        Mission mission1 = missionsWithBonusesToCompute.get(0);
        Nature nature1 = mission1.getNature();
        BigDecimal bonusM1 = BigDecimal.valueOf(this.scheduledTasksService.workedDays(mission1) * nature1.getBonusPercentage() / 100).multiply(nature1.getTjm());

        this.scheduledTasksService.computeBonusForCompletedMissions();

        assertEquals(bonusM1.compareTo(missionService.read(mission1.getId()).getBonus()), 0);

        Mission mission2 = missionsWithBonusesToCompute.get(1);
        Nature nature2 = mission2.getNature();
        BigDecimal bonusM2 = BigDecimal.valueOf(this.scheduledTasksService.workedDays(mission2) * nature2.getBonusPercentage() / 100).multiply(nature2.getTjm());

        assertEquals(bonusM2.compareTo(missionService.read(mission2.getId()).getBonus()), 0);
    }

    @Test
    void changeMissionStatus() throws BadRequestException {
        assertEquals(missionService.missionsToPutInWaitingValidation().size(), 2);
        scheduledTasksService.changeMissionStatus();
        assertEquals(missionService.missionsToPutInWaitingValidation().size(), 0);
    }

}