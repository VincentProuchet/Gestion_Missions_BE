package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents the nature of a mission
 * 
 * @author Joseph
 *
 */
/**
 * @author Vincent
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Nature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;

	/** givesBonus : does a mission with this nature give a bonus? */
	@Column(nullable = false)
	private boolean givesBonus =false;

	/** charged : is this mission charged to the client */
	@Column(nullable = false)
	private boolean charged = false;

	/** tjm : the amount charged per working day to the client */
	@Column(nullable = false)
	private BigDecimal tjm = BigDecimal.valueOf(0) ;

	/** bonusPercentage : the percentage of the charge which */
	@Column(nullable = false)
	private Float bonusPercentage = 0f;

	/** dateOfValidity : the date at which this nature can be used */
	@Column(nullable = false)
	private LocalDateTime dateOfValidity;

	/**
	 * endOfValidity : the nature can't be used past this date if null, the nature
	 * is currently valid
	 */
	private LocalDateTime endOfValidity = null;

	/** description : the name of the nature */
	@Column(nullable = false)
	private String description;
	
	/**
	 * Setter
	 * will set tjm propertie of the Nature 
	 * to the absolute value provided
	 * sooooo ... no negative 
	 * @param tjm provided
	 */
	public void setTjm(int tjm) {
		this.tjm = BigDecimal.valueOf(tjm);
	}
	/**
	 * Setter
	 * made because lombock is kinda not accepting overloading 
	 * @param tjm
	 */
	public void setTjm(BigDecimal tjm) {
		
		this.tjm = tjm;
	}
	
	public void setDescription(String description) {
		this.description = description.toLowerCase();		
	}
}
