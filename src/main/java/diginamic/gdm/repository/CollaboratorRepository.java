package diginamic.gdm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import diginamic.gdm.dao.Collaborator;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer>  {

	Optional<Collaborator> findByUsername(String username);
    List<Collaborator> findByManager(Collaborator manager);
}
