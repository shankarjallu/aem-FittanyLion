package com.fittanylion.aem.core.services;

import javax.sql.DataSource;

public interface CustomerTaskDBService {
	public String InsertTaskCompletionStatusInDB (DataSource dataSource, String customerId, String taskStartDate, String taskEndDate, String taskID, String custTaskCompleteIndicator);
}
