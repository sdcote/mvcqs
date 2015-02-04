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
package com.webapp.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.webapp.security.SecurityDataContext;

import coyote.commons.security.Login;
import coyote.commons.security.Permission;
import coyote.commons.security.Role;
import coyote.commons.security.SecurityContext;


/**
 * This is where we configure the security context for this web application.
 * 
 * <p>This uses the Coyote Commons security context for highly configurable
 * permissions based access control.</p>
 */
@Configuration
public class SecurityConfig {
  private static final Log LOG = LogFactory.getLog( SecurityConfig.class );

  DataSource dataSource = null;




  @Autowired
  public void setDataSource( DataSource source ) {
    dataSource = source;
    LOG.info( "DataSource wired" );
  }




  @Bean
  public SecurityContext securityContext() {

    if ( dataSource == null ) {
      LOG.fatal( "No datasource configured, cannot persist security context!" );
    }

    // Create a generic security context
    SecurityDataContext context = new SecurityDataContext( "mvcqs" );

    context.setDataSource( dataSource );

    try {
      context.init();
    } catch ( Exception e ) {
      LOG.error( "Could not initialize security context data store", e );
    }

    // Make sure there is an admin role and user in the context
    if ( context.getRole( "ADMIN" ) == null ) {
      Role adminRole = new Role( "ADMIN", "System Administrator" );

      // specify global "user" permissions for this role
      adminRole.addPermission( new Permission( "ADMIN", Permission.ALL ) );

      // add the role to the context
      context.add( adminRole );
    }

    // Add the Operations role if necessary
    if ( context.getRole( "OPER" ) == null ) {
      Role operatorRole = new Role( "OPER", "System Operator" );
      operatorRole.addPermission( new Permission( "OPER", Permission.ALL ) );
      context.add( operatorRole );
    }

    // Add the normal user role and login if necessary
    if ( context.getRole( "USER" ) == null ) {
      Role userRole = new Role( "USER", "System User" );
      userRole.addPermission( new Permission( "DOCUMENT", Permission.READ ) );
      userRole.addPermission( new Permission( "TICKET", Permission.READ | Permission.CREATE ) );
      context.add( userRole );
    }

    // Once we define roles in the context, we can generate some logins which 
    // use those roles for authorization

    // Create an administrator
    if ( context.getLoginByName( "admin" ) == null ) {
      Login login = new Login( "admin", "secret" );
      login.addRole( context.getRole( "ADMIN" ) ); // System administration role
      context.add( login );
    }

    // Create an operator login
    if ( context.getLoginByName( "oper" ) == null ) {
      Login login = new Login( "OPER", "secret" );
      login.addRole( context.getRole( "OPER" ) );
      context.add( login );
    }

    // Create a user login
    if ( context.getLoginByName( "user" ) == null ) {
      Login login = new Login( "USER", "secret" );
      login.addRole( context.getRole( "USER" ) );
      context.add( login );
    }

    // Return the newly built security context
    return context;
  }

}
