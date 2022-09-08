package diginamic.gdm.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for user account and authentication related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AccountController {

	/**
	 * The {@link CollaboratorService} dependency.
	 */
	private CollaboratorService collaboratorService;
	
	/**
	 * Registers a new user account
	 * 
	 * @param collaborator The new collaborator whose account to register
	 */
	@PostMapping(path = "signup")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void signup(@RequestBody Collaborator collaborator) {
		collaboratorService.create(collaborator);
	}
	
}
