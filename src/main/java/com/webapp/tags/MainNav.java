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

import coyote.commons.feature.SystemDescription;


/**
 * This is the tag used in our pages to generate the main navigation menu.
 */
public class MainNav extends SimpleTagSupport {

  private static final Log LOG = LogFactory.getLog( MainNav.class );

  private static WebApp webapp = null;




  private WebApp getSystemDescription() {

    if ( webapp == null ) {
      WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext( ( (PageContext)getJspContext() ).getServletContext() );

      if ( applicationContext != null ) {
        String[] names = applicationContext.getBeanDefinitionNames();
        if ( names.length > 0 ) {
          for ( int x = 0; x < names.length; x++ ) {
            LOG.info( "BEAN: " + names[x] );
          }
        } else {
          LOG.error( "There are NO BEAN NAMES!" );
        }
      } else {
        LOG.error( "There is no application context available to the custom tags!" );
      }

      if ( applicationContext != null && applicationContext.containsBean( WebApp.SYSTEM_DESCRIPTION ) ) {
        webapp = (WebApp)applicationContext.getBean( WebApp.SYSTEM_DESCRIPTION );
      }
      if ( webapp == null ) {
        LOG.warn( "Could not get system description...creating one" );
        webapp = new WebApp();
      }
    }

    return webapp;
  }




  public void doTag() throws JspException, IOException {
    JspWriter out = getJspContext().getOut();

    HttpServletRequest request = (HttpServletRequest)( (PageContext)getJspContext() ).getRequest();
    String contextPath = request.getContextPath();
    Locale locale = request.getLocale();

    StringBuffer content = new StringBuffer();
    content.append( "<nav class=\"navbar navbar-default navbar-static-top\" role=\"navigation\" style=\"margin-bottom: 0\">\r\n" );

    content.append( navHeader( contextPath, locale ) );
    content.append( navTopLinks( contextPath, locale ) );
    content.append( navSidebar( contextPath, locale ) );

    content.append( "</nav>\r\n" );

    out.println( content.toString() );
  }




  /**
   * Generates the top and left-most portion of the navigation header.
   * @param contextPath 
   * @param locale 
   * 
   * @return the portion of the navigation containing the web application home link.
   */
  private Object navHeader( String contextPath, Locale locale ) {
    StringBuffer b = new StringBuffer();
    b.append( "\t\t\t<div class=\"navbar-header\">\r\n" );
    b.append( "\t\t\t\t<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\r\n" );
    b.append( "\t\t\t\t\t<span class=\"sr-only\">Toggle navigation</span>\r\n" );
    b.append( "\t\t\t\t\t<span class=\"icon-bar\"></span>\r\n" );
    b.append( "\t\t\t\t\t<span class=\"icon-bar\"></span>\r\n" );
    b.append( "\t\t\t\t\t<span class=\"icon-bar\"></span>\r\n" );
    b.append( "\t\t\t\t</button>\r\n" );
    b.append( "\t\t\t\t<a class=\"navbar-brand\" href=\"" );
    b.append( contextPath );
    b.append( getSystemDescription().getLink() );
    b.append( "\">" );
    b.append( getSystemDescription().getDisplayName(locale) );
    b.append( "</a>\r\n" );
    b.append( "\t\t\t</div>\r\n" );
    return b.toString();
  }




  /**
   * @param contextPath 
   * @param locale 
   * @return
   */
  private Object navSidebar( String contextPath, Locale locale ) {
    StringBuffer b = new StringBuffer();
    b.append( "\t<div class=\"navbar-default sidebar\" role=\"navigation\">\r\n" );
    b.append( "\t\t<div class=\"sidebar-nav navbar-collapse\">\r\n" );
    b.append( "\t\t\t<ul class=\"nav\" id=\"side-menu\">\r\n" );

    // each line item is a menu option

    b.append( "\t\t\t</ul>\r\n" );
    b.append( "\t\t</div>\r\n" );
    b.append( "\t\t<!-- /.sidebar-collapse -->\r\n" );
    b.append( "\t</div>\r\n" );
    b.append( "\t<!-- /.navbar-static-side -->\r\n" );
    return b.toString();
  }




  /**
   * @param contextPath  
   * @param locale 
   * @return
   */
  private Object navTopLinks( String contextPath, Locale locale ) {
    StringBuffer b = new StringBuffer();
    b.append( "\t<ul class=\"nav navbar-top-links navbar-right\">\r\n" );
    b.append( "\t<li class=\"dropdown\">\r\n" );
    b.append( "\t\t<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\r\n" );
    b.append( "\t\t<i class=\"fa fa-user fa-fw\"></i>  <i class=\"fa fa-caret-down\"></i>\r\n" );
    b.append( "\t\t</a>\r\n" );
    b.append( "\t\t<ul class=\"dropdown-menu dropdown-user\">\r\n" );
    b.append( "\t\t\t\t\t<li><a href=\"#\"><i class=\"fa fa-user fa-fw\"></i> User Profile</a>\r\n" );
    b.append( "\t\t\t\t\t</li>\r\n" );
    b.append( "\t\t\t\t\t<li><a href=\"#\"><i class=\"fa fa-gear fa-fw\"></i> Settings</a>\r\n" );
    b.append( "\t\t\t\t\t</li>\r\n" );
    b.append( "\t\t\t\t\t<li class=\"divider\"></li>\r\n" );
    b.append( "\t\t\t\t\t<li><a href=\"login\"><i class=\"fa fa-sign-out fa-fw\"></i> Logout</a>\r\n" );
    b.append( "\t\t\t\t\t</li>\r\n" );
    b.append( "\t\t\t\t</ul>\r\n" );
    b.append( "\t\t\t\t<!-- /.dropdown-user -->\r\n" );
    b.append( "\t\t\t</li>\r\n" );
    b.append( "\t\t\t<!-- /.dropdown -->\r\n" );
    b.append( "\t\t</ul>\r\n" );
    b.append( "\t\t<!-- /.navbar-top-links -->\r\n" );
    b.append( "\t</div>\r\n" );
    return b.toString();
  }

}
