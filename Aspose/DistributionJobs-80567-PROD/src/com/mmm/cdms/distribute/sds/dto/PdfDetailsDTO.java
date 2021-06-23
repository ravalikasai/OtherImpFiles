/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;

/**
 * @author a5ak3zz
 * @version 1.0 01-Aug-2018
 */
public class PdfDetailsDTO {
	
	private int pages;
	private int dupRemovedCount;
	private int pkgCount;
	private int pageCount;
	private int deletedDocsCount;
	private int archivedDocsCount;
	private int batchPackageCount;
	private int batchBlankPageCount;
	private int batchPageCount;
	private int dupsRemoved;
	
	private long endTime;
	
	private String ftpMessage;
	private String processStatus;
	private String reportErrorMessage;
	private String startTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	
	private boolean isArchiveSuccess;
	private boolean isClearDataFolderSuccess;
	private boolean isDeleteSuccess;
	private boolean isMountSuccess;
	private boolean isDriveConnectSuccess;
	
	private Map<String, List<String>> transferMap = new HashMap<String, List<String>>();
	private List<String> emailList = new ArrayList<String>();
	private List<String> faxList = new ArrayList<String>();
	private List<String> paperMailList = new ArrayList<String>();
	private List<String> errorList = new ArrayList<String>();
	
	/**
	 * Constructor to set the transferMap
	 */
	public PdfDetailsDTO() {
		transferMap.put(DistributeSDSConstants.MEDIA_TYPE_EMAIL, emailList);
		transferMap.put(DistributeSDSConstants.MEDIA_TYPE_FAX, faxList);
		transferMap.put(DistributeSDSConstants.MEDIA_TYPE_PAPER_MAIL, paperMailList);
		transferMap.put(DistributeSDSConstants.ERROR, errorList);
	}
	
	/**
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}
	
	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}
	
	/**
	 * @return the dupRemovedCount
	 */
	public int getNoOfDuplicatesRemoved() {
		return dupRemovedCount;
	}
	
	/**
	 * @param dupRemovedCount
	 *            the dupRemovedCount to set
	 */
	public void setNoOfDuplicatesRemoved(int noOfDuplicatesRemoved) {
		this.dupRemovedCount = noOfDuplicatesRemoved;
	}
	
	/**
	 * @return the pkgCount
	 */
	public int getNoOfPackages() {
		return pkgCount;
	}
	
	/**
	 * @param pkgCount
	 *            the pkgCount to set
	 */
	public void setNoOfPackages(int noOfPackages) {
		this.pkgCount = noOfPackages;
	}
	
	/**
	 * @return the pageCount
	 */
	public int getNoOfPages() {
		return pageCount;
	}
	
	/**
	 * @param pageCount
	 *            the pageCount to set
	 */
	public void setNoOfPages(int noOfPages) {
		this.pageCount = noOfPages;
	}
	
	/**
	 * @return the deletedDocsCount
	 */
	public int getDeletedDocsCount() {
		return deletedDocsCount;
	}
	
	/**
	 * @param deletedDocsCount
	 *            the deletedDocsCount to set
	 */
	public void setDeletedDocsCount(int deletedDocsCount) {
		this.deletedDocsCount = deletedDocsCount;
	}
	
	/**
	 * @return the archivedDocsCount
	 */
	public int getArchivedDocsCount() {
		return archivedDocsCount;
	}
	
	/**
	 * @param archivedDocsCount
	 *            the archivedDocsCount to set
	 */
	public void setArchivedDocsCount(int archivedDocsCount) {
		this.archivedDocsCount = archivedDocsCount;
	}
	
	/**
	 * @return the batchPackageCount
	 */
	public int getBatchPackageCount() {
		return batchPackageCount;
	}
	
	/**
	 * @param batchPackageCount
	 *            the batchPackageCount to set
	 */
	public void setBatchPackageCount(int batchPackageCount) {
		this.batchPackageCount = batchPackageCount;
	}
	
	/**
	 * @return the batchBlankPageCount
	 */
	public int getBatchBlankPageCount() {
		return batchBlankPageCount;
	}
	
	/**
	 * @param batchBlankPageCount
	 *            the batchBlankPageCount to set
	 */
	public void setBatchBlankPageCount(int batchBlankPageCount) {
		this.batchBlankPageCount = batchBlankPageCount;
	}
	
	/**
	 * @return the batchPageCount
	 */
	public int getBatchPageCount() {
		return batchPageCount;
	}
	
	/**
	 * @param batchPageCount
	 *            the batchPageCount to set
	 */
	public void setBatchPageCount(int batchPageCount) {
		this.batchPageCount = batchPageCount;
	}
	
	/**
	 * @return the dupsRemoved
	 */
	public int getDupsRemoved() {
		return dupsRemoved;
	}
	
	/**
	 * @param dupsRemoved
	 *            the dupsRemoved to set
	 */
	public void setDupsRemoved(int dupsRemoved) {
		this.dupsRemoved = dupsRemoved;
	}
	
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	
	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @return the ftpMessage
	 */
	public String getFtpMessage() {
		return ftpMessage;
	}
	
	/**
	 * @param ftpMessage
	 *            the ftpMessage to set
	 */
	public void setFtpMessage(String ftpMessage) {
		this.ftpMessage = ftpMessage;
	}
	
	/**
	 * @return the processStatus
	 */
	public String getProcessStatus() {
		return processStatus;
	}
	
	/**
	 * @param processStatus
	 *            the processStatus to set
	 */
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	
	/**
	 * @return the reportErrorMessage
	 */
	public String getReportErrorMessage() {
		return reportErrorMessage;
	}
	
	/**
	 * @param reportErrorMessage
	 *            the reportErrorMessage to set
	 */
	public void setReportErrorMessage(String reportErrorMessage) {
		this.reportErrorMessage = reportErrorMessage;
	}
	
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	
	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * @return the isArchiveSuccess
	 */
	public boolean isArchiveSuccess() {
		return isArchiveSuccess;
	}
	
	/**
	 * @param isArchiveSuccess
	 *            the isArchiveSuccess to set
	 */
	public void setArchiveSuccess(boolean isArchiveSuccess) {
		this.isArchiveSuccess = isArchiveSuccess;
	}
	
	/**
	 * @return the isClearDataFolderSuccess
	 */
	public boolean isClearDataFolderSuccess() {
		return isClearDataFolderSuccess;
	}
	
	/**
	 * @param isClearDataFolderSuccess
	 *            the isClearDataFolderSuccess to set
	 */
	public void setClearDataFolderSuccess(boolean isClearDataFolderSuccess) {
		this.isClearDataFolderSuccess = isClearDataFolderSuccess;
	}
	
	/**
	 * @return the isDeleteSuccess
	 */
	public boolean isDeleteSuccess() {
		return isDeleteSuccess;
	}
	
	/**
	 * @param isDeleteSuccess
	 *            the isDeleteSuccess to set
	 */
	public void setDeleteSuccess(boolean isDeleteSuccess) {
		this.isDeleteSuccess = isDeleteSuccess;
	}
	
	/**
	 * @return the isMountSuccess
	 */
	public boolean isMountSuccess() {
		return isMountSuccess;
	}
	
	/**
	 * @param isMountSuccess
	 *            the isMountSuccess to set
	 */
	public void setMountSuccess(boolean isMountSuccess) {
		this.isMountSuccess = isMountSuccess;
	}
	
	/**
	 * @return the isDriveConnectSuccess
	 */
	public boolean isDriveConnectSuccess() {
		return isDriveConnectSuccess;
	}
	
	/**
	 * @param isDriveConnectSuccess
	 *            the isDriveConnectSuccess to set
	 */
	public void setDriveConnectSuccess(boolean isDriveConnectSuccess) {
		this.isDriveConnectSuccess = isDriveConnectSuccess;
	}
	
	/**
	 * @return the transferMap
	 */
	public Map<String, List<String>> getTransferMap() {
		return transferMap;
	}
	
	/**
	 * @param transferMap
	 *            the transferMap to set
	 */
	public void setTransferMap(Map<String, List<String>> transferMap) {
		this.transferMap = transferMap;
	}
	
	/**
	 * @return the emailList
	 */
	public List<String> getEmailList() {
		return emailList;
	}
	
	/**
	 * @param emailList
	 *            the emailList to set
	 */
	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}
	
	/**
	 * @return the faxList
	 */
	public List<String> getFaxList() {
		return faxList;
	}
	
	/**
	 * @param faxList
	 *            the faxList to set
	 */
	public void setFaxList(List<String> faxList) {
		this.faxList = faxList;
	}
	
	/**
	 * @return the paperMailList
	 */
	public List<String> getPaperMailList() {
		return paperMailList;
	}
	
	/**
	 * @param paperMailList
	 *            the paperMailList to set
	 */
	public void setPaperMailList(List<String> paperMailList) {
		this.paperMailList = paperMailList;
	}
	
	/**
	 * @return the errorList
	 */
	public List<String> getErrorList() {
		return errorList;
	}
	
	/**
	 * @param errorList
	 *            the errorList to set
	 */
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
	
}
