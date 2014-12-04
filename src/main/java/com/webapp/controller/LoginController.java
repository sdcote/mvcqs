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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.controller.login.LoginResult;
import com.webapp.controller.login.ResultCode;
import com.webapp.dao.WebAppDataStore;

import coyote.commons.security.Context;

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

	private static final Log LOG = LogFactory.getLog(LoginController.class);
	private static final Log SECURITY_LOG = LogFactory.getLog("SecurityEvent");

	protected ResourceBundleMessageSource messageSource;

	private WebAppDataStore dataStore = null;

	private Context securityContext = null;




	@Autowired
	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}




	@Autowired
	public void setContext(Context context) {
		securityContext = context;
		LOG.info("Security Context wired");
	}




	/**
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping(value = { "/login" }, method = { RequestMethod.GET, RequestMethod.HEAD })
	public String doLogin(Model m) {

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
	LoginResult login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		LOG.info("Received a login request!");

		LoginResult result = new LoginResult();

		result.setResultCode(authenticate(request, session));

		if (ResultCode.SUCCESS == result.getResultCode()) {
			// do all the things!
		} else {
			SECURITY_LOG.info("Login Failure from " + request.getRemoteAddr());
		}

		return result;

	}




	private ResultCode authenticate(HttpServletRequest request, HttpSession session) {
		ResultCode retval = ResultCode.SUCCESS;

		StringBuffer b = new StringBuffer("Request Parameters:\r\n");
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			b.append("Name: ");
			b.append(name);
			b.append(" Value: '");
			b.append(request.getParameter(name));
			b.append("'\r\n");
		}
		LOG.info(b.toString());
		return retval;
	}

}
