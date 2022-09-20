package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.City;
import diginamic.gdm.exceptions.BadRequestException;

/**
 * Interface to be implemented by an city service class
 * 
 * @author DorianBoel
 */
public interface CityService {

	/**
	 * Gets the full list of registered cities.
	 * 
	 * @return A list of all cities
	 */
	List<City> list();

	/**
	 * Saves a new {@link City} instance.
	 * 
	 * @param city The new city to be registered
	 */
	void create(City city);
	
	/**
	 * Gets a specific registered city.
	 * 
	 * @param id The id corresponding to the city to get
	 * @return The registered city corresponding to the given id
	 */
	City read(int id) throws BadRequestException;

	/**
	 * Updates the data for a specific registered city.
	 * 
	 * @param id The id corresponding to the city to update
	 * @param city The city to update with modified info
	 * @return The resulting city with updated info
	 */
	City update(int id, City city) throws BadRequestException;

	/**
	 * Deletes a specific registered city.
	 * 
	 * @param id The id of the city to delete
	 */
	void delete(int id) throws BadRequestException;

}
