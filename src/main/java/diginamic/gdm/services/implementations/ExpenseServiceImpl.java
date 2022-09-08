package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.services.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private ExpenseRepository expenseRepository;
	
	public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}

	@Override
	public List<Expense> list() {
		return this.expenseRepository.findAll();
	}

	@Override
	public void create(Expense expense) {
		this.expenseRepository.save(expense);

	}

	@Override
	public Expense update(Expense expense) {
		Expense current = this.expenseRepository.findById(expense.getId()).orElseThrow();
		current.setDate(expense.getDate());
		current.setTva(expense.getTva());
		current.setCost(expense.getCost());
		current.setExpenseType(expense.getExpenseType());
		return current;
	}

	@Override
	public void delete(int id) {
		Expense expense = this.expenseRepository.findById(id).orElseThrow();
		this.expenseRepository.delete(expense);
	}

}
