package diginamic.gdm.dto;

import diginamic.gdm.dao.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for the {@link diginamic.gdm.dao.ExpenseType ExpenseType} DAO
 * 
 * @author DorianBoel
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseTypeDTO implements DTO<ExpenseType>{

	/**
	 * Database id
	 */
	private int id = 0;
	
	/**
	 * Expense type name identifier.
	 */
	private String name = "";
	
	public ExpenseTypeDTO(ExpenseType expenseType) {
		this.id = expenseType.getId();
		this.name = expenseType.getName();
	}
	
	public ExpenseType instantiate() {
		return new ExpenseType(id, name); 
	}
	
}
