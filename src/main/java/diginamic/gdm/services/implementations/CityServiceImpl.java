package diginamic.gdm.services.implementations;

import java.util.List;

import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.City;
import diginamic.gdm.dto.CityDTO;
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
	public City create(City city) {
		return this.cityRepository.save(city);
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

	@Override
	public City read(String name) throws BadRequestException {
		// we first search it
		return this.cityRepository.findByName(name).orElseThrow(() -> new BadRequestException("City not found", ErrorCodes.cityNotFound));
		
	}

	@Override
	public City read(CityDTO city) throws BadRequestException {
		City city2;
		// attempt to find it by id
		try {
			System.err.println("search by id");
			 city2  = this.read(city.getId());
			
		} catch (BadRequestException e) {
			// attemp to find it by name
			try {
				System.err.println("id not found  search by name");
				city2 = read(city.getName());
				
			} catch (BadRequestException e2) {
				// creation of a new city
			System.err.println("create a new city");
				city2 = this.create(new City(city));
			}
			
		}		
		return city2;
	}

}
