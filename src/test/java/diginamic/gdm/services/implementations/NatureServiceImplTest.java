package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.ExpenseTypeRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.CityService;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.RoleService;
import diginamic.gdm.utilities.testTools;

// ce sont des tests d integrations et pas des tests unitaires... je sais pas trop comment tester plus petit ici
// ben en fait sur les servicesImpl  tu peux pas, il te faut un contexte Spring  
/**
 * these test are Only for Nature We dont test the dependency with other
 * Entities nor Services those kind of test belong to a separate class
 * 
 * @author Vincent 
 * @author Joseph // ton travail était une excellente base pour le refactoring de cette Junit,
 * 					 tu est déjà un bon programmeur	et tu ne peu que t'améliorer 
 * 
 */
@SpringBootTest
@ActiveProfiles("test")
class NatureServiceImplTest {

	/** natureServiceImpl
	 * Well this is supposed to be the class testing it 
	 *
	 */
	@Autowired
	private NatureServiceImpl natureService;
	@Autowired // we need this. some test need us to pass around the NatureService
	private NatureRepository natureRepository;
	/** collabService 
	 * to test :
	 *  - isNatureInUse 
	 */
	@Autowired
	private RoleService roleSrv;
	// and all repository
	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@Autowired
	private MissionRepository missionRepository;
	@Autowired
	private ExpenseTypeRepository expenseTypeRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private testTools tools ;
	

	private int naturesTobeExpected = 0;
	private String TestDescription = "TestNature";
	private int TestTjm = 4500;
	private float marginError = 0.001f;
	private LocalDateTime beforeCreation = LocalDateTime.now();
	private LocalDateTime afterCreation = LocalDateTime.now();
	


	@Test
	void list() {
		// well there must have more than 0 one since inityDb fill some values in the persistence
		assertNotEquals(natureService.list().size(), this.naturesTobeExpected);
	}
	/**
	 * only test if the creation of a nature create and persist the entities with the
	 * desired values
	 * 
	 * @throws BadRequestException
	 */
	@Test
	@Order(0)
	void createSuccess() throws BadRequestException {
		// we create a testNature with specific values
		this.beforeCreation = LocalDateTime.now();
		Nature nature1 = new Nature();
		// in our implementation date are automatically added
		// nature3.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10,
		// 10));
		// nature3.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10,
		// 10));
		nature1.setDescription(this.TestDescription + "Create");
		System.err.println(nature1.getDescription());
		// we set them to true because their default values are false
		nature1.setGivesBonus(true);
		nature1.setCharged(true);
		nature1.setTjm(this.TestTjm);

		Nature nature = natureService.create(nature1);
		// creating a nature with the same nature should faill
		assertThrows(BadRequestException.class, () -> natureService.create(nature1));

		// the name is tested here we would have it if the name wasn't equals
		assertNotNull(nature);
		assertNotEquals(nature.getId(), 0); // the id must never be 0 in DB
		assertTrue(nature.getDescription().equals(nature1.getDescription()));
		// we check if date of validity is now
		assertTrue(nature.getDateOfValidity().isAfter(this.beforeCreation));
		this.afterCreation = LocalDateTime.now();
		assertTrue(nature.getDateOfValidity().isBefore(this.afterCreation));
		// that end of validity is supposed to be null
		assertTrue(nature.getEndOfValidity() == null);
		assertTrue(nature1.isCharged() == nature.isCharged());
		assertTrue(nature1.isGivesBonus() == nature.isGivesBonus());

		// here we test the margin error of float persistence
		assertTrue(nature1.getBonusPercentage() + this.marginError >= nature.getBonusPercentage());
		assertTrue(nature1.getBonusPercentage() - this.marginError <= nature.getBonusPercentage());

		assertTrue(nature1.getTjm().compareTo(nature.getTjm()) == 0);
	}

	/**
	 * to refactor we don't need to use a nature in DB to test that
	 * 
	 * @throws BadRequestException
	 */
	@Test
	@Order(1)
	void isAValidNature() throws BadRequestException {
		Nature nature = new Nature();
		nature.setEndOfValidity(LocalDateTime.now());
		// start validity is null
		assertThrows(BadRequestException.class, () -> natureService.isAValidNature(nature));
		nature.setDateOfValidity(LocalDateTime.now());
		// date EOV is not null and before start validity
		assertThrows(BadRequestException.class, () -> natureService.isAValidNature(nature));
		nature.setEndOfValidity(null);
		// description is null
		assertThrows(BadRequestException.class, () -> natureService.isAValidNature(nature));
		nature.setDescription("");
		// description empty
		assertThrows(BadRequestException.class, () -> natureService.isAValidNature(nature));
		nature.setDescription(TestDescription + "is a valid nature");
		assertTrue(natureService.isAValidNature(nature));
	}

	/**
	 * need data in db
	 * 
	 * @throws BadRequestException
	 */
	@Test
	@Order(3)
	void canBeUpdated() throws BadRequestException {
		Nature nature = this.pleaseCreateOneNature(this.TestDescription + "canBeupdated");
		//assertThrows(BadRequestException.class, () -> natureService.canBeUpdated( nature));
		// should be true, its the only one with that description
		assertTrue( natureService.canBeUpdated(nature));
		Nature nature1 = tools.giveMeJustANature(this.TestDescription + "canBeupdated");
		this.natureRepository.save(nature1);
		this.natureRepository.save(nature1);
		assertFalse( natureService.canBeUpdated(nature));
	
	}

	/**
	 * to refactor we need to create a new one in it before
	 * 
	 * @throws BadRequestException
	 */
	@Test
	@Order(2)
	void canBeAdded() throws BadRequestException {
		Nature nature = new Nature();
		nature.setDescription(TestDescription + "can be added nature");
		assertTrue(natureService.canBeAdded(nature));
		assertFalse(natureService.canBeAdded(this.pleaseCreateOneNature(TestDescription + "can be added nature")));

	}

	@Test
	@Order(4)
	void isThisNatureInUse() throws Exception {
		
		Nature nature = this.pleaseCreateOneNature(TestDescription + "is this nature in use");
		// just created so it should say no
		assertFalse(natureService.isThisNatureInUse(nature));
		//this.setTools();
		Mission mission = tools.pleaseCreateAMission(nature);
		// with a mission associated it should say yes
		assertTrue(natureService.isThisNatureInUse(nature));
		
	}

	@Test
	void ReadingTest() throws Exception {
		assertThrows(BadRequestException.class, () -> natureService.read(0));
		assertEquals(natureService.read(TestDescription + "readings").size(), 0);
		Nature nature = this.pleaseCreateOneNature(TestDescription + "readings");
		Nature nature2 = natureService.read(nature.getDescription()).get(0);
		assertTrue(nature.getDescription().equals(nature2.getDescription()));
		assertEquals(nature.getId(), natureService.read(nature.getId()).getId());
	}

	@Test
	void getActiveNatures() throws BadRequestException {
		List<Nature> activeNatures = natureService.getActiveNatures();
		int previousSize = activeNatures.size();
		Nature nature = this.pleaseCreateOneNature(TestDescription + "activeNatures");
		activeNatures = natureService.getActiveNatures();
		assertNotEquals(activeNatures.size(), previousSize);
		assertEquals(activeNatures.size(), previousSize + 1);
		assertTrue(activeNatures.stream().filter((nature2) -> nature2.getId() == nature.getId()).toList().get(0)
				.getDescription().equals(nature.getDescription()));
		nature.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        this.natureRepository.save(nature);
        activeNatures = natureService.getActiveNatures();
        assertEquals(activeNatures.size(), previousSize);
	}

    /**
     * @TODO rework this to be a real test of updating nature
     * @throws BadRequestException
     */
    @Test
    void update() throws BadRequestException {
    	
        Nature nature = tools.giveMeJustANature(TestDescription+"update");
        assertNull(nature.getEndOfValidity());        
        nature.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 6, 10, 10, 10));
        nature.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        Nature persistedNature  = natureRepository.save(nature);
        assertNotNull(natureService.read(persistedNature.getId()));
        // id inconsistent
        assertThrows(BadRequestException.class, () -> natureService.update(0, persistedNature));
        assertEquals(natureService.read(TestDescription+"update1").size(), 0);

    }

	/**
	 * this is to persist a nature with default values and a provided description
	 * 
	 * @param description
	 * @return a persisted nature reference
	 * @throws BadRequestException
	 */
	public Nature pleaseCreateOneNature(String description) throws BadRequestException {
		Nature nature = new Nature();
		nature.setDescription(description);
		System.err.println(description);
		// we set them to true because their default values are false
		nature.setGivesBonus(true);
		nature.setCharged(true);
		nature.setTjm(this.TestTjm);

		return this.natureService.create(nature);
	}
	
	public void setTools() {
		this.tools.setRoleSrv(roleSrv);
		this.tools.setCollaboratorRepository(this.collaboratorRepository);
		this.tools.setCityRepository(this.cityRepository);
		this.tools.setExpenseTypeRepository(expenseTypeRepository);
		this.tools.setMissionRepository(missionRepository);
		
		
		
	}
	
	
}