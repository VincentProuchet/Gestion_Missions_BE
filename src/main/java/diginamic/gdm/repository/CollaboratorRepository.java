package diginamic.gdm.repository;

import diginamic.gdm.dao.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Collaborator;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer> {

    List<Collaborator> findByManager(Manager manager);
}
