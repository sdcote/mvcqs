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

import java.io.Serializable;

public class LoginResult implements Serializable {
	private static final long serialVersionUID = -192475760409031131L;

	ResultCode resultCode = ResultCode.UNVERIFIED;
	
	String redirect ="/";




	public LoginResult() {

	}




	public ResultCode getResultCode() {
		return resultCode;
	}




	public void setResultCode(ResultCode code) {
		resultCode = code;
	}




	public String getRedirect() {
		return redirect;
	}




	public void setRedirect(String path) {
		redirect = path;
	}

}
