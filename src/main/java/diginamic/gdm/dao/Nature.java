package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Nature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** givesBonus : does a mission with this nature give a bonus? */
	private boolean givesBonus;

	/** charged : is this mission charged to the client */
	private boolean charged;

	/** tjm : the amount charged per working day to the client */
	private BigDecimal tjm;

	/** bonusPercentage : the percentage of the charge which */
	private Float bonusPercentage;

	/** dateOfValidity : the date at which this nature can be used */
	private LocalDateTime dateOfValidity;

	/**
	 * endOfValidity : the nature can't be used past this date if null, the nature
	 * is currently valid
	 */
	private LocalDateTime endOfValidity;

	/** decription : the name of the nature */
	private String decription;
}
