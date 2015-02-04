package com.webapp.desc;

import coyote.commons.Version;
import coyote.commons.feature.Feature;
import coyote.commons.feature.MenuLocation;
import coyote.commons.feature.MenuSection;
import coyote.commons.feature.ToDo;


public class SearchTheme extends Feature {
  SearchTheme() {
    version = new Version( 1, 0, 0, Version.EXPERIMENTAL );
    name = "search";
    description = "The ability of the system to search various static content and dynamic data.";
    addLocation( new MenuLocation( MenuSection.LEFT, 1 ) );

    addToDo( new ToDo( "Develop a component API which allows agents to search business objects and dynamic data." ) );
    addToDo( new ToDo( "Develop a component to search static content." ) );
  }
}
