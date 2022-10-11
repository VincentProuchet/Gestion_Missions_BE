package diginamic.gdm.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import diginamic.gdm.dto.CityDTO;
import diginamic.gdm.vars.GDMVars;
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
@Entity
public class City {
	
	private static final String CLEANING_NAME= GDMVars.REGEX_NAMES;
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
		this.id = Math.abs(id);
		this.setName(name);
	}
	
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
	 * et passes par un retrait des caractères indésirables dans un nom
	 * @param name
	 * 
	 */
	public void setName(String name) {
		
		name = name.replaceAll(CLEANING_NAME,"");
		name = name.replaceAll("  "," ").replaceAll("  "," ").replaceAll("  "," ");
		name =name.strip().toLowerCase();		
		this.name =  name;
	}

}
