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
import com.fittanylion.aem.core.services.ForgotPassWordService;
import com.fittanylion.aem.core.services.RegistrationDBService;
import org.apache.sling.commons.json.JSONObject;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "= Fittany User Forgot Pass Word Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths="+ "/bin/getForgotPasswordServlet"
})
public class ForgotPassWordServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ForgotPassWordServlet.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Reference
	private ForgotPassWordService forgotPassWordService;
	
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {		
		try {
			//Getting datasource
			 CommonUtilities commonUtilities = new CommonUtilities();
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 String responseText = null;
			 		 
			 if(request.getParameterMap().containsKey("key")) {
				 responseText= forgotPassWordService.updatePassWordInDB(oracleDataSource, request);
			 }else if(request.getParameterMap().containsKey("emailId")){
				 responseText = forgotPassWordService.sendChangePassWordLinkToMail(oracleDataSource, request);
			 }
			 response.getOutputStream().print(responseText);
		}catch(Exception e) {
			LOG.error("Exception in Registration Servlet.....=>",e);
		}
	}
	
}
