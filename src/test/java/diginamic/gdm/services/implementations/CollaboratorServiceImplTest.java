package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.utilities.testTools;

@SpringBootTest
@ActiveProfiles("Test")
public class CollaboratorServiceImplTest {
	
	@Autowired
	private CollaboratorServiceImpl service;
	@Autowired 
	private testTools tools;
	
	private String baseName = "collsrvImplTest";
	private String oneMinus = "coll-srvTest";
	private String twoMinus = "coll--srvTest";
	private String tooMuchMinus = "coll---srvTest";
	private String tooMuchSpaces = "colls rv-test";
	private String tooMuchSpecialChars = "&~#'{([|`_\\^¨°)]+=}$£¤%*,?;.:/!§";
	
	
	
	@Test
	public void create() {
		System.err.println("valid ");
		Collaborator collaboratorValid  = tools.giveMeJustACollaborator(baseName);
		System.err.println(collaboratorValid.getLastName());
		assertDoesNotThrow(()->this.service.create(collaboratorValid));
		System.err.println("invalid same username");
		Collaborator collaboratorInvalid  = tools.giveMeJustACollaborator(baseName);
		System.err.println(collaboratorInvalid.getLastName());
		assertThrows(BadRequestException.class, ()->this.service.create(collaboratorInvalid));
		System.err.println("valid one minus ");
		Collaborator collaboratorValid2  = tools.giveMeJustACollaborator(baseName+1);
		collaboratorValid2.setLastName(oneMinus);
		System.err.println(collaboratorValid2.getLastName());
		assertDoesNotThrow(()->this.service.create(collaboratorValid2));

		System.err.println("too much minus ");
		Collaborator collaboratorInvalid2  = tools.giveMeJustACollaborator(baseName+2);
		collaboratorInvalid2.setLastName(tooMuchMinus);
		System.err.println(collaboratorInvalid2.getLastName());
		assertThrows(BadRequestException.class,()->this.service.create(collaboratorInvalid2));
		
		System.err.println("too much space");
		Collaborator collaboratorInvalid3  = tools.giveMeJustACollaborator(tooMuchSpaces);
		collaboratorInvalid3.setLastName(tooMuchSpaces);
		System.err.println(collaboratorInvalid3.getLastName());
		assertThrows(BadRequestException.class,()->this.service.create(collaboratorInvalid3));
		
		System.err.println(" special char ");
		Collaborator collaboratorInvalid4  = tools.giveMeJustACollaborator(baseName+4);
		collaboratorInvalid4.setLastName(tooMuchSpecialChars);
		System.err.println(collaboratorInvalid4.getLastName());
		assertThrows(BadRequestException.class,()->this.service.create(collaboratorInvalid4));		
		
	}
	@Test
	private void read() {
		
	}
	@Test
	private void update() {
		
	}
	@Test
	private void delete() {
		
	}
	@Test
	private void list() {
		
	}

}
