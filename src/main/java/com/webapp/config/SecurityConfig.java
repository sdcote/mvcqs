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

import coyote.commons.security.CredentialSet;
import coyote.commons.security.GenericSecurityPrincipal;
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
    Role role = new Role( "ADMIN" );

    // specify global "user" permissions for this role
    role.addPermission( new Permission( "USER", Permission.ALL ) );

    // add the role to the context
    context.add( role );

    // Add some logins to the context
    Login login = new Login( "admin", "secret" );
    // TODO: Make these configurable from system properties

    // add some roles to the login
    login.addRole( "ADMIN" ); // System administration permissions
    login.addRole( "OPER" ); // System operations permissions
    login.addRole( "USER" ); // Regular user permissions

    // Add the login to the context
    context.add( login );

    // Add the Operations role and login
    role = new Role( "OPER" );
    role.addPermission( new Permission( "OPER", Permission.ALL ) );
    context.add( role );
    login = new Login( "OPER", "secret" );
    login.addRole( "OPER" );
    context.add( login );

    // Add the normal user role and login
    role = new Role( "USER" );
    role.addPermission( new Permission( "USER", Permission.ALL ) );
    context.add( role );
    login = new Login( "USER" , "secret" );
    login.addRole( "USER" );
    context.add( login );

    // Return the newly built security context
    return context;
  }

}
