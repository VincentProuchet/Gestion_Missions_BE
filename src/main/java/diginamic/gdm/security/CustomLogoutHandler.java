package diginamic.gdm.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.exceptions.BadRequestException;
import diginamic.gdm.services.CollaboratorService;
import diginamic.gdm.vars.GDMVars;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

	/** collaboratorService */
	@Autowired
	private CollaboratorService collaboratorService;

	/**
	 * logout handling will set session's cookies life's to 0
	 *
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Cookie[] cookies = request.getCookies();
		// We search for springs security session cookie
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName() == GDMVars.SESSION_SESSION_COOKIE_NAME) {
					// and set its life to 0
					cookie.setMaxAge(0);
				}
			}
		}
	}

	/**
	 * will be called if authentication doesn't throw any error
	 *
	 * it form a correct http response
	 *
	 *
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		Collaborator collaborator = new Collaborator();
		String principal = "";

		try {
			principal = (String) authentication.getPrincipal();
			// we get the connected collaborator
			collaborator = this.collaboratorService.findByUserName(principal);
			// we clear te security context
			SecurityContextHolder.clearContext();
			// if everything goes smoothly we set an header

			response.addHeader(" user logged out ", collaborator.getFirstName());
			// and a status
			response.setStatus(200);

		} catch (NullPointerException e) { // string conversion error

			response.addHeader(" Nullpointer error ", "  string convertion ");
			response.setStatus(418);

			throw new ServletException(e.getMessage());

		} catch (BadRequestException e) { // find by username errors
			response.addHeader(" user not found ", "  there is no user ");
			response.setStatus(418);

			throw new ServletException(e.getMessage());

		}catch (Exception e) { // everything else
			// if not
			response.addHeader(" we don't know ", " you will have to figure out this yourself ");
			// i'm a teapot
			response.setStatus(418);

			throw new ServletException(e.getMessage());

		}

	}

}
