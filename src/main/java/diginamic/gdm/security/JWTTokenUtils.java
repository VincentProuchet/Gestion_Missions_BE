package diginamic.gdm.security;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import diginamic.gdm.dao.Collaborator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTTokenUtils {
	
	private static String SIGN_KEY ="fyulkfhngfhesdjns";
	
	public String generateToken(Collaborator employee) {
		Map<String,Object> claims = new HashMap<>();
		claims.put("firstName",employee.getFirstName() );
		claims.put("lastName",employee.getLastName() );
		claims.put("roles",employee.getRole() );
		claims.put("authority",employee.getAuthorities());
		claims.put("userName",employee.getUsername() );
		
		LocalDateTime now = LocalDateTime.now();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(now.toString()));
		calendar.add(Calendar.HOUR, 2);
		
		String token =
		Jwts.builder()
		.setClaims(claims)
		.setSubject(employee.getUsername())
		.setIssuer("GESTION DES MISSIONS")
		.setIssuedAt(new Date(now.toString()))
		.setExpiration(calendar.getTime())
		.signWith(SignatureAlgorithm.HS256, SIGN_KEY)
		.compact()
		;
		log.info("TOKEN{}",token);
		return  token ;
	}
	
	public String getUserNameFromToken(String token) {
		Claims claims = 
		getClaims(token);
		return claims.getSubject();
		
	}

	private Claims getClaims(String token) {
			
		return Jwts.parser()
		.setSigningKey(SIGN_KEY)
		.parseClaimsJws(token)
		.getBody();
		
		
	}
	
	/** Récupère les information d'un Token 
	 * 	effectue les comparaison et valide ou nom l'intégrité des données
	 * @param token
	 * @param employee
	 * @return
	 */
	public boolean isTokenValid(String token , UserDetails employee) {
		Claims claims = getClaims(token);
		Date expiration = claims.getExpiration();
		String UserNameFromToken = getUserNameFromToken(token);
		Boolean isValidUSerName = UserNameFromToken.equals(employee.getUsername());
		Boolean isValidDate = expiration.after(new Date());
		
		return isValidDate && isValidUSerName;
		
	}
}
