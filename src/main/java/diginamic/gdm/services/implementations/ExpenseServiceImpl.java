package diginamic.gdm.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.ExpenseRepository;
import diginamic.gdm.services.ExpenseService;
import diginamic.gdm.services.ExpenseTypeService;
import diginamic.gdm.services.MissionService;
import diginamic.gdm.vars.errors.impl.ExpenseErrors;
import diginamic.gdm.vars.errors.impl.MissionErrors;
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
	 * The {@link ExpenseTypeService} dependency;
	 */
	private ExpenseTypeService expenseTypeService;
	/**
	 * The {@link MissionService} dependency;
	 */
	private MissionService missionService;

	@Override
	public List<Expense> list() {
		return this.expenseRepository.findAll();
	}

	@Override
	public List<Expense> getExpensesOfMission(Mission mission) {
		return expenseRepository.findByMission(mission);
	}

	@Override
	public Expense create(Expense expense) throws Exception {
		// why waiting for setting theses ?
		expense.setExpenseType(expenseTypeService.read(expense.getExpenseType().getId()));
		// this is an overkill, the mission should have been already checked and set at
		// this point
		expense.setMission(missionService.read(expense.getMission().getId()));
		// we check if everything is all right
		this.isExpenseValid(expense);
		// and save
		return this.expenseRepository.save(expense);
	}

	@Override
	public Expense read(int id) throws BadRequestException {
		return this.expenseRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(ErrorCodes.expenseNotFound, ExpenseErrors.read.NOT_FOUND));
	}

	@Override
	public Expense update(int id, Expense expense) throws Exception {
		// we get the current values before any modifications
		Expense current = read(expense.getId());
		// this test is also an overkill
		// the missionService would throw an error if the id didn't exist
		if (id != current.getId()) {
			throw new BadRequestException(ErrorCodes.idInconsistent, ExpenseErrors.INCONSISTENT_ID);
		}
		// since this one throws is own exceptions
		// of course we test the data we want to put
		// not the ones allready in place
		this.isExpenseValid(expense);
		// We set data
		// this one first since its the last element that can throw an Exception
		current.setExpenseType(expenseTypeService.read(expense.getExpenseType().getId()));
		current.setDate(expense.getDate());
		current.setTva(expense.getTva());
		current.setCost(expense.getCost());
		// the expenses aren't supposed to change assignated mission, at least for now
		// current.setMission(missionService.read(expense.getMission().getId()));
		this.expenseRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) throws BadRequestException {
		// read throw is own exception if the mission doesn't exist so there is no need
		// to handle that
		this.expenseRepository.delete(this.read(id));
	}

	@Override
	public boolean isExpenseValid(Expense expense) throws Exception {
		// on recherche la mission dans la BDD
		// this is a safety check made in case its was not done beforehand
		Mission mission = missionService.read(expense.getMission().getId());
		// we check if the expense we are trying to update and the data coming from
		// back-end
		// are pointing to the same mission
		// its an overkill and only done here only to have more feedback when testing
		if (expense.getMission().getId() != mission.getId()) {
			throw new BadRequestException(ErrorCodes.idInconsistent, ExpenseErrors.INCONSISTENT_ID);
		}

		if (!missionService.isMissionDone(mission.getId())) {
			throw new BadRequestException(ErrorCodes.missionInvalid, MissionErrors.update.NOT_DONE,
					ExpenseErrors.EXPENSE, ExpenseErrors.CANT_BE, ExpenseErrors.ADDED);
		}
		// is the date valid?
		LocalDateTime date = expense.getDate();
		if (date == null) {
			throw new BadRequestException(ErrorCodes.expenseInvalid, ExpenseErrors.invalid.NULL_DATE);
		}
		// are all data needed present and in correct values
		if (expense.getExpenseType().equals(null)) {
			throw new BadRequestException(ErrorCodes.expenseInvalid,ExpenseErrors.invalid.NULL_TYPE);
		}
		// cost can't be negative
		if (expense.getCost().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new BadRequestException(ErrorCodes.expenseInvalid,ExpenseErrors.VALUE,ExpenseErrors.CANT_BE
					, ExpenseErrors.NEGATIVE);
		}
		// TVA can't be negative
		if (expense.getTva() < 0) {
			throw new BadRequestException(ErrorCodes.expenseInvalid,ExpenseErrors.TAXES,ExpenseErrors.CANT_BE
					, ExpenseErrors.NEGATIVE);
		}
		// expenses's date can't be before mission start
		if (date.isBefore(mission.getStartDate())) {
			throw new BadRequestException(ErrorCodes.expenseInvalid,ExpenseErrors.invalid.IS_BEFORE);
		}
		// expenses's date can't be after mission end
		if (date.isAfter(mission.getEndDate())) {
			throw new BadRequestException( ErrorCodes.expenseInvalid,ExpenseErrors.invalid.IS_AFTER);
		}
		return true;
	}

}
