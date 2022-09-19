package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.exceptions.BadRequestException;

/**
 * Interface to be implemented by an expense type service class
 * 
 * @author DorianBoel
 */
public interface ExpenseTypeService {

	/**
	 * Gets the full list of registered expense types.
	 * 
	 * @return A list of all expense types
	 */
	List<ExpenseType> list();
	
	/**
	 * Saves a new {@link ExpenseType} instance.
	 * 
	 * @param expenseType The new expense type to be registered
	 */
	void create(ExpenseType expenseType);
	
	/**
	 * Gets a specific registered expense type.
	 * 
	 * @param id The id corresponding to the expense type to get
	 * @return The registered expense type corresponding to the given id
	 */
	ExpenseType read(int id) throws BadRequestException;
	
	/**
	 * Updates the data for a specific registered expense type.
	 * 
	 * @param id The id corresponding to the expense type to update
	 * @param expenseType The expense type to update with modified info
	 * @return The resulting expense type with updated info
	 */
	ExpenseType update(int id, ExpenseType expenseType) throws BadRequestException;
	
	/**
	 * Deletes a specific registered expense type.
	 * 
	 * @param id The id of the expense type to delete
	 */
	void delete(int id) throws BadRequestException;
	
}
