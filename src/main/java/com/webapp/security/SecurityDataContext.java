/*
 * Copyright (c) 2014 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 * Stephan D. Cote 
 * - Initial concept and initial implementation
 */
package com.webapp.security;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import coyote.commons.ByteUtil;
import coyote.commons.StringUtil;
import coyote.commons.security.CredentialSet;
import coyote.commons.security.GenericSecurityContext;
import coyote.commons.security.GenericSecurityPrincipal;
import coyote.commons.security.Login;
import coyote.commons.security.Role;
import coyote.commons.security.SecurityContext;


/**
 * This is a security context backed by a data store.
 * 
 * <p>The main functionality is provided by the GenericContext superclass. This 
 * class has a few overrides which allow it to capture changes to the security
 * context data model and persist those changes to a database.<p>
 * 
 * <p>Other versions of this security context implement a distributed data 
 * store which allows many different servers in a data center to share security 
 * data such as sessions. This way it is possible for a user to login and 
 * establish as session on one server but still hit a different server on 
 * subsequent requests.</p> 
 */
public class SecurityDataContext extends GenericSecurityContext implements SecurityContext {

  private DataSource dataSource = null;
  private static final Log LOG = LogFactory.getLog( SecurityDataContext.class );
  private JdbcTemplate jdbcTemplate = null;




  /**
  * @param name
  */
  public SecurityDataContext( String name ) {
    super( name );
  }




  /**
  * @param source
  */
  public void setDataSource( DataSource source ) {
    this.dataSource = source;
    jdbcTemplate = new JdbcTemplate( source );

  }




  /**
  * Initialize the context
  */
  public void init() throws Exception {
    // Get a listing of the tables in this data store
    Set<String> tableset = new HashSet<String>();
    DatabaseMetaData md = dataSource.getConnection().getMetaData();
    ResultSet rs = md.getTables( null, null, "%", null );
    while ( rs.next() ) {
      tableset.add( rs.getString( 3 ) );
    }

    // Check to see if the needed tables exist and create them if necessary
    if ( !tableset.contains( "SECURITY_CONTEXT" ) ) {
      LOG.info( "Creating SECURITY_CONTEXT table" );
    }

    if ( !tableset.contains( "SECURITY_LOGIN" ) ) {
      LOG.info( "Creating SECURITY_LOGIN table" );

      // Create the table
      String createLoginTable = "CREATE TABLE SECURITY_LOGIN ( LOGIN_ID BIGINT NOT NULL AUTO_INCREMENT, CONTEXT VARCHAR(64) NOT NULL, NAME VARCHAR(64) NOT NULL, PASSWORD VARCHAR(64) NOT NULL, PASSWORD_HINT VARCHAR(64), IS_SYSTEM BOOLEAN, IS_ENABLED BOOLEAN, IS_LOGGEDOUT BOOLEAN, REQUIRE_PASSWORD_CHANGE BOOLEAN, CURRENCY_UOM VARCHAR(64), LOCALE VARCHAR(64), TIMEZONE VARCHAR(64), DISABLED_DATETIME TIMESTAMP, PARTY_ID BIGINT );";
      jdbcTemplate.execute( createLoginTable );

      LOG.info( "Creating SECURITY_LOGIN key" );
      // Create the primary key
      String createLoginkey = "ALTER TABLE SECURITY_LOGIN ADD CONSTRAINT pk_security_login PRIMARY KEY (CONTEXT,LOGIN_ID);";
      jdbcTemplate.execute( createLoginkey );

      LOG.info( "Creating SECURITY_LOGIN index" );
      // Create the primary index
      String createLoginIndex = "CREATE UNIQUE INDEX IDX_SECURITY_LOGIN ON SECURITY_LOGIN(CONTEXT,NAME);";
      jdbcTemplate.execute( createLoginIndex );
      LOG.info( "SECURITY_LOGIN table created successfully" );

    }

    // Now read in the security context from the database
    // TODO: Read in logins for the named security context...

    LOG.info( "Security context initialized" );
  }




  /**
  * @see coyote.commons.security.GenericContext#add(coyote.commons.security.Login)
  */
  @Override
  public void add( Login login ) {

    if ( login != null ) {

      if ( login.getPrincipal() != null ) {
        CredentialSet credentials = login.getCredentials();

        if ( credentials != null ) {

          // If there is a login with these credentials, generate a warning 
          if ( credentials != null ) {

            String principalName = login.getPrincipal().getName();

            // First we should see if there is already a login with this name 
            Login existing = retrieveLogin( principalName );

            if ( existing != null ) {
              LOG.warn( "A login with the name '" + principalName + "' already exists in the context '" + getName() + "' - login was not added" );
            } else {

              // add the login to the database
              String loginid = createLogin( login );

              // add the login into the cache
              super.add( login );
            }
          }
        } else {
          LOG.warn( "Ignoring attempt to add login with no security principal" );
        }
      } else {
        LOG.warn( "Ignoring attempt to add login without credentials" );
      }
    } else {
      LOG.warn( "Ignoring attempt to add a null login reference" );
    }

  }




  /**
  * @param login
  * 
  * @return
  */
  private String createLogin( final Login login ) {

    final String insertSql = "insert into SECURITY_LOGIN (CONTEXT,NAME,PASSWORD,PARTY_ID) values (?,?,?,?)";

    LOG.info( "Inserting new login (" + login.getPrincipal().getName() + ") into " + getName() );

    if ( login.getCredentials() != null && login.getCredentials().contains( CredentialSet.PASSWORD ) ) {

      // Get the credentials
      byte[] value = login.getCredentials().getValue( CredentialSet.PASSWORD );

      // If there is a value for the password...
      if ( value != null && value.length > 0 ) {

        // turn it into a hex string
        final String dbValue = ByteUtil.bytesToHex( value, null );

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
          jdbcTemplate.update( new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {

              PreparedStatement ps = connection.prepareStatement( insertSql.toString(), Statement.RETURN_GENERATED_KEYS );
              ps.setString( 1, getName().toLowerCase() ); //context
              ps.setString( 2, login.getPrincipal().getName().toLowerCase() ); // name
              ps.setString( 3, dbValue ); // password
              ps.setLong( 4, 0 );// No party ID for now
              return ps;
            }
          }, keyHolder );
        } catch ( DataAccessException e ) {
          LOG.error( "Could not create login: " + e.getMessage() );
        }

        // Return the ID of the login
        return keyHolder.getKey().toString();

      } else {
        LOG.error( "Empty password - login not added" );
      }
    } else {
      LOG.error( "No login credentials - login not added" );
    }

    return null;
  }




  /**
  * @param principal 
  * @param credentials
  * 
  * @return
  */
  private Login retrieveLogin( String principal ) {

    if ( principal != null ) {

      LOG.info( "Searching for login name of '" + principal + "' " );

      // do the database lookup
      List<Login> work = new ArrayList<Login>();

      final String SQL = "SELECT * FROM SECURITY_LOGIN WHERE CONTEXT = ? AND NAME = ?";

      LOG.debug( SQL );

      work = jdbcTemplate.query( SQL, new Object[] { getName().toLowerCase(), principal.toLowerCase() }, new LoginMapper() );

      if ( work.size() > 0 ) {
        if ( work.size() > 1 ) {
          LOG.fatal( "There are more than one security principals with the name of '" + principal + "' in the '" + getName() + "' security context; returning the first record" );
        }
        return work.get( 0 );
      }
    }
    return null;
  }




  /**
   * @see coyote.commons.security.GenericSecurityContext#getLogin(java.lang.String, coyote.commons.security.CredentialSet)
   */
  @Override
  public Login getLogin( String name, CredentialSet creds ) {

    LOG.info( "Searching for login '"+name+"' with a password of '"+ByteUtil.bytesToHex( creds.getValue( CredentialSet.PASSWORD ), null )+"'" );
    
    // Try the cache
    Login retval = super.getLogin( name, creds );;

    // If not in cache...
    if ( retval == null ) {
      LOG.info( "Did not find login '" + name + "' in cache, searching database" );
      // ... try the database
      retval = retrieveLogin( name, creds );

      if ( retval != null ) {
        LOG.info( "Found login in the database" );
        // add it to the cache
        super.add( retval );
      } else {
        LOG.info( "Did not find login '" + name + "' in database either" );
      }
    } else {
      LOG.info( "Found login in cache -> "+retval );
    }

    return retval;
  }




  /**
  * @param principal 
  * @param credentials
  * 
  * @return
  */
  private Login retrieveLogin( String principal, CredentialSet credentials ) {

    if ( principal != null ) {

      byte[] value = credentials.getValue( CredentialSet.PASSWORD );
      if ( value != null ) {
        String dbValue = ByteUtil.bytesToHex( value, null );
        LOG.info( "Searching for login name of '" + principal + "' with credentials of '" + dbValue + "'" );

        // do the database lookup
        List<Login> work = new ArrayList<Login>();

        final String SQL = "SELECT * FROM SECURITY_LOGIN WHERE CONTEXT = ? AND NAME = ? AND PASSWORD = ?";

        LOG.debug( SQL );

        work = jdbcTemplate.query( SQL, new Object[] { getName(), principal, dbValue }, new LoginMapper() );

        if ( work.size() > 0 ) {
          if ( work.size() > 1 ) {
            LOG.fatal( "There are more than one security principals with the name of '" + principal + "' in the '" + getName() + "' security context; returning the first record" );
          }
          return work.get( 0 );
        }
      } else {
        LOG.warn( "No password found in credentials" );
      }
    }
    return null;
  }




  private void updateLogin( Login login ) {

  }




  private void deleteLogin( Login login ) {

  }




  /**
  * @see coyote.commons.security.GenericContext#add(coyote.commons.security.Role)
  */
  @Override
  public void add( Role role ) {
    // TODO Add the role to the database
    super.add( role );
  }

  /**
   * Maps a login record to a simple Login record with a security principal and 
   * a credential set.
   */
  public final class LoginMapper implements RowMapper<Login> {

    @Override
    public Login mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
      final Login retval = new Login();

      retval.setId( rs.getString( "LOGIN_ID" ) );
      retval.setPrincipal( new GenericSecurityPrincipal( rs.getString( "PARTY_ID" ), rs.getString( "NAME" ) ) );
      String passwd = rs.getString( "PASSWORD" );
      if ( StringUtil.isNotBlank( passwd ) ) {
        CredentialSet creds = new CredentialSet();
        creds.add( CredentialSet.PASSWORD, ByteUtil.hexToBytes( passwd ) );
        retval.setCredentials( creds );
      } else {
        LOG.warn( "Retrieved a login (" + retval.getPrincipal().getName() + ") with no credentials" );
      }

      return retval;
    }
  }

}
