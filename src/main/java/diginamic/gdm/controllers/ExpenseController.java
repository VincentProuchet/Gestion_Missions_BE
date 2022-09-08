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

import diginamic.gdm.dao.Expense;
import diginamic.gdm.services.ExpenseService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "frais", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ExpenseController {
	
	private ExpenseService expenseService;

	@GetMapping
	public List<Expense> list() {
		return expenseService.list();
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody Expense expense) {
		expenseService.create(expense);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Expense update(@RequestBody Expense expense) {
		return expenseService.update(expense);
	}
	
	@DeleteMapping(path = "{id}")
	public void delete(@PathVariable int id) {
		expenseService.delete(id);
	}
	
}