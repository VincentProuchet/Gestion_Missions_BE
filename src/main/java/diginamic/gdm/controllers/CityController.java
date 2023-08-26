package diginamic.gdm.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
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
import diginamic.gdm.dto.CityDTO;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.services.CityService;
import diginamic.gdm.vars.GDMRoles;
import diginamic.gdm.vars.GDMRoutes;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link City} related paths.
 *
 * @author DorianBoel
 */
@RestController
// http://wwww.gdm/city/
@RequestMapping(path = GDMRoutes.CITY, produces = MediaType.APPLICATION_JSON_VALUE)
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
	@Secured({GDMRoles.COLLABORATOR})
	public List<CityDTO> list() {
		return cityService.list().stream().map(city -> new CityDTO(city)).toList();
	}

	/**
	 * Saves a new {@link City} instance.
	 *
	 * @param cityDTO The new city within the request body to be registered
	 * @throws Exception
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured({GDMRoles.ADMIN})
	public void create(@RequestBody CityDTO cityDTO) throws Exception {
		cityService.create( new City(cityDTO));
	}

	/**
	 * Gets a specific registered city.
	 *
	 * @param id The id corresponding to the city to get
	 * @return The registered city corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	@Secured({GDMRoles.COLLABORATOR})
	public CityDTO read(@PathVariable int id) throws BadRequestException {
		return new CityDTO(cityService.read(id));
	}

	/**
	 * Updates the data for a specific registered city.
	 *
	 * @param id The id corresponding to the city to update
	 * @param cityDTO The city within the request body with modified info
	 * @return The resulting city with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Secured({GDMRoles.ADMIN})
	public CityDTO update(@PathVariable int id, @RequestBody CityDTO cityDTO) throws BadRequestException {
		return new CityDTO(cityService.update(id,new City(cityDTO)));
	}

	/**
	 * Deletes a specific registered city.
	 *
	 * @param id The id of the city to delete
	 */
	@DeleteMapping(path = "{id}")
	@Secured({GDMRoles.ADMIN})
	public void delete(@PathVariable int id) throws BadRequestException {
		cityService.delete(id);
	}

}