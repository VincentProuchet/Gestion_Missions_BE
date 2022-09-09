package diginamic.gdm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class NatureDTO {
	
	
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
	private BigDecimal tjm;	
	/** % prime taux de prime à sur la montant total de la mission  */
	private float bonusPercentage;
	/**  début de validité de la nature  */
	private LocalDateTime dateOfValidity = null;
	/** endValidity  fin de validité de la nature
	 * une nature en cours de validité à cette valeur nulle*/
	private LocalDateTime endOfValidity = null;
	
	
	
	
}
