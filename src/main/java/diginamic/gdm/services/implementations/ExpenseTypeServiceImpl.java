package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.repository.ExpenseTypeRepository;
import diginamic.gdm.services.ExpenseTypeService;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link ExpenseTypeService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
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
	public void create(ExpenseType expenseType) {
		this.expenseTypeRepository.save(expenseType);
	}
	
	@Override
	public ExpenseType read(int id) {
		return this.expenseTypeRepository.findById(id).orElseThrow();
	}

	@Override
	public ExpenseType update(int id, ExpenseType expenseType) {
		ExpenseType current = read(expenseType.getId());
		current.setName(expenseType.getName());
		this.expenseTypeRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) {
		ExpenseType expenseType = read(id);
		this.expenseTypeRepository.delete(expenseType);
	}

}