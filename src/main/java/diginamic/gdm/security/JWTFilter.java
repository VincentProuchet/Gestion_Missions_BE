package diginamic.gdm.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.services.CollaboratorService;

public class JWTFilter extends OncePerRequestFilter{
	
	private static String TOKEN_STARTWITH = "Bearer " ;
	private CollaboratorService userService;
	private JWTTokenUtils jwtTokenUtils;
	private String autorization;

	public JWTFilter(CollaboratorService accountService, JWTTokenUtils jwtTokenUtils, String autorization) {
		super();
		this.userService = accountService;
		this.jwtTokenUtils = jwtTokenUtils;
		this.autorization = autorization;
	}




	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String header = request.getHeader(autorization);
		if(header!=null && header.startsWith(TOKEN_STARTWITH)) {
			System.err.println("do filter");
			String token =  header.substring(7);
			System.out.println(token);
			String userName = this.jwtTokenUtils.getUserNameFromToken(token);
			System.out.println("username" + userName);
			if(userName!=null) {
				// its here that we fetch the user from database
				UserDetails user = this.userService.loadUserByUsername(userName);
				// on vérifie la validité du tokens
				if(jwtTokenUtils.isTokenValid(token,user)) {
					System.out.println("token Valid");
					UsernamePasswordAuthenticationToken  authenticationToken =  
					// on le vérifie en contrôlant les autorisation de l'utilisateur
							new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
				
			}
			
		}
		filterChain.doFilter(request, response);
	}
	
}
