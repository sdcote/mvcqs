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
import java.util.ArrayList;
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

import coyote.commons.StringUtil;
import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuSection;
import coyote.commons.security.Login;
import coyote.commons.security.Permission;
import coyote.commons.security.SecurityPrincipal;


/**
 * This is the tag used in our pages to generate the main navigation menu.
 */
public class MainNav extends SimpleTagSupport {

  private static final Log LOG = LogFactory.getLog( MainNav.class );

  private static WebApp webapp = null;




  public void doTag() throws JspException, IOException {
    JspWriter out = getJspContext().getOut();

    HttpServletRequest request = (HttpServletRequest)( (PageContext)getJspContext() ).getRequest();
    String contextPath = request.getContextPath();
    Locale locale = request.getLocale();

    // Retrieve the login for this request
    Login login = WebApp.getLogin( request );

    StringBuffer content = new StringBuffer();
    content.append( "<nav class=\"navbar navbar-default navbar-static-top\" role=\"navigation\" style=\"margin-bottom: 0\">\r\n" );

    content.append( navHeader( contextPath, locale, login ) );
    content.append( navTopLinks( contextPath, locale, login ) );
    content.append( navSidebar( contextPath, locale, login ) );

    content.append( "\t\t</nav>\r\n" );
    content.append( "\t\t<!-- / Navigation -->\r\n" );

    out.println( content.toString() );
  }




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

    if ( webapp == null ) {
      WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext( ( (PageContext)getJspContext() ).getServletContext() );

      if ( applicationContext != null ) {
        String[] names = applicationContext.getBeanDefinitionNames();
        if ( names.length > 0 ) {
          for ( int x = 0; x < names.length; x++ ) {
            LOG.trace( "BEAN: " + names[x] );
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




  /**
   * Generates the top and left-most portion of the navigation header.
   * @param contextPath 
   * @param locale 
   * @param login 
   * 
   * @return the portion of the navigation containing the web application home link.
   */
  private Object navHeader( String contextPath, Locale locale, Login login ) {
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
    b.append( getSystemDescription().getDisplayName( locale ) );
    b.append( "</a>\r\n" );
    b.append( "\t\t\t</div>\r\n" );
    b.append( "\t\t\t<!-- /.navbar-header -->\r\n\r\n" );

    return b.toString();
  }




  /**
   * @param contextPath  
   * @param locale 
   * @param login 
   * @return
   */
  private Object navTopLinks( String contextPath, Locale locale, Login login ) {
    StringBuffer b = new StringBuffer();
    b.append( "\t\t\t<ul class=\"nav navbar-top-links navbar-right\">\r\n" );

    //

    // Menu Items across the top goes here

    //

    // We always have a user section at the far right side of the top menu
    b.append( "\t\t\t\t<li class=\"dropdown\">\r\n" );
    b.append( "\t\t\t\t\t<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">" );

    if ( login == null ) {
      // No login, we display a different user icon...
      b.append( "<i class=\"fa fa-question fa-fw\"></i><i class=\"fa fa-user fa-fw\"></i>  <i class=\"fa fa-caret-down\"></i></a>\r\n" );
      b.append( "\t\t\t\t\t<ul class=\"dropdown-menu dropdown-user\">\r\n" );

      // ... and just show the sign-in option
      String signinName = System.getProperty( WebApp.SIGNIN_PROPERTY );
      if ( signinName != null ) {
        Feature signInFeature = getSystemDescription().getFeature( signinName );
        if ( signInFeature != null ) {
          b.append( "\t\t\t\t\t\t<li>" );
          b.append( getFeatureLink( signInFeature, contextPath, locale, false ) );
          b.append( "</li>\r\n" );
        } else {
          LOG.error( "No feature named '" + signinName + "' defined in system description" );
        }
      } else {
        LOG.error( "There is no sign-in feature specified in system property '" + WebApp.SIGNIN_PROPERTY + "'" );
      }
    } else {
      // since we have a login, we provide a different menu to the user
      SecurityPrincipal principal = login.getPrincipal();
      String loginName = principal.getName();
      if ( StringUtil.isNotBlank( loginName ) ) {
        b.append( loginName );
        b.append( "  <i class=\"fa fa-user fa-fw\"></i>  <i class=\"fa fa-caret-down\"></i></a>\r\n" );
      } else {
        b.append( "<i class=\"fa fa-user fa-fw\"></i>  <i class=\"fa fa-caret-down\"></i></a>\r\n" );
      }
      b.append( "\t\t\t\t<ul class=\"dropdown-menu dropdown-user\">\r\n" );

      // Login profile feature - the login's home page
      String featureName = System.getProperty( WebApp.LOGIN_PROFILE_PROPERTY );
      if ( featureName != null ) {
        Feature feature = getSystemDescription().getFeature( featureName );
        if ( feature != null ) {
          b.append( "\t\t\t\t\t<li>" );
          b.append( getFeatureLink( feature, contextPath, locale, false ) );
          b.append( "</li>\r\n" );
        } else {
          LOG.error( "No feature named '" + featureName + "' defined in system description" );
        }
      } else {
        LOG.error( "There is no user profile feature specified in system property '" + WebApp.LOGIN_PROFILE_PROPERTY + "'" );
      }

      // Login settings feature - the login's application configuration
      featureName = System.getProperty( WebApp.LOGIN_SETTINGS_PROPERTY );
      if ( featureName != null ) {
        Feature feature = getSystemDescription().getFeature( featureName );
        if ( feature != null ) {
          b.append( "\t\t\t\t\t<li>" );
          b.append( getFeatureLink( feature, contextPath, locale, false ) );
          b.append( "</li>\r\n" );
        } else {
          LOG.error( "No feature named '" + featureName + "' defined in system description" );
        }
      } else {
        LOG.error( "There is no user profile feature specified in system property '" + WebApp.LOGIN_SETTINGS_PROPERTY + "'" );
      }

      // menu divider
      b.append( "\t\t\t\t\t<li class=\"divider\"></li>\r\n" );

      // Lookup the Sign-Out feature
      featureName = System.getProperty( WebApp.SIGNOUT_PROPERTY );
      if ( featureName != null ) {
        Feature feature = getSystemDescription().getFeature( featureName );
        if ( feature != null ) {
          b.append( "\t\t\t\t\t<li>" );
          b.append( getFeatureLink( feature, contextPath, locale, false ) );
          b.append( "</li>\r\n" );
        } else {
          LOG.error( "No feature named '" + featureName + "' defined in system description" );
        }
      } else {
        LOG.error( "There is no user profile feature specified in system property '" + WebApp.SIGNOUT_PROPERTY + "'" );
      }

    }

    b.append( "\t\t\t\t\t</ul>\r\n" );
    b.append( "\t\t\t\t\t<!-- /.dropdown-user -->\r\n" );

    b.append( "\t\t\t\t</li>\r\n" );
    b.append( "\t\t\t\t<!-- /.dropdown -->\r\n" );
    b.append( "\t\t\t</ul>\r\n" );
    b.append( "\t\t\t<!-- /.navbar-top-links -->\r\n\r\r" );

    return b.toString();
  }




  /**
   * This generates the menu for the LEFT side nav bar.
   * 
   * <p>It supports three levels of sub-menus.</p>
   * 
   * @param contextPath 
   * @param locale 
   * @param session
   *  
   * @return The HTML representing the left menu bar
   */
  private Object navSidebar( String contextPath, Locale locale, Login login ) {
    StringBuffer b = new StringBuffer();
    b.append( "\t\t\t<div class=\"navbar-default sidebar\" role=\"navigation\">\r\n" );
    b.append( "\t\t\t\t<div class=\"sidebar-nav navbar-collapse\">\r\n" );
    b.append( "\t\t\t\t\t<ul class=\"nav\" id=\"side-menu\">\r\n" );

    // Get a list of top level features from the system description
    List<Feature> features1 = getFeatures( getSystemDescription(), MenuSection.LEFT, login );
    for ( Feature feature : features1 ) {

      // Check to see if we are going to represent a multi-level menu
      List<Feature> features2 = getFeatures( feature, MenuSection.LEFT, login );
      boolean isParent = features2.size() > 0;

      b.append( "\t\t\t\t\t\t<li>" );
      b.append( getFeatureLink( feature, contextPath, locale, isParent ) );
      if ( !isParent ) {
        b.append( "</li>" );
      }
      b.append( "\r\n" );

      // if there are second level features...
      if ( features2.size() > 0 ) {
        b.append( "\t\t\t\t\t\t\t<ul class=\"nav nav-second-level\">\r\n" );
        for ( Feature feature2 : features2 ) {

          List<Feature> features3 = getFeatures( feature2, MenuSection.LEFT, login );
          boolean isLevel2Parent = features3.size() > 0;

          // gen the second level
          b.append( "\t\t\t\t\t\t\t\t<li>" );
          b.append( getFeatureLink( feature2, contextPath, locale, isLevel2Parent ) );
          if ( !isLevel2Parent ) {
            b.append( "</li>" );
          }
          b.append( "\r\n" );

          // Handle 3rd level menus (but this is a low as we go)
          if ( features3.size() > 0 ) {
            b.append( "\t\t\t\t\t\t\t\t\t<ul class=\"nav nav-third-level\">\r\n" );
            for ( Feature feature3 : features3 ) {
              b.append( "\t\t\t\t\t\t\t\t\t\t<li>" );
              b.append( getFeatureLink( feature3, contextPath, locale, false ) );
              b.append( "</li>\r\n" );
            }
            b.append( "\t\t\t\t\t\t\t\t\t</ul>\r\n" );
            b.append( "\t\t\t\t\t\t\t\t\t<!-- /.nav-third-level -->\r\n" );
            b.append( "\t\t\t\t\t\t\t\t</li>\r\n" );
          }
        }
        b.append( "\t\t\t\t\t\t\t</ul>\r\n" );
        b.append( "\t\t\t\t\t\t\t<!-- /.nav-second-level -->\r\n" );
        b.append( "\t\t\t\t\t\t</li>\r\n" );

      }
    }

    b.append( "\t\t\t\t\t</ul>\r\n" );
    b.append( "\t\t\t\t</div>\r\n" );
    b.append( "\t\t\t\t<!-- /.sidebar-collapse -->\r\n" );
    b.append( "\t\t\t</div>\r\n" );
    b.append( "\t\t\t<!-- /.navbar-static-side -->\r\n" );
    return b.toString();
  }




  /**
   * Create a link for a feature.
   * 
   * <p>Parent features do not have links to parts of the application; only 
   * child features do. This means if a feature is marked as a parent, it is to 
   * be displayed as the parent of a multi-level menu.</p>
   * 
   * @param signInFeature
   * @param contextPath
   * @param locale
   * @param parent 
   * @return
   */
  private String getFeatureLink( Feature feature, String contextPath, Locale locale, boolean parent ) {
    StringBuffer b = new StringBuffer();

    b.append( "<a href=\"" );

    // parent features are represented as a menu node; not a link to a
    // feature
    if ( parent ) {
      b.append( "#" );
    } else {
      b.append( contextPath );
      b.append( feature.getLink() );
    }
    b.append( "\">" );

    // If there is an icon defined for the feature, use it to generate one
    // in the UI
    if ( feature.getIcon() != null ) {
      b.append( "<i class=\"fa fa-" );
      b.append( feature.getIcon().toString() );
      b.append( " fa-fw\"></i> " );
    }

    // Display then name of the feature
    b.append( feature.getDisplayName( locale ) );

    // Parent features are shown as drop-downs in the UI
    if ( parent ) {
      b.append( "<span class=\"fa arrow\"></span>" );
    }
    b.append( "</a>" );

    return b.toString();
  }




  /**
   * This retrieves a list of child features from the given feature which are 
   * to be displayed in the given menu section and is executable by the given 
   * login.
   * 
   * @param feature The parent feature to query
   * @param section the menu section the child features are to be placed
   * @param login the login making the request
   * 
   * @return a list of child features meeting the given criteria; if none are 
   * found, the list will be empty. This method will not return {@code null}.
   */
  private List<Feature> getFeatures( Feature feature, MenuSection section, Login login ) {
    final List<Feature> retval = new ArrayList<Feature>();
    List<Feature> features = feature.getFeaturesBySection( section );
    for ( Feature child : features ) {
      if ( login != null ) {
        if ( getSystemDescription().getSecurityContext().allows( login, Permission.EXECUTE, "FEATURE:" + child.getName() ) ) {
          retval.add( child );
        } else {
          LOG.info( login.getPrincipal().getName() + " does not have access to feature " + child.getName() );
          retval.add( child ); // TODO: add it anyways...still working
          // on the SecurityContext and
          // permissions model
        }
      } else {
        LOG.warn( "There is no login!!!!!" );
        retval.add( child ); // TODO: add it anyways...still working on
        // the SecurityContext and permissions model
      }
    }

    return retval;
  }

}
