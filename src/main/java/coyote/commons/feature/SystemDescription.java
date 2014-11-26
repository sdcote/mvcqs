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
 * This is the root of all features and provides the starting point for 
 * discovering everything about how the functionality of the system is 
 * structured.
 * 
 * <p>This class assists with the programmatic creation of navigational 
 * elements such as menus. By starting with instances of this class, the system 
 * can generate the top level menu items and by traversing each item, generate 
 * a complete menu tree.</p>
 * 
 * <p>Modeling agile product management processes, this class starts with a 
 * list of high-level features called themes. These feature often do not 
 * represent atomic functionality but act as an aggregation of features. Themes
 * such as User Management, Reporting and Configuration are often the top level
 * menu items for many applications and can serve as such.</p> 
 * 
 * <p>Some may recognize a pattern from the early days of Java AWT where system 
 * action are constructed so as to allow the system to display UI components.
 * This class borrows from that pattern and applies it to other applications 
 * and development practice.</p>
 * 
 * <p>Product Owners and Product Managers have been known to generate strategic 
 * inventories of themes and features for their product based on client and 
 * market needs. The development team often does not have a clear picture of 
 * these themes and features as a whole and often make decistions without a 
 * contextual understanding of the product as a whole. Some development teams 
 * work with the product owners/managers and develop a model of the product 
 * during backlog refinement meetings and update this model in code so as to 
 * have not only a current copy but the revision history of all the features of 
 * the project.</p>
 * 
 * <p>This practice also assists with remembering the current state and plans 
 * of projects placed on hold. Often Proof of Concept systems are developed and 
 * shelved as company priority change. When market changes cause these PoC 
 * systems to be revisited, it is often difficult to ascertain the current 
 * state of the system. By storing this information with the code artifacts in 
 * the repository, the development team can run a few reports from the system 
 * or simply scan the feature classes to determine the current state of the 
 * project and quickly begin development with less time spent becoming familiar 
 * with the system.</p> 
 */
public class SystemDescription extends Feature {

  /** This is a list of top level features of the system. */
  private FeatureList themes = new FeatureList();




  protected void addTheme( Feature feature ) {
    themes.add( feature );
  }

}
