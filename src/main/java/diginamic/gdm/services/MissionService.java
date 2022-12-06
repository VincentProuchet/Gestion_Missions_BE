package diginamic.gdm.services;


import java.util.List;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
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
	 * @throws Exception
	 */
	void delete(int id) throws BadRequestException, Exception;



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

	/**
	 * Check if the mission has been completed
	 *
	 * @param id mission id
	 * @return true if completed
	 * @throws Exception
	 */
	public boolean isMissionDone(int id) throws Exception;
	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 *
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission RejectMission(int id) throws BadRequestException;

	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 *
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission NightComputing(int id) throws BadRequestException ;

	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 *
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	public Mission resetMission(int id) throws BadRequestException ;

	/**
	 * First draw  of a mission's status manipulator
	 * the idea is to secure status by limiting possibilities
	 *  to directly manipulate the data
	 *
	 * @param id
	 * @return the updated mission
	 * @throws BadRequestException
	 */
	Mission validateMission(int id) throws BadRequestException;

}
