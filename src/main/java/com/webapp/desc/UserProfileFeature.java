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


public class UserProfileFeature extends Feature {

  public UserProfileFeature() {
    version = new Version( 1, 0, Version.GENERAL );
    since = new Version( 1, 0, Version.GENERAL );
    name = "userprofile";
    featureId = "169";
    description = "Allows the user to manage user identity and demographic information. The user profile describes the identity of the user.";
    link = "/do/userProfile";
    

  }
}
