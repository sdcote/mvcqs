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
import coyote.commons.security.Permission;
import coyote.commons.security.Role;
import coyote.commons.security.SecurityContext;
import coyote.commons.security.Session;


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

  private static final Role NO_ROLE = new Role( "NONE", "NO ROLE" );




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
      String createLoginTable = "CREATE TABLE SECURITY_LOGIN ( LOGIN BIGINT NOT NULL AUTO_INCREMENT, CONTEXT VARCHAR(64) NOT NULL, NAME VARCHAR(64) NOT NULL, PASSWORD VARCHAR(64) NOT NULL, PASSWORD_HINT VARCHAR(64), IS_SYSTEM BOOLEAN, IS_ENABLED BOOLEAN, IS_LOGGEDOUT BOOLEAN, REQUIRE_PASSWORD_CHANGE BOOLEAN, CURRENCY_UOM VARCHAR(64), LOCALE VARCHAR(64), TIMEZONE VARCHAR(64), DISABLED_DATETIME TIMESTAMP, PARTY BIGINT );";
      jdbcTemplate.execute( createLoginTable );

      // Create the primary key
      String createLoginkey = "ALTER TABLE SECURITY_LOGIN ADD CONSTRAINT pk_security_login PRIMARY KEY (CONTEXT,LOGIN);";
      jdbcTemplate.execute( createLoginkey );

      // Create the primary index
      String createLoginIndex = "CREATE UNIQUE INDEX IDX_SECURITY_LOGIN ON SECURITY_LOGIN(CONTEXT,NAME);";
      jdbcTemplate.execute( createLoginIndex );
    }

    if ( !tableset.contains( "SECURITY_ROLE" ) ) {
      LOG.info( "Creating SECURITY_ROLE table" );
      String createRoleTable = "CREATE TABLE SECURITY_ROLE ( CONTEXT VARCHAR(64) NOT NULL, ROLE VARCHAR(64) NOT NULL, DESCRIPTION VARCHAR(255) NOT NULL );";
      jdbcTemplate.execute( createRoleTable );

      String createRolekey = "ALTER TABLE SECURITY_ROLE ADD CONSTRAINT PK_SECURITY_ROLE PRIMARY KEY (CONTEXT, ROLE);";
      jdbcTemplate.execute( createRolekey );
    }

    if ( !tableset.contains( "SECURITY_ROLE_PERMISSION" ) ) {
      LOG.info( "Creating SECURITY_ROLE_PERMISSION table" );
      String createRolePermTable = " CREATE TABLE SECURITY_ROLE_PERMISSION ( CONTEXT VARCHAR(64) NOT NULL, ROLE VARCHAR(64) NOT NULL, TARGET VARCHAR(255) NOT NULL, PERMISSION BIGINT );";
      jdbcTemplate.execute( createRolePermTable );

      String createLoginkey = "ALTER TABLE SECURITY_ROLE_PERMISSION ADD CONSTRAINT PK_SECURITY_ROLE_PERMISSION PRIMARY KEY (CONTEXT, ROLE, TARGET);";
      jdbcTemplate.execute( createLoginkey );
    }

    if ( !tableset.contains( "SECURITY_LOGIN_ROLE" ) ) {
      LOG.info( "Creating SECURITY_LOGIN_ROLE table" );
      String createLoginRoleTable = "CREATE TABLE SECURITY_LOGIN_ROLE ( CONTEXT VARCHAR(64) NOT NULL, LOGIN BIGINT NOT NULL, ROLE VARCHAR(64) NOT NULL, FROM_DATE TIMESTAMP, THRU_DATE TIMESTAMP );";
      jdbcTemplate.execute( createLoginRoleTable );

      String createLoginRolekey = "ALTER TABLE SECURITY_LOGIN_ROLE ADD CONSTRAINT PK_SECURITY_LOGIN_ROLE PRIMARY KEY (CONTEXT, LOGIN, ROLE);";
      jdbcTemplate.execute( createLoginRolekey );
    }

    if ( !tableset.contains( "SECURITY_LOGIN_PERMISSION" ) ) {
      LOG.info( "Creating SECURITY_LOGIN_PERMISSION table" );
      String createLoginPermTable = "CREATE TABLE SECURITY_LOGIN_PERMISSION ( CONTEXT VARCHAR(64) NOT NULL, LOGIN BIGINT NOT NULL, TARGET VARCHAR(255) NOT NULL, PERMISSION BIGINT );";
      jdbcTemplate.execute( createLoginPermTable );

      String createLoginPermKey = "ALTER TABLE SECURITY_LOGIN_PERMISSION ADD CONSTRAINT pk_security_login_permission PRIMARY KEY (CONTEXT, LOGIN, TARGET);";
      jdbcTemplate.execute( createLoginPermKey );
    }

    if ( !tableset.contains( "SECURITY_LOGIN_REVOCATION" ) ) {
      LOG.info( "Creating SECURITY_LOGIN_REVOCATION table" );
      String createLoginRevTable = "CREATE TABLE SECURITY_LOGIN_REVOCATION ( CONTEXT VARCHAR(64) NOT NULL, LOGIN BIGINT NOT NULL, TARGET VARCHAR(255) NOT NULL, PERMISSION BIGINT );";
      jdbcTemplate.execute( createLoginRevTable );

      String createLoginRevKey = "ALTER TABLE SECURITY_LOGIN_REVOCATION ADD CONSTRAINT pk_security_login_revocation PRIMARY KEY (CONTEXT, LOGIN, TARGET);";
      jdbcTemplate.execute( createLoginRevKey );
    }

    LOG.info( "Security context initialized" );
  }




  /**
   * @see coyote.commons.security.GenericSecurityContext#createSession(coyote.commons.security.Login)
   */
  @Override
  public Session createSession( Login login ) {
    // TODO Auto-generated method stub
    return super.createSession( login );
  }




  /**
   * @see coyote.commons.security.GenericSecurityContext#createSession(java.lang.String, coyote.commons.security.Login)
   */
  @Override
  public Session createSession( String id, Login login ) {
    // TODO Auto-generated method stub
    return super.createSession( id, login );
  }




  /**
   * @see coyote.commons.security.GenericSecurityContext#getLoginByName(java.lang.String)
   */
  @Override
  public Login getLoginByName( String loginName ) {
    // TODO Auto-generated method stub
    return super.getLoginByName( loginName );
  }




  /**
   * @see coyote.commons.security.GenericSecurityContext#getLoginBySession(java.lang.String)
   */
  @Override
  public Login getLoginBySession( String sessionId ) {
    // TODO Auto-generated method stub
    return super.getLoginBySession( sessionId );
  }




  /**
  * @see coyote.commons.security.GenericContext#add(coyote.commons.security.Login)
  */
  @Override
  public void add( Login login ) {

    // Make sure we have a login
    if ( login != null ) {

      // Make sure the login has a principal
      if ( login.getPrincipal() != null ) {
        CredentialSet credentials = login.getCredentials();

        // Make sure there are credentials associated with the login
        if ( credentials != null ) {

          // If there is a login with these credentials, generate a warning 
          if ( credentials != null ) {

            // get the login name (the name of the principal)
            String principalName = login.getPrincipal().getName();

            // First we should see if there is already a login with this name 
            Login existing = retrieveLogin( principalName );

            if ( existing != null ) {
              LOG.warn( "A login with the name '" + principalName + "' already exists in the context '" + getName() + "' - login was not added" );
            } else {

              // add the login to the database
              String loginid = createLogin( login );

              // if there is a login id, the database insert was successful
              if ( loginid != null ) {
                // add the login into the cache
                super.add( login );
              } else {
                LOG.error( "Could not add the login to the context" );
              }
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

    final String insertSql = "insert into SECURITY_LOGIN (CONTEXT,NAME,PASSWORD,PARTY) values (?,?,?,?)";

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
          return null;
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
    Login retval = null;

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
        retval = work.get( 0 );
      }

      // populate the login with roles and permissions;
      retval = populateLogin( retval );
    }
    return retval;
  }




  /**
   * Populate the given login with roles and permissions.
   * 
   * <p>For most applications, it is enough to retrieve a login as all many 
   * components need is authentication. When the application wants to perform 
   * authorizations, however, there is a need to retrieve roles and permissions
   * and place them in the login for future authorization calls. This can be an 
   * expensive operation and consumes quite a bit of memory so it is best to 
   * only populate the logins with roles and permissions only when absolutely 
   * necessary.</p>
   * 
   * <p>If the given login is null, then a null will be returned.</p>
   * 
   * @param login
   * 
   * @return
   * 
   * @see #allows(Login, long, String)
   */
  private Login populateLogin( Login login ) {
    Login retval = null;

    if ( login != null ) {
      retval = login;

      try {
        long loginId = Long.parseLong( retval.getId() );

        // Get roles
        retval.addRoles( retrieveLoginRoles( loginId ) );

        // retrieve permissions for the login
        retval.addPermissions( retrieveLoginPermissions( loginId ) );

        // retrieve revocations for the login
        retval.revokePermissions( retrieveLoginRevocations( loginId ) );
      } catch ( NumberFormatException e ) {
        LOG.error( "Login ID '" + retval.getId() + "' was not valid", e );
      }

      // Even if there are no roles or permissions, we should place a 'nothing' 
      // role object in there so we don't get called again
      if ( !retval.hasRoles() ) {
        retval.addRole( NO_ROLE );
      }
    }

    return retval;
  }




  /**
   * @see coyote.commons.security.GenericSecurityContext#allows(coyote.commons.security.Login, long, java.lang.String)
   */
  @Override
  public boolean allows( Login login, long actions, String target ) {
    // we need to make sure the login is populated from the database first, see 
    // if it has any roles or permissions
    if ( login.getRoles() == null || login.getRoles().size() == 0 || login.getPermissions() == null || login.getPermissions().size() == 0 ) {
      // populate it
      populateLogin( login );
    }
    return super.allows( login, actions, target );
  }




  private List<Permission> retrieveRolePermissions( String role ) {
    List<Permission> retval = new ArrayList<Permission>();
    if ( role != null ) {
      final String SQL = "SELECT * FROM SECURITY_ROLE_PERMISSION WHERE CONTEXT = ? AND ROLE = ?";
      LOG.debug( SQL );
      retval = jdbcTemplate.query( SQL, new Object[] { getName().toLowerCase(), role.toLowerCase() }, new PermissionMapper() );
    }
    return retval;
  }




  private List<Role> retrieveLoginRoles( long loginId ) {
    List<Role> retval = new ArrayList<Role>();
    final String SQL = "SELECT * FROM SECURITY_LOGIN_ROLE WHERE CONTEXT = ? AND LOGIN = ?";
    LOG.debug( SQL );
    retval = jdbcTemplate.query( SQL, new Object[] { getName().toLowerCase(), loginId }, new RoleMapper() );

    return retval;
  }




  private List<Permission> retrieveLoginPermissions( long loginId ) {
    List<Permission> retval = new ArrayList<Permission>();
    final String SQL = "SELECT * FROM SECURITY_LOGIN_PERMISSION WHERE CONTEXT = ? AND LOGIN = ?";
    LOG.debug( SQL );
    retval = jdbcTemplate.query( SQL, new Object[] { getName().toLowerCase(), loginId }, new PermissionMapper() );
    return retval;
  }




  private List<Permission> retrieveLoginRevocations( long loginId ) {
    List<Permission> retval = new ArrayList<Permission>();
    final String SQL = "SELECT * FROM SECURITY_LOGIN_REVOCATION WHERE CONTEXT = ? AND LOGIN = ?";
    LOG.debug( SQL );
    retval = jdbcTemplate.query( SQL, new Object[] { getName().toLowerCase(), loginId }, new PermissionMapper() );
    return retval;
  }




  /**
   * This is the standard login retrieval point for logins.
   * 
   * @see coyote.commons.security.GenericSecurityContext#getLogin(java.lang.String, coyote.commons.security.CredentialSet)
   */
  @Override
  public Login getLogin( String name, CredentialSet creds ) {

    LOG.info( "Searching for login '" + name + "' with a password of '" + ByteUtil.bytesToHex( creds.getValue( CredentialSet.PASSWORD ), null ) + "'" );

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
      LOG.info( "Found login in cache -> " + retval );
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

    Login retval = null;

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
          retval = work.get( 0 );
        }

        // populate the login with roles and permissions;
        retval = populateLogin( retval );

      } else {
        LOG.warn( "No password found in credentials" );
      }
    }
    return retval;
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
    if ( role != null ) {
      if ( role.getName() != null ) {
        String name = role.getName();
        Role existing = retrieveRole( name );

        if ( existing != null ) {
          LOG.warn( "A role with the name '" + name + "' already exists in the context '" + getName() + "' - role was not added" );
        } else {
          if ( createRole( role ) ) {
            super.add( role );
            LOG.info( "Role created successfully" );
          } else {
            LOG.error( "Could not create role in context" );
          }

        }
      } else {
        LOG.warn( "Ignoring attempt to add role without a name" );
      }
    } else {
      LOG.warn( "Ignoring attempt to add a null role reference" );
    }
    super.add( role );
  }




  /**
   * @param name
   * @return
   */
  private Role retrieveRole( String name ) {
    // TODO Auto-generated method stub
    return null;
  }




  /**
   * @param role
   */
  private boolean createRole( final Role role ) {
    if ( role != null ) {
      final String insertRoleSql = "insert into SECURITY_ROLE (CONTEXT, ROLE, DESCRIPTION) values (?,?,?)";
      final String insertRolePermSql = "insert into SECURITY_ROLE_PERMISSION (CONTEXT, ROLE, TARGET, PERMISSION) values (?,?,?,?)";

      LOG.info( "Inserting new role (" + role.getName() + "-" + role.getDescription() + ") into " + getName() + " context" );

      // If there is a value for the password...
      if ( role.getName() != null && role.getName().trim().length() > 0 ) {

        try {
          jdbcTemplate.update( new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {

              PreparedStatement ps = connection.prepareStatement( insertRoleSql.toString(), Statement.RETURN_GENERATED_KEYS );
              ps.setString( 1, getName().toLowerCase() ); //context
              ps.setString( 2, role.getName().toLowerCase() ); // name
              ps.setString( 3, role.getDescription() ); // description
              return ps;
            }
          } );
        } catch ( DataAccessException e ) {
          LOG.error( "Could not create role: " + e.getMessage() );
          return false;
        }

        // now we have to insert any permissions in the role

        List<Permission> perms = role.getPermissions();
        for ( final Permission perm : perms ) {

          try {
            jdbcTemplate.update( new PreparedStatementCreator() {
              public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {

                PreparedStatement ps = connection.prepareStatement( insertRolePermSql.toString(), Statement.RETURN_GENERATED_KEYS );
                ps.setString( 1, getName().toLowerCase() ); //context
                ps.setString( 2, role.getName().toLowerCase() ); // name
                ps.setString( 3, perm.getTarget() ); // target of the permission
                ps.setLong( 4, perm.getAction() );
                return ps;
              }
            } );
          } catch ( DataAccessException e ) {
            LOG.error( "Could not record permission target '" + perm.getTarget() + "' for role '" + role.getName() + "' : " + e.getMessage() );
            return false;
          }

        }

        return true;
      } else {
        LOG.error( "Empty role name - role not added" );
      }
    } else {
      LOG.error( "Null role cannot be added to context" );
    }
    return false;
  }

  /**
   * Maps a login record to a simple Login record with a security principal and 
   * a credential set.
   */
  public final class LoginMapper implements RowMapper<Login> {

    @Override
    public Login mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
      final Login retval = new Login();

      retval.setId( rs.getString( "LOGIN" ) );
      retval.setPrincipal( new GenericSecurityPrincipal( rs.getString( "PARTY" ), rs.getString( "NAME" ) ) );
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

  /**
   * Map a resultset row into a permission
   */
  public final class PermissionMapper implements RowMapper<Permission> {

    @Override
    public Permission mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
      final Permission retval = new Permission( rs.getString( "TARGET" ), rs.getLong( "PERMISSION" ) );
      return retval;
    }
  }

  /**
   * Map a resultset row to a Role object.
   */
  public final class RoleMapper implements RowMapper<Role> {

    @Override
    public Role mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
      final Role retval = new Role( rs.getString( "ROLE" ), rs.getString( "DESCRIPTION" ) );
      return retval;
    }
  }

}
