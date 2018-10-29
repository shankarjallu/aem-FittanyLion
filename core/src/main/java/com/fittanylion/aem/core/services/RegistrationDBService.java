package com.fittanylion.aem.core.services;

import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;

public interface RegistrationDBService {
	
	String insertIntoDataBase(DataSource dataSource, SlingHttpServletRequest request);

}
