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

	/**
	 * return an instance of a Collaborator class from the currentDTO
	 */
	public Collaborator instantiate() {
		return new Collaborator(this);
	}

	/**
	 * retrun a Json form of the object
	 * @return
	 */
	public String toJson() {

		StringBuilder json = new StringBuilder("{")
				.append("\"id\":").append(this.id)
				.append(",\"firstName\":\"").append(this.firstName).append("\"")
				.append(",\"lastName\":\"").append(this.lastName).append("\"")
				.append(",\"username\":\"").append(this.username).append("\"")
				.append(",\"email\":\"").append(this.email).append("\"");
		json.append(",\"manager\":");
		if (this.manager != null) {
			json.append(this.manager.toJson());
		} else {
			json.append("null");
		}
		json.append(",\"roles\":");
		if (this.manager == null) {
			json.append("null");

		} else {
			json.append("[");
			
			for (Roles roles2 : roles) {
				json
				.append("{\"id\":").append(roles2.getId())
				.append(",\"label\":\"").append(roles2.getLabel()).append("\"")
				.append(",\"authority\":\"").append(roles2.getAuthority()).append("\"");
				
				json.append("},");
			}
			json.append("]");
		}

		json.append("}");
		return json.toString();
	}

}
