package diginamic.gdm.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	@Fetch(FetchMode.JOIN)
	private Set<Expense> expenses = new HashSet<>();

	/** collaborator : the collaborator this mission is due to */
	@ManyToOne
	@JoinColumn(name = "collaboratorID")
	private Collaborator collaborator;

}
