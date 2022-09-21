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
    void computeBonusForCompletedMissions() {
        /*List<Mission> missionsWithBonusesToCompute = missionService.completedMissionsToCompute();
        assertEquals(missionsWithBonusesToCompute.size(), 2);
        Mission mission1 = missionsWithBonusesToCompute.get(0);
        Nature nature1 = mission1.getNature();*/

    }

    @Test
    void changeMissionStatus() throws BadRequestException {
        assertEquals(missionService.missionsToPutInWaitingValidation().size(), 2);
        scheduledTasksService.changeMissionStatus();
        assertEquals(missionService.missionsToPutInWaitingValidation().size(), 0);


    }
}