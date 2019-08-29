package com.lp.BOBService.selfRegistration;

import com.lp.connector.exception.ConnectorException;
import com.lp.BOBService.model.RegistrationRequest;
import com.lp.BOBService.model.RegistrationResponse;

public abstract interface PortalJCO {

	public abstract RegistrationResponse callCustomerValidation(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse callMandatoryIndicator(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse callCustomerRegistration(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse callWorkEamilValiation(RegistrationRequest paramRegistration)
			throws ConnectorException;

	/*
	 * public abstract RegistrationResponse initPortalUser( RegistrationRequest
	 * paramRegistration) throws ConnectorException;
	 */

	public abstract RegistrationResponse callInitEmailVerification(RegistrationRequest registrationReq)
			throws ConnectorException;

	public abstract RegistrationResponse callVerifyToken(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse callAddressCheck(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse callVerifyEmailActivation(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse callUpdatePassword(RegistrationRequest paramRegistration)
			throws ConnectorException;

	public abstract RegistrationResponse testJcoCall(RegistrationRequest paramRegistration) throws ConnectorException;

	public abstract RegistrationResponse callInitAllowEmail(RegistrationRequest paramRegistration)
			throws ConnectorException;

}