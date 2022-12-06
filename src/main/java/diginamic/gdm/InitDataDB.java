package diginamic.gdm;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.ApplicationParams;
import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.repository.ApplicationParamsRepository;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.repository.ExpenseTypeRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.ApplicationParamService;
import diginamic.gdm.services.CityService;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.ExpenseTypeService;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.NatureService;
import diginamic.gdm.services.RoleService;

/**
 * initialisation de la base de données appelée lors de l'initialisation de
 * l'application peupleras la base de données avec des données cohérentes et
 * utilisable lors de test
 *
 * @author Joseph
 *
 */
@Component
public class InitDataDB {

	private final LocalDateTime now;
	private final LocalDateTime lastYear;
	private final LocalDateTime nextYear;
	@SuppressWarnings("unused")
	@Autowired
	private CityService cityService;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private CollaboratorService collaboratorService;
	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@SuppressWarnings("unused")
	@Autowired
	private NatureService natureService;
	@Autowired
	private NatureRepository natureRepository;
	@SuppressWarnings("unused")
	@Autowired
	private ExpenseTypeService expenseTypeService;
	@Autowired
	private ExpenseTypeRepository expenseTypeRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private MissionRepository missionRepository;
	@SuppressWarnings("unused")
	@Autowired
	private MissionService missionService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ApplicationParamService paramsR;
	@SuppressWarnings("unused")
	@Autowired
	private ApplicationParamsRepository applicationParamsRepository;

	private Roles userR;
	private Roles adminR;
	private Roles managerR;

	private final String paramInitDBName = "intidb";
	private List<Collaborator> coll = new ArrayList<>();
	private List<Nature> natures = new ArrayList<>();
	private List<City> Cities = new ArrayList<>();
	private List<Mission> missions = new ArrayList<>();
	private List<ExpenseType> expenseTypes = new ArrayList<>();
	private List<Expense> expenses = new ArrayList<>();

	{
		now = nextWorkedDay(LocalDateTime.now().plusDays(1));
		lastYear = nextWorkedDay(now.minusYears(1));
		nextYear = nextWorkedDay(now.plusYears(1));

	}

	/**
	 * event called at application start
	 *
	 * @param event
	 * @throws Exception
	 * @throws BadRequestException
	 */
	@EventListener
	public void initApp(ContextRefreshedEvent event) throws Exception, BadRequestException {
		// 3 roles
		userR = new Roles(Role.COLLABORATOR);
		userR = roleService.create(userR);

		managerR = new Roles(Role.MANAGER);
		managerR = roleService.create(managerR);

		adminR = new Roles(Role.ADMIN);
		adminR = roleService.create(adminR);
		ApplicationParams paramdInitDB = new ApplicationParams();
		paramdInitDB.setValueb(false);
		// we search the initDB parameter
		try {
			// if it exist
			paramdInitDB = this.paramsR.read(paramInitDBName);
		} catch (Exception e) {
			// if it doesn't exist
			System.err.println(e.getMessage());
			// we reste DB to empty
			// because of duplicate entries
			this.deleteDb();
			// we create it

			paramdInitDB.setId(0);
			paramdInitDB.setName(paramInitDBName);
			paramdInitDB.setValueb(false);
			paramdInitDB = this.paramsR.create(paramdInitDB);
		} finally {
			// in any case we check the value
			if (!paramdInitDB.isValueb()) {
				// if false we initaialize the database
				this.initDB();
				paramdInitDB.setValueb(true);
				this.paramsR.update(paramdInitDB);
			}
			// else there is nothing to do
		}
	}

	/**
	 * initialise database with mock data mainly used for dveloppement and
	 * presentation
	 *
	 * @throws Exception
	 * @throws BadRequestException
	 */
	public void initDB() throws Exception, BadRequestException {
		System.out.println("init mock data");

		// 5 cities
		this.createCities();
		this.createExpenseTypes();

		// 5 collaborators, 2 managers, 1 admin
		// manager1 for the workers/manager2/ admin
		Collaborator manager1 = this.giveMeACollaborator("vincent", managerR, userR);
		// manager2 for the managers
		Collaborator manager2 = this.giveMeACollaborator("joseph", managerR, userR);
		// the admin /not manager
		Collaborator admin = this.giveMeACollaborator("dorian", adminR, userR);

		admin.setManager(manager1);
		manager1.setManager(manager2);
		manager2.setManager(manager1);

		manager2 = collaboratorService.update(manager2.getId(), manager2);
		manager1 = collaboratorService.update(manager1.getId(), manager1);
		admin = collaboratorService.update(admin.getId(), admin);

		// the 5 workers
		for (int i = 0; i < 5; i++) {
			Collaborator newColl = giveMeACollaborator("coll" + i, userR);
			newColl.setManager(manager1);
			coll.add(collaboratorService.update(newColl.getId(), newColl));
		}
		int yearsPrior = 5;
		// 6 natures
		this.createNatures("assistance", 5);
		this.createNatures("recherche et dévellopement", 5);
		this.createNatures("lêchage d'envellopes", 5);
		this.createNatures("installation de systèmes", 5);
		this.createNatures("observation", 5);
		this.createNatures("devis", 5);
		// for each collaborator

		for (Collaborator coll : coll) {

			this.giveMissionTo(coll, yearsPrior);
			this.giveOldSetMissions(coll);
		}
		this.giveMissionTo(admin, yearsPrior);
		this.giveMissionTo(manager1, yearsPrior);
		this.giveMissionTo(manager2, yearsPrior);

		this.giveOldSetMissions(admin);
		this.giveOldSetMissions(manager1);
		this.giveOldSetMissions(manager2);

		this.createExpense();

	}

	private void giveMissionTo(Collaborator coll, int yearsPrior) {
		LocalDateTime backTime = now.minusDays(now.getDayOfYear()).minusYears(yearsPrior);
		int rand = 0;
		List<Mission> local = new ArrayList<>();
		for (; backTime.isBefore(now); backTime = backTime.plusDays(12)) {
			// validated, begin years prior end 11 days after
			Mission newMission = new Mission();
			rand = giveMeAnumber(natures.size());
			newMission.setNature(natures.get(rand));
			rand = giveMeAnumber(Cities.size());
			newMission.setStartCity(Cities.get(rand));
			rand = giveMeAnumber(Cities.size());
			newMission.setEndCity(Cities.get(rand));
			newMission.setCollaborator(coll);
			newMission.setMissionTransport(Transport.Carshare);
			newMission.setStatus(Status.VALIDATED);
			newMission.setStartDate(backTime);
			newMission.setEndDate(nextWeek(backTime.plusDays(11)));
			local.add(newMission);
		}

		this.missions.addAll(missionRepository.saveAll(local));
	}

	/**
	 * créer des villes dans la base de données
	 *
	 */
	private void createCities() {
		List<City> local = new ArrayList<>();

		local.add(new City(0, "montpellier"));
		local.add(new City(0, "béziers"));
		local.add(new City(0, "narbonne"));
		local.add(new City(0, "agde"));
		local.add(new City(0, "Saint-Remy-en-Bouzemont-Saint-Genest-et-Isson"));
		local.add(new City(0, "séte"));
		local.add(new City(0, "valras"));
		local.add(new City(0, "paris"));
//			local.add(cityService.create(new City(0, "la tour sur orb")));
//			local.add(cityService.create(new City(0, "la tour sur mer")));
//			local.add(cityService.create(new City(0, "la salvetat sur âgout")));
		local.add(new City(0, "frontignan"));
		local.add(new City(0, "lattes"));
		local.add(new City(0, "lyon"));
		local.add(new City(0, "marseille"));

		try {
			this.Cities.addAll(cityRepository.saveAll(local));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * créer des natures dans la base de données durant une années chacune en
	 * utilisant yearsprior pour le nombre d'années à remonter
	 *
	 * @param natureName
	 * @param yearsPrior
	 * @throws BadRequestException
	 */
	private void createNatures(String natureName, int yearsPrior) throws BadRequestException {
		List <Nature> local = new ArrayList<>();
		LocalDateTime thisYear = LocalDateTime.now();
		thisYear = thisYear.minusDays(thisYear.getDayOfYear());
		LocalDateTime priorsYear = thisYear.minusYears(yearsPrior);
		Nature newNature;

		for (; priorsYear.isBefore(thisYear); priorsYear = priorsYear.plusYears(1)) {
			newNature = new Nature();
			newNature.setTjm((float) (Math.random() * 200));
			newNature.setDescription(natureName);
			newNature.setGivesBonus(true);
			try {
				newNature.setBonusPercentage((float) (Math.random() * 10));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			newNature.setCharged(true);
			newNature.setDateOfValidity(priorsYear);
			newNature.setEndOfValidity(priorsYear.plusYears(1));
			local.add(newNature);
		}
		// on créer la dernière nature come active
		newNature = new Nature();
		newNature.setTjm((float) (Math.random() * 200));
		newNature.setDescription(natureName);
		newNature.setGivesBonus(true);
		try {
			newNature.setBonusPercentage((float) (Math.random() * 10));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		newNature.setCharged(true);
		newNature.setDateOfValidity(priorsYear);
		newNature.setEndOfValidity(null);
		local.add(newNature);

		natures.addAll(this.natureRepository.saveAll(local));

	}

	/**
	 * ajoute des dépense à toutes les missions validées ou terminées et dont la
	 * date de fin est dans le passé présentes dans la base de données
	 */
	private void createExpense() {
		// pour chaque mission
		this.missions = missionRepository.findAll();
		List<Expense> local = new ArrayList<>();

		for (Mission mission : missions) {
			if ((mission.getStatus() == Status.VALIDATED || mission.getStatus() == Status.ENDED)
					&& mission.getEndDate().isBefore(now)) {
				// pour chaque type de dépense
				for (ExpenseType et : expenseTypes) {
					Expense exp = new Expense();
					exp.setId(0);
					exp.setExpenseType(et);
					exp.setMission(mission);
					exp.setCost((float) (Math.random() * 1000));
					exp.setTva(5f);
					// no more than 10 days from mission start
					int day = giveMeAnumber(10);
					exp.setDate(mission.getStartDate().plusDays(day));
					local.add(exp);
				}
			}

		}

		this.expenses.addAll(this.expenseRepository.saveAll(local));

	}

	/**
	 * créer des type de dépenses dans la bases de données
	 *
	 * @throws BadRequestException
	 */
	public void createExpenseTypes() throws BadRequestException {

		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "hébergement")));
		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "transport")));
		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "carburant")));
		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "fournitures de bureau")));
		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "fournitures d'entretien")));
		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "stylos")));
		expenseTypes.add(expenseTypeRepository.save(new ExpenseType(0, "papier toilette")));

	}

	private int giveMeAnumber(int limit) {
		return (int) ((Math.random() * 100) % limit);
	}

	private LocalDateTime previousWeek(LocalDateTime date) {
		return nextWorkedDay(date.minusWeeks(1));
	}

	private LocalDateTime nextWeek(LocalDateTime date) {
		return nextWorkedDay(date.plusWeeks(1));
	}

	@SuppressWarnings("unused")
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

	/**
	 * create a Collaborator with the name and roles provides all password are 1111
	 * and user is acitve et sans manager
	 *
	 * @param name
	 * @throws BadRequestException
	 * @return a JPA instance of the newly created collaborator
	 */
	public Collaborator giveMeACollaborator(String name, Roles... roles) {
		Collaborator collaborator = new Collaborator();
		collaborator.setFirstName(name);
		collaborator.setLastName(name);
		collaborator.setEmail(name.toLowerCase() + "@mail.com");

		collaborator.setUsername(name);
		collaborator.setPassword(passwordEncoder.encode("1111"));
		collaborator.setActive(true);
		collaborator.setAuthorities(Arrays.asList(roles));
		try {
			collaborator = this.collaboratorService.create(collaborator);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return collaborator;

	}

	/**
	 * ici ce sont les données crées par joseph un peu modifiées
	 *
	 * les différences elles ne sont plus assignée à un collaborateur fixe les
	 * missions dans le passé ont le statut rejected les villes et les natures sont
	 * choisies aléatoirement
	 *
	 * @param coll
	 */
	public void giveOldSetMissions(Collaborator coll) {

		// LocalDateTime backTime =
		// now.minusDays(now.getDayOfYear()).minusYears(yearsPrior);
		int rand = 0;
		// validated, begin now, end next week, collaborator 0

		Mission newMission = new Mission();

		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setStatus(Status.INIT);
		newMission.setStartDate(now);
		newMission.setEndDate(nextWeek(now));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// validated, begin nextYear, end nextYear + 1 week, collaborator 0
		newMission = new Mission();

		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setStatus(Status.VALIDATED);
		newMission.setMissionTransport(Transport.Flight);
		newMission.setStartDate(nextYear);
		newMission.setEndDate(nextWeek(nextYear));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// rejected, begin last week, end nextMonth, collaborator 0
		newMission = new Mission();

		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setMissionTransport(Transport.Car);
		newMission.setStatus(Status.REJECTED);
		newMission.setStartDate(previousWeek(now));
		newMission.setEndDate(nextMonth(now));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// init, begin now, promo nature, end nextMonth, collaborator 2
		newMission = new Mission();

		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setMissionTransport(Transport.Car);
		newMission.setStatus(Status.INIT);
		newMission.setStartDate(now);
		newMission.setEndDate(nextMonth(now));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// init, begin next month, end nextYear, collaborator 3

		newMission = new Mission();

		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setMissionTransport(Transport.Car);
		newMission.setStatus(Status.INIT);
		newMission.setStartDate(nextMonth(now));
		newMission.setEndDate(nextYear);
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// 3 missions ended, two of which need the night computing for their bonus
		// 2 validated, 1 refused (illustrate an issue here), 2 with disabled natures

		// validated, start last year, end lastyear + 1 week, old promo nature, manager1
		newMission = new Mission();

		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setMissionTransport(Transport.Train);
		newMission.setStatus(Status.REJECTED);
		newMission.setStartDate(lastYear);
		newMission.setEndDate(nextWeek(lastYear));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// validated, start lastyear + 1 week, end lastyear + 1 month, deprecated
		// nature, collaborator 0
		newMission = new Mission();


		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setMissionTransport(Transport.Flight);
		newMission.setStatus(Status.REJECTED);
		newMission.setStartDate(nextWeek(lastYear));
		newMission.setEndDate(nextMonth(lastYear));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);

		// rejected, start lastyear, end lastyear + 1 week, basic nature, admin
		newMission = new Mission();


		rand = giveMeAnumber(natures.size());
		newMission.setNature(natures.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setStartCity(Cities.get(rand));
		rand = giveMeAnumber(Cities.size());
		newMission.setEndCity(Cities.get(rand));

		newMission.setCollaborator(coll);
		newMission.setMissionTransport(Transport.Car);
		newMission.setStatus(Status.REJECTED);
		newMission.setStartDate(lastYear);
		newMission.setEndDate(nextWeek(lastYear));
		newMission = missionRepository.save(newMission);
		missions.add(newMission);
	}

	/**
	 * delete all db
	 */
	private void deleteDb() {
		this.expenseRepository.deleteAll();
		this.missionRepository.deleteAll();
		this.collaboratorRepository.deleteAll();
		this.expenseTypeRepository.deleteAll();
		this.natureRepository.deleteAll();
		this.cityRepository.deleteAll();

	}
}