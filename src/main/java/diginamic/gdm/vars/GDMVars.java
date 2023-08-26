package diginamic.gdm.vars;

/**
 * Classe utilitaire qui n'a d'autre but que de centraliser toutes les variables
 * Globales du Back-END vous avez besoins d'un variable int pour la durée de vos
 * cookies ? mettez la ici
 *
 * @author Vincent
 *
 */
public abstract class GDMVars {

	/** Durée de vie des tokens */
	public final static int TOKEN_LIFE = 36000; // ici 10H
	/** SHEDULED_INTERVAL intervel pour les tache planifiées */
	public final static long SHEDULED_INTERVAL = 7200000L;
	/** SESSION_SESSION_COOKIE_NAME nom du cookies de session Spring */
	public final static String SESSION_SESSION_COOKIE_NAME = "JSESSIONID";
	/**
	 * LOGINPAGE chemin ver la page login dans le dossier ressources notez le fait
	 * que le dossier public est le dossier par défaut
	 */
	public final static String LOGINPAGE = "/login.html";
	/** MAX_AUTHORIZED_SESSION */
	public final static int MAX_AUTHORIZED_SESSION = 1;
	/**
	 * temps minimal autorisé lorsque une mission utilise des transport de type
	 * aérien
	 *
	 */
	public final static int MIN_DAYS_BEFORE_FLIGHT_TRANSPORT = 7;
	/** FRONT_END_URL utilisé pour les réglage de CORS */
	public final static String FRONT_END_URL = "http://localhost:4200";
	/** BACK_END_URL utilisé pour les réglage de CORS */
	public final static String BACK_END_URL = "http://localhost:8080";

	/**
	 * REGEX
	 *
	 * - in replaceall will replace anything that is not a letter/number whithespace
	 * or a '-' - in mathes will say yes if found anything that is not a
	 * letter/number whithespace or a '-'
	 */
	public final static String REGEX_NAMES = "[^\\p{L}\\p{N}\s\\-]";


	/** REGEX_CITY_NAMES
	 * city names can be up to 180 char
	 * with "' ", ' , -  or space in the name and as many as necessary
	 * but they can't be consecutives
	 * reject all other punctuations
	 * accept UPPERCASES
	 * */
	public final static String REGEX_CITY_NAMES = "^([\\p{L}\\d]{1,1}((?<appoSpace>' )|(?<appo>')|[\\s\\-]|[\\p{L}\\d]){0,1}[\\p{L}\\d]{1,2}){1,45}$";
	/**
	 * REGEX
	 *
	 * accept letters/numbers  and one -  or one space (ex : e-r or : e r)
	 * reject e- or -e
	 * must be 2 < lettersLong < 128
	 * reject uppercases
	 * reject punctuation
	 * reject over 128
	 *
	 */
		public final static String REGEX_VALIDATE_NAMES = "^[\\p{L}\\d]{1,32}{\\s\\-}{0,1}[\\p{L}\\d]{1,32}[\\\\p{L}\\\\d]{1,32}{\\\\s\\\\-}{0,1}[\\\\p{L}\\\\d]{1,32}$";
	/**
	 * REGEX for Usernames
	 *
	 * Note that we accept digit for testing purpose
	 *
	 * - in mathes will say yes if string contains nothing but letters,numbers,- or _
	 * 															and has 6 to 64 char
	 */
	public final static String REGEX_USERNAMES = "^([\\p{L}\\d]{1,1}[_\\-]{0,1}[\\p{L}\\d]{1,2}){1,16}$";
	/**
	 * REGEX for humans last names
	 *
	 * Note that we accept digit for testing purpose

	 * mathes will say yes if string contains nothing but letters,numbers,- or --  but
	 * the name can be lastname | last-name | last--name
	 *
	 */
	public final static String REGEX_HUMANS_LAST_NAMES =  "^[\\p{L}\\d]{1,32}\\-{0,2}[\\p{L}\\d]{1,32}$";
	/**
	 * REGEX for humans first names
	 *
	 * Note that we accept digit for testing purpose
	 * the regex accept composed first names
	 * must have a min of 2 letters

	 */
	public final static String REGEX_HUMANS_FIRST_NAMES = "^[\\p{L}\\d]{1,32}\\-{0,1}[\\p{L}\\d]{1,32}$";

	/**
	 * REGEX
	 *
	 * pattern for e-mail validation
	 * accept up to 2 - or . but not consecutives (ex a-a-a@d.com)
	 * accept one - after the @
	 * reject UPPERCASES
	 * reject punctuation
	 *
	 */
	public final static String REGEX_EMAIL2 =  "^[\\p{Ll}\\d]{1,30}[\\-\\.]{0,1}[\\p{Ll}\\d]{1,30}[\\-\\.]{0,1}[\\p{Ll}\\d]{1,30}@[\\p{Ll}\\d]{1,30}\\-{0,1}[\\p{Ll}\\d]{1,30}\\.[a-z0-9]{2,6}$";

	/**
	 * REGEX_STUPID_WHITSPACES
	 *
	 * - in replaceAll remove multiple spaces placed in succession "a b" will become
	 * "ab" - in matches say yes if String contains 2 consecutive spaces
	 */
	public final static String REGEX_STUPID_WHITSPACES = " {2,}";
	/**
	 * REGEX_STUPID_MINUS - In replaceAll will replace multiple - placed in
	 * succession "r---------g" will become "rg" - in matches will say yes if the
	 * String contains 2 consecutive -
	 */
	public final static String REGEX_STUPID_MINUS = "-{2,}";
}
