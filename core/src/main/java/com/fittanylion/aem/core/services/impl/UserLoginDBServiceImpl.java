package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.sql.DataSource;

import com.fittanylion.aem.core.services.UserLoginDBService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = UserLoginDBService.class)


public class UserLoginDBServiceImpl implements UserLoginDBService {


    @Override
    public String verifyUserLogin(DataSource dataSource, SlingHttpServletRequest request) {
    	JSONObject jsonObjectConnection = new JSONObject();
        try {
            
        	jsonObjectConnection.put("statusCode",400);
			jsonObjectConnection.put("message","DataBase connection issue");
			
            StringBuilder sb = new StringBuilder();
              BufferedReader br = request.getReader();
              String str = null;
              while ((str = br.readLine()) != null) {
                  sb.append(str);
              }
              JSONObject jsnobject = new JSONObject(sb.toString());
            
            
            JSONObject jsonObject = new JSONObject();
            String custusername = jsnobject.getString("username");
            String custpassword = jsnobject.getString("password");
            
            Decoder decoder = Base64.getDecoder();
            String username = new String(decoder.decode(custusername));
            String password = new String(decoder.decode(custpassword));
            
            
            
            System.out.println("Hey this is Decode username <====>" + username);
            System.out.println("Hey this is Decode password <====>" + password);
            
            
            if(dataSource != null) {
                if(dataSource != null && username != null && password != null) {
                    final Connection connection = dataSource.getConnection();
                    final Statement statement = connection.createStatement();
                    String usernameQuery = "Select * from CUST Where CUST_EMAIL_AD='" + username + "'";
                    ResultSet usernameResultSet = statement.executeQuery(usernameQuery);
                    
                    int usernameResultSize = 0;
                    while(usernameResultSet.next()){
                        usernameResultSize++;
                    }

                    if(usernameResultSize > 0) {
                        String passwordQuery = "Select * from CUST Where CUST_EMAIL_AD='" + username + "' and CUST_PW_ID='" + password + "'";
                        ResultSet passwordResultSet = statement.executeQuery(passwordQuery);
                        int passwordResultSetSize = 0;
                        int customerId = 0;
                        String firstName = null;
                        String lastName = null;
                        String customerAgeGroup = null;
                        String customerAuthKey = null;
                        while(passwordResultSet.next()){
                            passwordResultSetSize++;
                            customerId = passwordResultSet.getInt("CUST_ID");
                            firstName = passwordResultSet.getString("CUST_FST_NM");
                            lastName = passwordResultSet.getString("CUST_LA_NM");
                            customerAgeGroup = passwordResultSet.getString("CUST_DOB_RNG_DS");
                            customerAuthKey = passwordResultSet.getString("CUST_PW_TOK_NO");
                        }
                        if(passwordResultSetSize > 0) {
                           
                        	String jsonCustTasks = readingCustTasks(connection,statement,customerId);
                        	
                            String jsonRespObject = readingTasksDetails(statement,customerId,firstName,lastName,customerAgeGroup, customerAuthKey);
                            //Need to call other table to retreive data if successfull login
                            return jsonRespObject;
                        }else {
                            jsonObject.put("statusCode",400);
                            jsonObject.put("message","Password wrong.");
                            return jsonObject.toString();
                        }
                    }else {
                        jsonObject.put("statusCode",400);
                        jsonObject.put("message","User Id wrong.");
                        return jsonObject.toString();
                    }
                }
            }else {
            	return jsonObjectConnection.toString();
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObjectConnection.toString();
    }
    
    
    
    
    public String readingCustTasks(Connection connection,Statement statement, int customerId) {
		// TODO Auto-generated method stub
    	 JSONObject custTasksJson = new JSONObject();
		
    	 try {
    		 String getDatesSql = "select * from TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT  ORDER BY TSK_SEQ_NO";
    		 ResultSet dateResults = statement.executeQuery(getDatesSql);

    		 int tasksDatesStatus = 0;
    		 String custtaskStartDate = null;
    		 String custtaskEndDate = null;
    		  SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    		 while(dateResults.next()){ 
    			 
    			 tasksDatesStatus++;   			 
    			 custtaskStartDate = dateFormat.format(dateResults.getDate("TSK_STRT_DT"));
                 custtaskEndDate = dateFormat.format(dateResults.getDate("TSK_END_DT"));
                                  
    		 }
    		 
    		 if(custtaskStartDate != null) {
    			 custtaskStartDate = custtaskStartDate.replace('-','/');//replaces all occurrences of - to / 
    			  custtaskEndDate= custtaskEndDate.replace('-','/');//replaces all occurrences of - to / 
    		 }
    		
	           String getCustTaskStatusDetails = "select * from CUSTTSK where CUST_ID = ? and CUSTTSK_STRT_DT >= ? and CUSTTSK_END_DT <= ?";

				PreparedStatement tskpreparedStmt = connection.prepareStatement(getCustTaskStatusDetails);
				tskpreparedStmt.setInt(1, customerId);
				
				java.util.Date insertStartDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(custtaskStartDate);
				java.sql.Date sqlInsertStartDate = new java.sql.Date(insertStartDateTskwkly.getTime());
				
				java.util.Date insertEndDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(custtaskEndDate);
				java.sql.Date sqlInsertEndDate = new java.sql.Date(insertEndDateTskwkly.getTime());

				tskpreparedStmt.setDate(2, sqlInsertStartDate);
				tskpreparedStmt.setDate(3, sqlInsertEndDate);
				
          System.out.println("tskpreparedStmt.toString()=====>" + tskpreparedStmt.toString());
				ResultSet custtaskStaResultSet = tskpreparedStmt.executeQuery();
				
				 JSONArray custtasksArray = new JSONArray();
				
				while (custtaskStaResultSet.next()) {
					  JSONObject custtasksJsonObject = new JSONObject();
					
					
					  System.out.println("HURRAY custtaskStaResultSet.getIntTASK_ID" + custtaskStaResultSet.getInt("TSK_ID"));
					  System.out.println("HURRAY custtaskStaResultSet.ger Complete" + custtaskStaResultSet.getString("CUSTTSK_CMPL_IN"));
					  
					custtasksJsonObject.put("taskId", custtaskStaResultSet.getInt("TSK_ID"));
					custtasksJsonObject.put("custTaskCompleteIndicator", custtaskStaResultSet.getString("CUSTTSK_CMPL_IN"));
					custtasksArray.put(custtasksJsonObject);
					
		              

				}
				custTasksJson.put("custTask", custtasksArray);
    		 System.out.println("custTasksJson.toString()========>>" + custTasksJson.toString());
    	      
    	       
    	 }catch(Exception e) {
    		 e.printStackTrace();
    	 }
		return custTasksJson.toString();
	}
    
    
    

	public String readingTasksDetails(Statement statement,int customerId,String firstName,String lastName,String customerAgeGroup, String customerAuthKey) {
        String dateRangeSql = "select * from TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT  ORDER BY TSK_SEQ_NO";
        JSONObject custTasksJsonObject = new JSONObject();
       
        try {
            
             SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
             
            custTasksJsonObject.put("StatusCode", 200);
            custTasksJsonObject.put("customerId", customerId);
            custTasksJsonObject.put("customerAgeGroup", customerAgeGroup);
            custTasksJsonObject.put("customerFirstName", firstName);
            custTasksJsonObject.put("customerLastName", lastName);
            custTasksJsonObject.put("customerAuthKey", customerAuthKey);

            
    //Hardcoding for now. Pull this data from CUSTTSKSTA table(column name: CUSTTSKSTA_CHNC_CT ),since u don’t initially we will have 0
            int taskTotalChancesCount = 0;
            custTasksJsonObject.put("taskTotalChancesCount", taskTotalChancesCount);
            
            ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeSql);
            int tasksDateRangeStatus = 0;
            JSONArray tasksArray = new JSONArray();
            String taskStartDate = null;
            String taskEndDate = null;
            while(dateRangeSqlResultSet.next()){
            	 System.out.print("Inside date range sql result Set =====>");
                 
                tasksDateRangeStatus++;
                JSONObject tasksJsonObject = new JSONObject();
                tasksJsonObject.put("taskID", dateRangeSqlResultSet.getInt("TSK_ID"));
                tasksJsonObject.put("taskTitle", dateRangeSqlResultSet.getString("TSK_TTL_NM"));
                tasksJsonObject.put("taskDescription", dateRangeSqlResultSet.getString("TSK_DS"));
                tasksJsonObject.put("taskUserManual", dateRangeSqlResultSet.getString("TSK_MAN_DS"));
                
                tasksJsonObject.put("TaskCompleteIndicator", dateRangeSqlResultSet.getString("TSK_CMPL_IN"));
                
                tasksJsonObject.put("taskSequence", dateRangeSqlResultSet.getInt("TSK_SEQ_NO"));
                tasksArray.put(tasksJsonObject);
                taskStartDate = dateFormat.format(dateRangeSqlResultSet.getDate("TSK_STRT_DT"));
                taskEndDate = dateFormat.format(dateRangeSqlResultSet.getDate("TSK_END_DT"));
            }
            
            custTasksJsonObject.put("taskStartDate", taskStartDate);
            custTasksJsonObject.put("taskEndDate", taskEndDate);
            //Reading tasks weekly table details
            readingTasksWeeklyDetails(statement,custTasksJsonObject);
         
            custTasksJsonObject.put("tasks", tasksArray);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return custTasksJsonObject.toString();
    }
    
    public void readingTasksWeeklyDetails(Statement statement,JSONObject custTasksJsonObject) {
        String dateRangeFromTaskWeeklyTable = "select * from TSKWKY WHERE trunc(sysdate) BETWEEN TSKWKY_STRT_DT AND TSKWKY_END_DT";
        
         try {
             ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeFromTaskWeeklyTable);
             while(dateRangeSqlResultSet.next()){
            	 System.out.print("Inside TSK WEEKLY TABLE========>");
                 custTasksJsonObject.put("taskWeekCount", dateRangeSqlResultSet.getInt("TSKWKY_CT"));
                
              
             }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
}
