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

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.fittanylion.aem.core.services.ForgotPassWordService;
import com.fittanylion.aem.core.utils.CommonUtilities;

@Component(immediate = true, service = ForgotPassWordService.class)
public class ForgotPassWordServiceImpl implements ForgotPassWordService {

 private static final Logger LOG = LoggerFactory.getLogger(ForgotPassWordServiceImpl.class);
 
 	@Reference
	private MessageGatewayService messageGatewayService;

@Override
public String updatePassWordInDB(DataSource dataSource, SlingHttpServletRequest request) {
	JSONObject resultObj = new JSONObject();
	try {
		if (dataSource != null) {
			String key = request.getParameter("key");
			Connection connection = dataSource.getConnection();
		    String sql = "select * from CUST where CUST_PW_TOK_NO = ?";
		    PreparedStatement preparedStmt = connection.prepareStatement(sql);
		    preparedStmt.setString(1, key);
		    ResultSet resultSet = preparedStmt.executeQuery();
		    boolean isKeyExists = false;
		    while (resultSet.next()) {
		    	isKeyExists = true;
		    //	System.out.println();
		    }
		   if(isKeyExists) {
			   String custPassword = request.getParameter("custnewPassword");
			   
			   Decoder decoder = Base64.getDecoder();
		       String newPassWord = new String(decoder.decode(custPassword));

			   byte[] salt = CommonUtilities.getSalt();
		       String secureNewPassword = CommonUtilities.get_SHA_1_SecurePassword(newPassWord, salt);
		       System.out.println("This is the key......==>" + key);
		       
		       
		       String updateQuery = " update CUST SET CUST_PW_TOK_NO = ? , CUST_PW_ID = ? where CUST_PW_TOK_NO = '" + key + "'";
		      
		       System.out.println("This is the failure point====>");
		          PreparedStatement updatePreparedStmt = connection.prepareStatement(updateQuery);
		          updatePreparedStmt.setString(1, secureNewPassword);
		          updatePreparedStmt.setString(2, newPassWord);
		         int isInsert = updatePreparedStmt.executeUpdate();
		         System.out.println("This is update...." + isInsert);
		            resultObj.put("statusCode",200);
		            resultObj.put("message","Updated password in customer table.");
		            return resultObj.toString();
		   }
		}
		 resultObj.put("statusCode",400);
	     resultObj.put("message","Not updated password.");
	}catch(Exception e) {
		e.printStackTrace();
	}
     return resultObj.toString();
}

@Override
public String sendChangePassWordLinkToMail(DataSource dataSource, SlingHttpServletRequest request) {
	JSONObject resultObj = new JSONObject();
	String firstName = null;
	try {
		if (dataSource != null) {
			String emailId = request.getParameter("emailId");
		    Connection connection = dataSource.getConnection();
		    String sql = "select CUST_FST_NM,HASH_KEY from CUST where CUST_EMAIL_AD = ?";
		    PreparedStatement preparedStmt = connection.prepareStatement(sql);
		    preparedStmt.setString(1, emailId);
		    ResultSet resultSet = preparedStmt.executeQuery();
		    boolean isEmailExists = false;
		    while (resultSet.next()) {
		    	isEmailExists = true;
		    	 firstName = resultSet.getString("CUST_FST_NM"); 
		    	String hashKey = resultSet.getString("HASH_KEY");
		    	System.out.println(firstName+"maillllllllllllllllllllllll"+hashKey);
		    	ResourceResolver resolver = request.getResourceResolver();
				sendEmail(messageGatewayService,emailId,hashKey,firstName,resolver);
		    }
		}
		if(firstName != null) {
			System.out.println("This is firstName...======>" + firstName);
			 System.out.println("This is coming from here....======>");
			 resultObj.put("statusCode",200);
		     resultObj.put("message","Email Sent to the user.");
		}else {
			System.out.println("This is firstName...======>" + firstName);
			 System.out.println("This is coming from here....======>");
			 resultObj.put("statusCode",400);
		     resultObj.put("message","This Email is not registered.Please enter the registerd email.");
		}
		
	}catch(Exception e) {
		e.printStackTrace();
	}
     return resultObj.toString();
}

public static void sendEmail(MessageGatewayService messageGatewayService,String recipientMailId,String hashKey,String firstName,ResourceResolver resolver ){
    try {
    	System.out.println("maillllllllllllllllllllllll");
        ArrayList<InternetAddress> emailRecipients = new ArrayList<InternetAddress>();
        String templateLink="/apps/hha/dmxfla/emailtemplates/forgot.txt";

        Session session = resolver.adaptTo(Session.class);
        System.out.println(recipientMailId+"========================="+session);
        String templateReference = templateLink.substring(1)+ "/jcr:content";
        Node root = session.getRootNode();
        Node jcrContent = root.getNode(templateReference);
        System.out.println(jcrContent.getPath());

        InputStream is = jcrContent.getProperty("jcr:data").getBinary().getStream();

        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int resultNumber = bis.read();
        while (resultNumber != -1) {
            byte b = (byte) resultNumber;
            buf.write(b);
            resultNumber = bis.read();
        }
        String bufString = buf.toString();
        LOG.info("template.."+bufString);
        System.out.println(bufString);
        System.out.println(firstName+"mai222222222222"+hashKey);
        bufString = bufString.replace("${firstName}", firstName);
        bufString = bufString.replace("${hashKey}", hashKey);
        System.out.println(firstName+"mai3333333333333333"+hashKey);
        System.out.println(bufString);
        LOG.info("mesage.."+bufString);
        HtmlEmail email = new HtmlEmail();

        emailRecipients.add(new InternetAddress(recipientMailId));
        email.setCharset("UTF-8");
        email.setFrom("gowrishankar.jallu@gmail.com");
        email.setTo(emailRecipients);
        email.setSubject("This is the test mail--->");
        email.setHtmlMsg(bufString);
        MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
        messageGateway.send(email);
        emailRecipients.clear();
    } catch (Exception e) {
    	System.out.println(e.getMessage());
        LOG.info(e.getMessage());
        e.printStackTrace();
    }
}

 
}