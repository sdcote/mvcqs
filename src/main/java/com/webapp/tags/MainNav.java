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
package com.webapp.tags;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.webapp.desc.WebApp;

import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuSection;
import coyote.commons.security.Login;

/**
 * This is the tag used in our pages to generate the main navigation menu.
 */
public class MainNav extends SimpleTagSupport {

	private static final Log LOG = LogFactory.getLog(MainNav.class);

	private static WebApp webapp = null;




	/**
	 * Retrieve the instance of the WebApp which is configured with components, 
	 * as opposed to using static members.
	 * 
	 * <p>This will have the currently set list of features and menu locations 
	 * for this application instance.</p>
	 * 
	 * @return The currently configured system description with all the features specified.
	 */
	private WebApp getSystemDescription() {

		if (webapp == null) {
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(((PageContext) getJspContext()).getServletContext());

			if (applicationContext != null) {
				String[] names = applicationContext.getBeanDefinitionNames();
				if (names.length > 0) {
					for (int x = 0; x < names.length; x++) {
						LOG.trace("BEAN: " + names[x]);
					}
				} else {
					LOG.error("There are NO BEAN NAMES!");
				}
			} else {
				LOG.error("There is no application context available to the custom tags!");
			}

			if (applicationContext != null && applicationContext.containsBean(WebApp.SYSTEM_DESCRIPTION)) {
				webapp = (WebApp) applicationContext.getBean(WebApp.SYSTEM_DESCRIPTION);
			}
			if (webapp == null) {
				LOG.warn("Could not get system description...creating one");
				webapp = new WebApp();
			}
		}

		return webapp;
	}




	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();

		HttpServletRequest request = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
		String contextPath = request.getContextPath();
		Locale locale = request.getLocale();

		// Retrieve the login for this request
		Login login = WebApp.getLogin(request);

		StringBuffer content = new StringBuffer();
		content.append("<nav class=\"navbar navbar-default navbar-static-top\" role=\"navigation\" style=\"margin-bottom: 0\">\r\n");

		content.append(navHeader(contextPath, locale, login));
		content.append(navTopLinks(contextPath, locale, login));
		content.append(navSidebar(contextPath, locale, login));

		content.append("</nav>\r\n");

		out.println(content.toString());
	}




	/**
	 * Generates the top and left-most portion of the navigation header.
	 * @param contextPath 
	 * @param locale 
	 * @param login 
	 * 
	 * @return the portion of the navigation containing the web application home link.
	 */
	private Object navHeader(String contextPath, Locale locale, Login login) {
		StringBuffer b = new StringBuffer();
		b.append("\t\t\t<div class=\"navbar-header\">\r\n");
		b.append("\t\t\t\t<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\r\n");
		b.append("\t\t\t\t\t<span class=\"sr-only\">Toggle navigation</span>\r\n");
		b.append("\t\t\t\t\t<span class=\"icon-bar\"></span>\r\n");
		b.append("\t\t\t\t\t<span class=\"icon-bar\"></span>\r\n");
		b.append("\t\t\t\t\t<span class=\"icon-bar\"></span>\r\n");
		b.append("\t\t\t\t</button>\r\n");
		b.append("\t\t\t\t<a class=\"navbar-brand\" href=\"");
		b.append(contextPath);
		b.append(getSystemDescription().getLink());
		b.append("\">");
		b.append(getSystemDescription().getDisplayName(locale));
		b.append("</a>\r\n");
		b.append("\t\t\t</div>\r\n");
		return b.toString();
	}




	/**
	 * @param contextPath  
	 * @param locale 
	 * @param login 
	 * @return
	 */
	private Object navTopLinks(String contextPath, Locale locale, Login login) {
		StringBuffer b = new StringBuffer();
		b.append("\t<ul class=\"nav navbar-top-links navbar-right\">\r\n");

		// Menu Items across the top goes here

		// We always have a user section at the far right side of the top menu
		b.append("\t<li class=\"dropdown\">\r\n");
		b.append("\t\t<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\r\n");

		if (login == null) {
			// No login, we display a different user icon...
			b.append("\t\t<i class=\"fa fa-question fa-fw\"></i><i class=\"fa fa-user fa-fw\"></i>  <i class=\"fa fa-caret-down\"></i></a>\r\n");
			b.append("\t\t<ul class=\"dropdown-menu dropdown-user\">\r\n");

			// ... and just show the sign-in option
			String signinName = System.getProperty(WebApp.SIGNIN_PROPERTY);
			if (signinName != null) {
				Feature signInFeature = getSystemDescription().getFeature(signinName);
				if (signInFeature != null) {
					b.append("\t\t\t\t\t<li><a href=\"");
					b.append(contextPath);
					b.append(signInFeature.getLink());
					b.append("\"><i class=\"fa fa-sign-in fa-fw\"></i> ");
					b.append(signInFeature.getDisplayName(locale));
					b.append("</a></li>\r\n");
				} else {
					LOG.error("No feature named '" + signinName + "' defined in system description");
				}
			} else {
				LOG.error("There is no sign-in feature specified in system property '" + WebApp.SIGNIN_PROPERTY + "'");
			}
		} else {
			// since we have a login, we provide a different menu to the user
			b.append("\t\t</i><i class=\"fa fa-user fa-fw\"></i>  <i class=\"fa fa-caret-down\"></i></a>\r\n");
			b.append("\t\t<ul class=\"dropdown-menu dropdown-user\">\r\n");

			// Lookup User Profile Feature
			b.append("\t\t\t\t\t<li><a href=\"#\"><i class=\"fa fa-user fa-fw\"></i> User Profile</a></li>\r\n");

			// Lookup User Settings Feature
			b.append("\t\t\t\t\t<li><a href=\"#\"><i class=\"fa fa-gear fa-fw\"></i> Settings</a></li>\r\n");

			// Lookup the Sign-Out feature
			b.append("\t\t\t\t\t<li class=\"divider\"></li>\r\n");
			b.append("\t\t\t\t\t<li><a href=\"logout\"><i class=\"fa fa-sign-out fa-fw\"></i> Logout</a></li>\r\n");
		}

		b.append("\t\t\t\t</ul>\r\n");
		b.append("\t\t\t\t<!-- /.dropdown-user -->\r\n");

		b.append("\t\t\t</li>\r\n");
		b.append("\t\t\t<!-- /.dropdown -->\r\n");
		b.append("\t\t</ul>\r\n");
		b.append("\t\t<!-- /.navbar-top-links -->\r\n");

		return b.toString();
	}




	/**
	 * @param contextPath 
	 * @param locale 
	 * @param session 
	 * @return
	 */
	private Object navSidebar(String contextPath, Locale locale, Login login) {
		StringBuffer b = new StringBuffer();
		b.append("\t<div class=\"navbar-default sidebar\" role=\"navigation\">\r\n");
		b.append("\t\t<div class=\"sidebar-nav navbar-collapse\">\r\n");
		b.append("\t\t\t<ul class=\"nav\" id=\"side-menu\">\r\n");

		// each line item is a menu option

		List<Feature> features = getSystemDescription().getFeaturesBySection(MenuSection.LEFT);
		for (Feature feature : features) {

			// TODO: Is the login allowed to see the feature?

			b.append("\t\t\t\t<li><a href=\"");
			b.append(contextPath);
			b.append(feature.getLink());
			b.append("\">");
			b.append(feature.getDisplayName(locale));
			b.append("</a></li>\r\n");

			// TODO: Handle Sub menu items by looking at the children of each
			// feature

		}

		b.append("\t\t\t</ul>\r\n");
		b.append("\t\t</div>\r\n");
		b.append("\t\t<!-- /.sidebar-collapse -->\r\n");
		b.append("\t</div>\r\n");
		b.append("\t<!-- /.navbar-static-side -->\r\n");
		return b.toString();
	}

}
