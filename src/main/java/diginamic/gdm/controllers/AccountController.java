package diginamic.gdm.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.dto.CollaboratorDTO;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.vars.GDMRoles;
import diginamic.gdm.vars.GDMRoutes;
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

	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Registers a new user account
	 * 
	 * @param collaborator The new collaborator whose account to register
	 */
	
	@PostMapping(path = GDMRoutes.SIGNUP)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured({GDMRoles.ADMIN})
	public void signup(@RequestBody Collaborator collaborator) {
		// we use a compression algorythm
		collaborator.setPassword(this.EncryptThat(collaborator.getPassword()));
		for (Roles role : collaborator.getAuthorities()) {
			System.err.println(role);
		}
		collaboratorService.create(collaborator);
	}

	/**
	 * 
	 * 
	 * @param collaborator The new collaborator whose account to register
	 * @throws Exception
	 */
	@GetMapping(path = GDMRoutes.COLLABORATOR)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@Secured({GDMRoles.COLLABORATOR})
	public CollaboratorDTO getConnectedUser() throws Exception {
		return new CollaboratorDTO(collaboratorService.getConnectedUser());			
	}

	/**
	 * this is a function for password Encryption for making things easier to test
	 * 
	 * @param password
	 * @return
	 */
	public String EncryptThat(String password) {
		return passwordEncoder.encode(password);

	}

}
