package com.lp.BOBService.service;

import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;

public interface PortalService {

	Response checkEmail(Request Request);

	Response checkCustomerNumber(Request Request);

	Response validateWorkEmail(Request Request);

	Response registerCustomer(Request Request);

	Response testJcoconnection(Request Request);

	Response initialiseUser(Request Request);

	Response verifyInitialUser(Request Request);

	Response verifyToken(Request Request);

	Response verifyAddress(Request Request);

	Response updatePassword(Request Request);

	Response resetPassword(Request Request);

	Response verifyEmailActivation(Request Request);

	Response registerEncryptSession(Request Request);

	String getEncryptSession();

	void killSession();

	void switchGoogleAPI(String indicator);

	Response checkEncryptSession(Request Request);

	Response checkCompleteRegisterPermission(Request Request);
	Response setCompleteRegisterPermission(Request Request);

	Response checkPortalAccountUnique(String loginId);

	String getGoogleGeoTest(String request);

	Response sendVerifyEmail();

	Response validateRecaptcha(Request Request);

	// poc of integration of NZ
	Response getAccessToken(String code);

	Response callSalesforceNZ(Request Request);

	Response createSalesforceNZ(Request Request);

	Response getDrivers(Request Request);

	String getUserGroup(String userId);

	Response updateUser2VerifiedGroups(Request Request);

	// Auth0
	Response createUMEIdentity(Request Request);

	Response validateAccount(String loginId, String password);

	Response getBPinfo(String loginId);
	
	Response setBPinfo(String loginId, String auth0email);

	String getAuth0Token(String secret);
	String storeAuth0Token(String secret, String auth0token);
    
	// Telstra user
	Response findBusinessPartnerByTelstraId(String loginId);
	Response registerTelstraUser(Request request);

}
