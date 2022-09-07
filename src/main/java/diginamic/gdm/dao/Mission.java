package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which reprensents a Collaborator
 * 
 * @author Joseph
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** startDate : the date of the start of the mission */
	private LocalDateTime startDate;

	/** startDate : the date of the end of the mission */
	private LocalDateTime endDate;

	/** bonus : the bonus for the collaborator */
	private BigDecimal bonus;

}
