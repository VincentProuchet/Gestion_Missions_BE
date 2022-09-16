package diginamic.gdm.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class Collaborator implements UserDetails {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -2542617641751124157L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/** lastName */
	private String lastName;
	/** firstName */
	private String firstName;
	/** email : identification mail address */
	private String email;
	
	/** userName */
	private String userName;
	/** password : remember to add security */
	private String password;
	/** isActive */
	private boolean isActive = true;
	
	/** authorities */
	@Transient
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	
	/** collaborator role */
	private Role role;

	/** missions : the missions this collaborator is in charge of */
	@OneToMany(mappedBy = "collaborator",fetch = FetchType.LAZY)
	private Set<Mission> missions = new HashSet<Mission>();

	/** manager : the manager of this collaborator */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managerID")
	private Collaborator manager = null;

	public Collaborator(int id, String lastName,String firstName, String email, Role role ) {
		System.err.println("making a collaborator");
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.role = role ;
		
		//id, lastName, firstName, email, "", role, null, null
	}
	/** Constructeur
	 * used for map
	 * @param user
	 */
	public Collaborator(Collaborator user) {
		this.id = user.getId();
		this.lastName = user.getLastName();
		this.firstName =user.getFirstName();
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.email =user.getEmail();
		this.role = user.getRole() ;
		this.addAuthorities(this.role.toString());
	}
	
	
	
	/**MAde ONLY to populate the object with mock data
	 * and testing 
	 * Must be deleted before production
	 * 
	 */
	public void giveMeMockData() {
		this.id = 1;
		this.lastName = "vincent";
		this.firstName = "prouchet";
		this.userName = "vin";
		this.password = "1111";
		this.email = "qsfgqf@hotmail.com";
		this.role = Role.ADMIN ;
		this.isActive = true;
		this.addAuthorities(this.role.toString());
	}
	
	
	
	
	
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.authorities.size()<1) {
			this.authorities.add(new SimpleGrantedAuthority(this.getClass().getName()));
		}
		return authorities;
	}
	public void  addAuthorities(String authority) {
		this.authorities.add(new SimpleGrantedAuthority(authority));
	}
	
	

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {

		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isActive;	}

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
