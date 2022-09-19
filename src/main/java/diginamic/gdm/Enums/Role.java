package diginamic.gdm.Enums;

import lombok.Getter;
@Getter
public enum Role {

	ANON(1000,"ANONYM"),
	USER(2000,"USER"),
	COLLABORATOR(3000,"COLLABORATOR"),
	MANAGER(4000,"MANAGER" ),
	ADMIN(5000,"ADMINISTRATOR");
	
	private int id;
	/**
	 * The name of the role
	 */
	public String LABEL;
	
	
	private Role(int id, String label) {
		this.id = id;
		this.LABEL = label;
	}
}
