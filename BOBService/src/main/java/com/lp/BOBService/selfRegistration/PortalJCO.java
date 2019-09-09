package com.lp.BOBService.selfRegistration;

import com.lp.connector.exception.ConnectorException;
import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;

public abstract interface PortalJCO {

	public abstract Response callCustomerValidation(Request paramRegistration)
			throws ConnectorException;

	public abstract Response callMandatoryIndicator(Request paramRegistration)
			throws ConnectorException;

	public abstract Response callCustomerRegistration(Request paramRegistration)
			throws ConnectorException;

	public abstract Response callWorkEamilValiation(Request paramRegistration)
			throws ConnectorException;

	/*
	 * public abstract Response initPortalUser( Request
	 * paramRegistration) throws ConnectorException;
	 */

	public abstract Response callInitEmailVerification(Request registrationReq)
			throws ConnectorException;

	public abstract Response callVerifyToken(Request paramRegistration)
			throws ConnectorException;

	public abstract Response callAddressCheck(Request paramRegistration)
			throws ConnectorException;

	public abstract Response callVerifyEmailActivation(Request paramRegistration)
			throws ConnectorException;

	public abstract Response callUpdatePassword(Request paramRegistration)
			throws ConnectorException;

	public abstract Response testJcoCall(Request paramRegistration) throws ConnectorException;

	public abstract Response callInitAllowEmail(Request paramRegistration)
			throws ConnectorException;

}