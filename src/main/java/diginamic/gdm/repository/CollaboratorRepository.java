package diginamic.gdm.repository;

import diginamic.gdm.dao.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Collaborator;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer>  {
	
	Collaborator findByUsername(String username);
    List<Collaborator> findByManager(Collaborator manager);
}
