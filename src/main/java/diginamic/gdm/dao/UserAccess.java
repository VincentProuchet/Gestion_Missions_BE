package diginamic.gdm.dao;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER_ACCESS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAccess {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@OneToOne
	private Collaborator userId;
	
	private Instant creation;
	private String grantToken;
	private String transactionToken;
	private String refreshToken;

}
