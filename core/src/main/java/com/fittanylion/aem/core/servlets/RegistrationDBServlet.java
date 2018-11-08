package com.fittanylion.aem.core.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.mail.internet.InternetAddress;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.services.RegistrationDBService;
import org.apache.sling.commons.json.JSONObject;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "= Fittany User Registration Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/getUserRegistrationServlet"
})
public class RegistrationDBServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RegistrationDBServlet.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Reference
	private RegistrationDBService registrationDBService;
	
	 @Reference
	    private MessageGatewayService messageGatewayService;
	    static ResourceResolver resolver = null;

	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {		
		try {
			JSONObject jsonObject = new JSONObject();
			//Getting datasource
			 CommonUtilities commonUtilities = new CommonUtilities();
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 String insertStatus = registrationDBService.insertIntoDataBase(oracleDataSource, request);
			if(!insertStatus.equals("success")) {
				jsonObject.put("statusCode",400);
				jsonObject.put("message",insertStatus);
				response.getOutputStream().print(jsonObject.toString());
			}else
			{
				jsonObject.put("statusCode",200);
				jsonObject.put("message",insertStatus);
				response.getOutputStream().print(jsonObject.toString());
				System.out.println("This is the place we need to send the user registration Hurray====>");
			//	 String email = request.getParameter("email");
				
				try { 
					String email = "Gowrishankar.jallu@highmarkhealth.org";
					resolver = request.getResourceResolver();
					 sendEmail(messageGatewayService,email);
				}catch(Exception e) {
					LOG.error("User Email Registration failed.....=>",e);
				}
				 
			}

		}catch(Exception e) {
			LOG.error("Exception in Registration Servlet.....=>",e);
		}
	}
	
	
	 public static void sendEmail(MessageGatewayService messageGatewayService,String recipientMailId ){
	        try {
	            ArrayList<InternetAddress> emailRecipients = new ArrayList<InternetAddress>();
	            String templateLink="/apps/hha/dmxfla/emailtemplates/exactTargetTemplate.txt";

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

	            bufString = bufString.replace("${email}", recipientMailId);
	            LOG.info("mesage.."+bufString);
	            HtmlEmail email = new HtmlEmail();

	            emailRecipients.add(new InternetAddress(recipientMailId));
	            email.setCharset("UTF-8");
	            email.setTo(emailRecipients);
	            email.setSubject("This is the test mail--->");
	            email.setHtmlMsg(bufString);
	            MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
	            messageGateway.send(email);
	            emailRecipients.clear();
	        } catch (Exception e) {
	            LOG.info(e.getMessage());
	            e.printStackTrace();
	        }
	    }

}
