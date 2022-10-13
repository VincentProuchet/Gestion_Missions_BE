package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import diginamic.gdm.dto.CityDTO;

/**
 * classe de test de DAO-DTO/City 
 * on teste aussi la conversion depuis et vers le DTO
 * 
 * @author Vincent
 *
 */
public class CityTest {
	/**
	 * Cities are stored in lowerCase
	 * more filter should be put in place 
	 * specially in regard of special characters
	 */
	@Test
	public void nameConventionCompliance() {
		City city1 = new City(0,"   MonTp:,;!,:!eL;lier   ");
		City city2 = new City(0,"   BouRg-----en        braise 2   ");
		City city3 = new City(0,"  01    23      45    67             89   ");
		City city4 = new City(0,"e,?;.:/r!§%*¨^£$¤&~t#'{([-----|`_\\q@)]°+=}");
		City city5 = new City(0,"aâäàe êëéè uûüù iîïì oôöò");
		
		assertEquals("montpellier",city1.getName());
		assertEquals("bourg-en braise 2",city2.getName());
		assertEquals("01 23 45 67 89",city3.getName());
		assertEquals("ert-q",city4.getName());
		assertEquals("aâäàe êëéè uûüù iîïì oôöò", city5.getName());
	}
	
	@Test
	public void convertionToDTO() {
		City city = new City(0,"   MonTp:,;!,:!eL;lier   ");
		CityDTO cityDTO = new CityDTO(city);
		assertTrue(cityDTO.getName().equals("montpellier"));
		assertEquals(0,cityDTO.getId());
				
	}
	@Test
	public void convertionFromDTO() {
		CityDTO cityDTO = new CityDTO(0,"   MonTp:,;!,:!eL;lier   ");
		City city = new City(cityDTO);
		assertTrue(city.getName().equals("montpellier"));
		assertEquals(0,city.getId());
				
	}
}
