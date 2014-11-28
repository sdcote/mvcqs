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

/**
 * 
 */
public class UserProfileTheme extends Feature {
  UserProfileTheme(){
  version = new Version( 1, 0, 0, Version.DEVELOPMENT );
  name="profile";
  description = "Manage settings related to the user login of a system.";
  }

}
