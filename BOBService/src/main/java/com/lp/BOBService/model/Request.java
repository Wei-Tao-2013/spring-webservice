package com.lp.BOBService.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Object of web service
 * 
 * @author taowe
 * @since 10-Nov-2019
 */
@Data
@NoArgsConstructor
public class Request implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 8918238123163132092L;
	
	private String customerUID;
	private String logonId;
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
	private String newPassword;
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
	private String googleRecaptchaToken;
	// salesforces keys
	private String salesforceAuthCode;
	private String salesforceAccessToken;
	private String salesforceInstanceUrl;
	private String salesforceRefreshToken;
	private String salesforceSignature;

   //caching auth0 Token
   private String secret;
   private String auth0Token;
	
}
