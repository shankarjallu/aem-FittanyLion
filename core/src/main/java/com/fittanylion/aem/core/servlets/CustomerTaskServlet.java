package com.fittanylion.aem.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.services.CustomerTaskDBService;
import com.fittanylion.aem.core.utils.CommonUtilities;
@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "= Fittany Customer Tasks Staus Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/customertaskstatus"
})
public class CustomerTaskServlet  extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(CustomerTaskServlet.class);
	
	@Reference
	private CustomerTaskDBService customerTaskDBService;
	
	@Reference
	private DataSourcePool dataSourceService;
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOG.info("Method start for doGet Servlet CustomerTaskServlet ");
		String status = null;
		JSONObject resultObj = new JSONObject();
		JSONObject customerTaskJson  = null;
		 //Getting datasource
		 CommonUtilities commonUtilities = new CommonUtilities();
		 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
		 if (request != null ) {
			 StringBuilder sb = new StringBuilder();
				 try {
				      BufferedReader br = request.getReader();
				      String str = null;
				      while ((str = br.readLine()) != null) {
				       sb.append(str);
				      }
			      customerTaskJson = new JSONObject(sb.toString());
			      
			      String customerId = customerTaskJson.getString("CustomerId");
			      String taskStartDate  = customerTaskJson.getString("taskStartDate ");
			      String taskEndDate = customerTaskJson.getString("taskEndDate");
			      String taskID = customerTaskJson.getString("taskID");
			      String custTaskCompleteIndicator  = customerTaskJson.getString("custTaskCompleteIndicator ");
			     
			      LOG.info("Method Parameter CustmoerID - {} TaskID - {}", customerId, taskID);
			     
			      status = customerTaskDBService.InsertTaskCompletionStatusInDB(oracleDataSource , customerId, taskStartDate, taskEndDate, taskID, custTaskCompleteIndicator);
			      LOG.info("CustomerTaskServlet-> Method doGet-> Insert query status - {}", status);
			      if (status.equals("true")) {
			    	  resultObj = new JSONObject();
	                    resultObj.put("statusCode",200);
	                    resultObj.put("result","success");
	                    resultObj.put("message","Customer Task Insert Task success.");
			      } else {
			    	  LOG.info("CustomerTaskServlet-> Method doGet-> Insert query status - {}", status);
			    	  resultObj.put("statusCode",102);
			    	  resultObj.put("result","fail");
	                  resultObj.put("message","Customer Task Insert Task success.");
			      }
			      response.getOutputStream().print(status);
				 } catch (Exception e) {
					 LOG.error("Expection in CustomerTaskServlet" + e.getMessage() );
				 }
			
			}
		 
	}

}
