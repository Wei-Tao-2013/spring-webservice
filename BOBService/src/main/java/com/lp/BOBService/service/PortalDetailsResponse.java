package com.lp.BOBService.service;

import com.lp.BOBService.model.RegistrationRequest;
import com.lp.BOBService.model.RegistrationResponse;

public interface PortalDetailsResponse {

	RegistrationResponse checkEmail(RegistrationRequest registrationRequest);

	RegistrationResponse checkCustomerNumber(RegistrationRequest registrationRequest);

	RegistrationResponse validateWorkEmail(RegistrationRequest registrationRequest);

	RegistrationResponse registerCustomer(RegistrationRequest registrationRequest);

	RegistrationResponse testJcoconnection(RegistrationRequest registrationRequest);

	RegistrationResponse initialiseUser(RegistrationRequest registrationRequest);

	RegistrationResponse verifyInitialUser(RegistrationRequest registrationRequest);

	RegistrationResponse verifyToken(RegistrationRequest registrationRequest);

	RegistrationResponse verifyAddress(RegistrationRequest registrationRequest);

	RegistrationResponse updatePassword(RegistrationRequest registrationRequest);

	RegistrationResponse resetPassword(RegistrationRequest registrationRequest);

	RegistrationResponse verifyEmailActivation(RegistrationRequest registrationRequest);

	RegistrationResponse registerEncryptSession(RegistrationRequest registrationRequest);

	String getEncryptSession();

	void killSession();

	void switchGoogleAPI(String indicator);

	RegistrationResponse checkEncryptSession(RegistrationRequest registrationRequest);

	RegistrationResponse checkCompleteRegisterPermission(RegistrationRequest registrationRequest);

	String getGoogleGeoTest(String request);

	RegistrationResponse sendVerifyEmail();

	RegistrationResponse validateRecaptcha(RegistrationRequest registrationRequest);

	// poc of integration of NZ
	RegistrationResponse getAccessToken(String code);

	RegistrationResponse callSalesforceNZ(RegistrationRequest registrationRequest);

	RegistrationResponse createSalesforceNZ(RegistrationRequest registrationRequest);

	RegistrationResponse getDrivers(RegistrationRequest registrationRequest);

}
