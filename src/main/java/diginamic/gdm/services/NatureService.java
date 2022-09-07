package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.Nature;

/**
 * Interface to be implemented by a mission nature service class
 * 
 * @author DorianBoel
 */
public interface NatureService {

	/**
	 * Gets the full list of registered mission natures.
	 * 
	 * @return A list of all natures
	 */
	List<Nature> list();
	
	/**
	 * Saves a new {@link Nature} instance.
	 * 
	 * @param nature The new nature to be registered
	 */
	void create(Nature nature);
	
	/**
	 * Updates the data for a specific registered mission nature.
	 * 
	 * @param id The id corresponding to the nature to update
	 * @return The resulting nature with updated info
	 */
	Nature update(int id);
	
	/**
	 * Deletes a specific registered mission nature.
	 * 
	 * @param id The id corresponding to the nature to delete
	 */
	void delete(int id);
	
}
