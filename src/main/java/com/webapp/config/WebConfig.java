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
package com.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * This is the main configuration class for the web application.
 * 
 * <p>This places the Spring configuration in one location so the developer 
 * does not have to search through several directories looking through various 
 * bean configuration XML files.</p>
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addViewControllers( ViewControllerRegistry registry ) {
    registry.addViewController( "/" ).setViewName( "index" );
  }




  @Override
  public void addResourceHandlers( ResourceHandlerRegistry registry ) {
    registry.addResourceHandler( "/docs/**" ).addResourceLocations( "/docs/" );
    registry.addResourceHandler( "/css/**" ).addResourceLocations( "/css/" );
    registry.addResourceHandler( "/img/**" ).addResourceLocations( "/img/" );
    registry.addResourceHandler( "/js/**" ).addResourceLocations( "/js/" );
    registry.addResourceHandler( "/font-awesome-4.1.0/**" ).addResourceLocations( "/font-awesome-4.1.0/" );
  }




  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix( "/WEB-INF/views/" );
    viewResolver.setSuffix( ".jsp" );
    return viewResolver;
  }




  /**
   * Defines a message source for the controllers. This effectively replaces 
   * the following XML configuration:<br/>
   * <pre>&lt;bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource"&gt;
   *   &lt;property name="basename" value="webapp"/&gt;
   * &lt;/bean&gt;</pre>
   * 
   * <p>Look in src/main/webapp/WEB-INF/classes for the property files used as
   * message sources / resource bundles.</p>
   * 
   * @return a resource bundle to be autowired into any component wanting it.
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource msrc = new ResourceBundleMessageSource();
    msrc.setBasename( "message" );
    return msrc;
  }
}