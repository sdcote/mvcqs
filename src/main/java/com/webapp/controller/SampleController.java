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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webapp.desc.WebApp;

/**
 * This is a sample controller 
 */
@Controller
public class SampleController {
	private static final Log LOG = LogFactory.getLog(SampleController.class);

	private WebApp webapp = null;




	/**
	 * Have Spring wire-up the WebApp system description object which contains 
	 * all our application specific facilities, constants and data.
	 * 
	 * @param sysDesc
	 */
	@Autowired
	public void setWebApp(WebApp sysDesc) {
		webapp = sysDesc;
	}




	/**
	 * This shows how to create a simple MVC endpoint.
	 * @param m
	 * @return
	 */
	@RequestMapping("home")
	public String loadHomePage(HttpServletRequest request, Model model) {

		// Get the title of the application in the request's locale
		model.addAttribute("title", webapp.getMessage("webapp.subtitle", null, request.getLocale()));

		return "home";
	}




	@RequestMapping("profile")
	public String loadProfilePage(HttpServletRequest request, Model model) {

		return "profile";
	}
}
