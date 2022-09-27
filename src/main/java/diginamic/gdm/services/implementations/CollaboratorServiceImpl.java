package diginamic.gdm.services.implementations;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import diginamic.gdm.GDMRoles;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.services.MissionService;
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

	//private MissionService missionService;
	
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
		return this.collaboratorRepository.findById(id).orElseThrow(() -> new BadRequestException("Collaborator not found", ErrorCodes.collaboratorNotFound));
	}

	@Override
	public Collaborator update(int id, Collaborator collaborator) {
		Collaborator current = this.collaboratorRepository.findById(id).orElseThrow();
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
		Collaborator coll = this.collaboratorRepository
				.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(" Username not found ")) ;
		return coll;
	}

	
// deleted for circular dependency
//	@Override
//	public Mission addMission(Mission mission, Collaborator collaborator) throws BadRequestException {
//		mission.setCollaborator(collaborator);
//		return missionService.create(mission);
//	}
//
//	@Override
//	public Mission reassignMission(Mission mission, Collaborator collaborator) throws BadRequestException {
//		mission.setCollaborator(collaborator);
//		return missionService.update(mission.getId(), mission);
//	}

	@Override
	public Collaborator findByUserName(String userName) throws UsernameNotFoundException {
		return this.collaboratorRepository.findByUsername(userName).orElseThrow(()-> new UsernameNotFoundException(" Can't find connected username") );
	}

	@Secured({GDMRoles.ADMIN,GDMRoles.MANAGER,GDMRoles.COLLABORATOR})
	public Collaborator getConnectedUser()throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String username = (String) auth.getPrincipal();
		return collaboratorRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(" Can't find connected username") );
		
	}

}
