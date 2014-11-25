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
package com.webapp.security;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import coyote.commons.security.Context;
import coyote.commons.security.GenericContext;
import coyote.commons.security.Login;
import coyote.commons.security.Role;


/**
 * This is a security context backed by a data store.
 * 
 * <p>The main functionality is provided by the GenericContext superclass. This 
 * class has a few overrides which allow it to capture changes to the security
 * context data model and persist those changes to a database.<p>
 */
public class SecurityDataContext extends GenericContext implements Context {

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
  public void init() throws Exception{
    // Get a listing of the tables in this data store
    Set<String> tableset = new HashSet<String>();
    DatabaseMetaData md = dataSource.getConnection().getMetaData();
    ResultSet rs = md.getTables( null, null, "%", null );
    while ( rs.next() ) {
      tableset.add( rs.getString( 3 ) );
    }

    // Check to see if the needed tables exist and create them if necessary
    if ( !tableset.contains( "SECURITY_CONTEXT" ) ) {
      
    }
    
    LOG.info( "Security context initialized" );
  }




  /**
   * @see coyote.commons.security.GenericContext#add(coyote.commons.security.Login)
   */
  @Override
  public void add( Login login ) {
    // TODO Add the login to the database
    super.add( login );
  }




  /**
   * @see coyote.commons.security.GenericContext#add(coyote.commons.security.Role)
   */
  @Override
  public void add( Role role ) {
    // TODO Add the role to the database
    super.add( role );
  }

  
  
}
