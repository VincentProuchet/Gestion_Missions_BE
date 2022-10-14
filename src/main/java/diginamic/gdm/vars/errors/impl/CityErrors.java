package diginamic.gdm.vars.errors.impl;

import diginamic.gdm.vars.errors.ErrorsMessage;

/**
 * City service error messages
 * @author Vincent
 *
 */
public class CityErrors extends ErrorsMessage {
	/**
	 * pour les erreur lors de lectures
	 * 
	 * @author Vincent
	 *
	 */
	public static class read {
		/** NOTFOUND */
		public static String NOT_FOUND = "City not found ";
		public static String INVALID_NAME = "City's name can't have punctuation";
		public static String ALLREADY_EXIST = "City's with name allready exist";
	}
}
