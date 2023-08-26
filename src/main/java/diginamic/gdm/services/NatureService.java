package diginamic.gdm.services;

import java.time.LocalDateTime;
import java.util.List;

import diginamic.gdm.dao.Nature;
import diginamic.gdm.exceptions.BadRequestException;

/**
 * Interface to be implemented by a mission nature service class the
 * implementation will have to handle persistence and the respect of all datas
 * integrity rules
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
	 * Saves a new {@link Nature} instance. The nature will have its start date of
	 * validity set to now
	 *
	 * @param nature The new nature to be registered
	 */
	Nature create(Nature nature) throws BadRequestException;

	/**
	 * Gets a specific registered mission nature.
	 *
	 * @param id The id corresponding to the nature to get
	 * @return The registered nature corresponding to the given id
	 */
	Nature read(int id) throws BadRequestException;

	/**
 	 * Gets a specific registered mission nature.
	 *
	 * @param description
	 * @return Nature
	 * @throws BadRequestException
	 */
	List<Nature> read(String description) throws BadRequestException;
	/**
	 * Updates the data for a specific registered mission nature.
	 *  The nature will be checked for its data integrity
	 *  and if its referenced by another entity in persistence
	 *  if so a new nature will be created
	 *
	 *  and entity using the previous one in the future (after start date )
	 *
	 *
	 * @param id     The id corresponding to the nature to update
	 * @param nature The nature to update with modified info
	 * @return The resulting nature with updated info
	 */
	Nature update(int id, Nature nature) throws BadRequestException;

	/**
	 * Deletes a specific registered mission nature.
	 *
	 * @param id The id corresponding to the nature to delete
	 */
	void delete(int id) throws BadRequestException;

	/**
	 * Return all the currently active natures, ie whose endDate is null
	 *
	 * @return
	 */
	List<Nature> getActiveNatures();
	/**
	 * Check if a nature was or is active at the given date
	 * if the date is null the present dateTime will be used,
	 *
	 * returns true if the nature is currently active
	 *
	 * @param nature must not be null
	 * @param date   if null, the date is now
	 * @return true if nature can be considered as active
	 */
	public boolean isNatureActive(Nature nature, LocalDateTime date);


}
