package diginamic.gdm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import diginamic.gdm.dao.Role;

@Configuration
public class SecurityConfiguration {

	//private static final String ADMIN = Administrator.class.getName();
	//private static final String MANAGER = Manager.class.getName();
	//private static final String COLLABORATOR = Collaborator.class.getName();
	private static final String ADMIN = Role.ADMIN.LABEL;
		private static final String MANAGER = Role.MANAGER.LABEL;
		private static final String COLLABORATOR = Role.COLLABORATOR.LABEL;
	//private static final String USER = UserDetailsImpl.class.getName();
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	protected DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> configure1(AuthenticationManagerBuilder auth) throws Exception {
		 return auth.userDetailsService(userDetailsService);
	}

	@Bean
	public SecurityFilterChain springSecurityfilterChain(HttpSecurity http) throws Exception {
		System.err.println("Filter Chain");
		http.authorizeRequests()
			.antMatchers("/nature").hasRole(ADMIN)
			.antMatchers("/expense/type").hasAnyRole(ADMIN, COLLABORATOR, MANAGER)
			.antMatchers("/city").permitAll()
			.and()
			.formLogin();
		
								
//			http.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//				.maximumSessions(1).expiredSessionStrategy(event -> event.getSessionInformation().expireNow())
//				;
//			http.logout() 
//			// le logout 
//			// s'appelle par la route /logout
//			.invalidateHttpSession(true)
//			//.logoutUrl(LOGOUT_PAGE).permitAll()
			
		
			;
			
			;
			return http.build();
		
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	
	
}
