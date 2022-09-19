package diginamic.gdm.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.dao.Collaborator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Vincent
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollaboratorDTO implements DTO<Collaborator> {
	
	/** id */
	private int id = 0;
	/** lastName  nom de famille*/
	private String lastName = null;
	/** firstName prénom */
	private String firstName = null;
	
	/** password */
	private String password =null;
	/** newPassword for an eventual implementation of password update*/
	private String newPassword =null;
	
	/** userName */
	private String userName = null;
	
	/** role */
	private Collection<GrantedAuthority> roles;
	
	/** email */
	private String email = null;
	/** manager  le manager a un collaborateur null pour éviter
	 * de passer toute la */
	private CollaboratorDTO manager = null;
	
	public CollaboratorDTO(Collaborator collaborator) {
		this.id = collaborator.getId();
		this.lastName = collaborator.getLastName();
		this.firstName = collaborator.getFirstName();
		this.email = collaborator.getEmail();
		this.roles = collaborator.getAuthorities();
	}
	
	
	public Collaborator instantiate() {
		return new Collaborator();
	}
	
	/**
	 * this give the granted authoritiesList of the object 
	 * from the role (wich is not a list)
	 * @return ArrayList of GrantedAuthorities
	 */
	public ArrayList<GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> au = new ArrayList<>();
		for (GrantedAuthority gA : this.roles) {
			au.add(gA);
		}
		return au;
	}
	
}
