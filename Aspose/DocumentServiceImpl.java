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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.SocketException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aspose.pdf.Document;
import com.aspose.pdf.Font;
import com.aspose.pdf.FontRepository;
import com.aspose.pdf.FontStyles;
import com.aspose.pdf.MarginInfo;
import com.aspose.pdf.Page;
import com.aspose.pdf.PageSize;
import com.aspose.pdf.TextBuilder;
import com.aspose.pdf.TextFragment;
import com.aspose.pdf.TextState;
import com.aspose.pdf.facades.PdfFileInfo;
import com.mmm.aspose.main.Aspose;
import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.beans.CoverAddressBoxBean;
import com.mmm.cdms.distribute.sds.beans.CoverLetterBean;
import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.distribute.sds.beans.MediaSettingsBean;
import com.mmm.cdms.distribute.sds.beans.PdfReaderBean;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.constants.PropertyFileConstants;
import com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao;
import com.mmm.cdms.distribute.sds.repositories.impl.PDFDocumentGeneratorDaoImpl;
import com.mmm.cdms.distribute.sds.services.DocumentService;
import com.mmm.cdms.distribute.sds.services.EmailService;
import com.mmm.cdms.distribute.sds.services.FaxService;
import com.mmm.cdms.distribute.sds.services.FileTransferService;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * 
 * Generates PDF documents, concatenate Kits and components for the passed
 * package_id
 * 
 * @author a5ak3zz
 *
 */
public class DocumentServiceImpl implements DocumentService {
	
	
	private SysOptionBean sysOption;
	private MediaSettingsBean mediaSettings;
	/**
	 * Constant representing a tab
	 */
	private static final String TAB_STRING = "\t";
	/**
	 * Logging element for this object
	 */
	private static final Logger logger = Logger.getLogger(DocumentServiceImpl.class);
	public static Map<String, String> returnEmailAddrMap = new HashMap<>();
	
	Aspose aspose = new Aspose();
	
	private String fileName;
	
	public DocumentServiceImpl(SysOptionBean sysOption, MediaSettingsBean mediaSettings) {
		this.sysOption = sysOption;
		this.mediaSettings = mediaSettings;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DocumentService#
	 * generatePdfdocument(java.util.List, int,
	 * com.mmm.cdms.distribute.sds.services.FileTransferService)
	 */
	public void generatePdfdocument(final List<DistributionQueueBean> completeDataList,
			final int inputMedia, FileTransferService fileService) throws Exception {
		logger.info("generatePdfdocument of DocumentServiceImpl");
		Aspose.settingAsposeLicense();
		List<Document> readers = new ArrayList<>();
		String usCountryCode = "US";
		int pageCount = 2;
		PDFDocumentGeneratorDao docGenDao = new PDFDocumentGeneratorDaoImpl();
		String homePath = sysOption.getHomePath() + DistributeSDSConstants.FILE_SEPERATOR;
		Set<String> docGrpNumList = new LinkedHashSet<>();
		Set<String> orderNumList = new LinkedHashSet<>();
		DistributionQueueBean genCvrLetterBean = null;
		boolean isUSPaperMail = false;
		logger.info("Going to iterate over all the documents");
		for (DistributionQueueBean dqBean : completeDataList) {
			if (dqBean.getCountryCode().equals(usCountryCode)) {
				this.setFileName(mediaSettings.getFileNameUS() + dqBean.getPackageID());
				isUSPaperMail = processUSPdfDocument(dqBean, inputMedia, pageCount, docGenDao, homePath);
				if (isUSPaperMail) {
					DistributeSDSApplication.pdfDetailsDTO
							.setProcessStatus(DistributeSDSConstants.GENERATE_PDF + "for File Name : "
									+ this.getFileName());
				}
			} else {
				this.setFileName(mediaSettings.getFileNameEU() + "_" + dqBean.getAddressId() + "_"
						+ dqBean.getSalesCustomerNum());
				// generating PDF Document for Non-US
				Writer fWriter = new BufferedWriter(
						new FileWriter(mediaSettings.getReportFileNameEU(), true));
				// return dgn and order num for non-US
				docGrpNumList.add(dqBean.getDocGroupNum());
				if (dqBean.getOrderNum() != null) {
					List<String> orderNums = Arrays.asList(dqBean.getOrderNum().split("\\s*,\\s*"));
					orderNumList.addAll(orderNums);
				}
				generatePDFDocForEMEA(dqBean, fWriter, readers, pageCount, docGenDao, homePath,
						docGrpNumList, orderNumList);
				fWriter.close();
				genCvrLetterBean = dqBean;
				DistributeSDSApplication.pdfDetailsDTO.setProcessStatus(
						DistributeSDSConstants.GENERATE_PDF + "for File Name : " + this.getFileName());
			}
			if (dqBean != null) {
				DistributeSDSApplication.pdfDetailsDTO
						.setNoOfPages(DistributeSDSApplication.pdfDetailsDTO.getNoOfPages()
								+ dqBean.getPackagePageCount());
				DistributeSDSApplication.pdfDetailsDTO
						.setNoOfPackages(DistributeSDSApplication.pdfDetailsDTO.getNoOfPackages() + 1);
				DistributeSDSApplication.pdfDetailsDTO.getTransferMap()
						.get(dqBean.getMediaTypeCode().toUpperCase())
						.add(this.getFileName() + DistributeSDSConstants.PDF_EXTN);
			}
		}
		if (genCvrLetterBean != null && !genCvrLetterBean.getCountryCode().isEmpty()
				&& genCvrLetterBean.getCountryCode() != usCountryCode) {
			buildFinalDocument(inputMedia, fileService, readers, homePath, docGrpNumList, orderNumList,
					genCvrLetterBean);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmm.cdms.distribute.sds.services.DocumentService#getDocumentPDF(com
	 * .mmm.cdms.distribute.sds.beans.DistributionQueueBean)
	 */
	@Override
	public Document getDocumentPDF(DistributionQueueBean distributeQueueBean, String docIndexID)
			throws DistributionException {
		Aspose.settingAsposeLicense();
		PDFDocumentGeneratorDao pdfDocGen = new PDFDocumentGeneratorDaoImpl();
		Document reader = new Document();
		
		reader.getPageInfo().setWidth(PageSize.getA4().getWidth());
		reader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		
		ResultSet rs = pdfDocGen.getDocumentPDF(distributeQueueBean, docIndexID);
		try {
			if (rs != null && rs.isBeforeFirst() && rs.next()) {
				reader = new Document((InputStream) rs.getBinaryStream(1));
			}
		} catch (SQLException e) {
			logger.error("Error occured in getDocumentPDF", e);
			throw new DistributionException("Error occured in getDocumentPDF", e);
		}
		return reader;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmm.cdms.distribute.sds.services.DocumentService#getKitComponents(
	 * com.mmm.cdms.distribute.sds.beans.DistributionQueueBean, int)
	 */
	@Override
	public PdfReaderBean getKitComponents(DistributionQueueBean distributeQueueBean, int sdsPages)
			throws DistributionException {
		Aspose.settingAsposeLicense();
		PdfReaderBean readerBean = new PdfReaderBean();
		List<Document> readerList = new ArrayList<>();
		PDFDocumentGeneratorDao pdfDocGen = new PDFDocumentGeneratorDaoImpl();
		List<String> kcList = pdfDocGen.getKitComponents(distributeQueueBean);
		Document pdfReader = new Document();
		pdfReader.getPageInfo().setWidth(PageSize.getA4().getWidth());
		pdfReader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		PdfFileInfo toCountDocPages = null;
		for (String kcDocIndexID : kcList) {
			if (sdsPages % 2 != 0) {
				// odd pages
				String blankPageName = this.fileName + DistributeSDSConstants.blankPage + kcDocIndexID
						+ DistributeSDSConstants.PDF_EXTN;
				try {
					buildblankPage(blankPageName);
				} catch (FileNotFoundException e) {
					logger.error("Error occured in getKitComponents", e);
					throw new DistributionException("Error occured in getKitComponents", e);
				}
				pdfReader = new Document(blankPageName);
				toCountDocPages = new PdfFileInfo(pdfReader);
				readerList.add(pdfReader);
				// add one for blank page
				sdsPages = sdsPages + 1;
			}
			pdfReader = getDocumentPDF(null, kcDocIndexID);
			readerList.add(pdfReader);
			sdsPages = sdsPages + toCountDocPages.getNumberOfPages();
		}
		readerBean.setSdsPages(sdsPages);
		readerBean.setPdfReaderList(readerList);
		return readerBean;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DocumentService#
	 * updateRulesBasedDocSummary(com.mmm.cdms.distribute.sds.beans.
	 * DistributionQueueBean)
	 */
	@Override
	public void updateRulesBasedDocSummary(DistributionQueueBean distributeQueueBean)
			throws DistributionException {
		PDFDocumentGeneratorDao pdfDocGen = new PDFDocumentGeneratorDaoImpl();
		pdfDocGen.updateRulesBasedDocSummary(distributeQueueBean);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DocumentService#
	 * insertDetailsIntoTempTable(int)
	 */
	@Override
	public void insertDetailsIntoTempTable(String packageId) throws DistributionException {
		PDFDocumentGeneratorDao pdfDocGen = new PDFDocumentGeneratorDaoImpl();
		pdfDocGen.insertDetailsIntoTempTable(packageId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DocumentService#
	 * insertDetailsIntoDL(com.mmm.cdms.distribute.sds.beans.
	 * DistributionQueueBean)
	 */
	@Override
	public void insertDetailsIntoDL(DistributionQueueBean distributeQueueBean)
			throws DistributionException {
		PDFDocumentGeneratorDao pdfDocGen = new PDFDocumentGeneratorDaoImpl();
		pdfDocGen.insertDetailsIntoDistributionLog(distributeQueueBean);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.DocumentService#
	 * retrieveDistributionQueueDetails()
	 */
	public List<DistributionQueueBean> retrieveDistributionQueueDetails() throws DistributionException {
		logger.debug("BEGIN : RETRIEVING Distribution Queue DETAILS ");
		List<DistributionQueueBean> dqBeanList = new ArrayList<>();
		PDFDocumentGeneratorDao docGenDao = new PDFDocumentGeneratorDaoImpl();
		ResultSet resultSet = docGenDao.retrieveDistributionQueueDetails();
		try {
			while (resultSet.next()) {
				DistributionQueueBean dqBean = loadQueueDataDetails(resultSet);
				dqBeanList.add(dqBean);
			}
		} catch (SQLException | IOException e) {
			logger.error("Error occured in retrieveDistributionQueueDetails", e);
			throw new DistributionException("Error occured in retrieveDistributionQueueDetails", e);
		}
		logger.info(DistributeSDSConstants.DASHED_COMMENT);
		logger.info("The data size from DQ is : " + dqBeanList.size());
		logger.info(DistributeSDSConstants.DASHED_COMMENT);
		logger.debug("END : RETRIEVING Queue DATA DETAILS ");
		return dqBeanList;
	}
	
	/**
	 * @param inputMedia
	 * @param fileService
	 * @param readers
	 * @param homePath
	 * @param docGrpNumList
	 * @param orderNumList
	 * @param genCvrLetterBean
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @throws SocketException
	 * @throws InterruptedException
	 * @throws DistributionException
	 */
	private void buildFinalDocument(final int inputMedia, FileTransferService fileService,
			List<Document> readers, String homePath, Set<String> docGrpNumList, Set<String> orderNumList,
			DistributionQueueBean genCvrLetterBean)
			throws IOException, InterruptedException, DistributionException {
		Aspose.settingAsposeLicense();
		String pdfFileName;
		pdfFileName = mediaSettings.getFileNameEU() + "_" + genCvrLetterBean.getAddressId() + "_"
				+ genCvrLetterBean.getSalesCustomerNum();
		// Step 1.ADD COVER LETTER PAGE
		logger.debug(pdfFileName);
		Document pdfReader = buildCoverLetterPage(genCvrLetterBean, docGrpNumList, orderNumList);
		String coversheetFile = pdfFileName + DistributeSDSConstants.coverSheet
				+ DistributeSDSConstants.PDF_EXTN;
		 new Document();
	//	pdfReader.save(coversheetFile);
		readers.add(0, pdfReader);
	//	pdfReader.save(coversheetFile);
		buildPdfDocument(this.getFileName() + DistributeSDSConstants.PDF_EXTN, readers);
		cleanUpFiles(homePath, coversheetFile);
		logger.debug(" END : generatePdfdocument method ");
		fileService.transferFiles(this.getFileName() + DistributeSDSConstants.PDF_EXTN,
				DistributeSDSUtils.getPropertyValue(PropertyFileConstants.DRIVE_MOUNT),
				DistributeSDSConstants.FILE_TRANSFER_OPERATION);
		if (inputMedia == 1) {
			Thread.sleep(1000);
		}
	}
	
	/**
	 * @param dqBean
	 * @param inputMedia
	 * @param readers
	 * @param pageCount
	 * @param docGenDao
	 * @param homePath
	 * @throws SQLException
	 * @throws DistributionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean processUSPdfDocument(DistributionQueueBean dqBean, final int inputMedia,
			int pageCount, PDFDocumentGeneratorDao docGenDao, String homePath)
			throws SQLException, DistributionException, IOException, InterruptedException {
		String reportFile;
		boolean isUSPaperMail = true;
		// generating PDF Document for US
		String pdfFileName = this.getFileName();
		generatePdfDocForUS(dqBean, pageCount, docGenDao, homePath, pdfFileName);
		if (inputMedia == 1) {
			EmailService emailService = new EmailServiceImpl();
			String emailBody = emailService.createEmailBodyForUS();
			String emailSubject = MessageFormat.format("3M _{0}({1})", dqBean.getDocGroupNum(),
					dqBean.getDocTradeName());
			emailService.sendEMailWithAttachment(sysOption.getMailServerAddress(),
					sysOption.getFromEmailAddress(), dqBean.getEmailAddress(), emailSubject, emailBody,
					pdfFileName.concat(".pdf"));
			isUSPaperMail = false;
		} else if (inputMedia == 2) {
			FaxService faxservice = new FaxServiceImpl();
			faxservice.configFaxSend(dqBean,
					DistributeSDSApplication.pdfDetailsDTO.getBatchPackageCount(),
					sysOption.getHomePath(), pdfFileName, sysOption);
			isUSPaperMail = false;
		} else if (inputMedia == 3) {
			reportFile = homePath + DistributeSDSConstants.US_PAPER_MAIL_REPORT_FILE_NAME;
			this.writeToReportFile(dqBean, reportFile);
		}
		Thread.sleep(1000);
		return isUSPaperMail;
	}
	
	/**
	 * Builds the cover letter page based on different countries and conditions
	 * 
	 * @param dqBean
	 * @param docGrpNumMulti
	 * @param orderNumMulti
	 * @throws IOException
	 * @throws DocumentException
	 * @throws DistributionException
	 */
	private Document buildCoverLetterPage(DistributionQueueBean dqBean, Set<String> docGrpNumMulti,
			Set<String> orderNumMulti) throws DistributionException, IOException {
		logger.debug("BEGIN : buildCoverLetter()");
		Aspose.settingAsposeLicense();
		PDFDocumentGeneratorDao docGenDao = new PDFDocumentGeneratorDaoImpl();
		Document pdfReader = docGenDao.getCoverLetterTemplate(dqBean.getAddressId());
		String langCode = dqBean.getSapLanguageCode();
		String countryCode = dqBean.getCountryCode();
		// setup address box on the cover letter
		CoverAddressBoxBean addressBean = new CoverAddressBoxBean();
		addressBean.setAttnName(dqBean.getAttnName());
		addressBean.setAddress1(dqBean.getAddress1());
		addressBean.setAddress2(dqBean.getAddress2());
		addressBean.setAddress3(dqBean.getAddress3());
		addressBean.setAddress4(dqBean.getAddress4());
		addressBean.setCity(dqBean.getCity());
		addressBean.setStateCode(dqBean.getStateCode());
		addressBean.setZipCode(dqBean.getZipCode());
		addressBean.setCompanyName(dqBean.getCompanyName());
		addressBean.setCountryName(dqBean.getCountryName());
		addressBean.setEmailAddress(dqBean.getEmailAddress());
		
		ArrayList<String> addressBox = new ArrayList<String>();
		if (addressBean != null && countryCode != null) {
			
			addressBox = setupAddressBox(addressBean, countryCode);
			// setup cover letter contents
			CoverLetterBean coverLetterBean = new CoverLetterBean();
			coverLetterBean.setCoveraddressOffsetX(dqBean.getCoveraddressOffsetX());
			coverLetterBean.setCoveraddressOffsetY(dqBean.getCoveraddressOffsetY());
			coverLetterBean.setCoverInfoOffsetX(dqBean.getCoverInfoOffsetX());
			coverLetterBean.setCoverInfoOffsetY(dqBean.getCoverInfoOffsetY());
			coverLetterBean.setCoveraddressFontsize(dqBean.getCoveraddressFontsize());
			coverLetterBean.setCoverinfoboxFontsize(dqBean.getCoverinfoboxFontsize());
			coverLetterBean.setOrderNumTransText(dqBean.getOrderNumTransText());
			coverLetterBean.setDateTransText(dqBean.getDateTransText());
			coverLetterBean.setDocGrpNumTransText(dqBean.getDocGrpNumTransText());
			if (coverLetterBean != null) {
				stampCoverLetter(pdfReader, addressBox, coverLetterBean, docGrpNumMulti, orderNumMulti,
						countryCode, langCode);
			}
		}
		logger.debug("END : buildCoverLetter()");
	//	pdfReader.save();
		return pdfReader;
	}
	
	/**
	 * Method to setup the address box to be set in the cover letter
	 * 
	 * @param addressBean
	 * @param countryCode
	 * @return
	 */
	private ArrayList<String> setupAddressBox(CoverAddressBoxBean addressBean, String countryCode) {
		int addrBoxIndex = 0;
		
		ArrayList<String> addressBox = new ArrayList<String>();
		addressBox.add(addrBoxIndex++, addressBean.getAttnName());
		addressBox.add(addrBoxIndex++, addressBean.getCompanyName());
		if (countryCode.equals("HU")) {
			addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(addressBean.getCity())
					.concat(DistributeSDSConstants.SPACE_CONST).concat(addressBean.getStateCode()));
		}
		if (StringUtils.isNotBlank(addressBean.getAddress3().trim())) {
			addressBox.add(addrBoxIndex++, addressBean.getAddress3());
		}
		if (StringUtils.isNotBlank(addressBean.getAddress4().trim())) {
			addressBox.add(addrBoxIndex++, addressBean.getAddress4());
		}
		
		// Adding functionality to adjust the address to new line
		// in case of France and its regions Issue : 161115-05BD
		switch (countryCode) {
			case DistributeSDSConstants.COUNTRY_FR:
			case DistributeSDSConstants.REGION_GF:
			case DistributeSDSConstants.REGION_GP:
			case DistributeSDSConstants.REGION_MQ:
			case DistributeSDSConstants.REGION_YT:
			case DistributeSDSConstants.REGION_RE:
			case DistributeSDSConstants.REGION_NC:
			case DistributeSDSConstants.REGION_PF:
			case DistributeSDSConstants.REGION_PM:
				addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(addressBean.getAddress1()));
				addressBox.add(addrBoxIndex++, addressBean.getAddress2());
				break;
			default:
				addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(addressBean.getAddress1())
						.concat(DistributeSDSConstants.SPACE_CONST).concat(addressBean.getAddress2()));
				break;
		}
		// added changes as per request from Daniel Morgan - 11/08/2016
		// changes to move zip plus in its own line for GB and after city for IE
		String zipCode = addressBean.getZipCode();
		if (zipCode != null) {
			if (DistributeSDSConstants.COUNTRY_GB.equalsIgnoreCase(countryCode)) {
				addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(addressBean.getCity())
						.concat(DistributeSDSConstants.SPACE_CONST).concat(addressBean.getStateCode()));
				if (!zipCode.equals(DistributeSDSConstants.ZIPCODE_NA)) {
					addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(zipCode));
				}
			} else if (DistributeSDSConstants.COUNTRY_IE.equalsIgnoreCase(countryCode)
					|| DistributeSDSConstants.COUNTRY_CA.equalsIgnoreCase(countryCode)) {
				if (zipCode.equals(DistributeSDSConstants.ZIPCODE_NA)) {
					zipCode = DistributeSDSConstants.SPACE_CONST;
				}
				
				System.out.println(DistributeSDSUtils.trimString(addressBean.getCity()));
				
				// if(addressBean.getCity())
				
				addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(addressBean.getCity())
						.concat(DistributeSDSConstants.SPACE_CONST).concat(addressBean.getStateCode())
						.concat(DistributeSDSConstants.SPACE_CONST)
						.concat(DistributeSDSUtils.trimString(zipCode)));
			} else {
				if (zipCode.equals(DistributeSDSConstants.ZIPCODE_NA)) {
					addressBox.add(addrBoxIndex++,
							DistributeSDSUtils.trimString(addressBean.getCity())
									.concat(DistributeSDSConstants.SPACE_CONST)
									.concat(addressBean.getStateCode()));
				} else if (countryCode.equalsIgnoreCase("HU")) {
					addressBox.add(addrBoxIndex++, DistributeSDSUtils.trimString(zipCode));
				} else if (countryCode.equalsIgnoreCase("LV")) {
					// the order is City <> State <> ZipCode
					addressBox.add(addrBoxIndex++,
							DistributeSDSUtils.trimString(addressBean.getCity()).concat(",")
									.concat(DistributeSDSConstants.SPACE_CONST).concat(countryCode)
									.concat("-").concat(DistributeSDSUtils.trimString(zipCode)));
				} else if (countryCode.equalsIgnoreCase(DistributeSDSConstants.COUNTRY_IT)) {
					addressBox.add(addrBoxIndex++,
							DistributeSDSUtils.trimString(zipCode)
									.concat(DistributeSDSConstants.SPACE_CONST)
									.concat(DistributeSDSUtils.trimString(addressBean.getCity())
											.concat(DistributeSDSConstants.SPACE_CONST)
											.concat(addressBean.getStateCode())));
					addressBox.add(addrBoxIndex++, addressBean.getEmailAddress());
				} else {
					addressBox.add(addrBoxIndex++,
							DistributeSDSUtils.trimString(zipCode)
									.concat(DistributeSDSConstants.SPACE_CONST)
									.concat(DistributeSDSUtils.trimString(addressBean.getCity())
											.concat(DistributeSDSConstants.SPACE_CONST)
											.concat(addressBean.getStateCode())));
				}
			}
		}
		// Country & Region names added to new line in the
		// address
		String countryNameList = DistributeSDSUtils
				.getPropertyValue(PropertyFileConstants.COUNTRY_NAMES_IN_ADDRESS);
		String[] countryNames = countryNameList.split(",");
		for (String country : countryNames) {
			if (countryCode.equals(country)) {
				addressBox.add(addrBoxIndex++, addressBean.getCountryName().toUpperCase());
				break;
			}
		}
		return addressBox;
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
	private int buildPdfDocument(String fileName, List<Document> readers) throws FileNotFoundException {
		logger.debug(" BEGIN : buildPdfDocument method " + fileName);
		
		Aspose.settingAsposeLicense();
		
	//	Document document = new Document();
	//	PdfFileInfo toCountPages;
		int totalPages = 0;
		try {
			
			
			aspose.createPDFWithDoumentLs(readers, fileName);
			
			/*Iterator<Document> readerIterator = readers.iterator();
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
			}*/
			logger.debug(" END : buildPdfDocument method " + fileName);
		} catch (Exception e) {
			e.getMessage();
		}
		return totalPages;
	}
	
	/**
	 * Creates the Separator Page
	 * 
	 * @param path
	 * @param fileName
	 * @param readers
	 * @throws DistributionException
	 */
	private void buildSeparatorPage(DistributionQueueBean distributeQueueBean, String fileName,
			boolean blankPageDuplexPrint) throws FileNotFoundException {
		
		logger.debug(" BEGIN : buildSeperatorPage method " + fileName);
		
		Aspose.settingAsposeLicense();
		
		new com.aspose.pdf.FontRepository();
		Font separatorPgFont = FontRepository.openFont("C:\\Windows\\Fonts\\Couri.ttf");
		separatorPgFont.setEmbedded(true);
		
		Document document = new Document();
		document.getPageInfo().setWidth(PageSize.getA4().getWidth());
		document.getPageInfo().setHeight(PageSize.getA4().getHeight());
		
		Page page = document.getPages().add();
		document.getPages().get_Item(1);
		boolean isUnicodeCountry = false;
		String unicodeCountries = DistributeSDSUtils
				.getPropertyValue(PropertyFileConstants.UNICODE_COUNTRIES);
		
		MarginInfo margin = new MarginInfo();
		margin.setLeft(50f);
		margin.setRight(30f);
		margin.setTop(40.5f);
		margin.setBottom(60f);
		page.getPageInfo().setMargin(margin);

		// use the font in a style

		TextState style = new TextState();
		style.setFont(separatorPgFont);
		style.setFontSize(10);
		style.setFontStyle(FontStyles.Regular);
		style.setLineSpacing(7f);
		style.setHorizontalAlignment(1);
		
		if (blankPageDuplexPrint) {
			// add a blank Page if SDS ended in odd Page so separator page
			// starts in odd page
			document.getPages().add();
		}
		if (unicodeCountries.contains(distributeQueueBean.getCountryCode())) {
			
			new com.aspose.pdf.FontRepository();
			
			com.aspose.pdf.Font baseFont = FontRepository.openFont("C://Windows//Fonts//Arial.ttf");
			separatorPgFont = baseFont;
			isUnicodeCountry = true;
		}
		
		TextFragment frag = new TextFragment(DistributeSDSConstants.SEPARATOR_PAGE_TITLE);
		TextFragment newLineInDoc = new TextFragment(DistributeSDSConstants.EMPTY_SPACE);
		TextFragment frag1 = new TextFragment(DistributeSDSConstants.DECLARATION_LINE1);
		TextFragment frag2 = new TextFragment(DistributeSDSConstants.DECLARATION_LINE2);
		newLineInDoc = new TextFragment(" ");
		
		frag.getTextState().applyChangesFrom(style);
		newLineInDoc.getTextState().applyChangesFrom(style);
		frag1.getTextState().applyChangesFrom(style);
		frag2.getTextState().applyChangesFrom(style);
		frag.setInLineParagraph(true);

		page.getParagraphs().add(frag);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag1);
		page.getParagraphs().add(frag2);
		page.getParagraphs().add(newLineInDoc);
		
		if (isUnicodeCountry) {
			createUnicodeSeparatorPg(distributeQueueBean, document, separatorPgFont, newLineInDoc);
		} else {
			createNonUnicodeSeparatorPg(distributeQueueBean, document, separatorPgFont);
		}
		logger.debug(" END : buildSeperatorPage method " + fileName);
		document.save(fileName);
	}
	
	/**
	 * Method to create the fields for countries that do not need Unicode
	 * 
	 * @author A5AK3ZZ
	 * @param dqBean
	 * @param document
	 * @param separatorPgFont
	 * @throws DocumentException
	 */
	private void createNonUnicodeSeparatorPg(DistributionQueueBean distributeQueueBean, Document document,
			Font separatorPgFont) {
		
		Aspose.settingAsposeLicense();
		
		Page page = document.getPages().get_Item(1);
		
		new com.aspose.pdf.FontRepository();
		separatorPgFont = FontRepository.openFont("C:\\Windows\\Fonts\\Couri.ttf");
		separatorPgFont.setEmbedded(true);
		
		TextState style = new TextState();
		style.setFont(separatorPgFont);
		style.setFontSize(10);
		style.setFontStyle(FontStyles.Regular);
		style.setLineSpacing(7f);
		style.setHorizontalAlignment(1);
		
		TextFragment frag12 = new TextFragment();
		TextFragment frag13 = new TextFragment();
		
		TextFragment newLineInDoc = new TextFragment(DistributeSDSConstants.EMPTY_SPACE);
	//	newLineInDoc = new TextFragment(" ");
		
		TextFragment frag4 = new TextFragment("Doc ID: " + distributeQueueBean.getDocGroupNum());
		newLineInDoc = new TextFragment(" ");
		
		TextFragment frag5 = new TextFragment();
		
		if (distributeQueueBean.getOrderNum() != null) {
			 frag5 = new TextFragment("Order Num: " + distributeQueueBean.getOrderNum());
		} else {
			 frag5 = new TextFragment("Order Num: " + " ");
		}
		
		newLineInDoc = new TextFragment(" ");
		
		TextFragment frag6 = new TextFragment("Customer Num: " + distributeQueueBean.getSalesCustomerNum());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag7 = new TextFragment("Media Type: " + distributeQueueBean.getMediaTypeCode());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag8 = new TextFragment("To: " + distributeQueueBean.getAddress1());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag9 = new TextFragment("Country Code: " + distributeQueueBean.getSapCountryCode());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag10 = new TextFragment("Language Code: " + distributeQueueBean.getSapLanguageCode());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag11 = new TextFragment("Return Email Address: " + sysOption.getFromEmailAddress());
		
		if (distributeQueueBean.getMediaTypeCode()
				.equalsIgnoreCase(DistributeSDSConstants.MEDIA_TYPE_EMAIL)) {
			 frag12 = new TextFragment("Email: " + distributeQueueBean.getEmailAddress());
		}
		
		if (distributeQueueBean.getMediaTypeCode()
				.equalsIgnoreCase(DistributeSDSConstants.MEDIA_TYPE_FAX)) {
			 frag13 = new TextFragment("Fax: " + distributeQueueBean.getFaxNumber());
			
		}
	//	frag4.setInLineParagraph(true);
		frag4.getTextState().applyChangesFrom(style);
		frag5.getTextState().applyChangesFrom(style);
		frag6.getTextState().applyChangesFrom(style);
		frag7.getTextState().applyChangesFrom(style);
		frag8.getTextState().applyChangesFrom(style);
		frag9.getTextState().applyChangesFrom(style);
		frag10.getTextState().applyChangesFrom(style);
		frag11.getTextState().applyChangesFrom(style);
		frag12.getTextState().applyChangesFrom(style);
		frag13.getTextState().applyChangesFrom(style);
		
		//frag4.setInLineParagraph(true);

		page.getParagraphs().add(frag4);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag5);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag6);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag7);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag8);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag9);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag10);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag11);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag12);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag13);
		
		document.getPages();
		document.optimizeResources(aspose.getOptimizeOptions());
		document.optimize();
		
	}
	
	/**
	 * Method to create the separator page for countries that need to handle the
	 * Unicode
	 * 
	 * The highlighted difference is : The extra spaces added between the String
	 * and the value to adjust as per Cypress
	 * 
	 * @author A5AK3ZZ
	 * 
	 * @param dqBean
	 * @param document
	 * @param separatorPgFont
	 * @param newLineInDoc
	 * @throws DocumentException
	 */
	private void createUnicodeSeparatorPg(DistributionQueueBean distributeQueueBean, Document document,
			Font separatorPgFont, TextFragment newLineInDoc) {
		
		Aspose.settingAsposeLicense();
		
		Page page = document.getPages().get_Item(1);
		
		new com.aspose.pdf.FontRepository();
		separatorPgFont = FontRepository.openFont("C:\\Windows\\Fonts\\Couri.ttf");
		separatorPgFont.setEmbedded(true);
		
		TextState style = new TextState();
		style.setFont(separatorPgFont);
		style.setFontSize(9);
		style.setFontStyle(FontStyles.Italic);
		style.setLineSpacing(7f);
		style.setHorizontalAlignment(1);
		
		TextFragment frag12 = new TextFragment();
		TextFragment frag13 = new TextFragment();
		
		newLineInDoc = new TextFragment(DistributeSDSConstants.EMPTY_SPACE);
		
		TextFragment frag4 = new TextFragment("Doc ID: " + distributeQueueBean.getDocGroupNum());
		newLineInDoc = new TextFragment(" ");
		
		TextFragment frag5 = new TextFragment();
		
		if (distributeQueueBean.getOrderNum() != null) {
			 frag5 = new TextFragment("Order Num:     " + distributeQueueBean.getOrderNum());
		} else {
			 frag5 = new TextFragment("Order Num:     " + DistributeSDSConstants.EMPTY_SPACE);
		}
		
		newLineInDoc = new TextFragment(" ");
		
		TextFragment frag6 = new TextFragment("Customer Num:     " + distributeQueueBean.getSalesCustomerNum());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag7 = new TextFragment("Media Type:       " + distributeQueueBean.getMediaTypeCode());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag8 = new TextFragment("To: " + distributeQueueBean.getAddress1());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag9 = new TextFragment("Country Code:       " + distributeQueueBean.getSapCountryCode());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag10 = new TextFragment("Language Code:      " + distributeQueueBean.getSapLanguageCode());
		newLineInDoc = new TextFragment(" ");
		TextFragment frag11 = new TextFragment("Return Email Address: " + sysOption.getFromEmailAddress());
		
		if (distributeQueueBean.getMediaTypeCode()
				.equalsIgnoreCase(DistributeSDSConstants.MEDIA_TYPE_EMAIL)) {
			 frag12 = new TextFragment("Email: " + distributeQueueBean.getEmailAddress());
		}
		
		if (distributeQueueBean.getMediaTypeCode()
				.equalsIgnoreCase(DistributeSDSConstants.MEDIA_TYPE_FAX)) {
			 frag13 = new TextFragment("Fax: " + distributeQueueBean.getFaxNumber());
			
		}
		
		frag4.getTextState().applyChangesFrom(style);
		frag5.getTextState().applyChangesFrom(style);
		frag6.getTextState().applyChangesFrom(style);
		frag7.getTextState().applyChangesFrom(style);
		frag8.getTextState().applyChangesFrom(style);
		frag9.getTextState().applyChangesFrom(style);
		frag10.getTextState().applyChangesFrom(style);
		frag11.getTextState().applyChangesFrom(style);
		frag12.getTextState().applyChangesFrom(style);
		frag13.getTextState().applyChangesFrom(style);
		
		frag4.setInLineParagraph(true);

		page.getParagraphs().add(frag4);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag5);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag6);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag7);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag8);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag9);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag10);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag11);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag12);
		page.getParagraphs().add(newLineInDoc);
		page.getParagraphs().add(frag13);
		
		document.getPages();
		document.optimizeResources(aspose.getOptimizeOptions());
		document.optimize();
		
	}
	
	/**
	 * Creates a blank page
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	@SuppressWarnings("static-access")
	private void buildblankPage(String fileName) throws FileNotFoundException {
		
		logger.debug(" BEGIN : buildblankPage method " + fileName);
		
		Aspose aspose = new Aspose();
		
		aspose.settingAsposeLicense();
		
		Document document = new Document(fileName);
		document.getPageInfo().setWidth(PageSize.getA4().getWidth());
		document.getPageInfo().setHeight(PageSize.getA4().getHeight());
		
		// add a blank Page if SDS ended in odd Page so separator page
		// starts in odd page
		document.getPages().add();
		document.optimize();
		document.optimizeResources(aspose.getOptimizeOptions());
		
		logger.debug(" END : buildblankPage method " + fileName);
		
	}
	
	/**
	 * Method creates the cover letter
	 * 
	 * @param pdfReader
	 * @param addressLine
	 * @param coveraddressOffsetX
	 * @param coveraddressOffsetY
	 * @param coverInfoOffsetX
	 * @param coverInfoOffsetY
	 * @param coveraddressFontSize
	 * @param coverInfoFontSize
	 * @param docGrpNumMulti
	 * @param orderNumMulti
	 * @param docDate
	 * @param orderNumTransText
	 * @param dateTransText
	 * @param docGrpNumTransText
	 * @param countryCode
	 * @param languageCode
	 * @throws IOException
	 * @throws DocumentException
	 */
	private Document stampCoverLetter(Document pdfReader, ArrayList<String> addressLine,
			CoverLetterBean coverLetterBean, Set<String> docGrpNumMulti, Set<String> orderNumMulti,
			String countryCode, String languageCode) throws IOException {
		logger.debug(" BEGIN : stampCoverLetter method ");
		
		Aspose.settingAsposeLicense();
		
	//	pdfReader.getPageInfo().setWidth(PageSize.getA4().getWidth());
	//	pdfReader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		
		Page pdfPage = pdfReader.getPages().get_Item(1);
		PdfFileInfo toCountNumberOfPages;
		toCountNumberOfPages = new PdfFileInfo(pdfReader);
		int numOfPages = toCountNumberOfPages.getNumberOfPages();
		
		TextFragment forTimes = new TextFragment();
		TextFragment textFragmentCourier = new TextFragment();
		textFragmentCourier.getTextState().setFont(FontRepository.findFont("Courier"));
		// textFragmentCourier.getTextState().setFontSize(40);
		textFragmentCourier.getTextState().setFontStyle(2);
		textFragmentCourier.getTextState().setForegroundColor(com.aspose.pdf.Color.getBlack());
		TextFragment docGrpNumTransText = new TextFragment();
		TextFragment dateTransText = new TextFragment();
		TextFragment textFragCountryCode = new TextFragment();
		TextFragment textFragLangCode = new TextFragment();
		TextFragment textFragForCoverAddressFont = new TextFragment();
		TextFragment orderNumTransText = new TextFragment();
		TextFragment coverInfoOffsetX = new TextFragment();
		TextFragment coverInfoOffsetY = new TextFragment();
		TextFragment forOrderNumMultiList = new TextFragment();
		TextFragment fordocDate = new TextFragment();
		TextFragment addressFrag = new TextFragment();
		
		for (int page = 1; page <= numOfPages; page++) {
			String euromsCountry = DistributeSDSUtils
					.getPropertyValue(PropertyFileConstants.EUROMS_COUNTRIES);
			String unicodeCountries = DistributeSDSUtils
					.getPropertyValue(PropertyFileConstants.UNICODE_COUNTRIES);
			
			// create TextBuilder object
			TextBuilder textBuilder = new TextBuilder(pdfPage);
			Font fontGlobal = FontRepository.findFont("Times");
			textBuilder.appendText(textFragmentCourier);
			if (euromsCountry.contains(countryCode)) {
				fontGlobal = FontRepository.findFont("Courier");
				textFragmentCourier.getTextState().setFontSize(12);
			} else if (unicodeCountries.contains(countryCode)) {
				fontGlobal = FontRepository.openFont("C://Windows//Fonts//times.ttf");
				fontGlobal.setEmbedded(true);
				forTimes.getTextState().setFontSize(12);
			}
			
			for (int i = 0; i < addressLine.size(); i++) {
				addressFrag.setText(addressLine.get(i));
				addressFrag.getPosition().setXIndent(120 + coverLetterBean.getCoveraddressOffsetX());
				addressFrag.getPosition()
						.setYIndent(690 - i * (coverLetterBean.getCoveraddressFontsize() + 2)
								+ coverLetterBean.getCoveraddressOffsetY());
				addressFrag.setVerticalAlignment(1);
			}
			if (countryCode.equalsIgnoreCase("CA")) {
				LinkedList<String> orderNumMultiList = new LinkedList<String>(orderNumMulti);
				
				DistributeSDSUtils.convertIntoString(orderNumMultiList);
				
				forOrderNumMultiList.getPosition()
						.setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				forOrderNumMultiList.getPosition()
						.setYIndent(490 + coverLetterBean.getCoveraddressOffsetY());
				forOrderNumMultiList.setVerticalAlignment(1);
				
				docGrpNumTransText.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				docGrpNumTransText.getPosition().setYIndent(490 + coverLetterBean.getCoverInfoOffsetY());
				docGrpNumTransText.setVerticalAlignment(1);
				
				DistributeSDSUtils.docDateFormatter(countryCode, languageCode);
				
				fordocDate.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				fordocDate.getPosition().setYIndent(462 + coverLetterBean.getCoverInfoOffsetY());
				fordocDate.setVerticalAlignment(1);
				
				textFragCountryCode.setText(countryCode);
				textFragCountryCode.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				textFragCountryCode.getPosition().setYIndent(462 + coverLetterBean.getCoverInfoOffsetY());
				textFragCountryCode.setVerticalAlignment(1);
				
				textFragLangCode.setText(languageCode);
				textFragLangCode.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				textFragLangCode.getPosition().setYIndent(462 + coverLetterBean.getCoverInfoOffsetY());
				textFragCountryCode.setVerticalAlignment(1);
				
			} else {
				
				// orderNumTransText.setText(coverLetterBean.getOrderNumTransText());
				orderNumTransText.getPosition().setXIndent(62 + coverLetterBean.getCoverInfoOffsetX());
				orderNumTransText.getPosition().setYIndent(490 + coverLetterBean.getCoverInfoOffsetY());
				orderNumTransText.setVerticalAlignment(1);
				
				LinkedList<String> orderNumMultiList = new LinkedList<String>(orderNumMulti);
				
				DistributeSDSUtils.convertIntoString(orderNumMultiList);
				
				docGrpNumTransText.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				docGrpNumTransText.getPosition().setYIndent(490 + coverLetterBean.getCoverInfoOffsetY());
				docGrpNumTransText.setVerticalAlignment(1);
				
				// dateTransText.setText(coverLetterBean.getDateTransText());
				dateTransText.getPosition().setXIndent(62 + coverLetterBean.getCoverInfoOffsetX());
				dateTransText.getPosition().setYIndent(462 + coverLetterBean.getCoverInfoOffsetY());
				dateTransText.setVerticalAlignment(1);
				
				DistributeSDSUtils.docDateFormatter(countryCode, languageCode);
				
				textFragCountryCode.setText(countryCode);
				textFragCountryCode.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				textFragCountryCode.getPosition().setYIndent(462 + coverLetterBean.getCoverInfoOffsetY());
				textFragCountryCode.setVerticalAlignment(1);
				
				textFragLangCode.setText(languageCode);
				textFragLangCode.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				textFragLangCode.getPosition().setYIndent(462 + coverLetterBean.getCoverInfoOffsetY());
				textFragLangCode.setVerticalAlignment(1);
				
				// docGrpNumTransText.setText(coverLetterBean.getDocGrpNumTransText());
				docGrpNumTransText.getPosition().setXIndent(62 + coverLetterBean.getCoverInfoOffsetX());
				docGrpNumTransText.getPosition().setYIndent(434 + coverLetterBean.getCoverInfoOffsetY());
				docGrpNumTransText.setVerticalAlignment(1);
				
			}
			
			LinkedList<String> docGrpNumMultiList = new LinkedList<String>(docGrpNumMulti);
			if (docGrpNumMulti.size() <= 5) {
				if (countryCode.equals("CH") && languageCode.equals(DistributeSDSConstants.COUNTRY_IT)) {
					coverInfoOffsetX.getPosition()
							.setXIndent(250 + coverLetterBean.getCoverInfoOffsetX());
					coverInfoOffsetX.setVerticalAlignment(1);
					coverInfoOffsetY.getPosition()
							.setYIndent(434 + coverLetterBean.getCoverInfoOffsetY());
					coverInfoOffsetY.setVerticalAlignment(1);
					
				} else {
					coverInfoOffsetX.getPosition()
							.setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
					coverInfoOffsetX.setVerticalAlignment(1);
					coverInfoOffsetY.getPosition()
							.setYIndent(434 + coverLetterBean.getCoverInfoOffsetY());
					coverInfoOffsetY.setVerticalAlignment(1);
				}
			} else {
				
				DistributeSDSUtils.convertIntoString(docGrpNumMultiList.subList(0, 5));
				coverInfoOffsetX.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				coverInfoOffsetX.setVerticalAlignment(1);
				coverInfoOffsetY.getPosition().setYIndent(434 + coverLetterBean.getCoverInfoOffsetY());
				coverInfoOffsetY.setVerticalAlignment(1);
				
				DistributeSDSUtils
						.convertIntoString(docGrpNumMultiList.subList(5, docGrpNumMultiList.size()));
				
				coverInfoOffsetX.getPosition().setXIndent(200 + coverLetterBean.getCoverInfoOffsetX());
				coverInfoOffsetX.setVerticalAlignment(1);
				coverInfoOffsetY.getPosition().setYIndent(420 + coverLetterBean.getCoverInfoOffsetY());
				coverInfoOffsetY.setVerticalAlignment(1);
			}
			textBuilder = new TextBuilder(pdfPage);
			
			// append the text fragment to the PDF page
			textBuilder.appendText(orderNumTransText);
			textBuilder.appendText(dateTransText);
			textBuilder.appendText(docGrpNumTransText);
			textBuilder.appendText(textFragmentCourier);
			textBuilder.appendText(textFragForCoverAddressFont);
			textBuilder.appendText(textFragLangCode);
			textBuilder.appendText(textFragCountryCode);
			textBuilder.appendText(addressFrag);
			textBuilder.appendText(fordocDate);
			textBuilder.appendText(forOrderNumMultiList);
			textBuilder.appendText(coverInfoOffsetY);
			textBuilder.appendText(coverInfoOffsetX);
			textBuilder.appendText(orderNumTransText);
			textBuilder.appendText(docGrpNumTransText);
		}
		// inserting a blank page only if there are less than 2 cover letter
		// pages
		// e.g. Canada won't have a blank page on second position.
		if (numOfPages == 1) {
			pdfReader.getPages().add();
		}
		// cvrLetterDoc.save("D:\\Aspose\\EU Cover Letters\\EU_Test.pdf");
		
		logger.info("Aspose: loadDataForEUCoverLetterTemplate-End()");
		 return pdfReader;
		 // pdfReader.save();
	}
	
	/*
	 * =========================================================================
	 * === Private Methods
	 * =========================================================================
	 * ===
	 */
	
	/**
	 * Method to generate PDF Document for Out-of-US countries
	 * 
	 * @param distributeQueueBean
	 * @param fWriter
	 * @param readers
	 * @param sdsPages
	 * @param docGenDao
	 * @param homePath
	 * @throws IOException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 * @throws DistributionException
	 * @throws SQLException
	 */
	private void generatePDFDocForEMEA(DistributionQueueBean distributeQueueBean, Writer fWriter,
			List<Document> readers, int sdsPages, PDFDocumentGeneratorDao docGenDao, String homePath,
			Set<String> docGrpNumMulti, Set<String> orderNumMulti) throws Exception {
		Aspose.settingAsposeLicense();
		Document pdfReader;
		PdfFileInfo toCountDocPages = null;
		boolean blankPageDuplexPrint = false;
		if (sdsPages % 2 != 0) {
			// odd pages
			blankPageDuplexPrint = true;
		}
		String pdfDocumentName = getFileName() + DistributeSDSConstants.seperatorPage
				+ distributeQueueBean.getPackageID() + DistributeSDSConstants.PDF_EXTN;
		
		// STEP 2. BUILD SEPARATOR PAGE
		buildSeparatorPage(distributeQueueBean, pdfDocumentName, blankPageDuplexPrint);
		pdfReader = new Document(pdfDocumentName);
		pdfReader.getPageInfo().setWidth(PageSize.getA4().getWidth());
		pdfReader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		readers.add(pdfReader);
		// add a page to count if a blank page is added by separator
		if (blankPageDuplexPrint) {
			sdsPages = sdsPages + 1;
		}
		// STEP 3. ADD THE SDS DOCUMENT
		// adding main document - cover letter for kit and original document
		// for non kits
		pdfReader = getDocumentPDF(distributeQueueBean, "0");
		toCountDocPages = new PdfFileInfo(pdfReader);
		readers.add(pdfReader);
		sdsPages = sdsPages + toCountDocPages.getNumberOfPages();
		PdfReaderBean readersBean = getKitComponents(distributeQueueBean, sdsPages);
		sdsPages = readersBean.getSdsPages();
		readers.addAll(readersBean.getPdfReaderList());
		
		distributeQueueBean.setPackagePageCount(sdsPages);
		
		// update rules based doc summary
		updateDistributionTables(distributeQueueBean, docGenDao);
		
		distributeQueueBean.setEventId(DistributeSDSConstants.ADDRESS_ID
				+ DistributeSDSConstants.VALUE_SEPARATOR + distributeQueueBean.getAddressId()
				+ DistributeSDSConstants.SALES_CUST_NBR + DistributeSDSConstants.VALUE_SEPARATOR
				+ distributeQueueBean.getSalesCustomerNum());
		fWriter.write(DistributeSDSConstants.DOC_GROUP_NUM + DistributeSDSConstants.VALUE_SEPARATOR
				+ docGrpNumMulti + DistributeSDSConstants.ORDER_NUM
				+ DistributeSDSConstants.VALUE_SEPARATOR + orderNumMulti
				+ DistributeSDSConstants.ADDRESS_ID + DistributeSDSConstants.VALUE_SEPARATOR
				+ distributeQueueBean.getAddressId() + DistributeSDSConstants.SALES_CUST_NBR
				+ DistributeSDSConstants.VALUE_SEPARATOR + distributeQueueBean.getSalesCustomerNum()
				+ DistributeSDSConstants.FILE_NAME_CONST + DistributeSDSConstants.VALUE_SEPARATOR
				+ getFileName() + System.getProperty("line.separator"));
	}
	
	/**
	 * @param distributeQueueBean
	 * @param docGenDao
	 * @throws DistributionException
	 * @throws SQLException
	 */
	private void updateDistributionTables(DistributionQueueBean distributeQueueBean,
			PDFDocumentGeneratorDao docGenDao) throws DistributionException, SQLException {
		// update Rulesbased_Doc_Summary
		updateRulesBasedDocSummary(distributeQueueBean);
		// insert package id into the temp table
		insertDetailsIntoTempTable(distributeQueueBean.getPackageID());
		// insert details into the DL table
		docGenDao.insertDetailsIntoDistributionLog(distributeQueueBean);
	}
	
	/**
	 * 
	 * Method to generate PDF document with cover letter, blank pages, separator
	 * pages and SDS for US process
	 * 
	 * @param conn
	 * @param transferBean
	 * @param distributeQueueBean
	 * @throws SQLException
	 */
	private void generatePdfDocForUS(DistributionQueueBean dqBean, int pageCount,
			PDFDocumentGeneratorDao docGenDao, String homePath, String pdfFileName) throws SQLException {
		Aspose.settingAsposeLicense();
		List<Document> readers = new ArrayList<>();
		Document pdfReader = new Document();
		pdfReader.getPageInfo().setWidth(PageSize.getA4().getWidth());
		pdfReader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		// PdfContentByte pdfContentByte = null;
		// Font font = FontFactory.getFont(BaseFont.TIMES_ROMAN, 10);
		Font font = FontRepository.findFont("TimesNewRoman");
		Connection conn = DistributeSDSUtils.getDBConnection();
		CallableStatement callableStatement = null;
		ResultSet resultset = null;
		PdfFileInfo toCountDocPages = null;
		int packageBlankPageCount = 1;
		// by default after cover letter one blank page will be there
		try {
			String coverSheetName = this.fileName + DistributeSDSConstants.COVER_LETTER_FILE_NAME;
			pdfReader = docGenDao.getUSCoverLetterTemplate(dqBean.getTemplateID());
			// createUSCoverLetter(pdfReader, distributeQueueBean,
			// font,coverSheetName);
			createUSCoverLetter(pdfReader, dqBean, font, coverSheetName);
			pdfReader = new Document(pdfFileName + DistributeSDSConstants.COVER_LETTER_FILE_NAME);
			readers.add(pdfReader);
			// add document
			pdfReader = getDocumentPDF(dqBean, "0");
			toCountDocPages = new PdfFileInfo(pdfReader);
			readers.add(pdfReader);
			pageCount = pageCount + toCountDocPages.getNumberOfPages();
			// add blank page
			if (sysOption.isDuplex() && (pageCount % 2) != 0) {
				buildblankPage(this.getFileName() + DistributeSDSConstants.blankPage
						+ dqBean.getDocIndexId() + DistributeSDSConstants.PDF_EXTN);
				pdfReader = new Document(this.getFileName() + DistributeSDSConstants.blankPage
						+ dqBean.getDocIndexId() + DistributeSDSConstants.PDF_EXTN);
				readers.add(pdfReader);
				// add one for blank page
				pageCount = pageCount + 1;
				packageBlankPageCount++;
			}
			// appending Kit doc with cover letter
			callableStatement = conn.prepareCall(
					DistributeSDSUtils.getPropertyValue(DistributeSDSConstants.SP_GET_KIT_DOCS));
			callableStatement.setString(1, dqBean.getDocIndexId());
			resultset = callableStatement.executeQuery();
			while (resultset.next()) {
				pdfReader = new Document((InputStream) (resultset.getBinaryStream("DOCUMENT_IMAGE")));
				toCountDocPages = new PdfFileInfo(pdfReader);
				pageCount = pageCount + toCountDocPages.getNumberOfPages();
				readers.add(pdfReader);
				if ((pageCount % 2) != 0) {
					buildblankPage(this.getFileName() + DistributeSDSConstants.blankPage
							+ dqBean.getDocIndexId() + DistributeSDSConstants.PDF_EXTN);
					pdfReader = new Document(this.getFileName() + DistributeSDSConstants.blankPage
							+ dqBean.getDocIndexId() + DistributeSDSConstants.PDF_EXTN);
					readers.add(pdfReader);
					// add one for blank page
					pageCount = pageCount + 1;
					packageBlankPageCount++;
				}
			}
			dqBean.setPackagePageCount(pageCount);
			// update rules based doc summary
			updateDistributionTables(dqBean, docGenDao);
			// Update counters
			int packagePageCount = dqBean.getPackagePageCount();
			DistributeSDSApplication.pdfDetailsDTO.setBatchPackageCount(
					DistributeSDSApplication.pdfDetailsDTO.getBatchPackageCount() + 1);
			DistributeSDSApplication.pdfDetailsDTO
					.setBatchPageCount(DistributeSDSApplication.pdfDetailsDTO.getBatchPageCount()
							+ (packagePageCount - packageBlankPageCount));
			DistributeSDSApplication.pdfDetailsDTO.setBatchBlankPageCount(
					DistributeSDSApplication.pdfDetailsDTO.getBatchBlankPageCount()
							+ packageBlankPageCount);
			
			buildPdfDocument(this.getFileName() + DistributeSDSConstants.PDF_EXTN, readers);
			pdfReader.close();
			
			cleanUpFiles(homePath, coverSheetName);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
	}
	
	/**
	 * @param distributeQueueBean
	 * @param pdfContentByte
	 * @param font
	 * @param docDate
	 * @throws IOException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	private Document createUSCoverLetter(Document pdfReader, DistributionQueueBean dqBean, Font font,
			String coverSheetName) {
		
		logger.info("Aspose: createUSCoverLetter-Start()");
		
		Aspose.settingAsposeLicense();
		
		pdfReader.getPageInfo().setWidth(PageSize.getA4().getWidth());
		pdfReader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		
		Page pdfPage = pdfReader.getPages().get_Item(1);
		TextFragment textFragDate = new TextFragment();
		textFragDate.setText(dqBean.getTmspLastUpdt().toString());
		textFragDate.getPosition().setXIndent(342);
		textFragDate.getPosition().setYIndent(675);
		
		TextFragment textFragOrderNum = new TextFragment();
		textFragOrderNum.setText(dqBean.getOrderNum());
		textFragOrderNum.getPosition().setXIndent(dqBean.getCoverInfoOffsetX());
		textFragOrderNum.getPosition().setYIndent(dqBean.getCoverInfoOffsetY());
		
		TextFragment textFragSalesInfo = new TextFragment();
		textFragSalesInfo.setText(dqBean.getSalesCustomerNum());
		textFragSalesInfo.getPosition().setXIndent(dqBean.getCoverInfoOffsetX());
		textFragSalesInfo.getPosition().setYIndent(dqBean.getCoverInfoOffsetY() - 13);
		
		TextFragment textFragAttnName = new TextFragment();
		textFragAttnName.setText(dqBean.getAttnName());
		textFragAttnName.getPosition().setXIndent(dqBean.getCoveraddressOffsetX());
		textFragAttnName.getPosition().setYIndent(dqBean.getCoveraddressOffsetY());
		
		TextFragment textFragCompanyName = new TextFragment();
		textFragCompanyName.setText(dqBean.getCompanyName());
		textFragCompanyName.getPosition().setXIndent(dqBean.getCoveraddressOffsetX());
		textFragCompanyName.getPosition().setYIndent(dqBean.getCoveraddressOffsetY() - 13);
		
		TextFragment textFragAddress = new TextFragment();
		textFragAddress.setText(dqBean.getAddress1() + " " + dqBean.getAddress2());
		textFragAddress.getPosition().setXIndent(dqBean.getCoveraddressOffsetX());
		textFragAddress.getPosition().setYIndent(dqBean.getCoveraddressOffsetY() - 26);
		
		TextFragment textFragStateCityCode = new TextFragment();
		textFragStateCityCode
				.setText(dqBean.getCity() + ", " + dqBean.getStateCode() + "    " + dqBean.getZipCode());
		textFragStateCityCode.getPosition().setXIndent(dqBean.getCoveraddressOffsetX());
		textFragStateCityCode.getPosition().setYIndent(dqBean.getCoveraddressOffsetY() - 39);
		
		TextFragment textFragCountryName = new TextFragment();
		textFragCountryName.setText(dqBean.getCountryName());
		textFragCountryName.getPosition().setXIndent(dqBean.getCoveraddressOffsetX());
		textFragCountryName.getPosition().setYIndent(dqBean.getCoveraddressOffsetY() - 52);
		
		TextFragment textFragTemplateAttnName = new TextFragment();
		textFragTemplateAttnName.setText(dqBean.getAttnName());
		if (dqBean.getTemplateID().equals("9")) {
			textFragTemplateAttnName.getPosition().setXIndent(95);
			textFragTemplateAttnName.getPosition().setYIndent(538);
		} else if (dqBean.getTemplateID().equals("4")) {
			textFragTemplateAttnName.getPosition().setXIndent(95);
			textFragTemplateAttnName.getPosition().setYIndent(552);
		} else if (dqBean.getTemplateID().equals("3") || dqBean.getTemplateID().equals("10")) {
			textFragTemplateAttnName.getPosition().setXIndent(95);
			textFragTemplateAttnName.getPosition().setYIndent(513);
		} else {
			textFragTemplateAttnName.getPosition().setXIndent(95);
			textFragTemplateAttnName.getPosition().setYIndent(510);
		}
		
		TextBuilder textBuilder = new TextBuilder(pdfPage);
		
		// append the text fragment to the PDF page
		textBuilder.appendText(textFragDate);
		textBuilder.appendText(textFragOrderNum);
		textBuilder.appendText(textFragSalesInfo);
		textBuilder.appendText(textFragAttnName);
		textBuilder.appendText(textFragCompanyName);
		textBuilder.appendText(textFragAddress);
		textBuilder.appendText(textFragStateCityCode);
		textBuilder.appendText(textFragCountryName);
		textBuilder.appendText(textFragTemplateAttnName);
		
		pdfReader.getPages().add();
		
		// pdfReader.save(fileName);
		logger.info("Aspose: createUSCoverLetter-End()");
		
		return pdfReader;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String strFileName) {
		this.fileName = strFileName;
	}
	
	/**
	 * Method deletes the temporary files created for doc processing
	 * 
	 * @param homePath
	 * @param coversheetFile
	 */
	private void cleanUpFiles(String homePath, String coversheetFile) {
		// delete the coversheet file
		new File(coversheetFile).delete();
		
		File folder = new File(homePath);
		String[] files = folder.list();
		
		// delete seperator Page and blank Page after PDF is built
		for (int i = 0; i < files.length; i++) {
			if (files[i].contains(DistributeSDSConstants.PDF_EXTN)
					&& files[i].contains(DistributeSDSConstants.seperatorPage)) {
				new File(homePath + DistributeSDSConstants.FILE_SEPERATOR + files[i]).delete();
			}
			if (files[i].contains(DistributeSDSConstants.PDF_EXTN)
					&& files[i].contains(DistributeSDSConstants.blankPage)) {
				new File(homePath + DistributeSDSConstants.FILE_SEPERATOR + files[i]).delete();
			}
		}
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private DistributionQueueBean loadQueueDataDetails(ResultSet rs) throws SQLException, IOException {
		DistributionQueueBean queueBean = new DistributionQueueBean();
		// loads each of the reportDetails row into a ReportVo value object
		try {
			queueBean.setPackageID(validateInput(rs.getString("PACKAGE_ID")));
			queueBean.setOpcoCode(validateInput(rs.getString("OPCO_CODE")));
			queueBean.setDocIndexId(validateInput(rs.getString("DOC_INDEX_ID")));
			queueBean.setTemplateID(validateInput(rs.getString("TEMPLATE_ID")));
			queueBean.setCompanyName(validateInput(rs.getNString("COMPANY_NAME")));
			queueBean.setAttnName(validateInput(rs.getNString("ATTN_NAME")));
			queueBean.setAddress1(validateInput(rs.getNString("ADDRESS_1")));
			queueBean.setAddress2(validateInput(rs.getNString("ADDRESS_2")));
			queueBean.setAddress3(validateInput(rs.getNString("ADDRESS_3")));
			queueBean.setAddress4(validateInput(rs.getNString("ADDRESS_4")));
			queueBean.setCity(validateInput(rs.getNString("CITY")));
			queueBean.setStateCode(validateInput(rs.getNString("STATE_CODE")));
			queueBean.setCountryCode(validateInput(rs.getString("COUNTRY_CODE")));
			queueBean.setZipCode(validateInput(rs.getString("ZIP_CODE")));
			queueBean.setSalesSourceCode(validateInput(rs.getString("SALES_SOURCE_CODE")));
			queueBean.setSalesCustomerNum(validateInput(rs.getString("SALES_CUSTOMER_NUM")));
			queueBean.setOrderNum(validateInput(rs.getNString("ORDER_NUM")));
			queueBean.setMediaTypeCode(validateInput(rs.getString("MEDIA_TYPE_CODE")));
			queueBean.setMediaAddress(validateInput(rs.getString("MEDIA_ADDRESS")));
			queueBean.setFaxNumber(validateInput(rs.getString("FAX_NUMBER")));
			queueBean.setEmailAddress(validateInput(rs.getNString("EMAIL_ADDRESS")));
			queueBean.setFtpSite(validateInput(rs.getString("FTP_SITE")));
			queueBean.setFtpUserId(validateInput(rs.getString("FTP_USER_ID")));
			queueBean.setFtpPassword(validateInput(rs.getString("FTP_PASSWORD")));
			queueBean.setFtpDirectory(validateInput(rs.getString("FTP_DIRECTORY")));
			queueBean.setEdiVan(validateInput(rs.getString("EDI_VAN")));
			queueBean.setEdiUserId(validateInput(rs.getString("EDI_USER_ID")));
			queueBean.setEdiPassword(validateInput(rs.getString("EDI_PASSWORD")));
			queueBean.setDocGroupNum(validateInput(rs.getString("DOC_GROUP_NUM")));
			queueBean.setRevisionNumber(rs.getBigDecimal("REVISION_NUMBER"));
			queueBean.setDocGenDate(rs.getTimestamp("DOC_GEN_DATE"));
			queueBean.setStatusDate(rs.getTimestamp("STATUS_DATE"));
			queueBean.setDocTradeName(validateInput(rs.getString("DOC_TRADE_NAME")));
			queueBean.setCdmsLangCode(validateInput(rs.getString("LANGUAGE_CODE")));
			queueBean.setTemplateFormatCode(validateInput(rs.getString("TEMPLATE_FORMAT_CODE")));
			queueBean.setDocTypeCode(validateInput(rs.getString("DOC_TYPE_CODE")));
			queueBean.setRegAgencyCode(validateInput(rs.getString("REG_AGENCY_CODE")));
			queueBean.setDistributionDate(rs.getTimestamp("DISTRIBUTION_DATE"));
			queueBean.setDivisionCode(validateInput(rs.getString("DIVISION_CODE")));
			queueBean.setSendUpdate(validateInput(rs.getString("SEND_UPDATE")));
			queueBean.setManualFlag(validateInput(rs.getString("MANUAL_FLAG")));
			queueBean.setShipDate(rs.getTimestamp("SHIP_DATE"));
			queueBean.setOrderPlacer(validateInput(rs.getString("ORDER_PLACER")));
			queueBean.setUserLastUpdt(validateInput(rs.getString("USER_LAST_UPDT")));
			queueBean.setTmspLastUpdt(rs.getTimestamp("TMSP_LAST_UPDT"));
			queueBean.setAddressId(validateInput(rs.getString("ADDRESS_ID")));
			queueBean.setOrderNumTransText(rs.getNString("ORDER_NUM_TRANS_TEXT"));
			queueBean.setDateTransText(rs.getNString("DATE_TRANS_TEXT"));
			queueBean.setDocGrpNumTransText(rs.getNString("DOC_GRP_NUM_TRANS_TEXT"));
			queueBean.setCoverInfoOffsetX(validateInt(rs.getString("COVERINFOBOX_OFFSET_X")));
			queueBean.setCoverInfoOffsetY(validateInt(rs.getString("COVERINFOBOX_OFFSET_Y")));
			queueBean.setCoveraddressOffsetX(validateInt(rs.getString("COVERADDRESS_OFFSET_X")));
			queueBean.setCoveraddressOffsetY(validateInt(rs.getString("COVERADDRESS_OFFSET_Y")));
			queueBean.setCoveraddressFontsize(validateInt(rs.getString("COVERADDRESS_FONTSIZE")));
			queueBean.setCoverinfoboxFontsize(validateInt(rs.getString("COVERINFOBOX_FONTSIZE")));
			queueBean.setAdhocSenderName(rs.getString("AD_HOC_SENDER_NAME"));
			queueBean.setAdhocSenderPhone(validateInput(rs.getString("AD_HOC_SENDER_PHONE")));
			queueBean.setCountryName(validateInput(rs.getString("COUNTRY_NAME")));
			queueBean.setSapCountryCode(validateInput(rs.getString("SAP_COUNTRY_CODE")));
			queueBean.setSapLanguageCode(validateInput(rs.getString("SAP_LANGUAGE_CODE")));
			queueBean.setPrintSendMethod(validateInput(rs.getString("PRINT_SEND_METHOD").trim()));
			queueBean.setNumCopies(validateInt(rs.getString("NUM_COPIES")));
			queueBean.setPackageDelete(validateInput(rs.getString("PACKAGE_DELETE")));
			queueBean.setAccountNum(validateInput(rs.getString("ACCOUNT_NUM")));
			queueBean.setDeptNum(validateInput(rs.getString("DEPT_NUM")));
		} catch (SQLException e) {
			logger.error("Error in retrieving Country specific Report Data from the Database ", e);
			throw e;
		}
		return queueBean;
	}
	
	/**
	 * Utility method to validate and return an String
	 * 
	 * @param inputString
	 * @return
	 */
	private String validateInput(final String inputString) {
		if (StringUtils.isNotBlank(inputString)) {
			return inputString;
		} else {
			return "";
		}
	}
	
	/**
	 * Utility method to validate and return an Integer
	 * 
	 * @param inputString
	 * @return
	 */
	private int validateInt(final String inputString) {
		if (StringUtils.isNotBlank(inputString)) {
			return Integer.parseInt(inputString);
		} else {
			return 0;
		}
	}
	
	/**
	 * Writes to a report file
	 * 
	 * @param queueVO
	 * @param filePath
	 * @throws IOException
	 */
	private void writeToReportFile(DistributionQueueBean queueVO, String filePath) throws IOException {
		File reportFile = new File(filePath);
		if (!reportFile.exists()) {
			reportFile.createNewFile();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Package id:").append(queueVO.getPackageID()).append(TAB_STRING);
		sb.append("Images:").append(queueVO.getPackagePageCount() - queueVO.getPackageBlankPageCount())
				.append(TAB_STRING);
		sb.append("Blanks: ").append(queueVO.getPackageBlankPageCount()).append(TAB_STRING);
		sb.append("Total: ").append(queueVO.getPackagePageCount()).append(TAB_STRING);
		sb.append("Division Code: ").append(queueVO.getDivisionCode()).append(TAB_STRING);
		sb.append("Dept Num: ").append(queueVO.getDeptNum()).append(TAB_STRING);
		sb.append("Account Num: ").append(queueVO.getAccountNum()).append(TAB_STRING);
		sb.append("Attn Name: ").append(queueVO.getAttnName()).append(TAB_STRING);
		sb.append("Shipp. Address: ").append(queueVO.getAddress1()).append(TAB_STRING);
		sb.append("City: ").append(queueVO.getCity()).append(TAB_STRING);
		sb.append("State Code: ").append(queueVO.getStateCode()).append(TAB_STRING);
		sb.append("Country Code: ").append(queueVO.getCountryCode()).append(TAB_STRING);
		sb.append("Zip Code: ").append(queueVO.getZipCode());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile, true))) {
			bw.write(sb.toString());
			bw.newLine();
		} catch (IOException ioExc) {
			logger.error("Error occurred while writing to report file" + ioExc.getMessage());
			throw ioExc;
		}
	}
	
}
