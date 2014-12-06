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

/**
 * Allows users to manage user their own login account information.
 * 
 * <p>This feature allows users to edit the meta data relating to their login 
 * accounts. This data is used by the system to control the way the system 
 * operates for the user. For example, the user can change the time zone and 
 * locale settings to alter the way the system presents data to the user.</p>   
 */
public class LoginProfileFeature extends Feature {

  public LoginProfileFeature() {
    version = new Version( 1, 0, Version.GENERAL );
    since = new Version( 1, 0, Version.GENERAL );
    name = "userprofile";
    featureId = "169";
    description = "Allows users to manage user their own login account information.";
    link = "/do/userProfile";
    setIcon( MenuIcon.USER );

  }
}
