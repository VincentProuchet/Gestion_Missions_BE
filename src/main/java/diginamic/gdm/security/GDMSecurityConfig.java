package diginamic.gdm.security;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import lombok.AllArgsConstructor;
/**
 * Security Configuration
 * this is where we declare  
 * @Beans related to security 
 * and how the spring security will accept or reject request
 *   
 * @author Vincent
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class GDMSecurityConfig extends SecurityAutoConfiguration {
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	
	/**
	 * this is where we configure 
	 * the method and path that can be accessed from outside
	 * the first commit will open everything 
	 * its head is Security OpenAll 
	 * @return 
	 */	
	  @Bean
	  public WebSecurityCustomizer webSecurityCustomizer() { return (web) ->
	  web .ignoring() .antMatchers(HttpMethod.POST)
	  .antMatchers(HttpMethod.GET) .antMatchers(HttpMethod.PUT)
	  .antMatchers(HttpMethod.DELETE)
	  
	  ; }
	 
	
}
