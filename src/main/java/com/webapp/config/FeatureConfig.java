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
package com.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.webapp.desc.WebApp;

import coyote.commons.feature.SystemDescription;


/**
 * This is the main mechanism to enable and disable features of the system.
 * 
 * <p>It is possible to configure the capabilities of the system through this 
 * class.</p>
 */
@Configuration
public class FeatureConfig {

  /** Create a new WebApp object which describes the current state of this system. */
  private final WebApp system = new WebApp();

  @Autowired
  public void setMessageSource( ResourceBundleMessageSource source ) {
    system.setMessageSource(source);
  }



  /**
   * This creates a bean accessible to all the components in the system which
   * which lists and describes the feature of the system.
   * 
   * <p>This list of features is used to generate navigational views of the 
   * system and other important development functions.</p>
   * 
   * <p>As the system is evolved, new features are added to this list and 
   * subsequently appear on menus.</p>
   * 
   * <p>Do not change the name of this method, as it is used by other Spring-
   * aware components in locating the system description bean in the 
   * application context. If this method name must be changed, you may want to 
   * consider changing {@code WebApp.SYSTEM_DESCRIPTION} as well.</p>
   * 
   * @return The list of features in the application.
   */
  @Bean
  public SystemDescription systemDescription() {
    return system;
  }

}
