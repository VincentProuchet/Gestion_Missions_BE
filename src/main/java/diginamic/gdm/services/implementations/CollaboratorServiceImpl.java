package diginamic.gdm.services.implementations;

import java.util.List;

import diginamic.gdm.dao.Mission;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.exceptions.ErrorCodes;
import diginamic.gdm.services.MissionService;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;

import javax.transaction.Transactional;

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

	private MissionService missionService;
	
	@Override
	public List<Collaborator> list() {
		return this.collaboratorRepository.findAll();
	}

	@Override
	public void create(Collaborator collaborator) {
		this.collaboratorRepository.save(collaborator);
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
	public boolean addMission(Mission mission, Collaborator collaborator) throws BadRequestException {
		mission.setCollaborator(collaborator);
		return missionService.create(mission);
	}

	@Override
	public Mission reassignMission(Mission mission, Collaborator collaborator) throws BadRequestException {
		mission.setCollaborator(collaborator);
		return missionService.update(mission.getId(), mission);
	}

}
