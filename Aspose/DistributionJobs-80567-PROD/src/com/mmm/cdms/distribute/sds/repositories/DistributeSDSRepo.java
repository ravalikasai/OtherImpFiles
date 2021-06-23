/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.repositories;

import com.mmm.cdms.distribute.sds.beans.SysOptionBean;

/**
 * The interface acts as a medium to access 
 * the database and all related operations
 * @author A5AK3ZZ
 *
 */
public interface DistributeSDSRepo {

	/**
	 * @param transferBean
	 * @param conn
	 * @param mediaType
	 * @param hasNoData
	 */
	void deleteDistributedRecords(final String mediaType, final boolean hasNoData);

	/**
	 * Method updates the Rulesbased_doc_summary tables for the removed duplicates
	 * @param conn
	 */
	void updateDuplicatesIntoRBDS();

	/**
	 * Method drops the temp table
	 * @param conn
	 */
	void dropTempTable();

	/**
	 * @param conn
	 * @param procName
	 */
	void executeSP(String procName);

	/**
	 * @param transferBean
	 * @param conn

	 */
	void createDQCounterTempTable();

	/**
	 * @param transferBean
	 * @return
	 */
	SysOptionBean loadSysOptionParams();
	
	/**
	 * Logs into the EVENT_LOG table that no data is found to process
	 * 
	 * @param msgShort
	 * @param msgLong
	 * @param statusCode
	 * @param jobName
	 */
	void logEvents(final String msgShort, final String msgLong, final int statusCode, final String jobName);

}
