package diginamic.gdm.vars;

/**
 * Classe utilitaire qui n'a d'autre but que de centraliser toutes les variables
 * Globales du Back-END 
 * vous avez besoins d'un variable int pour la durée de vos cookies ?
 * mettez la ici
 * 
 * @author Vincent
 *
 */
public abstract class GDMVars {

	/** Durée de vie des tokens */
	public final static int TOKEN_LIFE = 36000; // ici 10H
	/** SHEDULED_INTERVAL intervel pour les tache planifiées */
	public final static long SHEDULED_INTERVAL =  7200000L;
	/** SESSION_SESSION_COOKIE_NAME nom du cookies de session Spring */
	public final static String SESSION_SESSION_COOKIE_NAME ="JSESSIONID";
	/** LOGINPAGE  chemin ver la page login  dans le dossier ressources
	 * notez le fait que le dossier public est le dossier par défaut 
	 */
	public final static String LOGINPAGE = "/login.html";
	/** MAX_AUTHORIZED_SESSION */
	public final static int MAX_AUTHORIZED_SESSION = 1;
	/** temps minimal autorisé lorsque une  mission utilise des transport de type aérien
	 * 
	 */
	public final static int MIN_DAYS_BEFORE_FLIGHT_TRANSPORT = 7;
	/** FRONT_END_URL utilisé pour les réglage de CORS */
	public final static String FRONT_END_URL ="http://localhost:4200";
	/** BACK_END_URL  utilisé pour les réglage de CORS */
	public final static String BACK_END_URL ="http://localhost:8080";
	
	/** 
	 * REGEX for replace all
	 * will remove anything that is not a letter/number whithespace or a '-'   
	 */
	public final static String REGEX_NAMES  ="[^[a-zA-Z0-9- ]]"; 
}
