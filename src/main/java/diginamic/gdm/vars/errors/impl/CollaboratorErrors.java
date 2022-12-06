package diginamic.gdm.vars.errors.impl;

import diginamic.gdm.vars.errors.ErrorsMessage;

/**
 * classe d'erreurs des collaborateurs
 *
 * @author Vincent
 *
 */
public class CollaboratorErrors extends ErrorsMessage {


	/**
	 * pour les erreur lors de lectures
	 *
	 * @author Vincent
	 *
	 */
	public static class create {
		public static String NAME_ALLREADY_EXIST = " allready exist with username : ";
		public static String BAD_FIRSTNAME = " firstnames can't contain : numbers,spaces,- and special chars";
		public static String BAD_LASTNAME = " lastname can't contain : numbers,spaces, special chars and more than 2 minus '--' ";
		public static String BAD_USERNAME = " username can't contain : spaces and special chars";
		public static String BAD_EMAIL = " email has an invalid form. ";
	}
	/**
	 * pour les erreur lors de lectures
	 *
	 * @author Vincent
	 *
	 */
	public static class read {
		/** NOTFOUND */
		public static String NOT_FOUND = "user not found ";
	}

}
