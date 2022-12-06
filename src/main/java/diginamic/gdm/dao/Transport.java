package diginamic.gdm.dao;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import diginamic.gdm.Enums.Locales;
import lombok.Getter;

/**
 * Represents the mean of transport for the mission
 * They are suposed to be referenced as ordinal so please
 * If you need to add more
 * ad them at the end
 *
 * @author Joseph
 *	@author Vincent please don't mind the entities annotations
 *	that was a prevention in case this one gets promoted from enum to class
 */
@Getter
public enum Transport {
	/** Car */
	Car(0, "voiture"),
	/** Carshare */
	Carshare(1,"co-voiturage"),
	/** Train */
	Train(2,"train"),
	/** Flight */
	Flight(3,"aérien")
	;
	@Id
	private int id;
	/** localizedLabel label */
	@Column(name= "label")
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
