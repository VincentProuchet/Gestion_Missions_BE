package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.*;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.MissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MissionServiceImplTest {

    @Autowired
    private MissionService missionService;

    @Autowired
    private NatureRepository natureRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @BeforeEach
    void init() {
        Nature nature1 = new Nature();
        nature1.setDateOfValidity(LocalDateTime.of(2000, Month.JANUARY, 24, 01, 01, 01));
        nature1.setDescription("nature1Name");
        nature1.setEndOfValidity(null);
        nature1.setCharged(true);
        natureRepository.save(nature1);

        Nature nature2 = new Nature();
        nature2.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setDescription("nature2Name");
        nature2.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setCharged(false);
        natureRepository.save(nature2);

        City city1 = new City();
        city1.setName("city1");
        cityRepository.save(city1);

        City city2 = new City();
        city2.setName("city2");
        cityRepository.save(city2);

        Collaborator collaborator = new Collaborator();
        collaboratorRepository.save(collaborator);

        Mission m1 = new Mission();
        m1.setBonus(new BigDecimal(36));
        m1.setMissionTransport(Transport.Car);
        m1.setNature(nature1);
        m1.setStartCity(city1);
        m1.setEndCity(city2);
        m1.setStartDate(LocalDateTime.now().plusDays(10));
        m1.setEndDate(LocalDateTime.now().plusDays(12));
        m1.setCollaborator(collaborator);
        m1.setStatus(Status.VALIDATED);
        missionRepository.save(m1);

        Mission m2 = new Mission();
        m2.setBonus(new BigDecimal(100));
        m2.setMissionTransport(Transport.Flight);
        m2.setNature(nature1);
        m2.setStartCity(city1);
        m2.setEndCity(city1);
        m2.setStartDate(LocalDateTime.now().plusDays(15));
        m2.setCollaborator(collaborator);
        m2.setEndDate(LocalDateTime.now().plusDays(20));
        m2.setStatus(Status.INIT);
        missionRepository.save(m2);
    }

    @AfterEach
    void clean() {
        missionRepository.deleteAll();
        natureRepository.deleteAll();
        cityRepository.deleteAll();
        collaboratorRepository.deleteAll();
    }

    @Test
    void list() {
        assertEquals(missionService.list().size(), 2);
    }

    @Test
    void create() {

        City city1 = cityRepository.findAll().get(0);
        Nature nature1 = natureRepository.findAll().get(0);
        Collaborator collaborator = collaboratorRepository.findAll().get(0);

        Mission m3 = new Mission();
        m3.setBonus(new BigDecimal(100));
        m3.setMissionTransport(Transport.Flight);
        m3.setNature(nature1);
        m3.setStartCity(city1);
        m3.setEndCity(city1);
        m3.setStartDate(LocalDateTime.now().plusDays(5));
        m3.setEndDate(LocalDateTime.now().plusDays(7));
        m3.setCollaborator(collaborator);


        missionService.create(m3, true);
        assertEquals(missionRepository.findByCollaboratorOrderByStartDateDesc(collaborator).size(), 2);

        m3.setStartDate(LocalDateTime.now().plusDays(29));
        m3.setEndDate(LocalDateTime.now().plusDays(35));
        missionService.create(m3, true);
        assertEquals(missionRepository.findByCollaboratorOrderByStartDateDesc(collaborator).size(), 3);
    }

    @Test
    void delete() {
        missionRepository.findAll().stream().forEach(mission -> {
            missionService.delete(mission.getId());
        });

        assertEquals(missionRepository.findAll().size(), 0);
    }

    @Test
    void update() {
        Collaborator collaborator = collaboratorRepository.findAll().get(0);
        Mission m1 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.VALIDATED).get(0);
        m1.setMissionTransport(Transport.Carshare);
        missionService.update(m1.getId(), m1, true);
        assertFalse(missionRepository.findById(m1.getId()).get().getMissionTransport() == Transport.Carshare);

        Mission m2 = missionRepository.findByCollaboratorAndStatus(collaborator, Status.INIT).get(0);
        LocalDateTime oldEndDateM2 = m2.getEndDate();
        LocalDateTime newEndDateM2 = LocalDateTime.now().plusDays(22);
        m2.setEndDate(newEndDateM2);
        Mission updatedMission = missionService.update(m2.getId(), m2, true);
        assertFalse(missionRepository.findById(m2.getId()).get().getEndDate().isEqual(oldEndDateM2));

        LocalDateTime invalidDate = LocalDateTime.now().plusDays(14);
        m2.setEndDate(invalidDate);
        missionService.update(m2.getId(), m2, true);
        assertFalse(missionRepository.findById(m2.getId()).get().getEndDate().isEqual(invalidDate));

    }
    @Test
    void updateStatus() {
        List<Mission> allMissions = missionRepository.findAll();
        Mission m1 = allMissions.get(0);
        assertTrue(m1.getStatus() != Status.REJECTED);
        missionService.updateStatus(m1.getId(), Status.REJECTED);
        assertTrue(missionRepository.findById(m1.getId()).get().getStatus() == Status.REJECTED);

    }

    @Test
    void isThisMissionValid() {

        City city1 = cityRepository.findAll().get(0);
        Nature nature1 = natureRepository.findAll().get(0);
        Collaborator collaborator = collaboratorRepository.findAll().get(0);

        Mission m3 = new Mission();
        m3.setBonus(new BigDecimal(100));
        m3.setMissionTransport(Transport.Flight);
        m3.setNature(nature1);
        m3.setStartCity(city1);
        m3.setEndCity(city1);
        m3.setStartDate(LocalDateTime.now().plusDays(29));
        m3.setEndDate(LocalDateTime.now().plusDays(35));
        m3.setCollaborator(collaborator);

        assertTrue(missionService.isThisMissionValid(m3, true));

        m3.setStartDate(LocalDateTime.now().plusDays(5));
        m3.setEndDate(LocalDateTime.now().plusDays(7));
        assertFalse(missionService.isThisMissionValid(m3, true));

        m3.setMissionTransport(Transport.Car);
        assertTrue(missionService.isThisMissionValid(m3, true));

        m3.setEndDate(LocalDateTime.now().plusDays(11));
        assertFalse(missionService.isThisMissionValid(m3, true));

        m3.setEndDate(LocalDateTime.now());
        assertFalse(missionService.isThisMissionValid(m3, true));
    }
}