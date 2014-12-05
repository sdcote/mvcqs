package com.webapp.desc;

import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuLocation;
import coyote.commons.feature.MenuSection;

/**
 * This describes a feature we are adding to this application.
 * 
 * <p>The process for add a feature to this framework is as simple as creating 
 * a Feature like this and telling the framework where it is to appear in the 
 * application through the use of MenuLocation references.</p>
 * 
 * <p>Next, create a Controller, to add an endpoint to an existing controller.
 * See {@code HelloWorldController.java} for an example. It provides the data 
 * for the model.</p>
 * 
 * <p>Finally, create the view (see {@code helloworld.jsp}) that will be used 
 * to display the model.</p> 
 */
public class HelloWorldFeature extends Feature {

	public HelloWorldFeature() {

		// Name the feature
		name = "helloworld";

		// brief optional description
		description = "Just displays the HelloWorld page.";

		// this maps to our HelloWorld controller
		link = "/do/helloWorld";

		// add it to the left side menu in no particular sequence
		addLocation(new MenuLocation(MenuSection.LEFT));
	}
}
