package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.utilities.testTools;

/**
 * @Todo to refactor
 * @author Joseph
 * @author Vincent
 *
 */
@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class MissionServiceImplTest {

	/**
	 * missionService we test a specific implementation if you create another
	 * implementation you create another test class
	 */
	@Autowired
	private MissionServiceImpl missionService;

	@Autowired
	private NatureRepository natureRepository;
	@Autowired
	private MissionRepository missionRepository;
	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@Autowired
	private testTools tools;

	private String description = "missionServTest";

	private List<Mission> missions = new ArrayList<>();

	private List<Nature> natures = new ArrayList<>();

	private List<City> cities = new ArrayList<>();

	private List<Collaborator> collaborators = new ArrayList<>();

	private Collaborator manager;

	@BeforeAll
	public void init() {
		Nature nature = new Nature();
		nature.setDateOfValidity(LocalDateTime.of(2020, Month.JANUARY, 24, 1, 1, 1));
		nature.setDescription(description + "nature");
		nature.setEndOfValidity(null);
		nature.setCharged(true);
		this.natures.add(0, natureRepository.save(nature));

		Nature nature1 = new Nature();
		nature1.setDateOfValidity(LocalDateTime.of(2020, Month.JANUARY, 24, 1, 1, 1));
		nature1.setDescription(description + "nature1");
		nature1.setEndOfValidity(null);
		nature1.setCharged(true);
		this.natures.add(1, natureRepository.save(nature1));

		Nature nature2 = new Nature();
		nature2.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
		nature2.setDescription(description + "nature2");
		nature2.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
		nature2.setCharged(false);
		this.natures.add(2, natureRepository.save(nature2));

		this.cities = this.tools.createCities(description);

		Collaborator collaborator;
		int index = 0;

		// create
		this.collaborators.add(index, this.tools.CreateCollaborator(description));
		// update
		index++; // 1
		this.collaborators.add(index, this.tools.CreateCollaborator(description + index));
		index++;// 2
		this.collaborators.add(index, this.tools.CreateCollaborator(description + index));
		// updateStatus
		index++;// 3
		this.collaborators.add(index, this.tools.CreateCollaborator(description + index));
		// missionTovalidate
		index++;// 4
		this.collaborators.add(index, this.tools.CreateCollaborator(description + index));
		index++;// 5
		this.collaborators.add(index, this.tools.CreateCollaborator(description + index));
		// is this mission valid
		index++;// 6
		this.collaborators.add(index, this.tools.CreateCollaborator(description + index));

		// no mission for this one
		index++;//
		collaborator = this.tools.CreateCollaborator(description + index);
		collaborator.setActive(false);
		this.collaborators.add(index, collaborator);

		this.manager = this.tools.CreateCollaborator(description + "manager");

		for (Collaborator coll : collaborators) {
			coll.setManager(manager);
			coll = collaboratorRepository.save(coll);
		}
		manager.setTeam(collaborators.stream().collect(Collectors.toSet()));
		manager = collaboratorRepository.save(manager);

		Mission m1 = new Mission();
		index = 0;

		// create // not used 0
		m1 = new Mission();
		m1.setBonus(36f);
        m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(1));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(1));
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setCollaborator(collaborators.get(0));
		m1.setStatus(Status.VALIDATED);
		this.missions.add(index, missionRepository.save(m1));
		index++;
		// Update // validated Mission can't be updated 1
		m1 = new Mission();
		m1.setBonus(36f);
		 m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(1));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(1));
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setCollaborator(collaborators.get(1));
		m1.setStatus(Status.VALIDATED);
		this.missions.add(index, missionRepository.save(m1));

		// Update // init mission can be updated 2
		index++;
		m1 = new Mission();
		m1.setBonus(100f);
		 m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Flight);
		m1.setNature(natures.get(2));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(0));
		m1.setStartDate(LocalDateTime.now().plusDays(15));
		m1.setCollaborator(collaborators.get(2));
		m1.setEndDate(LocalDateTime.now().plusDays(20));
		m1.setStatus(Status.INIT);
		this.missions.add(index, missionRepository.save(m1));

		// updateStatus // to put it to reject 3
		index++;
		m1 = new Mission();
		m1.setBonus(36f);
		 m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(1));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(1));
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setCollaborator(collaborators.get(3));
		m1.setStatus(Status.INIT);
		this.missions.add(index, missionRepository.save(m1));
		// mission to validate 4
		index++;
		m1 = new Mission();

		m1.setNature(natures.get(1));
		m1.setBonus(36f);
		 m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Car);
		m1.setCollaborator(collaborators.get(4));
		m1.setStatus(Status.WAITING_VALIDATION);
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(0));
		this.missions.add(index, missionRepository.save(m1));

		// mission to validate 5
		index++;
		m1 = new Mission();
		m1.setNature(natures.get(0));
		m1.setBonus(100f);
		 m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Flight);
		m1.setCollaborator(collaborators.get(5));
		m1.setStatus(Status.WAITING_VALIDATION);
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(0));
		this.missions.add(index, missionRepository.save(m1));

		// mission to delete								6
		index++;
		m1 = new Mission();
		m1.setNature(natures.get(0));
		m1.setBonus(100f);
		 m1.setHasBonusBeenEvaluated(true);
		m1.setMissionTransport(Transport.Flight);
		m1.setCollaborator(collaborators.get(5));
		m1.setStatus(Status.REJECTED);
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(0));
		this.missions.add(index, missionRepository.save(m1));

	}

	@Test
	@Order(1)
	void list() {
		assertNotEquals(0, missionService.list().size());
	}

	@Test
	@Order(2)
	void create() throws BadRequestException {

		Mission m3 = new Mission();
		m3.setBonus(100f);
		m3.setMissionTransport(Transport.Flight);
		m3.setNature(this.natures.get(0));
		m3.setStartCity(this.cities.get(0));
		m3.setEndCity(this.cities.get(1));
		m3.setStartDate(LocalDateTime.now().plusDays(5));
		m3.setEndDate(LocalDateTime.now().plusDays(7));
		m3.setCollaborator(collaborators.get(0));
		// mission with flight tranpsort must leave a +7 days delay
		assertThrows(BadRequestException.class, () -> missionService.create(m3, true));
		m3.setStartDate(LocalDateTime.now().plusDays(11));
		m3.setEndDate(LocalDateTime.now().plusDays(35));
		assertThrows(BadRequestException.class, () -> missionService.create(m3, true));
		m3.setStartDate(LocalDateTime.now().plusDays(13));
		m3.setEndDate(LocalDateTime.now().plusDays(35));
		assertDoesNotThrow(() -> missionService.create(m3, true));


	}

	@Test
	@Order(3)
	public void read() {
		assertThrows(BadRequestException.class, () -> this.missionService.read(0));
		assertThrows(BadRequestException.class, () -> this.missionService.read(Integer.MAX_VALUE));
		assertDoesNotThrow(() -> this.missionService.read(1));

	}

	/**
	 * on update missions status must be checked mission data intégrity mus be
	 * checked
	 *
	 * @throws BadRequestException
	 */
	@Test
	@Order(4)
	void update() throws BadRequestException {

		Mission m1 = this.missions.get(1);
		// changing transport on a validated mission
		m1.setMissionTransport(Transport.Carshare);
		// should throw an error, mission is Validated and can't be modified
		assertThrows(BadRequestException.class, () -> missionService.update(m1.getId(), m1, true));
		this.missions.add(1, this.missionService.read(m1.getId()));

		Mission m2 = this.missions.get(2);
		m2.setStatus(Status.INIT);
		this.missionRepository.save(m2);
		// changing end date on a mission with init status
		m2.setStartDate(tools.nextWorkDay(LocalDateTime.now().plusDays(15)));
		m2.setEndDate(tools.nextWorkDay(LocalDateTime.now().plusDays(22)));
		// should happen normaly
		assertDoesNotThrow(() -> missionService.update(m2.getId(), m2, true));
		// now if we try with bad dates
		m2.setStartDate(LocalDateTime.now().plusDays(15));
		m2.setEndDate(LocalDateTime.now().plusDays(14));
		// it should throw an error
		assertThrows(BadRequestException.class, () -> missionService.update(m2.getId(), m2, true));

	}

	@Test
	@Order(5)
	void missionsToValidate() throws BadRequestException {

		List<Mission> missionsToValidate = missionService.missionsToValidate(manager.getId());
		// we check that they all have init status
		assertTrue(missionsToValidate.stream().allMatch(mission -> mission.getStatus() != Status.INIT));
		// we get their count
		long size = missionsToValidate.stream().filter(mission -> mission.getStatus() == Status.INIT).count();
		System.out.println(size);
		// now we check if we have the same count as waiting validation

	}

	@Test
	@Order(7)
	void updateStatus() throws BadRequestException {

		Mission m1 = this.missions.get(3);
		assertEquals(m1.getStatus(), Status.INIT);
		assertNotSame(m1.getStatus(), Status.REJECTED);
		assertNotSame(m1.getStatus(), Status.VALIDATED);
		assertNotSame(m1.getStatus(), Status.WAITING_VALIDATION);
		assertNotSame(m1.getStatus(), Status.ENDED);
		assertThrows( BadRequestException.class,() -> missionService.RejectMission(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.validateMission(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.resetMission(m1.getId()));
		assertDoesNotThrow(() -> missionService.NightComputing(m1.getId()));

		assertSame(missionRepository.findById(m1.getId()).get().getStatus(), Status.WAITING_VALIDATION);

		assertThrows( BadRequestException.class,() -> missionService.NightComputing(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.resetMission(m1.getId()));
		assertDoesNotThrow(() -> missionService.validateMission(m1.getId()));

		assertSame(missionRepository.findById(m1.getId()).get().getStatus(), Status.VALIDATED);

		assertThrows( BadRequestException.class,() -> missionService.validateMission(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.RejectMission(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.NightComputing(m1.getId()));
		assertDoesNotThrow(() -> missionService.resetMission(m1.getId()));

		assertSame(missionRepository.findById(m1.getId()).get().getStatus(), Status.WAITING_VALIDATION);
		assertDoesNotThrow(() -> missionService.RejectMission(m1.getId()));
		assertSame(missionRepository.findById(m1.getId()).get().getStatus(), Status.REJECTED);

		assertThrows( BadRequestException.class,() -> missionService.NightComputing(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.RejectMission(m1.getId()));
		assertThrows( BadRequestException.class,() -> missionService.validateMission(m1.getId()));

	}

	/**
	 * this one doesn't require a mission to be a repository mainly because its
	 * first purpose is to test mission before saving them
	 */
	@Test
	@Order(6)
	void isThisMissionValid() {

		City city = cities.get(0);
		Nature nature = natures.get(0);
		Collaborator collaborator = collaborators.get(6);

		Mission mission = new Mission();
		assertThrows(BadRequestException.class,()->missionService.isThisMissionValid(mission,false));
		mission.setBonus(100f);
		mission.setMissionTransport(Transport.Flight);
		mission.setNature(nature);
		mission.setStartCity(city);
		mission.setEndCity(city);
		mission.setStartDate(tools.nextWorkDay(LocalDateTime.now().plusDays(29)));
		mission.setEndDate(tools.nextWorkDay(LocalDateTime.now().plusDays(35)));
		mission.setCollaborator(collaborator);
		// the created mission is supposed to be valid
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));
		// no collaborator
		mission.setCollaborator(null);
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		mission.setCollaborator(collaborator);
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));
		// no nature
		mission.setNature(null);
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		mission.setNature(nature);
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));
		// no startCity
		mission.setStartCity(null);
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		mission.setStartCity(city);
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));
		// no Arrival city
		mission.setEndCity(null);
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		mission.setEndCity(city);
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));

		// week end for start day
		mission.setStartDate(tools.nextWeekEnd(LocalDateTime.now().plusDays(15)));
		mission.setEndDate(tools.nextWorkDay(LocalDateTime.now().plusDays(30)));
		System.err.println(mission.getStartDate().getDayOfWeek());
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		// week end for end day
		mission.setStartDate(tools.nextWorkDay(LocalDateTime.now().plusDays(15)));
		mission.setEndDate(tools.nextWeekEnd(LocalDateTime.now().plusDays(30)));
		System.err.println(mission.getEndDate().getDayOfWeek());
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		// end date before start date
		mission.setEndDate(tools.nextWorkDay(LocalDateTime.now()));
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));

		// dates incompatible with flightTransport
		mission.setMissionTransport(Transport.Flight);
		LocalDateTime start = LocalDateTime.now().plusDays(6);
		mission.setStartDate(start);
		mission.setEndDate(tools.nextWorkDay(start.plusDays(15)));
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, true));

		mission.setMissionTransport(Transport.Car);
		mission.setStartDate(tools.nextWorkDay(start));
		// changed transport
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));

		mission.setNature(natures.get(2));
		// inactive nature
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission, false));
		mission.setNature(natures.get(0));
		assertDoesNotThrow(() -> missionService.isThisMissionValid(mission, false));
		assertDoesNotThrow(() -> this.missionService.create(mission));

		Mission mission1 = new Mission();

		mission1.setBonus(100f);
		mission1.setMissionTransport(Transport.Flight);
		mission1.setNature(nature);
		mission1.setStartCity(city);
		mission1.setEndCity(city);
		mission1.setStartDate(tools.nextWorkDay(LocalDateTime.now().plusDays(6)));
		mission1.setEndDate(tools.nextWorkDay(LocalDateTime.now().plusDays(25)));
		mission1.setCollaborator(collaborator);
		assertThrows(BadRequestException.class, () -> missionService.isThisMissionValid(mission1, false));

	}

	@Test
	@Order(8)
	private void delete() {
		// validated
		assertThrows(BadRequestException.class, () -> this.missionService.delete(missions.get(0).getId()));
		// validated
		assertThrows(BadRequestException.class, () -> this.missionService.delete(missions.get(1).getId()));
		// init
		assertDoesNotThrow(() -> this.missionService.delete(missions.get(2).getId()));
		// allready deleted
		assertThrows(BadRequestException.class, () -> this.missionService.delete(missions.get(2).getId()));
		// validated
		assertThrows(BadRequestException.class, () -> this.missionService.delete(missions.get(3).getId()));
		// waiting validation
		assertDoesNotThrow(() -> this.missionService.delete(missions.get(4).getId()));
		assertDoesNotThrow(() -> this.missionService.delete(missions.get(5).getId()));

	}
}