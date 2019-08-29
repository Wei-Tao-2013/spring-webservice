package com.lp.connector.model;

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

	/**
	 * convert request object to string
	 * 
	 * @return the registrationRequest as Json format
	 */

	/**
	 * @return the postalAddress
	 */
	public Address getPostalAddress() {
		return postalAddress;
	}

	/**
	 * @return the simpleFlag
	 */
	public String getSimpleFlag() {
		return simpleFlag;
	}

	/**
	 * @param simpleFlag the simpleFlag to set
	 */
	public void setSimpleFlag(String simpleFlag) {
		this.simpleFlag = simpleFlag;
	}

	/**
	 * @param postalAddress the postalAddress to set
	 */
	public void setPostalAddress(Address postalAddress) {
		this.postalAddress = postalAddress;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the emailAdress
	 */
	public String getEmailAddress() {
		return emailAddress;
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
	 * @return the resiAddress
	 */
	public Address getResiAddress() {
		return resiAddress;
	}

	/**
	 * @param resiAddress the resiAddress to set
	 */
	public void setResiAddress(Address resiAddress) {
		this.resiAddress = resiAddress;
	}

	/**
	 * @return the salary
	 */
	public String getSalary() {
		return salary;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(String salary) {
		this.salary = salary;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the perNo
	 */
	public String getPerNo() {
		return perNo;
	}

	/**
	 * @param perNo the perNo to set
	 */
	public void setPerNo(String perNo) {
		this.perNo = perNo;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	 * @return the workEmail
	 */
	public String getWorkEmail() {
		return workEmail;
	}

	/**
	 * @param workEmail the workEmail to set
	 */
	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the strAbbr
	 */
	public String getStrAbbr() {
		return strAbbr;
	}

	/**
	 * @param strAbbr the strAbbr to set
	 */
	public void setStrAbbr(String strAbbr) {
		this.strAbbr = strAbbr;
	}

	/**
	 * @return the houseNumber2
	 */
	public String getHouseNumber2() {
		return houseNumber2;
	}

	/**
	 * @param houseNumber2 the houseNumber2 to set
	 */
	public void setHouseNumber2(String houseNumber2) {
		this.houseNumber2 = houseNumber2;
	}

	/**
	 * @return the houseNumber
	 */
	public String getHouseNumber() {
		return houseNumber;
	}

	/**
	 * @param houseNumber the houseNumber to set
	 */
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * @return the houseNumber3
	 */
	public String getHouseNumber3() {
		return houseNumber3;
	}

	/**
	 * @param houseNumber3 the houseNumber3 to set
	 */
	public void setHouseNumber3(String houseNumber3) {
		this.houseNumber3 = houseNumber3;
	}

	/**
	 * @return the strSuppl1
	 */
	public String getStrSuppl1() {
		return strSuppl1;
	}

	/**
	 * @param strSuppl1 the strSuppl1 to set
	 */
	public void setStrSuppl1(String strSuppl1) {
		this.strSuppl1 = strSuppl1;
	}

	/**
	 * @return the strSuppl2
	 */
	public String getStrSuppl2() {
		return strSuppl2;
	}

	/**
	 * @param strSuppl2 the strSuppl2 to set
	 */
	public void setStrSuppl2(String strSuppl2) {
		this.strSuppl2 = strSuppl2;
	}

	/**
	 * @return the strSuppl3
	 */
	public String getStrSuppl3() {
		return strSuppl3;
	}

	/**
	 * @param strSuppl3 the strSuppl3 to set
	 */
	public void setStrSuppl3(String strSuppl3) {
		this.strSuppl3 = strSuppl3;
	}

	/**
	 * @return the building
	 */
	public String getBuilding() {
		return building;
	}

	/**
	 * @param building the building to set
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the roomNumber
	 */
	public String getRoomNumber() {
		return roomNumber;
	}

	/**
	 * @param roomNumber the roomNumber to set
	 */
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
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
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the countryISO
	 */
	public String getCountryISO() {
		return countryISO;
	}

	/**
	 * @param countryISO the countryISO to set
	 */
	public void setCountryISO(String countryISO) {
		this.countryISO = countryISO;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the zZQuesetion1
	 */
	public String getZZQuesetion1() {
		return ZZQuesetion1;
	}

	/**
	 * @param zZQuesetion1 the zZQuesetion1 to set
	 */
	public void setZZQuesetion1(String zZQuesetion1) {
		ZZQuesetion1 = zZQuesetion1;
	}

	/**
	 * @return the zZAnswer1
	 */
	public String getZZAnswer1() {
		return ZZAnswer1;
	}

	/**
	 * @param zZAnswer1 the zZAnswer1 to set
	 */
	public void setZZAnswer1(String zZAnswer1) {
		ZZAnswer1 = zZAnswer1;
	}

	/**
	 * @return the zZQuesetion2
	 */
	public String getZZQuesetion2() {
		return ZZQuesetion2;
	}

	/**
	 * @param zZQuesetion2 the zZQuesetion2 to set
	 */
	public void setZZQuesetion2(String zZQuesetion2) {
		ZZQuesetion2 = zZQuesetion2;
	}

	/**
	 * @return the zZAnswer2
	 */
	public String getZZAnswer2() {
		return ZZAnswer2;
	}

	/**
	 * @param zZAnswer2 the zZAnswer2 to set
	 */
	public void setZZAnswer2(String zZAnswer2) {
		ZZAnswer2 = zZAnswer2;
	}

	/**
	 * @return the resgistrationURL
	 */
	public String getResgistrationURL() {
		return resgistrationURL;
	}

	/**
	 * @param resgistrationURL the resgistrationURL to set
	 */
	public void setResgistrationURL(String resgistrationURL) {
		this.resgistrationURL = resgistrationURL;
	}

	/**
	 * @return the refNumber
	 */
	public String getRefNumber() {
		return refNumber;
	}

	/**
	 * @param refNumber the refNumber to set
	 */
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	/**
	 * @return the verIndicator
	 */
	public String getVerIndicator() {
		return verIndicator;
	}

	/**
	 * @param verIndicator the verIndicator to set
	 */
	public void setVerIndicator(String verIndicator) {
		this.verIndicator = verIndicator;
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
	 * @return the googleIndicator
	 */
	public String getGoogleIndicator() {
		return googleIndicator;
	}

	/**
	 * @param googleIndicator the googleIndicator to set
	 */
	public void setGoogleIndicator(String googleIndicator) {
		this.googleIndicator = googleIndicator;
	}

}
