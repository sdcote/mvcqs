package com.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * This is the main configuration class for the web application 
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter
{

  @Override
  public void addViewControllers( ViewControllerRegistry registry )
  {
    registry.addViewController( "/" ).setViewName( "index" );
  }




  @Override
  public void addResourceHandlers( ResourceHandlerRegistry registry )
  {
    registry.addResourceHandler( "/static/**" ).addResourceLocations( "/static/" );
    registry.addResourceHandler( "/css/**" ).addResourceLocations( "/css/" );
    registry.addResourceHandler( "/img/**" ).addResourceLocations( "/img/" );
    registry.addResourceHandler( "/js/**" ).addResourceLocations( "/js/" );
  }




  @Bean
  public ViewResolver viewResolver()
  {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix( "/WEB-INF/views/" );
    viewResolver.setSuffix( ".jsp" );
    return viewResolver;
  }
}