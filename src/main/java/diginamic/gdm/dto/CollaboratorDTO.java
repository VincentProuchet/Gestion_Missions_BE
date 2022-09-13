package diginamic.gdm.dto;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Role;
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
	
	/** role */
	private Role role = null;
	
	/** email */
	private String email = null;
	/** manager  le manager a un collaborateur null pour éviter
	 * de passer toute la */
	private CollaboratorDTO manager = null;
	
	public CollaboratorDTO(Collaborator collaborator) {
		this.id = collaborator.getId();
		this.lastName = collaborator.getLastName();
		this.firstName = collaborator.getFirstName();
		this.role = collaborator.getRole();
		this.email = collaborator.getEmail();
		this.email = collaborator.getEmail();
	}
	
	public Collaborator instantiate() {
		return new Collaborator(id, lastName, firstName, email, "", role, null, null);
	}
	
}
