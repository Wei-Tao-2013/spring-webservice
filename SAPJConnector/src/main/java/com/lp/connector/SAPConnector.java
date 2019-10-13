package com.lp.connector;

import com.lp.connector.exception.ConnectorException;
import com.lp.connector.model.SAPConnectorRequest;
import com.lp.connector.model.SAPConnectorResponse;

public interface SAPConnector {

	public abstract SAPConnectorResponse callCustomerValidation(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callMandatoryIndicator(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callAddressCheck(SAPConnectorRequest SAPConnectData) throws ConnectorException;

	public abstract SAPConnectorResponse callCustomerRegistration(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callWorkEamilValiation(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callInitEmailVerification(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callVerifyToken(SAPConnectorRequest SAPConnectData) throws ConnectorException;

	public abstract SAPConnectorResponse callVerifyEmailActivation(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callUpdatePassword(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	public abstract SAPConnectorResponse callInitAllowEmail(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;

	/* test only */
	public abstract SAPConnectorResponse callCustomerValidationTest(SAPConnectorRequest SAPConnectData)
			throws ConnectorException;
}