/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.beans;

/**
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 */
public class SysOptionBean {
	
	/**************************************************************************************
	 * Class Variables
	 *************************************************************************************/

	/**
	 * homePath
	 */
	private String homePath;
	/**
	 * dataPath
	 */
	private String dataPath;
	
	/**
	 * logPath
	 */
	private String logPath;

	/**
	 * faxQueue
	 */
	private String faxQueue;
	/**
	 * mailServerAddress
	 */
	private String mailServerAddress;
	/**
	 * fromEmailAddress
	 */
	private String dateTime;
	/**
	 * fromEmailAddress
	 */
	private String fromEmailAddress;
	/**
	 * supportDir
	 */
	private String supportDir;

	/**
	 * corpName
	 */
	private String corpName;
	/**
	 * faxReturnNumber
	 */
	private String faxReturnNumber;
	
	/**
	 * messageLog
	 */
	private String messageLog;
	/**
	 * cypressPrinterFtpDir
	 */
	private String cypressPrinterFtpDir;
	/**
	 * cypressPrinterFtpAddr
	 */
	private String cypressPrinterFtpAddr;

	/**
	 * cypressPrinterFtpUserid
	 */
	private String cypressPrinterFtpUserid;
	/**
	 * cypressPrinterFtpPwd
	 */
	private String cypressPrinterFtpPwd;
	
	/**
	 * isDuplex
	 */
	private boolean isDuplex;
	/**
	 * hasFaxEmailNotify
	 */
	private boolean hasFaxEmailNotify;
	
	/**
	 * printerFtpDir
	 */
	private String printerFtpDir;
	/**
	 * printerFtpArchive
	 */
	private String printerFtpArchive;
	/**
	 * printerFtpAddr
	 */
	private String printerFtpAddr;
	/**
	 * printerFtpDestDir
	 */
	private String printerFtpDestDir;
	/**
	 * printerFtpUserid
	 */
	private String printerFtpUserid;
	/**
	 * printerFtpPwd
	 */
	private String printerFtpPwd;
	/**
	 * supportDirLoc
	 */
	private String supportDirLoc;

	
	/**************************************************************************************
	 * Getters & Setters
	 *************************************************************************************/
	
	/**
	 * @return the homePath
	 */
	public String getHomePath() {
		return homePath;
	}

	/**
	 * @param homePath the homePath to set
	 */
	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	/**
	 * @return the dataPath
	 */
	public String getDataPath() {
		return dataPath;
	}

	/**
	 * @param dataPath the dataPath to set
	 */
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	
	/**
	 * @return the logPath
	 */
	public String getLogPath() {
		return logPath;
	}

	
	/**
	 * @param logPath the logPath to set
	 */
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	/**
	 * @return the isDuplex
	 */
	public boolean isDuplex() {
		return isDuplex;
	}

	/**
	 * @param isDuplex the isDuplex to set
	 */
	public void setDuplex(boolean isDuplex) {
		this.isDuplex = isDuplex;
	}

	/**
	 * @return the faxQueue
	 */
	public String getFaxQueue() {
		return faxQueue;
	}

	/**
	 * @param faxQueue the faxQueue to set
	 */
	public void setFaxQueue(String faxQueue) {
		this.faxQueue = faxQueue;
	}

	/**
	 * @return the mailServerAddress
	 */
	public String getMailServerAddress() {
		return mailServerAddress;
	}

	/**
	 * @param mailServerAddress the mailServerAddress to set
	 */
	public void setMailServerAddress(String mailServerAddress) {
		this.mailServerAddress = mailServerAddress;
	}

	
	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	
	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the fromEmailAddress
	 */
	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	/**
	 * @param fromEmailAddress the fromEmailAddress to set
	 */
	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	/**
	 * @return the supportDir
	 */
	public String getSupportDir() {
		return supportDir;
	}

	/**
	 * @param supportDir the supportDir to set
	 */
	public void setSupportDir(String supportDir) {
		this.supportDir = supportDir;
	}

	/**
	 * @return the corpName
	 */
	public String getCorpName() {
		return corpName;
	}

	/**
	 * @param corpName the corpName to set
	 */
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	/**
	 * @return the hasFaxEmailNotify
	 */
	public boolean hasFaxEmailNotify() {
		return hasFaxEmailNotify;
	}

	/**
	 * @param hasFaxEmailNotify the hasFaxEmailNotify to set
	 */
	public void setHasFaxEmailNotify(boolean hasFaxEmailNotify) {
		this.hasFaxEmailNotify = hasFaxEmailNotify;
	}

	/**
	 * @return the faxReturnNumber
	 */
	public String getFaxReturnNumber() {
		return faxReturnNumber;
	}

	/**
	 * @param faxReturnNumber the faxReturnNumber to set
	 */
	public void setFaxReturnNumber(String faxReturnNumber) {
		this.faxReturnNumber = faxReturnNumber;
	}

	/**
	 * @return the messageLog
	 */
	public String getMessageLog() {
		return messageLog;
	}

	/**
	 * @param messageLog the messageLog to set
	 */
	public void setMessageLog(String messageLog) {
		this.messageLog = messageLog;
	}

	/**
	 * @return the cypressPrinterFtpDir
	 */
	public String getCypressPrinterFtpDir() {
		return cypressPrinterFtpDir;
	}

	/**
	 * @param cypressPrinterFtpDir the cypressPrinterFtpDir to set
	 */
	public void setCypressPrinterFtpDir(String cypressPrinterFtpDir) {
		this.cypressPrinterFtpDir = cypressPrinterFtpDir;
	}

	/**
	 * @return the cypressPrinterFtpAddr
	 */
	public String getCypressPrinterFtpAddr() {
		return cypressPrinterFtpAddr;
	}

	/**
	 * @param cypressPrinterFtpAddr the cypressPrinterFtpAddr to set
	 */
	public void setCypressPrinterFtpAddr(String cypressPrinterFtpAddr) {
		this.cypressPrinterFtpAddr = cypressPrinterFtpAddr;
	}

	/**
	 * @return the cypressPrinterFtpUserid
	 */
	public String getCypressPrinterFtpUserid() {
		return cypressPrinterFtpUserid;
	}

	/**
	 * @param cypressPrinterFtpUserid the cypressPrinterFtpUserid to set
	 */
	public void setCypressPrinterFtpUserid(String cypressPrinterFtpUserid) {
		this.cypressPrinterFtpUserid = cypressPrinterFtpUserid;
	}

	/**
	 * @return the cypressPrinterFtpPwd
	 */
	public String getCypressPrinterFtpPwd() {
		return cypressPrinterFtpPwd;
	}

	/**
	 * @param cypressPrinterFtpPwd the cypressPrinterFtpPwd to set
	 */
	public void setCypressPrinterFtpPwd(String cypressPrinterFtpPwd) {
		this.cypressPrinterFtpPwd = cypressPrinterFtpPwd;
	}

	/**
	 * @return the printerFtpDir
	 */
	public String getPrinterFtpDir() {
		return printerFtpDir;
	}

	/**
	 * @param printerFtpDir the printerFtpDir to set
	 */
	public void setPrinterFtpDir(String printerFtpDir) {
		this.printerFtpDir = printerFtpDir;
	}

	/**
	 * @return the printerFtpArchive
	 */
	public String getPrinterFtpArchive() {
		return printerFtpArchive;
	}

	/**
	 * @param printerFtpArchive the printerFtpArchive to set
	 */
	public void setPrinterFtpArchive(String printerFtpArchive) {
		this.printerFtpArchive = printerFtpArchive;
	}

	/**
	 * @return the printerFtpAddr
	 */
	public String getPrinterFtpAddr() {
		return printerFtpAddr;
	}

	/**
	 * @param printerFtpAddr the printerFtpAddr to set
	 */
	public void setPrinterFtpAddr(String printerFtpAddr) {
		this.printerFtpAddr = printerFtpAddr;
	}

	
	/**
	 * @return the printerFtpDestDir
	 */
	public String getPrinterFtpDestDir() {
		return printerFtpDestDir;
	}

	
	/**
	 * @param printerFtpDestDir the printerFtpDestDir to set
	 */
	public void setPrinterFtpDestDir(String printerFtpDestDir) {
		this.printerFtpDestDir = printerFtpDestDir;
	}

	/**
	 * @return the printerFtpUserid
	 */
	public String getPrinterFtpUserid() {
		return printerFtpUserid;
	}

	/**
	 * @param printerFtpUserid the printerFtpUserid to set
	 */
	public void setPrinterFtpUserid(String printerFtpUserid) {
		this.printerFtpUserid = printerFtpUserid;
	}

	/**
	 * @return the printerFtpPwd
	 */
	public String getPrinterFtpPwd() {
		return printerFtpPwd;
	}

	/**
	 * @param printerFtpPwd the printerFtpPwd to set
	 */
	public void setPrinterFtpPwd(String printerFtpPwd) {
		this.printerFtpPwd = printerFtpPwd;
	}

	/**
	 * @return the supportDirLoc
	 */
	public String getSupportDirLoc() {
		return supportDirLoc;
	}

	/**
	 * @param supportDirLoc the supportDirLoc to set
	 */
	public void setSupportDirLoc(String supportDirLoc) {
		this.supportDirLoc = supportDirLoc;
	}		
}
