package com.fittanylion.aem.core.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.mail.internet.InternetAddress;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;


@Component(service=Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Fittany Exact Target & API Status Servlet",
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
        HttpClient client = new DefaultHttpClient();

        try {


            System.out.println("testing before call");
            httpServletResp.getOutputStream().print(getJsonHttpPost(client, email, messageGatewayService
            ));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String getJsonHttpPost(HttpClient client, String email, MessageGatewayService messageGatewayService
    ) throws JSONException, ClientProtocolException, IOException {
        String responseText = "failure";
        HttpPost post = new HttpPost("https://servicesdenv.highmark.com/dpsext/x-services/exacttarget/hub/v1/dataevents");
        // post.setHeader("Authorization", "Bearer "+accessToken);
        post.setHeader("Content-Type", "application/json");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject emailObject = new JSONObject();
        emailObject.put("Email", email);
        jsonObject.put("keys", emailObject);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("testing new......");


        JSONObject phoneObject = new JSONObject();
        phoneObject.put("LastLogin", dateFormat.format(date));
        //phoneObject.put("Name", "test");
        jsonObject.put("values", phoneObject);
        jsonArray.put(jsonObject);

        StringEntity input = new StringEntity(jsonArray.toString());

        System.out.println("api called now.....=>");



        post.setEntity(input);

        HttpResponse response = client.execute(post);

        System.out.println("api success.....=>");
        Header[] header = post.getAllHeaders();


        for (int i = 0; i < header.length; i++) {
            System.out.println(header[i]);
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent()));
        String line = "";
        String responseString = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            responseString = line;
        }
        System.out.println(response.getStatusLine());
        if (response.getStatusLine().toString().contains("200")) {
            responseText = "success";
            sendEmail(messageGatewayService, email);
        }
        return responseString;


    }

    public static void sendEmail(MessageGatewayService messageGatewayService,String recipientMailId ){
        try {
            ArrayList<InternetAddress> emailRecipients = new ArrayList<InternetAddress>();
            String templateLink="/apps/hha/dmxfla/emailtemplates/exactTargetTemplate.txt";
            //String recipientMailId = request.getParameter("email");
            //String firstName = request.getParameter("firstName");
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
            //bufString = bufString.replace("${firstName}", firstName);
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