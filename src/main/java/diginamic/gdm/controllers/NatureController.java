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

import diginamic.gdm.dao.Nature;
import diginamic.gdm.services.NatureService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "nature", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class NatureController {
	
	private NatureService natureService;

	@GetMapping
	public List<Nature> list() {
		return natureService.list();
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody Nature nature) {
		natureService.create(nature);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Nature update(@RequestBody Nature nature) {
		return natureService.update(nature);
	}
	
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) {
		natureService.delete(id);
	}
	
}