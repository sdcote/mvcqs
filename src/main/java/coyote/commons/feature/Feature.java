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
 * This is a class which models a feature in the system.
 * 
 * <p>Its purpose is to provide a way for the development team to code the 
 * current state of a system feature and allow the system to interact with the 
 * concept of the features functionality.</p>
 * 
 * <p>Features can be used to programmatically display the current features of 
 * the system in UI components or in reports.</p>
 * 
 * <p>Features can drive the creation of navigational components. Dynamic menus 
 * can be generated programmatically through the feature list.</p>
 * 
 * <p>Features provide a hierarchy, allowing child features to inherit from its 
 * parent. This makes generating hierarchical navigation simple and consistent. 
 * The print feature, for example, is always represented under the File feature
 * no matter where it appears in the system or reports.</p>
 * 
 * <p>The system can base its operation on the state of a feature. Consider a 
 * set of features which are in development and the desire for the system to be 
 * run in a 'test' mode which enables these features, or in a production mode 
 * which does not allow these features.</p>
 * 
 * <p>Security models can be driven from this list of features. Roles can be 
 * generated in the system based on the needs of the feature. If the feature 
 * requires the "operator" role, then the security context can query the 
 * systems list of features and determine all the roles the system expects to 
 * have.</p>
 * 
 * <p>The feature can be used to generate a list of resources expected of the 
 * system. By scanning through all the features a system can generate message 
 * resource lists.</p>
 * 
 * <p>Features can be used to control which parts of a system should be enabled 
 * based on the licensing provided. This allows a system to operate in a trial 
 * mode until the proper license key is provided.</p>
 * 
 * <p>Features can be enabled for a particular time interval. Each feature can 
 * have a start and end time which controls how the feature is offered to the 
 * system users.</p>
 */
public abstract class Feature {

}
