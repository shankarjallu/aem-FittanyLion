package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
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
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;

@Component(immediate = true, service = TasksDBService.class)
public class TasksDBServiceImpl implements TasksDBService {

	String taskCompleteIndicator = "N";
	int taskRewardPointCount = 1;
	java.util.Date date = new java.util.Date();
	java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
	private static final Logger LOG = LoggerFactory.getLogger(TasksDBServiceImpl.class);



	@Override
	public String verifyTaskTableForUpdate(DataSource dataSource, SlingHttpServletRequest request) {
		LOG.info("Start of method verifyTaskTableForUpdate");
		String insertStatus = "failured";
		JSONObject resultObj = null;
		Connection connection = null;
		PreparedStatement updatePreparedStmt = null;
		PreparedStatement insertPreparedStmt = null;
			try 
			{
				int isInsert = 0;
				LOG.info("Datasouce in TAKSDBSERVICE Impl==>");
				if (dataSource != null) {
					connection = dataSource.getConnection();
					StringBuilder sb = new StringBuilder();
					BufferedReader br = request.getReader();
					String str = null;
					while ((str = br.readLine()) != null) {
						sb.append(str);
					}
					JSONObject jsnobject = new JSONObject(sb.toString());
					String taskStartDate = jsnobject.getString("taskStartDate");
					String taskEndDate = jsnobject.getString("taskEndDate");
					JSONArray jsonArray = jsnobject.getJSONArray("tasks");
					
					if (tasksAvailableforStartAndEndDate(connection, taskStartDate, taskEndDate) > 0) {
						if (taskCheckInBetweenFromStartAndEndDate(connection) > 0) {
							
							for (int i = 0; i < jsonArray.length(); i++) {
								
								JSONObject taskObj = jsonArray.getJSONObject(i);
								//String updateQuery = " update TSK SET TSK_TTL_NM = ? , TSK_DS = ?, TSK_MAN_DS = ?, TSK_SEQ_NO = ?, TSK_STRT_DT = ?, TSK_END_DT = ?,TSK_RCD_MNTD_TS = ? where TSK_STRT_DT = ? AND TSK_END_DT = ? AND TSK_SEQ_NO= ?";
								 updatePreparedStmt = connection.prepareStatement(SqlConstant.UPDATE_TASK_QUERY);
								updatePreparedStmt.setString(1, taskObj.getString("taskTitle"));
								updatePreparedStmt.setString(2, taskObj.getString("taskDescription"));
								updatePreparedStmt.setString(3, taskObj.getString("taskManualDescription"));
								updatePreparedStmt.setInt(4, Integer.parseInt(taskObj.getString("taskSequence")));
								updatePreparedStmt.setDate(5, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
								updatePreparedStmt.setDate(6, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
								updatePreparedStmt.setDate(7, new java.sql.Date(date.getTime()));
								updatePreparedStmt.setDate(8, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
								updatePreparedStmt.setDate(9, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
								updatePreparedStmt.setInt(10, Integer.parseInt(taskObj.getString("taskSequence")));
								
								isInsert = updatePreparedStmt.executeUpdate();
								LOG.info("This is update TASK SEQ NO=====>" + taskObj.getString("taskSequence") );
								LOG.info("This is update...." + isInsert );
							}
							resultObj = new JSONObject();
							resultObj.put("statusCode", 200);
							resultObj.put("message", "The Tasks were updated.");
							System.out.println("Upsate result" + resultObj);
							return resultObj.toString();
							
														
						} else {
							
							resultObj = new JSONObject();
							resultObj.put("statusCode", 400);
							resultObj.put("message", "You cannot update the tasks for current week");
							LOG.info("Error cant upsate " + resultObj);
							return resultObj.toString();

						}
	
					} else {
						boolean isDateInBetweenFlag = isDateInBetweenIncludingEndPoints(taskStartDate,taskEndDate);
						if (isDateInBetweenFlag) {
							resultObj = new JSONObject();
							resultObj.put("statusCode", 400);
							resultObj.put("message", "You cannot update the tasks for current week");
							
						} else {
							int insertTskWkyCount = inserTaskWeekCount(connection, taskStartDate, taskEndDate );
							int tskwkyCount = taskWeeklyChanceCount(connection, taskStartDate, taskEndDate );
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject taskObj = jsonArray.getJSONObject(i);
								insertPreparedStmt = connection.prepareStatement(SqlConstant.INSERT_INTO_TASK_TABLE_QUERY);
								insertPreparedStmt.setString(1, taskObj.getString("taskTitle"));
								insertPreparedStmt.setString(2, taskObj.getString("taskDescription"));
								insertPreparedStmt.setString(3, taskObj.getString("taskManualDescription"));
								insertPreparedStmt.setInt(4, Integer.parseInt(taskObj.getString("taskSequence")));
								insertPreparedStmt.setDate(5, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
								insertPreparedStmt.setDate(6, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
								insertPreparedStmt.setString(7, taskCompleteIndicator);
								insertPreparedStmt.setInt(8, taskRewardPointCount);
								insertPreparedStmt.setDate(9, sqlCurrentDate);
								insertPreparedStmt.setInt(10,tskwkyCount);
								isInsert = insertPreparedStmt.executeUpdate();
							}
							resultObj = new JSONObject();
							resultObj.put("statusCode", 200);
							resultObj.put("message", "Successfully Inserted the Tasks");
						}
						return resultObj.toString();
					}
				}
			if (isInsert != 0) {
				insertStatus = "success";	
			}
				
		} catch (Exception e) {
			LOG.error("Exception in TasksDBService Impl" + e);
		} finally {
			sqlDBUtil.sqlConnectionClose(null, connection, updatePreparedStmt, LOG);
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(null, insertPreparedStmt, LOG);
			
		}
		return insertStatus;
	}

	private int inserTaskWeekCount(Connection connection, String taskStartDate, String taskEndDate) {
		LOG.info("Insert Method for taskWeeklyChanceCount");
	
		int insertVal = 0;
		PreparedStatement tskWkypreparedStmt = null;
		ResultSet tskwkyResultSet  = null;
		try {
			tskWkypreparedStmt = connection.prepareStatement(SqlConstant.INSERT_TTSKWLY_QUERY);
			tskWkypreparedStmt.setDate(1, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
			tskWkypreparedStmt.setDate(2, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
			tskWkypreparedStmt.setDate(3, sqlCurrentDate);
				
			 
			insertVal = tskWkypreparedStmt.executeUpdate();
			
			
			
		} catch (Exception e) {
			LOG.error("Exception inside taskWeeklyChanceCount", e);
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(null, tskWkypreparedStmt, LOG);
		}
		LOG.info("Task weekly count" + insertVal);
		
		return insertVal;	
	}
	
	public boolean isDateInBetweenIncludingEndPoints(String taskStartDate, String taskEndDate) {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String currentDateFormat = sdf.format(date);
		java.util.Date startDate = null;;
		java.util.Date endDate = null;;
		try {
			startDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
			endDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if ((date.after(startDate) && (date.before(endDate)))
				|| (currentDateFormat.equals(sdf.format(startDate)) || currentDateFormat.equals(sdf.format(endDate)))) {
			return true;
		}
		return false;
	}
	
	public int tasksAvailableforStartAndEndDate(Connection connection,  String taskStartDate , String taskEndDate) {
		LOG.info("Start of Method tasksAvailableforStartAndEndDate");
		
		PreparedStatement preparedStmt = null;
		ResultSet resultSet = null;
		int tasksRecordStatus = 0;
		try {
			LOG.info("Get task start date====>"+ taskStartDate);
			LOG.info("Get task End date====>"+ taskEndDate);
			preparedStmt = connection.prepareStatement(SqlConstant.TASK_FROM_START_AND_END_DATE_QUERY);
			preparedStmt.setDate(1, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
			preparedStmt.setDate(2, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));
			resultSet = preparedStmt.executeQuery();
			while (resultSet.next()) {
				tasksRecordStatus++;
			}
		}catch (Exception e) {
			LOG.error("Exception inside tasksAvailableforStartAndEndDate", e);
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(resultSet, preparedStmt, LOG);
		}
		LOG.info("Tasks record status present for start and end date ::-" + tasksRecordStatus);
		LOG.info("End of Method tasksAvailableforStartAndEndDate");
	return 	tasksRecordStatus;
	}
	
	public int taskCheckInBetweenFromStartAndEndDate(Connection connection) {
		LOG.info("End of Method taskCheckInBetweenFromStartAndEndDate");
		PreparedStatement preparedStmt = null;
		ResultSet dateRangeSqlResultSet = null;
		int tasksDateRangeStatus = 0;
		try {
			preparedStmt = connection.prepareStatement(SqlConstant.TASK_DETAIL_IN_BETWEEN_START_AND_END_DATE_QUERY);
			dateRangeSqlResultSet = preparedStmt.executeQuery();
			while (dateRangeSqlResultSet.next()) {
				tasksDateRangeStatus++;
			}
		} catch (Exception e) {
			LOG.error("Exception inside taskCheckInBetweenFromStartAndEndDate", e.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(dateRangeSqlResultSet, preparedStmt, LOG);
		}
		LOG.info("Tasks record status present in between start and end date ::-" + tasksDateRangeStatus);
		LOG.info("End of Method taskCheckInBetweenFromStartAndEndDate");
		return tasksDateRangeStatus;
		
	}		
	
	public int taskWeeklyChanceCount(Connection connection, String taskStartDate, String taskEndDate) {
		LOG.info("start of Method taskWeeklyChanceCount");
		int tskwkyCount = 0;
		PreparedStatement tskpreparedStmt = null;
		ResultSet tskwkyResultSet  = null;
		try {
			 tskpreparedStmt = connection.prepareStatement(SqlConstant.SELECT_QUERY_FOR_TASK_CHANCE);
			tskpreparedStmt.setDate(1, sqlDBUtil.convertStartDateIntoSqldateformate(taskStartDate));
			tskpreparedStmt.setDate(2, sqlDBUtil.convertEndDateIntoSqldateformate(taskEndDate));

			tskwkyResultSet = tskpreparedStmt.executeQuery();
			int tskwkyReslutSetSize = 0;
			
			while (tskwkyResultSet.next()) {
				tskwkyReslutSetSize++;
				tskwkyCount = tskwkyResultSet.getInt("TSKWKY_CT");
			}

		} catch (Exception e) {
			LOG.error("Exception inside taskWeeklyChanceCount", e);
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(tskwkyResultSet, tskpreparedStmt, LOG);
		}
		LOG.info("Task weekly count" + tskwkyCount);
		LOG.info("End of Method taskWeeklyChanceCount");
		return tskwkyCount;	
		
	}
}