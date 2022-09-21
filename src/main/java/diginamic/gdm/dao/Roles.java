package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a collaborator's role/function
 * user for storing GrantedAuthorities in database 
 * and bind them to userAccounts
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

	/** id */
	@Id
	private int id;
	/**
	 * The name of the role
	 */	
	public String label;
		
	@Override
	public String getAuthority() {
		
		return this.label;
	}
	public Roles(Role role) {
		this.id = role.getId();
		this.label = role.getLABEL();
	}
	
}
