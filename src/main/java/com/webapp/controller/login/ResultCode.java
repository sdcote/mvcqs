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
package com.webapp.controller.login;

public enum ResultCode {
	SUCCESS,
	INVALID,
	UNAUTHORIZED,
	CONCURRENT,
	UNVERIFIED,
	INITIAL_PASSWORD_CHANGE;

	public static ResultCode safeValueOf(String name) {
		try {
			return ResultCode.valueOf(name);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

}
