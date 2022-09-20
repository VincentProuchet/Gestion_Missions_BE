package diginamic.gdm.controllers;

import java.util.List;

import diginamic.gdm.exceptions.BadRequestException;
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

import diginamic.gdm.GDMRoles;
import diginamic.gdm.GDMRoutes;
import diginamic.gdm.dao.Nature;
import diginamic.gdm.dto.NatureDTO;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;

/**
 * REST API controller for {@link Nature} related paths.
 * 
 * @author DorianBoel
 */
@RestController
@RequestMapping(path = GDMRoutes.NATURE, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class NatureController {
	
	/**
	 * The {@link NatureService} dependency.
	 */
	private NatureService natureService;

	/**
	 * Gets the full list of registered mission natures.
	 * 
	 * @return A list of all natures
	 */
	@GetMapping
	@Secured(GDMRoles.COLLABORATOR)
	public List<NatureDTO> list() {
		return natureService.list().stream().map(nature -> new NatureDTO(nature)).toList();
	}
	
	/**
	 * Saves a new {@link Nature} instance.
	 * 
	 * @param nature The new nature within the request body to be registered
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured({GDMRoles.ADMIN})
	public void create(@RequestBody NatureDTO nature) throws BadRequestException {
		natureService.create(nature.instantiate());
	}
	
	/**
	 * Gets a specific registered mission nature.
	 * 
	 * @param id The id corresponding to the nature to get
	 * @return The registered nature corresponding to the given id
	 */
	@GetMapping(path = "{id}")
	@Secured({GDMRoles.COLLABORATOR})
	public NatureDTO read(@PathVariable int id) throws BadRequestException {
		return new NatureDTO(natureService.read(id));
	}
	
	/**
	 * Updates the data for a specific registered mission nature.
	 * 
	 * @param id The id corresponding to the nature to update
	 * @param natureDTO The nature within the request body with modified info
	 * @return The resulting nature with updated info
	 */
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Secured(GDMRoles.ADMIN)
	public NatureDTO update(@PathVariable int id, @RequestBody NatureDTO natureDTO) throws BadRequestException {
		return new NatureDTO(natureService.update(id, natureDTO.instantiate()));
	}
	
	/**
	 * Deletes a specific registered mission nature.
	 * 
	 * @param id The id corresponding to the nature to delete
	 */
	@DeleteMapping(path = "{id}")
	@Secured(GDMRoles.ADMIN)
	public void delete(@PathVariable int id) throws BadRequestException {
		natureService.delete(id);
	}
	
}