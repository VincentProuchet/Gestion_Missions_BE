package diginamic.gdm.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Nature;

public interface NatureRepository extends JpaRepository<Nature, Integer> {

	// Set<Nature> findByEndOfValidityIsNull(); TODO needs testing
}
