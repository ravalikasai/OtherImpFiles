/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.beans;

import java.util.List;

import com.aspose.pdf.Document;

//import com.lowagie.text.pdf.PdfReader;

/**
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 */
public class PdfReaderBean {
	
	private int sdsPages;
	private List<Document> pdfReaderList;
	
	/**
	 * @return the sdsPages
	 */
	public int getSdsPages() {
		return sdsPages;
	}
	
	/**
	 * @param sdsPages the sdsPages to set
	 */
	public void setSdsPages(int sdsPages) {
		this.sdsPages = sdsPages;
	}
	
	/**
	 * @return the pdfReaderList
	 */
	public List<Document> getPdfReaderList() {
		return pdfReaderList;
	}
	
	/**
	 * @param pdfReaderList the pdfReaderList to set
	 */
	public void setPdfReaderList(List<Document> pdfReaderList) {
		this.pdfReaderList = pdfReaderList;
	} 
}
