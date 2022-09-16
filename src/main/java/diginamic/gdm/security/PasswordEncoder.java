package diginamic.gdm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
public class PasswordEncoder {
	@Bean
	public BCryptPasswordEncoder bcCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
		
		
	}
	@Primary
	public org.springframework.security.crypto.password.PasswordEncoder noCrypt() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	
}
