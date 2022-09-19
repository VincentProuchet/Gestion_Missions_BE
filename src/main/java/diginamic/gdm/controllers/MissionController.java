package diginamic.gdm.controllers;

import java.util.List;

import diginamic.gdm.exceptions.BadRequestException;
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
@RequestMapping(path = "mission", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MissionController {
	
	/**
	 * The {@link MissionService} dependency.
	 */
	private MissionService missionService;

	/**
	 * Gets the full list of registered missions.
	 * 
	 * @return A list of all missions
	 */
	@GetMapping
	public List<MissionDTO> list() {
		// get the identity of the collaborator, send only their missions
		return missionService.list().stream().map(mission -> new MissionDTO(mission)).toList();
	}

	/**
	 * Gets the list of missions waiting for validation, only for a manager, and only the missions of his team members
	 *
	 * @return A list of all missions
	 */
	@GetMapping(path = "manager/{idManager}")
	public List<MissionDTO> missionsWaitingValidation(@PathVariable int idManager) throws BadRequestException {
		// get the identity of the manager
		return missionService.missionsToValidate(idManager).stream().map(mission -> new MissionDTO(mission)).toList();
	}
	
	/**
	 * Saves a new {@link Mission} instance.
	 * 
	 * @param mission The new mission within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody MissionDTO mission) throws BadRequestException {
		// make this creation is asked by the collaborator it is assigned to
		missionService.create(mission.instantiate());
	}
	
	/**
	 * Gets a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to get
	 * @return The registered mission corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	public MissionDTO read(@PathVariable int id) throws BadRequestException {
		// only a collaborator or his manager can ask for this
		return new MissionDTO(missionService.read(id));
	}
	
	/**
	 * Updates the data for a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to update
	 * @param missionDTO The mission withing the request body with modified info
	 * @return The resulting mission with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public MissionDTO update(@PathVariable int id, @RequestBody MissionDTO missionDTO) throws BadRequestException {
		//
		return new MissionDTO(missionService.update(id, missionDTO.instantiate()));
	}
	
	/**
	 * Deletes a specific registered mission.
	 * 
	 * @param id The id corresponding to the mission to delete
	 */
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) throws BadRequestException {
		missionService.delete(id);
	}
	
	/**
	 * Validates a mission by updating its status to {@link Status#VALIDATED VALIDATED}
	 * 
	 * @param id The id corresponding to the mission to validate
	 */
	@PutMapping(path = "{id}/valider")
	public void validate(@PathVariable int id) throws BadRequestException {
		missionService.updateStatus(id, Status.VALIDATED);
	}
	
	/**
	 * Rejects a mission by updating its status to {@link Status#REJECTED REJECTED}
	 * 
	 * @param id The id corresponding to the mission to validate
	 */
	@PutMapping(path = "{id}/rejeter")
	public void reject(@PathVariable int id) throws BadRequestException {
		missionService.updateStatus(id, Status.REJECTED);
	}
	
}