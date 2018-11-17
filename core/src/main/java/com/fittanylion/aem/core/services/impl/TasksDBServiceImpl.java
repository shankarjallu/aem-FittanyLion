package com.fittanylion.aem.core.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
        String taskItems = request.getParameter("taskItems");
        System.out.println(taskItems);
        try {
            JSONObject jsnobject = new JSONObject(taskItems);
            String taskStartDate = jsnobject.getString("taskStartDate");
            String taskEndDate = jsnobject.getString("taskEndDate");
            JSONArray jsonArray = jsnobject.getJSONArray("tasks");
            int isInsert = 0;
            if (dataSource != null) {
                final Connection connection = dataSource.getConnection();
                final Statement statement = connection.createStatement();
                String query = " insert into tasks (taskTitle, taskDescription, taskSequence, start_date, due_date)" +
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
        String taskItems = request.getParameter("taskItems");
        System.out.println(taskItems);
        try {
            JSONObject jsnobject = new JSONObject(taskItems);
            String taskStartDate = jsnobject.getString("taskStartDate");
            String taskEndDate = jsnobject.getString("taskEndDate");
            java.sql.Date sqlStartDate;
            java.sql.Date sqlEndDate;
            JSONArray jsonArray = jsnobject.getJSONArray("tasks");
            int isInsert = 0;       
            Connection connection = null;
            if (dataSource != null) {
                connection = dataSource.getConnection();
                final Statement statement = connection.createStatement();
                for (int i = 0; i < jsonArray.length(); i++) {
                	JSONObject taskObj = jsonArray.getJSONObject(i);
                    System.out.println(taskObj.get("taskTitle") + "-----" + taskObj.get("taskDescription"));
                    java.util.Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                    sqlStartDate = new java.sql.Date(startDate.getTime());
                    java.util.Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                    sqlEndDate = new java.sql.Date(endDate.getTime());
                    String sql = "select * from tasks where start_date >= ? and due_date <= ?";
                    PreparedStatement preparedStmt = connection.prepareStatement(sql);
    	            preparedStmt.setDate(1, sqlStartDate);
    	            preparedStmt.setDate(2, sqlEndDate);
    	            ResultSet resultSet = preparedStmt.executeQuery();
    	            int tasksRecordStatus = 0;
    	            while(resultSet.next()){
    	            	tasksRecordStatus++;
    	                System.out.println(resultSet.getString(1));
    	            }
    	            if(tasksRecordStatus > 0) {
    	            	java.util.Date date = new java.util.Date();
                        java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
                        String dateRangeSql = "select * from tasks WHERE sysdate BETWEEN start_date AND due_date;";
                        PreparedStatement dateRangeSqlStmt = connection.prepareStatement(dateRangeSql);
                        dateRangeSqlStmt.setDate(1, sqlStartDate);
        	            dateRangeSqlStmt.setDate(2, sqlEndDate);
        	            ResultSet dateRangeSqlResultSet = dateRangeSqlStmt.executeQuery();
        	            int tasksDateRangeStatus = 0;
        	            while(dateRangeSqlResultSet.next()){
        	            	tasksDateRangeStatus++;
        	            }
        	            if(tasksDateRangeStatus > 0) {
        	            	return "Say an Error message u cannot update the tasks";
        	            	
        	          
					//		jsonObject.put("statusCode",400);
        			//		 jsonObject.put("message","Say an Error message u cannot update the tasks");
        	            }else {
        	            	String updateQuery = " update tasks (taskTitle, taskDescription, taskSequence, start_date, due_date)" +
        	                        " values (?, ?, ?, ?, ?)";
        	            	PreparedStatement updatePreparedStmt = connection.prepareStatement(updateQuery);
        	            	updatePreparedStmt.setString(1, taskObj.getString("taskTitle"));
        	            	updatePreparedStmt.setString(2, taskObj.getString("taskDescription"));
        	            	updatePreparedStmt.setString(3, taskObj.getString("taskSequence"));
                            java.util.Date updateStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                            java.sql.Date sqlUpdateStartDate = new java.sql.Date(updateStartDate.getTime());
                            java.util.Date updateEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                            java.sql.Date sqlUpdateEndDate = new java.sql.Date(updateEndDate.getTime());
                            updatePreparedStmt.setDate(4, sqlUpdateStartDate);
                            updatePreparedStmt.setDate(5, sqlUpdateEndDate);
                            isInsert = preparedStmt.executeUpdate();
        	            	
        	            }
    	            	
    	            }else {
    	            	String query = " insert into tasks (taskTitle, taskDescription, taskSequence, start_date, due_date)" +
    	                        " values (?, ?, ?, ?, ?)";
    	            	PreparedStatement insertPreparedStmt = connection.prepareStatement(query);
                        preparedStmt.setString(1, taskObj.getString("taskTitle"));
                        preparedStmt.setString(2, taskObj.getString("taskDescription"));
                        preparedStmt.setString(3, taskObj.getString("taskSequence"));
                        java.util.Date insertStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
                        java.sql.Date sqlInsertStartDate = new java.sql.Date(insertStartDate.getTime());
                        java.util.Date insertEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
                        java.sql.Date sqlInsertEndDate = new java.sql.Date(insertEndDate.getTime());
                        preparedStmt.setDate(4, sqlInsertStartDate);
                        preparedStmt.setDate(5, sqlInsertEndDate);
                        isInsert = preparedStmt.executeUpdate();
    	                }
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
}