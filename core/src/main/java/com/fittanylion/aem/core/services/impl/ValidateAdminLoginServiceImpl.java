
package com.fittanylion.aem.core.services.impl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

import com.fittanylion.aem.core.services.ValidateAdminLoginService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = ValidateAdminLoginService.class)
public class ValidateAdminLoginServiceImpl implements ValidateAdminLoginService {

	@Override
	public String validateAdminLogin(DataSource dataSource, SlingHttpServletRequest request) {
		JSONObject jsonObjectConnection = new JSONObject();
	
		try {
			jsonObjectConnection.put("statusCode",400);
			jsonObjectConnection.put("message","Network connection issue");
			
			
			JSONObject jsonObject = new JSONObject();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if(dataSource != null && username != null) {
				System.out.println("this is username=====>" + username);
				System.out.println("this is password=====>" + password);
				if(username.equals("FittanyAdmin") && password.equals("Fittany@123@")) {
					jsonObject.put("statusCode",200);
					jsonObject.put("message","Succuessful");
					return jsonObject.toString();
				}else {
					jsonObject.put("statusCode",400);
					jsonObject.put("message","Incorrect User Id or Password");
					return jsonObject.toString();

				}
				
			
				
			}			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		return jsonObjectConnection.toString();
	}

}

