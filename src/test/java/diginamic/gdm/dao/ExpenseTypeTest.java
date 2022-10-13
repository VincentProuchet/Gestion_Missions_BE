package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import diginamic.gdm.dto.ExpenseTypeDTO;

/**
 * classe de test de DAO-DTO/ExpenseType 
 * on teste aussi la conversion depuis et vers le DTO
 * 
 * @author Vincent
 *
 */
public class ExpenseTypeTest {
	@Test
	public void nameCompliance() {
		ExpenseType expenseType1 = new ExpenseType(0,"   MonTp:,;!,:!eL;lier   ");
		ExpenseType expenseType2 = new ExpenseType(0,"   BouRg-----en        braise 2   ");
		ExpenseType expenseType3 = new ExpenseType(0,"  01    23      45    67             89   ");
		ExpenseType expenseType4 = new ExpenseType(0,"e,?;.:/r!§%*¨^£$¤&~t#'{([-----|`_\\q@)]°+=}");
		
		assertEquals("montpellier",expenseType1.getName());
		assertEquals("bourg-en braise 2",expenseType2.getName());
		assertEquals("01 23 45 67 89",expenseType3.getName());
		assertEquals("ert-q",expenseType4.getName());
	}
	@Test
	public void convertionToDTO() {
		ExpenseType expenseType1 = new ExpenseType(0,"   MonTp:,;!,:!eL;lier   ");
		ExpenseTypeDTO etDTO = new ExpenseTypeDTO(expenseType1);
		assertEquals("montpellier",etDTO.getName());
		assertEquals(0,etDTO.getId());
				
	}
	@Test
	public void convertionFromDTO() {
		ExpenseTypeDTO expenseTypeDTO = new ExpenseTypeDTO(0,"   MonTp:,;!,:!eL;lier   ");
		ExpenseType expenseType = new ExpenseType(expenseTypeDTO);
		assertEquals("montpellier",expenseType.getName());
		assertEquals(0,expenseType.getId());
				
	}
}

