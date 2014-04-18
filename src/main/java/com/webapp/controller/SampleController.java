package com.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webapp.dao.WebAppDataStore;


/**
 * This is a sample controller 
 */
@Controller
public class SampleController
{

  private static final Log LOG = LogFactory.getLog( SampleController.class );

  private WebAppDataStore _dataStore = null;




  @Autowired
  public void setDataStore( WebAppDataStore datastore )
  {
    _dataStore = datastore;
    LOG.info( "DataStore wired" );
  }




  @RequestMapping("home")
  public String loadHomePage( Model m )
  {
    LOG.info( "Handling request..." );
    LOG.info( "data store = "+_dataStore );
    m.addAttribute( "name", "World" );
    LOG.info( "Request handled." );
    return "home";
  }
}
