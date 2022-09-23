package diginamic.gdm;

/**
 * Classe utilitaire qui n'a d'autre but que de centraliser toutes les variables
 * Globales du Back-END vous avez besoins d'un variable int pour la durée de vos
 * cookies, mettez la ici
 * 
 * @author Vincent
 *
 */
public abstract class GDMVars {

	/** Durée de vie des tokens */
	public final static int TOKEN_LIFE = 7200; // ici 2H
	public final static long SHEDULED_INTERVAL =  7200000L;
	public final static String SESSION_SESSION_COOKIE_NAME ="JSESSIONID";
	public final static String LOGINPAGE = "/login.html";
	public final static int MAX_AUTHORIZED_SESSION = 1;
}
