package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	private String validemail = "collsrvimpltest@dffd.com";
	private String invalidBaseName = "collsrv ImplTest";
	
	@Test
	public void create() {
		String name = baseName + "create";
		Collaborator collaboratorInvalid = tools.giveMeJustACollaborator(name);

		collaboratorInvalid.setFirstName(invalidBaseName);
		// invalid firstName
		assertThrows(BadRequestException.class, () -> this.service.create(collaboratorInvalid));
		collaboratorInvalid.setFirstName(baseName);
		collaboratorInvalid.setLastName(invalidBaseName);
		// invalid last name
		assertThrows(BadRequestException.class, () -> this.service.create(collaboratorInvalid));
		collaboratorInvalid.setLastName(baseName);
		collaboratorInvalid.setEmail(invalidBaseName);
		// invalid email
		assertThrows(BadRequestException.class, () -> this.service.create(collaboratorInvalid));
		collaboratorInvalid.setEmail(validemail);
		collaboratorInvalid.setUsername(invalidBaseName);
		// invalid username
		assertThrows(BadRequestException.class, () -> this.service.create(collaboratorInvalid));
		collaboratorInvalid.setUsername(name);
		// we now have a valid user
		assertDoesNotThrow(() -> this.service.create(collaboratorInvalid));
		// if we try to push it again it fail because duplicates are forbiden
		assertThrows(BadRequestException.class, () -> this.service.create(collaboratorInvalid));
		collaboratorInvalid.setUsername(name + 1);
		assertDoesNotThrow(() -> this.service.create(collaboratorInvalid));

	}

	@Test
	public void read() throws Exception {
		String name = baseName + "read";
		assertThrows(BadRequestException.class, () -> this.service.read(0));
		assertThrows(BadRequestException.class, () -> this.service.read(Integer.MAX_VALUE));
		Collaborator collaborator1 = tools.giveMeJustACollaborator(name);
		Collaborator collaborator = this.service.create(collaborator1);
		assertDoesNotThrow(() -> this.service.read(collaborator.getId()));
		
	}

	@Test
	public void update() throws Exception {
		String name = baseName + "update";
		assertThrows(BadRequestException.class, () -> this.service.read(0));
		assertThrows(BadRequestException.class, () -> this.service.read(Integer.MAX_VALUE));
		Collaborator collaborator1 = tools.giveMeJustACollaborator(name);
		Collaborator collaborator = this.service.create(collaborator1);
		// we check that its in persistence
		assertDoesNotThrow(()-> this.service.read(collaborator.getId()));
		collaborator.setFirstName(invalidBaseName);
		// invalid firstName
		assertThrows(BadRequestException.class, () -> this.service.update(collaborator.getId(),collaborator));
		collaborator.setFirstName(baseName);
		collaborator.setLastName(invalidBaseName);
		// invalid last name
		assertThrows(BadRequestException.class, () -> this.service.update(collaborator.getId(),collaborator));
		collaborator.setLastName(baseName);
		collaborator.setEmail(invalidBaseName);
		// invalid email
		assertThrows(BadRequestException.class, () -> this.service.update(collaborator.getId(),collaborator));
		collaborator.setEmail(validemail);
		collaborator.setUsername(invalidBaseName);
		// invalid username
		assertThrows(BadRequestException.class, () -> this.service.update(collaborator.getId(),collaborator));
		collaborator.setUsername(name);
		// we now have a valid user
		assertDoesNotThrow(() -> this.service.update(collaborator.getId(),collaborator));
		
	}

	/**
	 * Delete doesn't exit for this service
	 * @throws Exception
	 */
	@Test
	private void delete() throws Exception {
		String name = baseName + "delete";
		assertThrows(BadRequestException.class, () -> this.service.read(0));
		assertThrows(BadRequestException.class, () -> this.service.read(Integer.MAX_VALUE));
		Collaborator collaborator1 = tools.giveMeJustACollaborator(name);
		Collaborator collaborator = this.service.create(collaborator1);
		assertDoesNotThrow(() -> this.service.read(collaborator.getId()));
		// delete 
		
		// and check
		//assertThrows(BadRequestException.class, () -> this.service.read(collaborator.getId()));
		
		
	}

	@Test
	public void list() throws Exception {
		int quantities = this.service.list().size();
		String name = baseName + "list";
		assertThrows(BadRequestException.class, () -> this.service.read(0));
		assertThrows(BadRequestException.class, () -> this.service.read(Integer.MAX_VALUE));
		Collaborator collaborator1 = tools.giveMeJustACollaborator(name);
		Collaborator collaborator = this.service.create(collaborator1);
		assertDoesNotThrow(() -> this.service.read(collaborator.getId()));
		// list should have one more
		assertEquals(quantities+1, this.service.list().size());
	}

	@Test
	public void loadByUserName() throws BadRequestException {
		String name = baseName + "loadByUs";
		// not created
		assertThrows(UsernameNotFoundException.class, () -> this.service.loadUserByUsername(name));
		Collaborator collaborator1 = tools.giveMeJustACollaborator(name);
		assertThrows(UsernameNotFoundException.class, () -> this.service.loadUserByUsername(collaborator1.getUsername()));
		//creation 
		assertDoesNotThrow(()-> this.service.create(collaborator1));
		// should be there
		assertDoesNotThrow(() -> this.service.loadUserByUsername(collaborator1.getUsername()));
	}

	@Test
	public void findByUserName() throws BadRequestException {
		String name = baseName + "findByUs";
		// not created
		assertThrows(UsernameNotFoundException.class, () -> this.service.findByUserName(name));
		Collaborator collaborator1 = tools.giveMeJustACollaborator(name);
		assertThrows(UsernameNotFoundException.class, () -> this.service.findByUserName(collaborator1.getUsername()));
		//creation 
		assertDoesNotThrow(()->this.service.create(collaborator1));
		// should be there
		assertDoesNotThrow(() -> this.service.loadUserByUsername(collaborator1.getUsername()));
	}
}
