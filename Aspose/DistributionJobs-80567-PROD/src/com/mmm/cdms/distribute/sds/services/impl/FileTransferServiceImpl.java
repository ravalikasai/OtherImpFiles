/**

 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.beans.MediaSettingsBean;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.services.FileTransferService;
import com.mmm.cdms.distribute.sds.utils.FileUtils;
import com.mmm.cdms.distribute.sds.utils.StreamGobbler;
import com.mmm.cdms.exceptions.DistributionException;

/**
 *
 * Holds methods to manage file transfer to FTP/Share locations.
*/
public class FileTransferServiceImpl implements FileTransferService {
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(FileTransferServiceImpl.class);
	
	/**
	 * Holds the string that indicated the remote file transfer
	 */
	private static final String FILE_TRANSFER_OPERATION = "FILE_TRANSFER";
	
	/**
	 * Holds the string that indicated the remote file transfer
	 */
	private static final String ARCHIVE_OPERATION = "ARCHIVE";
	
	private static String zipfilePath; 
	
	/**
	 * String constant representing server
	 */
	public String server;
	
	/**
	 * String constant representing server password
	 */
	private String transferShareLocation;
	
	/**
	 * Default constructor
	 * 
	 */
	public FileTransferServiceImpl() {}
	
	/**
	 * constructor which initializes the FTP connection parameters
	 * 
	 * @param server
	 * @param userName
	 * @param password
	 * @param sharedLoc
	 */
	public FileTransferServiceImpl(String server, String sharedLoc) {
		this.server = server;
		this.transferShareLocation = sharedLoc;
	}
	
	/**
	 * constructor which initializes the File_Share connection parameters
	 * 
	 * @param userName
	 * @param password
	 * @param sharedLoc
	 */
	public FileTransferServiceImpl(String sharedLoc) {
		this.transferShareLocation = sharedLoc;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#transferFiles(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean transferFiles(String fileName, String driveName, String transfer_Mode)
			throws SocketException, IOException {
		LOGGER.debug("BEGIN : FileTransferServiceImpl:transferFiles method for: " + fileName + transfer_Mode);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean isFileTrnsfrSuccess = true;
		boolean isTransferSuccess = false;
		boolean isDriveConnectSuccess = false;
		if (StringUtils.isNotBlank(transferShareLocation) || ARCHIVE_OPERATION.equalsIgnoreCase(transfer_Mode)) {
			try {
				// checks if the transfer is the local/remote share path
				if (FILE_TRANSFER_OPERATION.equals(transfer_Mode)) {
					isDriveConnectSuccess = true; // already mounted
				} else if (ARCHIVE_OPERATION.equals(transfer_Mode)) {
					isDriveConnectSuccess = true;
					transferShareLocation = driveName;
				}
				if (isDriveConnectSuccess) {
					isTransferSuccess = true;
					if (StringUtils.isNotBlank(fileName) && (fileName.contains(".pdf"))) {
						File file = new File(fileName);
						if (file.exists()) {
							fis = new FileInputStream(fileName);
							if (FILE_TRANSFER_OPERATION.equals(transfer_Mode)) {
								fos = new FileOutputStream(new File(driveName + ":\\\\" + file.getName()));
							} else {
								fos = new FileOutputStream(new File(driveName + "\\" + file.getName()));
							}
							LOGGER.debug("Start transferring the file : " + fileName + " to Share_location : "
									+ transferShareLocation);
							isTransferSuccess = this.copyFile(fis, fos);
							if (isTransferSuccess) {
								LOGGER.debug("Transferred the file : " + fileName + " to Share_location : "
										+ transferShareLocation);
							} else {
								isFileTrnsfrSuccess = false;
								LOGGER.error("Error in Transferring the following file to Share_location : " + fileName);
							}
						} else {
							LOGGER.error("Skipped Errored file " + fileName);
						}
					} else {
						LOGGER.error(" Invalid file : " + fileName);
					}
				} else {
					isFileTrnsfrSuccess = false;
					LOGGER.error(" Error in connecting to the file_share using NET USE : " + transferShareLocation
							+ " . Files cannot be moved ");
					
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("File Not Found Exception Exception occurred in file transfer operation is : ", e);
				e.printStackTrace();
				isFileTrnsfrSuccess = false;
			} catch (IOException e) {
				LOGGER.error("IOException occured in file copy to share operation ", e);
				e.printStackTrace();
				isFileTrnsfrSuccess = false;
			} catch (Exception e) {
				LOGGER.error("Exception occured in file copy to share operation ", e);
				e.printStackTrace();
				isFileTrnsfrSuccess = false;
			} finally {
				if (isDriveConnectSuccess && FILE_TRANSFER_OPERATION.equals(transfer_Mode)) {
					// this.executeNetUseDisConnect(drive_Name + ":");
				}
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			}
		} else {
			LOGGER.error("FTP CONNECTION Parameters missing, check the connection parameters in database");
		}
		LOGGER.debug("END : FileTransferServiceImpl:transferFiles method for: " + fileName + transfer_Mode);
		return isFileTrnsfrSuccess;
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#archiveFiles(java.lang.String, java.lang.String)
	 */
	public boolean archiveFiles(String absoluteDir, String archivePath) throws SocketException, IOException {
		LOGGER.debug("BEGIN : FileTransferServiceImpl:archiveFiles method");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean isFileTrnsfrSuccess = true;
		int docsCount = 0;
		// flag that holds the file transfer success/failure value
		boolean isSuccess = false;
		if (StringUtils.isNotBlank(absoluteDir)) {
			try {
				isSuccess = true;
				File sourceDir = new File(absoluteDir);
				// Get the list of fileNames in the directory
				String files[] = sourceDir.list();
				LOGGER.info("processing Archival of the files");
				for (String fileName : files) {
					if (DistributeSDSConstants.ARCHIVE_FOLDER_NAME.equalsIgnoreCase(fileName)
							|| DistributeSDSConstants.ERROR_DATA_FOLDER_NAME.equalsIgnoreCase(fileName)) {
						LOGGER.debug("Ignored processing the directory :" + fileName);
						continue;
					} else if (fileName.contains(DistributeSDSConstants.US_PAPER_MAIL_FILE_NAME) ||
							fileName.contains(zipfilePath)) {
						LOGGER.debug("Ignored processing the US PDF and ZIP Files" + fileName);
						continue;
					}
					if (StringUtils.isNotBlank(fileName)) {
						File fileToArchive = new File(fileName);
						String absoluteFilePath = absoluteDir + DistributeSDSConstants.FILE_SEPERATOR + fileName;
						fis = new FileInputStream(absoluteFilePath);
						// local drive_Name will have the absolute drive path
						// already
						fos = new FileOutputStream(new File(archivePath + "\\" + fileToArchive.getName()));
						LOGGER.debug(
								"Start transferring the file : " + fileName + " to Archive Location : " + archivePath);
						isSuccess = this.copyFile(fis, fos);
						docsCount = docsCount + 1;
						if (isSuccess) {
							LOGGER.debug("Transferred the file : " + fileName + " to Archive Location : " + archivePath);
						} else {
							isFileTrnsfrSuccess = false;
							LOGGER.error("Error in Transferring the following file to Archive Location : " + absoluteFilePath);
						}
					} else {
						LOGGER.error(" Invalid file : " + fileName);
					}
				}
			} catch (FileNotFoundException e) {
				isFileTrnsfrSuccess = false;
				LOGGER.error("File Not Found Exception Exception occurred in file transfer operation is : ", e);
				throw e;
			} catch (IOException e) {
				isFileTrnsfrSuccess = false;
				LOGGER.error("IOException occured in file copy to share operation ", e);
				throw e;
			} finally {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			}
		} else {
			LOGGER.error("Archive CONNECTION Parameters missing, check the connection parameters in database");
		}
		DistributeSDSApplication.pdfDetailsDTO.setArchiveSuccess(isFileTrnsfrSuccess);
		DistributeSDSApplication.pdfDetailsDTO.setArchivedDocsCount(docsCount);
		LOGGER.debug("END : FileTransferServiceImpl:archiveFiles method");
		return isFileTrnsfrSuccess;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#executeNetUseConnect(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.mmm.cdms.distribute.sds.beans.PdfDetailsDTO)
	 */
	public boolean executeNetUseConnect(String sharePath, String userName, String password,	String drive_Name) throws DistributionException {
		LOGGER.debug("FileTransferServiceImpl:executeNetUseConnect method ");
		LOGGER.debug("Connecting the File share using NET Use:  FILE_SHARE - " + sharePath + "  USER_NAME : " + userName
				+ "  PASSWORD : ***********  DRIVE_NAME :" + drive_Name);
		boolean isSuccess = false;
		String command = null;
		File driveMount = null;
		try { // Execute a command without arguments
			driveMount = new File(drive_Name + ":");
			if (driveMount != null && driveMount.isDirectory() && driveMount.exists()) {
				LOGGER.error("Error in mounting the drive with the passed Drive Name.DriveName already in use : " + drive_Name);
			} else {
				if (userName != null && userName.length() > 0 && password != null && password.length() > 0) {
					command = "cmd /c net use " + drive_Name + ": " + sharePath + " /u:" + userName + " " + password;
				} else {
					command = "cmd /c net use " + drive_Name + ": " + sharePath;
				}
				isSuccess = executeCmd(command);
				File driveDest = new File(drive_Name + ":");
				// check if the drive has been created
				if (driveDest != null && driveDest.isDirectory() && driveDest.exists()) {
					isSuccess = true;
				} else {
					LOGGER.error("Error in mounting the share drive by executing the command : " + command);
				}
			}
		} catch (IOException e) {
			isSuccess = false;
			LOGGER.error("Exception in connecting to the File_share using the command : " + command, e);
			throw new DistributionException("Exception in connecting to the File_share ", e);
		} catch (InterruptedException e) {
			LOGGER.error("Error occured in executeNetUseConnect", e);
			throw new DistributionException("Error occured in executeNetUseConnect", e);
		}
		LOGGER.debug("Connecting the File share using NET Use:  FILE_SHARE - " + sharePath + "  USER_NAME : " + userName
				+ "  PASSWORD : " + password + "  DRIVE_NAME :" + drive_Name);
		LOGGER.debug("FileTransferServiceImpl:executeNetUseConnect method ");
		DistributeSDSApplication.pdfDetailsDTO.setMountSuccess(isSuccess);
		DistributeSDSApplication.pdfDetailsDTO.setDriveConnectSuccess(isSuccess);
		if (isSuccess) {
			DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.PROCESS_STATUS_MOUNTED);
		}
		return isSuccess;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#executeNetUseDisConnect(java.lang.String)
	 */
	public boolean executeNetUseDisConnect(final String driveName)
			throws DistributionException {
		LOGGER.debug("BEGIN : FileTransferServiceImpl:executeNetUseDisConnect method ");
		boolean isDisconnected = false;
		try {
			// Execute a command without arguments
			String command = "cmd /c net use " + driveName + " /delete";
			isDisconnected = executeCmd(command);
			File driveDest = new File(driveName + ":");
			// check if the drive has been created
			if (driveDest != null && driveDest.isDirectory() && driveDest.exists()) {
				LOGGER.error("Error in UnMounting the share drive by executing the command : " + command);
				isDisconnected = false;
			} else {
				isDisconnected = true;
			}
		} catch (IOException e) {
			isDisconnected = false;
			LOGGER.error("IOException in disconnecting the File Share : " + driveName +" - " + e.getMessage());
			throw new DistributionException("IOException in disconnecting the File Share", e);
		} catch (InterruptedException e) {
			isDisconnected = false;
			LOGGER.error("InterruptedException in disconnecting the File Share : " + driveName +" - " + e.getMessage());
			throw new DistributionException("InterruptedException in disconnecting the File Share", e);
		} catch (DistributionException e) {
			isDisconnected = false;
			LOGGER.error("DistributionException in disconnecting the File Share : " + driveName +" - " + e.getMessage());
			throw new DistributionException("DistributionException in disconnecting the File Share", e);
		}
		LOGGER.debug("END : DisConnecting the File share using NET Use: " + driveName);
		LOGGER.debug("END : FileTransferServiceImpl:executeNetUseDisConnect method ");
		return isDisconnected;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#copyFile(java.io.FileInputStream, java.io.FileOutputStream)
	 */
	public boolean copyFile(FileInputStream fis, FileOutputStream fos) throws IOException {
		LOGGER.debug("BEGIN : FileTransferServiceImpl:copyFile method - Copying files to the File Share ");
		FileChannel in = null;
		FileChannel out = null;
		boolean isSuccess = false;
		try {
			in = fis.getChannel();
			out = fos.getChannel();
			in.transferTo(0, in.size(), out);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("IOException occured in copying the fileInputStream : In copyFile Method : " + e);
			isSuccess = false;
			throw e;
		} finally {
			if (in != null) {
				in.close();
				fis.close();
			}
			if (out != null) {
				out.close();
				fos.close();
			}
		}
		LOGGER.debug("END : FileTransferServiceImpl:copyFile method - Copying files to the File Share ");
		return isSuccess;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#moveErrorFiles(java.lang.String, java.lang.String)
	 */
	public boolean moveErrorFiles(String localDataPath, String errorDataPath, String dateTime) throws IOException {
		LOGGER.debug("BEGIN : moveErrorFiles");
		File srcZipFile = new File(localDataPath + "\\POSTAL-" + dateTime + ".zip");
		zipfilePath = srcZipFile.getAbsolutePath();
		boolean isSuccess = true;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		LOGGER.debug("checking data directory if any files exist from previous batch run and move it to error folder if any");
		// check if the data path is a valid directory
		if (FileUtils.validateDir(localDataPath)) {
			File dataDir = new File(localDataPath);
			// Iterate all the country sub folders to check if any file exists
			// from previous run
			FileTransferServiceImpl fileTransferrer = new FileTransferServiceImpl();
			for (File file : dataDir.listFiles()) {
				String fileName = file.getName();
				// check if the file object is a folder
				if (file != null && FileUtils.validateDir(file.getAbsolutePath())) {
					if (DistributeSDSConstants.ARCHIVE_FOLDER_NAME.equalsIgnoreCase(fileName)
							|| DistributeSDSConstants.ERROR_DATA_FOLDER_NAME.equalsIgnoreCase(fileName)
							|| DistributeSDSConstants.THUMBSDB_FILE_NAME.equalsIgnoreCase(fileName)) {
						LOGGER.debug("Ignored processing the directory/file :" + file.getAbsolutePath());
					}  else if (fileName.contains(DistributeSDSConstants.US_PAPER_MAIL_FILE_NAME) ||
							fileName.contains(DistributeSDSConstants.US_EMAIL_FILE_NAME) ||
							fileName.contains(DistributeSDSConstants.US_FAX_FILE_NAME) ||
							fileName.contains(zipfilePath)) {
						LOGGER.debug("Ignored processing the US PDF and ZIP Files" + fileName);
						isSuccess = file.delete();
						if (!isSuccess) {
							LOGGER.error("Error in deleting the file from the data folder : " + file.getAbsolutePath());
						} else {
							LOGGER.debug("Deleted the file  : " + fileName + " succesfully !!");
						}
					} else {
						LOGGER.debug("--------Checking the file :" + file.getAbsolutePath() + "--------");
						// checks if the error directory is valid and create if
						// not exists
						if (FileUtils.checkAndCreateDir(errorDataPath)) {
							String outputErrorFile = errorDataPath + DistributeSDSConstants.FILE_SEPERATOR + fileName;
							try {
								fis = new FileInputStream(file);
								fos = new FileOutputStream(new File(outputErrorFile));
								LOGGER.debug("Start transferring the Error file : " + fileName + " to Share_location : "
										+ outputErrorFile);
								isSuccess = fileTransferrer.copyFile(fis, fos);
								if (isSuccess) {
									LOGGER.debug("Transferred the Error file : " + fileName
											+ " successfully to Share_location : " + outputErrorFile);
									// delete the error file from the data
									// folder
									isSuccess = file.delete();
									if (!isSuccess) {
										LOGGER.error(
												"Error in deleting the file from the data folder : " + file.getAbsolutePath());
									} else {
										LOGGER.debug("Deleted the file  : " + fileName
												+ " succesfully from the data folder : " + file.getAbsolutePath());
									}
								} else {
									LOGGER.error("Error/Exception occured in Transferring the Error file : " + file.getName()
											+ " successfully to Share_location : ");
								}
							} catch (FileNotFoundException e) {
								isSuccess = false;
								e.printStackTrace();
								LOGGER.error("FileNotFoundException occurred while clearing the error file : " + file.getName(),
										e);
							} catch (IOException e) {
								isSuccess = false;
								e.printStackTrace();
								LOGGER.error("IOException occurred while clearing the error file : " + file.getName(), e);
							} finally {
								if (fis != null) {
									fis.close();
								}
								if (fos != null) {
									fos.close();
								}
							}
						} else {
							LOGGER.error("--------Invalid error data path  :" + errorDataPath + "----");
							isSuccess = false;
						}
					}
				} else {
					LOGGER.debug("--------No Files exist in the country folder :" + file.getAbsolutePath() + "--------");
				}
			}
		}
		LOGGER.debug("** END : CHECKING DATA DIRECTORY IF ANY FILES EXIST FROM PREVIOUS BATCH RUN AND MOVE IT TO ERROR "
				+ "FOLDER IF ANY *******");
		DistributeSDSApplication.pdfDetailsDTO.setClearDataFolderSuccess(isSuccess);
		DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(DistributeSDSConstants.CLEAR_DATA_FOLDER_SUCCESS);
		LOGGER.debug("END : moveErrorFiles");
		LOGGER.info("Files in the packages folder moved to ERROR folder!!");
		return isSuccess;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.FileTransferService#processUSFilesTransfer(com.mmm.cdms.distribute.sds.beans.SysOptionBean, com.mmm.cdms.distribute.sds.beans.PdfDetailsDTO, com.mmm.cdms.distribute.sds.beans.MediaSettingsBean, com.mmm.cdms.distribute.sds.beans.DistributionQueueBean)
	 */
	public String processUSFilesTransfer(List<String> usFileList, SysOptionBean sysOption, MediaSettingsBean mediaSettings) throws IOException, InterruptedException, DistributionException {
		boolean isCopySuccess = true;
		File srcZipFile = new File(zipfilePath);
		File destFTPDir = new File(sysOption.getPrinterFtpDir());
		int mediaTypeCode = DistributeSDSApplication.mediaType;
		if (DistributeSDSApplication.pdfDetailsDTO.getBatchPackageCount() > 0 && mediaTypeCode == 3) {
			
			LOGGER.debug("zipping the pdf and freport file");
			this.zipFileList(srcZipFile, sysOption, mediaSettings, usFileList);
			
			LOGGER.debug("copying the zipped file to ftp dir");
			FileInputStream fis = new FileInputStream(srcZipFile);
			// local drive_Name will have the absolute drive path already
			File zipFileToCopy = new File(destFTPDir.getAbsolutePath() + "\\POSTAL-" + sysOption.getDateTime() + ".zip");
			FileOutputStream fos = new FileOutputStream(zipFileToCopy);
			isCopySuccess = this.copyFile(fis,fos);
			
			LOGGER.debug("Preparing for ftp process");
			sendFilesOverFTP(zipFileToCopy, sysOption);
			
			LOGGER.debug("Copying to Archive directory in case of errors");
			if (!isCopySuccess) {
				File fArchtoLocal = new File(sysOption.getHomePath() + "\\PAPERMAIL_ARCHIVE\\" + mediaSettings.getReportDate());
				if (!fArchtoLocal.exists())
					fArchtoLocal.mkdirs();
				this.copyFile(fis, fos);
			}
		}
		return zipfilePath;
	}
	
	/**
	 * Method to send files over sftp
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean sendFilesOverFTP(File zipFileToCopy, SysOptionBean sysOption) {
		LOGGER.info("=================sendFilesOverFTP() started=================");
		String sftpHost = sysOption.getPrinterFtpAddr();
		int sftpPort = 22;
		String sftpUser = sysOption.getPrinterFtpUserid();
		String sftpPwd = sysOption.getPrinterFtpPwd();
		String sftpWorkingDir = sysOption.getPrinterFtpDestDir();
		
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		boolean blTransflag = true;
		LOGGER.info("preparing the host information for sftp.");
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			session.setPassword(sftpPwd);
			// session.setTimeout(20000);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LOGGER.info("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			LOGGER.info("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(sftpWorkingDir);
			File f = new File(zipFileToCopy.getAbsolutePath());
			channelSftp.put(new FileInputStream(f), f.getName());
			LOGGER.info("File transfered successfully to host.");
		} catch (Exception ex) {
			blTransflag = false;
			LOGGER.error("Exception found while tranfer the response.");
			ex.printStackTrace();
			return blTransflag;
		} finally {
			if (channelSftp != null) {
				channelSftp.exit();
				LOGGER.info("sftp Channel exited.");
				channel.disconnect();
				LOGGER.info("Channel disconnected.");
				session.disconnect();
				LOGGER.info("Host Session disconnected.");
			}
		}
		LOGGER.info("=================sendFilesOverFTP() completed=================");
		return blTransflag;
	}
	
	/**
	 * zipping the files starting with stPDFFileName or stReportFileName in a
	 * directory
	 * @param zipFileName
	 * @param sysOptions
	 * @param mediaSettings
	 * @param stPDFFileName
	 */
	private void zipFileList(File zipFileName, SysOptionBean sysOptions, MediaSettingsBean mediaSettings, List<String> usFileList) {
		LOGGER.debug("BEGIN : zipFileList method ");
		try {
			// create byte buffer
			byte[] buffer = new byte[1024];
			FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String usFileName : usFileList) {
				File fileToZip = new File(sysOptions.getHomePath() + usFileName);
				FileInputStream fis = new FileInputStream(fileToZip);
				// begin writing a new ZIP entry, positions the stream to
				// the start of the entry data
				zos.putNextEntry(new ZipEntry(fileToZip.getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
				// close the InputStream
				fis.close();
			}
			// close the ZipOutputStream
			zos.close();
		} catch (IOException ioe) {
			LOGGER.error("Error creating zip file" + ioe);
		}
		LOGGER.debug("END : zipFileList method ");
	}
	
	/**
	 * Executes the given command
	 * 
	 * @param cmd
	 *            Command to be executed
	 * @return <boolean>true</boolean>Command executed successfully
	 *         <boolean>false</boolean>Problem occured in command execution
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws DistributionException 
	 * @throws DistributionException
	 */
	private boolean executeCmd(String cmd) throws IOException, InterruptedException, DistributionException {
		LOGGER.debug("BEGIN : FileTransferServiceImpl:executeCmd method");
		int exitValue = 0;
		// Getting the runtime to execute cmd
		try {
			Runtime runtime = Runtime.getRuntime();
			LOGGER.debug("Command Execute:\n" + cmd);
			Process process = runtime.exec(cmd);
			// Setting the output and error stream destinations
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT", "LOGINFO");
			StreamGobbler errorGobller = new StreamGobbler(process.getErrorStream(), "ERROR", "LOGERROR");
			outputGobbler.start();
			errorGobller.start();
			process.waitFor();
			// Getting the return code from the cmd
			exitValue = process.exitValue();
			if (exitValue == 0) {
				LOGGER.debug("Cmd execution status SUCCESS: " + cmd);
			} else {
				throw new DistributionException(cmd + "Error code returned from cmd:" + exitValue);
			}
		} catch (IOException e) {
			LOGGER.error("IOException occurred while executing CMD command method : " + e.getMessage());
			throw e;
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException occurred while executing CMD command method : " + e.getMessage());
			throw e;
		} catch (DistributionException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		LOGGER.debug("END : FileTransferServiceImpl:executeCmd method ");
		return true;
	}
	
}
