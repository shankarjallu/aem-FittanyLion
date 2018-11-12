package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.Base64.Decoder;

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
			
			StringBuilder sb = new StringBuilder();
			  BufferedReader br = request.getReader();
			  String str = null;
			  while ((str = br.readLine()) != null) {
			      sb.append(str);
			  }
			  JSONObject jsnobject = new JSONObject(sb.toString());
			
			
			JSONObject jsonObject = new JSONObject();
			String custusername = jsnobject.getString("username");
			String custpassword = jsnobject.getString("password");
			
			Decoder decoder = Base64.getDecoder();
			String username = new String(decoder.decode(custusername));
			String password = new String(decoder.decode(custpassword));
			
			
			
			System.out.println("Hey this is Decode username <====>" + username);
			System.out.println("Hey this is Decode password <====>" + password);
			
			
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
