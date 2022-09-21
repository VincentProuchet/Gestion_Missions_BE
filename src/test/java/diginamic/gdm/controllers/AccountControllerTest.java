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
		
//		
//				0,
//				"Patrick",
//				"Robert",
//				"PRobert@hotmail.com",
//				[null],
//				"mario",
//				"1111",
//				1,
//				[],
//				
				
				//);
		
	}
	
	
}
