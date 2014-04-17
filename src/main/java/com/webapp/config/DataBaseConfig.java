package com.webapp.config;

import java.sql.Driver;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.webapp.dao.DefaultDataStore;
import com.webapp.dao.WebAppDataStore;


@Configuration
public class DataBaseConfig
{
  private static final Log LOG = LogFactory.getLog( DataBaseConfig.class );

  static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
  static final String PROPERTY_NAME_DATABASE_URL = "db.url";
  static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
  static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";

  @Resource
  private Environment env;




  @Bean
  public DataSource dataSource()
  {
    LOG.info( "Creating a datasource.." );
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    try
    {
      LOG.info( "Classname: " + env.getRequiredProperty( PROPERTY_NAME_DATABASE_DRIVER ) );
      @SuppressWarnings("unchecked")
      Class<? extends Driver> driverClass = (Class<? extends Driver>)Class.forName( env.getRequiredProperty( PROPERTY_NAME_DATABASE_DRIVER ) );
      dataSource.setDriverClass( driverClass );

      LOG.info( "URL: " + env.getRequiredProperty( PROPERTY_NAME_DATABASE_URL ) );
      dataSource.setUrl( env.getRequiredProperty( PROPERTY_NAME_DATABASE_URL ) );

      LOG.info( "User: " + env.getRequiredProperty( PROPERTY_NAME_DATABASE_USERNAME ) );
      dataSource.setUsername( env.getRequiredProperty( PROPERTY_NAME_DATABASE_USERNAME ) );

      dataSource.setPassword( env.getRequiredProperty( PROPERTY_NAME_DATABASE_PASSWORD ) );
      LOG.info( "Datasource created successfully." );
    }
    catch( ClassNotFoundException | IllegalStateException e )
    {
      LOG.fatal( "Could not greate driver class '" + env.getRequiredProperty( PROPERTY_NAME_DATABASE_DRIVER ) + "'", e );
    }

    return dataSource;
  }




  @Bean
  public WebAppDataStore datastore( DataSource source )
  {
    WebAppDataStore retval = new DefaultDataStore( source );

    // Set properties

    // Initialize the data store
    try
    {
      retval.init();
    }
    catch( Exception e )
    {
      LOG.fatal( "Could not initialize data store.", e );    }

    return retval;
  }

}
