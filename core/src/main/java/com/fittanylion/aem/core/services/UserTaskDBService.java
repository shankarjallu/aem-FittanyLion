package com.fittanylion.aem.core.services;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;


public interface UserTaskDBService {

	public String verifyUserTasksPost(DataSource dataSource,SlingHttpServletRequest request);
	
}
