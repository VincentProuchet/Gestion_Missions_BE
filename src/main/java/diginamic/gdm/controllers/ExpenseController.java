package diginamic.gdm.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.services.ExpenseService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link Expense} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = "expense", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ExpenseController {
	
	/**
	 * The {@link ExpenseService} dependency.
	 */
	private ExpenseService expenseService;

	/**
	 * Gets the full list of registered expenses.
	 * 
	 * @return A list of all expenses
	 */
	@GetMapping
	public List<Expense> list() {
		return expenseService.list();
	}
	
	/**
	 * Saves a new {@link Expense} instance.
	 * 
	 * @param expense The new expense within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody Expense expense) {
		expenseService.create(expense);
	}
	
	/**
	 * Gets a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to get
	 * @return The registered expense corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	public Expense read(@PathVariable int id) {
		return expenseService.read(id);
	}
	
	/**
	 * Updates the data for a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to update
	 * @param expense The expense within the request body with modified info
	 * @return The resulting expense with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Expense update(@PathVariable int id, @RequestBody Expense expense) {
		return expenseService.update(id, expense);
	}
	
	/**
	 * Deletes a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to delete
	 */
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) {
		expenseService.delete(id);
	}
	
}