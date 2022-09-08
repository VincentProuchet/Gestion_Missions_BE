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

import diginamic.gdm.dao.Mission;
import diginamic.gdm.services.MissionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "mission", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MissionController {
	
	private MissionService missionService;

	@GetMapping
	public List<Mission> list() {
		return missionService.list();
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody Mission mission) {
		missionService.create(mission);
	}
	
	@GetMapping(path = "{id}")
	public Mission read(@PathVariable int id) {
		return missionService.read(id);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mission update(@RequestBody Mission mission) {
		return missionService.update(mission);
	}
	
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) {
		missionService.delete(id);
	}
	
}