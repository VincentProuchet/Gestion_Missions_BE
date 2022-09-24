package diginamic.gdm.Enums;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Getter;


/**
 * 
 * For types of transport
 * created for possible future improvement
 * they are supposed to get referenced as ordinal   
 * so ...if you need to add more
 * do it at the very end of enums
 * @author Vincent
 *
 */
@Getter
public enum Transport {
	
	CAR(0,Locales.fr, "voiture"),
	CARSHARE(1, Locales.fr,"co-voiturage")
	
	;
	@Id	
	private int id;
	
	/** localizedLabel map of label can be localized */
	@Column
	private Map<Locales, String> localizedLabel;
	
	@Transient
	public static Locales activeLocale = Locales.fr;
	
	/** Constructeur
	 * @param id
	 * @param locale
	 * @param label
	 */
	private Transport(int id,Locales locale ,String label) {
		this.id = id;
		this.localizedLabel.put(locale, label);
	}
	/** Constructeur
	 * 
	 */
	private Transport() {
	}
	
	/**
	 * return the localized label 
	 * depending of the active locale of the TransportEnum
	 * @return the label
	 */
	public String getLabel() {		
		return this.localizedLabel.get(activeLocale);
	}
	/**
	 * pour ajouter des labels à vos 
	 * enums
	 * ATTENTION ecraseras toutee valeur se trouvant déjà
	 * dans la valeur locale
	 * @param locale
	 * @param label
	 */
	public void addLabel(Locales locale, String label) {
		this.localizedLabel.put(locale, label);
	}
	
	
	
}
