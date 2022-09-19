package diginamic.gdm.controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.GDMRoutes;
import diginamic.gdm.GDMVars;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.dto.AuthenticationDTO;
import diginamic.gdm.dto.LoginDTO;
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
	
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Registers a new user account
	 * 
	 * @param collaborator The new collaborator whose account to register
	 */
	@PostMapping(path = GDMRoutes.SIGNUP)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void signup(@RequestBody Collaborator collaborator) {
		// we use a compression algorythm
		collaborator.setPassword(this.EncryptThat(collaborator.getPassword()));
		Collection<Roles> roles = collaborator.getAuthorities();
		for (Roles role : roles) {
			System.err.println(role);
		}
		collaboratorService.create(collaborator);
	}

	/**
	 * take crédential search database and valid or not the connection by a
	 * connection grant token
	 * 
	 * @param login
	 * @throws Exception 
	 */
	@PostMapping(path = GDMRoutes.SIGNIN, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public AuthenticationDTO logIn(@RequestBody LoginDTO login) throws Exception {
		AuthenticationDTO dtoToken;
		return null;
		
		
		

	}


	/**
	 * this is a  function for password Encryption
	 * for making things easier to test
	 * @param password
	 * @return
	 */
	public String EncryptThat(String password) {
		return passwordEncoder.encode(password);
		
	}
	
	
}
