package diginamic.gdm.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import diginamic.gdm.dao.Collaborator;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.repository.MissionRepository;
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
@Transactional
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
	public List<Expense> getExpensesOfMission(Mission mission) {
		return expenseRepository.findByMission(mission);
	}

	@Override
	public Expense create(Expense expense) throws BadRequestException {

		if (!isExpenseValid(expense)){
			throw new BadRequestException("Expense invalid : make sure the reqired data is present, the date is ok, and that the mission is already registered in DB", ErrorCodes.expenseInvalid);
		}

		return this.expenseRepository.save(expense);
	}
	
	@Override
	public Expense read(int id) throws BadRequestException {
		return this.expenseRepository.findById(id).orElseThrow(()->new BadRequestException("Expense not found", ErrorCodes.expenseNotFound));
	}

	@Override
	public Expense update(int id, Expense expense) throws BadRequestException {
		Expense current = read(expense.getId());

		if (id != expense.getId()){
			throw new BadRequestException("Expense id inconsistent", ErrorCodes.idInconsistent);
		}

		if (!isExpenseValid(expense)) {
			throw new BadRequestException("Expense invalid : ", ErrorCodes.expenseInvalid);
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
	public void delete(int id) throws BadRequestException {
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
		Optional<Mission> actualMissionOptional = missionRepository.findById(mission.getId());
		if (actualMissionOptional.isEmpty()){
			return false;
		}
		Mission actualMission = actualMissionOptional.get();

		boolean existsAndIsValid = missionService.isMissionDone(actualMission.getId());

		// is the date valid?
		LocalDateTime date = expense.getDate();
		if (date == null) {
			return false;
		}

		boolean isDateInMissionPeriod = date.isAfter(actualMission.getStartDate()) && date.isBefore(actualMission.getEndDate());

		// required data
		boolean isRequiredDataPresent = expense.getExpenseType() != null && expense.getCost().compareTo(BigDecimal.valueOf(0)) >= 0 && expense.getTva() >= 0;

		return existsAndIsValid && isDateInMissionPeriod && isRequiredDataPresent;
	}

}
