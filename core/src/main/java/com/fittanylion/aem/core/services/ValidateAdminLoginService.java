

package com.fittanylion.aem.core.services;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface ValidateAdminLoginService {
	
	public String validateAdminLogin(DataSource dataSource,SlingHttpServletRequest request);
}
