package diginamic.gdm.services.implementations;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.ExpenseType;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.ExpenseTypeRepository;
import diginamic.gdm.services.ExpenseTypeService;
import diginamic.gdm.vars.errors.ErrorsMessage;
import lombok.AllArgsConstructor;

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
	public ExpenseType create(ExpenseType expenseType) throws BadRequestException {
		if(!this.expenseTypeRepository.findByName(expenseType.getName()).isEmpty()){
			throw new BadRequestException(ErrorCodes.expenseTypeNotFound,ErrorsMessage.EXPENSE_TYPE,ErrorsMessage.create.NAME_ALLREADY_EXIST,expenseType.getName());
		}
		return this.expenseTypeRepository.save(expenseType) ;

	}

	@Override
	public ExpenseType read(int id) throws BadRequestException {
		return this.expenseTypeRepository.findById(id).orElseThrow(()->new BadRequestException(ErrorCodes.expenseTypeNotFound,ErrorsMessage.EXPENSE_TYPE,ErrorsMessage.read.NOT_FOUND));
	}

	@Override
	public ExpenseType update(int id, ExpenseType expenseType) throws BadRequestException {
		ExpenseType current = read(expenseType.getId());
		// we check if the new name does not allready exist
		if(!this.expenseTypeRepository.findByName(expenseType.getName()).isEmpty()){
			throw new BadRequestException(ErrorCodes.expenseTypeNotFound,ErrorsMessage.EXPENSE_TYPE,ErrorsMessage.create.NAME_ALLREADY_EXIST,expenseType.getName());
		}
		current.setName(expenseType.getName());
		return this.expenseTypeRepository.save(current);

	}

	/**
	 *@Todo implement controls forbiding suppression of ExpenseType referenced by one expense
	 */
	@Override
	public void delete(int id) throws BadRequestException {
		this.expenseTypeRepository.delete(this.read(id));
	}

}
