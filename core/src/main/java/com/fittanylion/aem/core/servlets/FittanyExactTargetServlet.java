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
import org.apache.commons.mail.HtmlEmail;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

@Component(service=Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Fittany Exact Target Status Template Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths="+ "/bin/getFittanyExactTargetStatus"
        })
public class FittanyExactTargetServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(FittanyExactTargetServlet.class);
    @Reference
    private MessageGatewayService messageGatewayService;
    static ResourceResolver resolver = null;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse httpServletResp) throws ServletException, IOException {

        String email = request.getParameter("email");

        resolver = request.getResourceResolver();
        sendEmail(messageGatewayService,email);


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



