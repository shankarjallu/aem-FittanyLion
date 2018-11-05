package com.fittanylion.aem.core.services;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface UserLoginDBService {
	
	public String verifyUserLogin(DataSource dataSource,SlingHttpServletRequest request);
}
