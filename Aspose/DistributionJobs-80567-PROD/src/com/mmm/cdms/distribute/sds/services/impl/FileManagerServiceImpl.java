/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.services.FileManagerService;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;
import com.mmm.cdms.distribute.sds.utils.FileUtils;

/**
 * @author A5AK3ZZ
 *
 */
public class FileManagerServiceImpl implements FileManagerService {
	
	/**
	 * @param transferBean
	 */
	public FileManagerServiceImpl() {}
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(FileManagerServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileManagerService#
	 * archiveAndDeleteFiles(com.mmm.cdms.distribute.sds.beans.
	 * PdfDetailsDTO, java.lang.String, java.lang.String)
	 */
	public void archiveAndDeleteFiles(String homePath, String dataPath) {
		LOGGER.debug(" ********** BEGIN : ARCHIVE AND DELETE FILES METHOD *********** ");
		boolean isArchiveSucces;
		try {
			this.archiveFiles(homePath, dataPath);
			// set the archive success flag if all files are archived
			// successfully,false if not
			isArchiveSucces = DistributeSDSApplication.pdfDetailsDTO.isArchiveSuccess();
			if (isArchiveSucces) {
				// delete all the files if archive success
				this.deleteFiles(homePath);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException occured in archive/delete operation for : " + homePath, e);
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("FileNotFoundException occured in archive/delete operation : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("IOException occured in archive/delete operation : " + e.getMessage());
			LOGGER.error("IOException occured in archive/delete operation for : " + homePath, e);
			e.printStackTrace();
		} catch (Exception e) {
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("DistributionException occured in archive/delete operation  : " + e.getMessage());
			LOGGER.error("DistributionException occured in archive/delete operation for : " + homePath, e);
			e.printStackTrace();
		}
		LOGGER.debug(" ********** END : ARCHIVE AND DELETE FILES METHOD *********** ");
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileManagerService#archiveFiles(com.mmm.cdms.distribute.sds.beans.PdfDetailsDTO, java.lang.String, java.lang.String)
	 */
	public void archiveFiles(String homePath, String dataPath)
			throws FileNotFoundException, IOException {
		LOGGER.debug("********* BEGIN : ARCHIVING FILES FOR :" + homePath + " *********** ");
		boolean isDocFolderExist = false;
		FileTransferServiceImpl filesTransfer = new FileTransferServiceImpl();
		try {
			isDocFolderExist = FileUtils.validateDir(dataPath);
			// Generate documents only if the given output folder location
			// is valid
			if (isDocFolderExist) {
				String archivePath = formArchivePath(dataPath);
				if (archivePath != null) {
					// transfer all files from all the folders of source
					// data path to the passed archive directory
					filesTransfer.archiveFiles(homePath, archivePath);
				}
			}
		} catch (FileNotFoundException e) {
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("File Not Found DistributionException in Archive opeartion, the exception is : " + e.getMessage());
			LOGGER.error("File Not Found DistributionException in Archive operation", e);
			DistributeSDSApplication.pdfDetailsDTO.setArchiveSuccess(false);
		} catch (IOException e) {
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("IOException in Archive operation ");
			LOGGER.error("DistributionException occurred in archival operation", e);
			DistributeSDSApplication.pdfDetailsDTO.setArchiveSuccess(false);
		}
		LOGGER.debug("********* END : ARCHIVING FILES FOR :" + homePath + " *********** ");
		if (DistributeSDSApplication.pdfDetailsDTO.isArchiveSuccess()) {
			DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.PROCESS_STATUS_ARCHIVED);
		}
	}
		
	/**
	 * Create the archive path specific to each country
	 * 
	 * @param strDataPath
	 * @return
	 */
	private String formArchivePath(String dataPath) {
		LOGGER.debug("********* BEGIN : FORMING ARCHIVE PATH FOR :" + dataPath + " *********** ");
		String archivePathFinal = null;
		String archivePath = dataPath + DistributeSDSConstants.ARCHIVE_FOLDER_NAME + DistributeSDSConstants.FILE_SEPERATOR;
		// checks if the directory exists . creates if not exists.
		if (FileUtils.checkAndCreateDir(archivePath)) {
			// add the current Date as a sub folder
			String dateFormat = DistributeSDSUtils.getCurrentDateTime();
			archivePathFinal = archivePath + dateFormat + DistributeSDSConstants.FILE_SEPERATOR;
			// create sub folder for a day , return null if path is invalid
			if (!FileUtils.checkAndCreateDir(archivePathFinal)) {
				archivePathFinal = null;
			}
		}
		LOGGER.debug("********* END : FORMING ARCHIVE PATH FOR :" + dataPath + " *********** ");
		return archivePathFinal;
	}
	
	/**
	 * Deletes the generated files from the country directory , once archive is complete
	 * 
	 * @param transferBean
	 * @param homePath
	 * @return
	 */
	private void deleteFiles(String homePath) {
		boolean isDeleteSuccess = true;
		int deletedDocsCount = 0;
		LOGGER.debug("DELETING FILES FROM DATA DIRECTORY AFTER ARCHIVING FOR : " + homePath + "***********");
		if (FileUtils.validateDir(homePath)) {
			File sourceDir = new File(homePath);
			if (sourceDir != null && sourceDir.isDirectory()) {
				// Get the list of fileNames in the directory
				File files[] = sourceDir.listFiles();
				if (files != null && files.length > 0) {
					for (int count = 0; count <= files.length - 1; count++) {
						File sourceFile = files[count];
						if (DistributeSDSConstants.ARCHIVE_FOLDER_NAME.equalsIgnoreCase(sourceFile.getName())
								|| DistributeSDSConstants.ERROR_DATA_FOLDER_NAME.equalsIgnoreCase(sourceFile.getName())) {
							LOGGER.debug("Ignored processing the directory :" + sourceFile);
							continue;
						}
						LOGGER.debug("Deleting the file : " + sourceFile.getName());
						boolean isDeleteFileSuccess = false;
						if (sourceFile.exists()) {
							isDeleteFileSuccess = sourceFile.delete();
						} else {
							LOGGER.debug("Error in Deleting the file : File doesn't exist :" + sourceFile.getName() + " from the share : " + sourceDir);
						}
						if (isDeleteFileSuccess) {
							deletedDocsCount = deletedDocsCount + 1;
							LOGGER.debug("Deleted the file successfully : " + sourceFile.getName() + " from the share : " + sourceDir);
						} else {
							isDeleteSuccess = false;
							LOGGER.error("Error in Deleting the file : " + sourceFile.getName() + " from the share : " + sourceDir);
							DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("DELETE Error : File : " + sourceFile.getName() + " from the share : " + sourceDir);
						}
					}
				}
			}
		} else {
			isDeleteSuccess = false;
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("Error in deleting files from the data directory. Data Directory does not exist for : " + homePath);
		}
		DistributeSDSApplication.pdfDetailsDTO.setDeletedDocsCount(deletedDocsCount);
		LOGGER.debug("DELETED " + deletedDocsCount + "documents for : " + homePath);
		// set the archive success flag if all files are archived
		// successfully,false if not
		DistributeSDSApplication.pdfDetailsDTO.setDeleteSuccess(isDeleteSuccess);
		if (isDeleteSuccess) {
			DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("");
			DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.PROCESS_STATUS_SUCCESS);
		}
		LOGGER.debug("DELETING FILES FROM DATA DIRECTORY AFTER ARCHIVING FOR : " + homePath + "***********");
	}
	
}
