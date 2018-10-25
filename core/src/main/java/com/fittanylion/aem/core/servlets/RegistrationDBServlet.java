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

//import test.core.services.RegistrationDBService;

@Component(service=Servlet.class,
		property={
				Constants.SERVICE_DESCRIPTION + "=Fittany User Registration Servlet",
				"sling.servlet.methods=" + HttpConstants.METHOD_GET,
				"sling.servlet.paths="+ "/bin/getUserRegistrationServlet"
		})
public class RegistrationDBServlet  extends SlingSafeMethodsServlet {

	private static final Logger LOG = LoggerFactory.getLogger(RegistrationDBServlet.class);

	@Reference
	private DataSourcePool dataSourceService;

	/*@Reference
	private RegistrationDBService registrationDBService;*/

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse resp) throws ServletException, IOException {
		RegistrationDBService registrationDBService = new RegistrationDBService();
		//Getting datasource
		DataSource oracleDataSource =  registrationDBService.getDataSource("fittany_Datasource",dataSourceService);
		String insertMessage = registrationDBService.insertIntoDataBase(oracleDataSource, request);
		resp.getOutputStream().print(insertMessage);

	}

}
