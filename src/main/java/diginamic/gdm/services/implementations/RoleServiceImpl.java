package diginamic.gdm.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.repository.RoleRepository;
import diginamic.gdm.services.RoleService;
import lombok.AllArgsConstructor;

/**
 * @author Vincent
 *
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

	private RoleRepository repository;

	@Override
	public Roles create(Roles role) {
		return this.repository.save(role);
	}

	@Override
	public Roles update(Roles role) {
		Roles role2 = this.repository.getReferenceById(role.getId());
		if (role2 == null) {
			this.repository.save(role);
			role2 = role;
		} else {
			role2.label = role.label;
			this.repository.save(role2);
		}
		return role;
	}
	
	/**
	 * This was coded to save all instances of Role enum 
	 * in one go
	 * it may not be useful in the end
	 * but it was usefull for testing
	 */
	public void saveAutorities() {
		
		ArrayList<Roles> rols = new ArrayList<Roles>();
		for (Role r : Role.values()) {
			rols.add(new Roles(r.getId(),r.getLABEL()));
			this.repository.saveAll(rols);
			
		}
		System.err.println("roles saved");
	}

	@Override
	public List<Roles> all() {
		
		return this.repository.findAll();
	}

	@Override
	public Roles read(int id) throws Exception {
		
		return this.repository.findById(id).orElseThrow(()-> new Exception(" authoritie not found ")) ;
	}

	@Override
	public Roles read(String label) throws Exception {
		
		return this.repository.findByLabel(label).orElseThrow(()-> new Exception(" authoritie not found "));
	}

}
