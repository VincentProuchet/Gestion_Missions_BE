package diginamic.gdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

}
