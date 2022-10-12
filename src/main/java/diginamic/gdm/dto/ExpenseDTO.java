package diginamic.gdm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import diginamic.gdm.dao.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for the {@link diginamic.gdm.dao.Expense Expense} DAO
 * 
 * @author DorianBoel
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseDTO implements DTO<Expense> {

	/**
	 * Database id
	 */
	private int id = 0;
	
	/**
	 * Database foreign key id of the associated mission
	 */
	private int idMission = 0;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense Expense.date}
	 */
	private LocalDateTime date = null;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense Expense.cost}
	 */
	private BigDecimal cost = BigDecimal.ZERO;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense Expense.tva}
	 */
	private float tva = 0;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense Expense.expenseType}
	 */
	private ExpenseTypeDTO type = null;
	
	public ExpenseDTO(Expense expense) {
		this.id = expense.getId();
		this.idMission = expense.getMission().getId();
		this.date = expense.getDate();
		this.cost = expense.getCost();
		this.tva = expense.getTva();
		this.type = new ExpenseTypeDTO(expense.getExpenseType());
	}
	
}
