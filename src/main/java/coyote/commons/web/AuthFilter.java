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
package coyote.commons.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import coyote.commons.security.Context;


/**
 * An authentication and authorization filter to protect access to resources.
 * 
 * <p>This filter ensures all requests have a session that are requesting 
 * access to private (non-public) resources.</p>
 */
public class AuthFilter implements Filter {
  private ServletContext context;
  private static final Log LOG = LogFactory.getLog( AuthFilter.class );

  ApplicationContext applicationContext = null;

  Context securityContext = null;




  @Override
  public void init( FilterConfig filterConfig ) throws ServletException {
    context = filterConfig.getServletContext();
    LOG.info( "Servlet Context:" + context );

    applicationContext = WebApplicationContextUtils.getWebApplicationContext( context );
    LOG.info( "Spring Context:" + applicationContext );

    @SuppressWarnings("rawtypes")
    Enumeration initNames = context.getInitParameterNames();
    if ( initNames != null ) {
      while ( initNames.hasMoreElements() ) {
        String name = (String)initNames.nextElement();
        String value = filterConfig.getInitParameter( name );
        LOG.trace( "Init:" + name + ":" + value );
      }
    }

    @SuppressWarnings("rawtypes")
    Enumeration attrNames = context.getAttributeNames();
    if ( attrNames != null ) {
      while ( attrNames.hasMoreElements() ) {
        String name = (String)attrNames.nextElement();
        String value = filterConfig.getInitParameter( name );
        LOG.trace( "Attr:" + name + ":" + value );
      }
    }

    @SuppressWarnings("rawtypes")
    Enumeration initParams = filterConfig.getInitParameterNames();
    if ( initParams != null ) {
      while ( initParams.hasMoreElements() ) {
        String name = (String)initParams.nextElement();
        String value = filterConfig.getInitParameter( name );
        LOG.trace( name + ":" + value );
      }
    }

    if ( applicationContext != null && applicationContext.containsBean( "securityContext" ) ) {
      securityContext = (Context)applicationContext.getBean( "securityContext" );
    }

    if ( securityContext != null ) {
      LOG.trace( "Security Context Initialized" );
    } else {
      LOG.fatal( "Could not obtain a reference to the security context); application is unsecured!" );
    }

    LOG.trace( "Authentication Filter initialized" );
  }




  @Override
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)request;

    @SuppressWarnings("unchecked")
    Enumeration<String> params = req.getParameterNames();
    while ( params.hasMoreElements() ) {
      String name = params.nextElement();
      String value = request.getParameter( name );
      LOG.trace( req.getRemoteAddr() + "::Request Params::{" + name + "=" + value + "}" );
    }

    Cookie[] cookies = req.getCookies();
    if ( cookies != null ) {
      for ( Cookie cookie : cookies ) {
        LOG.trace( req.getRemoteAddr() + "::Cookie::{" + cookie.getName() + "," + cookie.getValue() + "}" );
      }
    }

    String uri = req.getRequestURI();
    LOG.debug( "Requested Resource:" + uri );

    HttpSession session = req.getSession( false );

    HttpServletResponse res = (HttpServletResponse)response;

    if ( session == null && uriIsProtected( uri ) ) {
      LOG.warn( "Unauthorized access request" );
      res.sendRedirect( "login.html" );
    } else {
      // pass the request along the filter chain
      try {
        chain.doFilter( request, response );
      } catch ( Exception e ) {
        LOG.warn( "Exception sending request down the chain", e );
      }
    }

  }




  /**
   * 
   * @param uri
   * @return
   */
  private boolean uriIsProtected( String uri ) {

    // Check the URI against a list of anonymous URI patterns
    // if the pattern matches return false
    return false;
    // if there is no match, assume the URI is protected and requires
    // authentication (session)
    // return true;
  }




  @Override
  public void destroy() {

    context.log( "AuthFilter Filter destroyed" );
    LOG.info( "Authentication destroyed" );
  }

}
