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
	private int id = 0;

	/** name : the name of the city */
	private String name = "";
	
	/** Constructeur
	 * @param city
	 */
	public CityDTO(City city) {
		id = city.getId();
		name = city.getName();
	}
	
	@Override
	public City instantiate() {
		return new City(0, name);
	}
	
	/**
	 * @return Citie's name in lowerCase
	 */
	public String getName() {
		return this.name.toLowerCase();
	}
	
}
