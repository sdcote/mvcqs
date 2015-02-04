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

import com.webapp.desc.security.LoginFeature;
import com.webapp.desc.security.LoginProfileFeature;
import com.webapp.desc.security.LogoutFeature;

import coyote.commons.Version;
import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuIcon;
import coyote.commons.feature.MenuLocation;
import coyote.commons.feature.MenuSection;
import coyote.commons.feature.ToDo;


/**
 * This is a grouping of security features.
 * 
 * <p>Since this feature has a list of children, this feature is a composition, 
 * and has no link of its own. It serves as an aggregation of other features.
 * Usually only childless features have links.</p>
 */
public class SecurityTheme extends Feature {
  SecurityTheme() {
    version = new Version( 1, 0, 0, Version.DEVELOPMENT );
    name = "security";
    description = "The monitoring and management of authentication, authorization and auditing functions in the system.";
    addLocation( new MenuLocation( MenuSection.LEFT, 99 ) );
    icon = MenuIcon.LOCK;

    addToDo( new ToDo( "Allow concurrent logins for a single login" ) );
    addToDo( new ToDo( "Configure concurrent logins by login" ) );
    addToDo( new ToDo( "Configure system-wide concurrent logins" ) );
    addToDo( new ToDo( "System-wide concurrent logins can be disabled for particular logins" ) );

    addFeature( new LoginFeature() );
    addFeature( new LogoutFeature() );
    addFeature( new LoginProfileFeature() );
    // addFeature( new FindLoginFeature() );
    // addFeature( new AddLoginFeature() );
    // addFeature( new ChangeLoginFeature() );
    // addFeature( new DeleteLoginFeature() );
    // addFeature( new FindRoleFeature() );
    // addFeature( new AddRoleFeature() );
    // addFeature( new ChangeRoleFeature() );
    // addFeature( new DeleteRoleFeature() );
    // addFeature( new GrantRolePermissionFeature() );
    // addFeature( new RevokeRolePermissionFeature() );
    // addFeature( new GrantLoginPermissionFeature() );
    // addFeature( new RevokeLoginPermissionFeature() );
    // addFeature( new GrantRolePermissionFeature() );
    // addFeature( new ChangeCredentialFeature() );
  }

}
