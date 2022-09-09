package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Nature;
import diginamic.gdm.repository.NatureRepository;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link NatureService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
public class NatureServiceImpl implements NatureService {

	/**
	 * The {@link NatureRepository} dependency.
	 */
	private NatureRepository natureRepository;
	
	@Override
	public List<Nature> list() {
		return this.natureRepository.findAll();
	}

	@Override
	public void create(Nature nature) {
		this.natureRepository.save(nature);
	}

	@Override
	public Nature update(Nature nature) {
		Nature current = this.natureRepository.findById(nature.getId()).orElseThrow();
		current.setGivesBonus(nature.isGivesBonus());
		current.setCharged(nature.isCharged());
		current.setTjm(nature.getTjm());
		current.setBonusPercentage(nature.getBonusPercentage());
		current.setDateOfValidity(nature.getDateOfValidity());
		current.setEndOfValidity(nature.getEndOfValidity());
		current.setDescription(nature.getDescription());
		return current;
	}

	@Override
	public void delete(int id) {
		Nature nature = this.natureRepository.findById(id).orElseThrow();
		this.natureRepository.delete(nature);
	}

}
