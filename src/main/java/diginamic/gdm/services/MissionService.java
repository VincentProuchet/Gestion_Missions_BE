package diginamic.gdm.services;


import java.util.List;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Status;
import diginamic.gdm.exceptions.BadRequestException;

/**
 * Interface to be implemented by a mission service class.
 * 
 * @author DorianBoel
 */
public interface MissionService {
	
	/**
	 * Gets the full list of registered missions.
	 * For test and debug purposes
	 * 
	 * @return A list of all missions
	 */
	List<Mission> list();

	/**
	 * Get all the missions of a given collaborator
	 *
	 * @param collaborator the collaborator
	 * @return the list of missions
	 */
	List<Mission> getMissionsOfCollaborator(Collaborator collaborator);
	
	/**
	 * Saves a new {@link Mission} instance.
	 *
	 * @param mission The new mission to be registered
	 * @return the created mission in DB
	 */
	default Mission create(Mission mission) throws BadRequestException {
		return create(mission, false);
	}


	/**
	 * Saves a new {@link Mission} instance.
	 *
	 * @param allowWE allow to work in WE
	 * @param mission The new mission to be registered
	 * @return true if the mission has been created, false otherwise
	 */
	Mission create(Mission mission, boolean allowWE) throws BadRequestException;

	/**
	 * Gets a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to get
	 * @return The registered mission corresponding to the given id
	 */
	Mission read(int id) throws BadRequestException;
	
	/**
	 * Updates the data for a specific registered mission.
	 * Does not allow to add or remove expenses, use the appropriate service for this
	 *
	 * @param allowWE allow to work in WE
	 * @param id The id corresponding to the mission to update
	 * @param mission The mission to update with modified info
	 * @return The resulting mission with updated info, null if not possible (replace with exceptions)
	 */
	Mission update(int id, Mission mission, boolean allowWE) throws BadRequestException;

	/**
	 * Updates the data for a specific registered mission.
	 * Does not allow to add or remove expenses, use the appropriate service for this
	 *
	 * @param id The id corresponding to the mission to update
	 * @param mission The mission to update with modified info
	 * @return The resulting mission with updated info
	 */
	default Mission update(int id, Mission mission) throws BadRequestException {
		return update(id, mission, false);
	}
	
	/**
	 * Deletes a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to delete
	 */
	void delete(int id) throws BadRequestException;

	/**
	 * Updates the current status for a specific mission.
	 *
	 * @param id     The id corresponding to the mission whose status to update
	 * @param status The new status to be applied to the mission
	 * @return the updated mission in DB
	 */
	Mission updateStatus(int id, Status status) throws BadRequestException;

	/**
	 * Check the validity of the mission request
	 *
	 * @param mission the mission
	 * @return true if the mission is correctly formed
	 * @throws Exception 
	 */
	default boolean isThisMissionValid(Mission mission) throws Exception {
		return isThisMissionValid(mission, false);
	}

	/**
	 * Check the validity of the mission request, allow a date in WE
	 *
	 * @param allowWE allow to work in WE
	 * @param mission the mission
	 * @return true if the mission is correctly formed
	 * @throws BadRequestException 
	 */
	boolean isThisMissionValid(Mission mission, boolean allowWE) throws Exception;

	/**
	 * Check if the mission status is INIT or REJECTED
	 *
	 * @param mission the mission
	 * @return true if the status allows the update
	 * @throws Exception 
	 */
	boolean canBeUpdated(Mission mission) throws Exception;

	/**
	 * returns true, but check if the mission status is INIT or REJECTED
	 * @param mission the mission
	 * @return true if deleted
	 * @throws Exception 
	 */
	boolean canBeDeleted(Mission mission) throws Exception;

	/**
	 * Check if the mission has been completed
	 *
	 * @param id mission id
	 * @return true if completed
	 * @throws Exception 
	 */
	boolean isMissionDone(int id) throws Exception;

	/**
	 * Get the list of missions to validate of the team of a given manager
	 * @param idManager the manager
	 * @return the list of missions with status WAITING_VALIDATION
	 */
	List<Mission> missionsToValidate(int idManager) throws BadRequestException;

	/**
	 * Get all the missions with INIT status
	 *
	 * @return the list of missions with status INIT
	 */
	List<Mission> missionsToPutInWaitingValidation();

	/**
	 * Get all completed missions which bonus has not been set, ie with status validated and end date passed and hasBonusBeenEvaluated to false
	 *
	 * @return the list of missions with status VALIDATED and end date passed and hasBonusBeenEvaluated to false
	 */
	List<Mission> completedMissionsToCompute();

	/**
	 * Get all completed missions , ie with status validated and end date passed
	 *
	 * @return the list of missions with status VALIDATED and end date passed
	 */
	List<Mission> completedMissions();
}
