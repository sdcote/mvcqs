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
package com.webapp.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;


/**
 * This is the tag used in our pages to generate the main navigation menu.
 */
public class MainNav extends SimpleTagSupport {
  
  public void doTag() throws JspException, IOException {
    /*This is just to display a message, when
     * we will use our custom tag. This message
     * would be displayed
     */
    JspWriter out = getJspContext().getOut();
    out.println( "This is where the naigation menu will go." );
  }
}
