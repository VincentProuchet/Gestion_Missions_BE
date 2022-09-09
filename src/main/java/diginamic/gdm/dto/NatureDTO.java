package diginamic.gdm.dto;

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
	private Integer id=0; 
	/** Est-ce que la mission 
	 * fait l'objet de distribution de primes */
	private boolean hasPrime= false;
	/** Est-ce que la mission 
	 * fait l'objet de facturation */
	private boolean hisFacture = false;
	/** Taux journalier moyen pour la facturation à la journée de mission */
	private float tjm = 0;
	
	/** % prime taux de prime à sur la montant total de la mission  */
	private float prime = 0;
	/** StartValidity  début de validité de la nature  */
	private LocalDateTime StartValidity = null;
	/** endValidity  fin de validité de la nature
	 * une nature en cours de validité à cette valeur nulle*/
	private LocalDateTime endValidity = null;
	/** libelle  le code de la nature si deux nature ont le même libellé 
	 * une seule auras une endValidity nulle */
	private String libelle = "";
	
	
	
	
}
