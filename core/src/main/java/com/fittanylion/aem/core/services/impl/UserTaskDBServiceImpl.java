package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.services.UserTaskDBService;
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;


@Component(immediate = true, service = UserTaskDBService.class)

public class UserTaskDBServiceImpl implements UserTaskDBService {

	private static final Logger LOG = LoggerFactory.getLogger(UserTaskDBServiceImpl.class);

	  @Override
	    public String verifyUserTasksPost(DataSource dataSource, SlingHttpServletRequest request) {
		  LOG.info("Inside method verifyUserTasksPost");
		  String insertStatus = "failured";
			JSONObject jsonObjectConnection = new JSONObject();
			Connection connection = null;
			  try{
					 StringBuilder sb = new StringBuilder();
					  BufferedReader br = request.getReader();
					  String str = null;
					  while ((str = br.readLine()) != null) {
					      sb.append(str);
					  }
					  JSONObject jsnobject = new JSONObject(sb.toString());
					  
                        String taskStartDate = jsnobject.getString("taskStartDate");
						String taskEndDate = jsnobject.getString("taskEndDate");
						int customerId = jsnobject.getInt("customerId");
						int taskId = jsnobject.getInt("taskID");
						String custTaskCompleteIndicator = jsnobject.getString("custTaskCompleteIndicator");
						
					
							if(dataSource != null && taskStartDate != null && taskEndDate != null && custTaskCompleteIndicator != null) {
								JSONObject jsonObject = new JSONObject();
								LOG.info("taskStartDate====>" + taskStartDate + "taskEndDate======>" + taskEndDate + "customerId=======>" + customerId + "TaskId=======>" + taskId);
								connection = dataSource.getConnection();
			                    //final Statement statement = connection.createStatement();
			                  //Need to validate if a record exists in CUSTTSK for with TASD_ID for the given date ranges  
			                    if(taskId > 0) {
			                    		int insertCustTaskStatusCount = 0;
										if(validateTaksIDPresentforCustomer(connection,taskId,  customerId) == 0) {
											int isInsert = insertIntoCustTask(connection, taskId, customerId,  custTaskCompleteIndicator,  taskStartDate, taskEndDate);
								     	    //NEED to check if their are 3 TSK_ID records found in CUSTTSK for given Start Date and End date.If yes INSERT record into CUSTTSKSTA
							     	          if(readingCustomerTotalChanceCount(connection,customerId,taskStartDate,taskEndDate) == 3) {
								     	        	 // So once we have 3 records found then INSERT the records into CUSTTSKSTA table.
														//Insert Tasks into CUSTOMER TASK STATUS
														insertCustTaskStatusCount =	insertCustTaskStatus(connection,customerId, getTaskWeeklyCount(connection,taskStartDate, taskEndDate ));
								     	         }
								     	         if (isInsert != 0) {
											     	     if(insertCustTaskStatusCount > 0) {
											     	    	 	jsonObject.put("statusCode",200);
																jsonObject.put("message","Task inserted into custtsk and custtskstatus");
																jsonObject.put("congratsCard", true);
																return jsonObject.toString();
	
											     	     }else {
											     	    	  	jsonObject.put("statusCode",200);
																jsonObject.put("message","Task Sucessfully inserted");
																jsonObject.put("congratsCard", false);
																return jsonObject.toString();
											     	     }
										     	     }else {
										     	        	  	jsonObject.put("statusCode",400);
																jsonObject.put("message","Error while posting data");
																jsonObject.put("congratsCard", false);
																return jsonObject.toString();
										     	        	 
										     	         }
										}else {
											jsonObject.put("statusCode",400);
											jsonObject.put("message","This Task Id already Exists in Db");
											jsonObject.put("congratsCard", false);
											return jsonObject.toString();
											}

			                    }else {
			                    	LOG.info("TASK ID is not present:: " + taskId);
			                    }
			                    		     	            
							} else {
								jsonObjectConnection.put("statusCode",400);
								jsonObjectConnection.put("message","Network connection issue or missing required mandatort parameter like customer id, task id, start and end date");
							}
						
			  }catch(Exception e) {
				  LOG.error("Expection ", e.getMessage());
				  return jsonObjectConnection.toString();

			  } finally {
				  sqlDBUtil.sqlConnectionClose(null, connection, null, LOG);
			  }
			  return jsonObjectConnection.toString();
	        
	    }
	  
	//This method id for inserting records into Customer Task Status Table once the user clicks on all the 3 Tasks  
	  public int insertCustTaskStatus(Connection connection,int customerId, int tskWkyCount) {
		 LOG.info("Start of Method insertCustTaskStatus");
		  int custTaskInsertStatus = 0; 
		  PreparedStatement insertIntoCustTskStatusPS= null;
		  try {
	          	  insertIntoCustTskStatusPS = connection.prepareStatement(SqlConstant.INSERT_INTO_CUST_TASK_STATUS_QUERY);
	          	  //This will be always 1
	          	 String custTaskChanceCount = "1";
	          	 String custTaskWkyStatus = "Y";
	          	 
				//Set insert coulmn values
	          	insertIntoCustTskStatusPS.setInt(1,customerId);
	          	insertIntoCustTskStatusPS.setInt(2,tskWkyCount);
	          	insertIntoCustTskStatusPS.setString(3, custTaskChanceCount);
	          	insertIntoCustTskStatusPS.setString(4, custTaskWkyStatus);
	          	java.util.Date date = new java.util.Date();
	            java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
	            insertIntoCustTskStatusPS.setDate(5, sqlCurrentDate);
		        int isInsertTask = insertIntoCustTskStatusPS.executeUpdate();
		         if (isInsertTask != 0) {
		        	 custTaskInsertStatus = 100;
		       	  }
		  }catch(Exception e) {
			  LOG.error("Exception inside method insertCustTaskStatus" , e.getMessage());
		  } finally {
			  sqlDBUtil.sqlResultSetAndPreparedStatementClose(null, insertIntoCustTskStatusPS, LOG);
		  }
		  
		return custTaskInsertStatus;
		}

	public int readingCustomerTotalChanceCount(Connection connection,int customerId, String taskStartDate, String taskEndDate) { 
		  System.out.println("customerId present =====>" + customerId);
		  LOG.info("start of method readingCustomerTotalChanceCount");
		  int custTaskStatusReslutSetSize = 0;
		  PreparedStatement tskpreparedStmt = null;
		  ResultSet custtaskStaResultSet = null;
	    			try {
							tskpreparedStmt = connection.prepareStatement(SqlConstant.SELECT_CUSTTK_FOR_START_AND_END_DATE_QUERY);
							tskpreparedStmt.setInt(1, customerId);
							tskpreparedStmt.setDate(2, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
							tskpreparedStmt.setDate(3, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
							custtaskStaResultSet = tskpreparedStmt.executeQuery();
							while (custtaskStaResultSet.next()) {
								custTaskStatusReslutSetSize++;
							}
							System.out.println("tskwkyReslutSetSize =====>" + custTaskStatusReslutSetSize);
							LOG.info("tskwkyReslutSetSize =====>" + custTaskStatusReslutSetSize);
	    		           
	    			}catch(Exception e) {
	    				LOG.error("Exception inside method validateTaksIDPresentforCustomer" , e.getMessage());
	    			} finally {
	    				sqlDBUtil.sqlResultSetAndPreparedStatementClose(custtaskStaResultSet, tskpreparedStmt, LOG);
	    			}
	    			LOG.info("End of method readingCustomerTotalChanceCount");
	    			return custTaskStatusReslutSetSize;
	    	
	    }
	
	public int validateTaksIDPresentforCustomer(Connection connection, int taskId, int customerId) {
		LOG.info("Start of method validateTaksIDPresentforCustomer");
		int tskIdReslutSetSize = 0;
		PreparedStatement tskpreparedStmt = null;
		ResultSet tskIdResultSet = null;
		
		try {
			tskpreparedStmt = connection.prepareStatement(SqlConstant.SELECT_CUSTTK_TABLE_FOR_PRESENT_TASK_FOR_CUSTOMERID);
			tskpreparedStmt.setInt(1, taskId);
			tskpreparedStmt.setInt(2, customerId);
			tskIdResultSet = tskpreparedStmt.executeQuery();
			while (tskIdResultSet.next()) {
				tskIdReslutSetSize++;
			}
		} catch (Exception e) {
			LOG.error("Exception inside method validateTaksIDPresentforCustomer" , e.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(tskIdResultSet, tskpreparedStmt, LOG);
		}
		LOG.info("Task Present in CUSTTK table status : - " + tskIdReslutSetSize);
		LOG.info("End of method validateTaksIDPresentforCustomer");
		return  tskIdReslutSetSize;
		
	}
	
	public int insertIntoCustTask(Connection connection, int taskId, int customerId, String custTaskCompleteIndicator, String taskStartDate, String taskEndDate) {
		LOG.info("Start of method insertIntoCustTask");
		PreparedStatement insertIntoCustTskPS = null;
		int isInsert = 0;
		try {
            	insertIntoCustTskPS = connection.prepareStatement(SqlConstant.INSERT_INTO_CUST_TABLE_QUERY);
 				insertIntoCustTskPS.setInt(1,customerId);
 				insertIntoCustTskPS.setInt(2,taskId);
 				insertIntoCustTskPS.setString(3, custTaskCompleteIndicator);
 	            insertIntoCustTskPS.setDate(4, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
 	            insertIntoCustTskPS.setDate(5, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
 	            java.util.Date date = new java.util.Date();
 	            insertIntoCustTskPS.setDate(6, new java.sql.Date(date.getTime()));
 	            isInsert = insertIntoCustTskPS.executeUpdate();
		} catch (Exception e) {
			LOG.error("Exception inside method insertIntoCustTask" ,  e.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(null, insertIntoCustTskPS, LOG);
		}
		LOG.info("Successfully insert data in tabale  -" + isInsert);
		LOG.info("End of method insertIntoCustTask");
		return  isInsert;
		
	}
	
	public int getTaskWeeklyCount(Connection connection , String taskStartDate, String taskEndDate ) {
		LOG.info("End of method insertIntoCustTask");
		PreparedStatement tskWkyPreparedStmt = null;
		ResultSet tskwkyResultSet = null;
		int tskwkyReslutSetSize = 0;
		int tskWkyCount = 0;
		 try {
			 // First get TSKWKY_CT field from TSKWKY table for the given Start date and End Date.
			tskWkyPreparedStmt = connection.prepareStatement(SqlConstant.TASK_WEEKLY_COUNT_SELECT_QUERY);
			tskWkyPreparedStmt.setDate(1, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
			tskWkyPreparedStmt.setDate(2, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));

			tskwkyResultSet = tskWkyPreparedStmt.executeQuery();
			
			while (tskwkyResultSet.next()) {
				tskwkyReslutSetSize++;
				tskWkyCount = tskwkyResultSet.getInt("TSKWKY_CT");
												
			}
			LOG.info("Get Task wky count====>" + tskWkyCount);
			LOG.info("End of method getTaskWeeklyCount");
			
		}catch (Exception e) {
			LOG.error("Exception inside method getTaskWeeklyCount" ,  e.getMessage());
		}finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(tskwkyResultSet, tskWkyPreparedStmt, LOG);
		}
		return tskWkyCount;
	}
}





