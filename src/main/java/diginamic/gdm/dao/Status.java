package diginamic.gdm.dao;

/**
 * Represents the status of the request for the mission
 * 
 * @author Joseph
 *
 */
public enum Status {
	/** the mission is created */
	INIT, 
	/** 
	 * after an overnight process
	 *  the mission appear to managers for human decision 
	 */
	WAITING_VALIDATION,
	/** mission is validated by management */
	VALIDATED,
	/** 
	 * mission was rejected by management 
	 * and turned back to their creator 
	 * for modifications 
	 * or deletion
	 */
	REJECTED, 
	/** 
	 * the mission as ended its a new status made up for accelerating 
	 * missions treatments  
	 * not having these in the queue is
	 *	faster than comparing LocalDateTime 
	 */
	ENDED,
}
