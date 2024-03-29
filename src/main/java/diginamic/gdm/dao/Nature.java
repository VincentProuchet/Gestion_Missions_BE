package diginamic.gdm.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import diginamic.gdm.dto.NatureDTO;
import diginamic.gdm.vars.GDMVars;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents the nature of a mission
 *
 *  id
 *  giveBonus
 *  charged
 *  tjm
 *  bonusPercentage
 *  dateOfValidity
 *  endOfValidity
 *  description
 *
 * @author Joseph
 * @author Vincent
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Nature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;

	/** givesBonus : does a mission with this nature give a bonus? */
	@Column(nullable = false)
	private boolean givesBonus =false;

	/** charged : is this mission charged to the client */
	@Column(nullable = false)
	private boolean charged = false;

	/** tjm : the amount charged per working day to the client */
	@Column(nullable = false, precision = 2)
	private float tjm = 0f ;

	/** bonusPercentage : the percentage of the charge which */
	@Column(nullable = false, precision = 2)
	private Float bonusPercentage = 0f;

	/** dateOfValidity : the date at which this nature can be used */
	@Column(nullable = false)
	private LocalDateTime dateOfValidity;

	/**
	 * endOfValidity : the nature can't be used past this date if null, the nature
	 * is currently valid
	 */
	private LocalDateTime endOfValidity;

	/** description : the name of the nature */
	@Column(nullable = false)
	private String description;


	/**
	 * Setter
	 * made because lombock is kinda not accepting overloading
	 * @param tjm
	 */
	public void setTjm(float tjm) {

		this.tjm = tjm;
	}

	/**
	 * SETTER
	 * les description des natures sont toujours stockées en minuscules
	 * ne sont autorisés que les caractères alphanumérique, les tirets et espaces
	 * les espaces multiples ne sont pas autorisés
	 * de même que les tirets multiples
	 * @param description
	 */
	public void setDescription(String description) {
		description = description.replaceAll(GDMVars.REGEX_NAMES,"");
		description = description.replaceAll(GDMVars.REGEX_STUPID_WHITSPACES," ");
		description = description.replaceAll(GDMVars.REGEX_STUPID_MINUS,"-");
		this.description = description.trim().toLowerCase();
	}

	/** Constructeur
	 * @param nature
	 */
	public Nature(NatureDTO nature) {
		this.id = nature.getId();
		this.setDescription(nature.getDescription());
		this.bonusPercentage = nature.getBonusPercentage();
		this.charged = nature.isCharged();
		this.dateOfValidity = nature.getDateOfValidity();
		this.endOfValidity = nature.getEndOfValidity();
		this.givesBonus = nature.isGivesBonus();
		this.tjm = nature.getTjm();
	}
}
