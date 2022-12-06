package diginamic.gdm.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import diginamic.gdm.dto.ExpenseTypeDTO;
import diginamic.gdm.vars.GDMVars;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents the nature of an expense
 *
 * @author Joseph
 *
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ExpenseType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** name : the name of the type of expense */
	@Column(nullable = false,unique = true)
	private String name;

	public ExpenseType(ExpenseTypeDTO et) {
		this.id = et.getId();
		this.setName(et.getName());
	}


	/**
	 * SETTER
	 * @param name
	 */
	public void setName(String name) {
		name = name.replaceAll(GDMVars.REGEX_NAMES,"");
		name = name.replaceAll(GDMVars.REGEX_STUPID_WHITSPACES," ");
		name = name.replaceAll(GDMVars.REGEX_STUPID_MINUS,"-");
		name =name.strip().toLowerCase();
		this.name =  name;
	}


	/** Constructeur
	 * @AllArgConstructor
	 * @param id
	 * @param name
	 */
	public ExpenseType(int id, String name) {
		this.id = id;
		this.setName(name);
	}

}
