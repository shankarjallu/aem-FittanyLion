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
                String query = " insert into FITTTANYTASK (TSK_TL, TSK_DS, TSK_SQ, TSK_STRT_DT, TSK_END_DT)" +
                    " values (?, ?, ?, ?, ?)";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject taskObj = jsonArray.getJSONObject(i);
                    System.out.println(taskObj.get("taskTitle") + "-----" + taskObj.get("taskDescription"));
                    PreparedStatement preparedStmt = connection.prepareStatement(query);
                    preparedStmt.setString(1, taskObj.getString("taskTitle"));
                    preparedStmt.setString(2, taskObj.getString("taskDescription"));
                    preparedStmt.setString(3, taskObj.getString("taskSequence"));
                    java.util.Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                    java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                    java.util.Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                    java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
                    preparedStmt.setDate(4, sqlStartDate);
                    preparedStmt.setDate(5, sqlEndDate);
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
       String sql = "select * from FITTTANYTASK where TSK_STRT_DT >= ? and TSK_END_DT <= ?";

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
        String dateRangeSql = "select * from FITTTANYTASK WHERE sysdate BETWEEN TSK_STRT_DT AND TSK_END_DT";
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
          String updateQuery = " update FITTTANYTASK SET TSK_TL = ? , TSK_DS = ?, TSK_SQ = ?, TSK_STRT_DT = ?, TSK_END_DT = ? where TSK_SQ='" + Integer.parseInt(taskObj.getString("taskSequence")) + "'";
          PreparedStatement updatePreparedStmt = connection.prepareStatement(updateQuery);
          updatePreparedStmt.setString(1, taskObj.getString("taskTitle"));
          updatePreparedStmt.setString(2, taskObj.getString("taskDescription"));
          updatePreparedStmt.setInt(3, Integer.parseInt(taskObj.getString("taskSequence")));
          java.util.Date updateStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
          java.sql.Date sqlUpdateStartDate = new java.sql.Date(updateStartDate.getTime());
          java.util.Date updateEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
          java.sql.Date sqlUpdateEndDate = new java.sql.Date(updateEndDate.getTime());
          updatePreparedStmt.setDate(4, sqlUpdateStartDate);
          updatePreparedStmt.setDate(5, sqlUpdateEndDate);
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
        	   
        	   
        	   
               for (int i = 0; i < jsonArray.length(); i++) {

                     JSONObject taskObj = jsonArray.getJSONObject(i);
                     System.out.println("77777777777===<=>>>test>>>=====>");

                     //     insert into FITTTANYTASK(TSK_STRT_DT,TSK_END_DT,TSK_TL, TSK_DS, TSK_SQ) VALUES (sysdate, sysdate, 'fITTANY', 'SDFVGDBFD', 2);

                  
                     String query = " insert into FITTTANYTASK(TSK_STRT_DT,TSK_END_DT,TSK_TL, TSK_DS, TSK_SQ)" +
                      " values (?, ?, ?, ?, ?)";
                     PreparedStatement insertPreparedStmt = connection.prepareStatement(query);
                     System.out.println("This is StartDate before parse ====>" + taskStartDate);

                     java.util.Date insertStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                     java.sql.Date sqlInsertStartDate = new java.sql.Date(insertStartDate.getTime());
                     java.util.Date insertEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                     java.sql.Date sqlInsertEndDate = new java.sql.Date(insertEndDate.getTime());
                     
                     System.out.println("This is sqlInsertStartDate after parse ====>" + sqlInsertStartDate);
                     insertPreparedStmt.setDate(1, sqlInsertStartDate);
                     insertPreparedStmt.setDate(2, sqlInsertEndDate);

                     insertPreparedStmt.setString(3, taskObj.getString("taskTitle"));
                     insertPreparedStmt.setString(4, taskObj.getString("taskDescription"));
                     insertPreparedStmt.setInt(5, Integer.parseInt(taskObj.getString("taskSequence")));

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