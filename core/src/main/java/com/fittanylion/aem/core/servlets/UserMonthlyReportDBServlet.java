

package com.fittanylion.aem.core.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.fittanylion.aem.core.utils.CommonUtilities;


@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=User Monthly Prize DB Report Status Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/userMonthlyPrizeReport"
})
public class UserMonthlyReportDBServlet  extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(UserMonthlyReportDBServlet.class);
	
	
	@Reference
	private DataSourcePool dataSourceService;
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		
		JSONObject userMonthlyPrize = new JSONObject();
		try {
			userMonthlyPrize.put("statusCode",400);
			userMonthlyPrize.put("message","DataBase connection issue");
			
				
		 CommonUtilities commonUtilities = new CommonUtilities();
		 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
	 
		 final Connection connection = oracleDataSource.getConnection();
		 final Statement statement = connection.createStatement();
		 
		 String sqlquery =	 "SELECT CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT FROM CUSTTSKSTA INNER JOIN TSKWKY ON TSKWKY.TSKWKY_CT = CUSTTSKSTA.TSKWKY_CT "
		 		+ "where (TSKWKY.TSKWKY_STRT_DT >= ? AND TSKWKY.TSKWKY_END_DT  <= ?)";

		 
		 String taskStartDate= "21-01-2019";
	     String taskEndDate = "03-02-2019";
	     
	     if (taskStartDate != null && taskEndDate != null) {
	    	 taskStartDate = taskStartDate.replace('-', '/');// replaces all occurrences of - to /
	    	 taskEndDate = taskEndDate.replace('-', '/');// replaces all occurrences of - to /
			}
		    
		 
		 PreparedStatement ps =  connection.prepareStatement(sqlquery);
		 java.util.Date insertStartDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
			java.sql.Date sqlInsertStartDate = new java.sql.Date(insertStartDateTskwkly.getTime());

			java.util.Date insertEndDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
			java.sql.Date sqlInsertEndDate = new java.sql.Date(insertEndDateTskwkly.getTime());

			ps.setDate(1, sqlInsertStartDate);
			ps.setDate(2, sqlInsertEndDate);

		 ResultSet resultSet = ps.executeQuery();  		 
			
		 System.out.println("resultSet===" + resultSet.getFetchSize());
		 List<Object> weekydatesListobject = new ArrayList<Object>();
		 int custIdValues = 0;
		 int custChanceCount = 0;
		 while(resultSet.next()) {
			 custIdValues = resultSet.getInt("CUST_ID");
			 custChanceCount = resultSet.getInt("CUSTTSKSTA_CHNC_CT");
			 weekydatesListobject.add(custIdValues);
			 System.out.println("tskwkyvalues====>" + custIdValues);
			 System.out.println("CUSTTSKSTA_CHNC_CT====>" + custChanceCount);

			 userMonthlyPrize.put("custIdValues",custIdValues);
				userMonthlyPrize.put("message","DataBase connection issue");

		 }
		 response.getOutputStream().print(userMonthlyPrize.toString());

	       } catch (Exception e) {
		         e.printStackTrace();
	       }
	
		//return userMonthlyPrize.toString();
		
  }

	
  
}
