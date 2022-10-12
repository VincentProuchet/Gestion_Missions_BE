package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import diginamic.gdm.dao.City;
import diginamic.gdm.dto.CityDTO;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.services.CityService;

/**
 * @author Vincent
 *
 */
@SpringBootTest
@ActiveProfiles("test")
public class CityServiceImplTest {

	@Autowired
	private CityService service;

	private String baseName = "cityservice-test ";// trailing space is here on purpose
	private String baseJammedName = "    cityservice----test   &~#'{([|`_\\^¨°)]+=}$£¤%*,?;.:/!§      ";

	@Test
	public void create() {
		City city = new City(0, baseName + " create");
		this.service.create(city);
	}

	@Test
	public void read() throws BadRequestException {
		// we check the value doesn't exist
		
		assertThrows(BadRequestException.class, () -> this.service.read(baseName + "read"));
		assertThrows(BadRequestException.class, () -> this.service.read(new CityDTO()));
		// we create it than read it
		City city = this.service.create(new City(0, baseJammedName + "read"));
		// we read it
		assertDoesNotThrow(() -> this.service.read(baseName + "read"));
		City city2 = this.service.read(baseName + "read");
		assertDoesNotThrow(() -> this.service.read(city2.getId()));
		assertDoesNotThrow(() -> this.service.read(new CityDTO(city2)));
		assertNotEquals(city, city2);
		assertEquals(city.getId(), city2.getId());
	}

	@Test
	public void update() throws BadRequestException {
		String name = baseName +"update";
		String jammedName = baseName +"update";		
		// we check the value doesn't exist
		assertThrows(BadRequestException.class, () -> this.service.read(name));
		// we create it than read it
		City city = this.service.create(new City(0, jammedName));
		city.setName("new "+jammedName);
		assertDoesNotThrow(()->this.service.update(city.getId(), city));		
	}

	@Test
	public void delete() {
		String name = baseName +"delete";
		String jammedName = baseName +"delete";		
		// we check the value doesn't exist
		assertThrows(BadRequestException.class, () -> this.service.read(name));
		// we create it than read it
		City city = this.service.create(new City(0, jammedName));
		assertDoesNotThrow(()->this.service.read(city.getId()));
		assertThrows(BadRequestException.class,()->this.service.read(0));
		assertDoesNotThrow(()->this.service.delete(city.getId()));
		
	}

}
