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
package com.webapp.desc.security;

import coyote.commons.Version;
import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuIcon;
import coyote.commons.feature.ToDo;

/**
 * This is the home page for the login account.
 * 
 * <p>All information on this page relates to the login account. This is 
 * normally the landing page for the account after a successful login and there 
 * are no other forwards or redirects pending.</p>
 * 
 * <p>This feature is different from the Login Settings feature in that this 
 * feature is primarily a display-only page which provides links to other 
 * portions of the system. The Login Settings feature is where the login is 
 * managed and the way the application interacts with the login is defined.</p>
 */
public class LoginProfileFeature extends Feature {

  public LoginProfileFeature() {
    version = new Version( 1, 0, Version.GENERAL );
    since = new Version( 1, 0, Version.GENERAL );
    name = "loginprofile";
    featureId = "169";
    description = "Displays information related to the login account and provides login-related links to other parts of the application. This is the home page for the account in the application.";
    link = "/do/loginProfile";
    setIcon( MenuIcon.USER );

  }
}
