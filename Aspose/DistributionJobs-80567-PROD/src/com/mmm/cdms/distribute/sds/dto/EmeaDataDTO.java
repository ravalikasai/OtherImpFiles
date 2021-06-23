/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.dto;


public class EmeaDataDTO {
	
	private String docGroupNums;
	private String orderNums;
	
	/**
	 * @return the docGroupNums
	 */
	public String getDocGroupNums() {
		return docGroupNums;
	}
	
	/**
	 * @param docGroupNums the docGroupNums to set
	 */
	public void setDocGroupNums(String docGroupNums) {
		this.docGroupNums = docGroupNums;
	}
	
	/**
	 * @return the orderNums
	 */
	public String getOrderNums() {
		return orderNums;
	}
	
	/**
	 * @param orderNums the orderNums to set
	 */
	public void setOrderNums(String orderNums) {
		this.orderNums = orderNums;
	}
	
}
