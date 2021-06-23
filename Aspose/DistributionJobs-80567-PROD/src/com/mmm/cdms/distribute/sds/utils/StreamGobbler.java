/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * Outputs a given type of stream to the file specified
 * 
 * @author Auraavamoudhan S
 * @version 1.0 
 */
public class StreamGobbler extends Thread {
	
	/**
	 * Input stream
	 */
	private InputStream is;

	/**
	 * Type of the stream
	 */
	private String type;

	/**
	 * Output file
	 */
	private String logType;

	private static final Logger LOGGER = Logger.getLogger(StreamGobbler.class);

	
	/**
	 * @param is
	 *            Input stream to be read
	 * @param type
	 *            Type of the stream
	 * @param redirect
	 *            File name to which stream is to be redirected
	 */
	public StreamGobbler(InputStream is, String type, String logType) {
		this.is = is;
		this.type = type;
		this.logType = logType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			LOGGER.debug("logger Type :" + logType);
			// Gets a file writer for the given file name
			String line = null;
			// Appends an empty line for alignment
			// Writes the stream to the file till nothing is there to be read
			while ((line = br.readLine()) != null) {
				if("LOGINFO".equalsIgnoreCase(logType)) {
					LOGGER.debug(line);	
				}else if("LOGERROR".equalsIgnoreCase(logType)){
					LOGGER.error(logType);
				}
				LOGGER.debug(type + "-->" + line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}