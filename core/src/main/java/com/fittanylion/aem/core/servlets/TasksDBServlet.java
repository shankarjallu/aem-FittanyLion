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
import com.fittanylion.aem.core.services.TasksDBService;
import com.fittanylion.aem.core.utils.CommonUtilities;

//import test.core.services.CommonUtilities;
//import test.core.services.TasksDBService;
@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Tasks Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/insertTasksIntoDB"
})
public class TasksDBServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RegistrationDBServlet.class);

	@Reference
	private DataSourcePool dataSourceService;

	@Reference
	private TasksDBService tasksDBServie;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		//Getting datasource
		 CommonUtilities commonUtilities = new CommonUtilities();
		 try {
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 String tasksUpdate = request.getParameter("tasksUpdate");
			 String status = null;
			 status = tasksDBServie.verifyTaskTableForUpdate(oracleDataSource, request);
			 response.getOutputStream().print(status);
		 }catch(Exception e) {
			 LOG.info("Exception in UserLoginDBServlet",e.getMessage() );
		 }
	}

}
