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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import coyote.commons.Version;

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
 * resource lists for developers and translators.</p>
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

	private static final String DISPLAYNAME = "displayname";

	/** The parent feature */
	protected Feature parent = null;

	/** The resource name for the display name of the feature. It is also used as the root for other resource keys such as tooltip and description. */
	protected String name = null;

	/** The reference link to use when generating links to this feature's view */
	protected String link = null;

	/** The internal identifier for this feature. This is usually the requirement ID or story card ID used during the development process. This is useful when generating  release notes. */
	protected String featureId = null;

	/** The description of the feature. Used in generating release notes, it is often the body of the requirement or story card. */
	protected String description = null;

	/** The version of the product when this feature was introduced. */
	protected Version since = null;

	/** The version of the feature. This is incremented each time the feature is significantly updated. */
	protected Version version = null;

	/** The reference link to the help documentation for this feature. */
	protected String helpLink = null;

	/** The level of licensing required for this feature to be accessed. No licensing = '0' (zero). */
	protected int licenseLevel = 0;

	/** The list of roles expected to access this feature. */
	protected final List<String> roles = new ArrayList<String>();

	protected final List<Feature> children = new ArrayList<Feature>();

	/** The list of menu locations this feature appears. */
	protected final List<MenuLocation> locations = new ArrayList<MenuLocation>();




	/**
	 * @param feature
	 */
	public void setParent(Feature feature) {
		parent = feature;
	}




	/**
	 * @param child
	 */
	protected void removeChild(Feature child) {
		children.remove(child);
	}




	/**
	 * @param child the child to add;
	 */
	public void addFeature(Feature child) {

		if (child != null) {
			child.setParent(this);
			for (Feature feature : children) {
				if (feature == child)
					return;
			}
			children.add(child);
		}
	}




	/**
	 * @return
	 */
	public String getName() {
		if (name != null)
			return name;
		else
			return "UNNAMED";
	}




	/**
	 * @return
	 */
	public String getLink() {
		if (link != null)
			return link;
		else
			return "#";
	}




	public String getVersion() {
		if (version != null)
			return version.toString();
		else
			return "0.0";
	}




	/**
	 * Retrieve the feature with the given name.
	 * 
	 * <p>Performs a breadth first search for feature with the given name. There 
	 * are no guarantees as to the order of searching, but normally the search 
	 * follows the order if the features as they were added.</p>
	 *  
	 * @param name The name of the feature for which to search.
	 * 
	 * @return The feature with the given name or null if a feature with that name was not found.
	 */
	public Feature getFeature(String name) {
		Feature retval = null;
		if (name != null) {
			// search the top level of features (breadth first)
			for (Feature feature : children) {
				if (name.equals(feature.getName())) {
					retval = feature;
					break;
				} // if name matches
			} // for each

			// now search the next level
			if (retval == null) {
				// Try searching the children
				for (Feature child : children) {
					retval = child.getFeature(name);
					if (retval != null) {
						break;
					}
				} // for each
			} // if retval==null

		} // if name !null

		return retval;
	}




	/**
	 * Return the name of the system appropriate for the given locale
	 * 
	 * @param locale The locale requesting the name
	 * 
	 * @return Locale specific name of the system.
	 */
	public Object getDisplayName(Locale locale) {
		return getMessage(name + '.' + DISPLAYNAME, null, locale);
	}




	/**
	 * Resolve the given code and arguments as message in the given Locale, 
	 * returning the value of the given key if not found.
	 * 
	 * <p>This implementation simply delegates the call to its parent if set. 
	 * The reason for this is that each feature may not want to manage its own 
	 * resource bundles and let the root handle all resource message lookups 
	 * and caching of messages and bundles. This works because all features are 
	 * part of one global root feature or {@code SystemDescription} which is 
	 * often application specific and manages it resource bundles according to 
	 * the applications frameworks and APIs.</p>
	 * 
	 * <p>While the default {@code SystemDescription} class does not override 
	 * this method, it is left up to application-specific subclasses to override 
	 * this method and use the most appropriate approach to resolve messages.</p> 
	 *  
	 * @param key the key of the message to lookup up, such as 'printer.notfound'
	 * @param args array of arguments that will be filled in for parameters 
	 * within the message
	 * @param locale the Locale in which to do the lookup
	 * 
	 * @return the resolved message, or the key if not found
	 */
	public String getMessage(String key, Object[] args, Locale locale) {
		if (parent != null) {
			return parent.getMessage(key, args, locale);
		} else {
			return key;
		}

	}




	/**
	 * Add a location where this feature is to appear in the system menus.
	 * 
	 * <p>This exposes a feature to the user through the menus by specifying 
	 * the menu section in which it is to appear and the sequence in that 
	 * section in relation to other features.</p>
	 * 
	 * <p>Features can appear in many different locations so this method simply 
	 * adds to the existing list of menu locations.</p>
	 * 
	 * @param menuLocation The menu location to add
	 */
	protected void addLocation(MenuLocation menuLocation) {
		locations.add(menuLocation);
	}




	/**
	 * Perform a check if the given section location matches any of this 
	 * features menu locations.
	 * 
	 * <p>This is a convenience method which wraps a call to 
	 * <pre>getLocationBySection(section) != null</pre></p>
	 * 
	 * @param section The section to query.
	 * 
	 * @return True if this feature is to appear in the given section, false 
	 * otherwise. False is also returned if the argument passed is null.
	 * 
	 * @see #getLocationBySection(MenuSection)
	 */
	public boolean isLocatedIn(MenuSection section) {
		return (getLocationBySection(section) != null);
	}




	/**
	 * Retrieve the location which matches the given section.
	 * 
	 * <p>This method can be called to determine if this feature is supposed to 
	 * appear in the given section.</p? 
	 * 
	 * @param section The section to query.
	 * 
	 * @return the first location in this feature matching the given section, 
	 * {@code null} otherwise. {@code null} is also returned if the argument 
	 * passed is {@code null}.
	 */
	public MenuLocation getLocationBySection(MenuSection section) {
		if (section != null) {
			for (MenuLocation location : locations) {
				if (section == location.getSection()) {
					return location;
				}
			}
		}
		return null;
	}




	/**
	 * Return a list of child features which match the given section.
	 * 
	 * @param section The menu section to match
	 * 
	 * @return a list of sequenced child features which match the given section 
	 */
	public List<Feature> getFeaturesBySection(final MenuSection section) {
		List<Feature> retval = new ArrayList<Feature>();

		for (Feature feature : children) {
			if (feature.isLocatedIn(section)) {
				retval.add(feature);
			}
		}

		// Sort the list
		Collections.sort(retval, new Comparator<Feature>() {

			public int compare(Feature feature1, Feature feature2) {

				MenuLocation location1 = feature1.getLocationBySection(section);
				MenuLocation location2 = feature2.getLocationBySection(section);

				// ascending order
				return location1.compareTo(location2);

				// descending order
				// return location2.compareTo(location1);
			}
		});

		return retval;
	}
	
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Feature: ");
		b.append(name);
		b.append(" version: v");
		b.append(version);
		return b.toString();
	}

}
