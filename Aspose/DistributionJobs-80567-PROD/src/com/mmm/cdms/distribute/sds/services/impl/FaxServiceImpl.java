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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.services.FaxService;
import com.mmm.cdms.distribute.sds.utils.StreamGobbler;


/**
 * @author a5ak3zz
 *
 */
public class FaxServiceImpl implements FaxService {
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(FaxServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.serivce.mediatype.FaxService#configFaxSend(com.mmm.cdms.distribute.sds.beans.DistributionQueueBean, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void configFaxSend(DistributionQueueBean dqBean, int batchPackageCount, String homePath,
			String fileName, SysOptionBean sysOption) {
		LOGGER.debug("BEGIN : configFaxSend");
//		if (batchPackageCount == 0) {
			try {
				LOGGER.debug("create Fax Header file");
				createFaxHeaderFile(homePath, fileName);
				
				LOGGER.debug("create Fax Batch file");
				createFaxBatchFile(homePath, fileName, sysOption);
				
				LOGGER.debug("create Fax Footer file");
				createFaxFooterFile(dqBean, sysOption);
				
				LOGGER.debug("process fax sending");
				this.sendFax(dqBean, sysOption);
				
				this.deleteFaxFiles(homePath);
			} catch (Exception ex) {
				ex.printStackTrace();
				LOGGER.error(ex, ex);
			}
//		}
		LOGGER.debug("END : configFaxSend");
	}

	/**
	 * @param homePath
	 * @param fileName
	 * @param sysOption
	 * @throws IOException
	 */
	private void createFaxBatchFile(String homePath, String fileName, SysOptionBean sysOption)
			throws IOException {
		File faxBatchFile = new File(homePath + File.separator + "FaxBatch.cmd");
		if (!faxBatchFile.exists()) {
			faxBatchFile.createNewFile();
		} else {
			faxBatchFile.delete();
			faxBatchFile.createNewFile();
		}
		FileWriter batchFW = new FileWriter(faxBatchFile.getAbsoluteFile());
		BufferedWriter batchBW = new BufferedWriter(batchFW);
		batchBW.write("copy " + homePath + File.separator + "FaxHeader.txt + /B " + fileName + ".pdf + " + homePath + File.separator + "FaxFooter.txt " + homePath + File.separator
				+ "FaxSend.txt");
		batchBW.newLine();
		batchBW.write("copy " + homePath + File.separator + "FaxSend.txt " + sysOption.getFaxQueue());
		batchBW.close();
		batchFW.close();
	}

	/**
	 * @param homePath
	 * @param fileName
	 * @throws IOException
	 */
	private void createFaxHeaderFile(String homePath, String fileName) throws IOException {
		File faxHeaderFile = new File(homePath + File.separator + "FaxHeader.txt");
		if (!faxHeaderFile.exists()) {
			faxHeaderFile.createNewFile();
		} else {
			faxHeaderFile.delete();
			faxHeaderFile.createNewFile();
		}
		FileWriter headerFW = new FileWriter(faxHeaderFile.getAbsoluteFile());
		BufferedWriter headerBW = new BufferedWriter(headerFW);
		headerBW.write("{{begin}}");
		headerBW.write("{{begincvt " + fileName +".pdf}}");
		headerBW.close();
		headerFW.close();
	}
	
	/**
	 * Delete files after FAX sending
	 * 
	 * @param homePath
	 */
	private void deleteFaxFiles(String homePath) {
		boolean status = false;
		File folder = new File(homePath);
		// delete Fax related files after fax is sent
		for(String file : folder.list()) {
			if (file.contains("Fax") && (file.contains(".txt") || file.contains(".cmd"))) {
				LOGGER.debug("Trying to delete the file : " + file);
				status = new File(homePath + DistributeSDSConstants.FILE_SEPERATOR + file).delete();
				LOGGER.debug("Above file-deletion-status : " + status);
			}
		}
	}

	/**
	 * Handles the FAX sending variables and sends the FAX to the FAX number
	 * using RIGHTFAX server
	 * 
	 * @param dqBean
	 */
	private void sendFax(DistributionQueueBean dqBean, SysOptionBean sysOption) {
		LOGGER.debug("sendFax Entry :  ");
		int exitVal = 0;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(sysOption.getHomePath() + File.separator + "FaxBatch.cmd");
			// Setting the output and error stream destinations
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT", "LOGINFO");
			StreamGobbler errorGobller = new StreamGobbler(process.getErrorStream(), "ERROR", "LOGERROR");
			outputGobbler.start();
			errorGobller.start();
			process.waitFor();
			// Getting the return code from the cmd
			exitVal = process.exitValue();
		} catch (IOException e) {
			LOGGER.error("Error occured in sendFax", e);
		} catch (InterruptedException e) {
			LOGGER.error("Error occured in sendFax", e);
		}
		if (exitVal == 0) {
			LOGGER.debug("Cmd execution status SUCCESS: " + exitVal);
		} else {
			LOGGER.debug("Error code returned from cmd:" + exitVal);
		}
		LOGGER.debug("The Process Exit value is : " + exitVal);
		LOGGER.debug("sendFax Exit :  ");
	}

	/**
	 * @param dqBean
	 * @param sysOption
	 * @throws IOException
	 */
	private void createFaxFooterFile(DistributionQueueBean dqBean, SysOptionBean sysOption)
			throws IOException {
		File faxFooter = new File(sysOption.getHomePath() + File.separator + "FaxFooter.txt");
		if (!faxFooter.exists()) {
			faxFooter.createNewFile();
		}
		FileWriter footerFW = new FileWriter(faxFooter.getAbsoluteFile());
		BufferedWriter footerBW = new BufferedWriter(footerFW);
		footerBW.write("{{endcvt}}");
		footerBW.write("{{fax " + dqBean.getFaxNumber().trim() + "}}");
		footerBW.write("{{cover MSDS.pcl}}");
		footerBW.write("{{contact " + dqBean.getAttnName() + "}}");
		footerBW.write("{{company " + dqBean.getCompanyName() + "}}");
		if (StringUtils.isNotBlank(dqBean.getAdhocSenderName())) {
			footerBW.write("{{owner " + dqBean.getAdhocSenderName() + " - " + sysOption.getCorpName() + "}}");
		} else {
			footerBW.write("{{owner " + sysOption.getCorpName() + "}}");
		}
		footerBW.write("{{rti " + sysOption.getCorpName() + "}}");
		footerBW.write("{{CSI " + sysOption.getFaxReturnNumber() + "}}");
		footerBW.write("{{comment " + dqBean.getDocTypeCode() + "-" + dqBean.getDocGroupNum() + " ("
				+ dqBean.getDocTradeName() + ")}}");
		footerBW.write("{{User1 Sender#: " + dqBean.getAdhocSenderPhone() + "}}");
		footerBW.write("{{User2 CDMS DISTRIBUTION MODULE}}");
		footerBW.write("{{email " + sysOption.getFromEmailAddress() + "}}");
		footerBW.write("{{billing CDMS DISTRIBUTION MODULE}}");
		footerBW.write("{{billing2  00001832260}}");
		
		if (sysOption.hasFaxEmailNotify()) {
			footerBW.write("{{notifyhost mailsucc mailerr}}");
		} else {
			footerBW.write("{{notifyhost none mailerr}}");
		}
		
		footerBW.write("{{quality fine}}");
		footerBW.write("{{subject " + dqBean.getDocTypeCode() + "-" + dqBean.getDocGroupNum() + " ("
				+ dqBean.getDocTradeName() + ")}}");
		footerBW.write("{{COVERTEXT}}" + dqBean.getDocTypeCode() + "-" + dqBean.getDocGroupNum() + " ("
				+ dqBean.getDocTradeName() + ") {{ENDCOVERTEXT}}");
		footerBW.write("{{end}}");
		footerBW.close();
		footerFW.close();
	}
	
}
