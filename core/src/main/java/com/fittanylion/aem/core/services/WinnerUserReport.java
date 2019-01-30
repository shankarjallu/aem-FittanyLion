package com.fittanylion.aem.core.services;

import java.util.List;

import javax.sql.DataSource;

import org.apache.sling.api.SlingHttpServletRequest;

import com.fittanylion.aem.core.bean.CustomerItem;

public interface WinnerUserReport {

	public List<CustomerItem> userMonthlyWinnerReport(DataSource dataSource,SlingHttpServletRequest request);
	public CustomerItem userDetailsByCustID(int custID);
}
