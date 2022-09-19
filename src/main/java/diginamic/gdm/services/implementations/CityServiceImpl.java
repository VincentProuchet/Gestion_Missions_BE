package diginamic.gdm.services.implementations;

import java.util.List;

import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.City;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.services.CityService;
import lombok.AllArgsConstructor;

import javax.transaction.Transactional;

/**
 * Implementation for {@link CityService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
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
	public City read(int id) throws BadRequestException {
		return this.cityRepository.findById(id).orElseThrow(() -> new BadRequestException("City not found", ErrorCodes.cityNotFound));
	}

	@Override
	public City update(int id, City city) throws BadRequestException {
		City current = read(id);
		current.setName(city.getName());
		this.cityRepository.save(current);
		return current;
	}

	@Override
	public void delete(int id) throws BadRequestException {
		City city = read(id);
		this.cityRepository.delete(city);
	}

}
