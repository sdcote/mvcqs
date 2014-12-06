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
package com.webapp.desc.logintools;

import coyote.commons.Version;
import coyote.commons.feature.Feature;
import coyote.commons.feature.ToDo;


/**
 * Allows users to manage application settings related to their login accounts.
 * 
 * <p>This data is used by the system to control the way the system operates 
 * for the logged-in user. For example, the user can change the time zone and 
 * locale settings to alter the way the system presents data while logged-in.</p>
 * 
 * <p>This is different from the Login Profile Feature in that Settings 
 * relating to user configuration of the login and the Profile is a display-
 * only view. The profile is a landing page which shows data about the login 
 * and provides links to other portions of hte application.</p>
 * 
 * <p>It is expected that privacy control is performed through this page.</p>
 */
public class LoginSettingsFeature extends Feature {

  public LoginSettingsFeature() {
    version = new Version( 1, 0, Version.GENERAL );
    since = new Version( 1, 0, Version.GENERAL );
    name = "loginsettings";
    featureId = "165";
    description = "Allows the user to set application configuration attributes to their login. The user can configure how the system operates for them.";
    link = "/do/loginSetting";

    addToDo( new ToDo("Privacy settings are controlled from this feature/page.") );

  }
}
