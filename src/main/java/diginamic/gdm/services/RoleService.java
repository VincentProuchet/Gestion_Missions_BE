package diginamic.gdm.services;


import java.util.List;

import diginamic.gdm.dao.Roles;

/**
 * @author Vincent
 *
 */
public interface RoleService {
	public List<Roles>all()throws Exception;
	public Roles read(int id)throws Exception;
	public Roles read(String label)throws Exception;
	public void create(Roles role)throws Exception;	
	public Roles update(Roles role)throws Exception;
	public void saveAutorities();
}
