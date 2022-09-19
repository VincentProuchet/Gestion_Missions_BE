package diginamic.gdm.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import diginamic.gdm.dto.AuthenticationDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SECURITY_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SecurityToken {
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private Integer id;
	@Column(name = "grant_token")
	private String grant;
	@Column(name = "auth_token")
	private String authentification;
	@Column(name = "refresh_token")
	private String refresh;
	/** creation date for the security Token */
	private LocalDateTime issued;
	
	@OneToOne 
	private Collaborator collaborator;
	
	/**
	 *  set a new token as a grant one
	 *  you jut logged in and the system
	 *   now have to ask for an authentication 
	 * 
	 * @param user
	 */
	public void newGrant(Collaborator user) {
		this.collaborator = user ;
		this.grant = RandomStringUtils.random(6, false, true);
		this.issued = LocalDateTime.now();
		this.refresh = null;
		this.authentification = null;
	}
	/** set token value 
	 * authentication 
	 * refresh 
	 * as all authentication token should be
	 * not that you can't change the user assigne to that token 
	 * 
	 */
	public void granted() {
		this.grant = null;
		this.issued = LocalDateTime.now();
		this.refresh = RandomStringUtils.random(6, false, true);
		this.authentification = RandomStringUtils.random(6, false, true);
	}
	/**
	 * a logout doens't mean destruction of a toke
	 * it just mean that a can't be found by
	 *  authentication
	 *  refresh 
	 *  or grant
	 *  and you have to login again 
	 */
	public void logOut() {
		this.grant = null;
		this.issued = LocalDateTime.now();
		this.refresh = null;
		this.authentification = null;
	}
	public SecurityToken(AuthenticationDTO auth) {
		this.grant = auth.getGrant();
		this.issued =  auth.getCreationDate();
		this.refresh = auth.getRefresh();
		this.authentification = auth.getAuthentication();
		
	}
}
