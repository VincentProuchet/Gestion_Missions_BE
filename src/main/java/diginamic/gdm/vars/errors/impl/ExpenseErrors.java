package diginamic.gdm.vars.errors.impl;

import diginamic.gdm.vars.errors.ErrorsMessage;

/**
 * Classe d'erreur du service des frais
 * 
 * @author Vincent
 *
 */

public class ExpenseErrors extends ErrorsMessage {
	

	/**
	 * @author Vincent
	 *
	 */
	public static class read {
		public static String NOT_FOUND = " Expense not found ";
	}

	/**
	 * message for invalid data
	 * @author Vincent
	 *
	 */
	public static class invalid {
		public static String NULL_DATE="Expense's date is null ";
		public static String NULL_TYPE="expense type can't be null ";
		public static String IS_BEFORE="Expense's date is before ";
		public static String IS_AFTER="Expenses's date is after ";
		
	}
}
