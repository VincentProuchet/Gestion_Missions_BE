package diginamic.gdm.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import diginamic.gdm.dao.City;
import diginamic.gdm.services.CityService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link City} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = "city", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CityController {
	
	/**
	 * The {@link CityService} dependency.
	 */
	private CityService cityService;

	/**
	 * Gets the full list of registered cities.
	 * 
	 * @return A list of all cities
	 */
	@GetMapping
	public List<City> list() {
		return cityService.list();
	}
	
	/**
	 * Saves a new {@link City} instance.
	 * 
	 * @param city The new city within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody City city) {
		cityService.create(city);
	}
	
	/**
	 * Updates the data for a specific registered city.
	 * 
	 * @param city The city within the request body with modified info
	 * @return The resulting city with updated info
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public City update(@RequestBody City city) {
		return cityService.update(city);
	}
	
	/**
	 * Deletes a specific registered city.
	 * 
	 * @param id The id of the city to delete
	 */
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) {
		cityService.delete(id);
	}
	
}