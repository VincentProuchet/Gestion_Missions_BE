package diginamic.gdm.services.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.exceptions.BadRequestException;

@SpringBootTest
@ActiveProfiles("Test")
public class ExpenseTypeServiceImplTest {
	
	@Autowired
	private ExpenseTypeServiceImpl service;
	private String baseName = "expensetypeservice-test ";// trailing space is here on purpose
	private String baseJammedName = "    expensetypeservice----test   &~#'{([|`_\\^¨°)]+=}$£¤%*,?;.:/!§      ";
	
	
	@Test
	public void list() {
		assertDoesNotThrow(()-> this.service.list());
		List<ExpenseType> ets = this.service.list();
		assertFalse(ets.isEmpty());		
		
	}
	
	@Test
	public void create() throws BadRequestException {
		String name = baseName +"create";
		String jammedName = baseJammedName +"create";
		// due to the control on names (normal and jammed should give the same result) 
		ExpenseType expenseType = new ExpenseType(0, jammedName);
		ExpenseType expenseType2 = new ExpenseType(0, name);
		// we create it
		assertDoesNotThrow(() -> this.service.create(expenseType));
		// create it again should throw 
		assertThrows(BadRequestException.class, () -> this.service.create(expenseType2));
				 
	}
	
	@Test
	public void read() throws BadRequestException {
		String name = baseName +"read";
		String jammedName = baseJammedName +"read";
		ExpenseType expenseType = this.service.create(new ExpenseType(0, jammedName));
		assertThrows(BadRequestException.class,()->this.service.read(Integer.MAX_VALUE));
		assertDoesNotThrow(()->this.service.read(expenseType.getId()));		
	}
	@Test
	public void update() throws BadRequestException {
		String name = baseName +"update";
		String jammedName = baseJammedName +"update";
		// due to the control on names (normal and jammed should give the same result) 
		ExpenseType expenseType = new ExpenseType(0, jammedName);
		ExpenseType expenseType2 = new ExpenseType(0, name+2);
		// we create the first one
		final ExpenseType expenseTypeCreated =  this.service.create(expenseType);
		// the seconde one
		final ExpenseType expenseTypeC2 =  this.service.create(expenseType2);
		expenseTypeC2.setName(jammedName);
		// we update the second with the name of the first and it should throw 
		assertThrows(BadRequestException.class,()->this.service.update(expenseType2.getId(), expenseType2));
		expenseTypeC2.setName(jammedName+3);
		assertDoesNotThrow(()->this.service.update(expenseType2.getId(), expenseType2));
		
	}
	/**
	 * 
	 * le delete existe mais il n'est pas prévu dans l'intreface et encore moins dans les contrôleurs
	 * @throws BadRequestException
	 */
	@Test
	public void delete() throws BadRequestException {
		String name = baseName +"delete";
		String jammedName = baseJammedName +"delete";
		ExpenseType expenseType = this.service.create(new ExpenseType(0, jammedName));
		assertDoesNotThrow(()->this.service.delete(expenseType.getId()));
		assertThrows(BadRequestException.class,()->this.service.read(expenseType.getId()));
	
	}

}
