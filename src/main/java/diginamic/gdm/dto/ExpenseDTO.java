package diginamic.gdm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.ExpenseType;
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
public class ExpenseDTO extends TransactionDTO implements DTO<Expense> {

	/**
	 * Database id
	 */
	private int id = 0;
	
	/**
	 * Database foreign key id of the associated mission
	 */
	private int idMission = 0;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense#date Expense.date}
	 */
	private LocalDateTime date = null;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense#cost Expense.cost}
	 */
	private BigDecimal cost = null;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense#tva Expense.tva}
	 */
	private float tva = 0;
	
	/**
	 * Represents {@link diginamic.gdm.dao.Expense#expenseType Expense.expenseType}
	 */
	private ExpenseType type = null;
	
	public ExpenseDTO(Expense expense) {
		this.id = expense.getId();
		this.idMission = expense.getMission().getId();
		this.date = expense.getDate();
		this.cost = expense.getCost();
		this.tva = expense.getTva();
		this.type = expense.getExpenseType();
	}

	@Override
	public Expense instantiate() {
		return new Expense(id, date, cost, tva, null, type);
	}
	
}
