package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;

/**
 * Interface to be implemented by an expense service class.
 * 
 * @author DorianBoel
 */
public interface ExpenseService {

	/**
	 * Gets the full list of registered expenses.
	 * For test purposes
	 * 
	 * @return A list of all expenses
	 */
	List<Expense> list();

	/**
	 * Gets the full list of registered expenses.
	 *
	 * @return A list of all expenses
	 */
	List<Expense> getExpensesOfMission(Mission mission);
	/**
	 * Saves a new {@link Expense} instance.
	 *
	 * @param expense The new expense to be registered
	 * @return the newly created expense, or null (to replace with exceptions)
	 */
	Expense create(Expense expense) throws BadRequestException;
	
	/**
	 * Gets a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to get
	 * @return The registered expense corresponding to the given id
	 */
	Expense read(int id) throws BadRequestException;
	
	/**
	 * Updates the data for a specific registered expense.
	 * Does not allow to change the mission of the expense
	 * 
	 * @param id The id corresponding to the expense to update
	 * @param expense The expense to update with modified info
	 * @return The resulting expense with updated info
	 */
	Expense update(int id, Expense expense) throws BadRequestException;
	
	/**
	 * Deletes a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to delete
	 */
	void delete(int id) throws BadRequestException;

	/**
	 * Check the validity of an expense
	 *
	 * @param expense the expense to validate
	 * @return true if the expense is correctly formed
	 */
	boolean isExpenseValid(Expense expense);
	
}
