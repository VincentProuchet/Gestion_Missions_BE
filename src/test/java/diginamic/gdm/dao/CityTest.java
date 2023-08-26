package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
		City city1 = new City(0,"   MonTpeLlier   ");
		City city2 = new City(0,"   BouRg-en braise 2   ");

		City city5 = new City(0,"aâäàe êëéè uûüù iîïì oôöò");

		assertEquals("montpellier",city1.getName());
		assertEquals("bourg-en braise 2",city2.getName());

		assertEquals("aâäàe êëéè uûüù iîïì oôöò", city5.getName());
	}

	@Test
	public void convertionToDTO() {
		City city = new City(0,"    montpellier     ");
		CityDTO cityDTO = new CityDTO(city);
		assertEquals("montpellier",cityDTO.getName());
		assertEquals(0,cityDTO.getId());

	}
	@Test
	public void convertionFromDTO() {
		CityDTO cityDTO = new CityDTO(0,"  montpellier   ");
		City city = new City(cityDTO);
		assertEquals("montpellier",city.getName());
		assertEquals(0,city.getId());

	}
	@Test
	public void testIsCityNameValid() {
		// pretty long
		assertTrue(City.isValidName("ezdfqefqgmailcom"));
		assertTrue(City.isValidName("bourg-en braise"));
		assertTrue(City.isValidName("béziers"));
		assertTrue(City.isValidName("L' Abergement-Clémenciat"));
		assertTrue(City.isValidName("L'Abergement-Clémenciat"));
		assertTrue(City.isValidName("Gorsafawddacha'idraigodanheddogleddollônpenrhynareurdraethceredigion")); // one of the longest know name
		assertTrue(City.isValidName("Saint-Remy-en-Bouzemont-Saint-Genest-et-Isson"));// one of the longest french city's name
		// composed names
		assertTrue(City.isValidName("PzdfqGefq-Gsmailcom"));

		assertFalse(City.isValidName("PzdfqGefq--Gsmailcom"));
		// space
		assertTrue(City.isValidName("PzdfqGefq gsmailcom"));
		// smallest names
		assertTrue(City.isValidName("aa"));
		assertTrue(City.isValidName("a-a"));
		assertFalse(City.isValidName("a--a"));
		assertFalse(City.isValidName("a"));
		assertFalse(City.isValidName("a-"));
		assertFalse(City.isValidName("-a"));
		assertFalse(City.isValidName("a--"));
		assertFalse(City.isValidName("--a"));
		// longest Names
		String nametotest = new StringBuilder()
				.append("oooooooooPoooooooooPoooooooooPoooooooooPoooooooooPoooooooooP")// 60
				.append("oooooooooPoooooooooPoooooooooPoooooooooPoooooooooPoooooooooP")//120
				.append("oooooooooPoooooooooPoooooooooPoooooooooPoooooooooPoooooooooP")//180
				// JVM Strings seems to explode beyond that
				//.append("oooooooooPoooooo25oooPoooooooooPoooooooooPoooooooooPoooooooooP")// 240
				.toString();
		assertTrue(City.isValidName(nametotest));//25
		nametotest = new StringBuilder()
				.append("o-ooo-ooo-ooo-ooo-oPo-ooo-ooo-ooo-ooo-oPo-ooo-ooo-ooo-ooo-oP")// 60
				.append("o-ooo-ooo-ooo-ooo-oPo-ooo-ooo-ooo-ooo-oPo-ooo-ooo-ooo-ooo-oP")//120
				.append("o-ooo-ooo-ooo-ooo-oPo-ooo-ooo-ooo-ooo-oPo-ooo-ooo-ooo-ooo-oP")//180
				// JVM Strings seems to explode beyond that
				//.append("oooooooooPoooooo25oooPoooooooooPoooooooooPoooooooooPoooooooooP")// 240
				.toString();
		assertTrue(City.isValidName(nametotest));
		// just one more letter
		assertFalse(City.isValidName(nametotest+"o"));
		// or one more
		assertFalse(City.isValidName(nametotest+"o"));

		// accented char
		assertTrue(City.isValidName("PzdfqéGefq-gsmaàilcom"));
		assertTrue(City.isValidName("aâäàeêëéè-aâäàeêëéè"));
		assertTrue(City.isValidName("aâäàeêëéèÿ-aâäàeêëéèÿ"));
		String mirrorValue = "aâäàãeêëéèyÿ";
		assertTrue(City.isValidName(mirrorValue + "-" + mirrorValue));
		mirrorValue =  "uûüùiîïçìoôöòõµñ";
		assertTrue(City.isValidName(mirrorValue + "-" + mirrorValue));
		// punctuation
		String stringToTest;
		String charToTest = ",;:!*^$&\"(_)=?./§%¨£°+~#{[|`\\^@]}<>";
		for (int i = 0; i < charToTest.length(); i++) {
			System.err.println(" valid username "+ i);// this console log is here to help you pinpoint witch test has failed
			stringToTest = new StringBuilder("Pzq").append(charToTest.charAt(i)).append("Gom").toString();
			assertFalse(City.isValidName(stringToTest));
		}

		// numbers //true for now
		assertTrue(City.isValidName("0123456789-123456789"));
		// email
		assertFalse(City.isValidName("ezdfqefq@gmail.com"));

		City city = new City();
		// We test the strip fonction of the setLastname
		city.setName("ezdfqefqgmailcom         ");
		assertTrue(City.isValidName(city.getName()));
		city.setName("        ezdfqefqgmailcom");
		assertTrue(City.isValidName(city.getName()));
		assertTrue(City.isValidName("PzdfqGefqgsmailcom"));
	}
}
