package com.lp.BOBService.model;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Request Object of jco
 * 
 * @author taowe
 * @since 26-June-2019
 */
@Data @NoArgsConstructor
public class CustomerData implements java.io.Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -2688724493894714830L;

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


}
