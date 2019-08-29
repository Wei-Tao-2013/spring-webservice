package com.lp.connector.model;

/**
 * Request Object of jco
 * 
 * @author taowe
 * @since 08-Apr-2019
 */

public class CustomerNumber implements java.io.Serializable {

	/** For serialization. */

	private static final long serialVersionUID = 1851732803626906403L;

	private String bpId;
	private String description;
	private String descriptionName;
	private String descriptionLong;

	/**
	 * @return the bpId
	 */
	public String getBpId() {
		return bpId;
	}

	/**
	 * @param bpId the bpId to set
	 */
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the descriptionName
	 */
	public String getDescriptionName() {
		return descriptionName;
	}

	/**
	 * @param descriptionName the descriptionName to set
	 */
	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}

	/**
	 * @return the descriptionLong
	 */
	public String getDescriptionLong() {
		return descriptionLong;
	}

	/**
	 * @param descriptionLong the descriptionLong to set
	 */
	public void setDescriptionLong(String descriptionLong) {
		this.descriptionLong = descriptionLong;
	}

	

}
