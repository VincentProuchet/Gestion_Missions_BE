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
		ExpenseType expenseType4 = new ExpenseType(0,"e,?;.:/r!§%*µ¨^£$¤&~t#'{([-----|`_\\q@)]°+=}");
		
		assertTrue(expenseType1.getName().equals("montpellier"));
		assertTrue(expenseType2.getName().equals("bourg-en braise 2"));
		assertTrue(expenseType3.getName().equals("01 23 45 67 89"));
		assertTrue(expenseType4.getName().equals("ert-q"));
	}
	@Test
	public void convertionToDTO() {
		ExpenseType expenseType1 = new ExpenseType(0,"   MonTp:,;!,:!eL;lier   ");
		ExpenseTypeDTO etDTO = new ExpenseTypeDTO(expenseType1);
		assertTrue(etDTO.getName().equals("montpellier"));
		assertEquals(0,etDTO.getId());
				
	}
	@Test
	public void convertionFromDTO() {
		ExpenseTypeDTO expenseTypeDTO = new ExpenseTypeDTO(0,"   MonTp:,;!,:!eL;lier   ");
		ExpenseType expenseType = new ExpenseType(expenseTypeDTO);
		assertTrue(expenseType.getName().equals("montpellier"));
		assertEquals(0,expenseType.getId());
				
	}
}

