package diginamic.gdm.security;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import diginamic.gdm.GDMRoles;
import diginamic.gdm.GDMRoutes;
import diginamic.gdm.GDMVars;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.RoleService;
import diginamic.gdm.services.implementations.CollaboratorServiceImpl;
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
@EnableGlobalMethodSecurity(securedEnabled = true)
public class GDMSecurityConfig {
		
	@Autowired
	private GDMAuthentication authProvider;
	@Autowired
	private RoleService roleService;
	@Autowired
	CollaboratorService userService;	

	@Bean
	public SecurityFilterChain springSecurityfilterChain(HttpSecurity http) throws Exception {
		
		roleService.saveAutorities();
		System.err.println("Filter Chain");
		http.authenticationProvider(authProvider) 
		.formLogin()
		//.loginPage(GDMVars.LOGINPAGE)// pour definir la login page
		.permitAll()
		;
		http.rememberMe().key("RememberMe").tokenValiditySeconds(GDMVars.TOKEN_LIFE);
		http.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		.maximumSessions(1)
		// in case of a new login, the existing session is closed
		.expiredSessionStrategy(event -> event.getSessionInformation().expireNow())
		;
		http.logout() 
		.invalidateHttpSession(true)
		.deleteCookies(GDMVars.SESSION_SESSION_COOKIE_NAME)
		;
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, GDMRoutes.SIGNUP).permitAll()
			.antMatchers(HttpMethod.GET, GDMRoutes.FAVICON).permitAll()
			
			.anyRequest().authenticated()
			;
		http.httpBasic();
			
		
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
	public void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth.authenticationProvider(authProvider);
	}
	
	
	


	
	
}
