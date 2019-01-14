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
							     	           
							     	           
							     	          System.out.println("Hello Closing.....=>");
							     	          connection.close();
							     	         if (isInsert != 0) {
									     	        //   insertStatus = "success";
									     	          jsonObject.put("statusCode",200);
														jsonObject.put("message","Task Sucessfully inserted");
														return jsonObject.toString();

									     	         }else {
									     	        	 
									     	        	  jsonObject.put("statusCode",400);
															jsonObject.put("message","Error while posting data");
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
	
}

