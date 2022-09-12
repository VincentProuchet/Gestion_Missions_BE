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

import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.services.ExpenseTypeService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link ExpenseType} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = "expense_type", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ExpenseTypeController {
	
	/**
	 * The {@link ExpenseTypeService} dependency.
	 */
	private ExpenseTypeService expenseTypeService;

	/**
	 * Gets the full list of registered expense types.
	 * 
	 * @return A list of all expense types
	 */
	@GetMapping
	public List<ExpenseType> list() {
		return expenseTypeService.list();
	}
	
	/**
	 * Saves a new {@link ExpenseType} instance.
	 * 
	 * @param expenseType The new expense type within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody ExpenseType expenseType) {
		expenseTypeService.create(expenseType);
	}
	
	/**
	 * Updates the data for a specific registered expense type.
	 * 
	 * @param expenseType The expense type within the request body with modified info
	 * @return The resulting expense type with updated info
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ExpenseType update(@RequestBody ExpenseType expenseType) {
		return expenseTypeService.update(expenseType);
	}
	
	/**
	 * Deletes a specific registered expense type.
	 * 
	 * @param id The id of the expense type to delete
	 */
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) {
		expenseTypeService.delete(id);
	}
	
}