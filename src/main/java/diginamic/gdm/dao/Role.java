package diginamic.gdm.dao;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents a collaborator's role/function
 * 
 * @author DorianBoel
 */
public enum Role implements GrantedAuthority {

	USER("USER"),
	COLLABORATOR("COLLAB"),
	MANAGER("MAN" ),
	ADMIN("ADMIN");
	
	
	/**
	 * The name of the role
	 */
	public final String LABEL;
	
	
	private Role(String label) {
	LABEL = label;
	}


	@Override
	public String getAuthority() {
		
		return this.LABEL;
	}

	
}
