package diginamic.gdm.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import diginamic.gdm.repository.MissionRepository;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.services.ExpenseService;
import diginamic.gdm.services.MissionService;
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
	
	/**
	 * The {@link MissionService} dependency;
	 */
	private MissionService missionService;

	private MissionRepository missionRepository;

	@Override
	public List<Expense> list() {
		return this.expenseRepository.findAll();
	}

	@Override
	public Expense create(Expense expense) {

		if (!isExpenseValid(expense)){
			return null;
		}

		Expense actualExpense = this.expenseRepository.save(expense);
		Mission mission = missionRepository.findById(actualExpense.getMission().getId()).get();
		Set<Expense> expenses = mission.getExpenses();
		expenses.add(expense); // does mission need to be updated?
		return actualExpense;
	}
	
	@Override
	public Expense read(int id) {
		return this.expenseRepository.findById(id).orElseThrow();
	}

	@Override
	public Expense update(int id, Expense expense) {
		Expense current = read(expense.getId());

		if (current == null || id != expense.getId()){
			return null;
		}

		if (!isExpenseValid(expense)) {
			return null;
		}

		if ( expense.getMission().getId() != current.getMission().getId()
				|| current.getMission().getExpenses().stream().allMatch(expense1 -> expense1.getId() != id)) {
			return null;
		}


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
		Mission mission = expense.getMission();
		mission.setExpenses(mission.getExpenses().stream().filter(expense1 -> expense1.getId() != id).collect(Collectors.toSet()));

		this.expenseRepository.delete(expense);
	}

	@Override
	public boolean isExpenseValid(Expense expense) {
		Mission mission = expense.getMission();

		// does this mission exist?
		if (mission == null) {
			return false;
		}
		Mission actualMission = missionService.read(mission.getId());

		boolean existsAndIsValid = actualMission != null && missionService.isMissionDone(actualMission.getId());

		// is the date valid?
		LocalDateTime date = expense.getDate();
		if (date == null) {
			return false;
		}

		boolean isDateInMissionPeriod = date != null && date.isAfter(actualMission.getStartDate()) && date.isBefore(actualMission.getEndDate());

		// required data
		boolean isRequiredDataPresent = expense.getExpenseType() != null && expense.getCost().compareTo(new BigDecimal(0)) >= 0 && expense.getTva() >= 0;

		return existsAndIsValid && isDateInMissionPeriod && isRequiredDataPresent;
	}

}
