package com.webapp.dao;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * 
 * 
 */
public class DefaultDataStore implements WebAppDataStore {
  private DataSource _dataSource = null;

  private static final Log LOG = LogFactory.getLog( DefaultDataStore.class );
  private JdbcTemplate jdbcTemplate = null;




  /**
   * Set the SQL data source for this data store
   * 
   * @param source The data source this data store is to use.
   */
  public DefaultDataStore( DataSource source ) {
    _dataSource = source;
    jdbcTemplate = new JdbcTemplate( source );
  }




  // 

  // Data Store methods

  // 

  /**
   * Initialize the data store.
   * 
   * <p>This method is used to initialize the data store by executing data 
   * definitions if the tables appear to be missing.</p>
   * 
   * @throws Exception
   */
  @Override
  public void init() throws Exception {

    // Get a listing of the tables in this data store
    Set<String> tableset = new HashSet<String>();
    DatabaseMetaData md = _dataSource.getConnection().getMetaData();
    ResultSet rs = md.getTables( null, null, "%", null );
    while ( rs.next() ) {
      tableset.add( rs.getString( 3 ) );
    }

    // Check to see if the Consumer table exists and create it if necessary
    if ( !tableset.contains( "LTI_CONSUMER" ) ) {
      // Create the tables needed for this application
    }
  }




  @Override
  public void destroy() {
    // TODO how would this get called?
  }

}
