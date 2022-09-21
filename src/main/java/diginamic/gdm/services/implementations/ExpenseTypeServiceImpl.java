package diginamic.gdm.services.implementations;

import java.util.List;

import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.repository.ExpenseTypeRepository;
import diginamic.gdm.services.ExpenseTypeService;
import lombok.AllArgsConstructor;

import javax.transaction.Transactional;

/**
 * Implementation for {@link ExpenseTypeService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
public class ExpenseTypeServiceImpl implements ExpenseTypeService {

	/**
	 * The {@link ExpenseTypeRepository} dependency.
	 */
	private ExpenseTypeRepository expenseTypeRepository;
	
	@Override
	public List<ExpenseType> list() {
		return this.expenseTypeRepository.findAll();
	}

	@Override
	public ExpenseType create(ExpenseType expenseType) {
		return this.expenseTypeRepository.save(expenseType);
	}
	
	@Override
	public ExpenseType read(int id) throws BadRequestException {
		return this.expenseTypeRepository.findById(id).orElseThrow(()->new BadRequestException("Expense Type not found", ErrorCodes.expenseTypeNotFound));
	}

	@Override
	public ExpenseType update(int id, ExpenseType expenseType) throws BadRequestException {
		ExpenseType current = read(expenseType.getId());
		current.setName(expenseType.getName());
		this.expenseTypeRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) throws BadRequestException {
		ExpenseType expenseType = read(id);
		this.expenseTypeRepository.delete(expenseType);
	}

}
