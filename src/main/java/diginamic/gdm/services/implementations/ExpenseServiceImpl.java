package diginamic.gdm.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.services.ExpenseTypeService;
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
	private ExpenseTypeService expenseTypeService;
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
		// why waiting for setting thoses ?
		expense.setExpenseType(expenseTypeService.read(expense.getExpenseType().getId()));
		// this is an overkill, the mission should have been already checked and set at
		// this point
		expense.setMission(missionService.read(expense.getMission().getId()));
		// we check if everything is allright
		this.isExpenseValid(expense);
		// and save
		return this.expenseRepository.save(expense);
	}

	@Override
	public Expense read(int id) throws BadRequestException {
		return this.expenseRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Expense not found", ErrorCodes.expenseNotFound));
	}

	@Override
	public Expense update(int id, Expense expense) throws BadRequestException {
		Expense current = read(expense.getId());

		if (id != expense.getId()) {
			throw new BadRequestException("Expense id inconsistent", ErrorCodes.idInconsistent);
		}
		// since this one throws is own exceptions
		this.isExpenseValid(expense);

		if (expense.getMission().getId() != current.getMission().getId()) {
			return null;
		}

		current.setDate(expense.getDate());
		current.setTva(expense.getTva());
		current.setCost(expense.getCost());
		current.setExpenseType(expenseTypeService.read(expense.getExpenseType().getId()));
		current.setMission(missionService.read(expense.getMission().getId()));
		this.expenseRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) throws BadRequestException {
		Expense expense = read(id);

		this.expenseRepository.delete(expense);
	}

	@Override
	public boolean isExpenseValid(Expense expense) throws BadRequestException {
		// on recherche la mission dans la BDD
		Mission mission = missionRepository.findById(expense.getMission().getId())
				.orElseThrow(() -> new BadRequestException("the mission doesn't exist", ErrorCodes.missionInvalid));

		if (!missionService.isMissionDone(mission.getId())) {
			throw new BadRequestException("Mission is done", ErrorCodes.missionInvalid);
		}

		// is the date valid?
		LocalDateTime date = expense.getDate();
		if (date == null) {
			throw new BadRequestException("date is null ", ErrorCodes.missionInvalid);
		}

		// are all data needed present and in correct values
		if (expense.getExpenseType().equals(null) ) {
			throw new BadRequestException("Expense's types can't be null", ErrorCodes.expenseInvalid);
		}

		if (expense.getCost().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new BadRequestException("Expense's value can't be negative", ErrorCodes.expenseInvalid);

		}
		if (expense.getTva() < 0) {
			throw new BadRequestException("Expense's TVA can't be negative", ErrorCodes.expenseInvalid);
		}
		if (!date.isAfter(mission.getStartDate())) {
			throw new BadRequestException("Expense's dates can't be before mission start", ErrorCodes.expenseInvalid);
		}

		if (!date.isBefore(mission.getEndDate())) {
			throw new BadRequestException("Expense's dates can't be after mission end", ErrorCodes.expenseInvalid);
		}

		return true;
	}

}
