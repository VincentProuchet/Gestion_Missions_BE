package diginamic.gdm.dao;

/**
 * Represents a collaborator's role/function
 * 
 * @author DorianBoel
 */
public enum Role {

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

	
}
