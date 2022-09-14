package diginamic.gdm.services;

import java.util.List;

import org.springframework.security.provisioning.UserDetailsManager;

import diginamic.gdm.dao.Collaborator;

/**
 * Interface to be implemented by a collaborator service class.
 * 
 * @author DorianBoel
 */
public interface CollaboratorService extends UserDetailsManager {

	/**
	 * Gets the full list of registered collaborators.
	 * 
	 * @return A list of all collaborators
	 */
	List<Collaborator> list();
	
	/**
	 * Saves a new {@link Collaborator} instance.
	 * 
	 * @param mission The new collaborator to be registered
	 */
	void create(Collaborator collaborator);
	
	/**
	 * Gets a specific registered collaborator.
	 * 
	 * @param id The id corresponding to the collaborator to get
	 * @return The registered collaborator corresponding to the given id
	 */
	Collaborator read(int id);
	
	/**
	 * Updates the data for a specific registered collaborator.
	 * 
	 * @param id The id corresponding to the collaborator to update
	 * @param collaborator The collaborator to update with modified info
	 * @return The resulting collaborator with updated info
	 */
	Collaborator update(int id, Collaborator collaborator);
	
}
