package diginamic.gdm.dto;

import java.time.LocalDateTime;

import diginamic.gdm.dao.SecurityToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vincent
 *	DTO pour le transfert des token et toutes valeurs
 * ayant trait à l'authentification
 * notez que les identifiants et mots de passes n'y sont pas 
 * pour des raisons évidentes de sécurités
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationDTO {
	
	/** grantToken  provided by the authentication service 
	 * they expire in the same day or when used
	 */
	private String grant;
	/** authentication Token for  transactions
	 * THEY MUST have an expiration date */
	private String authentication;
	/** refresh token 
	 *  token used for reconnection or after 
	 * expiration of exchange token 
	 */
	private String refresh;
	
	/** Creation Date  of exchange Token*/
	private LocalDateTime creationDate;
	
	@Override
	public String toString() {
		
		return new StringBuilder()
				.append("\n grant Token : \n")
				.append(this.grant)
				.append("\n exchange Token : \n")
				.append(this.authentication)
				.append("\n expire : \n")
				.append(this.creationDate)
				.append("\n refresh Token : \n")
				.append(this.refresh)
				.toString();
	}
	
	/** Constructeur
	 * for conversiotn from security
	 * @param token
	 */
	public AuthenticationDTO(SecurityToken token){
	this.grant = token.getGrant();
		/** authentication Token for  transactions
		 * THEY MUST have an expiration date */
	this.authentication = token.getGrant();
		/** refresh token 
		 *  token used for reconnection or after 
		 * expiration of exchange token 
		 */
	this.refresh = token.getAuthentification() ;
		
		/** expirationDate  of exchange Token*/
	this.creationDate = token.getIssued();
	}
	/**
	 * will set the token for an authentication 
	 * thus only respond with the authentication par of the SEcurity token
	 * you should NOT put that on a cookies
	 */
	public void forAuthentication() {
		this.grant = null;
		this.refresh = null;
		this.creationDate = null;
	}
}
