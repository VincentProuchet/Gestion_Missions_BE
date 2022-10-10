package diginamic.gdm.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import diginamic.gdm.vars.GDMRoutes;

/**
 * used has a default error controller
 * you can change it for customized error 
 * @author Vincent
 *
 */
public class GDMErrorController implements ErrorController {


	  @RequestMapping( "/"+GDMRoutes.ERRORS)
	  @ResponseBody
	  String error(HttpServletRequest request) {
	    return ""
	    		+ "<h1>Error occurred</h1>"
	    		+ "une erreur s'est produite"
	    		
	    		;
	  }

	  
	  /**
	 * @return
	 */
	public String getErrorPath() {
	    return "/"+GDMRoutes.ERRORS;
	  }
	
}
