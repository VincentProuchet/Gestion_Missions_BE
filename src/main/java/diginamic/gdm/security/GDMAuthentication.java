package diginamic.gdm.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GDMAuthentication implements AuthenticationManager{

	DaoAuthenticationProvider AuthProvider;
	JdbcUserDetailsManager userManager;
	//SessionAuthenticationStrategy sessionStrategy;
	//SecurityContextHolder securityContextHolder;
	//RememberMeServices rememberMeServices;
	//ApplicationEventPublisher AEP;
	//AuthenticationSuccessHandler successHandler;
	
		
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		System.err.println(auth);
		/**
		 * you get something like this
		 * and I found no documentation that give it to you
		 * UsernamePasswordAuthenticationToken 
		 * [
		 * Principal=robert ,
		 * Credentials=[PROTECTED],
		 *  Authenticated=false,
		 *   Details=WebAuthenticationDetails [RemoteIpAddress=127.0.0.1, SessionId=55BC0E4D92812BD4C30F69D203ABD273],
		 *    Granted Authorities=[]]
		 
		 */
		 SecurityContext context = SecurityContextHolder.createEmptyContext();
		auth = AuthProvider.authenticate(auth); 
		if(auth == null ) {
			System.err.println("Bad Json");
			return null;
		}
		
		if(auth.getPrincipal().equals("lui")|| auth.getCredentials().equals("1234")) {
			
			
			
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);
			
			
			// get a connection grant token
			// from security Service
			System.out.println("Logged");
			return auth;
		}
		else {
			SecurityContextHolder.clearContext();

			System.err.println("login invalid");
			return null;
		}
		// send back the grant the auth
		
	}



}
