/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services;

import java.sql.SQLException;
import java.util.List;

import com.aspose.pdf.Document;
//import com.lowagie.text.pdf.PdfReader;
import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.distribute.sds.beans.PdfReaderBean;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * @author a5ak3zz
 *
 */
public interface DocumentService {

	/**
	 * Method to generate the PDF Documents for all countries
	 * 
	 * @param completeDataList
	 * @param inputMedia
	 * @param fileService
	 * @return
	 * @throws Exception
	 */
	void generatePdfdocument(List<DistributionQueueBean> completeDataList, final int inputMedia,
			FileTransferService fileService) throws Exception;
	
	/**
	 * Retrieve the data from Distribution Queue 
	 * 
	 * @param packageId
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	List<DistributionQueueBean> retrieveDistributionQueueDetails() throws DistributionException;
	
	/**
	 * Obtain the document pdf in the form of an image for a specific doc index ID
	 * 
	 * @param distributeQueueBean
	 * @param docIndexID
	 * @return
	 * @throws DistributionException
	 */
	Document getDocumentPDF(DistributionQueueBean distributeQueueBean, String docIndexID) throws DistributionException;
	
	/**
	 * Get the kit component associated to a document
	 * 
	 * @param distributeQueueBean
	 * @param sdsPages
	 * @return
	 * @throws DistributionException
	 */
	PdfReaderBean getKitComponents(DistributionQueueBean distributeQueueBean, int sdsPages) throws DistributionException;
	
	/**
	 * Update the RULESBASED_DOC_SUMMARY table after processing the pdfs
	 * 
	 * @param distributeQueueBean
	 * @throws DistributionException
	 */
	void updateRulesBasedDocSummary(DistributionQueueBean distributeQueueBean) throws DistributionException;
	
	/**
	 * Insert the docIndexID details into the temp table
	 * 
	 * @param packageId
	 * @throws DistributionException
	 */
	void insertDetailsIntoTempTable(String packageId) throws DistributionException;
	
	/**
	 * Insert details into the DISTRIBUTION_LOG table
	 * 
	 * @param distributeQueueBean
	 * @throws DistributionException
	 */
	void insertDetailsIntoDL(DistributionQueueBean distributeQueueBean) throws DistributionException;

}
