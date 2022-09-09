package diginamic.gdm.dto;

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
public class ExpenseTypeDTO {

	/**
	 * Expense type name identifier.
	 */
	private String name = "";
	
}
