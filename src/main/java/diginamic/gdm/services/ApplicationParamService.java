package diginamic.gdm.services;

import java.util.List;

import diginamic.gdm.dao.ApplicationParams;

/**
 * @author Vincent
 *
 */
public interface ApplicationParamService {
	
	
	/**
	 * @return
	 */
	List<ApplicationParams> list();
	
	/**
	 * @param param
	 * @return ApplicationParams
	 * @throws Exception
	 */
	ApplicationParams create(ApplicationParams param)throws Exception;
	
	/**
	 * @param id
	 * @return ApplicationParams
	 * @throws Exception
	 */
	ApplicationParams read(int id)throws Exception;
	/**
	 * @param name
	 * @return ApplicationParams
	 * @throws Exception
	 */
	ApplicationParams read(String name)throws Exception;

	/**
	 * @param param
	 * @return ApplicationParams
	 * @throws Exception
	 */
	ApplicationParams update(ApplicationParams param)throws Exception;
	
	/**
	 * @param param
	 * @return ApplicationParams
	 * @throws Exception
	 */
	ApplicationParams delete(ApplicationParams param)throws Exception;
}
