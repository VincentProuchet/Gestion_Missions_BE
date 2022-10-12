package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import diginamic.gdm.dto.ExpenseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents a Collaborator
 * 
 * @author Joseph
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0 ;

	/** date : the date of the expense */
	private LocalDateTime date = LocalDateTime.now();

	/** cost : the cost of the expense */
	private BigDecimal cost = BigDecimal.ZERO;

	/** tva : the applicable TVA */
	private Float tva = 0f ;

	/** mission : the mission which required this expense */
	@ManyToOne
	@JoinColumn(name = "missionID")
	private Mission mission;

	/** expenseType : the nature of this expense */
	@ManyToOne
	@JoinColumn(name = "expenseTypeID",nullable = false)
	private ExpenseType expenseType;
	
	
	public Expense(ExpenseDTO e) {
		this.id = e.getId();
		this.date = e.getDate();
		if(e.getCost()!=null) {
			this.cost = e.getCost();
		}
		this.mission = new Mission();
		this.mission.setId(e.getIdMission());
		this.tva = e.getTva();
		this.expenseType =new ExpenseType(e.getType()) ;
		
	}
}
