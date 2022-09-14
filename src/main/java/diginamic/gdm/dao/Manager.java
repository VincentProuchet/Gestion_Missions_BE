package diginamic.gdm.dao;

import javax.persistence.Entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Entity which represents a Manager
 * 
 * @author Joseph
 *
 */
@Entity
public class Manager extends Collaborator {

	/** serialVersionUID */
	private static final long serialVersionUID = 3L;

	{
		this.authority.add(new SimpleGrantedAuthority(this.getClass().getName()));
	}
}
