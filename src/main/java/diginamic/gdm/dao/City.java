package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import diginamic.gdm.dto.CityDTO;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
public class City {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** name : the name of the city */
	private String name;
	
	/** Constructeur
	 * @param city
	 */
	public City(CityDTO city) {
		this.id = city.getId();
		this.name = city.getName();
	}
	
	/**
	 * setter 
	 * les noms de villes sont tous en minuscule
	 * @param name
	 */
	public void setName(String name) {
		this.name =  name.strip().toLowerCase();
	}

}
