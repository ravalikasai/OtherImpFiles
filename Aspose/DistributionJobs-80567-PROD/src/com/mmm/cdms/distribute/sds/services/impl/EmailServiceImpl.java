/**

 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.DistributeSDSApplication;
import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.distribute.sds.constants.PropertyFileConstants;
import com.mmm.cdms.distribute.sds.repositories.DistributeSDSRepo;
import com.mmm.cdms.distribute.sds.repositories.impl.DistributeSDSRepoImpl;
import com.mmm.cdms.distribute.sds.services.EmailService;
import com.mmm.cdms.distribute.sds.utils.DistributeSDSUtils;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * Holds the methods for sending e-mails 
 * 
 */
public class EmailServiceImpl implements EmailService{
	
	private static final String HTML_TAG_FONT = "</font>";

	private static final String FONT_SIZE_2_LOG_FILE_LOCATION = "<font size=2> logger FILE LOCATION : ";

	private static final String FONT_TD_TR_TR_TD_FONT_SIZE_2 = "</font></td></tr><tr><td><font size=2>";
	
	/**
	 * Logging element for this object
	 */
	public static Logger LOGGER = Logger.getLogger(EmailServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.EmailService#sendEmail(java.lang.String, java.lang.String)
	 */
	public boolean sendEmail(String subject, final String body, SysOptionBean sysOptions) {
		LOGGER.debug("In the sendEmail() - Begin");
		boolean isSuccess = true ;
		try {
			Properties props = new Properties();
			LOGGER.debug("Getting the SMTP name ");
			String smtp = sysOptions.getMailServerAddress();
			String adminEmail = sysOptions.getFromEmailAddress();
			if (smtp != null && adminEmail != null) {
				props.put("mail.smtp.host", smtp);
				
				Session session = Session.getDefaultInstance(props, null);
				Message msg = new MimeMessage(session);
				
				InternetAddress admin = new InternetAddress(adminEmail);
				// Setting the from, to address and subject
				msg.setFrom(admin);
				msg.setRecipient(Message.RecipientType.TO, admin);
				msg.setRecipients(RecipientType.BCC, addAdditionalRecipients());
				subject = "ENV: " + DistributeSDSUtils.environment + " The JOB - " + subject;
				msg.setSubject(subject);
				// Creating the mime body part to set the body
				MimeBodyPart mbp = new MimeBodyPart();
				mbp.setText(body);
				// setting the mime body part to a multipart
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(mbp);
				// Setting the content of the message
				msg.setContent(body,"text/html");
				//msg.setContent(mp);
				LOGGER.debug("All details are set, mail is about to be sent");
				// Sending the MEDIA_TYPE_EMAIL
				Transport.send(msg);
				LOGGER.debug("----Mail sent with the following details-----\n"
						+ "SMTP:" + smtp + "\nTo/From Email Address:"
						+ adminEmail + "\nSubject: " + subject + "\nBody: "
						+ body + "\n---------------");
			} else {
				isSuccess =  false;
				throw new DistributionException("***Error occured when sending mail. Not sufficient info to send EMAIL. \nCheck the SMTP server and "
						+ "admin EMAIL address logged");
			}
		} catch (AddressException e) {
			LOGGER.error("Error occured while sending mail.", e);
			isSuccess =  false;
		} catch (MessagingException e) {
			LOGGER.error("Error occured in sendEmail", e);
			isSuccess =  false;
		} catch (DistributionException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e);
		}
		LOGGER.debug("In the sendEmail() - End");
		return isSuccess ;
	}

	/**
	 * Add additional recipients to the status email recipients list
	 * @return
	 */
	private Address[] addAdditionalRecipients() {
		LOGGER.debug("BEGIN - addAdditionalRecipients()");
		String recipientsList = DistributeSDSUtils.getPropertyValue(PropertyFileConstants.ADDITIONAL_RECIPIENTS);
		InternetAddress[] addressArray = null;
		try {
			addressArray = InternetAddress.parse(recipientsList);
		} catch (AddressException e) {
			LOGGER.error("Error occured in addAdditionalRecipients", e);
		}
		LOGGER.debug("BEGIN - addAdditionalRecipients()");
		return addressArray;
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.EmailService#sendEMailWithAttachment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendEMailWithAttachment(final String mailServerAddress, String fromAddress,
			String toAddress, String emailSubject, String msgBody, String attachmentFile) throws DistributionException {
		boolean debug = false;
		LOGGER.debug("BEGIN - sendMailWithAttachment()");
		// E-mail report & error files to business clients
		Properties props = new Properties();
		props.put("mail.smtp.host", mailServerAddress);
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);
		try {
			MimeMessage message = configureMessage(emailSubject, fromAddress, toAddress, session);
			// create and fill the first message part
			MimeBodyPart emailBody = new MimeBodyPart();
			emailBody.setText(msgBody);
			
			// create and fill the second message part
			MimeBodyPart emailAttachment = new MimeBodyPart();
			if (attachmentFile != null) {
				FileDataSource fds = new FileDataSource(attachmentFile);
				emailAttachment.setDataHandler(new DataHandler(fds));
				emailAttachment.setFileName(fds.getName());
			}
			// create the Multipart and its parts to it
			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(emailBody);
			if (attachmentFile != null) {
				multiPart.addBodyPart(emailAttachment);
			}
			// add the Multipart to the message
			message.setContent(multiPart);
			// send the message
			Transport.send(message);
			LOGGER.debug("Mail send success !!!");
		} catch (MessagingException messagingExc) {
			LOGGER.error("exception occurred while sending mail", messagingExc);
			throw new DistributionException("exception occurred while sending mail", messagingExc);
		}
		LOGGER.debug("END - sendMailWithAttachment()");
	}

	/**
	 * @param subject
	 * @param from
	 * @param emailAddress
	 * @param session
	 * @return
	 * @throws MessagingException
	 * @throws AddressException
	 */
	private MimeMessage configureMessage(final String subject, final String from, final String emailAddress,
			Session session) throws MessagingException, AddressException {
		// create a message
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		return msg;
	}
	
	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.EmailService#sendMailAndLogEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendEmailAndLogEvent(final String statusFlag, final String jobName,
			final String emailSubject, SysOptionBean sysOptions) {
		LOGGER.debug("SENDING E-MAIL");
		String mailBody = null;
		String mailSubject = null;
		if (DistributeSDSConstants.JOB_EXECUTION_SUCCESS_STATUS.equalsIgnoreCase(statusFlag)) {
			mailBody = this.createEmailBody(sysOptions.getLogPath());
			mailSubject = this.createEmailSubject(emailSubject);
		} else if (DistributeSDSConstants.JOB_DELETION_ERROR_STATUS.equalsIgnoreCase(statusFlag)) {
			mailSubject = jobName + " JOB : finished with errors";
			mailBody = "<font size = 2 color=red >" + " ERROR IN MOUNTING DRIVE OR CLEAN UP DATA. " + HTML_TAG_FONT;
		} else if (DistributeSDSConstants.JOB_EXCEPTION_STATUS.equalsIgnoreCase(statusFlag)) {
			mailSubject = jobName + " JOB : finished with errors";
			mailBody = "<font size = 2 color=red >" + " EXCEPTION OCCURRED WHILE EXECUTING JOB ." + "<BR><BR>"
					+ " Please check the log files : " + "<BR>" + sysOptions.getLogPath() + "<BR>"
					+ DistributeSDSApplication.pdfDetailsDTO.getReportErrorMessage() + "<BR>" + DistributeSDSApplication.pdfDetailsDTO.getProcessStatus()
					+ HTML_TAG_FONT;
		} else {
			mailBody = "<font size = 2 color=red >" + " EXCEPTION OCCURRED WHILE EXECUTING JOB ." + "<BR><BR>"
					+ " Please check the log files : " + "<BR>" + sysOptions.getLogPath() + "<BR>"
					+ HTML_TAG_FONT;
		}
		boolean isMailSuccess = this.sendEmail(mailSubject, mailBody, sysOptions);
		if (isMailSuccess) {
			LOGGER.debug("E-Mail has been sent succesfully.");
		} else {
			LOGGER.error("Error in sending E-Mail.");
		}
		// LOG the events status in the event_Log table
		if (mailSubject.indexOf("error") > 0) {
			logEvents(mailSubject.toString(), "For more info, check Log Directory " + sysOptions.getLogPath(), -1, jobName);
		} else {
			logEvents(mailSubject.toString(), "For more info, check Log Directory " + sysOptions.getLogPath(), 0, jobName);
		}
		LOGGER.debug("SENDING E-MAIL");
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmm.cdms.distribute.sds.services.EmailService#formMailSubject(java.
	 * lang.String)
	 */
	public String createEmailSubject(final String emailSubject) {
		LOGGER.debug("formMailSubject");
		StringBuffer mailSubject = new StringBuffer();
		mailSubject.append(emailSubject).append("**").append(DistributeSDSUtils.environment).append("** ENVIRONMENT");
		LOGGER.debug("formMailSubject");
		return mailSubject.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmm.cdms.distribute.sds.services.EmailService#formMailBody(com.mmm.
	 * cdms.distribute.sds.beans.PdfDetailsDTO, java.lang.String)
	 */
	public String createEmailBody(final String logDir) {
		LOGGER.debug("FORMING MAIL CONTENT");
		StringBuilder mailBody = new StringBuilder();
		if (DistributeSDSApplication.pdfDetailsDTO != null) {
			mailBody.append("<table border=0><tr><td><font size=2>");
			mailBody.append("Start Time : " + DistributeSDSApplication.pdfDetailsDTO.getStartTime() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append("</font></td></tr><tr><td>");
			mailBody.append("------------------------------------------------------");
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Total No. of PDF's created : " + DistributeSDSApplication.pdfDetailsDTO.getNoOfPackages() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Total No. of pages in the PDF's : " + DistributeSDSApplication.pdfDetailsDTO.getNoOfPages() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			Map<String, List<String>> transferMap = DistributeSDSApplication.pdfDetailsDTO.getTransferMap();
			Iterator<String> itr = transferMap.keySet().iterator();
			List<String> values;
			while (itr.hasNext()) {
				String key = itr.next();
				mailBody.append(key + "ed  Files List : ");
				values = transferMap.get(key);
				for (int i = 0; i < values.size(); i++) {
					mailBody.append(values.get(i) + "\t");
				}
				mailBody.append(DistributeSDSConstants.LINE_SEPARATOR);
				mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			}
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append(
					"Delete old files status : " + DistributeSDSApplication.pdfDetailsDTO.isClearDataFolderSuccess() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Mounted Drive : " + DistributeSDSApplication.pdfDetailsDTO.isMountSuccess() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Archive New files status : " + DistributeSDSApplication.pdfDetailsDTO.isArchiveSuccess() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Delete New files status : " + DistributeSDSApplication.pdfDetailsDTO.isDeleteSuccess() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Process status : " + DistributeSDSApplication.pdfDetailsDTO.getProcessStatus() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("Report Error Message : " + DistributeSDSApplication.pdfDetailsDTO.getReportErrorMessage() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append(
					"No. of Duplicates Removed : " + DistributeSDSApplication.pdfDetailsDTO.getNoOfDuplicatesRemoved() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append("FTP status : " + DistributeSDSApplication.pdfDetailsDTO.getFtpMessage() + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append(FONT_TD_TR_TR_TD_FONT_SIZE_2);
			mailBody.append(
					"End Time : " + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + DistributeSDSConstants.LINE_SEPARATOR);
			mailBody.append("</font></td></tr></table>");
			mailBody.append("<BR>");
			mailBody.append(FONT_SIZE_2_LOG_FILE_LOCATION + logDir + HTML_TAG_FONT);
		} else {
			mailBody.append("</table>");
			mailBody.append(DistributeSDSConstants.MAIL_BODY_NO_JOB_PROCESSED_ERROR);
			mailBody.append("<BR>");
			mailBody.append(FONT_SIZE_2_LOG_FILE_LOCATION + logDir + HTML_TAG_FONT);
		}
		LOGGER.debug("FORMING MAIL CONTENT");
		return mailBody.toString();
	}

	/* (non-Javadoc)
	 * @see com.mmm.cdms.distribute.sds.services.EmailService#createEmailBodyForUS()
	 */
	public String createEmailBodyForUS() {
		StringBuilder emailBody = new StringBuilder();
		emailBody.append(
				"Please find attached your 3M Material Safety Data Sheet (MSDS). This MSDS is sent by electronic mail per your prior ");
		emailBody.append(System.getProperty("line.separator"));
		emailBody.append(
				"agreement. If you have problems opening the attached file or if you would prefer to receive a paper copy of this MSDS, ");
		emailBody.append(System.getProperty("line.separator"));
		emailBody
				.append("please contact 3M’s eMSDS Administrator at emsdsadmin@mmm.com or 651-736-5875.");
		emailBody.append(System.getProperty("line.separator"));
		emailBody.append(
				"Please also notify 3M if you would prefer to use a different e-mail address to receive future MSDSs.You can access 3M ");
		emailBody.append(System.getProperty("line.separator"));
		
		emailBody.append("MSDSs over the Internet at www.3m.com/MSDSSearch. For any");
		emailBody.append(System.getProperty("line.separator"));
		emailBody.append("other questions regarding your MSDS or 3M products, ");
		emailBody.append(System.getProperty("line.separator"));
		emailBody.append("please contact the 3M Product Information Center at 1-800-364-3577.");
		return emailBody.toString();
	}
	
	/**
	 * Method to logger events
	 * 
	 * @param msgShort
	 * @param msgLong
	 * @param statusCode
	 * @param jobName
	 */
	private void logEvents(final String msgShort, final String msgLong, final int statusCode, final String jobName) {
		DistributeSDSRepo distributeSDSRepo = new DistributeSDSRepoImpl();
		distributeSDSRepo.logEvents(msgShort, msgLong, statusCode, jobName);
	}
	
		
}
