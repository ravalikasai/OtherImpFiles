/**
 * 
 */
package com.mmm.cdms.distribute.sds.utils;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aspose.pdf.Document;
import com.aspose.pdf.PageSize;
import com.aspose.pdf.facades.PdfFileInfo;

/**
 * @author A5AK3ZZ
 * 		
 */
public class DocumentUtils {
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(DocumentUtils.class);
	
	/**
	 * Creates a blank page
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public static void buildblankPage(String fileName) throws FileNotFoundException {
		LOGGER.debug(" BEGIN : buildblankPage method " + fileName);
		Document document = new Document(fileName);
		document.getPageInfo().setWidth(PageSize.getA4().getWidth());
		document.getPageInfo().setHeight(PageSize.getA4().getHeight());
		
		document.getPages().add(); // add an empty page
		LOGGER.debug(" END : buildblankPage method " + fileName);
	}
	
	/**
	 * Iterates the Pdf Reader list and builds the PDF document
	 * 
	 * @param path
	 * @param fileName
	 * @param readers
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public static int buildPdfDocument(String fileName, List<Document> readers)
			throws FileNotFoundException {
		LOGGER.debug(" BEGIN : buildPdfDocument method " + fileName);
		Document document = new Document(fileName);
		document.getPageInfo().setWidth(PageSize.getA4().getWidth());
		document.getPageInfo().setHeight(PageSize.getA4().getHeight());

		PdfFileInfo toCountPages;
		int totalPages = 0;
		LOGGER.debug(" BEGIN : buildPdfDocument method ");
		try {
			
			Iterator<Document> readerIterator = readers.iterator();
			while (readerIterator.hasNext()) {
				Document pdfReaderTemp = (Document) readerIterator.next();
				int pages = 0;
				toCountPages = new PdfFileInfo(pdfReaderTemp);
				while (pages < toCountPages.getNumberOfPages()) {
					pages++;
					document.getPages().add(pdfReaderTemp.getPages());
					totalPages++;
					// cb.addTemplate(page, 0, 0);
					document.getPages().add(); // adds empty page at the end
												// (Need to verify if necessary)
				}
			}
			LOGGER.debug(" END : buildPdfDocument method " + fileName);
		} catch (Exception e) {
			e.getMessage();
		}
		return totalPages;
	}
}
