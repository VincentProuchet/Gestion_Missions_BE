package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a collaborator's role/function
 * 
 * @author DorianBoel
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements GrantedAuthority {

	/** serialVersionUID */
	private static final long serialVersionUID = 6414675957320955726L;

	@Id
	private int id;
	/**
	 * The name of the role
	 */
	
	public String LABEL;
	//private static RoleService service;
	

	@Override
	public String getAuthority() {
		
		return this.LABEL;
	}

	
}
