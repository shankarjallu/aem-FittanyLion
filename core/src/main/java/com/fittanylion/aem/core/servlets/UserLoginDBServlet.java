package com.fittanylion.aem.core.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.services.UserLoginDBService;
@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=User Login DB Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/verifyUserLogin"
})
public class UserLoginDBServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(UserLoginDBServlet.class);
	
	@Reference
	private UserLoginDBService userLoginDBService;
	
	@Reference
	private DataSourcePool dataSourceService;
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOG.info("Inside doPost method of UserLoginDBServlet"); 
		//Getting datasource
		 CommonUtilities commonUtilities = new CommonUtilities();
		 try {
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 String status = userLoginDBService.verifyUserLogin(oracleDataSource, request);
			 response.getOutputStream().print(status);			 
		 }catch(Exception e) {
			 LOG.error("Exception in UserLoginDBServlet",e);
		 }
	}

}
