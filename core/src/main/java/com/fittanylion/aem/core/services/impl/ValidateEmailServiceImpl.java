package com.fittanylion.aem.core.services.impl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

import com.fittanylion.aem.core.services.ValidateEmailService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = ValidateEmailService.class)
public class ValidateEmailServiceImpl implements ValidateEmailService {

	@Override
	public String validateEmail(DataSource dataSource, SlingHttpServletRequest request) {
		try {
			JSONObject jsonObject = new JSONObject();
			String email = request.getParameter("email");
			if(dataSource != null && email != null) {
				final Connection connection = dataSource.getConnection();
				final Statement statement = connection.createStatement();
				String emailQuery = "Select * from CUST Where CUST_EMAIL_AD='" + email + "'";
				ResultSet emailResultSet = statement.executeQuery(emailQuery);
				if(emailResultSet.getFetchSize() > 0) {
					 jsonObject.put("statusCode",400);
					 jsonObject.put("message","email id already exists");
					return jsonObject.toString();
				}else {
					jsonObject.put("statusCode",200);
					jsonObject.put("message","You can keep this mail id.");
					return jsonObject.toString();
				}
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
