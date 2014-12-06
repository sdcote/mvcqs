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
package com.webapp.misc;

import coyote.commons.Version;
import coyote.commons.feature.Feature;


public class I18nFeature extends Feature {

  public I18nFeature() {
    version = new Version( 1, 0, Version.GENERAL );
    since = new Version( 1, 0, Version.GENERAL );
    name = "internationalization";
    featureId = "122";
    description = "System supports the ability to display data in a locale specific manner";
    link = null; // This is not a user accessible feature

  }
}
