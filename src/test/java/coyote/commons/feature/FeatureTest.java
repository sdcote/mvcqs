package coyote.commons.feature;

//import static org.junit.Assert.*;
import java.util.List;

import org.junit.Test;

import coyote.commons.Version;

public class FeatureTest {

	@Test
	public void testGetFeaturesBySection() {
		
		Feature application = new TestSystem();
		
		// Get all the features which are supposed to go in the top menu
		List<Feature> menuItems = application.getFeaturesBySection(MenuSection.TOP);
		for(Feature feature:menuItems){
			System.out.println(feature);
		}
		

	}

	class TestSystem extends Feature {
		public TestSystem() {
			version = new Version(1, 0, 0, Version.DEVELOPMENT);
			name = "testapp";
			link = "/";
			description = "This is a test system description.";

			Feature theme = new TestThemeOne();
			addFeature(theme);

			theme = new TestThemeFour();
			addFeature(theme);
			theme = new TestThemeThree();
			addFeature(theme);
			theme = new TestThemeFive();
			addFeature(theme);
			theme = new TestThemeTwo();
			addFeature(theme);

		}
	}

	class TestThemeOne extends Feature {
		public TestThemeOne() {
			version = new Version(1, 0, 0, Version.DEVELOPMENT);
			name = "testone";
			link = "/one";
			description = "This is a test feature.";
			addLocation(new MenuLocation(MenuSection.TOP, 1));
		}
	}

	class TestThemeTwo extends Feature {
		public TestThemeTwo() {
			version = new Version(1, 0, 0, Version.DEVELOPMENT);
			name = "testtwo";
			link = "/two";
			description = "This is a test feature.";
			addLocation(new MenuLocation(MenuSection.RIGHT, 2));
		}
	}

	class TestThemeThree extends Feature {
		public TestThemeThree() {
			version = new Version(1, 0, 0, Version.DEVELOPMENT);
			name = "testthree";
			link = "/three";
			description = "This is a test feature.";
			addLocation(new MenuLocation(MenuSection.TOP, 3));
		}
	}

	class TestThemeFour extends Feature {
		public TestThemeFour() {
			version = new Version(1, 0, 0, Version.DEVELOPMENT);
			name = "testfour";
			link = "/four";
			description = "This is a test feature.";
			addLocation(new MenuLocation(MenuSection.LEFT, 4));
			addLocation(new MenuLocation(MenuSection.TOP, 4));
		}
	}

	class TestThemeFive extends Feature {
		public TestThemeFive() {
			version = new Version(1, 0, 0, Version.DEVELOPMENT);
			name = "testfive";
			link = "/five";
			description = "This is a test feature.";
			addLocation(new MenuLocation(MenuSection.TOP, 5));
		}
	}

}
