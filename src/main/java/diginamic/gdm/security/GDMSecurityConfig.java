package diginamic.gdm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

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
public class GDMSecurityConfig {
	@Autowired
	CollaboratorService collaboratorService;
	AuthenticationEntryPoint entryPoint;
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.err.println("Filter Chain");
		http.authorizeHttpRequests((auth) ->
		auth
		.antMatchers("signin").permitAll()
		.antMatchers("refreshToken").permitAll()
		.antMatchers(HttpMethod.POST, "/auth/").permitAll()
		.antMatchers(HttpMethod.GET,"/city/").permitAll()
		//auth.antMatchers("/").permitAll()
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
		if (false) {
			return (web) -> {
			};

		}

		return (web) -> web.ignoring()
				// il FAUT mettre le slash avant
				
				.antMatchers(HttpMethod.GET,"/nature/")
				//.antMatchers(HttpMethod.GET,"/city/")
				//.antMatchers(HttpMethod.POST, "/signin/")
				//.antMatchers(HttpMethod.GET)
				//.antMatchers(HttpMethod.PUT).antMatchers(HttpMethod.DELETE)
				;
	}

	
}
