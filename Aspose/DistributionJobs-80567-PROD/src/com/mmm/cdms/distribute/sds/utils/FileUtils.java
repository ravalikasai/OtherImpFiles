/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author A5AK3ZZ
 *
 */
public class FileUtils {
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(FileUtils.class);
	
	/**
	 * validates output folder path to generate pdf documents. creates if not
	 * exists.
	 * 
	 * @param transferLocation
	 * @return
	 */
	public static boolean checkAndCreateDir(String transferLocation) {
		LOGGER.debug(" BEGIN : checkAndCreateDir METHOD FOR : " + transferLocation);
		boolean isOutputFolderCreated = false;
		// creating output folder to generate pdf documents
		if (null != transferLocation && transferLocation.length() > 0) {
			File outputFileLoc = new File(transferLocation);
			if (outputFileLoc != null && outputFileLoc.exists() && outputFileLoc.isDirectory()) {
				isOutputFolderCreated = true;
			} else {
				isOutputFolderCreated = outputFileLoc.mkdirs();
				isOutputFolderCreated = true;
			}
		}
		LOGGER.debug(" END : checkAndCreateDir METHOD FOR : " + transferLocation);
		return isOutputFolderCreated;
	}
	
	/**
	 * validates output folder path to generate pdf documents. creates if not
	 * exists.
	 * 
	 * @param transferLocation
	 * @return
	 */
	public static boolean validateDir(final String dirLocation) {
		LOGGER.debug("BEGIN : validateDir METHOD FOR : " + dirLocation);
		boolean isOutputFolderCreated = false;
		// creating output folder to generate pdf documents
		if (StringUtils.isNotBlank(dirLocation)) {
			File outputFileLoc = new File(dirLocation);
			if (outputFileLoc != null && outputFileLoc.exists()) {
				isOutputFolderCreated = true;
			}
		}
		LOGGER.debug(" END : validateDir METHOD FOR : " + dirLocation);
		return isOutputFolderCreated;
	}
	
}
