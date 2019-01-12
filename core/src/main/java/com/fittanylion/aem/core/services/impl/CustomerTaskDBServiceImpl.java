package com.fittanylion.aem.core.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.services.CustomerTaskDBService;

@Component(immediate = true, service = CustomerTaskDBService.class)
public class CustomerTaskDBServiceImpl implements CustomerTaskDBService{

	static final private Logger LOGGER = LoggerFactory.getLogger(CustomerTaskDBServiceImpl.class);
	
	@Override
	public String InsertTaskCompletionStatusInDB(DataSource dataSource, String customerId, String taskStartDate,
			String taskEndDate, String taskID, String custTaskCompleteIndicator)  {
		LOGGER.info("Start method InsertTaskCompletionStatusInDB");
		LOGGER.info("Method Parameter TaskStartDate - {} TaskEndDate - {}",   taskStartDate, taskEndDate );
		int isInsertStatus = 0;
		String successfullyInsertetedData = "false";
		Connection connection = null;
		PreparedStatement insertIntoCustTskPS = null;
		try {
			if (dataSource !=null) {
				connection = dataSource.getConnection();
				//Insert SQL query
				String insetQuery = "insert into CUSTTSK (CUST_ID, TSK_ID, CUSTTSK_CMPL_IN, CUSTTSK_STRT_DT, CUSTTSK_END_DT, CUSTTSK_RCD_MNTD_TS) values (?,?,?.?,?,?) ";
				//Get Prepared Statement object 
				insertIntoCustTskPS = connection.prepareStatement(insetQuery);
				
				
				//Set insert coulmn values
				insertIntoCustTskPS.setInt(1, Integer.parseInt(customerId));
				insertIntoCustTskPS.setInt(2, Integer.parseInt(taskID));
				 insertIntoCustTskPS.setString(3, custTaskCompleteIndicator);
				
	            java.util.Date insertTaskStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
	            java.sql.Date sqlInsertTaskStartDate = new java.sql.Date(insertTaskStartDate.getTime());
	            insertIntoCustTskPS.setDate(4, sqlInsertTaskStartDate);
	            
	            java.util.Date insertTaskEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
	            java.sql.Date sqlInsertTaskEndtDate = new java.sql.Date(insertTaskEndDate.getTime());
	            insertIntoCustTskPS.setDate(5, sqlInsertTaskEndtDate);
	            
	            java.util.Date date = new java.util.Date();
	            java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
	            insertIntoCustTskPS.setDate(6, sqlCurrentDate);
	           
	           
	            LOGGER.info("Insert Query:- " + insetQuery );
				LOGGER.info("Start data insertion into table CUSTTSK");
				isInsertStatus = insertIntoCustTskPS.executeUpdate();
				if (isInsertStatus > 0 )
					LOGGER.info("Data insertion into table CUSTTSK Successfully Completed");
				successfullyInsertetedData = "true";
	           return successfullyInsertetedData;
			}
		}catch (SQLException sq) {
			successfullyInsertetedData = "false";
			 LOGGER.error("SQL EXception while data insertion ", sq);
		} catch (Exception e) {
			successfullyInsertetedData = "false";
			LOGGER.error("EXception in Class  CustomerTaskDBServiceImpl", e);
		}finally{
		      try{
		         if(insertIntoCustTskPS!=null)
		        	 connection.close();
		      }catch(SQLException se){
		      }
		      try{
		         if(connection !=null)
		        	 connection.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		
		return successfullyInsertetedData;
	}

}
