/**

 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.beans;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * Holds the data for row in Distribution_Queue table
 * 
 * 
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 */

public class DistributionQueueBean {

	private String accountNum;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String addressId;
	private String adhocSenderName;
	private String adhocSenderPhone;
	private String attnName;
	private String cdmsLangCode;
	private String city;
	private String companyName;
	private String countryCode;
	private String countryName;
	private int coveraddressFontsize;
	private int coveraddressOffsetX;
	private int coveraddressOffsetY;
	private int coverinfoboxFontsize;
	private int coverInfoOffsetX;
	private int coverInfoOffsetY;
	private InputStream coverLetterImage;
	private String coverLetterTemplate;
	private String dateTransText;
	private String deptNum;
	private Timestamp distributionDate;
	private String divisionCode;
	private Timestamp docGenDate;
	private String docGroupNum;
	private String docGrpNumTransText;
	private String docIndexId;
	private String docTradeName;
	private String docTypeCode;
	private String ediPassword;
	private String ediUserId;
	private String ediVan;
	private String emailAddress;
	private String eventId;
	private String faxNumber;
	private String ftpDirectory;
	private String ftpPassword;
	private String ftpSite;
	private String ftpUserId;
	private String manualFlag;
	private String mediaAddress;
	private String mediaTypeCode;
	private int numCopies;
	private String opcoCode;
	private String orderNum;
	private String orderNumTransText;
	private String orderPlacer;
	private String orderPlacerPhone;
	private int packageBlankPageCount;
	private String packageDelete;
	private String packageID;
	private int packagePageCount;
	private String printSendMethod;
	private String regAgencyCode;
	private BigDecimal revisionNumber;
	private String salesCustomerNum;
	private String salesSourceCode;
	private String sapCountryCode;
	private String sapLanguageCode;
	private String sendUpdate;
	private Timestamp shipDate;
	private String stateCode;
	private Timestamp statusDate;
	private String templateFormatCode;
	private String templateID;
	private Timestamp tmspLastUpdt;
    private String userLastUpdt;
    private String zipCode;
	
	/**
	 * @return the accountNum
	 */
	public String getAccountNum() {
		return accountNum;
	}
	
	/**
	 * @param accountNum the accountNum to set
	 */
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}
	
	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	
	/**
	 * @return the address4
	 */
	public String getAddress4() {
		return address4;
	}
	
	/**
	 * @param address4 the address4 to set
	 */
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	
	/**
	 * @return the addressId
	 */
	public String getAddressId() {
		return addressId;
	}
	
	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	
	/**
	 * @return the adhocSenderName
	 */
	public String getAdhocSenderName() {
		return adhocSenderName;
	}
	
	/**
	 * @param adhocSenderName the adhocSenderName to set
	 */
	public void setAdhocSenderName(String adhocSenderName) {
		this.adhocSenderName = adhocSenderName;
	}
	
	/**
	 * @return the adhocSenderPhone
	 */
	public String getAdhocSenderPhone() {
		return adhocSenderPhone;
	}
	
	/**
	 * @param adhocSenderPhone the adhocSenderPhone to set
	 */
	public void setAdhocSenderPhone(String adhocSenderPhone) {
		this.adhocSenderPhone = adhocSenderPhone;
	}
	
	/**
	 * @return the attnName
	 */
	public String getAttnName() {
		return attnName;
	}
	
	/**
	 * @param attnName the attnName to set
	 */
	public void setAttnName(String attnName) {
		this.attnName = attnName;
	}
	
	/**
	 * @return the cdmsLangCode
	 */
	public String getCdmsLangCode() {
		return cdmsLangCode;
	}
	
	/**
	 * @param cdmsLangCode the cdmsLangCode to set
	 */
	public void setCdmsLangCode(String cdmsLangCode) {
		this.cdmsLangCode = cdmsLangCode;
	}
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}
	
	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	/**
	 * @return the coveraddressFontsize
	 */
	public int getCoveraddressFontsize() {
		return coveraddressFontsize;
	}
	
	/**
	 * @param coveraddressFontsize the coveraddressFontsize to set
	 */
	public void setCoveraddressFontsize(int coveraddressFontsize) {
		this.coveraddressFontsize = coveraddressFontsize;
	}
	
	/**
	 * @return the coveraddressOffsetX
	 */
	public int getCoveraddressOffsetX() {
		return coveraddressOffsetX;
	}
	
	/**
	 * @param coveraddressOffsetX the coveraddressOffsetX to set
	 */
	public void setCoveraddressOffsetX(int coveraddressOffsetX) {
		this.coveraddressOffsetX = coveraddressOffsetX;
	}
	
	/**
	 * @return the coveraddressOffsetY
	 */
	public int getCoveraddressOffsetY() {
		return coveraddressOffsetY;
	}
	
	/**
	 * @param coveraddressOffsetY the coveraddressOffsetY to set
	 */
	public void setCoveraddressOffsetY(int coveraddressOffsetY) {
		this.coveraddressOffsetY = coveraddressOffsetY;
	}
	
	/**
	 * @return the coverinfoboxFontsize
	 */
	public int getCoverinfoboxFontsize() {
		return coverinfoboxFontsize;
	}
	
	/**
	 * @param coverinfoboxFontsize the coverinfoboxFontsize to set
	 */
	public void setCoverinfoboxFontsize(int coverinfoboxFontsize) {
		this.coverinfoboxFontsize = coverinfoboxFontsize;
	}
	
	/**
	 * @return the coverInfoOffsetX
	 */
	public int getCoverInfoOffsetX() {
		return coverInfoOffsetX;
	}
	
	/**
	 * @param coverInfoOffsetX the coverInfoOffsetX to set
	 */
	public void setCoverInfoOffsetX(int coverInfoOffsetX) {
		this.coverInfoOffsetX = coverInfoOffsetX;
	}
	
	/**
	 * @return the coverInfoOffsetY
	 */
	public int getCoverInfoOffsetY() {
		return coverInfoOffsetY;
	}
	
	/**
	 * @param coverInfoOffsetY the coverInfoOffsetY to set
	 */
	public void setCoverInfoOffsetY(int coverInfoOffsetY) {
		this.coverInfoOffsetY = coverInfoOffsetY;
	}
	
	/**
	 * @return the coverLetterImage
	 */
	public InputStream getCoverLetterImage() {
		return coverLetterImage;
	}
	
	/**
	 * @param coverLetterImage the coverLetterImage to set
	 */
	public void setCoverLetterImage(InputStream coverLetterImage) {
		this.coverLetterImage = coverLetterImage;
	}
	
	/**
	 * @return the coverLetterTemplate
	 */
	public String getCoverLetterTemplate() {
		return coverLetterTemplate;
	}
	
	/**
	 * @param coverLetterTemplate the coverLetterTemplate to set
	 */
	public void setCoverLetterTemplate(String coverLetterTemplate) {
		this.coverLetterTemplate = coverLetterTemplate;
	}
	
	/**
	 * @return the dateTransText
	 */
	public String getDateTransText() {
		return dateTransText;
	}
	
	/**
	 * @param dateTransText the dateTransText to set
	 */
	public void setDateTransText(String dateTransText) {
		this.dateTransText = dateTransText;
	}
	
	/**
	 * @return the deptNum
	 */
	public String getDeptNum() {
		return deptNum;
	}
	
	/**
	 * @param deptNum the deptNum to set
	 */
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}
	
	/**
	 * @return the distributionDate
	 */
	public Timestamp getDistributionDate() {
		return distributionDate;
	}
	
	/**
	 * @param distributionDate the distributionDate to set
	 */
	public void setDistributionDate(Timestamp distributionDate) {
		this.distributionDate = distributionDate;
	}
	
	/**
	 * @return the divisionCode
	 */
	public String getDivisionCode() {
		return divisionCode;
	}
	
	/**
	 * @param divisionCode the divisionCode to set
	 */
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	
	/**
	 * @return the docGenDate
	 */
	public Timestamp getDocGenDate() {
		return docGenDate;
	}
	
	/**
	 * @param docGenDate the docGenDate to set
	 */
	public void setDocGenDate(Timestamp docGenDate) {
		this.docGenDate = docGenDate;
	}
	
	/**
	 * @return the docGroupNum
	 */
	public String getDocGroupNum() {
		return docGroupNum;
	}
	
	/**
	 * @param docGroupNum the docGroupNum to set
	 */
	public void setDocGroupNum(String docGroupNum) {
		this.docGroupNum = docGroupNum;
	}
	
	/**
	 * @return the docGrpNumTransText
	 */
	public String getDocGrpNumTransText() {
		return docGrpNumTransText;
	}
	
	/**
	 * @param docGrpNumTransText the docGrpNumTransText to set
	 */
	public void setDocGrpNumTransText(String docGrpNumTransText) {
		this.docGrpNumTransText = docGrpNumTransText;
	}
	
	/**
	 * @return the docIndexId
	 */
	public String getDocIndexId() {
		return docIndexId;
	}
	
	/**
	 * @param docIndexId the docIndexId to set
	 */
	public void setDocIndexId(String docIndexId) {
		this.docIndexId = docIndexId;
	}
	
	/**
	 * @return the docTradeName
	 */
	public String getDocTradeName() {
		return docTradeName;
	}
	
	/**
	 * @param docTradeName the docTradeName to set
	 */
	public void setDocTradeName(String docTradeName) {
		this.docTradeName = docTradeName;
	}
	
	/**
	 * @return the docTypeCode
	 */
	public String getDocTypeCode() {
		return docTypeCode;
	}
	
	/**
	 * @param docTypeCode the docTypeCode to set
	 */
	public void setDocTypeCode(String docTypeCode) {
		this.docTypeCode = docTypeCode;
	}
	
	/**
	 * @return the ediPassword
	 */
	public String getEdiPassword() {
		return ediPassword;
	}
	
	/**
	 * @param ediPassword the ediPassword to set
	 */
	public void setEdiPassword(String ediPassword) {
		this.ediPassword = ediPassword;
	}
	
	/**
	 * @return the ediUserId
	 */
	public String getEdiUserId() {
		return ediUserId;
	}
	
	/**
	 * @param ediUserId the ediUserId to set
	 */
	public void setEdiUserId(String ediUserId) {
		this.ediUserId = ediUserId;
	}
	
	/**
	 * @return the ediVan
	 */
	public String getEdiVan() {
		return ediVan;
	}
	
	/**
	 * @param ediVan the ediVan to set
	 */
	public void setEdiVan(String ediVan) {
		this.ediVan = ediVan;
	}
	
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}
	
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	/**
	 * @return the faxNumber
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	
	/**
	 * @param faxNumber the faxNumber to set
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	/**
	 * @return the ftpDirectory
	 */
	public String getFtpDirectory() {
		return ftpDirectory;
	}
	
	/**
	 * @param ftpDirectory the ftpDirectory to set
	 */
	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}
	
	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}
	
	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	
	/**
	 * @return the ftpSite
	 */
	public String getFtpSite() {
		return ftpSite;
	}
	
	/**
	 * @param ftpSite the ftpSite to set
	 */
	public void setFtpSite(String ftpSite) {
		this.ftpSite = ftpSite;
	}
	
	/**
	 * @return the ftpUserId
	 */
	public String getFtpUserId() {
		return ftpUserId;
	}
	
	/**
	 * @param ftpUserId the ftpUserId to set
	 */
	public void setFtpUserId(String ftpUserId) {
		this.ftpUserId = ftpUserId;
	}
	
	/**
	 * @return the manualFlag
	 */
	public String getManualFlag() {
		return manualFlag;
	}
	
	/**
	 * @param manualFlag the manualFlag to set
	 */
	public void setManualFlag(String manualFlag) {
		this.manualFlag = manualFlag;
	}
	
	/**
	 * @return the mediaAddress
	 */
	public String getMediaAddress() {
		return mediaAddress;
	}
	
	/**
	 * @param mediaAddress the mediaAddress to set
	 */
	public void setMediaAddress(String mediaAddress) {
		this.mediaAddress = mediaAddress;
	}
	
	/**
	 * @return the mediaTypeCode
	 */
	public String getMediaTypeCode() {
		return mediaTypeCode;
	}
	
	/**
	 * @param mediaTypeCode the mediaTypeCode to set
	 */
	public void setMediaTypeCode(String mediaTypeCode) {
		this.mediaTypeCode = mediaTypeCode;
	}
	
	/**
	 * @return the numCopies
	 */
	public int getNumCopies() {
		return numCopies;
	}
	
	/**
	 * @param numCopies the numCopies to set
	 */
	public void setNumCopies(int numCopies) {
		this.numCopies = numCopies;
	}
	
	/**
	 * @return the opcoCode
	 */
	public String getOpcoCode() {
		return opcoCode;
	}
	
	/**
	 * @param opcoCode the opcoCode to set
	 */
	public void setOpcoCode(String opcoCode) {
		this.opcoCode = opcoCode;
	}
	
	/**
	 * @return the orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}
	
	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	/**
	 * @return the orderNumTransText
	 */
	public String getOrderNumTransText() {
		return orderNumTransText;
	}
	
	/**
	 * @param orderNumTransText the orderNumTransText to set
	 */
	public void setOrderNumTransText(String orderNumTransText) {
		this.orderNumTransText = orderNumTransText;
	}
	
	/**
	 * @return the orderPlacer
	 */
	public String getOrderPlacer() {
		return orderPlacer;
	}
	
	/**
	 * @param orderPlacer the orderPlacer to set
	 */
	public void setOrderPlacer(String orderPlacer) {
		this.orderPlacer = orderPlacer;
	}
	
	/**
	 * @return the orderPlacerPhone
	 */
	public String getOrderPlacerPhone() {
		return orderPlacerPhone;
	}
	
	/**
	 * @param orderPlacerPhone the orderPlacerPhone to set
	 */
	public void setOrderPlacerPhone(String orderPlacerPhone) {
		this.orderPlacerPhone = orderPlacerPhone;
	}
	
	/**
	 * @return the packageBlankPageCount
	 */
	public int getPackageBlankPageCount() {
		return packageBlankPageCount;
	}
	
	/**
	 * @param packageBlankPageCount the packageBlankPageCount to set
	 */
	public void setPackageBlankPageCount(int packageBlankPageCount) {
		this.packageBlankPageCount = packageBlankPageCount;
	}
	
	/**
	 * @return the packageDelete
	 */
	public String getPackageDelete() {
		return packageDelete;
	}
	
	/**
	 * @param packageDelete the packageDelete to set
	 */
	public void setPackageDelete(String packageDelete) {
		this.packageDelete = packageDelete;
	}
	
	/**
	 * @return the packageID
	 */
	public String getPackageID() {
		return packageID;
	}
	
	/**
	 * @param packageID the packageID to set
	 */
	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}
	
	/**
	 * @return the packagePageCount
	 */
	public int getPackagePageCount() {
		return packagePageCount;
	}
	
	/**
	 * @param packagePageCount the packagePageCount to set
	 */
	public void setPackagePageCount(int packagePageCount) {
		this.packagePageCount = packagePageCount;
	}
	
	/**
	 * @return the printSendMethod
	 */
	public String getPrintSendMethod() {
		return printSendMethod;
	}
	
	/**
	 * @param printSendMethod the printSendMethod to set
	 */
	public void setPrintSendMethod(String printSendMethod) {
		this.printSendMethod = printSendMethod;
	}
	
	/**
	 * @return the regAgencyCode
	 */
	public String getRegAgencyCode() {
		return regAgencyCode;
	}
	
	/**
	 * @param regAgencyCode the regAgencyCode to set
	 */
	public void setRegAgencyCode(String regAgencyCode) {
		this.regAgencyCode = regAgencyCode;
	}
	
	/**
	 * @return the revisionNumber
	 */
	public BigDecimal getRevisionNumber() {
		return revisionNumber;
	}
	
	/**
	 * @param revisionNumber the revisionNumber to set
	 */
	public void setRevisionNumber(BigDecimal revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	
	/**
	 * @return the salesCustomerNum
	 */
	public String getSalesCustomerNum() {
		return salesCustomerNum;
	}
	
	/**
	 * @param salesCustomerNum the salesCustomerNum to set
	 */
	public void setSalesCustomerNum(String salesCustomerNum) {
		this.salesCustomerNum = salesCustomerNum;
	}
	
	/**
	 * @return the salesSourceCode
	 */
	public String getSalesSourceCode() {
		return salesSourceCode;
	}
	
	/**
	 * @param salesSourceCode the salesSourceCode to set
	 */
	public void setSalesSourceCode(String salesSourceCode) {
		this.salesSourceCode = salesSourceCode;
	}
	
	/**
	 * @return the sapCountryCode
	 */
	public String getSapCountryCode() {
		return sapCountryCode;
	}
	
	/**
	 * @param sapCountryCode the sapCountryCode to set
	 */
	public void setSapCountryCode(String sapCountryCode) {
		this.sapCountryCode = sapCountryCode;
	}
	
	/**
	 * @return the sapLanguageCode
	 */
	public String getSapLanguageCode() {
		return sapLanguageCode;
	}
	
	/**
	 * @param sapLanguageCode the sapLanguageCode to set
	 */
	public void setSapLanguageCode(String sapLanguageCode) {
		this.sapLanguageCode = sapLanguageCode;
	}
	
	/**
	 * @return the sendUpdate
	 */
	public String getSendUpdate() {
		return sendUpdate;
	}
	
	/**
	 * @param sendUpdate the sendUpdate to set
	 */
	public void setSendUpdate(String sendUpdate) {
		this.sendUpdate = sendUpdate;
	}
	
	/**
	 * @return the shipDate
	 */
	public Timestamp getShipDate() {
		return shipDate;
	}
	
	/**
	 * @param shipDate the shipDate to set
	 */
	public void setShipDate(Timestamp shipDate) {
		this.shipDate = shipDate;
	}
	
	/**
	 * @return the stateCode
	 */
	public String getStateCode() {
		return stateCode;
	}
	
	/**
	 * @param stateCode the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	/**
	 * @return the statusDate
	 */
	public Timestamp getStatusDate() {
		return statusDate;
	}
	
	/**
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}
	
	/**
	 * @return the templateFormatCode
	 */
	public String getTemplateFormatCode() {
		return templateFormatCode;
	}
	
	/**
	 * @param templateFormatCode the templateFormatCode to set
	 */
	public void setTemplateFormatCode(String templateFormatCode) {
		this.templateFormatCode = templateFormatCode;
	}
	
	/**
	 * @return the templateID
	 */
	public String getTemplateID() {
		return templateID;
	}
	
	/**
	 * @param templateID the templateID to set
	 */
	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}
	
	/**
	 * @return the tmspLastUpdt
	 */
	public Timestamp getTmspLastUpdt() {
		return tmspLastUpdt;
	}
	
	/**
	 * @param tmspLastUpdt the tmspLastUpdt to set
	 */
	public void setTmspLastUpdt(Timestamp tmspLastUpdt) {
		this.tmspLastUpdt = tmspLastUpdt;
	}
	
	/**
	 * @return the userLastUpdt
	 */
	public String getUserLastUpdt() {
		return userLastUpdt;
	}
	
	/**
	 * @param userLastUpdt the userLastUpdt to set
	 */
	public void setUserLastUpdt(String userLastUpdt) {
		this.userLastUpdt = userLastUpdt;
	}
	
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
