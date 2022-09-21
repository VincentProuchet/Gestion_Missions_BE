package diginamic.gdm.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

import diginamic.gdm.dto.CollaboratorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents a Collaborator implement UserDetails from SpringSecurity
 * project Document
 * the differentiation between different types of user
 * is made by the list of Roles 
 * who are also used by spring security for userAccess 
 * @author Joseph
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Collaborator implements UserDetails {

	/** serialVersionUID */
	private static final long serialVersionUID = -2542617641751124157L;
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/** lastName */
	@Column(name = "last_name")
	private String lastName;
	/** firstName */
	@Column(name = "first_name")
	private String firstName;
	/** email : identification mail address */
	@Column(name = "email")
	private String email;
	 @OneToMany(mappedBy = "manager")
	    private Set<Collaborator> team = new HashSet<Collaborator>();

	/** userName */
	@Column(nullable = false,unique = true)
	private String username = "robert";
	/** password : remember to add security */
	@Column(name = "password")
	private String password = "patrick";
	/** isActive */
	@Column(name = "is_active")
	private boolean isActive = true;

	/**
	 * authorities Roles implements GrantedAuthority
	 */
	@ManyToMany
	@Fetch(FetchMode.JOIN)
	private Collection<Roles> authorities ;

	/** missions : the missions this collaborator is in charge of */
	@OneToMany(mappedBy = "collaborator", fetch = FetchType.LAZY)
	private Set<Mission> missions = new HashSet<Mission>();

	/** manager : the manager of this collaborator */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managerid")
	private Collaborator manager = null;

	/** Constructeur
	 * @param id
	 * @param lastName
	 * @param firstName
	 * @param email
	 * @param role
	 */
	public Collaborator(int id, String lastName, String firstName, String email, Roles role) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
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
		this.lastName = user.getLastName();
		this.firstName = user.getFirstName();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.email = user.getEmail();
		this.authorities = user.getAuthorities();
	}
	/**
	 * Constructeur used for mapping from the DTO
	 *  
	 * @param user
	 */
	public Collaborator(CollaboratorDTO user) {
		this.id = user.getId();
		this.lastName = user.getLastName();
		this.firstName = user.getFirstName();
		this.email = user.getEmail();
		this.authorities = user.getRoles();
		if(user.getManager()!= null) { // just in case
			this.manager = new Collaborator(user.getManager(),false);
		}
		
	}
	/**
	 * Constructeur used for map
	 * this one is made to avoid StackOverflow
	 * @param user
	 */
	public Collaborator(CollaboratorDTO user,boolean withManager) {
		this.id = user.getId();
		this.lastName = user.getLastName();
		this.firstName = user.getFirstName();
		this.email = user.getEmail();
		if(user.getManager()!= null && withManager) {// for stackOverFlow prevention
			this.manager = new Collaborator(user.getManager(),false);
		}
	}

	@Override
	public Collection<Roles> getAuthorities() {		
		return this.authorities;
	}
	
	/**
	 * This is to simplify the Authority attribution for Spring Security
	 * yhea it look complicated for what it does 
	 * its due to the Collection being and immutable
	 *  and we can't bypass that because of the UserDetail implementation 
	 * @param Roles authority
	 */
	public void addAuthorities(Roles... authority) {
		List<Roles> construct = new ArrayList<>();
		for (Roles role : authority) {
			
			construct.add(role);
		}
		System.err.println("adding autohorities to user " + this.getFirstName());
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

}
