
package com.fittanylion.aem.core.services.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;

import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.services.UserSessionLoginService;
import com.fittanylion.aem.core.utils.CommonUtilities;

@Component(immediate = true, service = UserSessionLoginService.class)
public class UserSessionLoginServiceImpl implements UserSessionLoginService {

	private static final Logger LOG = LoggerFactory.getLogger(UserSessionLoginServiceImpl.class);

	@Override
	public String validateUserKey(DataSource dataSource, SlingHttpServletRequest request) {
		JSONObject resultObj = new JSONObject();
		try {
			if (dataSource != null) {
				String key = request.getParameter("key");
				// Connection connection = dataSource.getConnection();
				final Connection connection = dataSource.getConnection();
				final Statement statement = connection.createStatement();

				String passwordQuery = "select * from CUST where CUST_PW_TOK_NO = ?";
				PreparedStatement preparedStmt = connection.prepareStatement(passwordQuery);
				preparedStmt.setString(1, key);
				ResultSet passwordResultSet = preparedStmt.executeQuery();
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

						taskChanceCount = readingCustChanceCount(statement, connection, customerId);
						String jsonRespObject = readingTasksDetails(connection, customerId, firstName, lastName,
								customerAgeGroup, customerAuthKey, custEmailId, taskChanceCount);

						// Need to call other table to retreive data if successfull login

						return jsonRespObject;
					}

				} else {
					resultObj.put("statusCode", 400);
					resultObj.put("message", "This is not a valid key");
				}

			} else {
				resultObj.put("statusCode", 400);
				resultObj.put("message", "Database connection issue");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj.toString();
	}

	public String readingTasksDetails(Connection connection, int customerId, String firstName, String lastName,
			String customerAgeGroup, String customerAuthKey, String custEmailId, int taskChanceCount) {

		String dateRangeSql = "select * from TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT  ORDER BY TSK_SEQ_NO";
		JSONObject custTasksJsonObject = new JSONObject();

		try {
			final Statement statement = connection.createStatement();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			custTasksJsonObject.put("StatusCode", 200);
			custTasksJsonObject.put("customerId", customerId);
			custTasksJsonObject.put("customerAgeGroup", customerAgeGroup);
			custTasksJsonObject.put("customerFirstName", firstName);
			custTasksJsonObject.put("customerLastName", lastName);
			custTasksJsonObject.put("customerAuthKey", customerAuthKey);

			custTasksJsonObject.put("customerEmailId", custEmailId);
			custTasksJsonObject.put("taskTotalChancesCount", taskChanceCount);

			ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeSql);
			int tasksDateRangeStatus = 0;
			JSONArray tasksArray = new JSONArray();
			String taskStartDate = null;
			String taskEndDate = null;
			String TaskCompleteIndicatorUupdate = null;
			Map<Integer, String> custTaskMap = new HashMap<Integer, String>();
			while (dateRangeSqlResultSet.next()) {
				System.out.print("Inside date range sql result Set =====>");

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

				TaskCompleteIndicatorUupdate = custTaskMap.get(dateRangeSqlResultSet.getInt("TSK_ID"));
				System.out.print("Customer Status Task " + TaskCompleteIndicatorUupdate + "For Task ID"
						+ dateRangeSqlResultSet.getInt("TSK_ID"));
				tasksJsonObject.put("TaskCompleteIndicator", TaskCompleteIndicatorUupdate);
				// tasksJsonObject.put("TaskCompleteIndicatorUupdate",
				// TaskCompleteIndicatorUupdate);

				tasksJsonObject.put("taskSequence", dateRangeSqlResultSet.getInt("TSK_SEQ_NO"));

				tasksArray.put(tasksJsonObject);

			}

			custTasksJsonObject.put("taskStartDate", taskStartDate);
			custTasksJsonObject.put("taskEndDate", taskEndDate);

			// Reading tasks weekly table details
			readingTasksWeeklyDetails(statement, custTasksJsonObject);

			custTasksJsonObject.put("tasks", tasksArray);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return custTasksJsonObject.toString();
	}

	public void readingTasksWeeklyDetails(Statement statement, JSONObject custTasksJsonObject) {
		String dateRangeFromTaskWeeklyTable = "select * from TSKWKY WHERE trunc(sysdate) BETWEEN TSKWKY_STRT_DT AND TSKWKY_END_DT";

		try {
			ResultSet dateRangeSqlResultSet = statement.executeQuery(dateRangeFromTaskWeeklyTable);
			while (dateRangeSqlResultSet.next()) {
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

	public int readingCustChanceCount(Statement statement, Connection connection, int customerId) {
		// TODO Auto-generated method stub
		JSONObject custChanceCount = new JSONObject();
		int custChanceReslutSetSize = 0;
		try {
			String getCustChanceCount = "select * from CUSTTSKSTA WHERE CUST_ID = ?";

			// String getTskWkyQuery = "select * from TSKWKY where TSKWKY_STRT_DT >= ? and
			// TSKWKY_END_DT <= ?";

			PreparedStatement custChncPreparedStmt = connection.prepareStatement(getCustChanceCount);
			custChncPreparedStmt.setInt(1, customerId);

			ResultSet tskwkyResultSet = custChncPreparedStmt.executeQuery();

			int tskwkyReslutSetSize = 0;

			while (tskwkyResultSet.next()) {
				custChanceReslutSetSize++;

			}
			System.out.println("THE TOTAL CUSTOMER CHANCE===>" + custChanceReslutSetSize);
			return custChanceReslutSetSize;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return custChanceReslutSetSize;
	}

	public Map<Integer, String> readingCustTasks(Connection connection, int customerId, String custtaskStartDate,
			String custtaskEndDate) {
		// TODO Auto-generated method stub
		JSONObject custTasksJson = new JSONObject();
		Map<Integer, String> custTaskMap = new HashMap<Integer, String>();

		try {
			if (custtaskStartDate != null && custtaskEndDate != null) {
				custtaskStartDate = custtaskStartDate.replace('-', '/');// replaces all occurrences of - to /
				custtaskEndDate = custtaskEndDate.replace('-', '/');// replaces all occurrences of - to /
			}
			// Prepare query to task status from CUSTTSK
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
			while (custtaskStaResultSet.next()) {

				System.out.println("HURRAY custtaskStaResultSet.getIntTASK_ID" + custtaskStaResultSet.getInt("TSK_ID"));
				System.out.println(
						"HURRAY custtaskStaResultSet.ger Complete" + custtaskStaResultSet.getString("CUSTTSK_CMPL_IN"));

				/*
				 * custtasksJsonObject.put("taskId", custtaskStaResultSet.getInt("TSK_ID"));
				 * custtasksJsonObject.put("custTaskCompleteIndicator",
				 * custtaskStaResultSet.getString("CUSTTSK_CMPL_IN"));
				 * custtasksArray.put(custtasksJsonObject);
				 */
				custTaskMap.put(custtaskStaResultSet.getInt("TSK_ID"),
						custtaskStaResultSet.getString("CUSTTSK_CMPL_IN"));

			}
			// custTasksJson.put("custTask", custtasksArray);
			System.out.println("custTasksJson.toString()========>>" + custTaskMap.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return custTaskMap;
	}

}