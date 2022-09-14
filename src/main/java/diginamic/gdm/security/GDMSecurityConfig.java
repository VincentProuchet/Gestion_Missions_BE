package diginamic.gdm.security;

import java.security.Security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
public class GDMSecurityConfig {
	@Autowired
	CollaboratorService collaboratorService;
	AuthenticationEntryPoint entryPoint;
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((auth) -> 
		auth.antMatchers("/**").permitAll()
		// .antMatchers("/signin/").permitAll()
		// .antMatchers("/auth").permitAll()
		// .antMatchers("refreshToken/").permitAll()
//				.anyRequest().permitAll()
		)
		.exceptionHandling()
		.authenticationEntryPoint(entryPoint)
		.and()
		.httpBasic(Customizer.withDefaults())
		// .formLogin()
//	.authorizeExchange(
//			(autorize)->
//			autorize.anyExchange().permitAll()
//			.pathMatchers(HttpMethod.POST,"/auth","refreshToken","signin").permitAll()
//			
//			)

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
		if (true) {
			return (web) -> {
			};

		}

		return (web) -> web.ignoring().antMatchers(HttpMethod.POST, "login").antMatchers(HttpMethod.GET)
				.antMatchers(HttpMethod.PUT).antMatchers(HttpMethod.DELETE);
	}

	

	

	/*
	 * @Bean public void collaborators(AuthenticationManagerBuilder auth) {
	 * Collaborator user = new Collaborator(); user.setUserName("franck");
	 * user.password("password");
	 * 
	 * 
	 * collaboratorService.create(user);
	 * 
	 * }
	 */

 
}
