package com.fittanylion.aem.core.utils;

public class SqlConstant {
	
	public static final String USER_NAME_QUERY= "Select * from CUST Where CUST_EMAIL_AD= ?";
	public static final String USER_PASSWORD_QUERY= "Select * from CUST Where CUST_EMAIL_AD= ? and CUST_PW_ID= ?";
	public static final String USER_CHANCE_TASK_QUERY = "select * from CUSTTSKSTA WHERE CUST_ID = ?";
	public static final String ALL_TASK_IN_BETWEEN_START_AND_END_DATE_QUERY = "select * from TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT  ORDER BY TSK_SEQ_NO";
	
	public static final String DATE_RANGE_FROM_TASK_WEEKLY_TABLE_QUERY = "select * from TSKWKY WHERE trunc(sysdate) BETWEEN TSKWKY_STRT_DT AND TSKWKY_END_DT";
	
	public static final String TASK_STATUS_FOR_USER_QUERY = "select * from CUSTTSK where CUST_ID = ? and CUSTTSK_STRT_DT >= ? and CUSTTSK_END_DT <= ?"; 
	
	public static final String TASK_WEEKLY_QUERY  = "select * from TSKWKY where TSKWKY_STRT_DT >= ? and TSKWKY_END_DT <= ?"; 
	
	public static final String  CUST_STATUS_QUERY  = "Select * from CUSTTSKSTA Where TSKWKY_CT= ? AND CUST_ID= ?" ;
	public static final String VALIDATE_EMAL_QUERY= "Select * from CUST Where CUST_EMAIL_AD= ?";
	
	public static final String SELECT_QUERY_FOR_GET_ALL_CUSTOMER_FOR_EXCEL_REPORT = "select * from CUST";
	
	public static final String QUERY_FOR_WINNER_USER_SELECTION =  "SELECT CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT FROM CUSTTSKSTA INNER JOIN TSKWKY ON TSKWKY.TSKWKY_CT = CUSTTSKSTA.TSKWKY_CT "
	 		+ "where (TSKWKY.TSKWKY_STRT_DT >= ? AND TSKWKY.TSKWKY_END_DT  <= ?)";
	
	public static final String QUERY_FOR_WINNER_USER_SELECTION1 = "SELECT CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT, COUNT(CUSTTSKSTA.CUST_ID) AS Total FROM CUSTTSKSTA INNER JOIN TSKWKY ON TSKWKY.TSKWKY_CT = CUSTTSKSTA.TSKWKY_CT" 
			+ "where (TSKWKY.TSKWKY_STRT_DT >= ? AND TSKWKY.TSKWKY_END_DT  <= ?) GROUP BY  CUSTTSKSTA.CUST_ID,CUSTTSKSTA.CUSTTSKSTA_CHNC_CT";
	
	public static final String SELECT_QUERY_FOR_CUST_TABLE_BY_PASSING_CUST_ID = "SELECT * FROM CUST WHERE CUST_ID = ?";
	
	//Forgot password Impl Queries
	public static final String CUST_TOKEN_VALIDATE  = "select * from CUST where CUST_PW_TOK_NO = ?";
	//public static final String CUST_UPDATE_TOKEN = " update FTA.CUST SET CUST_PW_TOK_NO = ? , CUST_PW_ID = ? where CUST_PW_TOK_NO = '" + key + "'";
	public static final String CUST_DETAILS_TOKEN = "select CUST_FST_NM,CUST_PW_TOK_NO from CUST where CUST_EMAIL_AD = ?";
	
	
	
	
	//Cutsomer Table column 
	public static final String CUST_ID = "CUST_ID";
	public static final String CUST_FST_NM = "CUST_FST_NM";
	public static final String CUST_LA_NM = "CUST_LA_NM";
	public static final String CUST_DOB_RNG_DS = "CUST_DOB_RNG_DS";
	public static final String CUST_PW_TOK_NO = "CUST_PW_TOK_NO";
	public static final String CUST_PW_ID = "CUST_PW_ID";
	public static final String CUST_EMAIL_AD = "CUST_EMAIL_AD";
	
	//Task table column 
	public static final String TSK_STRT_DT = "TSK_STRT_DT";
	public static final String TSK_END_DT = "TSK_END_DT";
	public static final String TSK_ID = "TSK_ID";
	public static final String TSK_TTL_NM = "TSK_TTL_NM";
	public static final String TSK_DS = "TSK_DS";
	public static final String TSK_MAN_DS = "TSK_MAN_DS";
	public static final String TSK_SEQ_NO = "TSK_SEQ_NO";
	
	public static final String TSKWKY_CT = "TSKWKY_CT"; 
	
	//CUSTTSK table column
	public static final String CUSTTSK_CMPL_IN = "CUSTTSK_CMPL_IN";
	
}

