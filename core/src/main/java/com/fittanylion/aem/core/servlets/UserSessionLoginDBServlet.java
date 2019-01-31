
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

import com.fittanylion.aem.core.services.UserSessionLoginService;

import org.apache.sling.commons.json.JSONObject;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "= User session login Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/getUserSessionLogin"
})
public class UserSessionLoginDBServlet  extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ForgotPassWordServlet.class);
	
	@Reference
	private DataSourcePool dataSourceService;
	
	@Reference
	private UserSessionLoginService UserSessionLoginService;
	
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {	
		LOG.info("Inside doGet of UserSessionLoginDBServlet");
		try {
			//Getting datasource
			 CommonUtilities commonUtilities = new CommonUtilities();
			 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
			 String responseText = null;
			 		 
			 if(request.getParameterMap().containsKey("key")) {
				 System.out.println("Key is present=======>");
				 responseText= UserSessionLoginService.validateUserKey(oracleDataSource, request);
			 }
			 response.getOutputStream().print(responseText);
		}catch(Exception e) {
			LOG.error("Exception in User Session Login Servlet.....=>",e);
		}
	}
	
}
