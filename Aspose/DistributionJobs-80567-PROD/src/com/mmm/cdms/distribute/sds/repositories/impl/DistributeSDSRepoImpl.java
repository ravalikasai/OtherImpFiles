/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.repositories.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.constants.PropertyFileConstants;
import com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;

/**
 * @author A5AK3ZZ
 *
 */
public class DistributeSDSRepoImpl implements DistributeSDSRepo {
	
	/**
	 * @param transferBean
	 */
	public DistributeSDSRepoImpl() {}
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(DistributeSDSRepoImpl.class);
	
	public static Map<String, String> returnEmailAddrMap = new HashMap<String, String>();
	
	/**
	 * LOAD_SYS_OPTION_PARAMS constant
	 */
	public final String LOAD_SYS_OPTION_PARAMS = "loadSysOptionParameters";
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#deleteDistributedRecords(java.sql.Connection, java.lang.String, boolean)
	 */
	public void deleteDistributedRecords(final String mediaType, final boolean hasNoData) {
		LOGGER.debug("BEGIN : DeleteDistributeRecords Method of DistributeSDSRepoImpl");
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				String deleteDistributionQueueforSent = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.DELETE_DQ_FOR_SENT_QUERY);
				if (!hasNoData) {
					LOGGER.debug("Data exists - processing data");
					String countofduplicatesRemoved = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.COUNT_DUPS_REMOVED_QUERY);
					PreparedStatement st = connection.prepareStatement(countofduplicatesRemoved);
					ResultSet rs = st.executeQuery();
					if (rs.next()) {
						DistributeSDSApplication.pdfDetailsDTO.setNoOfDuplicatesRemoved(rs.getInt(1));
					}
					if (mediaType.equals(DistributeSDSConstants.MEDIA_TYPE_EMAIL) || mediaType.equals(DistributeSDSConstants.MEDIA_TYPE_PAPER_MAIL)) {
						updateDuplicatesIntoRBDS();
					}
					String updateDistributionQueueforSent = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.UPDATE_DQ_QUERY);
					st = connection.prepareStatement(updateDistributionQueueforSent);
					st.executeUpdate();
					st = connection.prepareStatement(deleteDistributionQueueforSent);
					st.executeUpdate();
				} else {
					LOGGER.debug("No data exists. Deleting existing data if any");
					PreparedStatement st = connection.prepareStatement(deleteDistributionQueueforSent);
					st.executeUpdate();
				}
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			LOGGER.error("DistributionException occured in deleteDistributedRecords()", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("DistributionException occured in deleteDistributedRecords() while RollBack.", e);
			}
		}
		LOGGER.debug("END : DeleteDistributeRecords Method of DistributeSDSRepoImpl");
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#updateDuplicatesIntoRBDS(java.sql.Connection)
	 */
	public void updateDuplicatesIntoRBDS() {
		LOGGER.debug("updateDuplicatesIntoRBDS Method of DistributeSDSRepoImpl");
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				String getDuplicatesList = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.GET_DUPLICATE_QUERY);
				PreparedStatement st = connection.prepareStatement(getDuplicatesList);
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					String addressID = "" + rs.getLong(DistributeSDSConstants.ADDRESS_ID);
					String docGrpNum = rs.getString(DistributeSDSConstants.DOC_GROUP_NUM);
					String orderNum = rs.getString(DistributeSDSConstants.ORDER_NUM);
					String adhocSender = rs.getString(DistributeSDSConstants.AD_HOC_SENDER_NAME);
					if ((adhocSender == null) || (adhocSender.trim() == "")) {
						String updateRulesBasedDocSum = MessageFormat.format(
								DistributeSDSUtils.getPropertyValue(PropertyFileConstants.UPDATE_RDS_QUERY),
								addressID, docGrpNum, orderNum);
						st = connection.prepareStatement(updateRulesBasedDocSum);
						st.executeUpdate();
					}
				}
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			LOGGER.error("DistributionException occured in updateDuplicatesIntoRBDS.", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("DistributionException occured in updateDuplicatesIntoRBDS method while rollback", e);
			}
		}
		LOGGER.debug("updateDuplicatesIntoRBDS Method of DistributeSDSRepoImpl");
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#dropTempTable(java.sql.Connection)
	 */
	public void dropTempTable() {
		LOGGER.debug("BEGIN : dropTempTable Method of DistributeSDSRepoImpl");
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				String deleteDistrQueueCounterEUTempTable = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.DROP_DQ_TEMP_QUERY);
				PreparedStatement st = connection.prepareStatement(deleteDistrQueueCounterEUTempTable);
				st.execute();
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			LOGGER.error("DistributionException occured in dropTempTable.", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("DistributionException occured in dropTempTable method while RollBack", e);
			}
		}
		LOGGER.debug("END : dropTempTable Method of DistributeSDSRepoImpl");
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#executeSP(java.sql.Connection, java.lang.String)
	 */
	public void executeSP(final String procName) {
		LOGGER.debug("BEGIN : executeSP Method of DistributeSDSRepoImpl ***********" + procName);
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				CallableStatement cs = connection.prepareCall(procName);
				cs.execute();
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			LOGGER.error("DistributionException occured in executeSP.", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("DistributionException occured in executeSP method while RollBack", e);
			}
		}
		LOGGER.debug("END : executeSP Method of DistributeSDSRepoImpl ***********" + procName);
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#createDQCounterTempTable(com.mmm.cdms.
	 * distribute.sds.beans.PdfDetailsDTO, java.sql.Connection)
	 */
	public void createDQCounterTempTable() {		
		LOGGER.debug("BEGIN : createTempTableDQCounter()");
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				String createDQCounterTempTable = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.CREATE_DQ_TEMP_QUERY);
				PreparedStatement st = connection.prepareStatement(createDQCounterTempTable);
				st.execute();		
				DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.PROCESS_STARTED);
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			LOGGER.error("DistributionException occured in createDQCounterTempTable.", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("DistributionException occured in createDQCounterTempTable while Rollback", e);
			}
		}
		LOGGER.debug("END : createTempTableDQCounter()");
		LOGGER.info("Creation of DQ temp table complete!");
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#loadSysOptionParameters(
	 * com.mmm.cdms.distribute.sds.beans.PdfDetailsDTO)
	 */
	public SysOptionBean loadSysOptionParams() {
		LOGGER.debug("BEGIN : loadSysOptionParameters method");
		SysOptionBean sysOption = new SysOptionBean();
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				String loadSysOption = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.GET_SYS_OPTION_QUERY);
				PreparedStatement prepStmt = connection.prepareStatement(loadSysOption);
				if (StringUtils.isNotBlank(loadSysOption)) {
					ResultSet sysOptionRS = prepStmt.executeQuery();
					String optionName;
					String stringValue;
					while (sysOptionRS.next()) {
						optionName = sysOptionRS.getString("option_name");
						stringValue = sysOptionRS.getString("string_value");
						if ("TEMP_DIR".equals(optionName)) {
							sysOption.setHomePath(stringValue);
						} else if ("DUPLEX".equals(optionName)) {
							if ("TRUE".equalsIgnoreCase(stringValue)) {
								sysOption.setDuplex(true);
							} else {
								sysOption.setDuplex(false);
							}
						} else if ("FAX_DEVICE".equals(optionName)) {
							sysOption.setFaxQueue(stringValue);
						} else if ("SMTP_ADDRESS".equals(optionName)) {
							sysOption.setMailServerAddress(stringValue);
						} else if ("SYSTEM_EMAIL_ADDR".equals(optionName)) {
							sysOption.setFromEmailAddress(stringValue);
						} else if ("SYSTEM_MESSAGES".equals(optionName)) {
							sysOption.setMessageLog(stringValue);
						} else if ("FROM_COMPANY".equals(optionName)) {
							sysOption.setCorpName(stringValue);
						} else if ("FAX_EMAIL_NOTIFY".equals(optionName)) {
							if ("TRUE".equalsIgnoreCase(stringValue)) {
								sysOption.setHasFaxEmailNotify(true);
							} else {
								sysOption.setHasFaxEmailNotify(false);
							}
						} else if ("FAX_RETURN_NUMBER".equals(optionName)) {
							sysOption.setFaxReturnNumber(stringValue);
						} else if ("CYPRESS_PRINTER_FTP_DIR".equals(optionName)) {
							sysOption.setCypressPrinterFtpDir(stringValue);
						} else if ("CYPRESS_PRINTER_FTP_USERID".equals(optionName)) {
							sysOption.setCypressPrinterFtpUserid(stringValue);
						} else if ("CYPRESS_PRINTER_FTP_PWD".equals(optionName)) {
							sysOption.setCypressPrinterFtpPwd(stringValue);
						} else if ("CYPRESS_PRINTER_FTP_ADDR".equals(optionName)) {
							sysOption.setCypressPrinterFtpAddr(stringValue);
						} else if ("PRINTER_FTP_DIR".equals(optionName)) {
							sysOption.setPrinterFtpDir(stringValue);
						} else if ("PRINTER_FTP_ARCHIVE".equals(optionName)) {
							sysOption.setPrinterFtpArchive(stringValue);
						} else if ("PRINTER_FTP_USERID".equals(optionName)) {
							sysOption.setPrinterFtpUserid(stringValue);
						} else if ("PRINTER_FTP_PWD".equals(optionName)) {
							sysOption.setPrinterFtpPwd(stringValue);
						} else if ("PRINTER_FTP_ADDR".equals(optionName)) {
							sysOption.setPrinterFtpAddr(stringValue);
						} else if ("PRINTER_FTP_DEST_DIR".equals(optionName)) {
							sysOption.setPrinterFtpDestDir(stringValue);
						} else if ("DISTRIBUTION_SAP_APP_SOURCE".equals(optionName)) {
							sysOption.setSupportDir(stringValue);
						} else if ("DISTRIBUTION_LOG_FILE".equals(optionName)) {
							sysOption.setLogPath(stringValue);
						} else if ("DISTRIBUTION_DATA_SOURCE".equals(optionName)) {
							sysOption.setDataPath(stringValue);
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.error("DistributionException occured in loadSysOptionParameters. Last loadSysOptionParameters failed", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("DistributionException occured in loadSysOptionParameters while Rollback", e);
			}
		}
		LOGGER.debug("END : loadSysOptionParameters method");
		DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.SYS_OPTION_LOADED);
		LOGGER.info("Loading SYS_OPTION table data complete!");
		return sysOption;
	}
			
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo#logEvents(java.lang.String, java.lang.String, 
	 * int, java.lang.String)
	 */
	public void logEvents(final String msgShort, final String msgLong, final int statusCode, final String jobName) {
		LOGGER.debug("logger EVENTS  ***********" + msgShort + " "  + msgLong + " " + statusCode);
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				String insertSQL = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.EVENT_LOG_QUERY);
				String logEvent = MessageFormat.format(insertSQL, msgShort, msgLong, jobName, statusCode);
				PreparedStatement sqlStatement = connection.prepareStatement(logEvent);
				sqlStatement.executeUpdate();
				connection.setAutoCommit(true);
			}
		} catch (SQLException sqlEx) {
			LOGGER.error("ERROR OCCURED IN EVENT LOGGING:", sqlEx);
			LOGGER.debug("Message to be logged to event logger:" + msgLong);
			try {
				connection.rollback();
			} catch (SQLException e) {
				LOGGER.error("ERROR OCCURED IN EVENT LOGGING while ROLLBACK", e);
			}
		}
		LOGGER.debug("logger EVENTS  ***********" + msgShort + " "  + msgLong + " " + statusCode);
	}
}
