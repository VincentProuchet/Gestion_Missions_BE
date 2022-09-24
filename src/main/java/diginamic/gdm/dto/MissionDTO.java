package diginamic.gdm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import diginamic.gdm.dao.City;
import diginamic.gdm.dao.Mission;
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
public class MissionDTO implements DTO<Mission> {
	
	/** id */
	private int id;
	/** start */
	private LocalDateTime start = null;
	/** end */
	private LocalDateTime end = null;
	/** startCity */
	private CityDTO startCity;
    /** arrivalCity */
    private CityDTO arrivalCity;
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
    
    /** Constructeur
     * @param mission
     */
    public MissionDTO(Mission mission) {
    	this.id = mission.getId();
    	this.start = mission.getStartDate();
    	this.end = mission.getEndDate();
    	this.startCity = new CityDTO(mission.getStartCity());
//    	this.startCity = mission.getStartCity().getName();
    	this.arrivalCity = new CityDTO(mission.getEndCity());
 //   	this.arrivalCity = mission.getEndCity().getName();
    	this.bonus = mission.getBonus();
    	this.transport = mission.getMissionTransport();
    	this.status = mission.getStatus();
    	this.nature = new NatureDTO(mission.getNature());
    	this.collaborator = new CollaboratorDTO(mission.getCollaborator());
    	this.expenses = mission.getExpenses().stream().map(expense -> new ExpenseDTO(expense)).toList();
    }
/**
 * I had to rewritte it bu please don't use it it will only give you an empty Mission
 */
	@Override
	public Mission instantiate() {
		
		return new Mission();
	}
    
 

}
