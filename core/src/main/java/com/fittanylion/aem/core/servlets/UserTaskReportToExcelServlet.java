
package com.fittanylion.aem.core.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.fittanylion.aem.core.utils.CommonUtilities;
import com.fittanylion.aem.core.utils.SqlConstant;
import com.fittanylion.aem.core.utils.sqlDBUtil;
import com.fittanylion.aem.core.services.ValidateEmailService;
import com.fittanylion.aem.core.services.UserLoginDBService;


@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=User Weekly Task DB Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/userTaskWeeklyReport"
})
public class UserTaskReportToExcelServlet  extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(UserTaskReportToExcelServlet.class);
	
	@Reference
	private ValidateEmailService validateEmailService;
	
	@Reference
	private  UserLoginDBService userLoginDBService;
	
	@Reference
	private DataSourcePool dataSourceService;
	
	private static String[] columns = {"Customer_Id", "First_Name", "Last_Name", "Email_AD", "Task_Indicator"};
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		//Getting datasource
		try {
			
		 CommonUtilities commonUtilities = new CommonUtilities();
		 DataSource oracleDataSource =  commonUtilities.getDataSource("fittany_Datasource",dataSourceService);
	//	 String status = validateEmailService.validateEmail(oracleDataSource, request);
		 
		 connection = oracleDataSource.getConnection();
		 //String sqlquery = "select * from CUST";
		 ps =  connection.prepareStatement(SqlConstant.SELECT_QUERY_FOR_GET_ALL_CUSTOMER_FOR_EXCEL_REPORT);
		 resultSet = ps.executeQuery();  
		 
		 String taskStartDate= "21-01-2019";
	   String taskEndDate = "27-01-2019";
	   
	  
		 
		
		Workbook wb = new HSSFWorkbook();
	    Sheet weeklyTaskSheet = wb.createSheet("WeeklyTaskSheet");
	    weeklyTaskSheet.setSelected(true);
	    
	    
	 // Create a Font for styling header cells
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());
        
        // Create a CellStyle with the font
        CellStyle headerCellStyle = cellStyle(wb);
        headerCellStyle.setFont(headerFont);
        
        
     // Create a Row
        Row headerRow = weeklyTaskSheet.createRow(0);
        
     // Creating cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
       
	 
	 CellStyle style = cellStyle(wb);
	    
	 int row = 1;
		 
		 
	 int custStatusIndicator = 0;
	
	 while(resultSet.next()) {
		 int customerId = resultSet.getInt("CUST_ID");
	     String firstname = resultSet.getString("CUST_FST_NM");
	     String email = resultSet.getString("CUST_EMAIL_AD");
	     String lastname = resultSet.getString("CUST_LA_NM");
	     String taskIndicator = "N";
	    
	  
	     custStatusIndicator =	userLoginDBService.readingCustStatusForCurrentWeek(connection, customerId, taskStartDate, taskEndDate);
	     System.out.println("custStatusIndicator====>" + custStatusIndicator);
	     if(custStatusIndicator > 0) {
	    	 taskIndicator = "Y";
	     }
	     
	     
	     Row dataRow = weeklyTaskSheet.createRow(row);
		 Cell dataCustIdCell = dataRow.createCell(0);
		 dataCustIdCell.setCellValue(customerId);
		 dataCustIdCell.setCellStyle(style);
	     
	     Cell datafirstNameCell = dataRow.createCell(1);
	     datafirstNameCell.setCellValue(firstname);
	     datafirstNameCell.setCellStyle(style);
	     
	     Cell datalastNameCell = dataRow.createCell(2);
	     datalastNameCell.setCellValue(lastname);
	     datalastNameCell.setCellStyle(style);
	     
	     Cell dataEmailCell = dataRow.createCell(3);
	     dataEmailCell.setCellValue(email);
	     dataEmailCell.setCellStyle(style);
	     
	     Cell datataskIndicatorCell = dataRow.createCell(4);
	     datataskIndicatorCell.setCellValue(taskIndicator);
	     datataskIndicatorCell.setCellStyle(style);
	    
	     System.out.println("taskIndicator" + taskIndicator);
	     
	     row = row + 1;
	 }
	 
	 autoSizeColumnWidth(weeklyTaskSheet, columns);
	 
//
//	 String outputDirPath = "/Users/gowrishankarjallu/Desktop/ IMPORTANT/WeeklyTaskSheet.xls";
//	 FileOutputStream fileOut = new FileOutputStream(outputDirPath);
//	 wb.write(fileOut);
//	 fileOut.close();
	 
	 String filename = "WeeklyTaskSheet";
	 response.setContentType("application/vnd.ms-excel");
     response.setHeader("Content-Disposition", "attachment; filename="+ filename +".xls");
     wb.write(response.getOutputStream());
     wb.close();
    // wb.dispose();
     
	} catch (Exception e) {
		LOG.error("Expection inside doGet of UserTaskReportToExcelServlet :::", e.getMessage());
		e.printStackTrace();
	} finally {
		sqlDBUtil.sqlConnectionClose(resultSet, connection, ps, LOG);
	}
	
	
  }

	public  CellStyle cellStyle(Workbook wb) {
		
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THICK);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderTop(BorderStyle.THICK);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
		
		return cellStyle;
	}

	public void autoSizeColumnWidth(Sheet weeklyTaskSheet, String [] columns) {
		// Auto size the column widths
		 for(int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
			 weeklyTaskSheet.autoSizeColumn(columnIndex);
		 }
	}
  
}
