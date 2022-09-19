package diginamic.gdm.services;

import diginamic.gdm.dao.Roles;

public interface RoleService {
	public void create(Roles role);	
	public Roles update(Roles role);
	public void saveAutorities();
}
