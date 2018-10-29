package com.fittanylion.aem.core.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.day.cq.mailer.MessageGatewayService;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.services.RegistrationDBService;
@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "= Fittany User Registration Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/getUserRegistrationServlet"
})
public class RegistrationDBServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RegistrationDBServlet.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Reference
	private RegistrationDBService registrationDBService;
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {		
		try {
			//Getting datasource
			 CommonUtilities commonUtilities = new CommonUtilities();
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittanydatasource",dataSourceService);
			 String insertStatus = registrationDBService.insertIntoDataBase(oracleDataSource, request);	 
			 response.getOutputStream().print(insertStatus);
		}catch(Exception e) {
			LOG.error("Exception in Registration Servlet.....=>",e);
		}
	}

}
