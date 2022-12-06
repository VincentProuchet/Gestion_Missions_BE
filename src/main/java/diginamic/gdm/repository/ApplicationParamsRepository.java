package diginamic.gdm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.ApplicationParams;

public interface ApplicationParamsRepository extends JpaRepository<ApplicationParams, Integer> {

	Optional<ApplicationParams> findByName(String name);

}
