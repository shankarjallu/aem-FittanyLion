package com.fittanylion.aem.core.services.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittanylion.aem.core.services.ValidateEmailService;
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;


@Component(immediate = true, service = ValidateEmailService.class)
public class ValidateEmailServiceImpl implements ValidateEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ValidateEmailServiceImpl.class);

	@Override
	public String validateEmail(DataSource dataSource, SlingHttpServletRequest request) {
		LOG.info("Inside validateEmail method of ValidateEmailServiceImpl");
		JSONObject jsonconnection = new JSONObject();
		Connection connection = null;
		PreparedStatement pstatement = null;
		ResultSet emailResultSet = null;
		try {
			JSONObject jsonValidateEmai = new JSONObject();
			String email = request.getParameter("email");
			LOG.info("Email address for validation :-" + email);
			if(dataSource != null && email != null) {
				connection = dataSource.getConnection();
				pstatement = connection.prepareStatement(SqlConstant.VALIDATE_EMAL_QUERY);
				pstatement.setString(1, email);
				emailResultSet = pstatement.executeQuery();
				int emailResultSize = 0;
				while(emailResultSet.next()){
					emailResultSize++;
				}
				if(emailResultSize > 0) {
					jsonValidateEmai.put("statusCode",400);
					jsonValidateEmai.put("message","This Email Id is already registerd");
					return jsonValidateEmai.toString();
				}else {
					jsonValidateEmai.put("statusCode",200);
					jsonValidateEmai.put("message","This Email is not registered.User can have this Email Id");
					return jsonValidateEmai.toString();
				}
			}			
		}catch(SQLException sqle) {
			try {
				jsonconnection.put("statusCode",400);
				jsonconnection.put("message","Network connection issue");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			LOG.error("Exception inside method validateEmail SQL Exception: " , sqle);
			sqle.printStackTrace();
		} catch (JSONException jsone) {
			LOG.error("Exception inside method validateEmail JSON Exception: " , jsone);
			jsone.printStackTrace();
		} catch(Exception e) {
			try {
				jsonconnection.put("statusCode",400);
				jsonconnection.put("message","Something went wrong");
			} catch (JSONException je) {
				je.printStackTrace();
			}
			LOG.error("Exception inside method validateEmail Exception: " , e.getMessage());
			e.printStackTrace();
			
		} finally {
			sqlDBUtil.sqlConnectionClose(emailResultSet, connection, pstatement, LOG);
		}
		return jsonconnection.toString();
	}

}

