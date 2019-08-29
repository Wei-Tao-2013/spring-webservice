package com.lp.connector.model;

/**
 * Request Object of jco  
 * 
 * @author taowe
 * @since  26-June-2017
 */

public class CustomerData implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 7884223604448715997L;

	private String title;
	private String fullAddress;
	private String licenceType;
	private String licenceRegion;
	private String driverLicence;
	private String employeeNumber;
	private String dateofBirth;
	private String telephoneNumber;
	private String mobileNumber;
	private String streetName;
	private String streetNumber;
	private String postalCode;
	private String suburb;
	private String state;
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the licenceType
	 */
	public String getLicenceType() {
		return licenceType;
	}
	/**
	 * @param licenceType the licenceType to set
	 */
	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}
	/**
	 * @return the licenceRegion
	 */
	public String getLicenceRegion() {
		return licenceRegion;
	}
	/**
	 * @param licenceRegion the licenceRegion to set
	 */
	public void setLicenceRegion(String licenceRegion) {
		this.licenceRegion = licenceRegion;
	}
	/**
	 * @return the driverLicence
	 */
	public String getDriverLicence() {
		return driverLicence;
	}
	/**
	 * @param driverLicence the driverLicence to set
	 */
	public void setDriverLicence(String driverLicence) {
		this.driverLicence = driverLicence;
	}
	/**
	 * @return the employeeNumber
	 */
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	/**
	 * @param employeeNumber the employeeNumber to set
	 */
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	/**
	 * @return the fullAddress
	 */
	public String getFullAddress() {
		return fullAddress;
	}
	/**
	 * @param fullAddress the fullAddress to set
	 */
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	/**
	 * @return the dateofBirth
	 */
	public String getDateofBirth() {
		return dateofBirth;
	}
	/**
	 * @param dateofBirth the dateofBirth to set
	 */
	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}
	/**
	 * @return the telephoneNumber
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	/**
	 * @param telephoneNumber the telephoneNumber to set
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}
	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	/**
	 * @return the streetNumber
	 */
	public String getStreetNumber() {
		return streetNumber;
	}
	/**
	 * @param streetNumber the streetNumber to set
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the suburb
	 */
	public String getSuburb() {
		return suburb;
	}
	/**
	 * @param suburb the suburb to set
	 */
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
}
