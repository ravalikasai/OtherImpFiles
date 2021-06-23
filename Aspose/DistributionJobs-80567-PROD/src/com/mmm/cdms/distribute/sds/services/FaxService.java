/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services;

import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;

public interface FaxService {
	
	/**
	 * Configure FAX and handle sending of fax
	 * 
	 * @param dqBean
	 * @param batchPackageCount
	 * @param homePath
	 * @param fileName
	 * @param sysOption
	 */
	void configFaxSend(DistributionQueueBean dqBean, int batchPackageCount, String homePath, String fileName, SysOptionBean sysOption);
	
}
