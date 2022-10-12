package diginamic.gdm.dto;

import java.util.Collection;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * for sending user
 * 
 * @author Vincent
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollaboratorDTO extends Object implements DTO<Collaborator> {

	/** id */
	private int id = 0;
	/** lastName nom de famille */
	private String lastName = null;
	/** firstName prénom */
	private String firstName = null;
	/**
	 * Username not in camel due to necessities from the front-end so please don
	 * change it to camelCase
	 */
	private String username = null;
	/** role */
	private Collection<Roles> roles;
	/** email */
	private String email = null;
	/**
	 * manager le manager a un collaborateur null pour éviter de passer toute la
	 */
	private CollaboratorDTO manager = null;

	/**
	 * Constructeur
	 * 
	 * @param collaborator
	 */
	public CollaboratorDTO(Collaborator collaborator) {
		this.id = collaborator.getId();
		this.lastName = collaborator.getLastName();
		this.firstName = collaborator.getFirstName();
		this.username = collaborator.getUsername();
		this.email = collaborator.getEmail();
		this.roles = collaborator.getAuthorities();
		if (collaborator.getManager() != null) {
			this.manager = new CollaboratorDTO(collaborator.getManager(), false);
		}
	}

	/**
	 * Alternate Constructeur made to get mangers of a user made to avoid the
	 * stackOverflow
	 * 
	 * @param collaborator
	 * @param withManager
	 */
	public CollaboratorDTO(Collaborator collaborator, Boolean withManager) {
		this.id = collaborator.getId();
		this.lastName = collaborator.getLastName();
		this.firstName = collaborator.getFirstName();
		this.email = collaborator.getEmail();
		if (withManager) {
			this.manager = new CollaboratorDTO(collaborator.getManager(), false);
		}
	}


	
}
