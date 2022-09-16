package diginamic.gdm.services;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.SecurityToken;	

public interface SecurityTokenService {
	
	public SecurityToken login(Collaborator user) throws Exception;

	public SecurityToken getGranted(SecurityToken auth, Long validitySeconds) throws Exception ;

	public SecurityToken Authenticate(SecurityToken auth, Long validitySeconds) throws Exception ;

	public SecurityToken Refresh(SecurityToken auth, Long validitySeconds) throws Exception;

	public void deleteToken(SecurityToken token);

	public boolean logout(SecurityToken token);
}
