package diginamic.gdm.services.implementations;

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
import diginamic.gdm.services.MissionService;
import diginamic.gdm.services.ScheduledTasksService;
import diginamic.gdm.utilities.testTools;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests use the data created by InitDataDB
 * 
 * @author Joseph
 */
@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(Lifecycle.PER_CLASS)
class ScheduledTasksServiceImplTest {

    @Autowired
    private MissionService missionService;
    @Autowired
    private NatureRepository natureRepository;
    @Autowired
    private CollaboratorRepository collaboratorRepository;
    @Autowired
    private MissionRepository missionRepository;
    
    
    @Autowired
    private ScheduledTasksService scheduledTasksService;
    
    @Autowired
	private testTools tools;

	private String description = "sheduledTaskServTest";

	private List<Mission> missions = new ArrayList<>();

	private List<Nature> natures = new ArrayList<>();

	private List<City> cities = new ArrayList<>();

	private List<Collaborator> collaborators = new ArrayList<>();

	private Collaborator manager;
	private final float marginError = 0.001f;
    
    
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
		//inactive nature
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
		// 								0
		m1 = new Mission();
		m1.setBonus(36);
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(1));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(1));
		m1.setStartDate(LocalDateTime.now().minusYears(1));
		m1.setEndDate(LocalDateTime.now().minusYears(1).plusDays(12));
		m1.setCollaborator(collaborators.get(0));
		m1.setStatus(Status.VALIDATED);
		this.missions.add(index, missionRepository.save(m1));
		index++;
		// 								1
		m1 = new Mission();
		m1.setBonus(30f);
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(1));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(1));
		m1.setStartDate(LocalDateTime.now().minusYears(1).plusDays(15));
		m1.setEndDate(LocalDateTime.now().minusYears(1).plusDays(75));
		m1.setCollaborator(collaborators.get(1));
		m1.setStatus(Status.VALIDATED);
		this.missions.add(index, missionRepository.save(m1));

		// 								2
		index++;
		m1 = new Mission();
		m1.setBonus( 100f);
		m1.setMissionTransport(Transport.Flight);
		m1.setNature(natures.get(2));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(0));
		m1.setStartDate(LocalDateTime.now().plusDays(15));
		m1.setCollaborator(collaborators.get(2));
		m1.setEndDate(LocalDateTime.now().plusDays(20));
		m1.setStatus(Status.INIT);
		this.missions.add(index, missionRepository.save(m1));

		// 							3
		index++;
		m1 = new Mission();
		m1.setBonus( 36f);
		m1.setMissionTransport(Transport.Car);
		m1.setNature(natures.get(1));
		m1.setStartCity(this.cities.get(0));
		m1.setEndCity(this.cities.get(1));
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setCollaborator(collaborators.get(3));
		m1.setStatus(Status.INIT);
		this.missions.add(index, missionRepository.save(m1));
		//  						4
		index++;
		m1 = new Mission();
		m1.setNature(natures.get(1));
		m1.setBonus( 36f);
		m1.setMissionTransport(Transport.Car);
		m1.setCollaborator(collaborators.get(4));
		m1.setStatus(Status.INIT);
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(0));
		this.missions.add(index, missionRepository.save(m1));

		// 							5
		index++;
		m1 = new Mission();
		m1.setNature(natures.get(0));
		m1.setBonus( 100f);
		m1.setMissionTransport(Transport.Flight);
		m1.setCollaborator(collaborators.get(5));
		m1.setStatus(Status.WAITING_VALIDATION);
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(0));
		this.missions.add(index, missionRepository.save(m1));

		// 							6
		index++;
		m1 = new Mission();
		m1.setNature(natures.get(0));
		m1.setBonus( 100f);
		m1.setMissionTransport(Transport.Flight);
		m1.setCollaborator(collaborators.get(5));
		m1.setStatus(Status.INIT);
		m1.setStartDate(LocalDateTime.now().plusDays(10));
		m1.setEndDate(LocalDateTime.now().plusDays(12));
		m1.setStartCity(cities.get(0));
		m1.setEndCity(cities.get(0));
		this.missions.add(index, missionRepository.save(m1));

	}

    @Test
    void workedDays() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 21, 1,1);
        Mission fakeMission = new Mission();
        fakeMission.setStartDate(now);
        fakeMission.setEndDate(now.plusDays(7));
        assertEquals(this.scheduledTasksService.workedDays(fakeMission), 5);
        fakeMission.setEndDate(now.plusDays(8));
        assertEquals(this.scheduledTasksService.workedDays(fakeMission), 6);
        fakeMission.setEndDate(now.plusDays(12));
        assertEquals(this.scheduledTasksService.workedDays(fakeMission), 8);
    }
    /**
     * @TODO to refactor bigDecimal clearly poses a lot of trouble because of the their sizes
     * @throws BadRequestException
     */
    @Test
    void computeBonusForCompletedMissions() throws BadRequestException {
        List<Mission> missionsWithBonusesToCompute = missionService.completedMissionsToCompute();
        assertNotEquals(0, missionsWithBonusesToCompute.size());
        this.scheduledTasksService.computeBonusForCompletedMissions();
        Nature nature1 = new Nature();
        float bonus = 0f;
        float actualValue = 0f;
        Mission mission  ;
        //for (Mission mission : missionsWithBonusesToCompute) {
        // oui on n'en test que 100 parce que Maven n'aime pas attendre
        for (int i=0 ;i<100 ;i++) {
        	mission = missionsWithBonusesToCompute.get(i);
        	actualValue =  missionService.read(mission.getId()).getBonus();
        	assertEquals(0, mission.getBonus());
        	nature1 = mission.getNature();
        	bonus =  (this.scheduledTasksService.workedDays(mission) * nature1.getBonusPercentage() / 100)*(nature1.getTjm());
        	assertTrue(bonus + marginError >= actualValue);
        	assertTrue(bonus - marginError <= actualValue);
			
		}

    }

    @Test
    void changeMissionStatus() throws BadRequestException {
        assertNotEquals(0,missionService.missionsToPutInWaitingValidation().size());
        scheduledTasksService.changeMissionStatus();
        assertEquals(0,missionService.missionsToPutInWaitingValidation().size());
    }

}