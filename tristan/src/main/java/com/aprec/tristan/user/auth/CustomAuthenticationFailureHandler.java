package com.aprec.tristan.user.auth;

import com.aprec.tristan.user.registration.RegistrationServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    @Autowired
    private RegistrationServiceInterface registrationService;

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
        log.info("onAuthenticationFailure");
        log.info("exception : [message : " + exception.getMessage() + ", class: " + exception.getClass()+ "]");
        
        if (exception.getMessage().equalsIgnoreCase("disabled")) {
        	if (registrationService.checkPassword(request.getParameter("username"), request.getParameter("password"))) {
        		registrationService.resendConfirmationMail(request.getParameter("username"));
        	} else {
        		request.getSession().setAttribute("alert", "badcredentials");
        		response.sendRedirect("/login?alert=true");
        	}
        } else {
        	request.getSession().setAttribute("alert", exception.getMessage());
            response.sendRedirect("/login?alert=true");
        }

        //request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
        
	}


}
