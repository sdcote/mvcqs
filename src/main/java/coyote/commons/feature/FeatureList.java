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
import java.util.List;

public class FeatureList {

	private final List<Feature> features = new ArrayList<Feature>();




	public FeatureList() {

	}




	public void add(Feature feature) {
		features.add(feature);
	}

}
