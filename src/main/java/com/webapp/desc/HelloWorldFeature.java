package com.webapp.desc;

import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuIcon;
import coyote.commons.feature.MenuLocation;
import coyote.commons.feature.MenuSection;
import coyote.commons.feature.MockFeature;


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
 * 
 * <p>OK, there might be a little more than that. The name of the feature will 
 * not resolve to a Locale-specific string until it is placed in the 
 * appropriate message_*_*.properties file. But this is a minor tweak.</p>
 */
public class HelloWorldFeature extends Feature {

  public HelloWorldFeature() {

    // Name the feature
    setName( "helloworld" );

    // brief optional description
    setDescription( "Just displays the HelloWorld page." );

    // this maps to our HelloWorld controller
    setLink( "/do/helloWorld" );

    // Set the type of icon to be displayed on the menu
    setIcon( MenuIcon.COGS );

    // add this feature to the left side menu
    addLocation( new MenuLocation( MenuSection.LEFT, 1 ) );

    // Test multi-level menus
    Feature level2 = new MockFeature( "Level2.1", MenuIcon.COGS, new MenuLocation( MenuSection.LEFT ) );
    addFeature( level2 );
    level2 = new MockFeature( "Level2.2", MenuIcon.COGS, new MenuLocation( MenuSection.LEFT ) );
    addFeature( level2 );
    level2 = new MockFeature( "Level2.3", MenuIcon.COGS, new MenuLocation( MenuSection.LEFT ) );
    addFeature( level2 );

    Feature level3 = new MockFeature( "Level3.1", MenuIcon.COGS, new MenuLocation( MenuSection.LEFT ) );
    level2.addFeature( level3 );
    level3 = new MockFeature( "Level3.2", MenuIcon.COGS, new MenuLocation( MenuSection.LEFT ) );
    level2.addFeature( level3 );
    level3 = new MockFeature( "Level3.3", MenuIcon.COGS, new MenuLocation( MenuSection.LEFT ) );
    level2.addFeature( level3 );

  }
}
