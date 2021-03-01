package com.lp.connector.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SAPConnectorRequest implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -1542627838960577675L;

	private String customerUID;
	private String emailAddress;
	private String customerNumber;
	private String dateofBirth;
	private String title;
	private Address resiAddress;
	private Address postalAddress;
	private String phoneNumber;
	private String salary;
	private String employeeNumber;
	private String driverLicence;
	private String firstName;
	private String lastName;
	private String password;
	private String perNo;
	private String middleName;
	private String licenceType;
	private String workEmail;
	private String telephoneNumber;
	private String mobileNumber;
	private String userId;
	private String streetName;
	private String strAbbr;
	private String houseNumber;
	private String houseNumber2;
	private String houseNumber3;
	private String strSuppl1;
	private String strSuppl2;
	private String strSuppl3;
	private String location;
	private String building;
	private String floor;
	private String roomNumber;
	private String postalCode;
	private String city;
	private String country;
	private String countryISO;
	private String region;
	private String gender;
	private String ZZQuesetion1;
	private String ZZAnswer1;
	private String ZZQuesetion2;
	private String ZZAnswer2;
	private String resgistrationURL;
	private String refNumber;
	private String verIndicator;
	private String licenceRegion;
	private String googleIndicator; // data from google (true) or manual input (false)
	private String simpleFlag; // inidcator of for password reset process if simpleFlag = ture in case of there
								// is no su01 in CRM
	private String Auth0BPEmail;
	private String telstraId;

}
