package diginamic.gdm.services.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import diginamic.gdm.dao.Administrator;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Manager;
import lombok.NoArgsConstructor;
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails{
	/** serialVersionUID */
	private static final long serialVersionUID = 4046102586300788021L;
	private String userName;
	private String password;
	private boolean isActive;
	private List<GrantedAuthority> authorities =new ArrayList<GrantedAuthority>();

	public UserDetailsImpl(Collaborator user) {
		System.err.println("found");
		System.err.println("making a Collaborator");
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.authorities.add(new SimpleGrantedAuthority(user.getClass().getName()));
	}
	public UserDetailsImpl(Administrator user) {
		System.err.println("found");
		System.err.println("making an Administror");
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.authorities.add(new SimpleGrantedAuthority(user.getClass().getName()));
	}
	public UserDetailsImpl(Manager user) {
		System.err.println("found");
		System.err.println("making a Manager");
		System.err.println("found");
		System.err.println("making a Collaborator");
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.isActive = user.isActive();
		this.authorities.add(new SimpleGrantedAuthority(user.getClass().getName()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

}
