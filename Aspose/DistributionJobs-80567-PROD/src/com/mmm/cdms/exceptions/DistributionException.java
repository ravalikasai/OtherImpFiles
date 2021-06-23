/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.exceptions;

/**
 * @author A5AK3ZZ
 *
 */
public class DistributionException extends Exception {

	/**
	 * Generated Serial version ID
	 */
	private static final long serialVersionUID = -7871056233321032569L;

	/**
	 * DistributionException constructor to log the message
	 * @param message
	 */
	public DistributionException(final String message) {
		super(message);		
	}
	
	/**
	 * DistributionException constructor to log the cause and the message string
	 * @param message
	 * @param cause
	 */
	public DistributionException(final String message, Throwable cause) {
		super(message, cause);
	}
	
}
