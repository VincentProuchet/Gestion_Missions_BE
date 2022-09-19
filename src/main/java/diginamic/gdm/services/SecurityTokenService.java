package diginamic.gdm.services;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.SecurityToken;	

/**
 * 
 * 
 * @author Vincent
 *
 */
public interface SecurityTokenService {
	/**
	 * this is the login method your username and password are valid
	 *	you normally test the username and password in another place 
	 *	mainly because you may want to user some encryption method
	 *	this will give you a grant token
	 */
	public SecurityToken login(Collaborator user) throws Exception;
	/**
	 * In OAuth2 you don't get the authentification token directly
	 * 
	 * you have a grant token that you give to the same or another system 
	 *  this will give you a full authentification token
	 *  
	 *  this was designed to allow user to login with a platform
	 *  from another platform (login with ... git,facebook, google) 
	 * 
	 */
	public SecurityToken getGranted(SecurityToken auth, Long validitySeconds) throws Exception ;
	/**
	 * Here we check the validity of your authentication token 
	 * if if ok, it will give you back your token
	 * make sure to NOT send back the refresh and issued to the FE 
	 */
	public SecurityToken Authenticate(SecurityToken auth, Long validitySeconds) throws Exception ;
	
	/**
	 *To refresh your token
	 *and get new values
	 *and a new creation date 
	 *note that all values are refreshed 
	 *meaning the refresh value is only used once for authentication
	 *the less your refresh is exchanged the less risk you have.
	 */
	public SecurityToken Refresh(SecurityToken auth, Long validitySeconds) throws Exception;
	/**
	 * reset all value of a token
	 * the logout date is keept
	 */
	public boolean logout(SecurityToken token) throws Exception;
}
