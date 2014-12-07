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

import com.webapp.desc.logintools.LoginSettingsFeature;

import coyote.commons.Version;
import coyote.commons.feature.Feature;


/**
 * 
 */
public class LoginToolsTheme extends Feature {
  LoginToolsTheme() {
    version = new Version( 1, 0, 0, Version.DEVELOPMENT );
    name = "profile";
    description = "Manage settings related to the user login of the system.";
    
    
    addFeature( new LoginSettingsFeature() );
    //addFeature( new LoginMessageInboxFeature() ); // Message Inbox
    //addFeature( new LoginMessageSendFeature() ); // Send Messages
    //addFeature( new LoginEULAFeature() ); // accept EULA & Terms
    //addFeature( new LoginAlertFeature() ); // events and notifications
    //addFeature( new LoginTaskFeature() ); // Simple task management
  }

}
