package diginamic.gdm.Enums;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
/**
 * @author Vincent
 *
 */
@Getter
public enum Role implements GrantedAuthority {

	ADMIN(1,"ADMINISTRATOR"),
	MANAGER(2000,"MANAGER" ),
	COLLABORATOR(3000,"COLLABORATOR"),
	USER(4000,"USER"),
	ANON(5000,"ANONYM"),
	;
	
	private int id;
	/**
	 * The name of the role
	 */
	public String LABEL;
	
	
	private Role(int id, String label) {
		this.id = id;
		this.LABEL = label;
	}


	@Override
	public String getAuthority() {
		
		return this.LABEL;
	}
}
