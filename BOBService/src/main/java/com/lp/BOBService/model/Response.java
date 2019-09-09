package com.lp.BOBService.model;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response Object of web service
 * 
 * @author taowe
 * @since 10-SEP-2019
 */
@Data
@NoArgsConstructor
public class Response implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 3465059963450102503L;

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
	private Map<String, List<?>> salseforceResults;
	private String strSalseforceResults;

	// auth user
	private AuthUser authUser;

	public void setReturnCRM(String returnCRM) {
		if ("X".equalsIgnoreCase(returnCRM))
			this.returnCRM = "true";
		else if ("-".equalsIgnoreCase(returnCRM))
			this.returnCRM = "false";
		else {
			this.returnCRM = "false";
		}

	}

}
