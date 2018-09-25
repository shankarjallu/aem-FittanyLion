package com.fittanylion.aem.core.servlets;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;

//import test.core.servlets.RegistrationDBServlet;


public class RegistrationDBService {
	
	private static final Logger log = LoggerFactory.getLogger(RegistrationDBService.class);

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
	
	public String insertIntoDataBase(DataSource dataSource,SlingHttpServletRequest request) {
		
		int id = Integer.parseInt(request.getParameter("id"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String DOB_RNG = request.getParameter("dobrange");
		String email = request.getParameter("email");
		String pwd = request.getParameter("pwd");
		String CUST_PENN_STE_UNIV_ALUM_IN = request.getParameter("email");
		String CUST_RCD_MNTD_ID = request.getParameter("email");
		/*String email = request.getParameter("email");*/
		Date CUST_RCD_MNTD_TS = null;
		
		try {
            
            //DataSource ds = (DataSource) dspService.getDataSource("hsqldbds");   
            if(dataSource != null) {
                final Connection connection = dataSource.getConnection();
                System.out.println(connection+"111111111111111111111");
                 final Statement statement = connection.createStatement();
                 System.out.println(statement+"2222222222222222222222222");
              // the mysql insert statement
                 String query = "insert into CUST (CUST_ID, CUST_FRST_NM,CUST_LST_NM,CUST_DOB_RNG_DS,CUST_EMAIL_AD,CUST_PW_ID,CUST_PENN_STE_UNIV_ALUM_IN,CUST_RCD_MNTD_ID,CUST_RCD_MNTD_TS)"
              + "values('5','sample','lastName','25','abhi@gmail.com','abhi','Y','abhi',SYSTIMESTAMP)";
                 // create the mysql insert preparedstatement
                 PreparedStatement preparedStmt = connection.prepareStatement(query);
                 System.out.println(preparedStmt+"33333333333333333333");
                 preparedStmt.setInt(1, id);
                 preparedStmt.setString (2, firstName);
                 preparedStmt.setString   (3, lastName);
                 preparedStmt.setString(4, DOB_RNG);
                 preparedStmt.setString (5, email);
                 preparedStmt.setString (6, pwd);
                 preparedStmt.setString   (7, CUST_PENN_STE_UNIV_ALUM_IN);
                 preparedStmt.setString(8, CUST_RCD_MNTD_ID);
                 preparedStmt.setDate  (9, CUST_RCD_MNTD_TS);
                 // execute the preparedstatement
                boolean isInsert =  preparedStmt.execute();
                System.out.println(isInsert+"44444444444444");
                String insertMsg = "not inserted";
                connection.close();
                if(isInsert)
                    return "inserted successfully";
             } 
          }catch (Exception e) {
              e.printStackTrace();
           } 
       return "not inserted";
       
   }
}
