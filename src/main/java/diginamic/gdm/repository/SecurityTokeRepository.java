package diginamic.gdm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.SecurityToken;

public interface SecurityTokeRepository extends JpaRepository<SecurityToken, Integer>{
	Optional<SecurityToken> findByGrant(String grant);
	Optional<SecurityToken> findByAuthentification(String authentification);
	Optional<SecurityToken> findByRefresh(String refresh);
	Optional<SecurityToken> findByCollaborator(Collaborator collaborator);
}
