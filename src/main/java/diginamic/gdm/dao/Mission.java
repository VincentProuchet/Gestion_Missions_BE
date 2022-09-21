package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity which represents a Collaborator
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
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** startDate : the date of the start of the mission */
	private LocalDateTime startDate;

	/** startDate : the date of the end of the mission */
	private LocalDateTime endDate;

	/** bonus : the bonus for the collaborator */
	private BigDecimal bonus;

	/** missionTransport : the type of transport for the mission */
	@Enumerated(EnumType.STRING)
	private Transport missionTransport;

	/** status : the status of the mission request */
	@Enumerated(EnumType.STRING)
	private Status status;

	/**
	 * nature : the nature of the mission, contains common details about the mission
	 */
	@ManyToOne
	@JoinColumn(name = "natureID")
	private Nature nature;

	/** startCity : the city where the collaborator is initially */
	@ManyToOne
	@JoinColumn(name = "startCityID")
	private City startCity;

	/** startCity : the city where the mission holds place */
	@ManyToOne
	@JoinColumn(name = "endCityID")
	private City endCity;

	/** expenses : business expenses for the mission */
	@OneToMany(mappedBy = "mission")
	//@Fetch(FetchMode.JOIN)
	private Set<Expense> expenses = new HashSet<>();

	/** collaborator : the collaborator this mission is due to */
	@ManyToOne
	@JoinColumn(name = "collaboratorID")
	private Collaborator collaborator;
}
