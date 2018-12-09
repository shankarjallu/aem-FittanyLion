package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
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
        
        try {
            
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
                        while(passwordResultSet.next()){
                            passwordResultSetSize++;
                            customerId = passwordResultSet.getInt("CUST_ID");
                            firstName = passwordResultSet.getString("CUST_FST_NM");
                            lastName = passwordResultSet.getString("CUST_LA_NM");
                            customerAgeGroup = passwordResultSet.getString("CUST_DOB_RNG_DS");
                        }
                        if(passwordResultSetSize > 0) {
                            //jsonObject.put("statusCode",200);
                            //jsonObject.put("message","Login Success.");
                            String jsonRespObject = readingTasksDetails(statement,customerId,firstName,lastName,customerAgeGroup);
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
                return "Having issue with Datasource";
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String readingTasksDetails(Statement statement,int customerId,String firstName,String lastName,String customerAgeGroup) {
        String dateRangeSql = "select * from FITTTANYTASK WHERE sysdate BETWEEN TSK_STRT_DT AND TSK_END_DT";
        JSONObject custTasksJsonObject = new JSONObject();
        try {
            
             SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
             
             System.out.print("this is dateRangeSql=====>" + dateRangeSql);
             
            custTasksJsonObject.put("StatusCode", 200);
            custTasksJsonObject.put("CustomerId", customerId);
            custTasksJsonObject.put("CustomerAgeGroup", customerAgeGroup);
            custTasksJsonObject.put("CustomerFirstName", firstName);
            custTasksJsonObject.put("CustomerLastName", lastName);
            
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
                tasksJsonObject.put("taskTitle", dateRangeSqlResultSet.getString("TSK_TL"));
                tasksJsonObject.put("taskDescription", dateRangeSqlResultSet.getString("TSK_DS"));
              //  tasksJsonObject.put("taskUserManual", dateRangeSqlResultSet.getString("TSK_MAN_DS"));
             //   tasksJsonObject.put("TaskCompleteIndicator", dateRangeSqlResultSet.getString("TASK_CMPL_IN"));
                tasksJsonObject.put("taskSequence", dateRangeSqlResultSet.getInt("TSK_SQ"));
                tasksArray.put(tasksJsonObject);
                taskStartDate = dateFormat.format(dateRangeSqlResultSet.getDate("TSK_STRT_DT"));
                taskEndDate = dateFormat.format(dateRangeSqlResultSet.getDate("TSK_END_DT"));
            }
            
            custTasksJsonObject.put("taskStartDate", taskStartDate);
            custTasksJsonObject.put("taskEndDate", taskEndDate);
            //Reading tasks weekly table details
            readingTasksWeeklyDetails(statement,custTasksJsonObject);
            System.out.print("No String.....====>");
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
        String dateRangeFromTaskWeeklyTable = "select * from TSKWKLYY WHERE sysdate BETWEEN TSK_STRT_DT AND TSK_END_DT";
        
         try {
             ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeFromTaskWeeklyTable);
             while(dateRangeSqlResultSet.next()){
            	 System.out.print("Inside TSK WEEKLY TABLE========>");
                 custTasksJsonObject.put("taskWeekCount", dateRangeSqlResultSet.getInt("TSKWKY_CT"));
                 custTasksJsonObject.put("custWeeklyStatus", "N");
               //  custTasksJsonObject.put("taskTotalChancesCount", 0); // Doubt need to ask
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
