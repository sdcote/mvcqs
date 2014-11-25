package com.webapp.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import coyote.commons.security.Context;
import coyote.commons.security.CredentialSet;
import coyote.commons.security.GenericContext;
import coyote.commons.security.Login;
import coyote.commons.security.Permission;
import coyote.commons.security.Role;

/**
 * This is where we configure the security context for this web application.
 * 
 * <p>This uses the Coyote Commons security context for highly configurable
 * permissions based access control.</p>
 */
@Configuration
public class SecurityConfig {
	private static final Log LOG = LogFactory.getLog(SecurityConfig.class);

	DataSource dataSource = null;




	@Autowired
	public void setDataSource(DataSource source) {
		dataSource = source;
		LOG.info("DataSource wired");
	}




	@Bean
	public Context securityContext() {

		if (dataSource == null) {
			LOG.fatal("No datasource configured, cannot persist security context!");
		}
		
		
		// Create a generic security context
		Context context = new GenericContext("Demo");

		
		// Load the context from the data store
		loadContext(context,dataSource);

		// Make sure there is an admin role and user in the context
		
		// Add some roles to the context
		Role role = new Role("ADMIN");

		// specify global "user" permissions for this role
		role.addPermission(new Permission("USER", Permission.ALL));

		// add the role to the context
		context.add(role);

		// Add some logins to the context
		Login login = new Login(new CredentialSet("admin", "secret"));
		// TODO: Make these configurable from system properties

		// add a role to the login
		login.addRole("ADMIN");

		// Add the login to the context
		context.add(login);

		// Return the newly built security context
		return context;
	}




	/**
	 * Load the given context with the data in the given data source.
	 * 
	 * @param context
	 * @param source
	 */
	private void loadContext(Context context, DataSource source) {

		JdbcTemplate jdbcTemplate = new JdbcTemplate( source );		
		
		
	}

}
