package diginamic.gdm.dao;

public enum Role {

	COLLABORATOR(Collaborator.class),
	MANAGER(Manager.class),
	ADMIN(Administrator.class);
	
	public final Class<? extends Collaborator> CLASS;
	
	private Role(Class<? extends Collaborator> cl) {
		CLASS = cl;
	}
	
}
