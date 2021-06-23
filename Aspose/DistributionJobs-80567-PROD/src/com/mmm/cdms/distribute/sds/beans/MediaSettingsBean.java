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
 *
 */
public class MediaSettingsBean {
	
	
	/**
	 * @return the fileNameEU
	 */
	public String getFileNameEU() {
		return fileNameEU;
	}

	
	/**
	 * @param fileNameEU the fileNameEU to set
	 */
	public void setFileNameEU(String fileNameEU) {
		this.fileNameEU = fileNameEU;
	}

	
	/**
	 * @return the fileNameUS
	 */
	public String getFileNameUS() {
		return fileNameUS;
	}

	
	/**
	 * @param fileNameUS the fileNameUS to set
	 */
	public void setFileNameUS(String fileNameUS) {
		this.fileNameUS = fileNameUS;
	}

	
	/**
	 * @return the reportFileNameEU
	 */
	public String getReportFileNameEU() {
		return reportFileNameEU;
	}

	
	/**
	 * @param reportFileNameEU the reportFileNameEU to set
	 */
	public void setReportFileNameEU(String reportFileNameEU) {
		this.reportFileNameEU = reportFileNameEU;
	}

	
	/**
	 * @return the reportFileNameUS
	 */
	public String getReportFileNameUS() {
		return reportFileNameUS;
	}

	
	/**
	 * @param reportFileNameUS the reportFileNameUS to set
	 */
	public void setReportFileNameUS(String reportFileNameUS) {
		this.reportFileNameUS = reportFileNameUS;
	}

	
	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}

	
	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	
	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}

	
	/**
	 * @param emailSubject the emailSubject to set
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	
	/**
	 * @return the reportDate
	 */
	public String getReportDate() {
		return reportDate;
	}

	
	/**
	 * @param reportDate the reportDate to set
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	/**
	 * fileNameEU
	 */
	private String fileNameEU;
	
	/**
	 * fileNameUS
	 */
	private String fileNameUS;
	
	/**
	 * reportFileName
	 */
	private String reportFileNameEU;
	
	/**
	 * reportFileName
	 */
	private String reportFileNameUS;
	
	/**
	 * mediaType
	 */
	private String mediaType;
	
	/**
	 * emailSubject
	 */
	private String emailSubject;

	/**
	 * reportDate
	 */
	private String reportDate;

}
