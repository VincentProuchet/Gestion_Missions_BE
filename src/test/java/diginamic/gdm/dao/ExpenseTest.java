package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.plugin.codec.LocalDateTimeCodec;

import diginamic.gdm.dto.ExpenseDTO;
import diginamic.gdm.dto.ExpenseTypeDTO;
import diginamic.gdm.dto.MissionDTO;

public class ExpenseTest {
	
	@Test
	public void baseValues() {
		Mission mission = new Mission();
		ExpenseType expenseType1 = new ExpenseType(0,"   MonTp:,;!,:!eL;lier   ");
		Expense expense = new Expense();
		
		assertEquals(0, expense.getId());
		assertEquals(0f, expense.getTva());
		assertEquals(BigDecimal.ZERO, expense.getCost());
		
		assertInstanceOf(LocalDateTime.class,expense.getDate());
		assertNotEquals(LocalDateTime.now(),expense.getDate() );
		
		assertNull(expense.getExpenseType());
		
		expense.setExpenseType(expenseType1);
		assertEquals(expenseType1, expense.getExpenseType());
		assertEquals("montpellier", expense.getExpenseType().getName());
		
		assertNull(expense.getMission());		
		expense.setMission(mission);
		assertInstanceOf(Mission.class, expense.getMission());
		assertEquals(mission, expense.getMission());
		
	}
	@Test
	public void convertToDTO() {
		Mission mission = new Mission();
		ExpenseType expenseType1 = new ExpenseType(0,"   MonTp:,;!,:!eL;lier   ");
		
		Expense expense1 = new Expense();		
		expense1.setExpenseType(expenseType1);
		assertEquals("montpellier", expense1.getExpenseType().getName());
		expense1.setMission(mission);
		// we convert Here
		ExpenseDTO expense = new ExpenseDTO(expense1);
		
		assertEquals(0, expense.getId());
		assertEquals(0f, expense.getTva());
		assertEquals(BigDecimal.ZERO, expense.getCost());
		
		assertInstanceOf(LocalDateTime.class,expense.getDate());
		assertNotEquals(LocalDateTime.now(),expense.getDate() );
		
		assertEquals("montpellier", expense.getType().getName());		
		
		assertInstanceOf(Integer.class, expense.getIdMission());
		assertEquals(mission.getId(), expense.getIdMission());		
	}
	@Test
	public void convertFromDTO() {
		ExpenseTypeDTO expenseType1 = new ExpenseTypeDTO(0,"   MonTp:,;!,:!eL;lier   ");
		ExpenseDTO expense1 = new ExpenseDTO();		
		expense1.setType(expenseType1);
		LocalDateTime now = LocalDateTime.now();
		expense1.setDate(now);
		assertEquals("   MonTp:,;!,:!eL;lier   ", expense1.getType().getName());
		expense1.setIdMission(256);
		// we convert here
		Expense expense = new Expense(expense1);
		
		assertEquals(0, expense.getId());
		assertEquals(0f, expense.getTva());
		assertEquals(BigDecimal.ZERO, expense.getCost());
		
		assertInstanceOf(LocalDateTime.class,expense.getDate());
		//assertNotEquals(LocalDateTime.now(),expense.getDate() );
		
		assertEquals("montpellier", expense.getExpenseType().getName());		
		
		assertInstanceOf(Mission.class, expense.getMission());
		assertEquals(256, expense.getMission().getId());		
	}


}
