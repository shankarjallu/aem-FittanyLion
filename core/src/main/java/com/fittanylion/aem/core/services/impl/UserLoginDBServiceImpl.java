package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.bean.TaskItem;
import com.fittanylion.aem.core.services.UserLoginDBService;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;

@Component(immediate = true, service = UserLoginDBService.class)

public class UserLoginDBServiceImpl implements UserLoginDBService {

	private static final Logger LOG = LoggerFactory.getLogger(UserLoginDBServiceImpl.class);
	
	@Override
	public String verifyUserLogin(DataSource dataSource, SlingHttpServletRequest request) {
		LOG.info("Start of Method verifyUserLogin");
		//JSONObject jsonObjectConnection = new JSONObject();
		JSONObject custTasksJsonObject = new JSONObject();
		Connection connection  = null;
		ResultSet userResultSet = null;
		PreparedStatement ps = null;
		ResultSet userpassResultSet = null;
		PreparedStatement userpassPS = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			JSONObject jsnobject = new JSONObject(sb.toString());

			//JSONObject jsonObject = new JSONObject();
			String custusername = jsnobject.getString("username");
			String custpassword = jsnobject.getString("password");

			Decoder decoder = Base64.getDecoder();
			String username = new String(decoder.decode(custusername));
			String password = new String(decoder.decode(custpassword));
			LOG.info("Checking Data Source in UserLoginDB serive Impl===>" + dataSource);
			if (dataSource != null) {
				if (dataSource != null && username != null && password != null) {
					connection = dataSource.getConnection();
					//final Statement statement = connection.createStatement();
					ps = connection.prepareStatement(SqlConstant.USER_NAME_QUERY);
					ps.setString(1, username);
					userResultSet = ps.executeQuery();
					
					int usernameResultSize = 0;
					while (userResultSet.next()) {
						usernameResultSize++;
					}
					LOG.info("User is valid if size is greater than zero : " + usernameResultSize);
					if (usernameResultSize > 0) {
						userpassPS = connection.prepareStatement(SqlConstant.USER_PASSWORD_QUERY);
						userpassPS.setString(1, username);
						userpassPS.setString(2, password);
						userpassResultSet = userpassPS.executeQuery();
						int passwordResultSetSize = 0;
						int customerId = 0;
						String firstName = null;
						String lastName = null;
						String customerAgeGroup = null;
						String customerAuthKey = null;
						String custPwd = null;
						String custEmailId = null;
						if (userpassResultSet !=null) {
							while (userpassResultSet.next()) {
								passwordResultSetSize++;
								customerId = userpassResultSet.getInt(SqlConstant.CUST_ID);
								firstName = userpassResultSet.getString(SqlConstant.CUST_FST_NM);
								lastName = userpassResultSet.getString(SqlConstant.CUST_LA_NM);
								customerAgeGroup = userpassResultSet.getString(SqlConstant.CUST_DOB_RNG_DS);
								customerAuthKey = userpassResultSet.getString(SqlConstant.CUST_PW_TOK_NO);
								custPwd = userpassResultSet.getString(SqlConstant.CUST_PW_ID);
								custEmailId = userpassResultSet.getString(SqlConstant.CUST_EMAIL_AD);
							}
						}

						if (passwordResultSetSize > 0) {
							LOG.info("User is validate by passing User name and password in DB , USerName is :-" + firstName + "CustomerID :-" + customerId);
							int taskChanceCount = 0;
							//Get Task chance for each customer
							taskChanceCount = readingCustChanceCount(connection, customerId);
							
							
							custTasksJsonObject.put("StatusCode", 200);
							custTasksJsonObject.put("customerId", customerId);
							custTasksJsonObject.put("customerAgeGroup", customerAgeGroup);
							custTasksJsonObject.put("customerFirstName", firstName);
							custTasksJsonObject.put("customerLastName", lastName);
							custTasksJsonObject.put("customerAuthKey", customerAuthKey);
							custTasksJsonObject.put("customerEmailId", custEmailId);
							custTasksJsonObject.put("taskTotalChancesCount", taskChanceCount);
							
							String jsonRespObject = readingTasksDetails(connection,customerId, custTasksJsonObject);
							// Need to call other table to retreive data if successfull login

							return jsonRespObject;
						} else {
							custTasksJsonObject.put("statusCode", 400);
							custTasksJsonObject.put("message", "Incorrect Password");
							return custTasksJsonObject.toString();
						}
					} else {
						custTasksJsonObject.put("statusCode", 400);
						custTasksJsonObject.put("message", "Incorrect Email Id");
						return custTasksJsonObject.toString();
					}
				}
			} else {
				custTasksJsonObject.put("statusCode", 400);
				custTasksJsonObject.put("message", "Network connection issue.Please Try Later...");
				return custTasksJsonObject.toString();
			}

		} catch (Exception e) {
			LOG.error("Exception inside method verifyUserLogin:==> " , e);
		//	e.printStackTrace();
		} finally {
			sqlDBUtil.sqlConnectionClose(userResultSet, connection, ps, LOG);
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(userpassResultSet, userpassPS, LOG);
		}
		return custTasksJsonObject.toString();
	}
	
		
	public int readingCustChanceCount(Connection connection, int customerId) {
		// TODO Auto-generated method stub
		int custChanceReslutSetSize = 0;
		PreparedStatement custChncPreparedStmt= null;
		ResultSet tskwkyResultSet = null;
		try {
			custChncPreparedStmt = connection.prepareStatement(SqlConstant.USER_CHANCE_TASK_QUERY);
			custChncPreparedStmt.setInt(1, customerId);
			tskwkyResultSet = custChncPreparedStmt.executeQuery();
			while (tskwkyResultSet.next()) {
				custChanceReslutSetSize++;

			}
			System.out.println("THE TOTAL CUSTOMER CHANCE===>" + custChanceReslutSetSize);
			LOG.info("THE TOTAL CUSTOMER CHANCE===>" + custChanceReslutSetSize);

		} catch (Exception e) {
			LOG.error("Exception inside method readingCustChanceCount: " , e.getMessage());
			e.printStackTrace();
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(tskwkyResultSet, custChncPreparedStmt, LOG);
		}

		return custChanceReslutSetSize;
	}

	public String readingTasksDetails(Connection connection, int customerId, JSONObject custTasksJsonObject) {

		//String dateRangeSql = "select * from FTA.TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT  ORDER BY TSK_SEQ_NO";
		//JSONObject custTasksJsonObject = new JSONObject();
		PreparedStatement ps = null;
		ResultSet dateRangeSqlResultSet = null;
		try {
			//final Statement statement = connection.createStatement();
			ps = connection.prepareStatement(SqlConstant.ALL_TASK_IN_BETWEEN_START_AND_END_DATE_QUERY);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			dateRangeSqlResultSet = ps.executeQuery();
			int tasksDateRangeStatus = 0;
			JSONArray tasksArray = new JSONArray();
			String taskStartDate = null;
			String taskEndDate = null;
			String TaskCompleteIndicatorUupdate = "N";
			Map<Integer, String> custTaskMap = new HashMap<Integer, String>();
			List<TaskItem> taskItemList = CommonUtilities.getTaskTitleAndImage();
			while (dateRangeSqlResultSet.next()) {
				System.out.print("Inside date range sql result Set =====>");
				tasksDateRangeStatus++;
				JSONObject tasksJsonObject = new JSONObject();
				taskStartDate = dateFormat.format(dateRangeSqlResultSet.getDate(SqlConstant.TSK_STRT_DT));
				taskEndDate = dateFormat.format(dateRangeSqlResultSet.getDate(SqlConstant.TSK_END_DT));
				tasksJsonObject.put("taskID", dateRangeSqlResultSet.getInt(SqlConstant.TSK_ID));
				tasksJsonObject.put("taskTitle", dateRangeSqlResultSet.getString(SqlConstant.TSK_TTL_NM));
				tasksJsonObject.put("taskDescription", dateRangeSqlResultSet.getString(SqlConstant.TSK_DS));
				tasksJsonObject.put("taskUserManual", dateRangeSqlResultSet.getString(SqlConstant.TSK_MAN_DS));
				// Customer task status from database.
				custTaskMap = readingCustTasks(connection, customerId, taskStartDate, taskEndDate);
				
				if (custTaskMap != null && custTaskMap.size() > 0) {  
				    TaskCompleteIndicatorUupdate = custTaskMap.get(dateRangeSqlResultSet.getInt(SqlConstant.TSK_ID));  
				}

				System.out.println("custTaskMap.Size........>" + custTaskMap.size());
				System.out.print("Customer Status Task " + TaskCompleteIndicatorUupdate + "For Task ID"
						+ dateRangeSqlResultSet.getInt("TSK_ID"));
				
				tasksJsonObject.put("TaskCompleteIndicator", TaskCompleteIndicatorUupdate != null ? TaskCompleteIndicatorUupdate :"N");
				
				System.out.println("TaskCompleteIndicatorUupdate......>" + TaskCompleteIndicatorUupdate);
				
				tasksJsonObject.put("taskSequence", dateRangeSqlResultSet.getInt(SqlConstant.TSK_SEQ_NO));
				
				TaskItem taskItem = CommonUtilities.getRandomTitleAndImage(taskItemList);
				tasksJsonObject.put("taskRandomTitle", taskItem.getTitle());
				tasksJsonObject.put("taskImagePath", taskItem.getImagePath());
				tasksArray.put(tasksJsonObject);

			}
			
			custTasksJsonObject.put("taskStartDate", taskStartDate);
			custTasksJsonObject.put("taskEndDate", taskEndDate);
			
			
			//This method is for reading if the Customer Have completed all the tasks for the week or not.
			int custWeekStatusResult = readingCustStatusForCurrentWeek( connection, customerId, taskStartDate, taskEndDate);
			custTasksJsonObject.put("congratsCard", custWeekStatusResult > 0 ? true : false);
			
			// Reading tasks weekly table details
			custTasksJsonObject.put("taskWeekCount", readingTasksWeeklyDetails(connection));
			custTasksJsonObject.put("tasks", tasksArray);
			
		} catch (SQLException sqlex) {
			LOG.error("Expection inside readingTasksDetails SQLException ::" , sqlex);
		} catch (JSONException jsone) {
			LOG.error("Expection inside readingTasksDetails JSONExpection ::" , jsone);
		} catch (Exception e) {
			LOG.error("Expection inside readingTasksDetails Exception ::" , e);
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(dateRangeSqlResultSet, ps, LOG);
		}

		return custTasksJsonObject.toString();
	}

	public int readingTasksWeeklyDetails( Connection connection) {
		LOG.info("start of method readingTasksWeeklyDetails");
		//String dateRangeFromTaskWeeklyTable = "select * from FTA.TSKWKY WHERE trunc(sysdate) BETWEEN TSKWKY_STRT_DT AND TSKWKY_END_DT";
		PreparedStatement ps = null;
		ResultSet dateRangeSqlResultSet = null;
		int taskWeekCount = 0;
		try {
			ps = connection.prepareStatement(SqlConstant.DATE_RANGE_FROM_TASK_WEEKLY_TABLE_QUERY);
			dateRangeSqlResultSet = ps.executeQuery();
			while (dateRangeSqlResultSet.next()) {
				taskWeekCount = dateRangeSqlResultSet.getInt(SqlConstant.TSKWKY_CT);
				//custTasksJsonObject.put("taskWeekCount", dateRangeSqlResultSet.getInt("TSKWKY_CT"));
			}
		} catch (SQLException e) {
			LOG.error("Expection inside readingTasksWeeklyDetails ::" , e);
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(dateRangeSqlResultSet, ps, LOG);
		}
		LOG.info("taskWeekCount:- " + taskWeekCount);
		LOG.info("End of method readingTasksWeeklyDetails");
		return taskWeekCount;

	}

	public int readingCustStatusForCurrentWeek( Connection connection, int customerId, String custtaskStartDate,String custtaskEndDate) {
		// TODO Auto-generated method stub
		LOG.info("start of Method readingCustStatusForCurrentWeek");
		int custWeekStatusResultSize = 0;
		ResultSet tskwkyResultSet = null;
		PreparedStatement tskWkyPreparedStmt= null;
		 try {
			 if (custtaskStartDate != null && custtaskEndDate != null) {
					custtaskStartDate = custtaskStartDate.replace('-', '/');// replaces all occurrences of - to /
					custtaskEndDate = custtaskEndDate.replace('-', '/');// replaces all occurrences of - to /
				}
			 // First get TSKWKY_CT field from TSKWKY table for the given Start date and End Date.
     		//String getTskWkyQuery = "select * from FTA.TSKWKY where TSKWKY_STRT_DT >= ? and TSKWKY_END_DT <= ?";
			tskWkyPreparedStmt = connection.prepareStatement(SqlConstant.TASK_WEEKLY_QUERY);
			
			tskWkyPreparedStmt.setDate(1, sqlDBUtil.convertStartDateIntoSqldateformate(custtaskStartDate));
			tskWkyPreparedStmt.setDate(2, sqlDBUtil.convertEndDateIntoSqldateformate(custtaskEndDate));

			tskwkyResultSet = tskWkyPreparedStmt.executeQuery();
			
			int tskwkyReslutSetSize = 0;
			int tskWkyCount = 0;
			while (tskwkyResultSet.next()) {
				tskwkyReslutSetSize++;
				tskWkyCount = tskwkyResultSet.getInt(SqlConstant.TSKWKY_CT);
				System.out.println("Get Task wky count====>" + tskWkyCount);
												
			}
			System.out.println("tskwkyReslutSetSize 1111111====>" + tskwkyReslutSetSize);
			if(tskwkyReslutSetSize > 0) {
				//String getCustStatus = "Select * from FTA.CUSTTSKSTA Where TSKWKY_CT= ? AND CUST_ID= ?" ;
				
				PreparedStatement custStatusIndicator = connection.prepareStatement(SqlConstant.CUST_STATUS_QUERY);
				custStatusIndicator.setInt(1,tskWkyCount );
				custStatusIndicator.setInt(2,customerId );
				ResultSet custResultSet = custStatusIndicator.executeQuery();
				
				while(custResultSet.next()){
					custWeekStatusResultSize++;
				}
			}
			
			LOG.info("custWeekStatusResultSize::- " + custWeekStatusResultSize);
			LOG.info("End of readingCustStatusForCurrentWeek method");
		
	}catch(Exception e) {
		LOG.error("Exception Inside method readingCustStatusForCurrentWeek" , e);
	} finally {
		sqlDBUtil.sqlResultSetAndPreparedStatementClose(tskwkyResultSet, tskWkyPreparedStmt, LOG);
	}
		 return custWeekStatusResultSize;	
		
	}
	
	public Map<Integer, String> readingCustTasks(Connection connection, int customerId, String custtaskStartDate,
			String custtaskEndDate) {
		LOG.info("start of method readingCustTasks");
		
		Map<Integer, String> custTaskMap = new HashMap<Integer, String>();
		PreparedStatement tskpreparedStmt = null;
		ResultSet custtaskStaResultSet = null;
		
		try {
			if (custtaskStartDate != null && custtaskEndDate != null) {
				custtaskStartDate = custtaskStartDate.replace('-', '/');// replaces all occurrences of - to /
				custtaskEndDate = custtaskEndDate.replace('-', '/');// replaces all occurrences of - to /
			}
			
			tskpreparedStmt = connection.prepareStatement(SqlConstant.TASK_STATUS_FOR_USER_QUERY);
			tskpreparedStmt.setInt(1, customerId);

			
			tskpreparedStmt.setDate(2, sqlDBUtil.convertStartDateIntoSqldateformate(custtaskStartDate));
			tskpreparedStmt.setDate(3, sqlDBUtil.convertEndDateIntoSqldateformate(custtaskEndDate));

			System.out.println("tskpreparedStmt.toString()=====>" + tskpreparedStmt.toString());
			custtaskStaResultSet = tskpreparedStmt.executeQuery();
			while (custtaskStaResultSet.next()) {

				System.out.println("HURRAY custtaskStaResultSet.getIntTASK_ID" + custtaskStaResultSet.getInt(SqlConstant.TSK_ID));
				System.out.println("HURRAY custtaskStaResultSet.ger Complete" + custtaskStaResultSet.getString(SqlConstant.CUSTTSK_CMPL_IN));
				custTaskMap.put(custtaskStaResultSet.getInt(SqlConstant.TSK_ID),custtaskStaResultSet.getString(SqlConstant.CUSTTSK_CMPL_IN));

			}
			// custTasksJson.put("custTask", custtasksArray);
			//System.out.println("custTasksJson.toString()========>>" + custTaskMap.size());
			LOG.info("custTasksJson.toString()========>>" + custTaskMap.size());
			LOG.info("End of method readingCustTasks");
		} catch (Exception e) {
			LOG.error("Exception inside method readingCustTasks: " , e);
		} finally {
			sqlDBUtil.sqlResultSetAndPreparedStatementClose(custtaskStaResultSet, tskpreparedStmt, LOG);
		}
		return custTaskMap;
	}
	

}
