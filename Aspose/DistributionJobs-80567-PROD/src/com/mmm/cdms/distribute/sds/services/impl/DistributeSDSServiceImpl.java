/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.distribute.sds.beans.MediaSettingsBean;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.constants.PropertyFileConstants;
import com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo;
import com.mmm.cdms.distribute.sds.repositories.impl.DistributeSDSRepoImpl;
import com.mmm.cdms.distribute.sds.services.DistributeSDSService;
import com.mmm.cdms.distribute.sds.services.DocumentService;
import com.mmm.cdms.distribute.sds.services.EmailService;
import com.mmm.cdms.distribute.sds.services.FileManagerService;
import com.mmm.cdms.distribute.sds.services.FileTransferService;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * @author A5AK3ZZ
 *
 */
public class DistributeSDSServiceImpl implements DistributeSDSService {
	
	/***********************************************************************************
	 * Constructors
	 ***********************************************************************************/
	
	/**
	 * Constructor to initialize the objects
	 */
	public DistributeSDSServiceImpl() {
		this.fileService = new FileTransferServiceImpl();
		this.distributeSDSRepo = new DistributeSDSRepoImpl();
		this.sysOption = new SysOptionBean();
		this.mediaSettings = new MediaSettingsBean();
	}
	
	/***********************************************************************************
	 * Static variables
	 ***********************************************************************************/
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(DistributeSDSServiceImpl.class);
	
	/**
	 * Constant representing a tab
	 */
	private static final String TAB_STRING = "\t";
	
	/**
	 * Constant representing a colon
	 */
	private static final String COLON_STRING = ":";
	
	/**
	 * Report Date format constant
	 */
	private static final String REPORT_DATE_FORMAT = "MMddyyyy";
	
	/**
	 * Report time format constant
	 */
	private static final String REPORT_TIME_FORMAT = "HHmm";
	
	/************************************************************************************
	 * Class variables
	 ***********************************************************************************/
	
	/**
	 * fileService
	 */
	public FileTransferServiceImpl fileService;
	/**
	 * sysOption
	 */
	public SysOptionBean sysOption;
	/**
	 * distributeSDSRepo
	 */
	public DistributeSDSRepo distributeSDSRepo;
	/**
	 * mediaSettingsBean
	 */
	public MediaSettingsBean mediaSettings;
	
	/************************************************************************************
	 * Public methods
	 ***********************************************************************************/
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmm.cdms.distribute.sds.services.DistributeSDSService#distributeSDS()
	 */
	public boolean distributeSDS() throws Exception {
		LOGGER.info("Begin distribution process");
		LOGGER.debug("BEGIN : distributeSDS");
		boolean isSDSDistributionSuccessful = true;
		LOGGER.debug("*****going to create a temp table*****");
		this.createDQCounterTempTable();
		sysOption = distributeSDSRepo.loadSysOptionParams();
		if (!hasMandatoryValues()) {
			// Abort the operation if any of the mandatory values from sys_option is not loaded
			LOGGER.error("Mandatory values missing from SYS OPTION");
			throw new DistributionException("Some Mandatory Values are not found in SYS_OPTION table. Please check");
		} else {
			isSDSDistributionSuccessful = this.checkFileStatus();
		}
		LOGGER.debug("END : distributeSDS");
		return isSDSDistributionSuccessful;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DistributeSDSService#
	 * deleteDistributedRecords(com.mmm.cdms.distribute.sds.beans.
	 * PdfDetailsDTO, java.sql.Connection, java.lang.String, boolean)
	 */
	@Override
	public void deleteDistributedRecords(String mediaType, boolean hasNoData) throws DistributionException {
		distributeSDSRepo.deleteDistributedRecords(mediaType, hasNoData);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DistributeSDSService#
	 * createDQCounterTempTable(com.mmm.cdms.distribute.sds.beans.
	 * PdfDetailsDTO)
	 */
	@Override
	public void createDQCounterTempTable() throws DistributionException {
		distributeSDSRepo.createDQCounterTempTable();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmm.cdms.distribute.sds.services.DistributeSDSService#dropTempTable()
	 */
	@Override
	public void dropTempTable() throws DistributionException {
		distributeSDSRepo.dropTempTable();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DistributeSDSService#
	 * archiveAndDeleteFiles(com.mmm.cdms.distribute.sds.beans.
	 * PdfDetailsDTO)
	 */
	@Override
	public void archiveAndDeleteFiles() {
		FileManagerService fileServices = new FileManagerServiceImpl();
		fileServices.archiveAndDeleteFiles(sysOption.getHomePath(),
				sysOption.getSupportDir() + sysOption.getDataPath());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DistributeSDSService#
	 * sendMailAndLogEvent(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	public void sendMailAndLogEvent(final String statusFlag, final String jobName) {
		EmailService emailService = new EmailServiceImpl();
		emailService.sendEmailAndLogEvent(statusFlag, jobName, mediaSettings.getEmailSubject(), sysOption);
	}
	
	/**
	 * Method to logger events
	 * 
	 * @param msgShort
	 * @param msgLong
	 * @param statusCode
	 * @param jobName
	 */
	public void logEvents(final String msgShort, final String msgLong, final int statusCode,
			final String jobName) {
		distributeSDSRepo.logEvents(msgShort, msgLong, statusCode, jobName);
	}
	
	/************************************************************************************
	 * Private methods
	 ***********************************************************************************/
	
	/**
	 * Method to remove duplicates for Email or Paper
	 * 
	 * @param inputMedia
	 * @throws DistributionException
	 */
	private void removeDuplicates(final int inputMedia) throws DistributionException {
		if (inputMedia == 1) {
			distributeSDSRepo.executeSP(
					DistributeSDSUtils.getPropertyValue(PropertyFileConstants.SP_REMOVE_EMAIL_DUPES));
		} else if (inputMedia == 3) {
			distributeSDSRepo.executeSP(
					DistributeSDSUtils.getPropertyValue(PropertyFileConstants.SP_REMOVE_PAPER_DUPES));
		}
	}
	
	/**
	 * Method checks whether all values have been populated from SYS_OPTION table
	 * 
	 * @return true or false
	 */
	private boolean hasMandatoryValues() {
		boolean hasPassed = false;
		if ((StringUtils.isNotBlank(sysOption.getHomePath()))
				&& (StringUtils.isNotBlank(sysOption.getSupportDir()))
				&& (StringUtils.isNotBlank(sysOption.getPrinterFtpDir()))
				&& (StringUtils.isNotBlank(sysOption.getPrinterFtpUserid()))
				&& (StringUtils.isNotBlank(sysOption.getPrinterFtpPwd()))
				&& (StringUtils.isNotBlank(sysOption.getPrinterFtpAddr()))
				&& (StringUtils.isNotBlank(sysOption.getCypressPrinterFtpDir()))
				&& (StringUtils.isNotBlank(sysOption.getCypressPrinterFtpUserid()))
				&& (StringUtils.isNotBlank(sysOption.getCypressPrinterFtpPwd()))
				&& (StringUtils.isNotBlank(sysOption.getCypressPrinterFtpAddr()))) {
			hasPassed = true;
		}
		return hasPassed;
	}
	
	/**
	 * Checks the status of the files in the Packages directory and 
	 * move/delete files already existing in the data path where new files are going to be created
	 * 
	 * @return isSDSDistrSuccess = success/failure of the distribution process
	 * @throws Exception
	 */
	private boolean checkFileStatus() throws Exception {
		LOGGER.debug("BEGIN : checkFileStatus Method");
		boolean isSDSDistrSuccess;
		DistributeSDSApplication.pdfDetailsDTO.setBatchPackageCount(0);
		this.configureMediaSettings(DistributeSDSApplication.mediaType);
		String homePath = sysOption.getHomePath();
		File file = new File(homePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// creating report file
		String completeDataPath = sysOption.getSupportDir() + sysOption.getDataPath();
		FileTransferService fileTransferService = new FileTransferServiceImpl();
		boolean isDataFolderCleared = fileTransferService.moveErrorFiles(homePath,
				completeDataPath + DistributeSDSConstants.ERROR_DATA_FOLDER_NAME
						+ DistributeSDSConstants.FILE_SEPERATOR
						+ DistributeSDSUtils.getCurrentDateTime(), sysOption.getDateTime());
		if (!isDataFolderCleared) {
			LOGGER.error("Unable to clear the data folder");
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("Unable to clear the data folder");
			isSDSDistrSuccess = false;
		} else {
			isSDSDistrSuccess = this.retrieveDataDetails();
		}
		LOGGER.debug("END : checkFileStatus Method");
		return isSDSDistrSuccess;
	}
	
	/**
	 * This method retrieves the data from distribution queue table and puts them into a java.util.Map
	 * for a unique key which is a combination of  - address ID + customer number +	media type code
	 *  
	 *  <br><br>For EMEA data having same key, 
	 *  we need to bundle it into one PDF with one cover letter which contains all the doc-group-num 
	 *  and all order numbers and separate each SDS with a separator page.
	 *  
	 *  <br><br>For US data we have to keep the PDF documents separate with their own cover letter, 
	 *  no matter even if the key is same.
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean retrieveDataDetails() throws Exception {
		boolean isSDSDistrSuccess = false;
		// method call to remove duplicates for MEDIA_TYPE Email & paper
		this.removeDuplicates(DistributeSDSApplication.mediaType);
		// obtain the data required for processing PDF
		DocumentService generator = new DocumentServiceImpl(sysOption, mediaSettings);
		LOGGER.debug("Going to retrieve the data from Distribution_Queue table");
		List<DistributionQueueBean> dqList = generator.retrieveDistributionQueueDetails();
		Map<String, List<DistributionQueueBean>> completeDataListMap = new HashMap<>();
		for (DistributionQueueBean dqBean : dqList) {
			String distributionKey = dqBean.getAddressId() + "_" + dqBean.getSalesCustomerNum() + "_" + dqBean.getMediaTypeCode();
			if (!completeDataListMap.keySet().contains(distributionKey)) {
				List<DistributionQueueBean> newpkgIDList = new ArrayList<>();
				newpkgIDList.add(dqBean);
				completeDataListMap.put(distributionKey, newpkgIDList);
			} else {
				List<DistributionQueueBean> existingList = completeDataListMap.get(distributionKey);
				existingList.add(dqBean);
				completeDataListMap.put(distributionKey, existingList);
			}
		}
		if (!completeDataListMap.isEmpty()) {
			if (!checkDriveAvailability()) {
				LOGGER.error("Drive " + PropertyFileConstants.DRIVE_MOUNT + ": already in use. Unable to mount the drive.");
				DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("Drive " + PropertyFileConstants.DRIVE_MOUNT
								+ ": already in use. Unable to mount the drive.");
				isSDSDistrSuccess = false;
			} else {
				this.beginDocGeneration(completeDataListMap);
				isSDSDistrSuccess = true;
			}
		} else {
			LOGGER.debug("===========There is no data to process for this run===========");
			DistributeSDSApplication.pdfDetailsDTO
					.setReportErrorMessage("There is no data to process for this run");
			deleteDistributedRecords(mediaSettings.getMediaType(), true);
			isSDSDistrSuccess = true;
		}
		return isSDSDistrSuccess;
	}
	
	/**
	 * Begin the doc generation process
	 * 
	 * @param completeDataListMap
	 * @throws Exception
	 */
	private void beginDocGeneration(Map<String, List<DistributionQueueBean>> completeDataListMap)
			throws Exception {
		LOGGER.info("--BEGIN : beginDocGeneration--");
//		LOGGER.info("bulding the email map as well");
//		this.buildEmailMap();
		LOGGER.info("starting the PDF Document process for all records");
		for (String key : completeDataListMap.keySet()) {
			this.processPDFgeneration(completeDataListMap.get(key), DistributeSDSApplication.mediaType);
		}
		List<String> usFileList = new ArrayList<>();
		//gather all US files into one list
		String homePath = sysOption.getHomePath() + DistributeSDSConstants.FILE_SEPERATOR;
		File homeDir = new File(homePath);
		for (String usFile : homeDir.list()) {
			if (usFile.contains(DistributeSDSConstants.US_PAPER_MAIL_FILE_NAME)
					|| usFile.contains(DistributeSDSConstants.US_PAPER_MAIL_REPORT_FILE_NAME)) {
				usFileList.add(usFile);
			}
		}
		//processing US Ftp
		if (!usFileList.isEmpty()) {
			LOGGER.debug("going to process US Files transfer");
			fileService.processUSFilesTransfer(usFileList, sysOption, mediaSettings);
		}
		this.createSummary(DistributeSDSApplication.pdfDetailsDTO.getBatchPackageCount(),
				DistributeSDSApplication.pdfDetailsDTO.getBatchBlankPageCount(),
				DistributeSDSApplication.pdfDetailsDTO.getBatchPageCount());
		// delete the distributed records marked using temp table
		deleteDistributedRecords(mediaSettings.getMediaType(), false);
		// write overall status in the report file
		this.dropTempTable();
		DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("");
		DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.PROCESS_STATUS_TRANSFERED);
		LOGGER.debug("distributeSDS Method of DistributeSDSManager ***********"
				+ DistributeSDSApplication.mediaType);
		LOGGER.info("The PDF Document process completed for all records");
	}
	
//	/**
//	 * Initializing map for return email addresses fetched from the database
//	 */
//	private void buildEmailMap() {
//		DistributeSDSRepo distributeRepo = new DistributeSDSRepoImpl();
//		DocumentServiceImpl.returnEmailAddrMap = distributeRepo.getReturnEmailAddress();
//	}
	
	/**
	 * @throws InterruptedException, DistributionException 
	 * @throws IOException 
	 * @throws DistributionException
	 */
	private boolean checkDriveAvailability()
			throws IOException, InterruptedException, DistributionException {
		boolean isDriveConnectSuccess = false;
		fileService = new FileTransferServiceImpl(sysOption.getCypressPrinterFtpAddr(),
				sysOption.getCypressPrinterFtpDir());
		isDriveConnectSuccess = fileService.executeNetUseConnect(sysOption.getCypressPrinterFtpDir(),
				sysOption.getCypressPrinterFtpUserid(), sysOption.getCypressPrinterFtpPwd(),
				DistributeSDSUtils.getPropertyValue(PropertyFileConstants.DRIVE_MOUNT));
		return isDriveConnectSuccess;
	}
	
	/**
	 * Method to process the data obtained for PDF processing and sending
	 * 
	 * @param orderDataMap
	 * @param inputMedia
	 * @param isDuplex
	 * @throws Exception 
	 */
	private void processPDFgeneration(final List<DistributionQueueBean> completeDataList, final int inputMedia) throws Exception {
		DocumentServiceImpl docService = new DocumentServiceImpl(sysOption, mediaSettings);
		docService.generatePdfdocument(completeDataList, inputMedia, fileService);
	}
	
	/**
	 * Method to do the settings based on media type
	 * 
	 * @param inputMedia
	 * @throws DistributionException
	 */
	private void configureMediaSettings(final int inputMedia) throws DistributionException {
		LOGGER.debug("BEGIN : configureMediaSettings Method");
		DateFormat df = new SimpleDateFormat(REPORT_DATE_FORMAT);
		Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		DateFormat format = new SimpleDateFormat(REPORT_TIME_FORMAT);
		sysOption.setDateTime(reportDate.concat("-").concat(format.format(today)));
		sysOption.setHomePath(sysOption.getSupportDir() + sysOption.getHomePath() + DistributeSDSConstants.FILE_SEPERATOR);
		//sysOption.setSupportDirLoc(sysOption.getSupportDir().concat(sysOption.getSupportDirLoc()));
//		sysOption.setMessageLog(sysOption.getSupportDir() + DistributeSDSConstants.FILE_SEPERATOR
//				+ sysOption.getMessageLog());
		mediaSettings.setMediaType(DistributeSDSApplication.mediaTypeString);
		mediaSettings.setEmailSubject(DistributeSDSApplication.jobName + " FINISHED IN ");
		if (inputMedia == 1) {
			mediaSettings.setFileNameEU(sysOption.getHomePath() + DistributeSDSConstants.EU_EMAIL_FILE_NAME);
			mediaSettings.setReportFileNameEU(sysOption.getHomePath() + DistributeSDSConstants.EU_EMAIL_REPORT_FILE_NAME);
			mediaSettings.setFileNameUS(sysOption.getHomePath() + DistributeSDSConstants.US_EMAIL_FILE_NAME);
			mediaSettings.setReportFileNameUS(sysOption.getHomePath() + DistributeSDSConstants.US_EMAIL_REPORT_FILE_NAME);
		} else if (inputMedia == 2) {
			mediaSettings.setFileNameEU(sysOption.getHomePath() + DistributeSDSConstants.EU_FAX_FILE_NAME);
			mediaSettings.setReportFileNameEU(sysOption.getHomePath() + DistributeSDSConstants.EU_FAX_REPORT_FILE_NAME);
			mediaSettings.setFileNameUS(sysOption.getHomePath() + DistributeSDSConstants.US_FAX_FILE_NAME);
			mediaSettings.setReportFileNameUS(sysOption.getHomePath() + DistributeSDSConstants.US_FAX_REPORT_FILE_NAME);
		} else if (inputMedia == 3) {
			mediaSettings.setFileNameEU(sysOption.getHomePath() + DistributeSDSConstants.EU_PAPER_MAIL_FILE_NAME);
			mediaSettings.setReportFileNameEU(sysOption.getHomePath() + DistributeSDSConstants.EU_PAPER_MAIL_REPORT_FILE_NAME);
			mediaSettings.setFileNameUS(sysOption.getHomePath() + DistributeSDSConstants.US_PAPER_MAIL_FILE_NAME);
			mediaSettings.setReportFileNameUS(sysOption.getHomePath() + DistributeSDSConstants.US_PAPER_MAIL_REPORT_FILE_NAME);
		} else {
			throw new DistributionException("Job invoked with wrong MediaType Code");
		}
		LOGGER.debug("END : configureMediaSettings Method");
		LOGGER.info("Media settings configured !!");
	}
	
	/**
	 * Create the summary data for the process to be appended to flat file and
	 * 
	 * @param fileName
	 * @param batchPackageCount
	 * @param batchBlankPageCount
	 * @param batchPageCount
	 * @throws IOException
	 */
	private void createSummary(final int batchPackageCount,	final int batchBlankPageCount, final int batchPageCount) throws IOException {
		File fileName = new File(sysOption.getHomePath() + DistributeSDSConstants.FILE_SEPERATOR
				+ "USDistributionSummary.txt");
		try (BufferedWriter reportBW = new BufferedWriter(new FileWriter(fileName, true))) {
			String formatString = TAB_STRING + COLON_STRING + TAB_STRING;
			reportBW.write(DistributeSDSConstants.DASHED_COMMENT);
			reportBW.newLine();
			reportBW.write("Report for" + TAB_STRING + TAB_STRING + formatString + DistributeSDSApplication.jobName);
			reportBW.newLine();
			reportBW.write("Run DateTime" + TAB_STRING + TAB_STRING + formatString + sysOption.getDateTime());
			reportBW.newLine();
			reportBW.write("Total no. of packages/jobs" + formatString + batchPackageCount);
			reportBW.newLine();
			reportBW.write("Total no. of pdf pages" + TAB_STRING + formatString + batchPageCount);
			reportBW.newLine();
			reportBW.write("Total no. of blank pages" + formatString + batchBlankPageCount);
			reportBW.newLine();
			reportBW.write(DistributeSDSConstants.DASHED_COMMENT);
			reportBW.newLine();
			reportBW.write("Total pages" + TAB_STRING +TAB_STRING + formatString + (batchBlankPageCount + batchPageCount));
		} catch (IOException ex) {
			LOGGER.error("Error occurred while sending summary into the file" + ex.getMessage());
			throw ex;
		}
	}

}