package diginamic.gdm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Expense;
import diginamic.gdm.dao.Mission;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByMission(Mission mission);

}
