package com.fittanylion.aem.core.services;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface ValidateEmailService {
	
	public String validateEmail(DataSource dataSource,SlingHttpServletRequest request);
}
