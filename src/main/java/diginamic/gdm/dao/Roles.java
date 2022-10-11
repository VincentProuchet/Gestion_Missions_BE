package diginamic.gdm.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.vars.GDMRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a collaborator's role/function user for storing GrantedAuthorities
 * in database and bind them to userAccounts
 * 
 * @author DorianBoel
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements GrantedAuthority {

	/** serialVersionUID */
	private static final long serialVersionUID = 6414675957320955726L;
	private static final String AUTHORITY_PREFIX = GDMRoles.AUTHORITY_PREFIX;

	/** id */
	@Id
	private int id;
	/**
	 * The name of the role
	 */
	@Column(nullable = false, unique = true)
	public String label;

	/**
	 * will automatically add the ROLE_ prefix if its not there
	 */
	@Override
	public String getAuthority() {
		if (!this.label.startsWith(AUTHORITY_PREFIX)) {
			return new StringBuilder(AUTHORITY_PREFIX).append(this.label).toString();
		}
		return this.label;
	}

	/**
	 * Constructeur for Role enum conversion to JPA entities
	 * 
	 * @param role
	 */
	public Roles(Role role) {
		this.id = role.getId();
		this.label = role.getLABEL();
	}

}
