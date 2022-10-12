package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.repository.ExpenseTypeRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.ExpenseService;

/**
 * 
 * @Todo to refactor 
 * @author Vincent
 *
 */
@SpringBootTest
class ExpenseServiceImplTest {

    @Autowired
    private NatureRepository natureRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CollaboratorRepository collaboratorRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseService expenseService;

    @BeforeEach
    void init() {

        Nature nature1 = new Nature();
        nature1.setDateOfValidity(LocalDateTime.of(2000, Month.JANUARY, 24, 01, 01, 01));
        nature1.setDescription("nature1Name");
        nature1.setEndOfValidity(null);
        nature1.setCharged(true);
        nature1 = natureRepository.save(nature1);

        Nature nature2 = new Nature();
        nature2.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setDescription("nature2Name");
        nature2.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setCharged(false);
        nature2 = natureRepository.save(nature2);

        City city1 = new City();
        city1.setName("city1");
        city1 = cityRepository.save(city1);

        City city2 = new City();
        city2.setName("city2");
        city2 = cityRepository.save(city2);

        Collaborator collaborator = new Collaborator();
        collaborator = collaboratorRepository.save(collaborator);

        ExpenseType expenseType = new ExpenseType();
        expenseType.setName("car");
        expenseType = expenseTypeRepository.save(expenseType);

        Mission m1 = new Mission();
        m1.setBonus(BigDecimal.valueOf(36));
        m1.setMissionTransport(Transport.Car);
        m1.setNature(nature1);
        m1.setStartCity(city1);
        m1.setEndCity(city2);
        m1.setStartDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        m1.setEndDate(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        m1.setCollaborator(collaborator);
        m1.setStatus(Status.VALIDATED);
        m1 = missionRepository.save(m1);

        Mission m2 = new Mission();
        m2.setBonus(BigDecimal.valueOf(100));
        m2.setMissionTransport(Transport.Flight);
        m2.setNature(nature1);
        m2.setStartCity(city1);
        m2.setEndCity(city1);
        m2.setStartDate(LocalDateTime.now().plusDays(15));
        m2.setCollaborator(collaborator);
        m2.setEndDate(LocalDateTime.now().plusDays(20));
        m2.setStatus(Status.INIT);
        m2 = missionRepository.save(m2);

        Set<Expense> expensesM1 = new HashSet<>();
        Expense expense1 = new Expense();
        expense1.setCost(BigDecimal.valueOf(30));
        expense1.setDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10).plusDays(3));
        expense1.setTva(0.2f);
        expense1.setExpenseType(expenseType);
        expense1.setMission(m1);
        expensesM1.add(expense1);

        Expense expense2 = new Expense();
        expense2.setCost(BigDecimal.valueOf(30));
        expense2.setDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10).plusDays(3));
        expense2.setTva(0.2f);
        expense2.setExpenseType(expenseType);
        expense2.setMission(m1);
        expensesM1.add(expense2);



        m1.setExpenses(expensesM1);
        m1 = missionRepository.save(m1);
        expense1 = expenseRepository.save(expense1);
        expense2 = expenseRepository.save(expense2);
    }

    @AfterEach
    void clean() {
        expenseRepository.deleteAll();
        expenseTypeRepository.deleteAll();
        missionRepository.deleteAll();
        collaboratorRepository.deleteAll();
        natureRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    void list() {
        assertEquals(expenseService.list().size(), 2);
    }
    @Test
    void isExpenseValid() {
        Collaborator collaborator = collaboratorRepository.findAll().get(0);
        Mission m1 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.VALIDATED).get(0);
        Mission m2 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.INIT).get(0);

        //assertTrue(expenseRepository.findAll().stream().allMatch(expense -> expenseService.isExpenseValid(expense)));


        Expense invalidExpense = new Expense();
        invalidExpense.setCost(BigDecimal.valueOf(30));
        invalidExpense.setDate(m2.getStartDate().plusDays(3));
        invalidExpense.setTva(0.2f);
        invalidExpense.setExpenseType(expenseTypeRepository.findAll().get(0));
        invalidExpense.setMission(m2);

        //assertFalse(expenseService.isExpenseValid(invalidExpense));

        Expense invalidExpense2 = new Expense();
        invalidExpense2.setCost(BigDecimal.valueOf(30));
        invalidExpense2.setDate(m1.getEndDate().plusDays(10));
        invalidExpense2.setTva(0.2f);
        invalidExpense2.setExpenseType(expenseTypeRepository.findAll().get(0));
        invalidExpense2.setMission(m1);

        //assertFalse(expenseService.isExpenseValid(invalidExpense2));
    }

    @Test
    void create() throws BadRequestException {
        Collaborator collaborator = collaboratorRepository.findAll().get(0);
        Mission m1 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.VALIDATED).get(0);
        Mission m2 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.INIT).get(0);

        Expense invalidExpense = new Expense();
        invalidExpense.setCost(BigDecimal.valueOf(30));
        invalidExpense.setDate(m2.getStartDate().plusDays(3));
        invalidExpense.setTva(0.2f);
        invalidExpense.setExpenseType(expenseTypeRepository.findAll().get(0));
        invalidExpense.setMission(m2);

        assertEquals(expenseRepository.findAll().size(), 2);
        assertThrows(BadRequestException.class, () -> expenseService.create(invalidExpense));
        assertEquals(expenseRepository.findAll().size(), 2);

        Expense invalidExpense2 = new Expense();
        invalidExpense2.setCost(BigDecimal.valueOf(30));
        invalidExpense2.setDate(m1.getEndDate().plusDays(10));
        invalidExpense2.setTva(0.2f);
        invalidExpense2.setExpenseType(expenseTypeRepository.findAll().get(0));
        invalidExpense2.setMission(m1);

        assertEquals(expenseRepository.findAll().size(), 2);
        assertThrows(BadRequestException.class, () -> expenseService.create(invalidExpense2));
        assertEquals(expenseRepository.findAll().size(), 2);

        Expense validExpense = new Expense();
        validExpense.setCost(BigDecimal.valueOf(30));
        validExpense.setDate(m1.getStartDate().plusDays(10));
        validExpense.setTva(0.2f);
        validExpense.setExpenseType(expenseTypeRepository.findAll().get(0));
        validExpense.setMission(m1);

        assertEquals(expenseRepository.findAll().size(), 2);
        //expenseService.create(validExpense);
        assertEquals(expenseRepository.findAll().size(), 3);
        assertEquals(expenseRepository.findByMission(m1).size(), 3);

    }

    @Test
    void update() throws BadRequestException {
        Collaborator collaborator = collaboratorRepository.findAll().get(0);
        Mission m1 = missionRepository.findByCollaboratorAndStatusNot(collaborator, Status.VALIDATED).get(0);
        Mission m2 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.INIT).get(0);

        List<Expense> expenses = expenseRepository.findAll();
        Expense expense1 = expenses.get(0);
        Expense expense2 = expenses.get(1);

        LocalDateTime invalidDate = expense1.getMission().getEndDate().plusDays(2);
        LocalDateTime oldDate = expense1.getDate();
        expense1.setDate(invalidDate);

        assertThrows(BadRequestException.class, () -> expenseService.update(expense1.getId(), expense1));
        assertTrue(expenseRepository.findById(expense1.getId()).get().getDate().isEqual(oldDate));
        expense1.setDate(oldDate);

        expense1.setMission(m2);
        assertThrows(BadRequestException.class, () -> expenseService.update(expense1.getId(), expense1));
        assertTrue(expenseRepository.findById(expense1.getId()).get().getMission().getId() != m2.getId());
        expense1.setMission(m1);

        expense1.setCost(BigDecimal.valueOf(2000));
        //expenseService.update(expense1.getId(), expense1);
        assertTrue(expenseRepository.findById(expense1.getId()).get().getCost().compareTo(BigDecimal.valueOf(2000)) == 0);


    }

    @Test
    void delete() {
        expenseRepository.findAll().stream().forEach(expense -> {
            try {
                expenseService.delete(expense.getId());
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals(expenseRepository.findAll().size(), 0);
    }

}