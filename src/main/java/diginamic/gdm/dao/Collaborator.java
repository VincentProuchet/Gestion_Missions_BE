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
import javax.persistence.OneToOne;
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
	private static final long serialVersionUID = 1L;

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
	/** collaborator role */
	private Role role;

	/** missions : the missions this collaborator is in charge of */
	@OneToMany(mappedBy = "collaborator")
	private Set<Mission> missions = new HashSet<Mission>();

	/** manager : the manager of this collaborator */
	@ManyToOne
	@JoinColumn(name = "managerID")
	private Manager manager;

	/// Spring Security /////
	// everything under that point is here for the sake of spring Security
	/**
	 * userName credentials used for login no need to panic we can copie emails or
	 * anything here its a good practice since its would allow users to use anything
	 * to authenticate and still have an email or any other names
	 * can't be null so they are all a banana
	 */
	private String userName = "Banane";

	/** Indicate if an account is valid or not revoked */
	private boolean accountActive = true;

	private boolean accountBanned = false;

	private boolean accountExpired = false;
	/**
	 * to use for password regeneration users would have to ask for a new password
	 */
	private boolean passwordValid = true;
	
	/**
	 * Yheay I decided to separate access tokens from the UserDetails
	 * but we still need to hook these together
	 */
	@OneToOne
	private UserAccess access;
	
	/** authority  non "persistent" puisque determine par la classe */
	@Transient
	protected Set<GrantedAuthority> authority = new HashSet<>();
	
	{
		// this is to ensure that the authority is never null 
		// and has a proper value
		this.authority.add(new SimpleGrantedAuthority(this.getClass().getName()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		
		return this.authority;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
