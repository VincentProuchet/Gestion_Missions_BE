package diginamic.gdm.Enums;

import javax.persistence.Column;
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
	
	CAR(0, "voiture"),
	CARSHARE(1,"co-voiturage")
	
	;
	@Id	
	private int id;
	
	/** localizedLabel map of label can be localized */
	@Column
	private  String localizedLabel;
	
	@Transient
	public static Locales activeLocale = Locales.fr;
	
	/** Constructeur
	 * @param id
	 * @param locale
	 * @param label
	 */
	private Transport(int id,String label) {
		this.id = id;
		this.localizedLabel = label;
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
		return this.localizedLabel;
	}

	
	
	
}
