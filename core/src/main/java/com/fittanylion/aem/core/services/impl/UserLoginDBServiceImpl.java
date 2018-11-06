package com.fittanylion.aem.core.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import com.fittanylion.aem.core.services.UserLoginDBService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = UserLoginDBService.class)


public class UserLoginDBServiceImpl implements UserLoginDBService {


	@Override
	public String verifyUserLogin(DataSource dataSource, SlingHttpServletRequest request) {
		
		try {
			JSONObject jsonObject = new JSONObject();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
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
						while(passwordResultSet.next()){

							passwordResultSetSize++;

						}
						if(passwordResultSetSize > 0) {
							jsonObject.put("statusCode",200);
							jsonObject.put("message","Login Success.");
							//Need to call other table to retreive data if successfull login
							return jsonObject.toString();
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
	
	
}
