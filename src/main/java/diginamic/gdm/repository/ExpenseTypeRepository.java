package diginamic.gdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.ExpenseType;

public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Integer> {

}
