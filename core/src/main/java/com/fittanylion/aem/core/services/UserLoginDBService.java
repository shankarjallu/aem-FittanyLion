package com.fittanylion.aem.core.services;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

public interface UserLoginDBService {
	
	public String verifyUserLogin(DataSource dataSource,SlingHttpServletRequest request);
	public int readingCustStatusForCurrentWeek(Connection connection, int customerId, String custtaskStartDate,String custtaskEndDate);
		
}


