package diginamic.gdm.dao;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy;

/**
 * @author Vincent
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplicationParams {
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = 0;
	/** Description */
	@Column(nullable = false, unique = true)	
	private String name = "" ;	
	// values 
	
		/** text */
		private String text= "";
		/** value integer */
		private Integer value =0;		
		/** value float */
		private float valuef = 0f;
		/** boolean */
		private boolean valueb = false;

}
