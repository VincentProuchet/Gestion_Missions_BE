package diginamic.gdm.security;

import java.io.IOException;
import java.net.CookieStore;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import diginamic.gdm.GDMRoutes;
import diginamic.gdm.GDMVars;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;

/**
 * Security Configuration this is where we declare
 * 
 * @Beans related to security and how the spring security will accept or reject
 *        request
 * 
 * @author Vincent
 *
 */
/**
 * @author Vincent
 *
 */
/**
 * @author Vincent
 *
 */
@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class GDMSecurityConfig {
		
	@Autowired
	private GDMAuthentication authProvider;

	@Autowired
	CollaboratorService userService;	

	/**
	 * configuration spring security
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain springSecurityfilterChain(HttpSecurity http) throws Exception {
		
		http
		.csrf().disable()// apparemment il faut au moins ça 
		//si on se connecte depuis autre chose que le login tout fait de spring security
		.authorizeRequests()
		// this one was for testing before creation of mock data
		//.antMatchers("/"+GDMRoutes.SIGNUP+"/**").permitAll()		
		.antMatchers(HttpMethod.GET, GDMRoutes.FAVICON).permitAll()
		.antMatchers(HttpMethod.POST, GDMRoutes.LOGIN).permitAll()
		// all other request need authentication
		.anyRequest().authenticated()
		;
		http.authenticationProvider(authProvider) 
		// here you say you use a form for login 
		.formLogin()
		// here you can provide your homemade login form
		//.loginPage(GDMVars.LOGINPAGE)
		// here you can tell it to use a custom made login page
		//.loginProcessingUrl(GDMRoutes.LOGIN)
		.successHandler(successHandler())
		.failureHandler(failureHandler())
		.permitAll()		
		;
		http.rememberMe().key("RememberMe").tokenValiditySeconds(GDMVars.TOKEN_LIFE);
		http.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		.maximumSessions(1)
		// in case of a new login, the existing session is closed
		.expiredSessionStrategy(event -> event.getSessionInformation().expireNow())
		;
		http.logout()
		.addLogoutHandler(logoutHandler())
		.clearAuthentication(true)
		.invalidateHttpSession(true)
		.deleteCookies(GDMVars.SESSION_SESSION_COOKIE_NAME)
		;
		http.httpBasic();		
		return http.build();
	}

	/**
	 * this is where we configure the method and path that can be accessed from
	 * outside the first commit will open everything its head is Security OpenAll
	 * 
	 * @return
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		System.err.println("webignoring");
		return (web) -> web.ignoring()
				// il FAUT mettre le slash avant
		//.antMatchers("/"+GDMRoutes.SIGNUP)
		//.antMatchers(HttpMethod.GET)
	//			.antMatchers(HttpMethod.POST,"/"+GDMRoutes.LOGIN)
//		.antMatchers(HttpMethod.PUT)
//		.antMatchers(HttpMethod.DELETE)
	
		;
	}
	/**
	 * authentication success handling
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new AuthenticationSuccessHandler() {
	        @Override
	        public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
	                HttpServletResponse httpServletResponse, Authentication authentication)
	                throws IOException, ServletException{
	            httpServletResponse.getWriter().append("OK");
	            httpServletResponse.setStatus(200);
	        }
	    };
	}

	/**
	 * bean for login failure handling
	 * @return
	 */
	@Bean
	public AuthenticationFailureHandler failureHandler() {
	    return new AuthenticationFailureHandler() {
	        @Override
	        public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
	                HttpServletResponse httpServletResponse, AuthenticationException e)
	                throws IOException, ServletException {
	            httpServletResponse.getWriter().append("Authentication failure");
	            httpServletResponse.setStatus(401);
	        }
	    };
	}
	
	public LogoutHandler logoutHandler() {
		return new LogoutHandler() {
			
			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
				System.err.println("Login out");
				Cookie[] cookies  =  request.getCookies();
				for (Cookie cookie : cookies) {
					if(cookie.getName()==GDMVars.SESSION_SESSION_COOKIE_NAME) {
						System.out.println("cookie !!!!!!");
					}
				}
				
				try {
					response.getWriter().append("Logged Out");
					response.setStatus(200);
					SecurityContextHolder.clearContext();
					System.err.println(" logOut Success");
				} catch (IOException e) {
					System.err.println(" logout Fail");
					response.setStatus(401);
				}
			}
		};
		
	}

	
//	@Bean
	/**Made to circumvent the CORS trouble we got when trying to connect backend to frontEnd 
	 * @return
	 */
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/login").allowedOrigins("http://localhost:4200");
//			}
//		};
//	}



	
	
}
