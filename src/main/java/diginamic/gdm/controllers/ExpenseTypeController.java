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
import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.dto.ExpenseTypeDTO;
import diginamic.gdm.services.ExpenseTypeService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link ExpenseType} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = GDMRoutes.EXPENSE+"_"+GDMRoutes.TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	@Secured(GDMRoles.COLLABORATOR)
	public List<ExpenseTypeDTO> list() {
		return expenseTypeService.list().stream().map(expenseType -> new ExpenseTypeDTO(expenseType)).toList();
	}
	
	/**
	 * Saves a new {@link ExpenseType} instance.
	 * 
	 * @param expenseType The new expense type within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(GDMRoles.ADMIN)
	public void create(@RequestBody ExpenseTypeDTO expenseType) {
		expenseTypeService.create(expenseType.instantiate());
	}
	
	/**
	 * Gets a specific registered expense type.
	 * 
	 * @param id The id corresponding to the expense type to get
	 * @return The registered expense type corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public ExpenseTypeDTO read(@PathVariable int id) throws BadRequestException {
		return new ExpenseTypeDTO(expenseTypeService.read(id));
	}
	
	/**
	 * Updates the data for a specific registered expense type.
	 * 
	 * @param id The id corresponding to the expense type to update
	 * @param expenseTypeDTO The expense type within the request body with modified info
	 * @return The resulting expense type with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Secured(GDMRoles.ADMIN)
	public ExpenseTypeDTO update(@PathVariable int id, @RequestBody ExpenseTypeDTO expenseTypeDTO) throws BadRequestException {
		return new ExpenseTypeDTO(expenseTypeService.update(id, expenseTypeDTO.instantiate()));
	}
	
	/**
	 * Deletes a specific registered expense type.
	 * 
	 * @param id The id of the expense type to delete
	 */
	@DeleteMapping(path = "{id}")
	@Secured(GDMRoles.ADMIN)
	public void delete(@PathVariable int id) throws BadRequestException {
		expenseTypeService.delete(id);
	}
	
}