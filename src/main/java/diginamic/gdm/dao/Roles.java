package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	//private static RoleService service;
	

	@Override
	public String getAuthority() {
		
		return this.label;
	}

	
}
