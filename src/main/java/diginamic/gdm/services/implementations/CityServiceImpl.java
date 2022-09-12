package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.City;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.services.CityService;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link CityService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

	/**
	 * The {@link CityRepository} dependency.
	 */
	private CityRepository cityRepository;
	
	@Override
	public List<City> list() {
		return this.cityRepository.findAll();
	}

	@Override
	public void create(City city) {
		this.cityRepository.save(city);
	}
	
	@Override
	public City read(int id) {
		return this.cityRepository.findById(id).orElseThrow();
	}

	@Override
	public City update(City city) {
		City current = read(city.getId());
		current.setName(city.getName());
		this.cityRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) {
		City city = read(id);
		this.cityRepository.delete(city);
	}

}
