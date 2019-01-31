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
	
	public static final String TASK_WEEKLY_COUNT_SELECT_QUERY = "select * from TSKWKY where TSKWKY_STRT_DT >= ? and TSKWKY_END_DT <= ?";
	
	public static final String INSERT_INTO_CUST_TASK_STATUS_QUERY  = "insert into CUSTTSKSTA( CUST_ID, TSKWKY_CT, CUSTTSKSTA_CHNC_CT, CUSTTSKSTA_WKY_STA_CD,CUSTTSKSTA_RCD_MNTD_TS) values (?, ?, ?, ?, ?)";
	
	//Forgot password Impl Queries
	public static final String CUST_TOKEN_VALIDATE  = "select * from CUST where CUST_PW_TOK_NO = ?";
	//public static final String CUST_UPDATE_TOKEN = " update FTA.CUST SET CUST_PW_TOK_NO = ? , CUST_PW_ID = ? where CUST_PW_TOK_NO = '" + key + "'";
	public static final String CUST_DETAILS_TOKEN = "select CUST_FST_NM,CUST_PW_TOK_NO from CUST where CUST_EMAIL_AD = ?";
	
	//This is Registartion Servelet Query.In prod value FTA.CUST$CUST_ID.NEXTVAL for CUST_ID
	public static final String  CUST_TABLE_INSERT = " insert into CUST (CUST_FST_NM,CUST_LA_NM,CUST_DOB_RNG_DS,\n" +
            "CUST_EMAIL_AD,CUST_PW_ID,CUST_PENN_STE_UNIV_ALUM_IN,CUST_RCD_MNTD_TS,CUST_PW_TOK_NO,CUST_PW_TOK_EXI_DT,CUST_PW_STA_DS)" +
  " values (?, ?, ?, ? , ? , ? ,? ,? ,? ,? )";
 
	
	public static final String TASK_FROM_START_AND_END_DATE_QUERY = "select * from TSK where TSK_STRT_DT >= ? and TSK_END_DT <= ?";
	
	public static final String TASK_DETAIL_IN_BETWEEN_START_AND_END_DATE_QUERY = "select * from TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT";
	
	public static final String UPDATE_TASK_QUERY = " update TSK SET TSK_TTL_NM = ? , TSK_DS = ?, TSK_MAN_DS = ?, TSK_SEQ_NO = ?, TSK_STRT_DT = ?, TSK_END_DT = ?,TSK_RCD_MNTD_TS = ? where TSK_STRT_DT = ? AND TSK_END_DT = ? AND TSK_SEQ_NO= ?";
	
	//public static final String INSERT_TTSKWLY_QUERY  = "insert into TSKWKY (TSKWKY_CT,TSKWKY_STRT_DT, TSKWKY_END_DT, TSKWKY_RCD_MNTD_TS) values (FTA.TSKWKY$TSKWKY_CT.nextval,?,?,?)";
	
	public static final String INSERT_TTSKWLY_QUERY  = "insert into TSKWKY (TSKWKY_STRT_DT, TSKWKY_END_DT, TSKWKY_RCD_MNTD_TS) values (?,?,?)";
	
	public static final String SELECT_QUERY_FOR_TASK_CHANCE = "select * from TSKWKY where TSKWKY_STRT_DT >= ? and TSKWKY_END_DT <= ?";
	
	//public static final String  INSERT_INTO_TASK_TABLE_QUERY = "insert into TSK (TSK_ID,TSK_TTL_NM, TSK_DS, TSK_MAN_DS, TSK_SEQ_NO, TSK_STRT_DT, TSK_END_DT,TSK_CMPL_IN, TSK_RWD_PNT_CT, TSK_RCD_MNTD_TS,TSKWKY_CT) values (FTA.TSK$TSK_ID.nextval,?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
	
	public static final String  INSERT_INTO_TASK_TABLE_QUERY = "insert into TSK (TSK_TTL_NM, TSK_DS, TSK_MAN_DS, TSK_SEQ_NO, TSK_STRT_DT, TSK_END_DT,TSK_CMPL_IN, TSK_RWD_PNT_CT, TSK_RCD_MNTD_TS,TSKWKY_CT) values (?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
	
	public static final String SELECT_CUSTTK_TABLE_FOR_PRESENT_TASK_FOR_CUSTOMERID = "select * from CUSTTSK where TSK_ID = ? AND CUST_ID = ? ";
	
	public static final String INSERT_INTO_CUST_TABLE_QUERY = "insert into CUSTTSK( CUST_ID, TSK_ID, CUSTTSK_CMPL_IN, CUSTTSK_STRT_DT, CUSTTSK_END_DT, CUSTTSK_RCD_MNTD_TS) values (?, ?, ?, ?, ?,?)";
	
	public static final String SELECT_CUSTTK_FOR_START_AND_END_DATE_QUERY= "select * from CUSTTSK where CUST_ID = ? and CUSTTSK_STRT_DT >= ? and CUSTTSK_END_DT <= ?";
	
	public static final String SELECT_PASSWORD_QUERY  = "select * from CUST where CUST_PW_TOK_NO = ?";
	
	public static final String  SELECT_QUERY_FOR_TASK= "select * from TSK WHERE trunc(sysdate) BETWEEN TSK_STRT_DT AND TSK_END_DT  ORDER BY TSK_SEQ_NO";
	
	public static final String  SELECT_QUERY_DATE_RANGE_FROM_TASK_WEEKLY_TABLE  = "select * from TSKWKY WHERE trunc(sysdate) BETWEEN TSKWKY_STRT_DT AND TSKWKY_END_DT";
	
	public static final String SELECT_GET_CUST_CHANGE_COUNT  = "select * from CUSTTSKSTA WHERE CUST_ID = ?";
	
	public static final String SELECT_CUST_TASK_STATUS_DETAILS_QUERY  = "select * from CUSTTSK where CUST_ID = ? and CUSTTSK_STRT_DT >= ? and CUSTTSK_END_DT <= ?";
	
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

