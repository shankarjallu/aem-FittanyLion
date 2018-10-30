package com.fittanylion.aem.core.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
public class LoginDBService {
	
	private static final Logger log = LoggerFactory.getLogger(LoginDBService.class);

	public DataSource getDataSource(String dataSourceName,DataSourcePool dataSourceService) {
        log.info("Using DataSourcePool service lookup " +
                "to get connection pool " + dataSourceName); 
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) dataSourceService.getDataSource(
                    dataSourceName);
        } catch (DataSourceNotFoundException e) {
            log.error("Unable to find datasource {}.", dataSourceName, e);
        }
        return dataSource;
    }
	
	public String verifyUserLogin(DataSource dataSource,SlingHttpServletRequest request) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");


//		String custEmailAddress = request.getParameter("custEmailAddress");
//		String custPassword = request.getParameter("custPassword");
//       Base64.Decoder decoder = Base64.getDecoder();
//       String customerPassword = new String(decoder.decode(custPassword));


		try {
		     if(dataSource != null) {
		    	 final Connection connection = dataSource.getConnection();
		          final Statement statement = connection.createStatement();
		          if (username != null && password != null) {
			            String sql = "Select * from users_table Where username='" + username + "' and password='" + password + "'";
			            ResultSet resultSet = statement.executeQuery(sql);
			            if (resultSet.next()) {
			            	return "success";

			            } else {
			            	return "failured";
			            }
			        }
		      } 
		   }catch (Exception e) {
			   e.printStackTrace();
		    } 
		return "failure";
		
	}
}
