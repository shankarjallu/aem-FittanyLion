
package com.fittanylion.aem.core.services;
import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface TasksDBService {
	
	public String insertTasksIntoDB(DataSource dataSource,SlingHttpServletRequest request);
	public String verifyTaskTableForUpdate(DataSource dataSource,SlingHttpServletRequest request);
}
