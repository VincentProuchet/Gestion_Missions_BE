package diginamic.gdm.services.implementations;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dao.Roles;
import diginamic.gdm.repository.RoleRepository;
import diginamic.gdm.services.RoleService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

	private RoleRepository repository;

	@Override
	public void create(Roles role) {
		this.repository.save(role);
	}

	@Override
	public Roles update(Roles role) {
		Roles role2 = this.repository.getReferenceById(role.getId());
		if (role2 == null) {
			this.repository.save(role);
			role2 = role;
		} else {
			role2.LABEL = role.LABEL;
			this.repository.save(role2);
		}
		return role;
	}
	public void saveAutorities() {
		
		ArrayList<Roles> rols = new ArrayList<Roles>();
		for (Role r : Role.values()) {
			rols.add(new Roles(r.getId(),r.getLABEL()));
			this.repository.saveAll(rols);
		}
	}

}
