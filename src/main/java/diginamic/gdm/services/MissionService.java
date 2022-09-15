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
	 */
	void create(Mission mission);
	
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
	 * @param id The id corresponding to the mission to update
	 * @param mission The mission to update with modified info
	 * @return The resulting mission with updated info
	 */
	Mission update(int id, Mission mission);
	
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

}
