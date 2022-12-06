package diginamic.gdm.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.repository.RoleRepository;
import diginamic.gdm.services.RoleService;
import diginamic.gdm.vars.errors.impl.RolesErrors;
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
		return this.repository.findByLabel(role.getLabel())
				.orElseGet(()-> this.repository.save(role));
		}

	@Override
	public Roles update(Roles role) {
		return this.repository.findByLabel(role.getLabel())
				.orElseGet(()-> this.repository.save(role));
	}
	/**
	 * This was coded to save all instances of Role enum
	 * in one go
	 * it may not be useful in the end
	 * but it was usefull for testing
	 */
	@Override
	public void saveAutorities() {

		for (Role r : Role.values()) {
				this.create(new Roles(r));
		}
	}

	@Override
	public List<Roles> all() {

		return this.repository.findAll();
	}

	@Override
	public Roles read(int id) throws Exception {

		return this.repository.findById(id).orElseThrow(()-> new Exception(RolesErrors.read.NOT_FOUND)) ;
	}

	@Override
	public Roles read(String label) throws Exception {

		return this.repository.findByLabel(label).orElseThrow(()-> new Exception(RolesErrors.read.NOT_FOUND));
	}

}
