package diginamic.gdm.controllers;

import java.util.List;

import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.services.CityService;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.ScheduledTasksService;
import diginamic.gdm.vars.GDMRoles;
import diginamic.gdm.vars.GDMRoutes;
import diginamic.gdm.vars.GDMVars;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
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

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dto.MissionDTO;
import diginamic.gdm.services.MissionService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link Mission} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = GDMRoutes.MISSION, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MissionController {

	/**
	 * The {@link CollaboratorService} dependency.
	 */
	private CollaboratorService collaboratorService;

	/**
	 * The {@link MissionService} dependency.
	 */
	private MissionService missionService;

	/** cityService */
	private CityService cityService;

	/**
	 * The {@link ScheduledTasksService} dependency.
	 */
	private ScheduledTasksService scheduledTasksService;

	/**
	 * The list of missions assigned to the connected user
	 * 
	 * @return A list of all missions
	 */
	@GetMapping
	@Secured(GDMRoles.COLLABORATOR)
	public List<MissionDTO> list() throws Exception {
		// get the identity of the collaborator, send only their missions
		Collaborator user = collaboratorService.getConnectedUser();
		return missionService.getMissionsOfCollaborator(user).stream().map(MissionDTO::new).toList();
	}

	/**
	 * Gets the list of missions waiting for validation, only for a manager, and
	 * only the missions of his team members
	 *
	 * @param idManager TODO this param is redundant, is it really more secure to
	 *                  keep it?
	 * @return A list of all missions
	 */
	@GetMapping(path = GDMRoutes.MANAGER + "/{idManager}")
	@Secured(GDMRoles.COLLABORATOR)
	public List<MissionDTO> missionsWaitingValidation(@PathVariable int idManager) throws Exception {
		// get the identity of the manager
		Collaborator user = collaboratorService.getConnectedUser();

		if (user.getId() == idManager) {
			return missionService.missionsToValidate(idManager).stream().map(MissionDTO::new).toList();
		}
		throw new Exception("it is not allowed to get the missions waiting validation if you are not the manager");
	}

	/**
	 * Saves a new {@link Mission} instance.
	 * 
	 * @param mission The new mission within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(GDMRoles.COLLABORATOR)
	public MissionDTO create(@RequestBody MissionDTO mission) throws Exception {
		// make sure this creation is asked by the collaborator it is assigned to
		Collaborator user = collaboratorService.getConnectedUser();
		City startCity;
		City arrivalCiTy;

		/**
		 * these try catch are a prototype for an automated way of adding cities on the
		 * fly it need to see a rework since the system is still case sensitive
		 */

		try {
			startCity = cityService.read(mission.getStartCity());
		} catch (BadRequestException e) {
			startCity = new City(mission.getStartCity());
			arrivalCiTy = cityService.create(startCity);
		}
		try {
			arrivalCiTy = cityService.read(mission.getArrivalCity());
		} catch (BadRequestException e) {
			arrivalCiTy = new City(mission.getArrivalCity());
			arrivalCiTy = cityService.create(arrivalCiTy);
		}
		return new MissionDTO(missionService.create(new Mission(mission, startCity, arrivalCiTy, user)));
	}

	/**
	 * Gets a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to get
	 * @return The registered mission corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public MissionDTO read(@PathVariable int id) throws BadRequestException {
		// only a collaborator or his manager can ask for this
		return new MissionDTO(missionService.read(id));
	}

	/**
	 * Updates the data for a specific registered mission.
	 * 
	 * @param id         The id corresponding to the mission to update
	 * @param missionDTO The mission withing the request body with modified info
	 * @return The resulting mission with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Secured(GDMRoles.COLLABORATOR)
	public MissionDTO update(@PathVariable int id, @RequestBody MissionDTO missionDTO) throws Exception {
		// Be aware that only mission with a certain status can be modified
		// and update should'nt take the status from client
		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = missionService.read(id);
		Collaborator assignee = mission.getCollaborator();
		// we check the first right to update
		if (user.getId() != assignee.getId()) {
			throw new Exception("it is not allowed to update a mission for someone else if you are not the manager");
		}
		Mission missionUpdated = new Mission(missionDTO);
		missionUpdated.setStartCity(cityService.read(missionDTO.getStartCity()));
		missionUpdated.setEndCity(cityService.read(missionDTO.getArrivalCity()));
		missionUpdated.setCollaborator(user);
		return new MissionDTO(missionService.update(id, missionUpdated));

	}

	/**
	 * Deletes a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to delete
	 */
	@DeleteMapping(path = "{id}")
	@Secured(GDMRoles.COLLABORATOR)
	public void delete(@PathVariable int id) throws Exception {

		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = missionService.read(id);

		if (user.getId() == mission.getCollaborator().getId()) {

			missionService.delete(id);
			return;
		}
		throw new Exception("it is not allowed to delete a mission for someone else");
	}

	/**
	 * Validates a mission by updating its status to {@link Status#VALIDATED
	 * VALIDATED}
	 * 
	 * @param id The id corresponding to the mission to validate
	 */
	@PutMapping(path = "{id}/" + GDMRoutes.VALIDER)
	@Secured(GDMRoles.MANAGER)
	public MissionDTO validate(@PathVariable int id) throws Exception {
		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = missionService.read(id);

		if (user.getId() == mission.getCollaborator().getManager().getId()) {
			return new MissionDTO(missionService.updateStatus(id, Status.VALIDATED));
		}
		throw new Exception("it is not allowed to validate a mission for someone not in your team");

	}

	/**
	 * Rejects a mission by updating its status to {@link Status#REJECTED REJECTED}
	 * 
	 * @param id The id corresponding to the mission to reject
	 */
	@PutMapping(path = "{id}/" + GDMRoutes.REJETER)
	@Secured(GDMRoles.MANAGER)
	public MissionDTO reject(@PathVariable int id) throws Exception {

		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = missionService.read(id);

		if (user.getId() == mission.getCollaborator().getManager().getId()) {
			return new MissionDTO(missionService.updateStatus(id, Status.REJECTED));
		}
		throw new Exception("it is not allowed to reject a mission for someone not in your team");
	}
	
	/**
	 * Resets a mission's status by updating its status to {@link Status#WAITING_VALIDATION WAITING_VALIDATION}
	 * 
	 * @param id The id corresponding to the mission to reset
	 */
	@PutMapping(path = "{id}/" + GDMRoutes.RESET)
	@Secured(GDMRoles.MANAGER)
	public MissionDTO reset(@PathVariable int id) throws Exception {

		Collaborator user = collaboratorService.getConnectedUser();
		Mission mission = missionService.read(id);

		if (user.getId() == mission.getCollaborator().getManager().getId()) {
			return new MissionDTO(missionService.updateStatus(id, Status.WAITING_VALIDATION));
		}
		throw new Exception("it is not allowed to reset a mission for someone not in your team");
	}

	/**
	 * First draft for the night computing
	 *
	 * @throws BadRequestException
	 */

	@GetMapping(path = GDMRoutes.TESTNIGHTCOMPUTING)
	@Secured(GDMRoles.ADMIN)
	@Scheduled(fixedRate = GDMVars.SHEDULED_INTERVAL)
	public void testNightComputing() throws BadRequestException {
		System.out.println("computing");

		scheduledTasksService.computeBonusForCompletedMissions();
		scheduledTasksService.changeMissionStatus();

	}

}