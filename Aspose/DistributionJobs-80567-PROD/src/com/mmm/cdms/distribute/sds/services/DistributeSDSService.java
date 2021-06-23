/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services;

import java.io.IOException;
import java.sql.SQLException;

//import com.lowagie.text.DocumentException;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * @author A5AK3ZZ
 *
 */
public interface DistributeSDSService {

	/**
	 * Method to distribute SDS 
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws InterruptedException
	 * @throws DistributionException
	 */
	boolean distributeSDS() throws IOException, InterruptedException, DistributionException, Exception;
	
	/**
	 * @param transferBean
	 * @param conn
	 * @param mediaType
	 * @param hasNoData
	 */
	void deleteDistributedRecords(final String mediaType, final boolean hasNoData)  throws DistributionException;
	
	/**
	 * @param transferBean
	 * @param conn
	 */
	void createDQCounterTempTable() throws DistributionException;
	
	/**
	 * Method to drop the temp table created in earlier steps
	 * @throws DistributionException
	 */
	void dropTempTable() throws DistributionException;
	
	/**
	 * Method to archive and delete files
	 * 
	 * @param transferBean
	 * @return
	 */
	void archiveAndDeleteFiles();
	
	/**
	 * @param statusFlag
	 * @param jobName
	 */
	void sendMailAndLogEvent(final String statusFlag,final String jobName);

}
