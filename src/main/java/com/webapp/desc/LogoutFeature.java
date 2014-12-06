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


public class LogoutFeature extends Feature {

  public LogoutFeature() {
    version = new Version( 1, 0, 0, Version.DEVELOPMENT );
    name = "logout";
    link = "/do/logout";
    description = "Deactivates the current authenticated session.";
    setIcon( MenuIcon.SIGN_OUT );
  }
}
