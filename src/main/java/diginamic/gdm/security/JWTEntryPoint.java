package diginamic.gdm.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *  Sert au debug pour remonter les erreur
 *  pas sûr
 * @author Vincent
 *
 */
@Component
public class JWTEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		System.out.println("In entry point");
		authException.printStackTrace();
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
	}

}
