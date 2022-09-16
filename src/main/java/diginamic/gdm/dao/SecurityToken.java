package diginamic.gdm.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	
	@OneToOne (mappedBy = "auth")
	private Collaborator collaborator;
	
	{
		this.issued = LocalDateTime.now();
	}
	
}
