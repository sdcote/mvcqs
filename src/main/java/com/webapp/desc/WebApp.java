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
package com.webapp.desc;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

import coyote.commons.StringUtil;
import coyote.commons.Version;
import coyote.commons.feature.Feature;
import coyote.commons.feature.SystemDescription;
import coyote.commons.security.Context;
import coyote.commons.security.Login;
import coyote.commons.security.Session;


/**
 * This is the description of this web application.
 * 
 * <p>This class is used to keep track of the current state of this systems 
 * evolution. It ties in all the features modeled in this package to provide a
 * structure which can be queried by system components to ensure uniform and 
 * consistent operation.</p>
 * 
 * <p>The system description is used, for example, to generate navigational 
 * views into each of the supported features. The attributes of each feature 
 * are used to generate labels, tool tips, help and relative navigational links 
 * to other features of the system.</p>
 * 
 * <p>This class and its associated features are also used in the development 
 * of the system. The teams definition of done (DoD) often includes the 
 * creation of documentation relating to the current state of a system. By 
 * coding these details into the system artifacts, all the details are 
 * committed to the code repository and made available to all members of the 
 * team and to automated build and deployment tools. Keeping this information 
 * close to the code has proven very useful and a best practice for effective 
 * agile teams.</p>   
 * 
 * <p>This is also the location of many of the Web Applications constants.</p>
 */
public class WebApp extends SystemDescription {
  private static final Log LOG = LogFactory.getLog( WebApp.class );

  public static final String SYSTEM_DESCRIPTION = "systemDescription";

  public static final String SIGNIN_PROPERTY = "signin.feature.name";
  public static final String SIGNOUT_PROPERTY = "signout.feature.name";
  public static final String USER_PROFILE_PROPERTY = "userprofile.feature.name";
  public static final String USER_SETTINGS_PROPERTY = "usersettings.feature.name";

  /** The name of the cookie containing the security context's session identifier */
  public static final String COOKIE_SESSION_KEY = "mvcqsid";

  /** The session attribute containing login reference */
  public static final String SESSION_LOGIN_KEY = "login";

  /** The session attribute containing the ultimate destination of this session */
  public static final String SESSION_TARGET_URI_KEY = "targetURI";

  private static Context securityContext = null;
  private static ResourceBundleMessageSource messageSource = null;




  public WebApp() {
    version = new Version( 1, 0, 0, Version.DEVELOPMENT );
    name = "webapp";
    link = "/";
    description = "This is a fully-functional web application from which developers can begin prototyping their own application in a matter of seconds. It is a starting point for prototypes, utilities and proof of concept systems which can run on the desktop for individual use or exposed to all members of the team or organization.";

    // We are providing security features
    Feature security = new SecurityTheme();
    addFeature( security );
    security.addFeature( new LoginFeature() );
    security.addFeature( new LogoutFeature() );
    // security.addFeature( new FindLoginFeature() );
    // security.addFeature( new AddLoginFeature() );
    // security.addFeature( new ChangeLoginFeature() );
    // security.addFeature( new DeleteLoginFeature() );
    // security.addFeature( new FindRoleFeature() );
    // security.addFeature( new AddRoleFeature() );
    // security.addFeature( new ChangeRoleFeature() );
    // security.addFeature( new DeleteRoleFeature() );
    // security.addFeature( new GrantRolePermissionFeature() );
    // security.addFeature( new RevokeRolePermissionFeature() );
    // security.addFeature( new GrantLoginPermissionFeature() );
    // security.addFeature( new RevokeLoginPermissionFeature() );
    // security.addFeature( new GrantRolePermissionFeature() );
    // security.addFeature( new ChangeCredentialFeature() );

    // User profile management
    Feature profile = new UserProfileTheme();
    addFeature( profile );
    profile.addFeature( new UserProfileFeature() );
    profile.addFeature( new UserSettingsFeature() );

    // Operations pages and functions; thread pools and background processes
    // Feature operations = new OperationsTheme();
    // operations.addFeature( new BackgroundJobsFeature() );
    // operations.addFeature( new SchedulerFeature() );

    // This shows how to add your feature to the system
    Feature helloworld = new HelloWorldFeature();
    addFeature( helloworld );

  }




  public static Login getLogin( HttpServletRequest request ) {
    Login retval = null;

    HttpSession session = request.getSession( true );

    try {
      retval = (Login)session.getAttribute( SESSION_LOGIN_KEY );
    } catch ( Exception e ) {
      LOG.error( e );
    }

    // If the login was not in the HTTP session, check for the cookie
    if ( retval == null ) {

      if ( request != null ) {
        String sessionid = null;

        Cookie[] cookies = request.getCookies();
        if ( cookies != null ) {

          for ( int i = 0; i < cookies.length; i++ ) {
            if ( COOKIE_SESSION_KEY.equals( cookies[i].getName() ) ) {
              sessionid = cookies[i].getValue();
              break;
            }
          }

          if ( StringUtil.isNotBlank( sessionid ) ) {
            if ( securityContext != null ) {
              LOG.trace( "Retrieving security session for " + sessionid );
              Session msession = securityContext.getSession( sessionid );
              if ( msession != null ) {
                // TODO: It might be interesting to ensure that
                // the servlet session identifier matches data
                // stored in the security context's session as
                // an additional check
                retval = msession.getLogin();
                LOG.trace( "Using existing session from security context" );
              } else {
                LOG.warn( "No security session for session ID :" + sessionid );
                // TODO: Should we clear the cookie since we
                // lost the session?
              }
            } else {
              LOG.warn( "Security Context not set" );
            }

          } // session id !null
        } // cookies !null
      } // request !null

    } // login was not stored in the local http session

    // return what we retrieved
    return retval;
  }




  /**
   * @param source
   */
  public void setMessageSource( ResourceBundleMessageSource source ) {
    messageSource = source;
  }




  /**
   * @param context The security context to use for this web application
   */
  public void setSecurityContext( Context context ) {
    securityContext = context;
  }




  /**
   * @return The security context to use for this web application
   */
  public Context getSecurityContext() {
    return securityContext;
  }




  /**
   * This performs all the message lookups for all child resources.
   * 
   * <p>Features delegate message retrieval to their parents by default. Since 
   * we extend {@code SystemDescription} which is a feature and all features 
   * are added to the system description (this class) this method is the root 
   * of all call to get messages for all features.</p>.
   * 
   * <p>This method uses the Spring {@code ResourceBundleMessageSource} to 
   * handle all message resolution and is auto-wired by the Spring IoC 
   * context.</p> 
   * 
   * @see coyote.commons.feature.Feature#getMessage(java.lang.String, java.lang.Object[], java.util.Locale)
   */
  @Override
  public String getMessage( String key, Object[] args, Locale locale ) {
    if ( messageSource != null ) {
      try {
        return messageSource.getMessage( key, args, locale );
      } catch ( NoSuchMessageException e ) {
        LOG.warn( e.getMessage() + " - returning key: '" + key + "'" );
        return key;
      }
    } else {
      return key;
    }

  }

}
