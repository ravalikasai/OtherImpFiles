/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.constants.PropertyFileConstants;
import com.mmm.cdms.distribute.sds.dto.PdfDetailsDTO;
import com.mmm.cdms.distribute.sds.services.DistributeSDSService;
import com.mmm.cdms.distribute.sds.services.impl.DistributeSDSServiceImpl;
import com.mmm.cdms.distribute.sds.services.impl.FileTransferServiceImpl;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * Main controller class which initiates and handles 
 * the EU doc generation/transfer
 * 
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 */
public class DistributeSDSApplication {
	
	/**
	 * Hyphen constant
	 */
	private static final String HYPHEN_CONSTANT = " - ";
	
	/**
	 * Logger for Controller class
	 */
	private static final Logger LOGGER = Logger.getLogger(DistributeSDSApplication.class);
		
	/**
	 * Directory that can be mounted for sending files through
	 * File Sharing operation - This directory should not be used already
	 */
	public static String mountDirName;
	
	/**
	 * String constant that holds the actual job_name
	 */
	public static String jobName;
	
	/**
	 * String constant that holds the actual job_name
	 */
	public static String mediaTypeString;
	
	/**
	 * Support Directory
	 */
	public static String supportDir;
	
	/**
	 * Media type lets the application know 
	 * which mode are we distributing the SDS in
	 */
	public static int mediaType;
	
	/**
	 * PropertyFile name
	 */
	public static String propertyFile;
	
	/**
	 * PdfDetailsDTO to move around the status and other 
	 * variables to classes 
	 */
	public static PdfDetailsDTO pdfDetailsDTO = new PdfDetailsDTO();
	
	/**
	 * Start the processing of data in Distribution_Queue table 
	 * and send SDS to the customers
	 *  
	 * @param args = input arguments for the jar which should be the property file path 
	 * 		  and the media type code 
	 */
	public static void main (final String[] args) {
		LOGGER.info(DistributeSDSConstants.DASHED_COMMENT);
		LOGGER.info(" START OF THE APPLICATION main()");
		LOGGER.info(DistributeSDSConstants.DASHED_COMMENT);
		DistributeSDSService distrService = new DistributeSDSServiceImpl();
		DistributeSDSApplication app = new DistributeSDSApplication();
		try {
			if (args.length == 2) {
				app.validateInputs(args);
				LOGGER.debug("Going to initialize properties files");
				final boolean isPropertySet = DistributeSDSUtils.initializeProperties(propertyFile);
				if (isPropertySet) {
					if (mediaType < 1 && mediaType >= 4) {
						throw new DistributionException("mediaType Not Found or Wrongly defined in properties file!" );
					}
					app.setJobName(DistributeSDSApplication.mediaType);
					LOGGER.info("Inputs valid. Begin with processing");
					app.process(distrService);
				}
			} else {
				LOGGER.error("Not enough attributes in the command file.");
				LOGGER.error("\n\nUsage: java -jar DistributeSDSApplication -p:<PropertyFile Location> -n:<MediaType>\n\n");
				LOGGER.error("***The 2 attributes expected in the command file are not available. Check the command file used to invoke the process.***");
			}
		} catch (Exception allExceptions) {
			LOGGER.error("Error occurred while processing SDS Distribution : " + allExceptions.getMessage());
			pdfDetailsDTO.setReportErrorMessage("Fatal Error occured : Not able to load properties files or could not get database connection");			
			distrService.sendMailAndLogEvent(DistributeSDSConstants.JOB_EXCEPTION_STATUS, jobName);
			LOGGER.error(allExceptions.getMessage());			
			LOGGER.error("Exception occured in the execution of DistributeSDSApplication main");
		}
		LOGGER.info(DistributeSDSConstants.DASHED_COMMENT);
		LOGGER.info(" END OF THE APPLICATION main()");
		LOGGER.info(DistributeSDSConstants.DASHED_COMMENT);
	}

	/**
	 * Validating the input arguments for this job
	 * 
	 * @param args
	 * @throws DistributionException
	 */
	private void validateInputs(final String[] args) throws DistributionException {
		for (final String inputArg : args) {
			final String attributeVal = inputArg.substring(3);
			if (inputArg.startsWith("-p:")) {
				if (StringUtils.isNotBlank(attributeVal)) {
					propertyFile = attributeVal;
					LOGGER.debug("PROPERTY FILE Location" + HYPHEN_CONSTANT + propertyFile);
				} else {
					throw new DistributionException("PROPERTY FILE Location not loaded");
				}
			} else if (inputArg.startsWith("-n:")) {
				if (StringUtils.isNotBlank(attributeVal)) {
					mediaType = Integer.parseInt(attributeVal);
					LOGGER.debug("MEDIA TYPE" + HYPHEN_CONSTANT + mediaType);
				} else {
					throw new DistributionException("MEDIA TYPE not loaded");
				}
			} else if (inputArg.startsWith("-?")) {
				LOGGER.error("\n\nUsage: java -jar DistributeSDSApplication -p:<PropertyFile Location> -n:<MediaType>\n\n");
			}
		}
	}
	
	/**
	 * Method to initiate the processing of data from DQ table, 
	 * bundling into PDF documents and sending to customers/cypress (for EMEA)  
	 * 
	 * @param distrService
	 */
	private void process(final DistributeSDSService distrService) {
		LOGGER.debug("process Method of DistributeSDSApplication");
		String statusFlag = null;
		try {
			final boolean isSuccess = distrService.distributeSDS();
			if (isSuccess) {
				if (pdfDetailsDTO != null) {
					// Archive the current day's file to a new archive directory
					distrService.archiveAndDeleteFiles();
					statusFlag = DistributeSDSConstants.JOB_EXECUTION_SUCCESS_STATUS;
				}
			} else {
				statusFlag = DistributeSDSConstants.JOB_DELETION_ERROR_STATUS;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in executing run method : ", e);
			pdfDetailsDTO.setReportErrorMessage("Exception occured in executing run method : " + e.getMessage());
			statusFlag = DistributeSDSConstants.JOB_EXCEPTION_STATUS;
		} finally {
			try {
				distrService.sendMailAndLogEvent(statusFlag, jobName);
				if (pdfDetailsDTO.isDriveConnectSuccess()) {
					final FileTransferServiceImpl fileTransfer = new FileTransferServiceImpl();
					final String disconnectDrive = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.DRIVE_MOUNT) + ":";
					fileTransfer.executeNetUseDisConnect(disconnectDrive);
				}
			} catch (DistributionException e) {
				LOGGER.error("DistributionException - Unable to Unmount drive ", e);
			}
		}
		LOGGER.debug("process Method of DistributeSDSApplication");
	}
	
	/**
	 * Sets the jobName for the current run
	 * @param mediaType
	 * @return
	 */
	private String setJobName(final int mediaType) {
		switch (mediaType) {
			case 1:
				jobName = DistributeSDSConstants.JOB_NAME_805;
				mediaTypeString = DistributeSDSConstants.MEDIA_TYPE_EMAIL;
				break;
			case 2:
				jobName = DistributeSDSConstants.JOB_NAME_806;
				mediaTypeString = DistributeSDSConstants.MEDIA_TYPE_FAX;
				break;
			case 3:
				jobName = DistributeSDSConstants.JOB_NAME_807;
				mediaTypeString = DistributeSDSConstants.MEDIA_TYPE_PAPER_MAIL;
				break;
			default:
				LOGGER.error("Check if correct mediaType is passed.");
				break;
		}
		return jobName;
	}
}
