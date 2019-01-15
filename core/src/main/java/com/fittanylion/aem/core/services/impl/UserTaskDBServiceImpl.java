package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.sql.DataSource;
import java.util.Date;

import com.fittanylion.aem.core.services.UserTaskDBService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = UserTaskDBService.class)

public class UserTaskDBServiceImpl implements UserTaskDBService {

	  @Override
	    public String verifyUserTasksPost(DataSource dataSource, SlingHttpServletRequest request) {
		  String insertStatus = "failured";
		 

			JSONObject jsonObjectConnection = new JSONObject();
			  try{
				   jsonObjectConnection.put("statusCode",400);
					jsonObjectConnection.put("message","Database connection issue");
					
					
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
								
								System.out.println("taskStartDate====>" + taskStartDate);
								System.out.println("taskEndDate======>" + taskEndDate);
								System.out.println("customerId=======>" + customerId);
								
								final Connection connection = dataSource.getConnection();
			                    final Statement statement = connection.createStatement();
			                    
			                  //Need to validate if a record exists in CUSTTSK for with TASD_ID for the given date ranges  
			                    if(taskId > 0) {
			                    	try {

										String getTskwkyQuery = "select * from CUSTTSK where TSK_ID >= ? ";

										PreparedStatement tskpreparedStmt = connection.prepareStatement(getTskwkyQuery);
										tskpreparedStmt.setInt(1, taskId);
										
										ResultSet tskIdResultSet = tskpreparedStmt.executeQuery();
										int tskIdReslutSetSize = 0;
										
										while (tskIdResultSet.next()) {
											tskIdReslutSetSize++;
											
										}
										
									

										if(tskIdReslutSetSize > 0) {
											jsonObject.put("statusCode",400);
											jsonObject.put("message","This Task Id already Exists in Db");
											jsonObject.put("congratsCard", false);
											return jsonObject.toString();
										}else {
																					}
										  System.out.println("Before Insett====>");
							             	String insetQuery = " insert into CUSTTSK( CUST_ID, TSK_ID, CUSTTSK_CMPL_IN, CUSTTSK_STRT_DT, CUSTTSK_END_DT, CUSTTSK_RCD_MNTD_TS)"
													+ " values (?, ?, ?, ?, ?,?)";
							    				//Get Prepared Statement object 
							                	 PreparedStatement insertIntoCustTskPS = connection.prepareStatement(insetQuery);
							    				

							     				//Set insert coulmn values
							     				insertIntoCustTskPS.setInt(1,customerId);
							     				insertIntoCustTskPS.setInt(2,taskId);
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
							     	            
							     	           int isInsert = insertIntoCustTskPS.executeUpdate();
							     	           
							     	          //NEED to check if their are 3 TSK_ID records found in CUSTTSK for given Start Date and End date.If yes INSERT record into CUSTTSKSTA
								     	          	           
							     	           int taskCount = 0;
							     	          taskCount =  readingCustomerTotalChanceCount(connection,customerId,taskStartDate,taskEndDate);
							     	         int insertCustTaskStatusCount = 0;
							     	         if(taskCount == 3) {
							     	        	 System.out.println("Task Insert into another Table");
						// So once we have 3 records found then INSERT the records into CUSTTSKSTA table.
							     	        	 						     	      
							     	        	 try {
							     	        		 // First get TSKWKY_CT field from TSKWKY table for the given Start date and End Date.
							     	        		String getTskWkyQuery = "select * from TSKWKY where TSKWKY_STRT_DT >= ? and TSKWKY_END_DT <= ?";

													PreparedStatement tskWkyPreparedStmt = connection.prepareStatement(getTskWkyQuery);
													tskWkyPreparedStmt.setDate(1, sqlInsertTaskStartDate);
													tskWkyPreparedStmt.setDate(2, sqlInsertTaskEndtDate);

													ResultSet tskwkyResultSet = tskWkyPreparedStmt.executeQuery();
													int tskwkyReslutSetSize = 0;
													int tskWkyCount = 0;
													while (tskwkyResultSet.next()) {
														tskwkyReslutSetSize++;
														tskWkyCount = tskwkyResultSet.getInt("TSKWKY_CT");
														System.out.println("Get Task wky count====>" + tskWkyCount);
																						
													}
													
									//Insert Tasks into CUSTOMER TASK STATUS
													
													insertCustTaskStatusCount =	insertCustTaskStatus(connection,customerId, tskWkyCount);
													
													System.out.println("insertCustTaskStatusCount 111222333====>" + insertCustTaskStatusCount);
													
													
							     	        	 }catch(Exception e) {
							     	        		 e.printStackTrace();
							     	        	 }
							     	         }
							     	           
							     	          System.out.println("Hello Closing.....=>");
							     	          connection.close();
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
									

									} catch (Exception e) {
										e.printStackTrace();
										 

									}
			                    }else {
			                    	
			                    }
			                    		     	            
			     	           
							}
						
						
					
			  }catch(Exception e) {
				  e.printStackTrace();
				  return jsonObjectConnection.toString();

			  }
			  return jsonObjectConnection.toString();
	    	
	        
	    }
	  
	  
	  public int insertCustTaskStatus(Connection connection,int customerId, int tskWkyCount) {
		 //This method id for inserting records into Customer Task Status Table once the user clicks on all the 3 Tasks  
		  int custTaskInsertStatus = 0; 
		  try {
		 			  
			  String insertCustTaskStatusQuery = " insert into CUSTTSKSTA( CUST_ID, TSKWKY_CT, CUSTTSKSTA_CHNC_CT, CUSTTSKSTA_WKY_STA_CD,CUSTTSKSTA_RCD_MNTD_TS)"
						+ " values (?, ?, ?, ?, ?)";
					//Get Prepared Statement object 
	          	 PreparedStatement insertIntoCustTskStatusPS = connection.prepareStatement(insertCustTaskStatusQuery);
					
	          	  //This will be always 1
	          	 String custTaskChanceCount = "1";
	          	 String custTaskWkyStatus = "Y";
	          	 
					//Set insert coulmn values
	          	insertIntoCustTskStatusPS.setInt(1,customerId);
	          	insertIntoCustTskStatusPS.setInt(2,tskWkyCount);
	          	insertIntoCustTskStatusPS.setString(3, custTaskChanceCount);
	          	insertIntoCustTskStatusPS.setString(4, custTaskWkyStatus);
	          	
	          	 System.out.println("custTaskChanceCount Hurray=====>" + custTaskChanceCount);
	          	System.out.println("This is task Week tskWkyCount=====>" + tskWkyCount);
	          	
	          	java.util.Date date = new java.util.Date();
	            java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
	            insertIntoCustTskStatusPS.setDate(5, sqlCurrentDate);
					     
		           int isInsertTask = insertIntoCustTskStatusPS.executeUpdate();
		           
		           if (isInsertTask != 0) {
		        	   custTaskInsertStatus = 100;

		       	        	 }
			  
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		  
		  
		  
		return custTaskInsertStatus;
			}

	  
	  
	public int readingCustomerTotalChanceCount(Connection connection,int customerId, String taskStartDate, String taskEndDate) { 
	    	//String getTotalChanceCount = "select * from CUSTTSKSTA where "
		  System.out.println("customerId present =====>" + customerId);
		  int custTaskStatusReslutSetSize = 0;
	    			try {
	    				
	    				  java.util.Date custStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
	    		           java.sql.Date custsqlTaskStartDate = new java.sql.Date(custStartDate.getTime());
	    		          
	    		           java.util.Date custEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
	    		           java.sql.Date custsqlTaskEndDate = new java.sql.Date(custEndDate.getTime());
	    		           	           
	    		           System.out.println("custsqlTaskStartDate =====>" + custsqlTaskStartDate);
	    		           System.out.println("custsqlTaskEndDate" + custsqlTaskEndDate);
	    		           
	    		           String getCustTaskStatus = "select * from CUSTTSK where CUST_ID = ? and CUSTTSK_STRT_DT >= ? and CUSTTSK_END_DT <= ?";

							PreparedStatement tskpreparedStmt = connection.prepareStatement(getCustTaskStatus);
							tskpreparedStmt.setInt(1, customerId);
							tskpreparedStmt.setDate(2, custsqlTaskStartDate);
							tskpreparedStmt.setDate(3, custsqlTaskEndDate);
                        System.out.println("tskpreparedStmt.toString()=====>" + tskpreparedStmt.toString());
							ResultSet custtaskStaResultSet = tskpreparedStmt.executeQuery();
							
							
							while (custtaskStaResultSet.next()) {
							
								custTaskStatusReslutSetSize++;
							//	custTaskStatusReslutSetSize = custtaskStaResultSet.getInt(1);
								System.out.println("Inside loopp.....=>");

							}
							System.out.println("tskwkyReslutSetSize =====>" + custTaskStatusReslutSetSize);
	    		           return custTaskStatusReslutSetSize;
	    			}catch(Exception e) {
	    				 
	    				 e.printStackTrace();
	    				 return custTaskStatusReslutSetSize;
	    			}
	    	
	    }
	
}





