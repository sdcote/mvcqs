package com.webapp.controller;

import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webapp.dao.WebAppDataStore;


/**
 * This is a sample controller 
 */
@Controller
public class SampleController {
  private static final Log LOG = LogFactory.getLog( SampleController.class );

  protected ResourceBundleMessageSource messageSource;

  private WebAppDataStore _dataStore = null;




  @Autowired
  public void setMessageSource( ResourceBundleMessageSource messageSource ) {
    this.messageSource = messageSource;
  }




  @Autowired
  public void setDataStore( WebAppDataStore datastore ) {
    _dataStore = datastore;
    LOG.info( "DataStore wired" );
  }




  /**
   * This shows how to create a simple MVC endpoint.
   * @param m
   * @return
   */
  @RequestMapping("home")
  public String loadHomePage( Model m ) {
    Context ctx = null;
    try {
      ctx = new InitialContext();
    } catch ( Exception e ) {
      LOG.error( "No initial context", e );
    }

    LOG.info( "Handling 'home' request..." );
    LOG.info( "data store = " + _dataStore );
    m.addAttribute( "name", "World" );
    try {
      m.addAttribute( "title", messageSource.getMessage( "title", null, Locale.getDefault() ) );
    } catch ( NoSuchMessageException e ) {
      LOG.error( e.getMessage() );
    }
    LOG.info( "Request for 'home' handled." );
    
    return "home";
  }
}
