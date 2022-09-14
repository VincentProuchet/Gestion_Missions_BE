package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.services.ExpenseService;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link ExpenseService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
	
	/**
	 * The {@link ExpenseRepository} dependency.
	 */
	private ExpenseRepository expenseRepository;

	@Override
	public List<Expense> list() {
		return this.expenseRepository.findAll();
	}

	@Override
	public void create(Expense expense) {
		this.expenseRepository.save(expense);
	}
	
	@Override
	public Expense read(int id) {
		return this.expenseRepository.findById(id).orElseThrow();
	}

	@Override
	public Expense update(int id, Expense expense) {
		Expense current = read(expense.getId());
		current.setDate(expense.getDate());
		current.setTva(expense.getTva());
		current.setCost(expense.getCost());
		current.setExpenseType(expense.getExpenseType());
		this.expenseRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) {
		Expense expense = read(id);
		this.expenseRepository.delete(expense);
	}

}