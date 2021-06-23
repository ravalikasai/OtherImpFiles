/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used for defining constants
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 *
 */
public class DistributeSDSConstants {
	
	/**
	 * File constants
	 */
	public static final String ERROR_DATA_FOLDER_NAME = "ERROR_DATA";

	public static final String ARCHIVE_FOLDER_NAME = "ARCHIVE";	

	public static final String THUMBSDB_FILE_NAME = "Thumbs.db";

	public static final String FILE_SEPERATOR = "\\";
	
	public static final String LINE_SEPARATOR = "\n";

	public static final String PDF_EXTN = ".pdf";

	public static final String PROCESS_STARTED = "PROCESS_STARTED";

	public static final String SYS_OPTION_LOADED = "SYS_OPTION_LOADED";

	public static final String CLEAR_DATA_FOLDER_SUCCESS = "CLEAR_DATA_FOLDER";

	public static final String GENERATE_PDF = "GENERATE_PDF";	

	public static String coverSheet = "_coverSheet";

	public static String seperatorPage = "_seperator";

	public static String blankPage = "_blank";

	public static final String JOB_DELETION_ERROR_STATUS = "DELETION_ERROR";

	public static final String JOB_EXECUTION_SUCCESS_STATUS = "EXECUTION_SUCCESS";

	public static final String JOB_EXCEPTION_STATUS = "EXCEPTION_STATUS";

	public static final String PROCESS_STATUS_DELETED = "DELETED";

	public static final String PROCESS_STATUS_ARCHIVED = "ARCHIVED";

	public static final String PROCESS_STATUS_GENERATED = "GENERATED";

	public static final String MAIL_BODY_NO_JOB_PROCESSED_ERROR = " <font size=2 color=red> * Job Failed as  Check Event logger for details. </font>";

	public static final String FAILURE_STATUS_MESSAGE = "FAILED";

	public static final String UNENABLED_STATUS_MESSAGE= "NA";

	public static final String SUCCESS_STATUS_MESSAGE = "SUCCESS";

	public static final String PROCESS_STATUS_NOT_PROCESSED = "NOT_PROCESSED";

	public static final String PROCESS_STATUS_RETRIEVED = "RETRIEVED";

	public static final String PROCESS_STATUS_SUCCESS = "PROCESSED_SUCCESSFULLY";

	public static final String PROCESS_STATUS_TRANSFERED = "TRANSFERED";

	public static final String PROCESS_STATUS_MOUNTED = "DRIVE_MOUNTED";		

	/**
	 * Holds the string that indicated the remote file transfer
	 */
	public static final String FILE_TRANSFER_OPERATION = "FILE_TRANSFER";

	/**
	 * Holds the string that indicated the remote file transfer
	 */
	public static final String ARCHIVE_OPERATION = "ARCHIVE";	

	/**
	 * EU PDF File names and report file names
	 */
	public static String EU_PAPER_MAIL_FILE_NAME = new SimpleDateFormat("'PrintEU_'yyyyMMddhhmm").format(new Date());
	public static String EU_PAPER_MAIL_REPORT_FILE_NAME = new SimpleDateFormat("'PReport'yyyyMMddhhmm'.txt'").format(new Date());

	public static String EU_EMAIL_FILE_NAME =  new SimpleDateFormat("'EmailEU_'yyyyMMddhhmm").format(new Date());
	public static String EU_EMAIL_REPORT_FILE_NAME = new SimpleDateFormat("'EReport'yyyyMMddhhmm'.txt'").format(new Date());

	public static String EU_FAX_FILE_NAME = new SimpleDateFormat("'FaxEU_'yyyyMMddhhmm").format(new Date());
	public static String EU_FAX_REPORT_FILE_NAME = new SimpleDateFormat("'FReport'yyyyMMddhhmm'.txt'").format(new Date());	

	/**
	 * US PDF File names and report file names
	 */
	public static String US_PAPER_MAIL_FILE_NAME = "PPrint";
	public static String US_PAPER_MAIL_REPORT_FILE_NAME = "PReport.txt";

	public static  String US_EMAIL_FILE_NAME = "3M-MSDS_Email_";
	public static String US_EMAIL_REPORT_FILE_NAME = "EReport.txt";

	public static String US_FAX_FILE_NAME = "FaxUS_";
	public static String US_FAX_REPORT_FILE_NAME = "FReport.txt";
	
	/**
	 * String constant representing "Driver"
	 */
	public static final String DATABASE_DRIVER = "distribute.sds.driver";

	public static final String JOB_NAME_805 = "WCDMS805N";
	public static final String JOB_NAME_806 = "WCDMS806N";
	public static final String JOB_NAME_807 = "WCDMS807N";

	public static final String COUNTRY_GB = "GB";
	public static final String COUNTRY_FR = "FR";
	public static final String COUNTRY_IE = "IE";
	
	//Added country Iceland as a part of Issue# 170118-5786
	public static final String COUNTRY_IS = "IS";
	//Added country Canada as a part of Issue# 160106-F5FA
	public static final String COUNTRY_CA = "CA";
	
	public static final String COUNTRY_IT = "IT";
	public static final String REGION_GF = "GF";
	public static final String REGION_GP = "GP";
	public static final String REGION_MQ = "MQ";
	public static final String REGION_YT = "YT";
	public static final String REGION_RE = "RE";
	public static final String REGION_NC = "NC";
	public static final String REGION_PF = "PF";
	public static final String REGION_PM = "PM";

	//Added regions for Spain
	public static final String REGION_IC = "IC";
	public static final String REGION_AD = "AD";
	public static final String REGION_GI = "GI";

	public static final String SPACE_CONST = " ";
	public static final String ZIPCODE_NA = "NA";	

	public static final String MEDIA_TYPE_EMAIL = "EMAIL";
	public static final String MEDIA_TYPE_FAX = "FAX";
	public static final String MEDIA_TYPE_PAPER_MAIL = "PAPER MAIL";
	public static final String ERROR = "ERROR";

	public static final String VALUE_SEPARATOR = ":";
	public static final String ADDRESS_ID = "ADDRESS_ID";
	public static final String DOC_GROUP_NUM = "DOC_GROUP_NUM";
	public static final String ORDER_NUM = "ORDER_NUM";
	public static final String SALES_CUST_NBR = "SALES_CUSTOMER_NUM";
	public static final String AD_HOC_SENDER_NAME = "AD_HOC_SENDER_NAME";
	public static final String FILE_NAME_CONST = "File_Name";

	public static final String RETURN_EMAIL_STRING = "returnEmail_";
	public static final String SUPPORTDIR = "SUPPORTDIR";

	public static final String POSTAL = "POSTAL";
	public static final String NONPOSTAL = "NONPOSTAL";
	public static final String NON_POSTAL = "NON POSTAL";
	public static final String BLANK_FILE_NAME = "Blank.pdf";
	public static final String COVER_LETTER_FILE_NAME = "_Cover.pdf";
	public static final String SP_GET_KIT_DOCS="distribute.sds.proc.distribGetKitDocs";
	
	/**
	 * Cypress Separator page constants
	 * */
	public static final String SEPARATOR_PAGE_TITLE = "MSDS Separator Page";
	public static final String DECLARATION_LINE1 = "This page is only meant as a means to transfer below needed data to Cypress RDMs. ";
	public static final String DECLARATION_LINE2 = "This page is meant to be discarded, no matter if printed, faxed or emailed ";
	
	/**
	 * Cover letter date formats
	 */
	public static final String DATE_FORMAT_DEFAULT = "yy-MM-dd";
	public static final String DATE_FORMAT_01 = "dd/MM/yyyy";
	
	
	/**
	 * General text processors
	 */
	public static final String EMPTY_SPACE = " ";
	public static final String DASHED_COMMENT = "==================================";
}
