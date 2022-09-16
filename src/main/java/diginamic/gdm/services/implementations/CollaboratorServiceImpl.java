package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;

/**
 * Implementation for {@link CollaboratorService}.
 * 
 * @author DorianBoel
 */
@Service
@AllArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService, UserDetailsService {

	/**
	 * The {@link CollaboratorRepository} dependency.
	 */
	private CollaboratorRepository collaboratorRepository;
	
	@Override
	public List<Collaborator> list() {
		return this.collaboratorRepository.findAll();
	}

	@Override
	public void create(Collaborator collaborator) {
		this.collaboratorRepository.save(collaborator);
	}
	
	@Override
	public Collaborator read(int id) {
		return this.collaboratorRepository.findById(id).orElseThrow();
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
		System.err.println("fetching username");
		
		
		System.err.println("we have something");
		Collaborator result =collaboratorRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException( username + " Non trouvé "));
		//ollaborator result = giveMEMockData();
		System.err.println("shit looks good");		
		return result;
	
	}
	
	/**
	 * for dev Only MUST be deleted in productions
	 * @return
	 */
	public Collaborator giveMEMockData() {
		System.err.println("making mock data");
		Collaborator coll = new Collaborator();
		coll.giveMeMockData();
			
		return coll;
	}

}
