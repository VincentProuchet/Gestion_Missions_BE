package diginamic.gdm.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import diginamic.gdm.GDMRoutes;

public class GDMErrorController implements ErrorController {


	  @RequestMapping( "/"+GDMRoutes.ERRORS)
	  @ResponseBody
	  String error(HttpServletRequest request) {
	    return ""
	    		+ "<h1>Error occurred</h1>"
	    		+ "une erreur s'est produite"
	    		
	    		;
	  }

	  
	  public String getErrorPath() {
	    return "/"+GDMRoutes.ERRORS;
	  }
	
}