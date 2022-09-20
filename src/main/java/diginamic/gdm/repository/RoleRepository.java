package diginamic.gdm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Roles;

public interface RoleRepository extends JpaRepository<Roles,Integer> {
	Optional<Roles>	findByLabel(String label);
}
