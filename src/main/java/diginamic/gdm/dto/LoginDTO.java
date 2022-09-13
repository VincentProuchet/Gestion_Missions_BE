package diginamic.gdm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDTO {
	/**identifiant de connexion */
	private String login;
	
	/** mot de passe  */
	private String password;
}
