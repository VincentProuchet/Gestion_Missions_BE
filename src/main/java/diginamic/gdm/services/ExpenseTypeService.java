package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.ExpenseType;

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
	 * Updates the data for a specific registered expense type.
	 * 
	 * @param expenseType The expense type to update with modified info
	 * @return The resulting expense type with updated info
	 */
	ExpenseType update(ExpenseType expenseType);
	
	/**
	 * Deletes a specific registered expense type.
	 * 
	 * @param name The name of the expense type to delete
	 */
	void delete(int id);
	
}
