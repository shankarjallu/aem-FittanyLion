
package com.fittanylion.aem.core.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.services.UserSessionLoginService;
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;

@Component(immediate = true, service = UserSessionLoginService.class)
public class UserSessionLoginServiceImpl implements UserSessionLoginService {

	private static final Logger LOG = LoggerFactory.getLogger(UserSessionLoginServiceImpl.class);

	@Override
	public String validateUserKey(DataSource dataSource, SlingHttpServletRequest request) {
		JSONObject resultObj = new JSONObject();
		Connection connection = null;
		ResultSet passwordResultSet = null;
		PreparedStatement preparedStmt = null;
		try {
			if (dataSource != null) {
				String key = request.getParameter("key");
				// Connection connection = dataSource.getConnection();
				connection = dataSource.getConnection();
				preparedStmt = connection.prepareStatement(SqlConstant.SELECT_PASSWORD_QUERY);
				preparedStmt.setString(1, key);
				passwordResultSet = preparedStmt.executeQuery();
				boolean isKeyExists = false;
				int passwordResultSetSize = 0;
				int customerId = 0;
				String firstName = null;
				String lastName = null;
				String customerAgeGroup = null;
				String customerAuthKey = null;
				String custPwd = null;
				String custEmailId = null;

				while (passwordResultSet.next()) {
					isKeyExists = true;
					// System.out.println();
					passwordResultSetSize++;
					customerId = passwordResultSet.getInt("CUST_ID");
					firstName = passwordResultSet.getString("CUST_FST_NM");
					lastName = passwordResultSet.getString("CUST_LA_NM");
					customerAgeGroup = passwordResultSet.getString("CUST_DOB_RNG_DS");
					customerAuthKey = passwordResultSet.getString("CUST_PW_TOK_NO");
					custPwd = passwordResultSet.getString("CUST_PW_ID");
					custEmailId = passwordResultSet.getString("CUST_EMAIL_AD");
				}
				if (isKeyExists) {
					if (passwordResultSetSize > 0) {
						int taskChanceCount = 0;
						taskChanceCount = readingCustChanceCount(connection, customerId);
						String jsonRespObject = readingTasksDetails(connection, customerId, firstName, lastName,
								customerAgeGroup, customerAuthKey, custEmailId, taskChanceCount);
						return jsonRespObject;
					}

				} else {
					resultObj.put("statusCode", 400);
					resultObj.put("message", "This is not a valid key");
				}

			} else {
				resultObj.put("statusCode", 400);
				resultObj.put("message", "Network connection issue");
			}

		} catch (Exception e) {
			LOG.error("Exception inside validateUserKey ", e.getMessage());
		} finally {
			sqlDBUtil.sqlConnectionClose(passwordResultSet, connection, preparedStmt, LOG);
		}
		return resultObj.toString();
	}

	public String readingTasksDetails(Connection connection, int customerId, String firstName, String lastName,
			String customerAgeGroup, String customerAuthKey, String custEmailId, int taskChanceCount) {
		JSONObject custTasksJsonObject = new JSONObject();
		ResultSet dateRangeSqlResultSet = null;
		PreparedStatement ps= null;
		try {
			ps = connection.prepareStatement(SqlConstant.SELECT_QUERY_FOR_TASK);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			custTasksJsonObject.put("StatusCode", 200);
			custTasksJsonObject.put("customerId", customerId);
			custTasksJsonObject.put("customerAgeGroup", customerAgeGroup);
			custTasksJsonObject.put("customerFirstName", firstName);
			custTasksJsonObject.put("customerLastName", lastName);
			custTasksJsonObject.put("customerAuthKey", customerAuthKey);

			custTasksJsonObject.put("customerEmailId", custEmailId);
			custTasksJsonObject.put("taskTotalChancesCount", taskChanceCount);

			dateRangeSqlResultSet = ps.executeQuery();
			int tasksDateRangeStatus = 0;
			JSONArray tasksArray = new JSONArray();
			String taskStartDate = null;
			String taskEndDate = null;
			String TaskCompleteIndicatorUupdate = null;
			Map<Integer, String> custTaskMap = new HashMap<Integer, String>();
			while (dateRangeSqlResultSet.next()) {
				tasksDateRangeStatus++;
				JSONObject tasksJsonObject = new JSONObject();
				taskStartDate = dateFormat.format(dateRangeSqlResultSet.getDate("TSK_STRT_DT"));
				taskEndDate = dateFormat.format(dateRangeSqlResultSet.getDate("TSK_END_DT"));
				tasksJsonObject.put("taskID", dateRangeSqlResultSet.getInt("TSK_ID"));
				tasksJsonObject.put("taskTitle", dateRangeSqlResultSet.getString("TSK_TTL_NM"));
				tasksJsonObject.put("taskDescription", dateRangeSqlResultSet.getString("TSK_DS"));
				tasksJsonObject.put("taskUserManual", dateRangeSqlResultSet.getString("TSK_MAN_DS"));
				// Customer task status from database.
				custTaskMap = readingCustTasks(connection, customerId, taskStartDate, taskEndDate);
				if (custTaskMap != null && custTaskMap.size() > 0) {
					TaskCompleteIndicatorUupdate = custTaskMap.get(dateRangeSqlResultSet.getInt("TSK_ID"));
				}
				tasksJsonObject.put("TaskCompleteIndicator",
						TaskCompleteIndicatorUupdate != null ? TaskCompleteIndicatorUupdate : "N");
				tasksJsonObject.put("taskSequence", dateRangeSqlResultSet.getInt("TSK_SEQ_NO"));
				tasksArray.put(tasksJsonObject);

			}
			custTasksJsonObject.put("taskStartDate", taskStartDate);
			custTasksJsonObject.put("taskEndDate", taskEndDate);

			// Reading tasks weekly table details
			readingTasksWeeklyDetails(connection, custTasksJsonObject);

			custTasksJsonObject.put("tasks", tasksArray);

		} catch (SQLException e) {
			LOG.info("Expection inside readingTasksDetails", e.getMessage());
		} catch (JSONException jsone) {
			// TODO Auto-generated catch block
			LOG.info("Expection inside readingTasksDetails", jsone.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(dateRangeSqlResultSet, ps, LOG);
		}

		return custTasksJsonObject.toString();
	}

	public void readingTasksWeeklyDetails(Connection connection, JSONObject custTasksJsonObject) {
		LOG.info("Start of  method readingTasksWeeklyDetails");
		PreparedStatement pstaskweekly = null;
		ResultSet dateRangeSqlResultSet = null;
		try {
			pstaskweekly = connection.prepareStatement(SqlConstant.SELECT_QUERY_DATE_RANGE_FROM_TASK_WEEKLY_TABLE);
			dateRangeSqlResultSet = pstaskweekly.executeQuery();
			while (dateRangeSqlResultSet.next()) {
				custTasksJsonObject.put("taskWeekCount", dateRangeSqlResultSet.getInt("TSKWKY_CT"));
			}
		} catch (Exception e) {
			LOG.info("Expection inside readingTasksWeeklyDetails", e.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(dateRangeSqlResultSet, pstaskweekly, LOG);
		}
		LOG.info("END of  method readingTasksWeeklyDetails");
	}

	public int readingCustChanceCount(Connection connection, int customerId) {
		LOG.info("start of Method readingCustChanceCount");
		// TODO Auto-generated method stub
		PreparedStatement custChncPreparedStmt = null;
		ResultSet tskwkyResultSet = null;
		int custChanceReslutSetSize = 0;
		try {
			custChncPreparedStmt = connection.prepareStatement(SqlConstant.SELECT_GET_CUST_CHANGE_COUNT);
			custChncPreparedStmt.setInt(1, customerId);
			tskwkyResultSet = custChncPreparedStmt.executeQuery();
			while (tskwkyResultSet.next()) {
				custChanceReslutSetSize++;

			}
			LOG.info("THE TOTAL CUSTOMER CHANCE===>" + custChanceReslutSetSize);
			LOG.info("End of Method readingCustChanceCount");
			return custChanceReslutSetSize;

		} catch (Exception e) {
			LOG.error("Excpetion inside method readingCustChanceCount" , e.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(tskwkyResultSet, custChncPreparedStmt, LOG);
		}

		return custChanceReslutSetSize;
	}

	public Map<Integer, String> readingCustTasks(Connection connection, int customerId, String custtaskStartDate,
			String custtaskEndDate) {
		LOG.info("Start of method readingCustTasks");
		// TODO Auto-generated method stub
		JSONObject custTasksJson = new JSONObject();
		Map<Integer, String> custTaskMap = new HashMap<Integer, String>();
		PreparedStatement tskpreparedStmt = null;
		ResultSet custtaskStaResultSet = null;
		try {
			if (custtaskStartDate != null && custtaskEndDate != null) {
				custtaskStartDate = custtaskStartDate.replace('-', '/');// replaces all occurrences of - to /
				custtaskEndDate = custtaskEndDate.replace('-', '/');// replaces all occurrences of - to /
			}
			// Prepare query to task status from CUSTTSK
			tskpreparedStmt = connection.prepareStatement(SqlConstant.SELECT_CUST_TASK_STATUS_DETAILS_QUERY);
			tskpreparedStmt.setInt(1, customerId);
			tskpreparedStmt.setDate(2, sqlDBUtil.convertStartDateIntoSqldateformate(custtaskStartDate));
			tskpreparedStmt.setDate(3, sqlDBUtil.convertEndDateIntoSqldateformate(custtaskEndDate));
			custtaskStaResultSet = tskpreparedStmt.executeQuery();
			while (custtaskStaResultSet.next()) {
				custTaskMap.put(custtaskStaResultSet.getInt("TSK_ID"),
						custtaskStaResultSet.getString("CUSTTSK_CMPL_IN"));
			}
			LOG.info("Customer MAP with Task size :- " + custTaskMap.size());
		} catch (Exception e) {
			LOG.error("Expection inside method readingCustTasks" , e.getMessage());
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(custtaskStaResultSet, tskpreparedStmt, LOG);
		}
		LOG.info("END of method readingCustTasks");
		return custTaskMap;
	}

}