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
	 * @return the newly created expense, or null (to replace with exceptions)
	 */
	Expense create(Expense expense);

	/**
	 * TODO
	 *
	 * @param missionId
	 * @param expense
	 * @return
	 */
	default Expense create(int missionId, Expense expense) {
		return create(expense);
	}
	
	/**
	 * Gets a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to get
	 * @return The registered expense corresponding to the given id
	 */
	Expense read(int id);
	
	/**
	 * Updates the data for a specific registered expense.
	 * Does not allow to change the mission of the expense
	 * 
	 * @param id The id corresponding to the expense to update
	 * @param expense The expense to update with modified info
	 * @return The resulting expense with updated info
	 */
	Expense update(int id, Expense expense);
	
	/**
	 * Deletes a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to delete
	 */
	void delete(int id);

	/**
	 * Check the validity of an expense
	 *
	 * @param expense
	 * @return
	 */
	boolean isExpenseValid(Expense expense);
	
}
