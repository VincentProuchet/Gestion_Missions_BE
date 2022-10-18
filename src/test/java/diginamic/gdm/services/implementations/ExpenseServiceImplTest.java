package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.ExpenseService;
import diginamic.gdm.utilities.testTools;

/**
 * 
 * @Todo to refactor
 * @author Vincent
 *
 */
@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class ExpenseServiceImplTest {

	@Autowired
	private NatureRepository natureRepository;
	@Autowired
	private MissionRepository missionRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private ExpenseServiceImpl service;
	@Autowired
	private testTools tools;

	private String description = "ExpenseSrvTest";

	/** cities */
	private List<City> cities = new ArrayList<>();
	/** expensetypes */
	private List<ExpenseType> expensetypes= new ArrayList<>();
	/** expenses */
	private List<Expense> expenses= new ArrayList();
	/** colls */
	private List<Collaborator> colls= new ArrayList<>();
	/** natures */
	private List<Nature> natures= new ArrayList<>();
	/** missions */
	private List<Mission> missions= new ArrayList<>();

	@BeforeAll
	void init() {
		// valid nature
		Nature nature;
		nature = tools.giveMeJustANature(description);
		nature.setDateOfValidity(LocalDateTime.of(2000, Month.JANUARY, 24, 01, 01, 01));
		natures.add(0,natureRepository.save(nature));
		// invalid nature
		nature = tools.giveMeJustANature(description);
		nature.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
		nature.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
		nature.setCharged(false);		
		natures.add(1,natureRepository.save(nature));
		
		cities = tools.createCities(description);
		
		colls.add(tools.CreateCollaborator(description));// collaborator that get expenses from init
		colls.add(tools.CreateCollaborator(description+1));		
		colls.add(tools.CreateCollaborator(description+2));		
		
		expensetypes = tools.creatExpensesTypes(description);
		// mission that will get expenses
		LocalDateTime missionStart = LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10);
		LocalDateTime missionEnd = LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10);
		Mission m1;
		m1 = new Mission();
		m1.setBonus(BigDecimal.valueOf(36));
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(0));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(1));
		m1.setStartDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
		m1.setEndDate(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
		m1.setCollaborator(colls.get(0));
		m1.setStatus(Status.VALIDATED);
		missions.add(missionRepository.save(m1));
		
		
		// mission that Can't get expenses
		m1 = new Mission();
		m1.setBonus(BigDecimal.valueOf(100));
		m1.setMissionTransport(Transport.Flight);
		m1.setNature(natures.get(0));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(1));
		m1.setStartDate(LocalDateTime.now().plusDays(15));
		m1.setCollaborator(colls.get(0));
		m1.setEndDate(LocalDateTime.now().plusDays(15+30));
		m1.setStatus(Status.INIT);
		missions.add(missionRepository.save(m1));		
		
		Expense expense1;
		expense1 = new Expense();
		expense1.setCost(BigDecimal.valueOf(30));
		expense1.setDate(tools.nextWorkDay(missionStart.plusDays(3)));
		expense1.setTva(0.2f);
		expense1.setExpenseType(expensetypes.get(0));
		expense1.setMission(missions.get(0));
		expenses.add(expense1);

		expense1 = new Expense();
		expense1.setCost(BigDecimal.valueOf(30));
		expense1.setDate(tools.nextWorkDay(missionStart.plusDays(3)));
		expense1.setTva(0.2f);
		expense1.setExpenseType(expensetypes.get(0));
		expense1.setMission(missions.get(0));
		expenses.add(expense1);
		Set<Expense>exp = new HashSet<Expense>();
		exp.addAll(expenses);
		missions.get(0).setExpenses(exp );
		// update mission in persistence
		for (Mission m : missions) {
			m = this.missionRepository.save(m);
		}
		// update expense in persistence
		for (Expense e: expenses) {
			e = expenseRepository.save(e);			
		}
	
	}

	@Test
	private void list() {
		assertNotEquals(0,service.list().size());
	}

	@Test
	private void isExpenseValid() {
		
	}

	@Test
	@Order(1)
	public void create() throws BadRequestException {
		Mission mission = missions.get(0);
		ExpenseType et = expensetypes.get(0);
		
		Expense expense = new Expense();
		LocalDateTime date = tools.nextWorkDay(mission.getStartDate().plusDays(3));
		assertThrows(BadRequestException.class, ()->this.service.create(expense));

		expense.setCost(BigDecimal.valueOf(30));
		expense.setDate(date);
		expense.setTva(0.2f);
		expense.setExpenseType(et);
		expense.setMission(mission);
		// we should have a valid expense 
		assertDoesNotThrow(()->service.create(expense));
		// expense out of mission
		expense.setDate(tools.nextWorkDay(LocalDateTime.now()));
		assertThrows(BadRequestException.class,()->service.create(expense));		
		// expense Week-end
		expense.setDate(tools.nextWeekEnd(date));
		assertThrows(BadRequestException.class,()->service.create(expense));		
		expense.setDate(date);
		// neg cost
		expense.setCost(BigDecimal.valueOf(-30));
		assertThrows(BadRequestException.class,()->service.create(expense));
		expense.setCost(BigDecimal.valueOf(130));
		// neg taxes 
		expense.setTva(-0.2f);
		assertThrows(BadRequestException.class,()->service.create(expense));
		// taxes overflow
		expense.setTva(101f);
		assertThrows(BadRequestException.class,()->service.create(expense));
		expense.setTva(5f);
		assertDoesNotThrow(()->service.create(expense));
		// expense type null
		expense.setExpenseType(null);
		assertThrows(BadRequestException.class,()->service.create(expense));
		expense.setExpenseType(et);
		// mission null
		expense.setMission(null);
		assertThrows(BadRequestException.class,()->service.create(expense));
		expense.setMission(mission);
		// Mission Mission not done
		
		
		assertDoesNotThrow(()->service.create(expense));
		
	}

	@Test
	@Order(2)
	public void update() throws BadRequestException {
		
		Expense expense = expenses.get(0);
		int id = expense.getId();
		ExpenseType et = expense.getExpenseType();
		Mission mission = expense.getMission();
		LocalDateTime date = tools.nextWorkDay(mission.getStartDate().plusDays(3));
		
		assertDoesNotThrow( ()->this.service.update(id,expense));

		expense.setCost(BigDecimal.valueOf(30));
		expense.setDate(date);
		expense.setTva(0.2f);
		expense.setExpenseType(et);
		expense.setMission(mission);
		// we should have a valid expense 
		assertDoesNotThrow(()->service.update(id,expense));
		// wrong id 
		assertThrows(BadRequestException.class,()->service.update(Integer.MAX_VALUE,expense));
		// not found
		expense.setId(Integer.MAX_VALUE);
		assertThrows(BadRequestException.class,()->service.update(id,expense));
		expense.setId(id);
		// expense out of mission
		expense.setDate(tools.nextWorkDay(LocalDateTime.now()));
		assertThrows(BadRequestException.class,()->service.update(id,expense));		
		// expense Week-end
		expense.setDate(tools.nextWeekEnd(date));
		assertThrows(BadRequestException.class,()->service.update(id,expense));		
		expense.setDate(date);
		// neg cost
		expense.setCost(BigDecimal.valueOf(-30));
		assertThrows(BadRequestException.class,()->service.update(id,expense));
		expense.setCost(BigDecimal.valueOf(130));
		// neg taxes 
		expense.setTva(-0.2f);
		assertThrows(BadRequestException.class,()->service.update(id,expense));
		// taxes overflow
		expense.setTva(101f);
		assertThrows(BadRequestException.class,()->service.update(id,expense));
		expense.setTva(5f);
		assertDoesNotThrow(()->service.update(id,expense));
		// expense type null
		expense.setExpenseType(null);
		assertThrows(BadRequestException.class,()->service.update(id,expense));
		expense.setExpenseType(et);
		// mission null
		expense.setMission(null);
		assertThrows(BadRequestException.class,()->service.update(id,expense));
		expense.setMission(mission);
		assertDoesNotThrow(()->service.update(id,expense));
	}

	@Test
	private void delete() {
		for (Expense e :expenses) {
			assertDoesNotThrow(()->this.service.delete(e.getId()));
		}
	}

}