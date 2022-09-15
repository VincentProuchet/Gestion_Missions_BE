package diginamic.gdm.dto;

import diginamic.gdm.dao.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Vincent
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CityDTO implements DTO<City> {
	
	/** id */
	private int id;

	/** name : the name of the city */
	private String name;
	
	public CityDTO(City city) {
		id = city.getId();
		name = city.getName();
	}
	
	@Override
	public City instantiate() {
		return new City(0, name);
	}
	
}
