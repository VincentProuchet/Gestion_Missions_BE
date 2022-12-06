package diginamic.gdm.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;

/**
 * Entity which represents a Manager
 * we stopped used those because it caused
 * the Spring security to not bing able to convert
 * from entities coming form the database to be converter
 * to their precious UserDetails
 * Its kept here as a Grim reminder
 * @author Joseph
 *
*/
@Deprecated
public class Manager extends Collaborator {



	/** serialVersionUID */
	private static final long serialVersionUID = -3424216385400607728L;
	@OneToMany(mappedBy = "manager")
    private Set<Collaborator> team = new HashSet<>();

}
