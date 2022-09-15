package diginamic.gdm.services.implementations;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dao.Transport;
import diginamic.gdm.repository.MissionRepository;
import diginamic.gdm.repository.NatureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// ce sont des tests d integrations et pas des tests unitaires... je sais pas trop comment tester plus petit ici
@SpringBootTest
class NatureServiceImplTest {

    @Autowired
    private NatureServiceImpl natureService;

    @Autowired
    private NatureRepository natureRepository;

    @Autowired
    private MissionRepository missionRepository;

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

        Mission m1 = new Mission();
        m1.setBonus(new BigDecimal(36));
        m1.setMissionTransport(Transport.Car);
        m1.setNature(nature1);
        missionRepository.save(m1);

        Mission m2 = new Mission();
        m2.setBonus(new BigDecimal(100));
        m2.setMissionTransport(Transport.Flight);
        m2.setNature(nature1);
        missionRepository.save(m2);


    }

    @AfterEach
    void cleanDB() {
        missionRepository.deleteAll(missionRepository.findAll());
        natureRepository.deleteAll(natureRepository.findAll());
    }

    @Test
    void list() {
        assertEquals(natureService.list().size(), 2);
    }

    @Test
    void canBeUpdated() {

        Nature nature3 = new Nature();
        nature3.setId(3);
        nature3.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setDescription("nature3Name");
        nature3.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setCharged(false);

        assertFalse(natureService.canBeUpdated(nature3.getId(), nature3));
        assertFalse(natureService.canBeUpdated(2, nature3));


        Nature nature2 = new Nature();
        nature2.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setDescription("nature2Name");
        nature2.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setCharged(false);
        int idNature2 = natureRepository.findByDescriptionOrderByDateOfValidityDesc(nature2.getDescription()).get(0).getId();
        nature2.setId(idNature2);

        Nature nature1 = natureRepository.findByDescription("nature1Name").get(0);
        assertFalse(natureService.canBeUpdated(nature1.getId(), nature2));
        assertFalse(natureService.canBeUpdated(1000, nature2));

        /* Detailed test

        List<Nature> orderedListOfNatures = natureRepository.findByDescriptionOrderByDateOfValidityDesc(nature2.getDescription());
        Optional<Nature> registeredNatureOptional = natureRepository.findById(2);

        assertFalse(orderedListOfNatures.size() == 0);
        assertFalse(registeredNatureOptional.isEmpty());
        assertFalse(!(orderedListOfNatures.get(0).getId() == registeredNatureOptional.get().getId()));
        */

        assertTrue(natureService.canBeUpdated(idNature2, nature2));

        assertTrue(natureService.canBeUpdated(nature1.getId(), nature1));


        // test if the id is not the most recent with the same description

    }

    @Test
    void canBeAdded() {

        // note : this method does not check validity
        Nature nature3 = new Nature();
        nature3.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setDescription("nature3Name");
        nature3.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setCharged(false);

        assertTrue(natureService.canBeAdded(nature3), "the list found : " + natureRepository.findByDescription(nature3.getDescription()) + " the size : " + natureRepository.findByDescription(nature3.getDescription()).size());

        Nature nature4 = new Nature();
        nature4.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature4.setDescription("nature1Name");
        nature4.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature4.setCharged(false);

        assertFalse(natureService.canBeAdded(nature4), "the list found : " + natureRepository.findByDescription(nature3.getDescription()) + " the size : " + natureRepository.findByDescription(nature3.getDescription()).size());
    }

    @Test
    void isAValidNature() {
        Nature invalidNature1 = new Nature();
        invalidNature1.setDateOfValidity(LocalDateTime.now());
        invalidNature1.setEndOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));

        assertFalse(natureService.isAValidNature(invalidNature1));

        Nature validNature = new Nature();
        validNature.setDateOfValidity(LocalDateTime.now());
        validNature.setEndOfValidity(null);
        validNature.setDescription("validNatureName");

        assertTrue(natureService.isAValidNature(validNature));
    }


    @Test
    void delete() {
        int idNature1 = natureRepository.findByDescription("nature1Name").get(0).getId();
        int idNature2 = natureRepository.findByDescription("nature2Name").get(0).getId();

        natureService.delete(idNature2);
        assertTrue(natureRepository.findById(idNature2).isEmpty());


        Optional<Nature> optionalNature1Before = natureRepository.findById(idNature1);
        assertNull(optionalNature1Before.get().getEndOfValidity());
        assertTrue(natureService.isThisNatureInUse(optionalNature1Before.get()));
        natureService.delete(idNature1);

        Optional<Nature> optionalNature1 = natureRepository.findById(idNature1);
        assertTrue(optionalNature1.isPresent());
        assertTrue(optionalNature1.get().getEndOfValidity() != null, " end of validity : " + optionalNature1.get().getEndOfValidity());
    }

    @Test
    void isThisNatureInUse() {
        assertTrue(natureService.isThisNatureInUse(natureRepository.findByDescription("nature1Name").get(0)));
        assertFalse(natureService.isThisNatureInUse(natureRepository.findByDescription("nature2Name").get(0)));
    }
    @Test
    void create() {

        Nature nature3 = new Nature();
        nature3.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setDescription("nature3Name");
        nature3.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setCharged(false);

        natureService.create(nature3);
        assertNotNull(natureRepository.findByDescription("nature3Name").get(0));


        Nature nature1Bis = new Nature();
        nature1Bis.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature1Bis.setDescription("nature1Name");
        nature1Bis.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature1Bis.setCharged(false);

        natureService.create(nature1Bis);
        assertTrue(natureRepository.findByDescription("nature1Name").get(0).isCharged());


        Nature invalidNature1 = new Nature();
        invalidNature1.setDateOfValidity(LocalDateTime.now());
        invalidNature1.setEndOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature1Bis.setDescription("invalidNature1name");

        natureService.create(invalidNature1);
        assertEquals(natureRepository.findByDescription("invalidNature1name").size(), 0);

    }

    @Test
    void getActiveNatures() {
        List<Nature> activeNatures = natureService.getActiveNatures();
        assertEquals(activeNatures.size(), 1);
        assertTrue(activeNatures.get(0).getDescription().equals("nature1Name"));
    }

    @Test
    void update() {
        Nature nature3 = new Nature();
        nature3.setId(3);
        nature3.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setDescription("nature3Name");
        nature3.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature3.setCharged(false);

        natureService.update(nature3.getId(), nature3);
        assertFalse(natureRepository.findById(nature3.getId()).isPresent());
        assertEquals(natureRepository.findByDescription("nature3name").size(), 0);


        Nature nature2 = new Nature();
        nature2.setDateOfValidity(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setDescription("nature2Name");
        nature2.setEndOfValidity(LocalDateTime.of(2021, Month.DECEMBER, 10, 10, 10, 10));
        nature2.setCharged(true);
        int idNature2 = natureRepository.findByDescriptionOrderByDateOfValidityDesc(nature2.getDescription()).get(0).getId();
        nature2.setId(idNature2);

        natureService.update(1000, nature2);
        assertFalse(natureRepository.findById(1000).isPresent());


        natureService.update(idNature2, nature2);
        assertTrue(natureRepository.findById(idNature2).get().isCharged());
        assertEquals(natureRepository.findByDescription("nature2name").size(), 1);

        Nature nature1 = natureRepository.findByDescription("nature1name").get(0);
        nature1.setCharged(!nature1.isCharged());
        natureService.update(nature1.getId(), nature1);

        List<Nature> natures1oldAndNew = natureRepository.findByDescription("nature1name");
        assertEquals(natures1oldAndNew.size(), 2);
        assertTrue(natures1oldAndNew.get(0).isCharged() != natures1oldAndNew.get(1).isCharged());


    }



}