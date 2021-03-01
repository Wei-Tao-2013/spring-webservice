package com.lp.BOBService.components;

import com.lp.connector.exception.ConnectorException;
import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;

public abstract interface PortalJCO {

	public abstract Response callCustomerValidation(Request paramRegistration) throws ConnectorException;

	public abstract Response callMandatoryIndicator(Request paramRegistration) throws ConnectorException;

	public abstract Response callCustomerRegistration(Request paramRegistration) throws ConnectorException;

	public abstract Response callWorkEamilValiation(Request paramRegistration) throws ConnectorException;

	/*
	 * public abstract Response initPortalUser( Request paramRegistration) throws
	 * ConnectorException;
	 */

	public abstract Response callInitEmailVerification(Request request) throws ConnectorException;

	public abstract Response callVerifyToken(Request request) throws ConnectorException;

	public abstract Response callAddressCheck(Request request) throws ConnectorException;

	public abstract Response callVerifyEmailActivation(Request request) throws ConnectorException;

	public abstract Response callUpdatePassword(Request request) throws ConnectorException;

	public abstract Response testJcoCall(Request request) throws ConnectorException;

	public abstract Response callInitAllowEmail(Request request) throws ConnectorException;

	// get BP info from CRM
	public abstract Response callGetBPInfo(Request request) throws ConnectorException;

	// update BP info into CRM
	public abstract Response callSetBPInfo(Request request) throws ConnectorException;
    
	// get Telstra info by telstra Id

	public abstract Response callFindBusinessPartnerForLogonId(String telstraId) throws ConnectorException;
	// register Telstra info into portal
	public abstract Response callRegisterTheLogonId(Request request) throws ConnectorException;



}