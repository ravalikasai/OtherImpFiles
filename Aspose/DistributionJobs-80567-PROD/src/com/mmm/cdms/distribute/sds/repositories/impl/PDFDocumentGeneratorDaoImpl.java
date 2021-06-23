/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.repositories.impl;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aspose.pdf.Document;
import com.aspose.pdf.PageSize;
import com.mmm.aspose.main.Aspose;
import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.beans.DistributionQueueBean;
import com.mmm.cdms.distribute.sds.constants.PropertyFileConstants;
import com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * @author a5ak3zz
 *
 */
public class PDFDocumentGeneratorDaoImpl implements PDFDocumentGeneratorDao {
	
	/**
	 * Logging element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(PDFDocumentGeneratorDaoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#insertDetailsIntoDistributionLog(com.mmm.cdms.distribute.sds.beans.DistributionQueueBean)
	 */
	public void insertDetailsIntoDistributionLog(DistributionQueueBean distributeQueueVO) throws DistributionException {
		Connection conn = null;
		try {
			conn = DistributeSDSUtils.getDBConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				LOGGER.debug(" BEGIN : insertDetailsIntoDistributionLog method");
				String updateDistirbutionLog = DistributeSDSUtils
						.getPropertyValue(PropertyFileConstants.SP_UPDATE_DISTR_LOG);
				
				// update event_id to unique combination of 3
				CallableStatement cs = conn.prepareCall(updateDistirbutionLog);
				cs.setString(1, distributeQueueVO.getPackageID());
				cs.setString(2, distributeQueueVO.getAddressId());
				cs.setString(3, distributeQueueVO.getOpcoCode());
				cs.setString(4, distributeQueueVO.getDocIndexId());
				cs.setString(5, distributeQueueVO.getTemplateID());
				cs.setString(6, distributeQueueVO.getCompanyName());
				cs.setString(7, distributeQueueVO.getAttnName());
				cs.setString(8, distributeQueueVO.getAddress1());
				cs.setString(9, distributeQueueVO.getAddress2());
				cs.setString(10, distributeQueueVO.getAddress3());
				cs.setString(11, distributeQueueVO.getAddress4());
				cs.setString(12, distributeQueueVO.getCity());
				cs.setString(13, distributeQueueVO.getStateCode());
				cs.setString(14, distributeQueueVO.getCountryCode());
				cs.setString(15, distributeQueueVO.getZipCode());
				cs.setString(16, distributeQueueVO.getSalesSourceCode());
				cs.setString(17, distributeQueueVO.getSalesCustomerNum());
				cs.setString(18, distributeQueueVO.getOrderNum());
				cs.setString(19, distributeQueueVO.getMediaTypeCode());
				cs.setString(20, distributeQueueVO.getMediaAddress());
				cs.setString(21, distributeQueueVO.getFaxNumber());
				cs.setString(22, distributeQueueVO.getEmailAddress());
				cs.setString(23, distributeQueueVO.getFtpSite());
				cs.setString(24, distributeQueueVO.getFtpUserId());
				cs.setString(25, distributeQueueVO.getFtpPassword());
				cs.setString(26, distributeQueueVO.getFtpDirectory());
				cs.setString(27, distributeQueueVO.getEdiVan());
				cs.setString(28, distributeQueueVO.getEdiUserId());
				cs.setString(29, distributeQueueVO.getEdiPassword());
				cs.setString(30, distributeQueueVO.getDocGroupNum());
				cs.setBigDecimal(31, distributeQueueVO.getRevisionNumber());
				cs.setTimestamp(32, distributeQueueVO.getDocGenDate());
				cs.setTimestamp(33, distributeQueueVO.getStatusDate());
				cs.setString(34, distributeQueueVO.getDocTradeName());
				cs.setString(35, distributeQueueVO.getCdmsLangCode());
				cs.setString(36, distributeQueueVO.getTemplateFormatCode());
				cs.setString(37, distributeQueueVO.getDocTypeCode());
				cs.setString(38, distributeQueueVO.getRegAgencyCode());
				cs.setTimestamp(39, distributeQueueVO.getDistributionDate());
				cs.setString(40, distributeQueueVO.getDivisionCode());
				cs.setString(41, distributeQueueVO.getSendUpdate());
				cs.setString(42, distributeQueueVO.getManualFlag());
				cs.setTimestamp(43, distributeQueueVO.getShipDate());
				cs.setString(44, distributeQueueVO.getOrderPlacer());
				cs.setString(45, distributeQueueVO.getOrderPlacerPhone());
				cs.setString(46, distributeQueueVO.getAdhocSenderName());
				cs.setString(47, distributeQueueVO.getAdhocSenderPhone());
				cs.setInt(48, 1);
				cs.setString(49, distributeQueueVO.getUserLastUpdt());
				cs.setTimestamp(50, distributeQueueVO.getTmspLastUpdt());
				cs.setInt(51, distributeQueueVO.getPackagePageCount());
				// -
				// nPackageBlankPageCount
				cs.setInt(52, 0);
				cs.setString(53, distributeQueueVO.getPrintSendMethod());
				cs.setInt(54, distributeQueueVO.getNumCopies());
				
				cs.execute();
				LOGGER.debug(" END : insertDetailsIntoDistributionLog method");
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			LOGGER.error("Exception occured in insertDetailsIntoDistributionLog", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				LOGGER.error("Exception occured in insertDetailsIntoDistributionLog", e1);
				throw new DistributionException("Exception occured in insertDetailsIntoDistributionLog", e1);
			}
			throw new DistributionException("Exception occured in insertDetailsIntoDistributionLog", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#getDocumentPDF()
	 */
	@Override
	public ResultSet getDocumentPDF(DistributionQueueBean distributeQueueBean, String docIndexID) throws DistributionException {
		String sdsDocImageSQL = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.DOC_IMAGE_QUERY);
		ResultSet result = null;
		String queryDocIndexID;
		if (StringUtils.isNotBlank(sdsDocImageSQL)) {
			if (docIndexID.equals("0")) {
				LOGGER.debug("using DQ Bean for Doc Index ID");
				LOGGER.debug("Getting the document for doc_index_id : " + distributeQueueBean.getDocIndexId());
				queryDocIndexID = distributeQueueBean.getDocIndexId();
			} else {
				LOGGER.debug("using Kit component Doc Index ID");
				LOGGER.debug("Getting the document for doc_index_id : " + docIndexID);
				queryDocIndexID = docIndexID;
			}
			Connection conn = null;
			try {
				conn = DistributeSDSUtils.getDBConnection();
				if (conn != null) {
					conn.setAutoCommit(false);
					PreparedStatement ps = conn.prepareStatement(sdsDocImageSQL);
					ps.setString(1, queryDocIndexID);
					result = ps.executeQuery();
					if (result == null) {
						LOGGER.debug("No document image for the doc index id: " + distributeQueueBean.getDocIndexId());
						DistributeSDSApplication.pdfDetailsDTO.setReportErrorMessage("No document image for the doc index id: " + distributeQueueBean.getDocIndexId());
					}
				}
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				LOGGER.error("Exception occured in getDocumentPDF. Getting Document PDF failed", e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					LOGGER.error("Exception occured in getDocumentPDF. Getting Document PDF failed", e1);
					throw new DistributionException("Exception occured in getDocumentPDF. Getting Document PDF failed", e1);
				}
				throw new DistributionException("Exception occured in getDocumentPDF. Getting Document PDF failed", e);
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#getKitComponent(com.mmm.cdms.distribute.sds.beans.DistributionQueueBean)
	 */
	@Override
	public List<String> getKitComponents(DistributionQueueBean distributeQueueBean) throws DistributionException {
		List<String> componentIDList = new ArrayList<>();
		String retrieveCompssql = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.SP_GET_KIT_COMPONENT_ID);
		if (StringUtils.isNotBlank(retrieveCompssql)) {
			// For the kit components
			Connection conn = null;
			try {
				conn = DistributeSDSUtils.getDBConnection();
				if (conn != null) {
					conn.setAutoCommit(false);
					CallableStatement cs = conn.prepareCall(retrieveCompssql);
					cs.setString(1, distributeQueueBean.getDocIndexId());
					ResultSet rs = cs.executeQuery();
					while (rs.next()) {
						componentIDList.add(rs.getString(1));
					}
					conn.setAutoCommit(true);
				}
			} catch (SQLException e) {
				LOGGER.error("Exception occured in getKitComponent. Getting Kit Component failed", e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					LOGGER.error("Exception occured in getKitComponent while Rollback", e1);
					throw new DistributionException("Exception occured in getKitComponent while Rollback", e1);
				}
				throw new DistributionException("Exception occured in getKitComponent. Getting Kit Component failed", e);
			}
		}
		return componentIDList;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#updateRulesBasedDocSummary(com.mmm.cdms.distribute.sds.beans.DistributionQueueBean)
	 */
	@Override
	public void updateRulesBasedDocSummary(DistributionQueueBean distributeQueueBean) throws DistributionException {
		if (StringUtils.isBlank(distributeQueueBean.getAdhocSenderName())) {
			String updateRulesBasedDocSum = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.SP_UPDATE_RBDS);
			Connection conn = null;
			try {
				conn = DistributeSDSUtils.getDBConnection();
				if (conn != null) {
					conn.setAutoCommit(false);
					CallableStatement cs = conn.prepareCall(updateRulesBasedDocSum);
					cs.setString(1, distributeQueueBean.getAddressId());
					cs.setString(2, distributeQueueBean.getDocGroupNum());
					cs.setString(3, distributeQueueBean.getOrderNum());
					cs.execute();
				}
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				LOGGER.error("Exception occured in updateRulesBasedDocSummary. ", e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					LOGGER.error("Exception occured in updateRulesBasedDocSummary while RollBack ", e1);
					throw new DistributionException("Exception occured in updateRulesBasedDocSummary while Rollback ", e1);
				}
				throw new DistributionException("Exception occured in updateRulesBasedDocSummary. ", e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#insertDetailsIntoTempTable(int)
	 */
	@Override
	public void insertDetailsIntoTempTable(final String packageId) throws DistributionException {
		LOGGER.debug(" BEGIN : insertDetailsIntoTempTable method for the packageId " + packageId);
		Connection connection = null;
		try {
			connection = DistributeSDSUtils.getDBConnection();
			if (connection != null) {
				connection.setAutoCommit(false);
				String insertDistrQueueCounterEUTempTable = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.INSERT_TEMP_TABLE_QUERY);
				PreparedStatement st = connection.prepareStatement(insertDistrQueueCounterEUTempTable);
				st.setString(1, packageId);
				st.execute();
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			LOGGER.error("Exception occured in insertDetailsIntoTempTable. ", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("Exception occured in insertDetailsIntoTempTable while Rollback ", e1);
				throw new DistributionException("Exception occured in insertDetailsIntoTempTable while Rollback ", e1);
			}
			throw new DistributionException("Exception occured in insertDetailsIntoTempTable. ", e);
		}
		LOGGER.debug(" END : insertDetailsIntoTempTable method for the packageId " + packageId);
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#retrieveDistributionQueueDetails()
	 */
	@Override
	public ResultSet retrieveDistributionQueueDetails() throws DistributionException {
		LOGGER.debug("BEGIN : RETRIEVING Distribution Queue DETAILS ");
		ResultSet distributePackageRS = null;
		Connection conn = null;
		try {
			conn = DistributeSDSUtils.getDBConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String getDistribPackagesProc = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.SP_GET_DISTRIB_PKG_DETAILS);
				CallableStatement cs = conn.prepareCall(getDistribPackagesProc);
				cs.setString(1, DistributeSDSApplication.jobName);
				cs.setString(2, DistributeSDSApplication.mediaTypeString);
				if (StringUtils.isNotBlank(getDistribPackagesProc)) {
					distributePackageRS = cs.executeQuery();
				} else {
					LOGGER.error("Error retrieving sql query getDistribPackagesSql " + getDistribPackagesProc);
				}
			}
			conn.setAutoCommit(true);
		} catch (Exception e) {
			LOGGER.error("Exception while retrieving Queue details ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				LOGGER.error("Exception while retrieving Queue details while Rollback", e1);
				throw new DistributionException("Exception while retrieving Queue details while Rollback", e1);
			}
			throw new DistributionException("Exception while retrieving Queue details ", e);
		}
		LOGGER.debug("END : RETRIEVING Queue DATA DETAILS ");
		return distributePackageRS;
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#getCoverLetterTemplate(int)
	 */
	@Override
	public Document getCoverLetterTemplate(final String addressID) throws DistributionException {
		LOGGER.debug("Retrieving cover letter template for EMEA");
		Aspose.settingAsposeLicense();
		Connection conn = null;
		Document pdfReader = null;		
	//	pdfReader.getPageInfo().setWidth(PageSize.getA4().getWidth());
	//	pdfReader.getPageInfo().setHeight(PageSize.getA4().getHeight());
		try {
			ResultSet cvrLetterRS = null;
			conn = DistributeSDSUtils.getDBConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String getCoverLetterQry = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.GET_CVR_LETTER_IMAGE_QRY);
				String coverLetterQry =  MessageFormat.format(getCoverLetterQry, addressID);
				PreparedStatement prepStmt = conn.prepareStatement(coverLetterQry);
				cvrLetterRS = prepStmt.executeQuery();
				while (cvrLetterRS.next()) {
					pdfReader = new Document((InputStream)cvrLetterRS.getBinaryStream(1));
				}
				conn.setAutoCommit(true);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while retrieving Queue details ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				LOGGER.error("Exception while retrieving Queue details while Rollback", e1);
				throw new DistributionException("Exception while retrieving Queue details while Rollback", e1);
			}
			throw new DistributionException("Exception while retrieving Queue details ", e);
		}
		LOGGER.debug("Retrieving complete for cover letter template for EMEA");
		// pdfReader.save("C:\\Users\\a9myazz\\Desktop\\805BatchJob\\test.pdf");
		return pdfReader;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.repositories.PDFDocumentGeneratorDao#getUSCoverLetterTemplate(int)
	 */
	@Override
	public Document getUSCoverLetterTemplate(final String templateID) throws DistributionException {
		LOGGER.debug("Retrieving cover letter template for US");
		Aspose.settingAsposeLicense();
		Connection conn = null;
		Document pdfReader = null;
		try {
			ResultSet cvrLetterRS = null;
			conn = DistributeSDSUtils.getDBConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String getCoverLetterQry = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.GET_US_CVR_LETTER_IMAGE_QRY);
				String coverLetterQry =  MessageFormat.format(getCoverLetterQry, templateID);
				PreparedStatement prepStmt = conn.prepareStatement(coverLetterQry);
				cvrLetterRS = prepStmt.executeQuery();
			}
			while (cvrLetterRS.next()) {
				pdfReader = new Document((InputStream)cvrLetterRS.getBinaryStream(1));
			}
			conn.setAutoCommit(true);
		} catch (Exception e) {
			LOGGER.error("Exception while retrieving Queue details ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				LOGGER.error("Exception while retrieving Queue details while Rollback", e1);
				throw new DistributionException("Exception while retrieving Queue details while Rollback", e1);
			}
			throw new DistributionException("Exception while retrieving Queue details ", e);
		}
		LOGGER.debug("Retrieving complete cover letter template for US");
		return pdfReader;
	}
	
}
