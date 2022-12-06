package diginamic.gdm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.City;

public interface CityRepository extends JpaRepository<City, Integer> {

	 Optional<City>findByName(String name);

}
