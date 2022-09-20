package diginamic.gdm.controllers;

import java.util.List;

import diginamic.gdm.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.GDMRoles;
import diginamic.gdm.GDMRoutes;
import diginamic.gdm.dao.Expense;
import diginamic.gdm.dto.ExpenseDTO;
import diginamic.gdm.services.ExpenseService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link Expense} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = GDMRoutes.EXPENSE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	@Secured(GDMRoles.COLLABORATOR)
	public List<ExpenseDTO> list() {
		return expenseService.list().stream().map(expense -> new ExpenseDTO(expense)).toList();
	}
	
	/**
	 * Saves a new {@link Expense} instance.
	 * 
	 * @param expense The new expense within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(GDMRoles.COLLABORATOR)
	public void create(@RequestBody ExpenseDTO expense) throws BadRequestException {
		expenseService.create(expense.getIdMission(), expense.instantiate());
	}
	
	/**
	 * Gets a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to get
	 * @return The registered expense corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public ExpenseDTO read(@PathVariable int id) throws BadRequestException {
		return new ExpenseDTO(expenseService.read(id));
	}
	
	/**
	 * Updates the data for a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to update
	 * @param expenseDTO The expense within the request body with modified info
	 * @return The resulting expense with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Secured(GDMRoles.COLLABORATOR)
	public ExpenseDTO update(@PathVariable int id, @RequestBody ExpenseDTO expenseDTO) throws BadRequestException {
		return new ExpenseDTO(expenseService.update(id, expenseDTO.instantiate()));
	}
	
	/**
	 * Deletes a specific registered expense.
	 * 
	 * @param id The id corresponding to the expense to delete
	 */
	@DeleteMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public void delete(@PathVariable int id) throws BadRequestException {
		expenseService.delete(id);
	}
	
}