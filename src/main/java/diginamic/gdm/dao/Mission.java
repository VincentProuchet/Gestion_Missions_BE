package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import diginamic.gdm.dto.MissionDTO;
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
	@Column(nullable = false)
	private LocalDateTime startDate;

	/** endDate : the date of the end of the mission */
	@Column(nullable = false)
	private LocalDateTime endDate;

	/** bonus : the bonus for the collaborator */
	private BigDecimal bonus;

	/** hasBonusBeenEvaluated : a technical data to make it easier to get missions missing a bonus
	 * it is true only after the night computing has set the value of bonus */
	private boolean hasBonusBeenEvaluated = false;

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
	@JoinColumn(name = "natureID",nullable = false)
	private Nature nature;

	/** startCity : the city where the collaborator is initially */
	@ManyToOne
	@JoinColumn(name = "startCityID",nullable = false)
	private City startCity;

	/** startCity : the city where the mission holds place */
	@ManyToOne
	@JoinColumn(name = "endCityID",nullable = false)
	private City endCity;

	/** expenses : business expenses for the mission */
	@OneToMany(mappedBy = "mission")
	@Fetch(FetchMode.JOIN)
	private Set<Expense> expenses = new HashSet<>();

	/** collaborator : the collaborator this mission is due to */
	@ManyToOne
	@JoinColumn(name = "collaboratorID", nullable = false)
	private Collaborator collaborator;
	
	public Mission(MissionDTO m, City start, City arrival,Collaborator collaborator) {
		
		super();
		this.id = m.getId();
		this.startDate = m.getStart();
		this.endDate = m.getEnd();
		this.bonus = m.getBonus();
		this.missionTransport = m.getTransport();
		this.nature = m.getNature().instantiate();
		this.startCity = start;
		this.endCity = arrival;
		this.collaborator = collaborator;
	}

	
	
	
}
