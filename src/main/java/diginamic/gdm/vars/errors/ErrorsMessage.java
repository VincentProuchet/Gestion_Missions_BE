package diginamic.gdm.vars.errors;

/**
 * 
 * classe mère des messages d'erreurs 
 * censée contenir les messages de base 
 * @author Vincent
 *
 */
public class ErrorsMessage {
	/** CITY */
	public static String CITY ="city ";
	/** COLLABORATEUR */
	public static String COLLABORATEUR ="collaborator ";
	/** MISSION */
	public static String MISSION = "mission ";
	/** NATURE */
	public static String NATURE = "nature ";
	/** STATUS */
	public static String STATUS = "status ";
	/** TRANSPORT */
	public static String TRANSPORT = "transport ";
	/** EXPENSE */
	public static String EXPENSE = "expense ";
	/** EXPENSE_TYPE */
	public static String EXPENSE_TYPE = "expense's type ";
	/** DATE */
	public static String DATE = "date ";	
	/** END */
	public static String END = "end ";
	/** START */
	public static String START = "start ";
	/** NOW */
	public static String NOW = "today date ";	
	/** ON */
	public static String ON = "on ";
	/** AND */
	public static String AND = "and "; 
	/** ADDED */
	public static String ADDED = "added "; 
	/** CANT_BE */
	public static String CANT_BE ="can't be ";
	/** NULL */
	public static String NULL = "null. ";
	/** FROM_TODAY */
	public static String FROM_TODAY = "days from today. ";
	/** BEFORE */
	public static String BEFORE = "before ";
	/** NO_SECURITY_CONTEXT */
	public static String NO_SECURITY_CONTEXT = "security context is null";
	/** NO_AUTHENTICATION_CONTEXT */
	public static String NO_AUTHENTICATION_CONTEXT = "authentication context is null";
	/** INCONSISTENT_ID */
	public static String INCONSISTENT_ID = "inconsistent IDs ";
	
	public static String VALUE = "Expense cost ";
	public static String TAXES = "TVA ";
	public static String NEGATIVE="négative ";
	/**
	 * @author Vincent
	 *
	 */
	public static class create {
		
	}

	/**
	 * pour les erreur lors de lectures
	 * 
	 * @author Vincent
	 *
	 */
	public static class read {
		/** NOTFOUND */
		public static String NOT_FOUND = "not found ";
	}

	/**
	 * @author Vincent
	 *
	 */
	public static class update {
		/** CANT_BE_MODIFIED */
		public static String CANT_BE_MODIFIED = "can't be modified. ";
	}

	/**
	 * @author Vincent
	 *
	 */
	public static class delete {
		/** CANT_BE_DELETED */
		public static String CANT_BE_DELETED="can't be deleted.";
	}

	/**
	 * @author Vincent
	 *
	 */
	public static class invalid {
		/** START_CANT_BE */
		public static String START_CANT_BE="can't start ";
		/** END_CANT_BE */
		public static String END_CANT_BE="can't end ";
	}
}
