
package com.fittanylion.aem.core.services.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;

import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
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
		//	Connection connection = dataSource.getConnection();
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
		    //	System.out.println();
		    	 passwordResultSetSize++;
                 customerId = passwordResultSet.getInt("CUST_ID");
                 firstName = passwordResultSet.getString("CUST_FST_NM");
                 lastName = passwordResultSet.getString("CUST_LA_NM");
                 customerAgeGroup = passwordResultSet.getString("CUST_DOB_RNG_DS");
                 customerAuthKey = passwordResultSet.getString("CUST_PW_TOK_NO");
                 custPwd = passwordResultSet.getString("CUST_PW_ID");
                 custEmailId = passwordResultSet.getString("CUST_EMAIL_AD");
		    }   
		   if(isKeyExists) {
			  
			   			   
			   System.out.println("HELLO test 123====>" + customerId );
			   System.out.println("HELLO test 22222====>" + firstName );
			   
			   resultObj.put("statusCode",200);
      	     resultObj.put("message","This key is present");
			
			                 }else {
			                	 resultObj.put("statusCode",400);
			            	     resultObj.put("message","This is not a valid key");
			                 }
               
			   
			   
		   }else {
			   resultObj.put("statusCode",400);
			     resultObj.put("message","Database connection issue");
		   }
		
		
	}catch(Exception e) {
		e.printStackTrace();
	}
     return resultObj.toString();
}

 
}