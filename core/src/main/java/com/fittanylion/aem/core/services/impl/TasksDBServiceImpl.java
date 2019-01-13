package com.fittanylion.aem.core.services.impl;


import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.services.TasksDBService;

@Component(immediate = true, service = TasksDBService.class)
public class TasksDBServiceImpl implements TasksDBService {
	
	String taskCompleteIndicator = "N";
	int taskRewardPointCount = 1;
	 java.util.Date date = new java.util.Date();
     java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
    private static final Logger LOG = LoggerFactory.getLogger(TasksDBService.class);
    @Override
    public String insertTasksIntoDB(DataSource dataSource, SlingHttpServletRequest request) {
        String insertStatus = "failured";
     //   String taskItems = request.getParameter("taskItems");
     //   System.out.println(taskItems);
        try {
           
            
            StringBuilder sb = new StringBuilder();
              BufferedReader br = request.getReader();
              String str = null;
              while ((str = br.readLine()) != null) {
                  sb.append(str);
              }
             
              System.out.println("Get JSON Object 11111111====>");
              JSONObject jsnobject = new JSONObject(sb.toString());
            

           
           
            String taskStartDate = jsnobject.getString("taskStartDate");
            String taskEndDate = jsnobject.getString("taskEndDate");
            JSONArray jsonArray = jsnobject.getJSONArray("tasks");
            int isInsert = 0;
            if (dataSource != null) {
                final Connection connection = dataSource.getConnection();
                final Statement statement = connection.createStatement();
                String query = " insert into TSK (TSK_TTL_NM, TSK_DS, TSK_MAN_DS, TSK_SEQ_NO, TSK_STRT_DT, TSK_END_DT,TSK_CMPL_IN, TSK_RWD_PNT_CT, TSK_RCD_MNTD_TS)" +
                    " values (?, ?, ?, ?, ?,?, ?, ?, ?)";
                
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject taskObj = jsonArray.getJSONObject(i);
                    System.out.println(taskObj.get("taskTitle") + "-----" + taskObj.get("taskDescription"));
                    PreparedStatement preparedStmt = connection.prepareStatement(query);
                    preparedStmt.setString(1, taskObj.getString("taskTitle"));
                    preparedStmt.setString(2, taskObj.getString("taskDescription"));
                    preparedStmt.setString(3, taskObj.getString("taskManualDescription"));
                    preparedStmt.setInt(4, taskObj.getInt("taskSequence"));
                    java.util.Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                    java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                    java.util.Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                    java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
                    preparedStmt.setDate(5, sqlStartDate);
                    preparedStmt.setDate(6, sqlEndDate);
                    
                    preparedStmt.setString(7, taskCompleteIndicator);
                    preparedStmt.setInt(8, taskRewardPointCount);
                   
                    preparedStmt.setDate(9, sqlCurrentDate);
                    
                    isInsert = preparedStmt.executeUpdate();
                }
                if (isInsert != 0)
                    insertStatus = "success";
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            insertStatus = e.getMessage();
            System.out.println(e.getMessage());
            LOG.error("Exception in TasksDBService " + e.getMessage());
        }
        return insertStatus;
    }
   
    @Override
    public String verifyTaskTableForUpdate(DataSource dataSource, SlingHttpServletRequest request) {
     String insertStatus = "failured";
     JSONObject resultObj =null;


     //  String taskItems = request.getParameter("taskItems");
     //    System.out.println(taskItems);
     try {
      //  JSONObject jsnobject = new JSONObject(taskItems);


      StringBuilder sb = new StringBuilder();
      BufferedReader br = request.getReader();
      String str = null;
      while ((str = br.readLine()) != null) {
       sb.append(str);
      }

      System.out.println("Get JSON Object 222222222====>");
      JSONObject jsnobject = new JSONObject(sb.toString());
      // JSONObject jsnobject = new JSONObject(taskItems);

      String taskStartDate = jsnobject.getString("taskStartDate");
      System.out.println("Get task start date====>" + taskStartDate);
      String taskEndDate = jsnobject.getString("taskEndDate");
      java.sql.Date sqlStartDate;
      java.sql.Date sqlEndDate;
      JSONArray jsonArray = jsnobject.getJSONArray("tasks");
      int isInsert = 0;
      Connection connection = null;
      if (dataSource != null) {
       connection = dataSource.getConnection();
       final Statement statement = connection.createStatement();

       java.util.Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
       sqlStartDate = new java.sql.Date(startDate.getTime());
       java.util.Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
       sqlEndDate = new java.sql.Date(endDate.getTime());
       String sql = "select * from TSK where TSK_STRT_DT >= ? and TSK_END_DT <= ?";

       PreparedStatement preparedStmt = connection.prepareStatement(sql);
       preparedStmt.setDate(1, sqlStartDate);
       preparedStmt.setDate(2, sqlEndDate);

       System.out.println("this is sqlStartDate=====>>" + sqlStartDate);

       ResultSet resultSet = preparedStmt.executeQuery();
       System.out.println("3333333=========>");
       int tasksRecordStatus = 0;

       while (resultSet.next()) {
        System.out.println("this is getResult set=====>" + resultSet.getString(1));
        tasksRecordStatus++;
        System.out.println("44444444444=========>");
       }

       if (tasksRecordStatus > 0) {
       
        System.out.println("5555555555=========>");
        String dateRangeSql = "select * from TSK WHERE sysdate BETWEEN TSK_STRT_DT AND TSK_END_DT";
        // PreparedStatement dateRangeSqlStmt = connection.prepareStatement(dateRangeSql);
        //dateRangeSqlStmt.setDate(1, sqlStartDate);
        // dateRangeSqlStmt.setDate(2, sqlEndDate);
        ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeSql);
        int tasksDateRangeStatus = 0;
        while (dateRangeSqlResultSet.next()) {
         tasksDateRangeStatus++;
        }
        if (tasksDateRangeStatus > 0) {
            resultObj = new JSONObject();
            resultObj.put("statusCode",400);
            resultObj.put("message","Say an Error message u cannot update the tasks.");
             System.out.println("Error cant upsate" + resultObj);
         return resultObj.toString();


         //        jsonObject.put("statusCode",400);
         //         jsonObject.put("message","Say an Error message u cannot update the tasks");
        } else {
         System.out.println("666666666666=========>");
         for (int i = 0; i < jsonArray.length(); i++) {
        	 
          JSONObject taskObj = jsonArray.getJSONObject(i);
          String updateQuery = " update TSK SET TSK_TTL_NM = ? , TSK_DS = ?, TSK_MAN_DS = ?, TSK_SEQ_NO = ?, TSK_STRT_DT = ?, TSK_END_DT = ?,TSK_RCD_MNTD_TS = ? where TSK_SEQ_NO='" + Integer.parseInt(taskObj.getString("taskSequence")) + "'";
          PreparedStatement updatePreparedStmt = connection.prepareStatement(updateQuery);
          updatePreparedStmt.setString(1, taskObj.getString("taskTitle"));
          updatePreparedStmt.setString(2, taskObj.getString("taskDescription"));
          updatePreparedStmt.setString(3, taskObj.getString("taskManualDescription"));
          updatePreparedStmt.setInt(4, Integer.parseInt(taskObj.getString("taskSequence")));
          System.out.println("This is update TASK SEQ NO=====>" + taskObj.getString("taskSequence"));
          java.util.Date updateStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
          java.sql.Date sqlUpdateStartDate = new java.sql.Date(updateStartDate.getTime());
          java.util.Date updateEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
          java.sql.Date sqlUpdateEndDate = new java.sql.Date(updateEndDate.getTime());
          updatePreparedStmt.setDate(5, sqlUpdateStartDate);
          updatePreparedStmt.setDate(6, sqlUpdateEndDate);
          
          updatePreparedStmt.setDate(7, sqlCurrentDate);
          
          
          isInsert = updatePreparedStmt.executeUpdate();
          System.out.println("This is update...." + isInsert);
         }
         resultObj = new JSONObject();
         resultObj.put("statusCode",200);
         resultObj.put("message","update the tasks.");
          System.out.println("Upsate result" + resultObj);
      return resultObj.toString();


        }

       } else {
           java.util.Date date = new java.util.Date();
           SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
           String currentDateFormat = sdf.format(date);
           boolean isDateInBetweenFlag = isDateInBetweenIncludingEndPoints(startDate,endDate,date,currentDateFormat,sdf);
           System.out.println("isDateInBetweenFlag====>" + isDateInBetweenFlag);
           if (isDateInBetweenFlag) {
               resultObj = new JSONObject();
               resultObj.put("statusCode",400);
               resultObj.put("message","Say an Error message u cannot update the tasks.");
                System.out.println("Error cant upsate" + resultObj);
           // return resultObj.toString();
           } else {
        	   
        	   
        	   String inserttskwklyQuery = "insert into TSKWKY (TSKWKY_STRT_DT, TSKWKY_END_DT, TSKWKY_RCD_MNTD_TS)" + 
                       " values (?,?,?)";
PreparedStatement insertIntoTskwkly = connection.prepareStatement(inserttskwklyQuery);

java.util.Date insertStartDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
java.sql.Date sqlInsertStartDateTskwkly = new java.sql.Date(insertStartDateTskwkly.getTime());
java.util.Date insertEndDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
java.sql.Date sqlInsertEndDateTskwkly = new java.sql.Date(insertEndDateTskwkly.getTime());

insertIntoTskwkly.setDate(1, sqlInsertStartDateTskwkly);
insertIntoTskwkly.setDate(2, sqlInsertEndDateTskwkly);
insertIntoTskwkly.setDate(3, sqlCurrentDate);
isInsert = insertIntoTskwkly.executeUpdate();


//  Need to make sure data is inserted into TSKWKY.Once that is successful then below code must execute
//Get The Tskwky_ct from tskwky table
System.out.println("This is Task wky count code is excutution starts =====>");
String getTskwkyQuery = "select * from TSKWKY WHERE sysdate BETWEEN TSKWKY_STRT_DT AND TSKWKY_END_DT";
ResultSet tskwkyResultSet = statement.executeQuery(getTskwkyQuery);
int tskwkyReslutSetSize = 0;
int tskwkyCount = 0;
while(tskwkyResultSet.next()) {
	tskwkyReslutSetSize++;
	tskwkyCount = tskwkyResultSet.getInt("TSKWKY_CT");
}

System.out.println("This is Task wky count====>" + tskwkyCount);

// code ends get tskwky_ct
        	          	   
               for (int i = 0; i < jsonArray.length(); i++) {

                     JSONObject taskObj = jsonArray.getJSONObject(i);
                     System.out.println("77777777777===<=>>>test>>>=====>");

                     //     insert into FITTTANYTASK(TSK_STRT_DT,TSK_END_DT,TSK_TL, TSK_DS, TSK_SQ) VALUES (sysdate, sysdate, 'fITTANY', 'SDFVGDBFD', 2);

                  
                     String query = " insert into TSK (TSK_TTL_NM, TSK_DS, TSK_MAN_DS, TSK_SEQ_NO, TSK_STRT_DT, TSK_END_DT,TSK_CMPL_IN, TSK_RWD_PNT_CT, TSK_RCD_MNTD_TS)" +
                             " values (?, ?, ?, ?, ?,?, ?, ?, ?)";
                         
                     PreparedStatement insertPreparedStmt = connection.prepareStatement(query);
                     System.out.println("This is StartDate before parse ====>" + taskStartDate);

                     java.util.Date insertStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                     java.sql.Date sqlInsertStartDate = new java.sql.Date(insertStartDate.getTime());
                     java.util.Date insertEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                     java.sql.Date sqlInsertEndDate = new java.sql.Date(insertEndDate.getTime());
                     
                     System.out.println("This is sqlInsertStartDate after parse ====>" + sqlInsertStartDate);
                    
                  
                     
                     insertPreparedStmt.setString(1, taskObj.getString("taskTitle"));
                     insertPreparedStmt.setString(2, taskObj.getString("taskDescription"));
                     insertPreparedStmt.setString(3, taskObj.getString("taskManualDescription"));
                     insertPreparedStmt.setInt(4, Integer.parseInt(taskObj.getString("taskSequence")));
                     insertPreparedStmt.setDate(5, sqlInsertStartDate);
                     insertPreparedStmt.setDate(6, sqlInsertEndDate);
                     
                     insertPreparedStmt.setString(7, taskCompleteIndicator);
                     insertPreparedStmt.setInt(8, taskRewardPointCount);
                    
                     insertPreparedStmt.setDate(9, sqlCurrentDate);
                     

                     isInsert = insertPreparedStmt.executeUpdate();
                    }
                    resultObj = new JSONObject();
                    resultObj.put("statusCode",200);
                    resultObj.put("message","Insert the tasks.");
                     System.out.println("Inser result" + resultObj);
           }

       
     return resultObj.toString();
       }
      }
      if (isInsert != 0)
       insertStatus = "success";
      connection.close();

     } catch (Exception e) {
      e.printStackTrace();
      insertStatus = e.getMessage();
      System.out.println(e.getMessage());
      LOG.error("Exception in TasksDBService " + e.getMessage());
     }
     return insertStatus;
    }
   
    public boolean isDateInBetweenIncludingEndPoints( Date startDate, final Date endDate, Date date, String currentDate,SimpleDateFormat sdf){
       if((date.after(startDate) && (date.before(endDate))) || (currentDate.equals(sdf.format(startDate)) ||currentDate.equals(sdf.format(endDate)))){
            return true;
        }
        return false;
    }
}