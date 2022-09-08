package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.Expense;

/**
 * Interface to be implemented by an expense service class.
 * 
 * @author DorianBoel
 */
public interface ExpenseService {

	/**
	 * Gets the full list of registered expenses.
	 * 
	 * @return A list of all expenses
	 */
	List<Expense> list();
	
	/**
	 * Saves a new {@link Expense} instance.
	 * 
	 * @param expense The new expense to be registered
	 */
	void create(Expense expense);
	
	/**
	 * Updates the data for a specific registered expense.
	 * 
	 * @param expense The expense to update with modified info
	 * @return The resulting expense with updated info
	 */
	Expense update(Expense expense);
	
	/**
	 * Deletes a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to delete
	 */
	void delete(int id);
	
}
