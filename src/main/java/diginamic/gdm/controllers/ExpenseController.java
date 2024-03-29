package diginamic.gdm.controllers;

import java.util.List;

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

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dto.ExpenseDTO;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.ExpenseService;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.vars.GDMRoles;
import diginamic.gdm.vars.GDMRoutes;
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

	/** missionService */
	private MissionService missionService;

	/** collaboratorService */
	private CollaboratorService collaboratorService;

	/**
	 * Gets the full list of registered expenses.
	 * For test purposes
	 *
	 * @return A list of all expenses
	 */
	@GetMapping
	@Secured({GDMRoles.COLLABORATOR})
	public List<ExpenseDTO> list() throws Exception {
		return expenseService.list().stream().map(ExpenseDTO::new).toList();
	}

	/**
	 * Saves a new {@link Expense} instance.
	 *
	 * @param expenseDTO The new expense within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(GDMRoles.COLLABORATOR)
	public ExpenseDTO create(@RequestBody ExpenseDTO expenseDTO) throws Exception {
		// we get the connected user
		Collaborator user = collaboratorService.getConnectedUser();
		// we get the mission form DB
		Mission mission = missionService.read(expenseDTO.getIdMission());
		// we check if the user has the righrs to add expenses to THAT mission
		if(mission.getCollaborator().getId() == user.getId()){
			// we create a new expense
			Expense newExpense = new Expense(expenseDTO);
			// and give it he mission its supposed to
			newExpense.setMission(mission);

			return new ExpenseDTO(expenseService.create(newExpense));
		}
		throw new Exception("Only the assignee can create an expense for a mission");
	}

	/**
	 * Gets a specific registered expense.
	 *
	 * @param id The id corresponding to the expense to get
	 * @return The registered expense corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public ExpenseDTO read(@PathVariable int id) throws Exception {
		Collaborator user = collaboratorService.getConnectedUser();
		Expense expense = expenseService.read(id);
		Mission mission = expense.getMission();
		if(mission.getCollaborator().getId() == user.getId()){
			return new ExpenseDTO(expense);
		}
		throw new BadRequestException(ErrorCodes.expenseNotFound,"Only the assignee can see the expenses of a mission");
	}
	/**
	 * Gets the expenses associated to a given mission
	 *
	 * @param id The id of the mission
	 * @return the list of expenses of the mission
	 */
	@GetMapping(path = "/"+GDMRoutes.MISSION)
	@Secured(GDMRoles.COLLABORATOR)
	public List<ExpenseDTO> readByMission(@PathVariable int id) throws Exception {

		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = missionService.read(id);
		if(mission.getCollaborator().getId() == user.getId()){
			return expenseService.getExpensesOfMission(mission).stream().map(ExpenseDTO::new).toList();
		}
		throw new BadRequestException(ErrorCodes.expenseNotFound,"Only the assignee can see the expenses of a mission");
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
	public ExpenseDTO update(@PathVariable int id, @RequestBody ExpenseDTO expenseDTO) throws Exception {

		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = expenseService.read(id).getMission();
		if(mission.getCollaborator().getId() == user.getId()){
			Expense modifiedExpense = new Expense(expenseDTO);
			Mission emptyMission = new Mission();
			emptyMission.setId(expenseDTO.getIdMission());
			modifiedExpense.setMission(emptyMission);
			return new ExpenseDTO(expenseService.update(id, modifiedExpense));
		}
		throw new Exception("Only the assignee can update the expenses of a mission");
	}

	/**
	 * Deletes a specific registered expense.
	 *
	 * @param id The id corresponding to the expense to delete
	 */
	@DeleteMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public void delete(@PathVariable int id) throws Exception {
		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = expenseService.read(id).getMission();
		if(mission.getCollaborator().getId() != user.getId()){
			throw new Exception("Only the assignee can delete the expenses of a mission");
		}
		expenseService.delete(id);
	}

}