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
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;

@Component(immediate = true, service = ForgotPassWordService.class)
public class ForgotPassWordServiceImpl implements ForgotPassWordService {

 private static final Logger LOG = LoggerFactory.getLogger(ForgotPassWordServiceImpl.class);
 
 	@Reference
	private MessageGatewayService messageGatewayService;

@Override
public String updatePassWordInDB(DataSource dataSource, SlingHttpServletRequest request) {
	JSONObject resultObj = new JSONObject();
	
	ResultSet resultSet = null;
	Connection connection = null;
	PreparedStatement preparedstatement = null;
	ResultSet resultCustKey = null;
	PreparedStatement updatePreparedStmt = null;
	
	try {
		if (dataSource != null) {
			String key = request.getParameter("key");
			 connection = dataSource.getConnection();
			 
		
		    preparedstatement = connection.prepareStatement(SqlConstant.CUST_TOKEN_VALIDATE);
		    preparedstatement.setString(1, key);
		     resultSet = preparedstatement.executeQuery();
		    boolean isKeyExists = false;
		    while (resultSet.next()) {
		    	isKeyExists = true;
		    //	System.out.println();
		    }
		   if(isKeyExists) {
			   
			   try {
				   
				   String custPassword = request.getParameter("custnewPassword");
				   
				   Decoder decoder = Base64.getDecoder();
			       String newPassWord = new String(decoder.decode(custPassword));

				   byte[] salt = CommonUtilities.getSalt();
			       String secureNewPassword = CommonUtilities.get_SHA_1_SecurePassword(newPassWord, salt);
			      
			       String updateQuery = " update CUST SET CUST_PW_TOK_NO = ? , CUST_PW_ID = ? where CUST_PW_TOK_NO = '" + key + "'";
			      
			       System.out.println("This is the failure point====>");
			           updatePreparedStmt = connection.prepareStatement(updateQuery);
			          updatePreparedStmt.setString(1, secureNewPassword);
			          updatePreparedStmt.setString(2, newPassWord);
			          resultCustKey = updatePreparedStmt.executeQuery();
			       
			            resultObj.put("statusCode",200);
			            resultObj.put("message","Your Password has been successfully updated");
			            return resultObj.toString();
				   
			   }catch(Exception e) {
				   e.printStackTrace();
			   }finally {
					sqlDBUtil.sqlResultSetAndPreparedStatementClose(resultCustKey, updatePreparedStmt, LOG);
			   }
			   
			  
		   }
		}
		 resultObj.put("statusCode",400);
	     resultObj.put("message","Some issue in updating your password.Please try later");
	}catch(Exception e) {
		e.printStackTrace();
	}finally {
		sqlDBUtil.sqlConnectionClose(resultSet, connection, preparedstatement, LOG);
	}
     return resultObj.toString();
}



@Override
public String sendChangePassWordLinkToMail(DataSource dataSource, SlingHttpServletRequest request) {
	JSONObject resultObj = new JSONObject();
	String firstName = null;
	
	ResultSet resultSet = null;
	Connection connection = null;
	PreparedStatement preparedStmt = null;
	try {
		LOG.info("Checking the data source in Forgot Pwd Impl===>" + dataSource);
		if (dataSource != null) {
			
			String emailId = request.getParameter("emailId");
		    connection = dataSource.getConnection();
		 //   String sql = "select CUST_FST_NM,CUST_PW_TOK_NO from FTA.CUST where CUST_EMAIL_AD = ?";
		    preparedStmt = connection.prepareStatement(SqlConstant.CUST_DETAILS_TOKEN);
		    preparedStmt.setString(1, emailId);
		    resultSet = preparedStmt.executeQuery();
		    boolean isEmailExists = false;
		    while (resultSet.next()) {
		    	isEmailExists = true;
		    	 firstName = resultSet.getString("CUST_FST_NM"); 
		    	String hashKey = resultSet.getString("CUST_PW_TOK_NO");
		    	ResourceResolver resolver = request.getResourceResolver();
				sendForgotEmail(messageGatewayService,emailId,hashKey,firstName,resolver);
		    }
		}
		if(firstName != null) {
			
			 resultObj.put("statusCode",200);
		     resultObj.put("message","An Email has sent to the registered email");
		}else {
			
			 resultObj.put("statusCode",400);
		     resultObj.put("message","This Email is not registered.Please enter the registerd email.");
		}
		
	}catch(Exception e) {
		LOG.info("If data source is null in Forgot password Impl===>" + e);
		e.printStackTrace();
	}finally {
		sqlDBUtil.sqlConnectionClose(resultSet, connection, preparedStmt, LOG);
	}
     return resultObj.toString();
}

public static void sendForgotEmail(MessageGatewayService messageGatewayService,String recipientMailId,String hashKey,String firstName,ResourceResolver resolver ){
    try {
    	
        ArrayList<InternetAddress> emailRecipients = new ArrayList<InternetAddress>();
        String templateLink="/content/digital-marketing/en/highmark/fittanylion/useremailregister.html?wcmmode=disabled";

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
        
        bufString = bufString.replace("${firstName}", firstName);
        bufString = bufString.replace("${hashKey}", hashKey);
       
        System.out.println(bufString);
        LOG.info("mesage.."+bufString);
        HtmlEmail email = new HtmlEmail();

        emailRecipients.add(new InternetAddress(recipientMailId));
        email.setCharset("UTF-8");
        email.setFrom("noreply@fittanylion.com");
        email.setTo(emailRecipients);
        email.setSubject("RESET PASSWORD");
        email.setHtmlMsg(bufString);
        MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
        LOG.info("Forgot pwd message gateway sending email===>");
        messageGateway.send(email);
        LOG.info("Forgot pwd successfully sent email===>");
        emailRecipients.clear();
    } catch (Exception e) {
    	
     
        LOG.error("Exception while sending Forgot Password Email===>" + e);
        e.printStackTrace();
    }
}

 
}