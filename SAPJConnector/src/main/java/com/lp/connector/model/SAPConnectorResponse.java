package com.lp.connector.model;

import java.util.List;

public class SAPConnectorResponse implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 8111352672753729362L;

	private String customerUID;
	private String emailAddress;
	private String customerNumber;
	private String customerDesc;
	private String customerDescLong;
	private String customerDescName;
	private String returnCRM;
	private String returnUME;
	private String customerTxt;
	private String appStatus;
	private String errReason;
	private String errCode;
	private String returnVerify;
	private String groupType;
	private String Token;
	private String firstName;
	private String lastName;
	private CustomerData customerData;
	private String mandatory;
	private List<CustomerNumber> customerNumberList;
	private String multCNFound;
	private String hideMultCNMatch;

	/**
	 * @return the emailAdress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the hideMultCNMatch
	 */
	public String getHideMultCNMatch() {
		return hideMultCNMatch;
	}

	/**
	 * @param hideMultCNMatch the hideMultCNMatch to set
	 */
	public void setHideMultCNMatch(String hideMultCNMatch) {
		this.hideMultCNMatch = hideMultCNMatch;
	}

	/**
	 * @return the multCNFound
	 */
	public String getMultCNFound() {
		return multCNFound;
	}

	/**
	 * @param multCNFound the multCNFound to set
	 */
	public void setMultCNFound(String multCNFound) {
		this.multCNFound = multCNFound;
	}

	/**
	 * @param emailAddress the emailAdress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * @return the customerUID
	 */
	public String getCustomerUID() {
		return customerUID;
	}

	/**
	 * @param customerUID the customerUID to set
	 */
	public void setCustomerUID(String customerUID) {
		this.customerUID = customerUID;
	}

	public String getReturnCRM() {
		return returnCRM;
	}

	public void setReturnCRM(String returnCRM) {
		this.returnCRM = returnCRM;
	}

	public String getCustomerTxt() {
		return customerTxt;
	}

	public void setCustomerTxt(String customerTxt) {
		this.customerTxt = customerTxt;
	}

	public String getErrReason() {
		return errReason;
	}

	public void setErrReason(String errReason) {
		this.errReason = errReason;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getCustomerDesc() {
		return customerDesc;
	}

	public void setCustomerDesc(String customerDesc) {
		this.customerDesc = customerDesc;
	}

	public String getCustomerDescLong() {
		return customerDescLong;
	}

	public void setCustomerDescLong(String customerDescLong) {
		this.customerDescLong = customerDescLong;
	}

	public String getCustomerDescName() {
		return customerDescName;
	}

	public void setCustomerDescName(String customerDescName) {
		this.customerDescName = customerDescName;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	/**
	 * @return the returnUME
	 */
	public String getReturnUME() {
		return returnUME;
	}

	/**
	 * @param returnUME the returnUME to set
	 */
	public void setReturnUME(String returnUME) {
		this.returnUME = returnUME;
	}

	/**
	 * @return the returnVerify
	 */
	public String getReturnVerify() {
		return returnVerify;
	}

	/**
	 * @param returnVerify the returnVerify to set
	 */
	public void setReturnVerify(String returnVerify) {
		this.returnVerify = returnVerify;
	}

	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return Token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		Token = token;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the customerData
	 */
	public CustomerData getCustomerData() {
		return customerData;
	}

	/**
	 * @param customerData the customerData to set
	 */
	public void setCustomerData(CustomerData customerData) {
		this.customerData = customerData;
	}

	/**
	 * @return the mandatory
	 */
	public String getMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(String mandatory) {
		if ("X".equalsIgnoreCase(mandatory)) {
			this.mandatory = "true";
		} else {
			this.mandatory = "false";
		}

	}

	/**
	 * @return the customerNumberList
	 */
	public List<CustomerNumber> getCustomerNumberList() {
		return customerNumberList;
	}

	/**
	 * @param customerNumberList the customerNumberList to set
	 */
	public void setCustomerNumberList(List<CustomerNumber> customerNumberList) {
		this.customerNumberList = customerNumberList;
	}

}
