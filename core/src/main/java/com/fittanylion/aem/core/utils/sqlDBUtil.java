package com.fittanylion.aem.core.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

public class sqlDBUtil {

	
	public static void sqlConnectionClose(ResultSet resultSet, Connection connection, PreparedStatement preparedstatement, Logger LOG) {
		if (resultSet != null) {
            try {
         	   resultSet.close();
         	   resultSet = null;
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e.fillInStackTrace());
            }
        }

        if (preparedstatement != null) {
            try {
         	   preparedstatement.close();
         	   preparedstatement = null;
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e.fillInStackTrace());
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
     	   LOG.error(sqle.getMessage(), sqle.fillInStackTrace());
        }
    }
	
}
