package com.fittanylion.aem.core.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.services.ValidateEmailService;
@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Validate Email Status Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/validateEmail"
})
public class ValidateEmailServlet  extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ValidateEmailServlet.class);
	
	@Reference
	private ValidateEmailService validateEmailService;
	
	@Reference
	private DataSourcePool dataSourceService;
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		
		 //Getting datasource
		 CommonUtilities commonUtilities = new CommonUtilities();
		 try {
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 String status = validateEmailService.validateEmail(oracleDataSource, request);
			
			 response.getOutputStream().print(status);
		 }catch(Exception e) {
			 LOG.error("Exception inside ValidateEmailServlet===>" + e.getMessage());
			e.printStackTrace(); 
		 }
		
	}

}
