
package com.fittanylion.aem.core.services;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface UserSessionLoginService {
	
	public String validateUserKey(DataSource dataSource,SlingHttpServletRequest request);
}
