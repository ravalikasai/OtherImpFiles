/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import com.mmm.cdms.distribute.sds.beans.MediaSettingsBean;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * @author A5AK3ZZ
 *
 */
public interface FileTransferService {
	
	public boolean transferFiles(String fileName, String driveName, String transfer_Mode) 
			throws SocketException, IOException;
	
	/**
	 * Transfer files to the file share. Used to transfer files ARCHIVE
	 * operations
	 * 
	 * @param absolute_Dir
	 * @param drive_Name
	 *            localDrive to be mounted to link the remote file share for
	 *            file transfer
	 * @param transfer_Mode
	 *            holds "local" for local file transfer , "Remote" if its an
	 *            remote windows directory
	 * @return CountryReportVO
	 * @throws SocketException
	 * @throws IOException
	 */
	public boolean archiveFiles(String absoluteDir, String driveName) throws SocketException, IOException;
	
	/**
	 * Checks the data directory at the start of batch run. If any file has been
	 * left from archive/delete operation of previous run , all these files will
	 * be moved in error folder in the data directory .
	 * 
	 * 
	 * @param transferBean
	 * @param localDataPath
	 * @param errorDataPath
	 * @return
	 * @throws IOException
	 */
	boolean moveErrorFiles(String localDataPath, String errorDataPath, String dateTime) throws IOException;
	
	/**
	 * Executes the net use command to link / mount the drive with the file
	 * share path
	 * 
	 * @param sharePath
	 * @param userName
	 * @param password
	 * @param drive_Name
	 * @return true if mounting the given drive is possible and valid, false if
	 *         not
	 * @throws InterruptedException 
	 * @throws IOException
	 */
	public boolean executeNetUseConnect(String sharePath, String userName, String password, String drive_Name)
			throws DistributionException, IOException, InterruptedException;
	
	/**
	 * Executes the net use command to disconnect the share path mapped as local
	 * drive
	 * 
	 * @param driveName
	 * @return
	 * @throws IOException
	 * @throws DistributionException
	 * @throws InterruptedException
	 */
	public boolean executeNetUseDisConnect(final String driveName)
			throws IOException, InterruptedException, DistributionException;
	
	/**
	 * Transfer files to the destination location
	 * 
	 * @param fis
	 * @param fos
	 * @return true if the file transfer is success , false if not
	 * @throws IOException
	 */
	public boolean copyFile(FileInputStream fis, FileOutputStream fos) throws IOException;
	
	/**
	 * @param usFileList
	 * @param sysOption
	 * @param mediaSettings
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DistributionException
	 */
	public String processUSFilesTransfer(List<String> usFileList, SysOptionBean sysOption, MediaSettingsBean mediaSettings)
			throws IOException, InterruptedException, DistributionException;
	
}
