package com.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Bean
	public Context securityContext() {
		// Create a generic security context
		Context context = new GenericContext("Demo");

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

}
