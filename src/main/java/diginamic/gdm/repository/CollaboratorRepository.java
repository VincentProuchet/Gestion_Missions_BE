package diginamic.gdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer>  {
	//Collaborator getByUsername(String username);
	Collaborator findByUsername(String username);
}
