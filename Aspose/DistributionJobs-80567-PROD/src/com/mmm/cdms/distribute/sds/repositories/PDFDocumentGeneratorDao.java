/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.aspose.pdf.Document;
//import com.lowagie.text.pdf.PdfReader;
import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * @author a5ak3zz
 *
 */
public interface PDFDocumentGeneratorDao {

	/**
	 * @param distributeQueueVO
	 * @throws SQLException
	 */
	void insertDetailsIntoDistributionLog(DistributionQueueBean distributeQueueVO) throws DistributionException;
	
	/**
	 * @return
	 * @throws SQLException
	 */
	ResultSet retrieveDistributionQueueDetails() throws DistributionException;
	
	/**
	 * @param dqBean
	 * @param docIndexID
	 * @return
	 * @throws SQLException
	 */
	ResultSet getDocumentPDF(DistributionQueueBean dqBean, String docIndexID) throws DistributionException;
	
	/**
	 * @param dqBean
	 * @return
	 * @throws SQLException
	 */
	List<String> getKitComponents(DistributionQueueBean dqBean) throws DistributionException;
	
	/**
	 * @param dqBean
	 * @throws SQLException
	 */
	void updateRulesBasedDocSummary(DistributionQueueBean dqBean) throws DistributionException;
	
	/**
	 * @param packageId
	 * @throws SQLException
	 */
	void insertDetailsIntoTempTable(String packageId) throws DistributionException;
	
	/**
	 * @param addressID
	 * @return PdfReader
	 * @throws DistributionException
	 */
	Document getCoverLetterTemplate(String addressID) throws DistributionException;
	
	/**
	 * @param addressID
	 * @return PdfReader
	 * @throws DistributionException
	 */
	Document getUSCoverLetterTemplate(String templateID) throws DistributionException;
}
