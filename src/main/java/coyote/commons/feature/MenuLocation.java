package coyote.commons.feature;

/**
 * Describes where a feature is to appear in the menus of an application.
 * 
 * <p>The section is a generic menu location, normally TOP,LEFT,RIGHT or 
 * BOTTOM. By default, menu sections are HIDDEN, meaning they will not be 
 * placed on any of the 4 menu sections.</p>
 *  
 * <p>The sequence specifies the order in which they appear. So for all the 
 * features returned for the TOP section, they will be placed in order of their 
 * sequence.NOTE: Sequences should be positive to ensure the 
 * (@code compareTo(MenuLocation)} works properly.</p>
 * 
 * <p>Multi-layered menus are created by scanning the features children and for 
 * all the menu locations which are not HIDDEN, they are placed beneath their 
 * respective parent in order of their sequence.</p>
 */
public class MenuLocation implements Comparable<MenuLocation> {
	private MenuSection section = MenuSection.HIDDEN;
	private int sequence = 0;




	/**
	 * Constructs a hidden menu location;
	 */
	public MenuLocation() {
		this(MenuSection.HIDDEN, 0);
	}




	/**
	 * Construct a menu location of a given section, level and sequence.
	 * @param section The menu section this location represents.
	 * @param sequence The sequence/order this location is in respect to other 
	 * locations in this section.
	 */
	public MenuLocation(MenuSection section, int sequence) {
		this.section = section;
		this.sequence = sequence;
	}




	/**
	 * @return the generic location of the menu this location represents.
	 */
	public MenuSection getSection() {
		return section;
	}




	/**
	 * @return the sequence this is to appear in relation to other features in 
	 * this section.
	 */
	public int getSequence() {
		return sequence;
	}




	/**
	 * This compares the sequence between to locations.
	 * 
	 * <p>The section is not considered in the evaluation as they have concept 
	 * of precedence.</p>
	 *  
	 * @param that The menu location to compare with this location.
	 *  
	 * @return 0 if the locations are equal, a negative value if this value is 
	 * less than the given value or a positive value if this value is greater. 
	 */
	@Override
	public int compareTo(MenuLocation that) {
		return this.sequence - that.sequence;
	}
}
