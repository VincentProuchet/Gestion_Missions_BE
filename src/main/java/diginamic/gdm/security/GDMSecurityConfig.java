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
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import diginamic.gdm.services.CollaboratorService;
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
public class GDMSecurityConfig {
	
	private static String SESSION_SESSION_COOKIE_NAME ="JSESSIONID";
	private static String INDEX_PAGE = "/index";
	private static String LOGIN_PAGE = "/login";
	private static String LOGOUT_PAGE = "/login";
	@Autowired
	CollaboratorService collaboratorService;
	@Autowired
	private PasswordEncoder  passwordEncoder;
	
	
//	@Autowired
//	SecurityContextHolder securityContext;
//	@Autowired
//	RequestCache resquestCache;
	@Autowired
	AuthenticationEntryPoint entryPoint;
	@Autowired
	AuthenticationManager authManager;
	

	@Bean
	public SecurityFilterChain springSecurityfilterChain(HttpSecurity http) throws Exception {
		System.err.println("Filter Chain");
		http.authorizeRequests()
			.antMatchers("/signin").permitAll()
			.antMatchers("/refreshToken").permitAll()
			.antMatchers("/auth").permitAll()
			.antMatchers("/city/").permitAll()
			.anyRequest()			
			.authenticated();
			
			
			//.httpBasic(Customizer.withDefaults())
			
		http.authenticationManager(authManager) 
		// le login s'appelle 
		// par la route /login
			.formLogin()
			//.loginPage(LOGIN_PAGE)
			.permitAll()
			//.failureForwardUrl(LOGOUT_PAGE).permitAll();
			;
		
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.maximumSessions(1).expiredSessionStrategy(event -> event.getSessionInformation().expireNow())
			;
		http.logout() 
		// le logout 
		// s'appelle par la route /logout
		.invalidateHttpSession(true)
		//.logoutUrl(LOGOUT_PAGE).permitAll()
		.deleteCookies(SESSION_SESSION_COOKIE_NAME)
		;
		
		;
		return http.build();
	}
//	@Bean
//	 public void logIn(AuthenticationManagerBuilder auth){
//		System.out.println("I m in");
//		
//	}

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

				//.antMatchers(HttpMethod.GET, "/nature/")
		// .antMatchers(HttpMethod.GET)
		// .antMatchers(HttpMethod.POST)
		// .antMatchers(HttpMethod.PUT)
		// .antMatchers(HttpMethod.DELETE)
		;
	}



	
	
}
