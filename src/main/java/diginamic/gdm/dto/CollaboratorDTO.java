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
	private Integer id = 0;
	/** lastName  nom de famille*/
	private String lastName = null;
	/** firstName prénom */
	private String firstName = null;
	/** password */
	private String password = null;
	/** email */
	private String email = null;
	/** manager */
	private CollaboratorDTO manager = null;
	
}
