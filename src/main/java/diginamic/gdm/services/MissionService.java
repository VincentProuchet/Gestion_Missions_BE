package diginamic.gdm.services;


import java.util.List;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Status;

/**
 * Interface to be implemented by a mission service class.
 * 
 * @author DorianBoel
 */
public interface MissionService {
	
	/**
	 * Gets the full list of registered missions.
	 * 
	 * @return A list of all missions
	 */
	List<Mission> list();
	
	/**
	 * Saves a new {@link Mission} instance.
	 *
	 * @param mission The new mission to be registered
	 * @return
	 */
	default boolean create(Mission mission) {
		return create(mission, false);
	}


	/**
	 * Saves a new {@link Mission} instance.
	 *
	 * @param allowWE allow to work in WE
	 * @param mission The new mission to be registered
	 * @return true if the mission has been created, false otherwise
	 */
	boolean create(Mission mission, boolean allowWE);

	/**
	 * Gets a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to get
	 * @return The registered mission corresponding to the given id
	 */
	Mission read(int id);
	
	/**
	 * Updates the data for a specific registered mission.
	 *
	 * @param allowWE allow to work in WE
	 * @param id The id corresponding to the mission to update
	 * @param mission The mission to update with modified info
	 * @return The resulting mission with updated info, null if not possible (replace with exceptions)
	 */
	Mission update(int id, Mission mission, boolean allowWE);

	/**
	 * Updates the data for a specific registered mission.
	 *
	 * @param id The id corresponding to the mission to update
	 * @param mission The mission to update with modified info
	 * @return The resulting mission with updated info
	 */
	default Mission update(int id, Mission mission) {
		return update(id, mission, false);
	}
	
	/**
	 * Deletes a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to delete
	 */
	void delete(int id);

	/**
	 * Updates the current status for a specific mission.
	 * 
	 * @param id The id corresponding to the mission whose status to update
	 * @param status The new status to be applied to the mission
	 */
	void updateStatus(int id, Status status);

	/**
	 * Check the validity of the mission request
	 *
	 * @param mission
	 * @return true if the mission is correctly formed
	 */
	default boolean isThisMissionValid(Mission mission) {
		return isThisMissionValid(mission, false);
	}

	/**
	 * Check the validity of the mission request, allow a date in WE
	 *
	 * @param allowWE allow to work in WE
	 * @param mission
	 * @return true if the mission is correctly formed
	 */
	boolean isThisMissionValid(Mission mission, boolean allowWE);

	/**
	 * Check if the mission status is INIT or REJECTED
	 *
	 * @param mission
	 * @return true if the status allows the update
	 */
	boolean canBeUpdated(Mission mission);

	/**
	 * returns true, but check if the mission status is INIT or REJECTED
	 * @param mission
	 * @return
	 */
	boolean canBeDeleted(Mission mission);

}
