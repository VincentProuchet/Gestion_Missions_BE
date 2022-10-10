package diginamic.gdm.utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.ExpenseType;
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
import diginamic.gdm.services.ExpenseTypeService;
import diginamic.gdm.services.NatureService;
import diginamic.gdm.services.RoleService;
import diginamic.gdm.services.implementations.CityServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * cette classe contient les outils pour les tests notamment : - des méthodes de
 * création d'instances d'objects JPA valides
 * 
 * @author Vincent
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Component
@Setter
@Getter
public class testTools {
	
	// this will need services
	@Autowired
	private RoleService roleSrv;
	// and all repository
	@Autowired
	private NatureRepository natureRepository;
	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@Autowired
	private MissionRepository missionRepository;
	@Autowired
	private ExpenseTypeRepository expenseTypeRepository;
	@Autowired
	private CityRepository cityRepository;
	
	

	private int TestTjm = 4500;
	private float marginError = 0.005f;
	private LocalDateTime nextMonth = LocalDateTime.now().plusMonths(1);
	private LocalDateTime theMonthAfter = LocalDateTime.now().plusMonths(2);

	private Roles userRole;
	private Roles adminRole;
	private Roles managerRole;



	/**
	 * create a Collaborator with the name provided with admins rights in database
	 * 
	 * 
	 * @param name
	 * @throws BadRequestException 
	 * @return a JPA instance of the newly created collaborator
	 */
	public Collaborator CreateCollaborator(String name) {
		this.userRole = new Roles(Role.COLLABORATOR);
		userRole = roleSrv.create(userRole);

		this.managerRole = new Roles(Role.MANAGER);
		managerRole = roleSrv.create(managerRole);

		this.adminRole = new Roles(Role.ADMIN);
		adminRole = roleSrv.create(adminRole);
		
		Collaborator admin = new Collaborator();
		admin.setAuthorities(Arrays.asList(managerRole, userRole, adminRole));
		admin.setEmail(name + "@mail");
		admin.setPassword("1111");
		admin.setFirstName(name + "firstname");
		admin.setLastName(name + "lastname");
		admin.setUsername(name + "username");
		admin.setActive(true);
		admin = collaboratorRepository.save(admin);
		admin.setManager(admin);
		return collaboratorRepository.save(admin);
	}
	/**
	 * create a list of 5 new cities
	 * with name in the for name + an iterator
	 * @param name provided name
	 * @return list of 5 cities entities for testing purposes
	 */
	public List<City> createCities(String name) {
		 List<City> cities = new ArrayList<>(5);
	        for (int i = 0; i < 5; i++) {
	            City newCity = new City();
	            newCity.setName("city"+name+ i);
	            newCity = this.cityRepository.save(newCity);
	            cities.add(newCity);
	        }
	        return cities;
	}
	/**
	 * this gives you an instance of nature 
	 * with : 
	 * startDOV = now
	 * endDOV = null;
	 * description = description
	 * giveBonus = true
	 * charged = true
	 * tjm  = this.TestTjm
	 * 
	 * its just to not write that block for each test
	 * @param description
	 * @return a NON-persisted nature entity
	 * @throws BadRequestException
	 */
	public Nature giveMeJustANature(String description) throws BadRequestException {
		Nature nature = new Nature();
		// in our implementation date are automatically added
		nature.setDateOfValidity(LocalDateTime.now());
		nature.setEndOfValidity(null);
		nature.setDescription(description);
		// we set them to true because their default values are false
		nature.setGivesBonus(true);
		nature.setCharged(true);
		nature.setTjm(this.TestTjm);

		return nature;
	}
	/**
	 * this is to persist a nature with default values and a provided description
	 * 
	 * @param description
	 * @return a persisted nature entity
	 * @throws BadRequestException
	 */
	public Nature pleaseCreateOneNature(String description) throws BadRequestException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime end = null;
		Nature nature = new Nature();
		nature.setDescription(description);
		// we set them to true because their default values are false
		nature.setDateOfValidity(now);
		nature.setEndOfValidity(end);
		nature.setGivesBonus(true);
		nature.setCharged(true);
		nature.setTjm(this.TestTjm);

		return this.natureRepository.save(nature);
	}
	
	/**
	 * this will create mission with just a nature
	 * it will use nature's description as name for creation of  
	 * - cities for the mission (5)
	 * - mission collaborator names, an administrator Collaborator 
	 * the mission will 
	 * - start next month from now 
	 * - end in the month after the next one from now
	 * 
	 * this directly use mission repository 
	 * there is not control on mission data validity 
	 * 
	 * @param nature
	 * @return a Persisted Mission entity 
	 */
	public Mission pleaseCreateAMission(Nature nature ) {
		Mission mission = new Mission();
		mission.setNature(nature);
		mission.setStartDate(this.nextMonth);
		mission.setEndDate(this.theMonthAfter);		
		mission.setCollaborator(this.CreateCollaborator(nature.getDescription()));
		List<City> cities = this.createCities(nature.getDescription());;		
		mission.setStartCity(cities.get(0));
		mission.setEndCity(cities.get(0));
		return this.missionRepository.save(mission);		
	}
	
	/**
	 * will create and persist  3 expensetypes of names
	 * made of name+iterator 
	 * @param name 
	 * @return list of persisted expensesType Entities
	 */
	public List<ExpenseType> creatExpensesType(String name){
		 // 3 expenses types
        List<ExpenseType> expenseTypes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ExpenseType newExpenseType = new ExpenseType();
            newExpenseType.setName(name + i);
            newExpenseType = expenseTypeRepository.save(newExpenseType);
            expenseTypes.add(newExpenseType);
        }
        return expenseTypes;
	}
}
