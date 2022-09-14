package diginamic.gdm.dao;

import javax.persistence.Entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Entity which represents an Administrator
 * 
 * @author Joseph
 *
 */
@Entity
public class Administrator extends Collaborator {

	

	/** serialVersionUID */
	private static final long serialVersionUID = 2L;

	{
		this.authority.add(new SimpleGrantedAuthority(this.getClass().getName()));
	}

}
