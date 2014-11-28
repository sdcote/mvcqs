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

  private static SystemDescription webapp = null;




  private SystemDescription getSystemDescription() {

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
        webapp = (SystemDescription)applicationContext.getBean( WebApp.SYSTEM_DESCRIPTION );
      }
      if ( webapp == null ) {
        LOG.warn( "Could not get system description...creating one" );
        //webapp = new WebApp();
      }
    }

    return webapp;
  }




  public void doTag() throws JspException, IOException {

    JspWriter out = getJspContext().getOut();
    out.println( "This is where the navigation menu for " + getSystemDescription().getDisplayName() + getSystemDescription().getVersion() +" will go." );
  }
}
