package com.fittanylion.aem.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.bean.TaskItem;

public class CommonUtilities {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonUtilities.class);

	 public DataSource getDataSource(String dataSourceName, DataSourcePool dataSourceService) {
		 LOG.info("Using DataSourcePool service lookup to get connection pool......=> " + dataSourceName);
	  DataSource dataSource = null;
	  try {
	   dataSource = (DataSource) dataSourceService.getDataSource(
	    dataSourceName);
	  } catch (DataSourceNotFoundException e) {
		  LOG.error("Unable to find datasource.........=>", dataSourceName, e);
	  }
	  return dataSource;
	 }
	 
	 public static String get_SHA_1_SecurePassword(String passwordToHash, byte[] salt)
	    {
	        String generatedPassword = null;
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-1");
	            md.update(salt);
	            byte[] bytes = md.digest(passwordToHash.getBytes());
	            StringBuilder sb = new StringBuilder();
	            for(int i=0; i< bytes.length ;i++)
	            {
	                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	            }
	            generatedPassword = sb.toString();
	        }
	        catch (NoSuchAlgorithmException e)
	        {
	            e.printStackTrace();
	        }
	        return generatedPassword;
	    }
	     
	   
	     
	    //Add salt
	 public static byte[] getSalt() throws NoSuchAlgorithmException
	    {
	        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	        byte[] salt = new byte[16];
	        sr.nextBytes(salt);
	        return salt;
	    }
	 
		public static String JSON_DATA = "[" +
								"{" +
					             "\"title\": \"You're Unstoppable\", "+
					              "\"fitttany_Img\": \"/content/dam/digital-marketing/en/highmark/fittanylion/NL_02@2x.png\""
					          + "}," +
					          "{" +
					          "\"title\": \"Faaaaaantastic\", "+
					           "\"fitttany_Img\": \"/content/dam/digital-marketing/en/highmark/fittanylion/NL_03@2x.png\""
					           + "}," +
					           "{" +
						          "\"title\": \"Welldone\", "+
						           "\"fitttany_Img\": \"/content/dam/digital-marketing/en/highmark/fittanylion/NL_04@2x.png\""
						           + "}," +
						           "{" +
							          "\"title\": \"That's yhe Spirit\", "+
							           "\"fitttany_Img\": \"/content/dam/digital-marketing/en/highmark/fittanylion/NL_05@2x.png\""
							           + "}," +
							           "{" +
								          "\"title\": \"You will be a Nittany Fit in No time\", "+
								           "\"fitttany_Img\": \"/content/dam/digital-marketing/en/highmark/fittanylion/NL_06@2x.png\""
								           + "}," +
								           "{" +
									          "\"title\": \"You are doing great.\", "+
									           "\"fitttany_Img\": \"/content/dam/digital-marketing/en/highmark/fittanylion/NL_07@2x.png\""
									           + "}," 
					 
          				+ "]";
	 
         
	 public static TaskItem getRandomTitleAndImage(List<TaskItem> list) {
		    Random random = new Random();
		    TaskItem  taskItem=list.get(random.nextInt(list.size()));
		    return taskItem;
		}
	 
	 public static List<TaskItem>  getTaskTitleAndImage() {
	 JSONArray jsonArr;
	 List<TaskItem> listdata = new ArrayList<TaskItem>(); 
	 try {
		jsonArr = new JSONArray(JSON_DATA);
			if (jsonArr != null) { 
			   for (int i=0;i<jsonArr.length();i++){ 
				   JSONObject jsonObj = jsonArr.getJSONObject(i);

				   TaskItem taskitem = new TaskItem();
				   taskitem.setTitle(jsonObj.getString("title"));
				   taskitem.setImagePath(jsonObj.getString("fitttany_Img"));
			    listdata.add(taskitem);
			   } 
			}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return listdata;
	 }
	 
}
