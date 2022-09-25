package diginamic.gdm.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import diginamic.gdm.dto.ExpenseDTO;
import diginamic.gdm.dto.ExpenseTypeDTO;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
		this.name = et.getName();
	}

}
