package com.fittanylion.aem.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;

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
}
