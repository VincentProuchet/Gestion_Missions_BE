package diginamic.gdm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import diginamic.gdm.GDMRoutes;
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
@EnableWebSecurity
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
		//.anyRequest().permitAll();
			//.antMatchers(HttpMethod.POST,"/"+GDMRoutes.SIGNUP).permitAll()
			//.antMatchers("/"+GDMRoutes.ERRORS).permitAll()
				
			//.authenticated()
			;
			
		http.authenticationProvider(authProvider) 
			.formLogin()
			//.loginPage(LOGIN_PAGE) // pour definir la login page
			.permitAll()
			;
		
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
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
//		.antMatchers(HttpMethod.GET)
//		.antMatchers(HttpMethod.POST)
//		.antMatchers(HttpMethod.PUT)
//		.antMatchers(HttpMethod.DELETE)
		;
	}



	
	
}
