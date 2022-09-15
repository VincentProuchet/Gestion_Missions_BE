package diginamic.gdm.dao;

/**
 * Represents a collaborator's role/function
 * 
 * @author DorianBoel
 */
public enum Role {

	COLLABORATOR("collaborator", Collaborator.class),
	MANAGER("manager", Manager.class),
	ADMIN("admin", Administrator.class);
	
	/**
	 * The name of the role
	 */
	public final String LABEL;
	
	/**
	 * The class entity associated with this specific role
	 */
	public final Class<? extends Collaborator> AS_CLASS;
	
	private Role(String label, Class<? extends Collaborator> cl) {
		LABEL = label;
		AS_CLASS = cl;
	}
	
}
