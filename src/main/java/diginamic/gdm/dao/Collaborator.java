package diginamic.gdm.dao;

import java.util.Collection;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import diginamic.gdm.dto.CollaboratorDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity which reprensents a Collaborator
 * 
 * @author Joseph
 *
 */
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Collaborator extends User {

	/** serialVersionUID */
	private static final long serialVersionUID = -3934593190573329338L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** lastName */
	private String lastName;
	/** firstName */
	private String firstName;
	/** email : identification mail address */
	private String email;
	/** collaborator role */
	private Role role;

	/** missions : the missions this collaborator is in charge of */
	@OneToMany(mappedBy = "collaborator")
	private Set<Mission> missions = new HashSet<Mission>();

	/** manager : the manager of this collaborator */
	@ManyToOne
	@JoinColumn(name = "managerID")
	private Collaborator manager;

	

	/** Constructeur
	 * Converteur CollaboratorDTO 
	 * @param collaboratorDTO
	 */
	public Collaborator(CollaboratorDTO collab) {
		super(collab.getUserName(), collab.getPassword(), collab.getRoles());
		this.id = collab.getId();
		this.lastName = collab.getLastName();
		this.firstName = collab.getFirstName();
		//this.setRole(collab.getRole());
		this.email = collab.getEmail();
		this.setManager(collab.getManager());
	}

	public void setManager(CollaboratorDTO managerDTO) {
		if (managerDTO != null) {
			this.manager = new Collaborator(managerDTO);
		}

	}
	public void setManager(Collaborator manager) {
		this.manager = manager;
	}
	
	/** Constructeur
	 * @param username
	 * @param password
	 * @param authorities
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param email
	 * @param role
	 * @param missions
	 * @param manager
	 */
	public Collaborator(String username, String password, Collection<? extends GrantedAuthority> authorities, int id,
			String lastName, String firstName, String email, Role role, Set<Mission> missions, Collaborator manager) {
		super(username, password, authorities);
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.role = role;
		this.missions = missions;
		this.manager = manager;
	}
	/**
	 * base arg constructor Constructeur
	 * 
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 */
	public Collaborator(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	/**
	 * No Args Constructeur
	 * 
	 */
	public Collaborator() {
		super("rep", "1111",Collaborator.BaseGrantedAuthorities());
	}
	
	private static HashSet<GrantedAuthority> BaseGrantedAuthorities() {
		HashSet<GrantedAuthority> auth =  new HashSet<GrantedAuthority>();
		return auth;
	}
}