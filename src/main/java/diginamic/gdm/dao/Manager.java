package diginamic.gdm.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity which represents a Manager
 * 
 * @author Joseph
 *
*/
public class Manager extends Collaborator {


    @OneToMany(mappedBy = "manager")
    private Set<Collaborator> team = new HashSet<Collaborator>();

}
