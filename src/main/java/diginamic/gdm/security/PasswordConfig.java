package diginamic.gdm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class PasswordConfig {
	
	@Bean
	public BCryptPasswordEncoder bcCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	

}
