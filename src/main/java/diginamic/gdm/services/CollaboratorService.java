package diginamic.gdm.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.exceptions.BadRequestException;

/**
 * Interface to be implemented by a collaborator service class.
 *
 * @author DorianBoel
 */
public interface CollaboratorService extends UserDetailsService {

	/**
	 * Gets the full list of registered collaborators.
	 *
	 * @return A list of all collaborators
	 */
	List<Collaborator> list();

	/**
	 * Saves a new {@link Collaborator} instance.
	 *
	 * @param collaborator The new collaborator to be registered
	 * @throws BadRequestException
	 * @throws Exception
	 */
	Collaborator create(Collaborator collaborator) throws  Exception;

	/**
	 * Gets a specific registered collaborator.
	 *
	 * @param id The id corresponding to the collaborator to get
	 * @return The registered collaborator corresponding to the given id
	 */
	Collaborator read(int id) throws BadRequestException;

	/**
	 * Updates the data for a specific registered collaborator.
	 *
	 * @param id The id corresponding to the collaborator to update
	 * @param collaborator The collaborator to update with modified info
	 * @return The resulting collaborator with updated info
	 * @throws BadRequestException
	 * @throws Exception
	 */
	Collaborator update(int id, Collaborator collaborator) throws Exception;

	/**
	 * Collaborator deletion
	 * it's not supposed to delete a collaborator data
	 * only switch its account to inactive
	 * @param id
	 * @param collaborator
	 * @return
	 * @throws Exception
	 */
	Collaborator delete(int id, Collaborator collaborator) throws Exception;


	/**
	 * Add a new mission to a collaborator
	 *
	 * @param mission
	 * @param collaborator
	 * @return
	 */
//	Mission addMission(Mission mission, Collaborator collaborator) throws BadRequestException;

	/**
	 * Reassign an existing mission to another collaborator
	 *
	 * @param mission
	 * @param collaborator
	 * @return
	 */
//	Mission reassignMission(Mission mission, Collaborator collaborator) throws BadRequestException;

	/**
	 * Collaborator from username
	 * to not mistake from UserDetail from username
	 * @param userName
	 * @return Collaborator
	 * @throws Exception
	 */
	Collaborator findByUserName(String userName)throws Exception;

	/**
	 * Let you get Collaborator data from
	 * the user in  the SecurityContext
	 *
	 * You NEED to have a user connected
	 *
	 * @return Connected Collaborator
	 * @throws Exception
	 */
	public Collaborator getConnectedUser()throws Exception ;
}
