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

	/**
	 * REGEX for humans last names
	 * 
	 * Note that we accept digit for testing purpose
	 * 
	 * - in mathes will say yes if straing contains letters and number or a '-' and
	 * has 5 to 64 char
	 */
	public final static String REGEX_USERNAMES = "^[\\p{L}\\p{N}\\-]{5,64}$";
	/**
	 * REGEX for humans last names
	 * 
	 * Note that we accept digit for testing purpose
	 * 
	 * - in replaceall will replace anything that is a letter/number or a '-' - in
	 * mathes will say no if found anything that is not a letter/number or a '-' but
	 * the name can be lastname | last-name | last--name
	 */
	public final static String REGEX_HUMANS_LAST_NAMES = "^[\\p{L}\\p{N}]{2,64}$"
									+"|^[\\p{L}\\p{N}]{2,30}\\-{0,2}[\\p{L}\\p{N}]{2,30}]$"
									
	;
	/**
	 * REGEX for humans last names
	 * 
	 * Note that we accept digit for testing purpose
	 * 
	 * - in replaceall will replace anything that is a letter/number - in mathes
	 * will say no if found anything that is not a letter/number - but the name can
	 * be firstname
	 */
	public final static String REGEX_HUMANS_FIRST_NAMES = "^[\\p{L}\\p{N}]{2,64}$";
	/**
	 * REGEX
	 * 
	 * - in replaceall will replace anything that is not a letter/number whithespace
	 * or a '-' and @ - in mathes will say yes if found anything that is not a
	 * letter/number whithespace or a '-' and @
	 * 
	 * this one is the simpliest but won't like specific some non latin UTF-8 char
	 */
	public final static String REGEX_EMAIL = "^[\\w\\.\\-]{1,64}@[\\w\\-.]{1,64}\\.[\\w]{2,6}$";
	/**
	 * REGEX
	 * 
	 * pattern for e-mail validation ex:
	 * azertyuiopqsdfg987013hjklmwxcvbn-qefqfef465@azertyuiopqsdf465klmwxcvbn-q464fqf.sfdgfd
	 * 
	 */
	public final static String REGEX_EMAIL2 =  "^[\\p{Ll}\\d]{1,30}[\\-\\.]{0,1}[\\p{Ll}\\d]{1,30}@[\\p{Ll}\\d]{1,30}\\-{0,1}[\\p{Ll}\\d]{1,30}\\.[a-z0-9]{2,6}$";

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
