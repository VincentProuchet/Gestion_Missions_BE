package diginamic.gdm.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.dao.Collaborator;
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

	/**
	 * take crédential search database and valid or not the connection by a
	 * connection grant token
	 * 
	 * @param login
	 */
	@PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CHECKPOINT)
	public AuthenticationDTO logIn(@RequestBody LoginDTO login) {
		// we instanciate an authenticationDTO
		AuthenticationDTO auth = new AuthenticationDTO();
		// running encryption method on the password value
		// looking like that
		
		// login.setPassword(encryptThat(login.getPassword()));
		
		// do credential search		
		// wich should look like something like this :
		
		// if(collaboratorService.list().stream()
		//.filter(c->(c.getEmail().equals(login.getLogin())
		// and yes there should be an encryption method before the comparison.
		//			||c.getPassword().equals(login.getPassword()))));
		if(login.getLogin().equals("lui")|| login.getPassword().equals("1234")) {
			// get a connection grant token
			// from security Service
			auth.setGrantToken("gtsergfbvsnbrshsfbgfbs");
		}
		else {
			// we shoud have an error 
			System.err.println("login invalid");
		}
		// send back the grant token
		return auth;

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
	 */	
	@PostMapping(path="refreshToken",consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CHECKPOINT)
public AuthenticationDTO getToken(@RequestBody AuthenticationDTO grantToken) {
		AuthenticationDTO auth = new AuthenticationDTO();
		// the client send us a tokenDTO
		// we test the refresh and the grant
		if (grantToken.getRefreshToken().equals("gtsergfbvsnbrshsfbgfbs") || grantToken.getGrantToken() .equals("qfqsdfqsdfsfdgsghshsgh") )
		{
			// if one of them is valid 
			// we get new token
			auth.setExchangeToken("sdghrzhsfdgfdgdfsqergsgfg");
			auth.setRefreshToken("qfqsdfqsdfsfdgsghshsgh");
			auth.setExpirationDate(LocalDateTime.now().plusHours(1).toString());
			// and send them back tu client
			return auth;
		}
		else {
			// we should 
			System.err.println(auth.toString());
			return auth;
		}

}

	/**
	 * authentification is an automated process separate from the login
	 * and I'm thinking about if this is really the place for that function 
	 * who wont be called from outside but only from inside
	 * 
	 * keep in mind that the refreshToken is only send once
	 * THIS fonction say No
	 * @param auth
	 */
	@PostMapping(path = "auth", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public boolean authenticate(@RequestBody AuthenticationDTO auth) {
		// login comparing exchange toke
		if (auth.getExchangeToken().equals("sdghrzhsfdgfdgdfsqergsgfg"))
			return true;
		else
			/**
			// this is not an error
			// the exchange token is either 
			 * invalid 
			 * or expired 
			 * yes we can test it but what about 
			 * really old tokens that got wiped out of the database ?
			 * so we don't throw an error
			 * we just say NO
			 */		
			return false;
	}
}
