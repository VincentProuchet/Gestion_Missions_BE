package diginamic.gdm.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dto.CollaboratorDTO;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * a custom authentication provider for injection in GDMSecurity and its
 * filterChain
 * 
 * @author Vincent
 *
 */
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class GDMAuthentication
		implements AuthenticationProvider, AuthenticationSuccessHandler, AuthenticationFailureHandler {

	@Autowired
	private CollaboratorService collaboratorService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Authentication method this is here that you can catch the authentication and
	 * do the magic
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// if a security context already exist
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
//			System.out.println("allready authenticated");
			return SecurityContextHolder.getContext().getAuthentication();
		}
		String username = (String) auth.getPrincipal();
		if (username == null) {
			throw new BadCredentialsException(" Username can't be null ");
		}
		Collaborator coll = (Collaborator) collaboratorService.loadUserByUsername(username);
		if (coll != null) {
			if (!coll.isActive()) {
				throw new BadCredentialsException("le compte n'est pas activé");
			}
			String password = (String) auth.getCredentials();
			if (passwordEncoder.matches(password, coll.getPassword())) {
				UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
						coll.getUsername(), null, coll.getAuthorities());
				return userToken;
			}
		}
		throw new BadCredentialsException("les informations de compte sont incorrectes");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	/**
	 *
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setContentType("application/text");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Authentication failure", " ");
		response.getWriter().append("Authentication failure");
		response.setStatus(401);
	}

	/**
	 *
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String outprint = "";
		// we have to set respons parameters
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			// we get connected user an make a Data transfert Object of it
			CollaboratorDTO collab = new CollaboratorDTO(collaboratorService.getConnectedUser());
			// an place it in Json form in the outprint
			outprint = new ObjectMapper().writeValueAsString(collab);// this create a json formated string of any object

		} catch (Exception e) {
			// we add an header , responses MUST have an header
			response.addHeader(" user not found ", "  there is no user ");
			outprint = " ";
			// setting status response
			response.setStatus(500);
		}
		// we add an header , responses MUST have an header
		// any response without header is considerer as an error by angular
		response.addHeader("user", outprint);
		// we put things in the body
		// this can only be made once
		// only one body
		response.getWriter().append(outprint);
		// and set status to accepted
		response.setStatus(200);
	}

}
