package com.fittanylion.aem.core.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.bean.CustomerItem;
import com.fittanylion.aem.core.services.WinnerUserReport;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;

public class WinnerUserReportImpl implements WinnerUserReport{

	private static final Logger LOG = LoggerFactory.getLogger(WinnerUserReportImpl.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Override
	public List<CustomerItem> userMonthlyWinnerReport(DataSource dataSource, SlingHttpServletRequest request) {
		LOG.info("Inside Method :: userMonthlyWinnerReport");
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement preparedstatement = null;
		List<CustomerItem> customerList = new ArrayList<CustomerItem>();
		try {
			 CommonUtilities commonUtilities = new CommonUtilities();
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 connection = oracleDataSource.getConnection();
			 //final Statement statement = connection.createStatement();
			 /*String sqlquery =	 "SELECT CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT FROM CUSTTSKSTA INNER JOIN TSKWKY ON TSKWKY.TSKWKY_CT = CUSTTSKSTA.TSKWKY_CT "
			 		+ "where (TSKWKY.TSKWKY_STRT_DT >= ? AND TSKWKY.TSKWKY_END_DT  <= ?)";
			 */
			 /*String sqlquery1 =	 "SELECT CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT, COUNT(CUSTTSKSTA.CUST_ID) AS Total FROM CUSTTSKSTA INNER JOIN TSKWKY ON TSKWKY.TSKWKY_CT = CUSTTSKSTA.TSKWKY_CT "
				 		+ "where (TSKWKY.TSKWKY_STRT_DT >= ? AND TSKWKY.TSKWKY_END_DT  <= ?) GROUP BY  CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT`";
				*/
				 
			 
			 
			 String taskStartDate= "21-01-2019"; //request.getParameter("taskStartDate");
		     String taskEndDate = "03-02-2019";  //request.getParameter("taskEndDate");
		     
		     if (taskStartDate != null && taskEndDate != null) {
		    	 taskStartDate = taskStartDate.replace('-', '/');// replaces all occurrences of - to /
		    	 taskEndDate = taskEndDate.replace('-', '/');// replaces all occurrences of - to /
				}
			    
			 	preparedstatement =  connection.prepareStatement(SqlConstant.QUERY_FOR_WINNER_USER_SELECTION1);
			 	/*java.util.Date insertStartDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
				java.sql.Date sqlInsertStartDate = new java.sql.Date(insertStartDateTskwkly.getTime());
	
				java.util.Date insertEndDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
				java.sql.Date sqlInsertEndDate = new java.sql.Date(insertEndDateTskwkly.getTime());*/
	
				preparedstatement.setDate(1, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
				preparedstatement.setDate(2, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
	
				String statementText = preparedstatement.toString();
				String queryGenerated = statementText.substring( statementText.indexOf( ": " ) + 2 );
				LOG.info("Query before execution :- " + queryGenerated);
				System.out.println("Query Before execution :- " + queryGenerated);
				
				resultSet = preparedstatement.executeQuery();  		 
				
			 System.out.println("resultSet===" + resultSet.getFetchSize());
			 
			 int custIdValues = 0;
			 int custChanceCount = 0;
			 int custtotalChanceCount = 0;
			 while(resultSet.next()) {
				 custIdValues = resultSet.getInt("CUST_ID");
				 custChanceCount = resultSet.getInt("CUSTTSKSTA_CHNC_CT");
				 custtotalChanceCount = resultSet.getInt("Total");
				 CustomerItem customerItem = new CustomerItem();
				 customerItem.setCustomerId(custIdValues);
				 customerItem.setRelativeChance(custtotalChanceCount);
				 System.out.println("tskwkyvalues====>" + custIdValues);
				 System.out.println("CUSTTSKSTA_CHNC_CT====>" + custChanceCount);
				 System.out.println("Total====>" + custtotalChanceCount);
				 customerList.add(customerItem);
			 }
	
		       } catch (Exception e) {
		    	   LOG.error("Exception inside method userMonthlyWinnerReport: " , e.getMessage());
		       } finally {
		    	   sqlDBUtil.sqlConnectionClose(resultSet, connection, preparedstatement, LOG); 
			
		}
		
		return customerList;
	}
	
	@Override
	public CustomerItem userDetailsByCustID(int custID) {

		ResultSet resultSet = null;
		Connection connection = null;
		CustomerItem winnerUser = new CustomerItem();
		PreparedStatement preparedstatement = null;
		try {
			 CommonUtilities commonUtilities = new CommonUtilities();
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 connection = oracleDataSource.getConnection();
			 //String sqlUserquery = "SELECT * FROM CUST WHERE CUST_ID = ?";
			 preparedstatement =  connection.prepareStatement(SqlConstant.SELECT_QUERY_FOR_CUST_TABLE_BY_PASSING_CUST_ID);
			 preparedstatement.setInt(1, custID);
			 resultSet = preparedstatement.executeQuery(); 
			 
			 while(resultSet.next()) { 
				 winnerUser.setCustomerId(custID); 
				 winnerUser.setFirstname(resultSet.getString(SqlConstant.CUST_FST_NM));
				 winnerUser.setLasttname(resultSet.getString(SqlConstant.CUST_LA_NM));
				 winnerUser.setEmail(resultSet.getString(SqlConstant.CUST_EMAIL_AD));
				 System.out.println("CUST_ID: " + winnerUser.getCustomerId() + "Name :- " + winnerUser.getFirstname() + "Email :- " + winnerUser.getEmail());
			 }
			 LOG.info("CUST_ID: " + winnerUser.getCustomerId() + "Name :- " + winnerUser.getFirstname() + "Email :- " + winnerUser.getEmail());
			 
		}catch (Exception e) {
			LOG.error("Exception inside method userMonthlyWinnerReport: " , e);
		} finally {
			sqlDBUtil.sqlConnectionClose(resultSet, connection, preparedstatement, LOG); 
		}
		
		return winnerUser;
		
	}
	
	
	
	
}


