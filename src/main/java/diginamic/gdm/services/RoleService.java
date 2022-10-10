package diginamic.gdm.services;


import java.util.List;

import diginamic.gdm.dao.Roles;

/**
 * @author Vincent
 *
 */
public interface RoleService {
	/** Get all role/authorities
	 * @return
	 * @throws Exception
	 */
	public List<Roles>all()throws Exception;
	/**
	 * get one role by id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Roles read(int id)throws Exception;
	/**
	 * get one role by label
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public Roles read(String label)throws Exception;
	/**
	 * save that role to persistence
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public Roles create(Roles role);
	/**
	 * update that role in persistence
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public Roles update(Roles role)throws Exception;
	/**
	 * this may depends on you implementation
	 * refer to the implementation documentation 
	 * for more information
	 * Originaly its intended to populate en empty database with
	 * all Roles found in a Role enum
	 */
	public void saveAutorities();
}
