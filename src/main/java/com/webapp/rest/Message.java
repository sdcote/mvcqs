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
package com.webapp.rest;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;


/**
 * 
 */
public class Message implements Serializable {

  private static final long serialVersionUID = -4335987071790684240L;

  private int identifier = 0;
  private String body = null;
  private Date createdDate = new Date();




  /**
   * @return the identifier
   */
  public int getId() {
    return identifier;
  }




  /**
   * @param id the identifier to set
   */
  public void setId( int id ) {
    this.identifier = id;
  }




  /**
   * @return the createdDate
   */
  @JsonSerialize(using = DateSerializer.class)
  public Date getCreatedDate() {
    return createdDate;
  }




  /**
   * @param date the createdDate to set
   */
  public void setCreatedDate( Date date ) {
    this.createdDate = date;
  }




  /**
   * @return the body
   */
  public String getBody() {
    return body;
  }




  /**
   * @param id
   */
  public Message( int id ) {
    identifier = id;
  }




  /**
   * @param i
   * @param text
   */
  public Message( int id, String text ) {
    identifier = id;
    body = text;
  }




  /**
   * @param text
   */
  public void setBody( String text ) {
    body = text;

  }

}
