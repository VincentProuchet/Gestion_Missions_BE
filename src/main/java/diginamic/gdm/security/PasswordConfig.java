package diginamic.gdm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class PasswordConfig {

	@Bean
	public BCryptPasswordEncoder bcCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}





}
