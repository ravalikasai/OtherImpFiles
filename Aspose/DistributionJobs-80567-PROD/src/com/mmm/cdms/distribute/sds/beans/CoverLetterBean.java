/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.beans;

import java.io.InputStream;

/**
 * @author a5ak3zz
 * @version 1.0  01-Aug-2018
 *
 */
public class CoverLetterBean {
	
	private InputStream coverLetterImage;
	private int coveraddressOffsetX;
	private int coveraddressOffsetY;
	private int coverInfoOffsetX;
	private int coverInfoOffsetY;
	private int coveraddressFontsize;
	private int coverinfoboxFontsize;
	private String orderNumTransText;
	private String dateTransText;
	private String docGrpNumTransText;
	
	/**
	 * @return the coverLetterImage
	 */
	public InputStream getCoverLetterImage() {
		return coverLetterImage;
	}
	
	/**
	 * @param coverLetterImage the coverLetterImage to set
	 */
	public void setCoverLetterImage(InputStream coverLetterImage) {
		this.coverLetterImage = coverLetterImage;
	}
	
	/**
	 * @return the coveraddressOffsetX
	 */
	public int getCoveraddressOffsetX() {
		return coveraddressOffsetX;
	}
	
	/**
	 * @param coveraddressOffsetX the coveraddressOffsetX to set
	 */
	public void setCoveraddressOffsetX(int coveraddressOffsetX) {
		this.coveraddressOffsetX = coveraddressOffsetX;
	}
	
	/**
	 * @return the coveraddressOffsetY
	 */
	public int getCoveraddressOffsetY() {
		return coveraddressOffsetY;
	}
	
	/**
	 * @param coveraddressOffsetY the coveraddressOffsetY to set
	 */
	public void setCoveraddressOffsetY(int coveraddressOffsetY) {
		this.coveraddressOffsetY = coveraddressOffsetY;
	}
	
	/**
	 * @return the coverInfoOffsetX
	 */
	public int getCoverInfoOffsetX() {
		return coverInfoOffsetX;
	}
	
	/**
	 * @param coverInfoOffsetX the coverInfoOffsetX to set
	 */
	public void setCoverInfoOffsetX(int coverInfoOffsetX) {
		this.coverInfoOffsetX = coverInfoOffsetX;
	}
	
	/**
	 * @return the coverInfoOffsetY
	 */
	public int getCoverInfoOffsetY() {
		return coverInfoOffsetY;
	}
	
	/**
	 * @param coverInfoOffsetY the coverInfoOffsetY to set
	 */
	public void setCoverInfoOffsetY(int coverInfoOffsetY) {
		this.coverInfoOffsetY = coverInfoOffsetY;
	}
	
	/**
	 * @return the coveraddressFontsize
	 */
	public int getCoveraddressFontsize() {
		return coveraddressFontsize;
	}
	
	/**
	 * @param coveraddressFontsize the coveraddressFontsize to set
	 */
	public void setCoveraddressFontsize(int coveraddressFontsize) {
		this.coveraddressFontsize = coveraddressFontsize;
	}
	
	/**
	 * @return the coverinfoboxFontsize
	 */
	public int getCoverinfoboxFontsize() {
		return coverinfoboxFontsize;
	}
	
	/**
	 * @param coverinfoboxFontsize the coverinfoboxFontsize to set
	 */
	public void setCoverinfoboxFontsize(int coverinfoboxFontsize) {
		this.coverinfoboxFontsize = coverinfoboxFontsize;
	}
	
	/**
	 * @return the orderNumTransText
	 */
	public String getOrderNumTransText() {
		return orderNumTransText;
	}
	
	/**
	 * @param orderNumTransText the orderNumTransText to set
	 */
	public void setOrderNumTransText(String orderNumTransText) {
		this.orderNumTransText = orderNumTransText;
	}
	
	/**
	 * @return the dateTransText
	 */
	public String getDateTransText() {
		return dateTransText;
	}
	
	/**
	 * @param dateTransText the dateTransText to set
	 */
	public void setDateTransText(String dateTransText) {
		this.dateTransText = dateTransText;
	}
	
	/**
	 * @return the docGrpNumTransText
	 */
	public String getDocGrpNumTransText() {
		return docGrpNumTransText;
	}
	
	/**
	 * @param docGrpNumTransText the docGrpNumTransText to set
	 */
	public void setDocGrpNumTransText(String docGrpNumTransText) {
		this.docGrpNumTransText = docGrpNumTransText;
	}
    
	
}
