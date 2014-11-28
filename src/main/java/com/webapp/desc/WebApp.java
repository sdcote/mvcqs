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
import coyote.commons.feature.SystemDescription;


/**
 * This is the description of this web application.
 * 
 * <p>This class is used to keep track of the current state of this systems 
 * evolution. It ties in all the features modeled in this package to provide a
 * structure which can be queried by system components to ensure uniform and 
 * consistent operation.</p>
 * 
 * <p>The system description is used, for example, to generate navigational 
 * views into each of the supported features. The attributes of each feature 
 * are used to generate labels, tool tips, help and relative navigational links 
 * to other features of the system.</p>
 * 
 * <p>This class and its associated features are also used in the development 
 * of the system. The teams definition of done (DoD) often includes the 
 * creation of documentation relating to the current state of a system. By 
 * coding these details into the system artifacts, all the details are 
 * committed to the code repository and made available to all members of the 
 * team and to automated build and deployment tools. Keeping this information 
 * close to the code has proven very useful and a pest practice for effective 
 * agile teams.</p>   
 */
public class WebApp extends SystemDescription {

  public WebApp() {
    version = new Version( 1, 0, 0, Version.DEVELOPMENT );
    name="webapp";
    description = "This is a fully-functional web application from which developers can begin prototyping their own application in a matter of seconds. It is a starting point for prototypes, utilities and proof of concept systems which can run on the desktop for individual use or exposed to all members of the team or organization.";

    // We are providing security features
    Feature security = new SecurityTheme();
    addTheme( security );
    
    // User profile management
    Feature profile = new UserProfileTheme();
    addTheme( profile );

  }

}
