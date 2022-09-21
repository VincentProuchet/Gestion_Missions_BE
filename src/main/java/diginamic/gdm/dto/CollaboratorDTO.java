package diginamic.gdm.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import diginamic.gdm.dao.Collaborator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * for sending user
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
	/** userName */
	private String userName = null;	
	/** role */
	private Collection<? extends GrantedAuthority> roles;	
	/** email */
	private String email = null;
	/**
	 * manager  le manager a un collaborateur null pour éviter
	 * de passer toute la 
	 */
	private CollaboratorDTO manager = null;
	
	/** Constructeur
	 * @param collaborator
	 */
	public CollaboratorDTO(Collaborator collaborator) {
		this.id = collaborator.getId();
		this.lastName = collaborator.getLastName();
		this.firstName = collaborator.getFirstName();
		this.email = collaborator.getEmail();
		this.roles = collaborator.getAuthorities();
	}
	
	
	/**
	 *
	 */
	public Collaborator instantiate() {
		return new Collaborator();
	}
	
	
}
