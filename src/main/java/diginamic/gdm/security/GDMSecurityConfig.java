package diginamic.gdm.security;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import diginamic.gdm.GDMRoles;
import diginamic.gdm.GDMRoutes;
import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.RoleService;
import lombok.AllArgsConstructor;

/**
 * Security Configuration this is where we declare
 * 
 * @Beans related to security and how the spring security will accept or reject
 *        request
 * 
 * @author Vincent
 *
 */
@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = false, prePostEnabled = true)
public class GDMSecurityConfig {
	


	@Autowired
	AuthenticationEntryPoint entryPoint;
	@Autowired
	GDMAuthentication authProvider;
	@Autowired
	RoleService roleService;
	

	@Bean
	public SecurityFilterChain springSecurityfilterChain(HttpSecurity http) throws Exception {
		
		roleService.saveAutorities();
		System.err.println("Filter Chain");
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST,"/"+GDMRoutes.SIGNUP).permitAll()
			.antMatchers("/"+GDMRoutes.ERRORS).permitAll()
				
			//.authenticated()
			;
			
		http.authenticationProvider(authProvider) 
			.formLogin()
			//.loginPage(LOGIN_PAGE) // pour definir la login page
			.permitAll()
			;
		
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.maximumSessions(1)
			// in case of a new login, the existing session is closed
			.expiredSessionStrategy(event -> event.getSessionInformation().expireNow())
			;
		http.logout() 
		.invalidateHttpSession(true)
		;
		
		return http.build();
	}

	/**
	 * this is where we configure the method and path that can be accessed from
	 * outside the first commit will open everything its head is Security OpenAll
	 * 
	 * @return
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		System.err.println("webignoring");
		return (web) -> web.ignoring()
				// il FAUT mettre le slash avant
		.antMatchers("/"+GDMRoutes.SIGNUP)
		.antMatchers(HttpMethod.GET)
		.antMatchers(HttpMethod.POST)
		.antMatchers(HttpMethod.PUT)
		.antMatchers(HttpMethod.DELETE)
		;
	}



	
	
}