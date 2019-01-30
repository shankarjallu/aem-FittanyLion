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
		
		try {

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
			int isInsert = 0;
			if (dataSource != null) {
				final Connection connection = dataSource.getConnection();
				final Statement statement = connection.createStatement();
				String query = " insert into FTA.TSK (TSK_ID,TSK_TTL_NM, TSK_DS, TSK_MAN_DS, TSK_SEQ_NO, TSK_STRT_DT, TSK_END_DT,TSK_CMPL_IN, TSK_RWD_PNT_CT, TSK_RCD_MNTD_TS)"
						+ " values (FTA.TSK$TSK_ID.nextval,?, ?, ?, ?, ?,?, ?, ?, ?)";

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
		JSONObject resultObj = null;

		
		try {
			
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		
			JSONObject jsnobject = new JSONObject(sb.toString());
			
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
				String sql = "select * from FTA.TSK where TSK_STRT_DT >= ? and TSK_END_DT <= ?";

				PreparedStatement preparedStmt = connection.prepareStatement(sql);
				preparedStmt.setDate(1, sqlStartDate);
				preparedStmt.setDate(2, sqlEndDate);

				
				ResultSet resultSet = preparedStmt.executeQuery();
				
				int tasksRecordStatus = 0;

				while (resultSet.next()) {
					
					tasksRecordStatus++;
					
				}

				if (tasksRecordStatus > 0) {

					
					String dateRangeSql = "select * from FTA.TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT";
					
					ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeSql);
					int tasksDateRangeStatus = 0;
					while (dateRangeSqlResultSet.next()) {
						tasksDateRangeStatus++;
					}
					if (tasksDateRangeStatus > 0) {
						resultObj = new JSONObject();
						resultObj.put("statusCode", 400);
						resultObj.put("message", "You cannot update the tasks for current week");
						System.out.println("Error cant upsate" + resultObj);
						return resultObj.toString();

						
					} else {
						System.out.println("666666666666=========>");
						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject taskObj = jsonArray.getJSONObject(i);
							String updateQuery = " update TSK SET TSK_TTL_NM = ? , TSK_DS = ?, TSK_MAN_DS = ?, TSK_SEQ_NO = ?, TSK_STRT_DT = ?, TSK_END_DT = ?,TSK_RCD_MNTD_TS = ? where TSK_STRT_DT = ? AND TSK_END_DT = ? AND TSK_SEQ_NO='"
									+ Integer.parseInt(taskObj.getString("taskSequence")) + "'";
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
							updatePreparedStmt.setDate(8, sqlUpdateStartDate);
							updatePreparedStmt.setDate(9, sqlUpdateEndDate);
							isInsert = updatePreparedStmt.executeUpdate();
							System.out.println("This is update...." + isInsert);
						}
						resultObj = new JSONObject();
						resultObj.put("statusCode", 200);
						resultObj.put("message", "update the tasks.");
						System.out.println("Upsate result" + resultObj);
						return resultObj.toString();

					}

				} else {
					java.util.Date date = new java.util.Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					String currentDateFormat = sdf.format(date);
					boolean isDateInBetweenFlag = isDateInBetweenIncludingEndPoints(startDate, endDate, date,
							currentDateFormat, sdf);
					
					if (isDateInBetweenFlag) {
						resultObj = new JSONObject();
						resultObj.put("statusCode", 400);
						resultObj.put("message", "Say an Error message u cannot update the tasks.");
						
					} else {

						String inserttskwklyQuery = "insert into FTA.TSKWKY (TSKWKY_CT,TSKWKY_STRT_DT, TSKWKY_END_DT, TSKWKY_RCD_MNTD_TS)"
								+ " values (FTA.TSKWKY$TSKWKY_CT.nextval,?,?,?)";
						PreparedStatement insertIntoTskwkly = connection.prepareStatement(inserttskwklyQuery);

						java.util.Date insertStartDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskStartDate);
						java.sql.Date sqlInsertStartDateTskwkly = new java.sql.Date(insertStartDateTskwkly.getTime());
						java.util.Date insertEndDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(taskEndDate);
						java.sql.Date sqlInsertEndDateTskwkly = new java.sql.Date(insertEndDateTskwkly.getTime());

						insertIntoTskwkly.setDate(1, sqlInsertStartDateTskwkly);
						insertIntoTskwkly.setDate(2, sqlInsertEndDateTskwkly);
						insertIntoTskwkly.setDate(3, sqlCurrentDate);
						isInsert = insertIntoTskwkly.executeUpdate();
						int tskwkyCount = 0;
						try {

							String getTskwkyQuery = "select * from FTA.TSKWKY where TSKWKY_STRT_DT >= ? and TSKWKY_END_DT <= ?";

							PreparedStatement tskpreparedStmt = connection.prepareStatement(getTskwkyQuery);
							tskpreparedStmt.setDate(1, sqlStartDate);
							tskpreparedStmt.setDate(2, sqlEndDate);

							ResultSet tskwkyResultSet = tskpreparedStmt.executeQuery();
							int tskwkyReslutSetSize = 0;
							
							while (tskwkyResultSet.next()) {
								tskwkyReslutSetSize++;
								tskwkyCount = tskwkyResultSet.getInt("TSKWKY_CT");
								
							}

						

						} catch (Exception e) {
							e.printStackTrace();

						}

						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject taskObj = jsonArray.getJSONObject(i);
							

							String query = " insert into FTA.TSK (TSK_ID,TSK_TTL_NM, TSK_DS, TSK_MAN_DS, TSK_SEQ_NO, TSK_STRT_DT, TSK_END_DT,TSK_CMPL_IN, TSK_RWD_PNT_CT, TSK_RCD_MNTD_TS,TSKWKY_CT)"
									+ " values (FTA.TSK$TSK_ID.nextval,?, ?, ?, ?, ?,?, ?, ?, ?, ?)";

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

	public boolean isDateInBetweenIncludingEndPoints(Date startDate, final Date endDate, Date date, String currentDate,
			SimpleDateFormat sdf) {
		if ((date.after(startDate) && (date.before(endDate)))
				|| (currentDate.equals(sdf.format(startDate)) || currentDate.equals(sdf.format(endDate)))) {
			return true;
		}
		return false;
	}
}