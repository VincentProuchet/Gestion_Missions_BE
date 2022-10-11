package diginamic.gdm.Enums;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.vars.GDMRoles;
import lombok.Getter;
/**
 * here lies the Granted authorities 
 * that will be inserted in the database at 
 * the application startup
 * in the meantime
 * just not changing the id's would do the trick 
 * @author Vincent
 *
 */
@Getter
public enum Role implements GrantedAuthority {

	/** ADMIN */
	ADMIN(1,GDMRoles.ADMIN),
	/** MANAGER */
	MANAGER(2000,GDMRoles.MANAGER),
	/** COLLABORATOR */
	COLLABORATOR(3000,GDMRoles.COLLABORATOR),
	/** USER */
	USER(4000,GDMRoles.USER),
	/** ANON */
	ANON(5000,GDMRoles.ANON),
	;
	
	public static final String AUTHORITY_PREFIX = GDMRoles.AUTHORITY_PREFIX; 
	/** id */
	private final int id;
	/**
	 * The name of the role
	 */
	public final String LABEL;
	
	
	/** Constructeur
	 * @param id
	 * @param label
	 */
	private Role(int id, String label) {
		this.id = id;
		this.LABEL = label;
	}


	@Override
	public String getAuthority() {		
		if (!this.LABEL.startsWith(AUTHORITY_PREFIX)) {
			return new StringBuilder(AUTHORITY_PREFIX).append(this.LABEL).toString();
		}
		return this.LABEL;
	}
}
