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
package coyote.commons.feature;

/**
 * A specialized feature which acts as a place-holder for a future feature
 */
public class ToDo extends Feature {

  /**
   * Create a ToDo item with and identifier and a description.
   * 
   * @param id an identifying string
   * @param desc a description of the task to perform.
   */
  public ToDo( String id, String desc ) {
    description = desc;
  }




  /**
   * Construct a ToDo item with a description.
   * 
   * @param desc a description of the task to perform.
   */
  public ToDo( String desc ) {
    this( null, desc );
  }

}
