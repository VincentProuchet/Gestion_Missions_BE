package diginamic.gdm.vars.errors.impl;

import diginamic.gdm.vars.errors.ErrorsMessage;

/**
 * classe de message d'erreur pour les natures
 *
 * @author Vincent
 *
 */
public class NatureErrors extends ErrorsMessage {
	/**
	 * @author Vincent
	 *
	 */
	public static class create {
		public static String SAME_NAME = "A nature with the same name allready exist, please consider modify the existing one instead. ";

	}
	/**
	 * pour les erreur lors de lectures
	 *
	 * @author Vincent
	 *
	 */
	public static class read {
		/** NOTFOUND */
		public static String NOT_FOUND = "Nature not found ";
	}

	/**
	 * @author Vincent
	 *
	 */
	public static class update {
		/** CANT_BE_MODIFIED */
		public static String CANT_BE_MODIFIED = "Nature can't be modified. ";
		public static String CANT_CHANGE_NAME = "you are trying to change the name of the nature and there is allready an active nature of the name : ";

	}

	/**
	 * @author Vincent
	 *
	 */
	public static class delete {
		/** CANT_BE_DELETED */
		public static String CANT_BE_DELETED="Nature can't be deleted.";
		public static String IS_ASSIGNED="Nature as assigned mission and can't be deleted.";
	}

	/**
	 * @author Vincent
	 *
	 */
	public static class invalid {
		/** START_CANT_BE */
		public static String START_CANT_BE="Start date of validity can't be ";
		/** END_CANT_BE */
		public static String END_CANT_BE_BEFORE_START="End of validity can't be before start ";
		public static String NULL_DESCIPTION = "description can't be null ";
		public static String EMPTY_DESCIPTION = "description can't be empty ";
		public static String NEGATIVE_BONUS = " bonus can't be negative ";
		public static String NEGATIVE_TJM = " TJM can't be negative ";
	}
}
