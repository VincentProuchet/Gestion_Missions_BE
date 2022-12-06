package diginamic.gdm.vars;

/**
 * Roles used for
 * @Secure
 * Enum Role Label
 * Class Role database persistence
 * and userAccount rights of access
 *
 *  Having them starting with ROLE_
 *  is NOT a thing I fancy
 *  but a TRUE REQUIREMENT for the
 *  spring security to recognize the GrantedAuthorities
 *  the guy who designed that instead of a simple string comparison needs
 *  to be publicly stripped and hanged by the left ball
 * @author Vincent
 */
public abstract class GDMRoles {
	/** ADMIN */
	public static final String ADMIN ="ROLE_ADMINISTRATOR";
	/** MANAGER */
	public static final String MANAGER ="ROLE_MANAGER";
	/** COLLABORATOR */
	public static final String COLLABORATOR ="ROLE_COLLABORATOR";
	/** USER */
	public static final String USER ="ROLE_USER";
	/** ANON */
	public static final String ANON ="ROLE_ANONY";

	public static final String AUTHORITY_PREFIX = "ROLE_";
}
