package diginamic.gdm.dto;

import java.lang.reflect.Field;

public interface DTO<T> {
	
	/**
	 * this was an experiments to convert JAVA object to JSON
	 * that was before I found the new ObjectMapper().writeValueAsString(Object);
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	default String toJsonString(Object obj) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder json = new StringBuilder();
		if(obj==null) {
			return "null";
		}
		
		// On commence par récupérer la classe de l'objet passée en paramètre.
		// la classe fournit toutes les informations sur la structure d'un objet.
		Class<?> classe = obj.getClass();
		
		// Sur cette classe on récupère le tableau des variables d'instance
		Field[] fields = classe.getDeclaredFields();
		// On fait une boucle sur ce tableau
		
		json.append("{");
		for (Field field : fields) {
			if( field.getClass().isAssignableFrom(DTO.class)) {
				json.append(this.toJsonString(field));				
			}
			else {
				json.append("\"").append(field.getName()).append("\":").append(field.get(obj));
			}			
			json.append(",");
		}
		json.append("}");
	
		
		return json.toString();
	}
	
}
