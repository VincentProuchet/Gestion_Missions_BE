package diginamic.gdm.services;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;

public interface ScheduledTasksService {

    /**
     * Compute the bonus for completed missions
     * workedDays*tjm*%bonus/100, data from the nature active at the start date of the mission
     */
    void computeBonusForCompletedMissions();

    /**
     * Change the status INIT of missions to WAITING_VALIDATION
     * set the ended status for just ended missions
     */
    void changeMissionStatus() throws BadRequestException;


    /**
     * Return the number of days between start and end, excluding the WEs
     *
     * @param mission to evaluate
     * @return a number of days
     */
    long workedDays(Mission mission);

    //TODO send a mail to the manager
}
