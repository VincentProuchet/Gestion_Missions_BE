package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.ApplicationParams;
import diginamic.gdm.repository.ApplicationParamsRepository;
import diginamic.gdm.services.ApplicationParamService;
import lombok.AllArgsConstructor;

/**
 * @author Vincent
 *
 */
@Service
@AllArgsConstructor
public class ApplicationParamImpl implements ApplicationParamService {

	@Autowired
	ApplicationParamsRepository repository;

	@Override
	public List<ApplicationParams> list() {

		return this.repository.findAll();
	}

	@Override
	public ApplicationParams create(ApplicationParams param) throws Exception {
		if (param.getId() != 0) {
			throw new Exception(" new param id not 0");
		}
		if (this.repository.findByName(param.getName()).isPresent()) {
			throw new Exception(" new param name already exist");
		}
		return this.repository.save(param);
	}

	@Override
	public ApplicationParams read(int id) throws Exception {
		return this.repository.findById(id).orElseThrow(() -> new Exception("no param of id " + id));
	}

	@Override
	public ApplicationParams read(String name) throws Exception {
		return this.repository.findByName(name).orElseThrow(() -> new Exception("no param of name " + name));
	}

	@Override
	public ApplicationParams update(ApplicationParams param) throws Exception {
		ApplicationParams paramP = this.read(param.getId());

		paramP.setText(param.getText());
		paramP.setValue(param.getValue());
		paramP.setValuef(param.getValuef());
		paramP.setValueb(param.isValueb());
		return this.repository.save(paramP);
	}

	@Override
	public ApplicationParams delete(ApplicationParams param) throws Exception {

		return null;
	}

}
