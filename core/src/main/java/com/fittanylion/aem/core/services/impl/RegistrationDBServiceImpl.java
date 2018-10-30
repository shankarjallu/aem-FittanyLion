package com.fittanylion.aem.core.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.sql.Date;
import javax.sql.DataSource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.services.RegistrationDBService;
import java.sql.Timestamp;


@Component(immediate = true, service = RegistrationDBService.class)
public class RegistrationDBServiceImpl implements RegistrationDBService {

 private static final Logger LOG = LoggerFactory.getLogger(RegistrationDBServiceImpl.class);

 public String insertIntoDataBase(DataSource dataSource, SlingHttpServletRequest request) {

  String insertStatus = "failured";
  int custIdentifier = Integer.parseInt(request.getParameter("custIdentifier"));
  String custFirstName = request.getParameter("custFirstName");
  String custLastName = request.getParameter("custLastName");
  String custDOBRangeDesc = request.getParameter("custDOBRangeDesc");
  String custEmailAddress = request.getParameter("custEmailAddress");

  if(custEmailAddress != null){
	  custEmailAddress = custEmailAddress.toLowerCase();}

  String custPassword = request.getParameter("custPassword");
  String custPennStateUnivAlumniIN = request.getParameter("custPennStateUnivAlumniIN");
  String custRecordMntdID = request.getParameter("custRecordMntdID");
 // String custRecordMntdTimestamp = request.getParameter("custRecordMntdTimestamp");




//This is for dev testing  only
//	 int custIdentifier = 771177;
//	 String custFirstName = "shnkar";
//	 String custLastName = "jallu";
//	 String custDOBRangeDesc = "25 and below";
//	 String custEmailAddress = "shan@gmail.com";
//	 String custPassword = "c2hhbmthcjExNA==";
////	 String custPennStateUnivAlumniIN = "yes";
//	 //String custRecordMntdID = "123";
  
  Decoder decoder = Base64.getDecoder();
  String customerPassword = new String(decoder.decode(custPassword));

	 System.out.println("This is the custpasswors" + customerPassword);

  try {
	  ///Date custRecordMntdTS=Date.valueOf(custRecordMntdTimestamp);//converting string into sql date
	  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	   if (dataSource != null) {
	    final Connection connection = dataSource.getConnection();
	    final Statement statement = connection.createStatement();
	    String query = " insert into CUST (CUST_ID, CUST_FST_NM,CUST_LA_NM,CUST_DOB_RNG_DS,\n" +
				"CUST_EMAIL_AD,CUST_PW_ID,CUST_PENN_STE_UNIV_ALUM_IN,CUST_RCD_MNTD_ID,CUST_RCD_MNTD_TS,CUST_RCD_MNTD_TS)" +
	     " values (?, ?, ?, ?, ?, ?,?,?,?)";
	    PreparedStatement preparedStmt = connection.prepareStatement(query);
	    preparedStmt.setInt(1, custIdentifier);
	    preparedStmt.setString(2, custFirstName);
	    preparedStmt.setString(3, custLastName);
	    preparedStmt.setString(4, custDOBRangeDesc);
	    preparedStmt.setString(5, custEmailAddress);
	    preparedStmt.setString(6, customerPassword);
	    preparedStmt.setString(7, custPennStateUnivAlumniIN);
	    preparedStmt.setString(8, custRecordMntdID);
	  preparedStmt.setTimestamp(9, timestamp);
	    int isInsert = preparedStmt.executeUpdate();
	    LOG.info(isInsert + "Insert into Cust data base");
	    connection.close();
	    if (isInsert != 0)
	     insertStatus = "success";

	    //Need to call messageGateway here
	   }
	  } catch (Exception e) {
	   e.printStackTrace();
	  insertStatus = e.getMessage();

	 // System.out.println(e.getMessage());
	   LOG.error("Exception in RegistrationDBService....=> " + e.getMessage());
	  }
	  return insertStatus;
 }
}