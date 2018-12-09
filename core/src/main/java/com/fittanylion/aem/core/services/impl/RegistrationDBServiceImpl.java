package com.fittanylion.aem.core.services.impl;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.sql.Date;
import javax.sql.DataSource;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.services.RegistrationDBService;
import com.fittanylion.aem.core.utils.CommonUtilities;

import java.sql.Timestamp;



@Component(immediate = true, service = RegistrationDBService.class)
public class RegistrationDBServiceImpl implements RegistrationDBService {

 private static final Logger LOG = LoggerFactory.getLogger(RegistrationDBServiceImpl.class);

 public String insertIntoDataBase(DataSource dataSource, SlingHttpServletRequest request) {

  String insertStatus = "failured";
  
  
  
 
  try {
  
  StringBuilder sb = new StringBuilder();
  BufferedReader br = request.getReader();
  String str = null;
  while ((str = br.readLine()) != null) {
      sb.append(str);
  }
  
  System.out.println("Get JSON Object====>");
  JSONObject jsnobject = new JSONObject(sb.toString());
  // JSONObject jsnobject = new JSONObject(taskItems);
  
  

//int custIdentifier = Integer.parseInt(jsnobject.getString("custIdentifier"));
String custFirstName = jsnobject.getString("custFirstName");
String custLastName = jsnobject.getString("custLastName");
String custDOBRangeDesc = jsnobject.getString("custDOBRangeDesc");
String custEmailAddress = jsnobject.getString("custEmailAddress");

if(custEmailAddress != null){
      custEmailAddress = custEmailAddress.toLowerCase();
}

String custPassword = jsnobject.getString("custPassword");
String custPennStateUnivAlumniIN = jsnobject.getString("custPennStateUnivAlumniIN");
//String custRecordMntdID = jsnobject.getString("custRecordMntdID");

//System.out.println("This is the custIdentifier====>" + custIdentifier);
System.out.println("This is the custFirstName====>" + custFirstName);
System.out.println("This is the custLastName====>" + custLastName);


//This is for dev testing  only
//     int custIdentifier = 9000909;
//     String custFirstName = "shnkar1234";
//     String custLastName = "jallu1234";
//     String custDOBRangeDesc = "25 and below";
//     String custEmailAddress = "ppppp@gmail.com";
//     String custPassword = "c2hhbmthcjExNA==";
//     String custPennStateUnivAlumniIN = "N";
//     String custRecordMntdID = "123";

Decoder decoder = Base64.getDecoder();
String customerPassword = new String(decoder.decode(custPassword));

System.out.println("This is the custpasswors after decode===>" + customerPassword);

Timestamp timestamp = new Timestamp(System.currentTimeMillis());
if (dataSource != null) {
 final Connection connection = dataSource.getConnection();
 final Statement statement = connection.createStatement();
 
 //We need to change Table name as FTA.CUST in Test and prod.
 String query = " insert into CUST (CUST_FST_NM,CUST_LA_NM,CUST_DOB_RNG_DS,\n" +
            "CUST_EMAIL_AD,CUST_PW_ID,CUST_PENN_STE_UNIV_ALUM_IN,CUST_RCD_MNTD_TS,CUST_PW_TOK_NO,CUST_PW_TOK_EXI_DT,CUST_PW_STA_DS)" +
  " values (?, ?, ?, ? , ? , ? ,? ,? ,? ,? )";
 PreparedStatement preparedStmt = connection.prepareStatement(query);
// preparedStmt.setInt(1, custIdentifier);
 preparedStmt.setString(1, custFirstName);
 preparedStmt.setString(2, custLastName);
 preparedStmt.setString(3, custDOBRangeDesc);
 preparedStmt.setString(4, custEmailAddress);
 preparedStmt.setString(5, customerPassword);
 preparedStmt.setString(6, custPennStateUnivAlumniIN);
 preparedStmt.setTimestamp(7, timestamp);
 
 byte[] salt = CommonUtilities.getSalt();
 String securePassword = CommonUtilities.get_SHA_1_SecurePassword(customerPassword, salt); 
 preparedStmt.setString(8, securePassword);
 
 java.util.Date date = new java.util.Date();
 java.sql.Date sqlCurrentDate = new java.sql.Date(date.getTime());
 preparedStmt.setDate(9, sqlCurrentDate);
 
 preparedStmt.setString(10, "ACT");
 int isInsert = preparedStmt.executeUpdate();
 LOG.info(isInsert + "Insert into Cust data base");
 connection.close();
 if (isInsert != 0)
  insertStatus = "success";

 //Need to call messageGateway here
}
  
  
  }catch(Exception e) {
      e.printStackTrace();
      insertStatus = e.getMessage();

         // System.out.println(e.getMessage());
           LOG.error("Exception in RegistrationDBService....=> " + e.getMessage());
  }
  
      return insertStatus;
 }
}

