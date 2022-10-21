package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import diginamic.gdm.dto.NatureDTO;

/**
 * classe de test de DAO-DTO/Nature on teste aussi la conversion depuis et vers
 * le DTO
 * 
 * @author Vincent
 *
 */
public class NatureTest {
	
	private float marginError = 0.001f;
	private int marginTime = 1;// in minutes 
	private int id = 256;
	private String description = "nature-isolé perdue";
	private String jammedDescription = " na,?;.:/!§%*^¨$£¤&~#'([|`_\\@=+<>ture---isolé       perdue";
	private int tjm = 2500;
	private float bonus = 4.32f;

	@Test
	public void DAObaseValues() {
		Nature nature = new Nature();
		assertEquals(0, nature.getId());
		assertFalse(nature.isGivesBonus());
		assertFalse(nature.isCharged());
		assertNull(nature.getDateOfValidity());
		assertNull(nature.getEndOfValidity());
		assertEquals(0f, nature.getTjm());
		assertEquals(0f, nature.getBonusPercentage());
		assertNull(nature.getDescription());

	}

	@Test
	public void DAOsetValues() {
		LocalDateTime beforeCreation = LocalDateTime.now();
		LocalDateTime afterCreation;
		Nature nature = new Nature();
		nature.setId(this.id);
		nature.setDescription(this.jammedDescription);
		nature.setDateOfValidity(LocalDateTime.now().plusMinutes(5));
		nature.setEndOfValidity(LocalDateTime.now().plusMinutes(6));
		nature.setTjm(this.tjm);
		nature.setBonusPercentage(this.bonus);
		nature.setGivesBonus(true);
		nature.setCharged(true);
		
		
		assertEquals(this.id, nature.getId());
		assertEquals(this.tjm, nature.getTjm());
		assertEquals(this.description, nature.getDescription());
		// that end of validity is supposed to be null
		assertTrue(nature.isCharged());
		assertTrue(nature.isGivesBonus());
		// here we test the margin error of float
		assertTrue(nature.getBonusPercentage() + marginError >= nature.getBonusPercentage());
		assertTrue(nature.getBonusPercentage() - marginError <= nature.getBonusPercentage());
		
		// we check if date of validity is now
		assertTrue(nature.getDateOfValidity().isAfter(beforeCreation));
		assertTrue(nature.getEndOfValidity().isAfter(beforeCreation));
		afterCreation = LocalDateTime.now().plusMinutes(10);
		//you have to give it some delta or the test can't make the difference,
		// this is because the localdateTime isn't precise enought to compare
		// micro-seconds
		assertTrue(nature.getDateOfValidity().isBefore(afterCreation));
		assertTrue(nature.getEndOfValidity().isBefore(afterCreation));

	}

	@Test
	public void convertionToDTO() {
		LocalDateTime beforeCreation = LocalDateTime.now().minusMinutes(this.marginTime);
		LocalDateTime afterCreation = LocalDateTime.now().plusMinutes(this.marginTime);
		Nature natureDAO = new Nature();
		natureDAO.setId(this.id);
		natureDAO.setDescription(this.description);
		natureDAO.setDateOfValidity(LocalDateTime.now());
		natureDAO.setEndOfValidity(LocalDateTime.now());
		natureDAO.setTjm(this.tjm);
		natureDAO.setBonusPercentage(this.bonus);
		natureDAO.setGivesBonus(true);
		natureDAO.setCharged(true);
		
		NatureDTO natureDTO = new NatureDTO(natureDAO);
		assertEquals(this.id, natureDTO.getId());
		assertEquals(this.tjm, natureDTO.getTjm());
		assertEquals(this.description, natureDTO.getDescription());
		// that end of validity is supposed to be null
		assertTrue(natureDTO.isCharged());
		assertTrue(natureDTO.isGivesBonus());
		// here we test the margin error of float
		assertTrue(natureDTO.getBonusPercentage() + marginError >= natureDTO.getBonusPercentage());
		assertTrue(natureDTO.getBonusPercentage() - marginError <= natureDTO.getBonusPercentage());
		
		// we check if date of validity is now
		assertTrue(natureDTO.getDateOfValidity().isAfter(beforeCreation));
		assertTrue(natureDTO.getEndOfValidity().isAfter(beforeCreation));
		//you have to give it some delta or the test can't make the difference,
		// this is because the localdateTime isn't precise enought to compare
		// micro-seconds
		assertTrue(natureDTO.getDateOfValidity().isBefore(afterCreation));
		assertTrue(natureDTO.getEndOfValidity().isBefore(afterCreation));

	}

	@Test
	public void convertionFromDTO() {
		LocalDateTime beforeCreation = LocalDateTime.now().minusMinutes(this.marginTime);
		LocalDateTime afterCreation = LocalDateTime.now().plusMinutes(this.marginTime);
		NatureDTO natureDTO = new NatureDTO();
		natureDTO.setId(this.id);
		natureDTO.setDescription(this.description);
		natureDTO.setDateOfValidity(LocalDateTime.now());
		natureDTO.setEndOfValidity(LocalDateTime.now());
		natureDTO.setTjm((this.tjm));
		natureDTO.setBonusPercentage(this.bonus);
		natureDTO.setGivesBonus(true);
		natureDTO.setCharged(true);
		
		Nature nature = new Nature(natureDTO);
		assertEquals(this.id, nature.getId());
		assertEquals(this.tjm, nature.getTjm());
		assertEquals(this.description, nature.getDescription());
		// that end of validity is supposed to be null
		assertTrue(nature.isCharged());
		assertTrue(nature.isGivesBonus());
		// here we test the margin error of float
		assertTrue(nature.getBonusPercentage() + marginError >= nature.getBonusPercentage());
		assertTrue(nature.getBonusPercentage() - marginError <= nature.getBonusPercentage());
		
		// we check if date of validity is now
		assertTrue(nature.getDateOfValidity().isAfter(beforeCreation));
		assertTrue(nature.getEndOfValidity().isAfter(beforeCreation));
		
		//you have to give it some delta or the test can't make the difference,
		// this is because the localdateTime isn't precise enought to compare
		// micro-seconds
		assertTrue(nature.getDateOfValidity().isBefore(afterCreation));
		assertTrue(nature.getEndOfValidity().isBefore(afterCreation));
		
	}

}
