package diginamic.gdm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class GDMAuthentication implements AuthenticationManager {

	UserDetailsService userManager;
	CollaboratorService collaboratorService;
	JdbcUserDetailsManager UserManager;
	private Collaborator collaboratorDAO;
	// SessionAuthenticationStrategy sessionStrategy;
	// RememberMeServices rememberMeServices;
	// ApplicationEventPublisher AEP;
	// AuthenticationSuccessHandler successHandler;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// if a security context already exist
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			return SecurityContextHolder.getContext().getAuthentication();
		}
		// if auth est null
		if (auth == null) {
			System.err.println("Bad Json");
			return null;
		}
		System.err.println(auth);
		String username = (String) auth.getPrincipal();
		
		if(username!=null) {
			Collaborator coll = (Collaborator) collaboratorService.loadUserByUsername(username);
			if (coll!=null) {
				String password = (String) auth.getCredentials();
				if(passwordEncoder.matches(password, coll.getPassword())) {
					UsernamePasswordAuthenticationToken userToken =  new UsernamePasswordAuthenticationToken(coll.getUsername(),null, coll.getAuthorities());
					return userToken;					
				}
			}
		}
		throw new BadCredentialsException("les informations de commptes sont incorrectes");
	}

}
