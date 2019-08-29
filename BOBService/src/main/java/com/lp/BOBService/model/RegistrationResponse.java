package com.lp.BOBService.model;

import java.util.List;
import java.util.Map;

/**
 * Response Object of web service
 * 
 * @author taowe
 * @since 10-Nov-2016
 */

public class RegistrationResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6396483772350423484L;

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
	private String googleAPI;
	private String mandatory;
	private String googleRecaptchaSuccess;
	// salesforces keys
	
	private String salesforceAccessToken;
	private String salesforceInstanceUrl;
	private String salesforceRefreshToken;
	private String salesforceSignature;
	private Map<String,List<?>> salseforceResults;
	private String strSalseforceResults;


	/**
	 * @return the emailAdress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	

	

	/**
	 * @return the salseforceResults
	 */
	public Map<String, List<?>> getSalseforceResults() {
		return salseforceResults;
	}

	/**
	 * @param salseforceResults the salseforceResults to set
	 */
	public void setSalseforceResults(Map<String, List<?>> salseforceResults) {
		this.salseforceResults = salseforceResults;
	}

	/**
	 * @return the strSalseforceResults
	 */
	public String getStrSalseforceResults() {
		return strSalseforceResults;
	}

	/**
	 * @param strSalseforceResults the strSalseforceResults to set
	 */
	public void setStrSalseforceResults(String strSalseforceResults) {
		this.strSalseforceResults = strSalseforceResults;
	}

	
	/**
	 * @return the salesforceSignature
	 */
	public String getSalesforceSignature() {
		return salesforceSignature;
	}

	/**
	 * @param salesforceSignature the salesforceSignature to set
	 */
	public void setSalesforceSignature(String salesforceSignature) {
		this.salesforceSignature = salesforceSignature;
	}

	/**
	 * @return the salesforceRefreshToken
	 */
	public String getSalesforceRefreshToken() {
		return salesforceRefreshToken;
	}

	/**
	 * @param salesforceRefreshToken the salesforceRefreshToken to set
	 */
	public void setSalesforceRefreshToken(String salesforceRefreshToken) {
		this.salesforceRefreshToken = salesforceRefreshToken;
	}

	/**
	 * @return the salesforceInstanceUrl
	 */
	public String getSalesforceInstanceUrl() {
		return salesforceInstanceUrl;
	}

	/**
	 * @param salesforceInstanceUrl the salesforceInstanceUrl to set
	 */
	public void setSalesforceInstanceUrl(String salesforceInstanceUrl) {
		this.salesforceInstanceUrl = salesforceInstanceUrl;
	}

	/**
	 * @return the salesforceAccessToken
	 */
	public String getSalesforceAccessToken() {
		return salesforceAccessToken;
	}

	/**
	 * @param salesforceAccessToken the salesforceAccessToken to set
	 */
	public void setSalesforceAccessToken(String salesforceAccessToken) {
		this.salesforceAccessToken = salesforceAccessToken;
	}

	/**
	 * @param emailAdress the emailAdress to set
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

		if ("X".equalsIgnoreCase(returnCRM))
			this.returnCRM = "true";
		else if ("-".equalsIgnoreCase(returnCRM))
			this.returnCRM = "false";
		else {
			this.returnCRM = "false";
		}

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

	public String getCustomerDescName() {
		return customerDescName;
	}

	public void setCustomerDescName(String customerDescName) {
		this.customerDescName = customerDescName;
	}

	public String getCustomerDescLong() {
		return customerDescLong;
	}

	public void setCustomerDescLong(String customerDescLong) {
		this.customerDescLong = customerDescLong;
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
	 * @return the appStatus
	 */
	public String getAppStatus() {
		return appStatus;
	}

	/**
	 * @param appStatus the appStatus to set
	 */
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
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
	 * @return the googleAPI
	 */
	public String getGoogleAPI() {
		return googleAPI;
	}

	/**
	 * @param googleAPI the googleAPI to set
	 */
	public void setGoogleAPI(String googleAPI) {
		this.googleAPI = googleAPI;
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
		this.mandatory = mandatory;
	}

	public String getGoogleRecaptchaSuccess() {
		return googleRecaptchaSuccess;
	}

	public void setGoogleRecaptchaSuccess(String googleRecaptchaSuccess) {
		this.googleRecaptchaSuccess = googleRecaptchaSuccess;
	}

}
