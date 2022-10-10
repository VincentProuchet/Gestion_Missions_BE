package diginamic.gdm.services.implementations;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.MissionService;
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
	public Collaborator create(Collaborator collaborator) {
		return this.collaboratorRepository.save(collaborator);
	}

	@Override
	public Collaborator read(int id) throws BadRequestException {
		return this.collaboratorRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(ErrorCodes.collaboratorNotFound,
						CollaboratorErrors.read.NOT_FOUND ));
	}

	@Override
	public Collaborator update(int id, Collaborator collaborator) throws BadRequestException {		
		Collaborator current = this.read(id);
		current.setFirstName(collaborator.getFirstName());
		current.setLastName(collaborator.getLastName());
		current.setPassword(collaborator.getPassword());
		current.setEmail(collaborator.getEmail());
		current.setManager(collaborator.getManager());
		this.collaboratorRepository.save(current);
		return current;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Collaborator coll = this.collaboratorRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(CollaboratorErrors.read.NOT_FOUND));
		return coll;
	}

	@Override
	public Collaborator findByUserName(String userName) throws UsernameNotFoundException {
		return this.collaboratorRepository.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(CollaboratorErrors.read.NOT_FOUND));
	}

	/**
	 * this return connected user informations if 
	 * - there is a security context
	 * - user informations exist in database
	 *
	 */
	@Secured({ GDMRoles.ADMIN, GDMRoles.MANAGER, GDMRoles.COLLABORATOR })
	public Collaborator getConnectedUser() throws Exception {		
		SecurityContext context = SecurityContextHolder.getContext();// I left if well sequenced on purpose 
		Authentication auth = context.getAuthentication();
		// these are useless
		// the simple presence of the @secured annotation 
		// prevent any access without security context  
		// if (context == null){ throw new Exception(CollaboratorErrors.NO_SECURITY_CONTEXT); }
		// if (auth == null) { throw new Exception(CollaboratorErrors.NO_AUTHENTICATION_CONTEXT); }
		
		String username = (String) auth.getPrincipal();
		return collaboratorRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(CollaboratorErrors.read.NOT_FOUND));

	}

}
