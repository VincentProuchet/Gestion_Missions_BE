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
	CollaboratorService collaboratorService;
	
	@Bean
	public BCryptPasswordEncoder bcCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		System.err.println("auth provider");
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		// on lui dit comment gérer les compte utilisateurs et c'est l'extension de
		// l'interface UserDetail service
		authenticationProvider.setUserDetailsService(collaboratorService);
		// on doit lui indiquer quel alghorythme doit être utilisé pour les mots de
		// passes
		authenticationProvider.setPasswordEncoder(PasswordConfig.passwordEncoder());
		return authenticationProvider;
	}

}
