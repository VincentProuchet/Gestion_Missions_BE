package diginamic.gdm.dto;

import lombok.Data;

/**
 * @author Vincent
 *	DTO pour le transfert des token et toutes valeurs
 * ayant trait à l'authentification
 * notez que les identifiants et mots de passes n'y sont pas 
 * pour des raisons évidentes de sécurités
 */

@Data
public class AuthenticationDTO {
	
	/** grantToken  provided by the authentication service 
	 * they expire in the same day or when used
	 */
	private String grantToken;
	/** authentication Token for  transactions
	 * THEY MUST have an expiration date */
	private String exchangeToken;
	/** refresh token 
	 *  token used for reconnection or after 
	 * expiration of exchange token 
	 */
	private String refreshToken;
	
	/** expirationDate  of exchange Token*/
	private String expirationDate;
	
	@Override
	public String toString() {
		
		return new StringBuilder()
				.append("\n grant Token : \n")
				.append(this.grantToken)
				.append("\n exchange Token : \n")
				.append(this.exchangeToken)
				.append("\n expire : \n")
				.append(this.expirationDate)
				.append("\n refresh Token : \n")
				.append(this.refreshToken)
				.toString();
	}
}
