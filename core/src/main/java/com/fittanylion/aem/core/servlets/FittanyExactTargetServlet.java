package com.fittanylion.aem.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
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
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Fittany Exact Target Staus Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/getFittanyExactTargetStaus"
})
public class FittanyExactTargetServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(FittanyExactTargetServlet.class);
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse httpServletResp) throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
         HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("https://auth.exacttargetapis.com/v1/requestToken");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("clientId",
                        "mgnrzotnb2xkpvtolga8qohp"));
                nameValuePairs.add(new BasicNameValuePair("clientSecret",
                        "xDVvLm0DPzml83SGNK5NLgsi"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(post);
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String line = "";
                String accessTokenResp="";
                while ((line = rd.readLine()) != null) {
                    accessTokenResp = line;
                }
                JSONObject resp = new JSONObject(accessTokenResp);                
                httpServletResp.getOutputStream().print(getJsonHttpPost(client,resp.get("accessToken").toString(),email));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        
    }
    
    public static String getJsonHttpPost(HttpClient client,String accessToken,String email) throws JSONException, ClientProtocolException, IOException{
        String responseText = "failure";
        HttpPost post = new HttpPost("https://www.exacttargetapis.com/hub/v1/dataevents/key:AEMEXKEY/rowset");
        post.setHeader("Authorization", "Bearer "+accessToken);
        post.setHeader("Content-Type", "application/json");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject emailObject = new JSONObject();
        emailObject.put("Email", email);
        jsonObject.put("keys",emailObject);
        JSONObject phoneObject = new JSONObject();
        phoneObject.put("PhoneNumber", "6038543939");
        phoneObject.put("Name", "test");
        jsonObject.put("values",phoneObject);
        jsonArray.put(jsonObject);
        
        StringEntity input = new StringEntity(jsonArray.toString());
        //input.setContentType("application/json");
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        Header[] header = post.getAllHeaders();
        for(int i=0;i<header.length;i++){
            System.out.println(header[i]);
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println(response.getStatusLine());
        if(response.getStatusLine().toString().contains("200")){
            responseText = "success";
        }
        return responseText;
        
        
    }

}