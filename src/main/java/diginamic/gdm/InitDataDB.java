package diginamic.gdm;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.*;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * initialisation de la base de données 
 * appelée lors de l'initialisation de l'application
 * peupleras la base de données avec des données cohérentes et utilisable lors de test
 * 
 * @author Joseph 
 *
 */
@Component
public class InitDataDB {

    private final LocalDateTime now;
    private final LocalDateTime lastYear;
    private final LocalDateTime nextYear;
    @Autowired
    private CityService cityService;
    @Autowired
    private CollaboratorService collaboratorService;
    @Autowired
    private NatureService natureService;
    @Autowired
    private NatureRepository natureRepository;
    @Autowired
    private ExpenseTypeService expenseTypeService;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private MissionService missionService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    {
        now = nextWorkedDay(LocalDateTime.now().plusDays(1));
        lastYear = nextWorkedDay(now.minusYears(1));
        nextYear = nextWorkedDay(now.plusYears(1));

    }

   /**
 * @param event
 * @throws Exception
 * @throws BadRequestException
 */
@EventListener
    public void initDB(ContextRefreshedEvent event) throws Exception,BadRequestException {
        System.out.println("init mock data");

        // 5 cities
        List<City> cities = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            City newCity = new City();
            newCity.setName("cityName" + i);
            newCity = cityService.create(newCity);
            cities.add(newCity);
        }

        // 3 roles
        Roles user = new Roles(Role.COLLABORATOR);
        user = roleService.create(user);

        Roles managerRole = new Roles(Role.MANAGER);
        managerRole = roleService.create(managerRole);

        Roles adminRole = new Roles(Role.ADMIN);
        adminRole = roleService.create(adminRole);
        String emailEnd = "@carapace.com"; 
        // 5 collaborators, 2 managers, 2 admin
        // manager1 for the workers
        String m1Name = "vincent";
        Collaborator manager1 = new Collaborator();
        manager1.setAuthorities(Arrays.asList(managerRole, user));
        manager1.setEmail( m1Name + emailEnd);
        manager1.setPassword(passwordEncoder.encode("1111"));
        manager1.setFirstName(m1Name+ "firstname");
        manager1.setLastName(m1Name+"lastname");
        manager1.setUsername(m1Name+"username");
        manager1.setActive(true);

        // manager2 for the managers (himself included...) and the admin
        String m2Name = "joseph";
        Collaborator manager2 = new Collaborator();
        manager2.setAuthorities(Arrays.asList(managerRole, user));
        manager2.setEmail(m2Name+emailEnd);
        manager2.setPassword(passwordEncoder.encode("1111"));
        manager2.setFirstName(m2Name+"firstname");
        manager2.setLastName(m2Name+"lastname");
        manager2.setUsername(m2Name+"username");
        manager2.setActive(true);

        manager1.setManager(manager2);
        manager2.setManager(manager2);

        // the admin
        
        String a1Name = "dorian";
        Collaborator admin = new Collaborator();
        admin.setAuthorities(Arrays.asList(adminRole, user));
        admin.setEmail(a1Name +emailEnd);
        admin.setPassword(passwordEncoder.encode("1111"));
        admin.setFirstName(a1Name +"firstname");
        admin.setLastName(a1Name +"lastname");
        admin.setUsername(a1Name +"username");
        admin.setActive(true);
        admin.setManager(admin);
        
       

        manager2 = collaboratorService.create(manager2);
        manager1 = collaboratorService.create(manager1);
        admin = collaboratorService.create(admin);

        // the 6 workers
        List<Collaborator> collaborators = new ArrayList<>();        
        
        for (int i = 0; i < 5; i++) {
            Collaborator newColl = new Collaborator();
            newColl.setAuthorities(Arrays.asList(user));
            newColl.setEmail("coll" + i + emailEnd);
            newColl.setFirstName("coll" + i + "firstname");
            newColl.setLastName("coll" + i + "lastname");
            newColl.setUsername("coll" + i + "username");
            newColl.setPassword(passwordEncoder.encode(""+ i + i + i + i));
            newColl.setActive(true);
            newColl.setManager(manager1);
            newColl = collaboratorService.create(newColl);
            collaborators.add(newColl);
        }
        Collaborator user1 = new Collaborator();
        user1.setAuthorities(Arrays.asList(user));
        user1.setEmail("m-itsumi"+emailEnd );
        user1.setPassword(passwordEncoder.encode("1111"));
        user1.setFirstName("Mario");
        user1.setLastName("Istumi");
        user1.setUsername("mario");
        user1.setActive(true);
        user1.setManager(manager2);
        user1 = collaboratorService.create(user1);
        collaborators.add(user1);
        
        // 6 natures
        List<Nature> natures = new ArrayList<>(5);

        // basic nature
        Nature newNature = new Nature();
        newNature.setTjm(BigDecimal.valueOf(200));
        newNature.setDescription("Hard work type 1");
        newNature.setDateOfValidity(lastYear);
        newNature.setEndOfValidity(null);
        newNature.setGivesBonus(true);
        newNature.setBonusPercentage(5f);
        newNature.setCharged(true);
        newNature = natureService.create(newNature);
        natures.add(newNature);

        // basic nature, more money
        Nature newNature2 = new Nature();
        newNature2.setTjm(BigDecimal.valueOf(500));
        newNature2.setDescription("Hard work type 2");
        newNature2.setDateOfValidity(lastYear);
        newNature2.setEndOfValidity(null);
        newNature2.setGivesBonus(true);
        newNature2.setBonusPercentage(7f);
        newNature2.setCharged(true);
        newNature2 = natureService.create(newNature2);
        natures.add(newNature2);

        // non charged nature
        Nature newNature3 = new Nature();
        newNature3.setTjm(BigDecimal.valueOf(0));
        newNature3.setDescription("Formation");
        newNature3.setDateOfValidity(lastYear);
        newNature3.setEndOfValidity(null);
        newNature3.setGivesBonus(false);
        newNature3.setBonusPercentage(0f);
        newNature3.setCharged(false);
        newNature3 = natureService.create(newNature3);
        natures.add(newNature3);


        // time limited nature
        Nature newNature4 = new Nature();
        newNature4.setTjm(BigDecimal.valueOf(50));
        newNature4.setDescription("Promotional work");
        newNature4.setDateOfValidity(previousWeek(now));
        //newNature4.setEndOfValidity(nextWeek(now));// no more end of validity in the future
        newNature4.setGivesBonus(true);
        newNature4.setBonusPercentage(50f);
        newNature4.setCharged(true);
        newNature4 = natureService.create(newNature4);
        natures.add(newNature4);

        // ended nature with same description
        Nature newNature5 = new Nature();
        newNature5.setTjm(BigDecimal.valueOf(150));
        newNature5.setDescription("Promotional work");
        newNature5.setDateOfValidity(previousWeek(lastYear));
        newNature5.setEndOfValidity(nextWeek(lastYear));
        newNature5.setGivesBonus(true);
        newNature5.setBonusPercentage(50f);
        newNature5.setCharged(true);
        newNature5 = natureRepository.save(newNature5);
        natures.add(newNature5);

        // ended nature
        Nature newNature6 = new Nature();
        newNature6.setTjm(BigDecimal.valueOf(50));
        newNature6.setDescription("Deprecated work");
        newNature6.setDateOfValidity(previousMonth(lastYear));
        newNature6.setEndOfValidity(nextMonth(lastYear));
        newNature6.setGivesBonus(false);
        newNature6.setBonusPercentage(0f);
        newNature6.setCharged(true);
        newNature6 = natureService.create(newNature6);
        natures.add(newNature6);

        // 8 missions
        // 5 missions currently active or for the near future, 2 VALIDATED, 1 REJECTED, 2 INIT
        List<Mission> missions = new ArrayList<>(10);

        // validated, begin now, end next week, collaborator 0
        Mission newMission = new Mission();
        newMission.setStartCity(cities.get(0));
        newMission.setEndCity(cities.get(1));
        newMission.setCollaborator(collaborators.get(0));
        newMission.setMissionTransport(Transport.Carshare);
        newMission.setNature(newNature);
        newMission.setStatus(Status.INIT);
        newMission.setStartDate(now);
        newMission.setEndDate(nextWeek(now));
        newMission = missionRepository.save(newMission);
        missions.add(newMission);

        // validated, begin nextYear, end nextYear + 1 week, collaborator 0
        Mission newMission2 = new Mission();
        newMission2.setStartCity(cities.get(1));
        newMission2.setEndCity(cities.get(1));
        newMission2.setCollaborator(collaborators.get(0));
        newMission2.setMissionTransport(Transport.Flight);
        newMission2.setNature(newNature2);
        newMission2.setStatus(Status.VALIDATED);
        newMission2.setStartDate(nextYear);
        newMission2.setEndDate(nextWeek(nextYear));
        newMission2 = missionRepository.save(newMission2);
        missions.add(newMission2);

        // rejected, begin last week, end nextMonth, collaborator 0
        Mission newMission3 = new Mission();
        newMission3.setStartCity(cities.get(4));
        newMission3.setEndCity(cities.get(2));
        newMission3.setCollaborator(collaborators.get(0));
        newMission3.setMissionTransport(Transport.Car);
        newMission3.setNature(newNature3);
        newMission3.setStatus(Status.REJECTED);
        newMission3.setStartDate(previousWeek(now));
        newMission3.setEndDate(nextMonth(now));
        newMission3 = missionRepository.save(newMission3);
        missions.add(newMission3);

        // init, begin now, promo nature, end nextMonth, collaborator 2
        Mission newMission4 = new Mission();
        newMission4.setStartCity(cities.get(3));
        newMission4.setEndCity(cities.get(0));
        newMission4.setCollaborator(collaborators.get(2));
        newMission4.setMissionTransport(Transport.Car);
        newMission4.setNature(newNature4);
        newMission4.setStatus(Status.INIT);
        newMission4.setStartDate(now);
        newMission4.setEndDate(nextMonth(now));
        newMission4 = missionRepository.save(newMission4);
        missions.add(newMission4);

        // init, begin next month, end nextYear, collaborator 3
        Mission newMission5 = new Mission();
        newMission5.setStartCity(cities.get(2));
        newMission5.setEndCity(cities.get(4));
        newMission5.setCollaborator(collaborators.get(3));
        newMission5.setMissionTransport(Transport.Car);
        newMission5.setNature(newNature2);
        newMission5.setStatus(Status.INIT);
        newMission5.setStartDate(nextMonth(now));
        newMission5.setEndDate(nextYear);
        newMission5 = missionRepository.save(newMission5);
        missions.add(newMission5);


        // 3 missions ended, two of which need the night computing for their bonus
        // 2 validated, 1 refused (illustrate an issue here), 2 with disabled natures

        // validated, start last year, end lastyear + 1 week, old promo nature, manager1

        Mission newMission6 = new Mission();
        newMission6.setStartCity(cities.get(0));
        newMission6.setEndCity(cities.get(0));
        newMission6.setCollaborator(manager1);
        newMission6.setMissionTransport(Transport.Train);
        newMission6.setNature(newNature4);
        newMission6.setStatus(Status.VALIDATED);
        newMission6.setStartDate(lastYear);
        newMission6.setEndDate(nextWeek(lastYear));
        newMission6 = missionRepository.save(newMission6);
        missions.add(newMission6);

        // validated, start lastyear + 1 week, end lastyear + 1 month, deprecated nature, collaborator 0
        Mission newMission7 = new Mission();
        newMission7.setStartCity(cities.get(1));
        newMission7.setEndCity(cities.get(1));
        newMission7.setCollaborator(collaborators.get(0));
        newMission7.setMissionTransport(Transport.Flight);
        newMission7.setNature(newNature5);
        newMission7.setStatus(Status.VALIDATED);
        newMission7.setStartDate(nextWeek(lastYear));
        newMission7.setEndDate(nextMonth(lastYear));
        newMission7 = missionRepository.save(newMission7);
        missions.add(newMission7);

        // rejected, start lastyear, end lastyear + 1 week, basic nature, admin
        Mission newMission8 = new Mission();
        newMission8.setStartCity(cities.get(2));
        newMission8.setEndCity(cities.get(2));
        newMission8.setCollaborator(admin);
        newMission8.setMissionTransport(Transport.Car);
        newMission8.setNature(newNature);
        newMission8.setStatus(Status.REJECTED);
        newMission8.setStartDate(lastYear);
        newMission8.setEndDate(nextWeek(lastYear));
        newMission8 = missionRepository.save(newMission8);
        missions.add(newMission8);

        // 3 expenses types
        List<ExpenseType> expenseTypes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ExpenseType newExpenseType = new ExpenseType();
            newExpenseType.setName("expenseType" + i);
            newExpenseType = expenseTypeService.create(newExpenseType);
            expenseTypes.add(newExpenseType);
        }

        // 10 expenses
        List<Expense> expenses = new ArrayList<>();
        List<Mission> completedMissions = missionService.completedMissions();
        for (int i = 0; i < 10; i++) {
            Mission mission = completedMissions.get(i % completedMissions.size());
            Expense expense = new Expense();
            expense.setCost(BigDecimal.valueOf(10 * i));
            expense.setMission(mission);
            expense.setDate(mission.getStartDate().plusDays(1));
            expense.setExpenseType(expenseTypes.get(i % expenseTypes.size()));
            expense.setTva((float) i);
            expense = expenseRepository.save(expense);
            expenses.add(expense);
        }
    }

    private LocalDateTime previousWeek(LocalDateTime date) {
        return nextWorkedDay(date.minusWeeks(1));
    }

    private LocalDateTime nextWeek(LocalDateTime date) {
        return nextWorkedDay(date.plusWeeks(1));
    }

    private LocalDateTime previousMonth(LocalDateTime date) {
        return nextWorkedDay(date.minusMonths(1));
    }

    private LocalDateTime nextMonth(LocalDateTime date) {
        return nextWorkedDay(date.plusMonths(1));
    }

    private LocalDateTime nextWorkedDay(LocalDateTime date) {
    	date.plusDays(1);
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return date.plusDays(1);
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return date.plusDays(2);
        }
        return date;
    }
}