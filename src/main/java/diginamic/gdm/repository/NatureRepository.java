package diginamic.gdm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Nature;

public interface NatureRepository extends JpaRepository<Nature, Integer> {

	List<Nature> findByEndOfValidityIsNull();

	List<Nature> findByDescription(String description);

	/**
	 * Find the currently active nature with given description if there is
	 * @param description
	 * @return the currently active nature with the given description, or null if there is none
	 */
	Nature findByDescriptionAndEndOfValidityIsNull(String description);

	/**
	 * Find the list of natures with the given description, ordered by starting dates
	 * @param description
	 * @return the ordered list of natures with the given description
	 */
	List<Nature> findByDescriptionOrderByDateOfValidityDesc(String description);
}
