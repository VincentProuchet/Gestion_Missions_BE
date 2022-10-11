package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Class de test  DAO/City
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
	public void textLowerCaseName() {
		City city1 = new City(0,"   MonTp:,;!,:!eL;lier   ");
		City city2 = new City(0,"   BouRg-----en        braise 2   ");
		City city3 = new City(0,"  01    23      45    67             89   ");
		City city4 = new City(0,"e,?;.:/r!§%*µ¨^£$¤&~t#'{([-----|`_\\q@)]°+=}");
		
		assertTrue(city1.getName().equals("montpellier"));
		assertTrue(city2.getName().equals("bourg-en braise 2"));
		assertTrue(city3.getName().equals("01 23 45 67 89"));
		assertTrue(city4.getName().equals("ert-q"));
	}
}
