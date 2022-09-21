package diginamic.gdm.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.Collaborator;

@SpringBootTest
public class AccountControllerTest {

	@Autowired
	private AccountController controller;
	
	@Test
	void signup() {
		
		
		Collaborator coll = new Collaborator();
		coll.setUsername("mario");
		coll.setPassword("1111");
		coll.setManager(coll);
		coll.addAuthorities(Role.ADMIN);
		coll.addAuthorities(Role.COLLABORATOR);
		
		
		controller.signup(coll);
		// TODO not really a test
		assert(true);
	}

}
