
package com.fittanylion.aem.core.services;
import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface ForgotPassWordService {
	
	public String sendChangePassWordLinkToMail(DataSource dataSource,SlingHttpServletRequest request);
	public String updatePassWordInDB(DataSource dataSource,SlingHttpServletRequest request);
}
