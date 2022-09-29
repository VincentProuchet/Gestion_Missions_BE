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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import diginamic.gdm.GDMVars;
import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

	@Autowired
	private CollaboratorService collaboratorService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.err.println("Login out");
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName() == GDMVars.SESSION_SESSION_COOKIE_NAME) {
					System.out.println("cookie !!!!!!");
				}
			}
			
		}
			

	}

	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String principal = (String) authentication.getPrincipal();
		Collaborator collaborator = new Collaborator();
		try {
			collaborator = this.collaboratorService.findByUserName(principal);
		} catch (Exception e) {
			response.addHeader(" user not found ", "  there is no user ");
		}

		SecurityContextHolder.clearContext();
		if(true) {
//		if (SecurityContextHolder.getContext() == null) {
			response.addHeader(" user logged out ", collaborator.getFirstName() );
			response.setStatus(200);
			System.out.println("The user " + collaborator.getFirstName() + " has logged out.");
			System.err.println(" logOut Success");
		}
//		else {
//			response.addHeader(" user is still logged in ", collaborator.getFirstName() );
//			response.getWriter().append("something went  wrong");
//			response.setStatus(500);
//			System.out.println("The user " + collaborator.getFirstName() + " his still here logged out.");
//			System.err.println(" logOut fail");
//
//		}

	}

}
