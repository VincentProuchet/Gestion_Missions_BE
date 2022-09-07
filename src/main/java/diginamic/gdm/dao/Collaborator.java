package diginamic.gdm.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

	/** missions : the missions this collaborator is in charge of */
	@OneToMany(mappedBy = "collaborator")
	private Set<Mission> missions = new HashSet<Mission>();

	/** manager : the manager of this collaborator */
	@ManyToOne
	@JoinColumn(name = "managerID")
	private Manager manager;

}
