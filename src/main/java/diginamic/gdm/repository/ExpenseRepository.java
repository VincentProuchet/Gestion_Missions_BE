package diginamic.gdm.repository;

import diginamic.gdm.dao.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Expense;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByMission(Mission mission);

}
