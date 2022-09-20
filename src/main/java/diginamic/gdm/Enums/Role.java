package diginamic.gdm.Enums;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.GDMRoles;
import lombok.Getter;
/**
 * @author Vincent
 *
 */
@Getter
public enum Role implements GrantedAuthority {

	ADMIN(1,GDMRoles.ADMIN),
	MANAGER(2000,GDMRoles.MANAGER),
	COLLABORATOR(3000,GDMRoles.COLLABORATOR),
	USER(4000,GDMRoles.USER),
	ANON(5000,GDMRoles.ANON),
	;
	
	private final int id;
	/**
	 * The name of the role
	 */
	public final String LABEL;
	
	
	private Role(int id, String label) {
		this.id = id;
		this.LABEL = label;
	}


	@Override
	public String getAuthority() {
		
		return this.LABEL;
	}
}
