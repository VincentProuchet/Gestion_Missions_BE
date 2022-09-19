package diginamic.gdm.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import diginamic.gdm.GDMRoutes;
import diginamic.gdm.dao.Role;
import diginamic.gdm.services.CollaboratorService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	//private static final String ADMIN = Administrator.class.getName();
	//private static final String MANAGER = Manager.class.getName();
	//private static final String COLLABORATOR = Collaborator.class.getName();
	private static final String ADMIN = Role.ADMIN.LABEL;
		private static final String MANAGER = Role.MANAGER.LABEL;
		private static final String COLLABORATOR = Role.COLLABORATOR.LABEL;

	
	@Autowired
	private CollaboratorService collaboratorService;
	@Autowired
	private JWTEntryPoint jwtEntryPoint;
	@Autowired
	private JWTTokenUtils jwtTokenUtils;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Bean
//	protected DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> configure1(AuthenticationManagerBuilder auth) throws Exception {
//		 return auth.userDetailsService(collaboratorService);
//	}

	@Bean
	public SecurityFilterChain springSecurityfilterChain(HttpSecurity http) throws Exception {
		System.err.println("Filter Chain");
		http.authorizeRequests()
			.antMatchers("/"+GDMRoutes.NATURE).hasAnyAuthority(ADMIN)
			.antMatchers("/"+GDMRoutes.EXPENSE+"/"+GDMRoutes.TYPE).hasAnyAuthority(ADMIN, COLLABORATOR, MANAGER)
			.antMatchers("/"+GDMRoutes.EXPENSE).hasAnyAuthority(ADMIN, COLLABORATOR, MANAGER)
			.antMatchers("/"+GDMRoutes.MISSION).hasAnyAuthority(ADMIN, COLLABORATOR, MANAGER)
			.antMatchers("/"+GDMRoutes.CITY).permitAll()
			
			.and()
			
			
			;
		http.exceptionHandling()
		.authenticationEntryPoint(jwtEntryPoint)
		
		;
		http.formLogin()		
		;
		http.httpBasic(Customizer.withDefaults())
			
			;
		JWTFilter jwtFilter = new JWTFilter(collaboratorService, jwtTokenUtils, "Authorization");
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
					
			;
			return http.build();
		
	}
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.antMatchers(POST, "/"+GDMRoutes.SIGNUP)
				.antMatchers(GET, "/"+GDMRoutes.AUTH+"/**")
				.antMatchers(POST, "/"+GDMRoutes.SIGNIN)
				.antMatchers(POST)
				.antMatchers(GET)
				.antMatchers(HttpMethod.PATCH)
				.antMatchers(HttpMethod.DELETE)
		;
	}

	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		// on lui dit comment gére les compte utilisateurs et c'est l'extension de
		// l'interface UserDetail service
		authenticationProvider.setUserDetailsService(collaboratorService);
		// on lui dit comment les mots de passes sont crypté
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("vin").password("{noop}1111").roles(Role.ADMIN.LABEL);
	}

	
	
}
