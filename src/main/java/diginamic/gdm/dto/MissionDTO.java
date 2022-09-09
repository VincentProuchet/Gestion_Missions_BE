package diginamic.gdm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Status;
import diginamic.gdm.dao.Transport;
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
public class MissionDTO {
	
	/** id */
	private int id;
	/** start */
	private LocalDateTime start = null;
	/** end */
	private LocalDateTime end = null;
	/** startCity */
	private City startCity;
    /** arrivalCity */
    private City arrivalCity;
    /** bonus */
    private BigDecimal bonus;
    /** transport */
    private Transport transport;
    /** status */
    private Status status; 
    /** nature */
    private NatureDTO nature;
    /** collaborator */
    private CollaboratorDTO collaborator;
    /** expenses */
    private List<ExpenseDTO> expenses;

}
