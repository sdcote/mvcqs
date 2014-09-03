package com.webapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration for application @Components such as @Services, @Repositories, 
 * and @Controllers.
 * 
 * <p>Loads externalized property values required to configure the various 
 * application properties. Not much else here, as we rely on @Component 
 * scanning in conjunction with @Inject by-type auto-wiring.</p>
 */
@Configuration
@ComponentScan(basePackages = "com.webapp", excludeFilters = { @Filter(Configuration.class) })
public class ComponentConfig
{

}