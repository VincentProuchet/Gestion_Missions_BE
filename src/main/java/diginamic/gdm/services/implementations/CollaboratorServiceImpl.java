package diginamic.gdm.services.implementations;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.vars.GDMRoles;
import diginamic.gdm.vars.errors.impl.CollaboratorErrors;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link CollaboratorService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
@Transactional
public class CollaboratorServiceImpl implements CollaboratorService {

	/**
	 * The {@link CollaboratorRepository} dependency.
	 */
	private CollaboratorRepository collaboratorRepository;

	// private MissionService missionService;

	@Override
	public List<Collaborator> list() {
		return this.collaboratorRepository.findAll();
	}

	@Override
	public Collaborator create(Collaborator collaborator) throws Exception {
		// names Must respect some standards
		this.isValidUser(collaborator);;
		// that username must not allready exist
		if (!this.collaboratorRepository.findByUsername(collaborator.getUsername()).isEmpty()) {
			throw new BadRequestException(ErrorCodes.collaboratorNotFound,
					CollaboratorErrors.create.NAME_ALLREADY_EXIST, collaborator.getUsername());
		}

		return this.collaboratorRepository.save(collaborator);
	}

	@Override
	public Collaborator read(int id) throws BadRequestException {
		return this.collaboratorRepository.findById(id).orElseThrow(
				() -> new BadRequestException(ErrorCodes.collaboratorNotFound, CollaboratorErrors.read.NOT_FOUND));
	}

	@Override
	public Collaborator update(int id, Collaborator collaborator) throws Exception {
		Collaborator current = this.read(id);
		this.isValidUser(collaborator);
		current.setFirstName(collaborator.getFirstName());
		current.setLastName(collaborator.getLastName());
		current.setPassword(collaborator.getPassword());
		current.setEmail(collaborator.getEmail());
		current.setManager(collaborator.getManager());
		current.setUsername(collaborator.getUsername());
		
		// we get a list with the current user filtered out
		Optional<Collaborator> OptexistingUsers = this.collaboratorRepository.findByUsername(collaborator.getUsername())
				.filter((element) -> element.getId() != current.getId());
		// if the is one user, that is not the current user withe the same userName
		if (!OptexistingUsers.isEmpty()) {
			// we throw an errors
			throw new BadRequestException(ErrorCodes.collaboratorNotFound,
					CollaboratorErrors.create.NAME_ALLREADY_EXIST, collaborator.getUsername());
		}
		else {
			current.setUsername(collaborator.getUsername());
		}

		this.collaboratorRepository.save(current);
		return current;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.collaboratorRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(CollaboratorErrors.read.NOT_FOUND));
	}

	@Override
	public Collaborator findByUserName(String userName) throws UsernameNotFoundException {
		return this.collaboratorRepository.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(CollaboratorErrors.read.NOT_FOUND));
	}

	/**
	 * this return connected user informations if - there is a security context -
	 * user informations exist in database
	 *
	 */
	@Secured({ GDMRoles.ADMIN, GDMRoles.MANAGER, GDMRoles.COLLABORATOR })
	public Collaborator getConnectedUser() throws BadRequestException {
		SecurityContext context = SecurityContextHolder.getContext();// I left if well sequenced on purpose
		Authentication auth = context.getAuthentication();		
		 if (auth == null) { throw new BadRequestException(CollaboratorErrors.NO_AUTHENTICATION_CONTEXT); }
		String username = (String) auth.getPrincipal();
		return collaboratorRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(CollaboratorErrors.read.NOT_FOUND));
	}

	/**
	 * valid user data integrity
	 * @param collaborator
	 * @throws BadRequestException
	 */
	private void isValidUser(Collaborator collaborator) throws BadRequestException {
		// names Must respect some standards

		if (!Collaborator.isValidFirstName(collaborator.getFirstName())) {
			throw new BadRequestException(ErrorCodes.collaboratorNotFound, CollaboratorErrors.create.BAD_FIRSTNAME);
		}
		;
		if (!Collaborator.isValidLastName(collaborator.getLastName())) {
			throw new BadRequestException(ErrorCodes.collaboratorNotFound, CollaboratorErrors.create.BAD_LASTNAME);
		}
		;
		if (!Collaborator.isValidUserName(collaborator.getUsername())) {

			throw new BadRequestException(ErrorCodes.collaboratorNotFound, CollaboratorErrors.create.BAD_USERNAME);
		}
		;
		if (!Collaborator.isValidEmail(collaborator.getEmail())) {
			throw new BadRequestException(ErrorCodes.collaboratorNotFound, CollaboratorErrors.create.BAD_EMAIL);
		}
		;
	}

	@Override
	public Collaborator delete(int id, Collaborator collaborator) throws Exception {
		if(id!= collaborator.getId()) {
			throw new BadRequestException(ErrorCodes.idInconsistent,CollaboratorErrors.INCONSISTENT_ID);
		}
		Collaborator current = this.read(id);
		// deletion is just setting it to  not active
				current.setActive(false);
		return this.collaboratorRepository.save(current) ;
	}

}
