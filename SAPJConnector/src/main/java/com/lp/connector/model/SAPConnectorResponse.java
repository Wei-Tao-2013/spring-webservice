package com.lp.connector.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(String mandatory) {
		if ("X".equalsIgnoreCase(mandatory)) {
			this.mandatory = "true";
		} else {
			this.mandatory = "false";
		}

	}

}
