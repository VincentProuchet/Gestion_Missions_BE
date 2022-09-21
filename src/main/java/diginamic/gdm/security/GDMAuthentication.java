package diginamic.gdm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class GDMAuthentication implements AuthenticationProvider {

	@Autowired
	private CollaboratorService collaboratorService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// if a security context already exist
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			System.out.println("Authenticated");
			return SecurityContextHolder.getContext().getAuthentication();
		}
		String username = (String) auth.getPrincipal();
		
		if(username!=null) {
			Collaborator coll = (Collaborator) collaboratorService.loadUserByUsername(username);
			if (coll!=null) {
				if(!coll.isActive()) {
					throw new BadCredentialsException("le compte n'est pas activé");
				}
				String password = (String) auth.getCredentials();
				if(passwordEncoder.matches(password, coll.getPassword())) {
					UsernamePasswordAuthenticationToken userToken =  new UsernamePasswordAuthenticationToken(coll.getUsername(),null, coll.getAuthorities());
					System.err.println("authenticated with granted authorities");
					for (GrantedAuthority authority : userToken.getAuthorities()) {
						System.err.println(authority.getAuthority());
					}					
					return userToken;					
				}
			}
		}		
		throw new BadCredentialsException("les informations de compte sont incorrectes");
	}

	@Override
	public boolean supports(Class<?> authentication) {		
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
