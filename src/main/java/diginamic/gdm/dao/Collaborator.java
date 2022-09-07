package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which reprensents a Collaborator
 * 
 * @author Joseph
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Collaborator {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** lastName */
	private String lastName;
	/** firstName */
	private String firstName;
	/** email : identification mail address */
	private String email;
	/** password : remember to add security */
	private String password;

}
