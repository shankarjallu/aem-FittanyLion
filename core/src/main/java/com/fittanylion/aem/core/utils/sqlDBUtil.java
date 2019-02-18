package com.fittanylion.aem.core.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;

public class sqlDBUtil {

	
	public static void sqlConnectionClose(ResultSet resultSet, Connection connection, PreparedStatement preparedstatement, Logger LOG) {
		if (resultSet != null) {
            try {
         	   resultSet.close();
         	   resultSet = null;
            } catch (SQLException e) {
                LOG.error("Exception while closing the Resultset" + e, e.fillInStackTrace());
            }
        }

        if (preparedstatement != null) {
            try {
         	   preparedstatement.close();
         	   preparedstatement = null;
            } catch (SQLException e) {
                LOG.error("Exception while closing the preparedstatement" + e, e.fillInStackTrace());
            }
        }

        try {
            if (connection!= null && !connection.isClosed()){
                if (!connection.getAutoCommit()) {
             	   connection.commit();
             	   connection.setAutoCommit(true);
                }
                connection.close();
                connection= null;
            }
        } catch (SQLException sqle) {
     	   LOG.error("Exception while closing the connection" + sqle.getMessage(), sqle.fillInStackTrace());
        }
    }
	
	public static void sqlResultSetAndPreparedStatementClose(ResultSet resultSet,PreparedStatement preparedstatement, Logger LOG) {
		if (resultSet != null) {
            try {
         	   resultSet.close();
         	   resultSet = null;
            } catch (SQLException e) {
                LOG.error("Exception while closing the Resultset" + e, e.fillInStackTrace());
            }
        }

        if (preparedstatement != null) {
            try {
         	   preparedstatement.close();
         	   preparedstatement = null;
            } catch (SQLException e) {
                LOG.error("Exception while closing the preparedstatement" + e, e.fillInStackTrace());
            }
        }
    }
	
	public static java.sql.Date convertStartDateIntoSqldateformate(String startDate) {
		java.util.Date insertStartDateTskwkly;
		java.sql.Date sqlInsertStartDate = null;
		try {
			insertStartDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
			sqlInsertStartDate = new java.sql.Date(insertStartDateTskwkly.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return sqlInsertStartDate;
		
	}
	
	public static java.sql.Date convertEndDateIntoSqldateformate(String EndDate) {
		java.util.Date insertEndDateTskwkly;
		java.sql.Date sqlInsertEndDate = null;
		try {
			insertEndDateTskwkly = new SimpleDateFormat("dd/MM/yyyy").parse(EndDate);
			sqlInsertEndDate = new java.sql.Date(insertEndDateTskwkly.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return sqlInsertEndDate;
		
	}
	
	
}
