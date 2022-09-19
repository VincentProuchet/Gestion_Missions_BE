package diginamic.gdm.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.GDMRoutes;
import diginamic.gdm.GDMVars;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.SecurityToken;
import diginamic.gdm.dto.AuthenticationDTO;
import diginamic.gdm.dto.LoginDTO;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.SecurityTokenService;
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
	private SecurityTokenService tokenService;
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
		SecurityToken token;
		// we instanciate an authenticationDTO
		Collaborator user =  this.collaboratorService.getByUsername(login.getLogin());
		// we use the compression algorythm
		//login.setLogin(this.EncryptThat(login.getLogin()));
		
		
//		if(user.getPassword()==((this.EncryptThat(login.getLogin())))) {
		if(true) {
		
		    token = this.tokenService.login(user);
		    // we call the get granted because the authentication server 
		    // and the acces server are the same
			token = this.tokenService.getGranted(token, GDMVars.TOKEN_LIFE);
			AuthenticationDTO auth = new AuthenticationDTO(token);
			System.out.println(auth);
			return auth;
		}
		else {
			System.err.println(login.getLogin());
			System.err.println(login.getPassword());
		
			System.err.println(user.getUsername());
			System.err.println(user.getPassword());
			// we shoud have an error 
			throw new Exception("Invalid Crédentials");
		}
		// send back the grant token
		

	}

	/**
	 * take grantToke given by login Procedure
	 * or a refreshToken
	 * checks it and give back 
	 * a token for transaction
	 * a token for refresh
	 * 
	 * the refresh or the grant MUST ALLWAYS be a sole value
	 * Hence the first value is ALLWAYS NULL 
	 * @param grantToken
	 * @throws Exception 
	 */	
	@PostMapping(path=GDMRoutes.AUTH+"/"+GDMRoutes.GRANT ,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
public AuthenticationDTO getToken(@RequestBody AuthenticationDTO grantToken) throws Exception {
		AuthenticationDTO auth = new AuthenticationDTO();
		// the client send us a tokenDTO
		// we test the refresh and the grant
		SecurityToken token = new SecurityToken(grantToken);	
	return new AuthenticationDTO(this.tokenService.getGranted(token, GDMVars.TOKEN_LIFE));

}

	/**
	 * authentification is an automated process separate from the login
	 * and I'm thinking about if this is really the place for that function 
	 * who wont be called from outside but only from inside
	 * 
	 * keep in mind that the refreshToken should be only send twice, 
	 * to the FE at login
	 * to the backEnd to Refresh
	 * 
	 * THIS fonction
	 * @param auth
	 * @return Id of the authenticated User
	 * @throws Exception
	 */
	@PostMapping(path = GDMRoutes.AUTH+"/"+GDMRoutes.AUTH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public Integer authenticate(AuthenticationDTO auth) throws Exception {
		System.err.println("Authenticate");
		System.out.println(auth);
		SecurityToken token = new SecurityToken(auth); 
		return this.tokenService.Authenticate(token, GDMVars.TOKEN_LIFE).getCollaborator().getId();		
	}
	@PostMapping(path = GDMRoutes.AUTH+"/"+GDMRoutes.REFRESH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public AuthenticationDTO refresh(@RequestBody AuthenticationDTO auth) throws Exception {
		// the client send us a tokenDTO
		// we test the refresh and the grant
		SecurityToken token = new SecurityToken(auth);	
	return new AuthenticationDTO(this.tokenService.Refresh(token, GDMVars.TOKEN_LIFE));

	}
	
	/**
	 * this is a mock function for password Encryption
	 * they are nbot encrypted yet 
	 * for making things easier to test
	 * @param password
	 * @return
	 */
	public String EncryptThat(String password) {
		//return passwordEncoder.encode(password);
		return password;
	}
	
	
}
