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

import coyote.commons.Version;
import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuIcon;
import coyote.commons.feature.MenuLocation;
import coyote.commons.feature.MenuSection;
import coyote.commons.feature.ToDo;


/**
 * 
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
  }

}
