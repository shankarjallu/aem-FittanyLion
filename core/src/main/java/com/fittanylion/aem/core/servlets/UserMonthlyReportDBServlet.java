

package com.fittanylion.aem.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.bean.CustomerItem;
import com.fittanylion.aem.core.services.WinnerUserReport;
import com.fittanylion.aem.core.utils.CommonUtilities;


@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=User Monthly Prize DB Report Status Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/userMonthlyPrizeReport"
})
public class UserMonthlyReportDBServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(UserMonthlyReportDBServlet.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Reference
	private WinnerUserReport winnerUserReport;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOG.info("Inside doGet of UserMonthlyReportDBServlet");
		JSONObject userMonthlyPrizeJson = new JSONObject();
		List<CustomerItem> userList = new ArrayList<CustomerItem>();
		CustomerItem userDetails = new CustomerItem();
		try {
		 CommonUtilities commonUtilities = new CommonUtilities();
		 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
		 if (oracleDataSource != null) {
			 userList = winnerUserReport.userMonthlyWinnerReport(oracleDataSource, request);
			 if (userList != null && userList.size() > 0) {
				 LOG.info("User List size from final table " + userList.size());
				//call to generate random number and corresponding data.
				 userDetails = getRandom(userList);
				 int custID = userDetails.getCustomerId();
				 CustomerItem winnerUser = winnerUserReport.userDetailsByCustID(custID);
				 if (userDetails != null) {
					 userMonthlyPrizeJson.put("UserID", winnerUser.getCustomerId());
					 userMonthlyPrizeJson.put("UserFirstName", winnerUser.getFirstname());
					 userMonthlyPrizeJson.put("UserLastName", winnerUser.getLasttname());
					 userMonthlyPrizeJson.put("Email", winnerUser.getEmail());
					 userMonthlyPrizeJson.put("statusCode",200);
				 }
			 } else {
				 userMonthlyPrizeJson.put("statusCode" , 200);
				 userMonthlyPrizeJson.put("message" , "No User has completed task");
			 }
			 
		 }
		} catch (Exception e) {
			try {
				userMonthlyPrizeJson.put("statusCode" , 400);
				userMonthlyPrizeJson.put("message" , "Something went wrong in DB connection");
			} catch (JSONException e1) {
				LOG.error("Execption Inside doGet of UserMonthlyReportDBServlet for JSONException" + e);
				e1.printStackTrace();
			}
			
			//LOG.error("Execption Inside doGet of UserMonthlyReportDBServlet");
		}
		response.getOutputStream().print(userMonthlyPrizeJson.toString());
		
  }
	
	public CustomerItem getRandom(List<CustomerItem> customeritemsList) {
		Random rand = new Random();
		int totalSum = 0;
		for(CustomerItem customerItem : customeritemsList) {
            totalSum = totalSum + customerItem.relativeChance;
        }
        int index = rand.nextInt(totalSum) +1 ;
        int sum = 0;
        int i=0;
        while(sum < index ) {
             sum = sum + customeritemsList.get(i++).relativeChance;
        }
        return customeritemsList.get(Math.max(0,i-1));
    }


	
  
}
