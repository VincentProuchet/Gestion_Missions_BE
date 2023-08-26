package diginamic.gdm.services.implementations;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import diginamic.gdm.dao.City;
import diginamic.gdm.dto.CityDTO;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.CityRepository;
import diginamic.gdm.services.CityService;
import diginamic.gdm.vars.errors.impl.CityErrors;
import lombok.AllArgsConstructor;

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
	public City create(City city) throws BadRequestException {
		if(!City.isValidName(city.getName()) || !this.cityRepository.findByName(city.getName()).isEmpty()){
			throw new BadRequestException(ErrorCodes.cityNotFound,CityErrors.read.INVALID_NAME);

		}
		return this.cityRepository.findByName(city.getName()).orElseGet(()->this.cityRepository.save(city));
	}

	@Override
	public City read(int id) throws BadRequestException {
		return this.cityRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(ErrorCodes.cityNotFound,CityErrors.read.NOT_FOUND));
	}

	@Override
	public City update(int id, City city) throws BadRequestException {
		if(!City.isValidName(city.getName())) {
			throw new BadRequestException(ErrorCodes.cityNotFound,CityErrors.read.INVALID_NAME);
		}
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

	@Override
	public City read(String name) throws BadRequestException {
		// we first search it
		return this.cityRepository.findByName(name)
				.orElseThrow(() -> new BadRequestException(ErrorCodes.cityNotFound,CityErrors.read.NOT_FOUND));

	}

	@Override
	public City read(CityDTO city) throws BadRequestException {
		// attempt to find it by id
		try {
			return this.read(city.getId());
		} catch (BadRequestException e) {
			// attemp to find it by name

			return this.read(city.getName());
		}

	}
}
