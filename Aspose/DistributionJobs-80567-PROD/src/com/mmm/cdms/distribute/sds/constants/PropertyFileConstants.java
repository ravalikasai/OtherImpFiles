/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.constants;

/**
 * This class is used for defining constants for 
 * the property file entries
 * 
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 */
public class PropertyFileConstants {
	
	/**********************************************************
	 * Constants for the properties file entries
	 *********************************************************/
	
	public static final String COUNT_DUPS_REMOVED_QUERY = "distribute.sds.query.countOfDuplicatesRemoved";
	public static final String CREATE_DQ_TEMP_QUERY = "distribute.sds.query.createTempTableDQCounter";
	public static final String DELETE_DQ_FOR_SENT_QUERY = "distribute.sds.query.deleteDQforSent";
	public static final String DOC_IMAGE_QUERY = "distribute.sds.query.documentImage";
	public static final String DROP_DQ_TEMP_QUERY = "distribute.sds.query.dropTempTableDQCounter";
	public static final String GET_COMPONENT_ID_QUERY = "distribute.sds.query.getComponentIDs";
	public static final String GET_CURRENT_TMSP_QUERY = "distribute.sds.query.getCurrentTmspSql";
	public static final String GET_CVR_LETTER_IMAGE_QRY = "distribute.sds.query.getCvrLetterImage";
	public static final String GET_US_CVR_LETTER_IMAGE_QRY = "distribute.sds.query.getUSCvrLetterImage";
	public static final String GET_RETURN_EMAIL_ADDRESSES_QRY = "distribute.sds.query.getReturnEmailAddresses";
	public static final String GET_DISTINCT_ORDER_QUERY = "distribute.sds.query.getDistinctOrderPerCustomer";
	public static final String GET_DOC_DETAILS_QUERY = "distribute.sds.query.getDocDetails";
	public static final String GET_DUPLICATE_QUERY = "distribute.sds.query.getDuplicateRecords";
	public static final String GET_PKG_ID_QUERY = "distribute.sds.query.getPackageID";
	public static final String GET_RETURN_EMAIL_ADDRESSES = "distribute.sds.query.getReturnEmailAddr";
	
	public static final String INSERT_TEMP_TABLE_QUERY = "distribute.sds.query.insertDQCounterTempTable";
	
	public static final String EVENT_LOG_QUERY = "distribute.sds.query.logevents";
	public static final String GET_BATCH_JOBS_STATUS = "distribute.sds.query.jobs.status";
	
	public static final String GET_SYS_OPTION_QUERY = "distribute.sds.query.getSysOptionParams";
	
	public static final String UPDATE_DQ_QUERY = "distribute.sds.query.updateDQueueforsent";
	public static final String UPDATE_RDS_QUERY = "distribute.sds.query.updateRDSRecords";
	
	public static final String SP_GET_PRIMARY_DOC = "distribute.sds.proc.distribGetPrimaryDoc";
	public static final String SP_GET_KIT_DOCS = "distribute.sds.proc.distribGetKitDocs";
	public static final String SP_GET_KIT_COMPONENT_ID = "distribute.sds.proc.getkitcomponentid";
	public static final String SP_GET_DISTRIB_PKG_DETAILS = "distribute.sds.proc.getDistribPackageDetails";
	public static final String SP_UPDATE_RBDS = "distribute.sds.proc.updateRulesBasedDocSum";
	public static final String SP_UPDATE_DISTR_LOG = "distribute.sds.proc.updateDistributionLog";
	public static final String SP_REMOVE_EMAIL_DUPES = "distribute.sds.proc.removeEmailDups";
	public static final String SP_REMOVE_PAPER_DUPES = "distribute.sds.proc.removePaperDups";
	
	public static final String JOB_NAME = "distribute.sds.jobName";
	public static final String SMTP_ADDRESS = "distribute.sds.smtpaddress";
	public static final String SYSTEM_EMAIL_ADDRESS = "distribute.sds.systememailaddr";
	public static final String DRIVE_MOUNT = "distribute.sds.mountdrive";
	public static final String MEDIA_TYPE = "distribute.sds.nMediaType";
	public static final String EUROMS_COUNTRIES = "distribute.sds.euromsCountry";
	public static final String UNICODE_COUNTRIES = "distribute.sds.unicodeCountries";
	public static final String COUNTRY_NAMES_IN_ADDRESS = "distribute.sds.addCountryName.list";
	
	public static final String ADDITIONAL_RECIPIENTS = "distribute.sds.additional.recipients.email";
	
	public static final String CERTIFICATE_CONST = "distribute.sds.cert";
	public static final String SERVER_LOCATION_FOR_FILES = "distribute.sds.ftp.destination";
	public static final String FTP_LOG_LEVEL = "distribute.sds.ftp.loglevel";
	
}
