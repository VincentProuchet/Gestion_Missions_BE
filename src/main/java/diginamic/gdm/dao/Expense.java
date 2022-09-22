package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	private int id;

	/** date : the date of the expense */
	private LocalDateTime date;

	/** cost : the cost of the expense */
	private BigDecimal cost;

	/** tva : the applicable TVA */
	private Float tva;

	/** mission : the mission which required this expense */
	@ManyToOne
	@JoinColumn(name = "missionID")
	private Mission mission;

	/** expenseType : the nature of this expense */
	@ManyToOne
	@JoinColumn(name = "expenseTypeID",nullable = false)
	private ExpenseType expenseType;
}
