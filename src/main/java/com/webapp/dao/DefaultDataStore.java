package com.webapp.dao;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import sun.security.krb5.internal.crypto.Nonce;


/**
 * 
 * 
 */
public class DefaultDataStore implements WebAppDataStore
{
  private DataSource _dataSource = null;

  private static final Log LOG = LogFactory.getLog( DefaultDataStore.class );
  private JdbcTemplate jdbcTemplate = null;




  /**
   * Set the SQL data source for this data store
   * 
   * @param source The data source this data store is to use.
   */
  public DefaultDataStore( DataSource source )
  {
    _dataSource = source;
    jdbcTemplate = new JdbcTemplate( source );
  }




  public Nonce getNonce( String consumerKey, String nonce )
  {
    String SQL = "select * from lti_nonce where consumer_key = ? and nonce = ?";
    List<Nonce> results = jdbcTemplate.query( SQL, new Object[] { consumerKey, nonce }, new NonceMapper() );

    if( results.isEmpty() )
    {
      return null;
    }
    else
    {
      return results.get( 0 );
    }
  }

  // 

  // 

  // 

  /**
   * Maps all the column of the LTI Nonce table to the Nonce type
   */
  class NonceMapper implements RowMapper<Nonce>
  {
    public Nonce mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      //Nonce nonce = new Nonce( rs.getString( "consumer_key" ), rs.getString( "nonce" ) );
      Nonce nonce = new Nonce();
      return nonce;
    }
  }


  // 

  // Data Store methods

  // 


  /**
   * Initialize the data store.
   * 
   * <p>
   * This method is used to initialize the data store by executing data definitions if the tables appear to be
   * missing.
   * </p>
   * 
   * @throws Exception
   */
  @Override
  public void init() throws Exception
  {

    // Get a listing of the tables in this data store
    Set<String> tableset = new HashSet<String>();
    DatabaseMetaData md = _dataSource.getConnection().getMetaData();
    ResultSet rs = md.getTables( null, null, "%", null );
    while( rs.next() )
    {
      tableset.add( rs.getString( 3 ) );
    }

    // Check to see if the Consumer table exists and create it if necessary
    if( !tableset.contains( "LTI_CONSUMER" ) )
    {
      LOG.info( "Creating the Consumer table" );
      String createConsumerTable = "CREATE TABLE lti_consumer (" + "consumer_key varchar(255) NOT NULL," + "name varchar(45) NOT NULL," + "secret varchar(32) NOT NULL," + "lti_version varchar(12) DEFAULT NULL," + "enabled tinyint(1) NOT NULL," + "PRIMARY KEY (consumer_key)" + ");";
      jdbcTemplate.execute( createConsumerTable );

      // Throw a little sample data into it for testing
      String insertSampleConsumer = "INSERT INTO  lti_consumer ( consumer_key, name,  secret,  lti_version, enabled) VALUES ('0457f682-9f82-4c23-8625-adc5cd98dfb8','Hilliard','71969192f93811b45dcd83cf5955e7dd','1.0',1)";
      jdbcTemplate.update( insertSampleConsumer );

    }

    // Create the Nonce table if it does not exist
    if( !tableset.contains( "LTI_NONCE" ) )
    {
      LOG.info( "Creating the Nonce table" );
      String createNonceTable = "CREATE TABLE lti_nonce (" + "consumer_key varchar(255) NOT NULL," + "nonce varchar(32) NOT NULL," + "issued datetime NOT NULL," + "PRIMARY KEY (consumer_key, nonce)" + ");";
      jdbcTemplate.execute( createNonceTable );

      String addNonceConstraint = "ALTER TABLE lti_nonce " + "ADD CONSTRAINT lti_nonce_consumer_FK1 FOREIGN KEY (consumer_key)" + "REFERENCES lti_consumer (consumer_key);";
      jdbcTemplate.execute( addNonceConstraint );
    }
  }




  @Override
  public void destroy()
  {
    // TODO how would this get called?
  }

}
