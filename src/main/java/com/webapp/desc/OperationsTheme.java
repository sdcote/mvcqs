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
package com.webapp.desc;

import coyote.commons.Version;
import coyote.commons.feature.Feature;


/**
 * Operations pages and functions; thread pools and background processes
 * 
 * <p>Since this feature has a list of children, this feature is a composition, 
 * and has no link of its own. It serves as an aggregation of other features.
 * Usually only childless features have links.</p>
 */
public class OperationsTheme extends Feature {

  OperationsTheme() {
    version = new Version( 1, 0, 0, Version.EXPERIMENTAL );
    name = "operations";
    description = "Monitor and manage application.";

    // addFeature( new BackgroundJobsFeature() ); view & manage jobs
    // addFeature( new SchedulerFeature() ); // schedule a job
    // addFeature( new MaintenanceWindowFeature() ); // currently down for maintenance
    // addFeature( new BackupDatabaseFeature() );
    // addFeature( new RestoreDatabaseFeature() );
    // addFeature( new ImportDatabaseFeature() );
    // addFeature( new ExportDatabaseFeature() );
    // addFeature( new DatabaseExplorerFeature() );
  }
}
