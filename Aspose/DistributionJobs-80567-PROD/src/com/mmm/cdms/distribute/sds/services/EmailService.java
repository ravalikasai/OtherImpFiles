/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.services;

import javax.mail.MessagingException;

import com.mmm.cdms.distribute.sds.beans.SysOptionBean;
import com.mmm.cdms.exceptions.DistributionException;

public interface EmailService {
	
	/**
	 * Method to send email without attachments. 
	 * This method is used to send status email, report email etc.
	 * 
	 * @param subject
	 * @param body
	 * @return
	 */
	boolean sendEmail(String subject, final String body, SysOptionBean sysOption);
	
	/**
	 * Method to send email with attachments
	 * 
	 * @param mailServerAddress
	 * @param fromAddress
	 * @param toAddress
	 * @param emailSubject
	 * @param msgBody
	 * @param attachmentFile
	 * @throws MessagingException
	 */
	void sendEMailWithAttachment(final String mailServerAddress, String fromAddress, String toAddress, String emailSubject,
			String msgBody, String attachmentFile) throws DistributionException;
	
	/**
	 * Method to send Email and LOG an event in Event_Log table
	 * @param statusFlag
	 * @param jobName
	 * @param logDir
	 */
	void 
	sendEmailAndLogEvent(String statusFlag, String jobName, String emailSubject, SysOptionBean sysOptions);
	
	/**
	 * Method to create a email subject
	 * @param jobName
	 * @param emailSubject
	 * @return
	 */
	String createEmailSubject(String emailSubject);
	
	/**
	 * Method to create an email body
	 * 
	 * @param transferBean
	 * @param logDir
	 * @return
	 */
	String createEmailBody(String logDir);

	/**
	 * Create email body for US 
	 * 
	 * @return
	 */
	String createEmailBodyForUS();
}
