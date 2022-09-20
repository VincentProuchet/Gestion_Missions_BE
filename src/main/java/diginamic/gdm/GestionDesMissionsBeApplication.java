package diginamic.gdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionDesMissionsBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDesMissionsBeApplication.class, args);
	}

}
