package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
       // String taskItems = request.getParameter("taskItems");
     //   System.out.println(taskItems);
        try {
        	
        	
        	StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            
            JSONObject jsnobject = new JSONObject(sb.toString());
            
          //  JSONObject jsnobject = new JSONObject(taskItems);
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
}