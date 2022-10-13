package diginamic.gdm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.ExpenseType;

public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Integer> {
	 
	Optional<ExpenseType>findByName(String name);
}
