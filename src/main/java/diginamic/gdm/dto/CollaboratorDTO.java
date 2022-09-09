package diginamic.gdm.dto;

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
public class CollaboratorDTO {
	
	/** id */
	private int id = 0;
	/** lastName  nom de famille*/
	private String lastName = null;
	/** firstName prénom */
	private String firstName = null;
	
	/** email */
	/**Il manque le Rôle il va falloir
	 *  créer la classe qui le définit 
	 * pour pouvoir les passer dans le DTO
	 * ou le passer en texte
	 * private Role role;
	 */
	
	private String email = null;
	/** manager  le manager a un collaborateur null pour éviter
	 * de passer toute la */
	private CollaboratorDTO manager = null;
	
}
