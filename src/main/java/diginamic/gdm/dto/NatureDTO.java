package diginamic.gdm.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import diginamic.gdm.dao.Nature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * DataTransferObject 
 * @author Vincent
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NatureDTO implements DTO<Nature> {
	
	/** id */
	private int id=0; 
	/** libelle  le code de la nature si deux nature ont le même libellé 
	 * une seule auras une endValidity nulle */
	private String description = "";
	/** Est-ce que la mission 
	 * fait l'objet de distribution de primes */
	private boolean givesBonus= false;
	/** Est-ce que la mission 
	 * fait l'objet de facturation */
	private boolean charged = false;
	/** Taux journalier moyen pour la facturation à la journée de mission */
	private float tjm;	
	/** % prime taux de prime à sur la montant total de la mission  */
	private float bonusPercentage;
	/**  début de validité de la nature  */
	private LocalDateTime dateOfValidity = null;
	/** endValidity  fin de validité de la nature
	 * une nature en cours de validité à cette valeur nulle*/
	private LocalDateTime endOfValidity = null;
	
	public NatureDTO(Nature nature) {
		this.id = nature.getId();
		this.description = nature.getDescription();
		this.givesBonus = nature.isGivesBonus();
		this.charged = nature.isCharged();
		this.tjm = nature.getTjm();
		this.bonusPercentage = nature.getBonusPercentage();
		this.dateOfValidity = nature.getDateOfValidity();
		this.endOfValidity = nature.getEndOfValidity();
	}
	
	public Nature instantiate() {
		return new Nature(id, givesBonus, charged, tjm, bonusPercentage, dateOfValidity, endOfValidity, description);
	}
	

}
