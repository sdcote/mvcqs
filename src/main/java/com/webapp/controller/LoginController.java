/*
 * Copyright (c) 2014 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 *   Stephan D. Cote 
 *      - Initial concept and initial implementation
 */
package com.webapp.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.controller.login.LoginResult;
import com.webapp.controller.login.ResultCode;
import com.webapp.desc.WebApp;

import coyote.commons.StringUtil;
import coyote.commons.security.CredentialSet;
import coyote.commons.security.Login;
import coyote.commons.security.Session;


/**
 * This is the login controller for the basic authentication of logins in the 
 * system.
 * 
 * <p>The login endpoint behaved differently based on the method of the 
 * request. If a GET or HEAD, it is assumed to come from a users request to 
 * login and retrieve the view (form) which will gather their credentials. When
 * the user submits the form, JavaScript in the form makes an AJAX call to this 
 * endpoint using the POST method. The credentials are then authenticated and 
 * the results sent back as a JSON object for processing by the JavaScript to 
 * either present as an error, or to redirect the browser to the page 
 * appropriate for the logged-in user. This initial landing page can be set in 
 * the user settings or as part of the request.</p>
 */
@Controller
public class LoginController {

  public static final String ACCOUNT_PARAM_KEY = "account";
  public static final String PASSWORD_PARAM_KEY = "password";
  public static final String REMEMBER_PARAM_KEY = "remember";

  private static final Log LOG = LogFactory.getLog( LoginController.class );
  private static final Log SECURITY_LOG = LogFactory.getLog( "SecurityEvent" );

  private WebApp webapp = null;




  /**
   * Have Spring wire-up the WebApp system description object which contains 
   * all our application specific facilities, constants and data.
   * 
   * @param sysDesc the WebApp object which describes our system.
   */
  @Autowired
  public void setWebApp( WebApp sysDesc ) {
    webapp = sysDesc;
  }




  /**
   * 
   * @param m
   * @return
   */
  @RequestMapping(value = { "/login" }, method = { RequestMethod.GET, RequestMethod.HEAD })
  public String doLogin( Model m ) {

    // prepare the system for an eventual request...

    // Generate a model so the form is generated with the appropriate data

    // send the prepared Model and View

    return "login"; // TODO: for now this is all we do
  }




  /**
   * Handles the Login Form Submission. This is assumed to be executed via an 
   * AJAX request and returns an appropriate view.
   * 
   * <p>The above method generates a form and sends it to the browser. The 
   * JavaScript code in the login form posts credentials to this method via 
   * AJAX calls with the results of these calls being sent back to the login 
   * page for presentation processing.</p?
   * 
   * @param login The LoginCommand containing the form data.
   * 
   * @throws Exception
   */
  @RequestMapping(value = { "/login" }, method = RequestMethod.POST)
  public @ResponseBody
  LoginResult login( HttpServletRequest request, HttpServletResponse response, HttpSession session ) throws Exception {
    LoginResult result = new LoginResult();

    result.setResultCode( authenticate( request, session ) );

    if ( ResultCode.SUCCESS == result.getResultCode() ) {
      // do all the things!
      String remember = request.getParameter( ACCOUNT_PARAM_KEY );
      if ( StringUtil.isNotBlank( remember ) ) {
        Login login = (Login)session.getAttribute( WebApp.SESSION_LOGIN_KEY );

        Session loginSession = webapp.getSecurityContext().createSession( login );
        LOG.trace( "Remembering " + login + " with sessionId " + loginSession.getId() );

        Cookie loginCookie = new Cookie( WebApp.COOKIE_SESSION_KEY, loginSession.getId() );
        loginCookie.setMaxAge( 30 * 60 ); // TODO make this configurable
        response.addCookie( loginCookie );
        
        // TODO Now we need to persist the session 
      }

      // Now set the redirect to the appropriate location. This could be
      // the user's home page, profile page or a generic landing page set
      // in the configuration. The login.js will handle the result code
      // and the redirection for us.
      result.setRedirect( request.getContextPath() + "/" ); // home for now

    } else {
      SECURITY_LOG.info( "Login Failure from " + request.getRemoteAddr() );
    }

    return result;

  }




  /**
   * This method authenticates the credentials pass as part of the request.
   * 
   * <p>On successful authentication, a login-object is added to the 
   * container's session for this request. THis login can be retrieved by 
   * other components to retrieve identity and permissions related to the 
   * requester.</p> 
   * 
   * @param request the request from the browser
   * @param session the session from the container
   * 
   * @return the results of authentication.
   */
  private ResultCode authenticate( HttpServletRequest request, HttpSession session ) {
    ResultCode retval = ResultCode.UNVERIFIED;

    // look for the credentials
    String account = request.getParameter( ACCOUNT_PARAM_KEY );
    String passwd = request.getParameter( PASSWORD_PARAM_KEY );

    // Debugging information
    if ( StringUtil.isBlank( account ) ) {
      LOG.warn( "Request did not contain " + ACCOUNT_PARAM_KEY + " parameter" );
    }
    if ( StringUtil.isBlank( passwd ) ) {
      LOG.warn( "Request did not contain " + PASSWORD_PARAM_KEY + " parameter" );
    }

    // authenticate the credentials through the security context
    Login login = webapp.getSecurityContext().getLogin( new CredentialSet( account, passwd ) );

    // If we retrieved a login, then the credentials are authentic
    if ( login != null ) {
      retval = ResultCode.SUCCESS;
      session.setAttribute( WebApp.SESSION_LOGIN_KEY, login );
      LOG.info( "Successful login: " + login.toString() );
    }

    return retval;
  }

}
