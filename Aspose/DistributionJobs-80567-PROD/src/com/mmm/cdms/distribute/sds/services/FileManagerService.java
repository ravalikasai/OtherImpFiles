/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author A5AK3ZZ
 *
 */
public interface FileManagerService {
	
	/**
	 * @param transferBean
	 * @param homePath
	 * @param dataPath
	 * @return
	 */
	void archiveAndDeleteFiles(String homePath, String dataPath);
	
	/**
	 * Archives the files to the Archive directory , specific to country and the
	 * date of execution
	 * 
	 * @param transferBean
	 * @param homePath
	 * @param strDataPath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void archiveFiles(String homePath, String strDataPath) throws FileNotFoundException, IOException;

}
