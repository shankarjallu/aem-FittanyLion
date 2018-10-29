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
//import test.core.services.LoginDBService;



@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Fittany Verify User Login",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/verifyUserLogin"
})
public class UserLoginDBServlet  extends SlingSafeMethodsServlet {

	private static final Logger LOG = LoggerFactory.getLogger(UserLoginDBServlet.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Reference
	private LoginDBService loginDBService;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

		//Getting datasource
		 DataSource oracleDataSource =  loginDBService.getDataSource("fittanydatasource",dataSourceService);
		 String userLoginStatus = loginDBService.verifyUserLogin(oracleDataSource, request);
		 response.getOutputStream().print(userLoginStatus);
	}

}
