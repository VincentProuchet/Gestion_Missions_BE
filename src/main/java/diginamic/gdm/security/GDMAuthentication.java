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

	/** CONTENT_TYPE_JSON */
	private static final String CONTENT_TYPE_JSON = "application/json";
	/** CONTENT_TYPE */
	@SuppressWarnings("unused")
	private static final String CONTENT_TYPE = "Content-Type";
	/** CHARACTER_ENCODING */
	private static final String CHARACTER_ENCODING = "UTF-8";

	/** collaboratorService */
	@Autowired
	private CollaboratorService collaboratorService;
	/** passwordEncoder */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Authentication method
	 * this is here that you can catch the authentication and
	 * do the magic
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// if a security context already exist
		if (SecurityContextHolder.getContext().getAuthentication() != null) {

			return SecurityContextHolder.getContext().getAuthentication();
		}
		if (auth ==null) {
			throw new BadCredentialsException("le contexte  d'authentification est null la session est peut-être terminée");
		}
		String username = (String) auth.getPrincipal();
		if (username == null) {
			throw new BadCredentialsException(" Username can't be null ");
		}
		Collaborator coll = (Collaborator) collaboratorService.loadUserByUsername(username);
		if (coll == null) {
			throw new BadCredentialsException("les informations de compte sont incorrectes");
		}
		if (!coll.isActive()) {
			throw new BadCredentialsException("le compte n'est pas activé");
		}
		String password = (String) auth.getCredentials();
		if (passwordEncoder.matches(password, coll.getPassword())) {
			UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
				coll.getUsername(), coll.getPassword(), coll.getAuthorities());
			return userToken;
		}
		throw new BadCredentialsException("les informations de compte sont incorrectes");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	/**
	 *handler for authentication failure
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setContentType(CONTENT_TYPE_JSON);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setStatus(401);
	}

	/**
	 * handler for authentication success
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String outprint = "";
		// we have to set respons parameters
		response.setContentType(CONTENT_TYPE_JSON);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		try {
			// we get connected user an make a Data transfert Object of it
			CollaboratorDTO collab = new CollaboratorDTO(collaboratorService.getConnectedUser());
			// an place it in Json form in the outprint
			outprint = new ObjectMapper().writeValueAsString(collab);// this can create a json formated string of any object

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
