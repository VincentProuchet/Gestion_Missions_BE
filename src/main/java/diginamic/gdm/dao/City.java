package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import diginamic.gdm.dto.CityDTO;
import diginamic.gdm.vars.GDMVars;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents a city
 *
 * @author Joseph
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;
	
	/** name : the name of the city */
	private String name;

	/** Constructeur
	 * @param id
	 * @param name
	 */
	public City(int id,String name) {
		this.id =id;
		this.setName(name);
	}

	/** Constructeur
	 * @param city
	 */
	public City(CityDTO city) {
		if( city.getId() >= 0 ) {
			this.id = city.getId();
		}
		this.setName(city.getName());
	}

	/**
	 * setter
	 * les noms de villes sont tous en minuscule
	 * et passes par un retrait des caractères indésirables dans un nom
	 * @param name
	 * @TODO refactor we won't modify user entry
	 * 			but notify him of a wrong format
	 *
	 */
	public void setName(String name) {
		name = name.strip().toLowerCase();
		this.name =  name;
	}
	/**
	 *
	 * @param name of a city
	 * @return true if the provided string is a valid name for the object
	 */
	public static boolean isValidName(String name) {
		return name.matches(GDMVars.REGEX_CITY_NAMES);
	}

}
