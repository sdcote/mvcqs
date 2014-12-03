package coyote.commons.feature;

/**
 * Potential sections of a page where menus may appear.
 * 
 * <p>There are 5 generic sections:<ul>
 * <li>HIDDEN - Feature does not appear on any menu,</li>
 * <li>TOP - The menu which appears across the top of the page (e.g. header),</li>
 * <li>BOTTOM - The menu which appears across the bottom of the page (e.g. footer),</li>
 * <li>LEFT - The vertical menu on the left side of the page,</li>
 * <li>RIGHT - the vertical menu on the right side of the page.</li></ul></p>
 * 
 * <p>These section locations are generic and left to the implementation to 
 * determine exactly where the feature is to be represented.</p>
 * 
 * <p>It is expected that many if not most of the features in a system will be 
 * HIDDEN and not represented in a menu. Also, a feature may be represented in 
 * multiple menu sections.</p> * 
 */
public enum MenuSection {
	HIDDEN, TOP, BOTTOM, LEFT, RIGHT;

	private MenuSection() {
	}




	public static MenuSection safeValueOf(String name) {
		try {
			return MenuSection.valueOf(name);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
}
