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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.webapp.desc.LoginFeature;

import coyote.commons.feature.FeatureList;


/**
 * This is the main mechanism to enable and disable features of the system.
 * 
 * <p>It is possible to configure the capabilities of the system through this 
 * class.</p>
 */
@Configuration
public class FeatureConfig {

	private final FeatureList featureList = new FeatureList();




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
	 * @return The list of features in the application.
	 */
	@Bean
	public FeatureList initFeatureList() {

		featureList.add(new LoginFeature());
		return featureList;
	}

}
