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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.dao.WebAppDataStore;


/**
 * This is a sample REST service.
 */
@RestController
@RequestMapping("/inbox/")
public class SampleService {

  private static final Log LOG = LogFactory.getLog( SampleService.class );

  protected ResourceBundleMessageSource messageSource;

  private WebAppDataStore _dataStore = null;




  @Autowired
  public void setMessageSource( ResourceBundleMessageSource messageSource ) {
    this.messageSource = messageSource;
  }




  @Autowired
  public void setDataStore( WebAppDataStore datastore ) {
    _dataStore = datastore;
    LOG.info( "DataStore wired into service" );
  }




  @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
  public @ResponseBody Message getMsg( @PathVariable
  int id ) {
    Message retval = new Message( id );
    retval.setBody( "This is a body of the message." );
    LOG.info( "Returning message id " + retval.getId() );
    return retval;
  }




  @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
  public @ResponseBody List<Message> getAllMessages() {
    List<Message> retval = new ArrayList<Message>();
    retval.add( new Message( 1, "Message One" ) );
    retval.add( new Message( 2, "Message Two" ) );
    retval.add( new Message( 3, "Message Three" ) );
    LOG.info( "Returning " + retval.size() + " messages" );

    return retval;
  }

}