package diginamic.gdm.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dto.CollaboratorDTO;
import diginamic.gdm.vars.GDMVars;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents a Collaborator implement UserDetails from
 * SpringSecurity project Document the differentiation between different types
 * of user is made by the list of Roles who are also used by spring security for
 * userAccess
 * 
 * @author Joseph
 */
@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class Collaborator implements UserDetails {

	/** serialVersionUID */
	private static final long serialVersionUID = -2542617641751124157L;
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	/** lastName */
	@Column(name = "last_name")
	private String lastName ;
	/** firstName */
	@Column(name = "first_name")
	private String firstName;
	/** email : identification mail address */
	@Column(name = "email")
	private String email;
	@OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
	private Set<Collaborator> team = new HashSet<Collaborator>();

	/** userName */
	@Column(nullable = false, unique = true)
	private String username;
	/** password : remember to add security */
	@Column(name = "password", nullable = false)
	private String password ;
	/** isActive */
	@Column(name = "is_active")
	private boolean isActive = false;

	/**
	 * authorities Roles implements GrantedAuthority
	 */
	@ManyToMany
	@Fetch(FetchMode.JOIN)
	private Collection<Roles> authorities;

	/** missions : the missions this collaborator is in charge of */
	@OneToMany(mappedBy = "collaborator", fetch = FetchType.LAZY)
	private Set<Mission> missions = new HashSet<Mission>();

	/** manager : the manager of this collaborator */
	@JoinColumn(name = "managerid")
//	@ManyToOne(fetch = FetchType.LAZY) add to change it because it messed up the authentication 
	// the risk of stack overflow is contained by DTO conversion
	@ManyToOne
	private Collaborator manager = null;

	/**
	 * Constructeur
	 * 
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param email
	 * @param role
	 */
	public Collaborator(int id, String lastName, String firstName, String email, Roles role) {
		this.id = id;
		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.email = email;
		this.authorities.add(role);

		// id, lastName, firstName, email, "", role, null, null
	}

	/**
	 * Constructeur used for map
	 * 
	 * @param user
	 */
	public Collaborator(Collaborator user) {
		this.id = user.getId();
		this.setLastName(user.getLastName());
		this.setFirstName(user.getFirstName());
		this.setUsername(user.getUsername());
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.email = user.getEmail();
		this.authorities = user.getAuthorities();
		if (user.getManager() != null) {
			this.manager = new Collaborator(user.getManager(), false);
		}
	}

	/**
	 * Constructeur used for map this one exist to prevent stackOverflow
	 * 
	 * @param user
	 */
	public Collaborator(Collaborator user, boolean withManager) {
		this.id = user.getId();
		this.setLastName(user.getLastName());
		this.setFirstName( user.getFirstName());
		this.setUsername(user.getUsername());
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.email = user.getEmail();
		this.authorities = user.getAuthorities();
		if (user.getManager() != null && withManager) {
			this.manager = new Collaborator(user.getManager(), false);
		}
	}

	/**
	 * Constructeur used for mapping from the DTO
	 * 
	 * @param user
	 */
	public Collaborator(CollaboratorDTO user) {
		this.id = user.getId();
		this.setLastName(user.getLastName());
		this.setFirstName(user.getFirstName());
		this.setUsername(user.getUsername());
		this.email = user.getEmail().strip();
		this.authorities = user.getRoles();
		if (user.getManager() != null) { // just in case
			this.manager = new Collaborator(user.getManager(), false);
		}

	}

	/**
	 * Constructeur used for map this one is made to avoid StackOverflow
	 * 
	 * @param user
	 */
	public Collaborator(CollaboratorDTO user, boolean withManager) {
		this.id = user.getId();
		this.setLastName(user.getLastName());
		this.setFirstName(user.getFirstName());
		this.email = user.getEmail();
		if (user.getManager() != null && withManager) {// for stackOverFlow prevention
			this.manager = new Collaborator(user.getManager(), false);
		}
	}

	@Override
	public Collection<Roles> getAuthorities() {
		return this.authorities;
	}

	/**
	 * This is to simplify the Authority attribution for Spring Security yhea it
	 * look complicated for what it does its due to the Collection being and
	 * immutable and we can't bypass that because of the UserDetail implementation
	 * this take authorities already the user already have and add it the one passed
	 * as parameters * @param Roles authority
	 */
	public void setRoles(Role... authority) {
		Set<Roles> construct = new HashSet<Roles>();
		for (Roles role : this.authorities) {
			construct.add(role);
		}

		for (Role role : authority) {

			construct.add(new Roles(role));
		}
		this.authorities = construct;
	}

	/**
	 * This is to simplify the Authority attribution for Spring Security yhea it
	 * look complicated for what it does its due to the Collection being and
	 * immutable and we can't bypass that because of the UserDetail implementation
	 * this take authorities already the user already have and add it the one passed
	 * as parameters * @param Roles authority
	 */
	public void removeAuthorities(Roles authority) {
		Set<Roles> construct = new HashSet<Roles>();
		// we reconstruct the authorithy collection
		for (Roles role : this.authorities) {
			// as long as the authority to add is NOT the one we want to remove
			if (!role.getLabel().equals(authority.getLabel())) {
				// its add to the new collection
				construct.add(role);
			}
		}
		// and we ovewrite the new
		this.authorities = construct;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isActive;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isActive;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public Collaborator(int id, String lastName, String firstName, String email, Set<Collaborator> team,
			String username, String password, boolean isActive, Collection<Roles> authorities, Set<Mission> missions,
			Collaborator manager) {
		super();
		this.id = id;
		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.email = email;
		this.team = team;
		this.setUsername(username);
		this.password = password;
		this.isActive = isActive;
		this.authorities = authorities;
		this.missions = missions;
		if (manager != null)
			this.manager = new Collaborator(manager, false);
	}
	
	/**
	 * SETTER
	 * @param name
	 */
	public void setFirstName(String name) {	
		name =name.strip();
		this.firstName = name;
	}
	/**
	 * SETTER
	 * @param name
	 */
	public void setLastName(String name) {	
		name =name.strip();
		this.lastName = name;
	}
	/**
	 * SETTER
	 * @param name
	 */
	public void setUsername(String name) {
		name =name.strip();
		this.username = name;
	}
	
	/**
	 * test if a string is a valid name for the collaborator
	 * @param name
	 * @return
	 */
	public static boolean isValidFirstName(String name) {
		return name.matches(GDMVars.REGEX_HUMANS_FIRST_NAMES);
	}
	/**
	 * test if a string is a valid name for the collaborator
	 * @param name
	 * @return
	 */
	public static boolean isValidLastName(String name) {
		return name.matches(GDMVars.REGEX_HUMANS_LAST_NAMES);
	}
	/**
	 * test if a string is a valid name for the collaborator
	 * @param name
	 * @return
	 */
	public static boolean isValidUserName(String name) {
		return name.matches(GDMVars.REGEX_USERNAMES);
	}
	/**
	 * test if a string is a valid email format
	 * @param name
	 * @return
	 */
	public static boolean isValidEmail(String name) {		
		return name.matches(GDMVars.REGEX_EMAIL2);
	}

}
