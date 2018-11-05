package com.fittanylion.aem.core.utils;

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
}
