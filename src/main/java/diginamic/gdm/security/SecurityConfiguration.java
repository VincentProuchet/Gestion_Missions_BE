package diginamic.gdm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import diginamic.gdm.dao.Administrator;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Manager;
import diginamic.gdm.services.implementations.UserDetailsImpl;

@Configuration
public class SecurityConfiguration {

	private static final String ADMIN = Administrator.class.getName();
	private static final String MANAGER = Manager.class.getName();
	private static final String COLLABORATOR = Collaborator.class.getName();
	//private static final String USER = UserDetailsImpl.class.getName();
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	protected DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> configure1(AuthenticationManagerBuilder auth) throws Exception {
		 return auth.userDetailsService(userDetailsService);
	}

	@Bean
	protected HttpSecurity securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/nature").hasRole(ADMIN)
			.antMatchers("/expense/type").hasAnyRole(ADMIN, COLLABORATOR, MANAGER)
			.antMatchers("/city").permitAll()
			.and().formLogin();
		return http;
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	
	
}
