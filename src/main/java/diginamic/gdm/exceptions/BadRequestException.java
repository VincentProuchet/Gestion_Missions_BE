package diginamic.gdm.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * handler for error in the program
 * these are supposed to get back to the endUser
 * @author Vincent
 *
 */
@Getter
@Setter
public class BadRequestException extends Exception {

    /** serialVersionUID */
	private static final long serialVersionUID = 4968915439221191862L;
	private String code;

    /**
     *  Constructeur
     *  désolé joseph j'ai dût interchanger le message et le code erreur
     *  pour des raisons pratiques
     *  
     * @param message
     * @param code
     */
    public BadRequestException(String code,String message){
        super(message);
        this.code = code;
    }
    /** Constructeur
     * made to simplify error message construction
     * and avoid having stringbuilder and append everywhere
     * mind that the message constructor doesn't take care of spaces, and the rest
     * @param code
     * @param message these are string that you can pass indefinitely
     */
    public BadRequestException(String code,String... message){
    	super(constructMessage(message));
    	this.code = code;
    } 
    
    
    /**
     * this will construct the error message from string given in argument 
     * @param message
     * @return
     */
    private static String constructMessage(String [] message) {    	
    	StringBuilder texte = new StringBuilder();
    	for (String string : message) {
			texte.append(string);
		}
    	return texte.toString();
    }
}
