package com.webapp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * This allows for the population of the System properties from a variety of
 * locations.
 * 
 * <p>Once the webapp is deployed, it can be very difficult to configure its 
 * operation since the administrators do not have ready access to the XML files
 * in the WEB-INF directory. One way to make an application more configurable 
 * is to use system properties at runtime.</p>  
 * 
 * <p>This is called in the web.xml file and used to initialize the system with
 * application specific properties.
 * 
 */
public class SystemPropertiesLoader implements ServletContextListener
{

  /** Root name of the web application property file */
  public static final String COMMON_PROPS = "webapp";

  /** System property which specifies the user name for the proxy server */
  public static final String PROXY_USER = "http.proxyUser";

  /** System property which specifies the user password for the proxy server */
  public static final String PROXY_PASSWORD = "http.proxyPassword";

  /** System property which specifies the proxy server host name */
  public static final String PROXY_HOST = "http.proxyHost";

  /** System property which specifies the configuration directory for the web application */
  public static final String CONFIG_DIR = "cfg.dir";




  /**
   * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
   */
  @Override
  public void contextInitialized( ServletContextEvent sce )
  {
    // Load specific files from the WEB-INF
    loadPropertiesIntoSystem( COMMON_PROPS, true, sce.getServletContext().getRealPath( "/WEB-INF/" ) );

    // Next load specific property files from the base path which over-rides 
    // those properties 
    loadPropertiesIntoSystem( COMMON_PROPS, false, getBasePath() );

    // Load the Java proxy authenticator if system properties contained the 
    // necessary data
    installProxyAuthenticatorIfNeeded();
  }




  /**
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
   */
  @Override
  public void contextDestroyed( ServletContextEvent sce )
  {
  }




  private void loadPropertiesIntoSystem( String fileName, boolean errIfMissing, String pathName )
  {
    String filename = pathName+ File.separator + fileName + ".properties";
    System.out.println( this.getClass().getCanonicalName() + ": Trying to load properties from " + filename + " into system" );
    Properties props = new Properties();
    try
    {
      props.load( new FileInputStream( filename ) );
      System.getProperties().putAll( props );
    }
    catch( IOException e )
    {
      String msg = "Failed to read from " + filename;
      System.err.println( this.getClass().getCanonicalName() + ": " + msg + ": " + e.getMessage() );
      if( errIfMissing )
      {
        throw new IllegalStateException( msg, e );
      }
    }
  }




  /**
   * Load the Java proxy authenticator if the are system properties specifying 
   * a proxy host and user name.
   */
  private void installProxyAuthenticatorIfNeeded()
  {
    final String user = System.getProperty( PROXY_USER );
    final String password = System.getProperty( PROXY_PASSWORD );
    final String host = System.getProperty( PROXY_HOST );
    if( !isBlank( user ) && !isBlank( password ) && !isBlank( host ) )
    {
      System.out.println( String.format( "Detected http proxy settings (%s@%s), will setup authenticator", user, host ) );
      Authenticator.setDefault( new Authenticator()
      {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
          return new PasswordAuthentication( user, password.toCharArray() );
        }
      } );
    }
  }




  public static String getBasePath()
  {
    String fs = File.separator;
    StringBuilder path = new StringBuilder();
    String cfgdir = System.getProperty( CONFIG_DIR );
    if( isBlank( cfgdir ) )
    {
      System.getProperty( "user.dir" );
    }
    else
    {
      path.append( cfgdir );
    }
    path.append( fs );

    return path.toString();
  }




  /**
   * Checks if a String is whitespace, empty ("") or null.
   *
   * <pre>
   * StringUtils.isBlank(null)       = true
   * StringUtils.isBlank("")         = true
   * StringUtils.isBlank(" ")        = true
   * StringUtils.isBlank("biff")     = false
   * StringUtils.isBlank("  biff  ") = false
   * </pre>
   *
   * @param str  the String to check, may be null
   * @return <code>true</code> if the String is null, empty or whitespace
   * @since 2.0
   */
  public static boolean isBlank( String str )
  {
    int strLen;
    if( str == null || ( strLen = str.length() ) == 0 )
    {
      return true;
    }
    for( int i = 0; i < strLen; i++ )
    {
      if( ( Character.isWhitespace( str.charAt( i ) ) == false ) )
      {
        return false;
      }
    }
    return true;
  }
}
