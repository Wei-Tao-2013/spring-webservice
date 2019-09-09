package com.lp.BOBService.selfRegistration.impl;

import com.lp.connector.SAPConnector;
import com.lp.connector.exception.ConnectorException;
import com.lp.connector.impl.SAPConnectorImpl;
import com.lp.connector.model.Address;
import com.lp.connector.model.SAPConnectorRequest;
import com.lp.connector.model.SAPConnectorResponse;
import com.lp.BOBService.model.CustomerData;
import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;
import com.lp.BOBService.selfRegistration.PortalJCO;
import com.lp.BOBService.utils.PortalServiceUtils;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component("portalJCOImpl")
public class PortalJCOImpl implements PortalJCO {

	private static final Location loc = Location.getLocation(PortalJCOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.selfRegistration.PortalJCO#testJcoCall(com.lp.
	 * BOBService.model.Request)
	 */
	@Override
	public Response testJcoCall(Request registrationReq) throws ConnectorException {
		Response RegResponse = new Response();
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		SAPConnectorResponse sapResponse = new SAPConnectorResponse();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		sapResponse = sapConnector.callCustomerValidationTest(sapReq);
		// /convert sapResponse to RegisterResponse
		RegResponse.setCustomerNumber(sapResponse.getCustomerNumber());
		RegResponse.setEmailAddress(sapResponse.getEmailAddress());
		RegResponse.setReturnCRM(sapResponse.getReturnCRM());
		RegResponse.setCustomerTxt(sapResponse.getCustomerTxt());
		return RegResponse;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalJCO#callMandatoryIndicator(com.lp
	 * .BOBService.model.Request)
	 */
	@Override
	public Response callMandatoryIndicator(Request registrationReq) throws ConnectorException {
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		/*
		 * System.out.
		 * println("Call callCustomerValidation from PortalJCOImpl with Json request " +
		 * PortalServiceUtils.converToJson(sapReq));
		 */
		SimpleLogger.trace(Severity.INFO, loc, "Call callMandatoryIndicator from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callMandatoryIndicator(sapReq);
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc, "Response from callMandatoryIndicator to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalJCO#callCustomerRegistration(com.
	 * lp.BOBService.model.Request)
	 */
	@Override
	public Response callCustomerRegistration(Request registrationReq)
			throws ConnectorException {
		// TODO Auto-generated method stub
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		com.lp.connector.model.Address addr = new Address();
		addr = (com.lp.connector.model.Address) PortalServiceUtils.copyProperties(registrationReq.getResiAddress(),
				addr);
		sapReq.setResiAddress(addr);
		SimpleLogger.trace(Severity.INFO, loc, "Call callCustomerRegistration from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callCustomerRegistration(sapReq);
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc, "Response from callCustomerRegistration to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);

		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalJCO#callWorkEamilValiation(com.lp
	 * .BOBService.model.Request)
	 */
	@Override
	public Response callWorkEamilValiation(Request registrationReq) throws ConnectorException {
		// TODO Auto-generated method stub
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		if ("true".equalsIgnoreCase(registrationReq.getVerIndicator())) {
			registrationReq.setVerIndicator("X");
		} else {
			registrationReq.setVerIndicator("");
		}
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		SimpleLogger.trace(Severity.INFO, loc, "Call callWorkEamilValiation from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callWorkEamilValiation(sapReq);
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc, "Response from callWorkEamilValiation to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		CustomerData customerData = new CustomerData();
		customerData = (CustomerData) PortalServiceUtils.copyProperties(sapResponse.getCustomerData(), customerData);
		RegResponse.setCustomerData(customerData);
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalJCO#callInitEmailVerification(com
	 * .lp.BOBService.model.Request)
	 */
	@Override
	public Response callInitEmailVerification(Request registrationReq)
			throws ConnectorException {

		// TODO Auto-generated method stub
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);

		SimpleLogger.trace(Severity.INFO, loc, "Call callInitEmailVerification from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callInitEmailVerification(sapReq);

		SimpleLogger.trace(Severity.INFO, loc, "Response from callInitEmailVerification to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.selfRegistration.PortalJCO#callVerifyToken(com.lp.
	 * BOBService.model.Request)
	 */
	@Override
	public Response callVerifyToken(Request registrationReq) throws ConnectorException {
		// TODO Auto-generated method stub
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		SAPConnector sapConnector = new SAPConnectorImpl();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		SimpleLogger.trace(Severity.INFO, loc, "Call callVerifyToken from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callVerifyToken(sapReq);
		
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc, "Response from callVerifyToken to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;

	}

	public Response callAddressCheck(Request registrationReq) throws ConnectorException {

		// TODO Auto-generated method stub
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		if ("true".equalsIgnoreCase(registrationReq.getGoogleIndicator())) {
			registrationReq.setGoogleIndicator("X");
		} else {
			registrationReq.setGoogleIndicator("");
		}
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		SimpleLogger.trace(Severity.INFO, loc,
				"Call callAddressCheck from PortalJCOImpl with Json request" + PortalServiceUtils.converToJson(sapReq));
		com.lp.connector.model.Address addr = new Address();
		addr = (com.lp.connector.model.Address) PortalServiceUtils.copyProperties(registrationReq.getResiAddress(),
				addr);
		sapReq.setResiAddress(addr);
		SAPConnectorResponse sapResponse = sapConnector.callAddressCheck(sapReq);
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc, "Response from callAddressCheck to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;

	}

	@Override
	public Response callVerifyEmailActivation(Request registrationReq)
			throws ConnectorException {

		// TODO Auto-generated method stub
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);

		SimpleLogger.trace(Severity.INFO, loc, "Call callInitEmailVerification from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callVerifyEmailActivation(sapReq);
		SimpleLogger.trace(Severity.INFO, loc, "Response from callVerifyEmailActivation to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;

	}

	@Override
	public Response callUpdatePassword(Request registrationReq) throws ConnectorException {

		// TODO Auto-generated method stub
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		/*
		 * System.out.
		 * println("Call callInitEmailVerification from PortalJCOImpl with Json request "
		 * + PortalServiceUtils.converToJson(sapReq));
		 */
		SimpleLogger.trace(Severity.INFO, loc, "Call callInitEmailVerification from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		// SAPConnectorResponse sapResponse =
		// sapConnector.callInitEmailVerification(sapReq);
		SAPConnectorResponse sapResponse = new SAPConnectorResponse();
		sapResponse.setCustomerUID("Demo1 User");
		// /convert sapResponse to RegisterResponse
		/*
		 * System.out.
		 * println("Response from callInitEmailVerification to PortalJCOImpl with Json data "
		 * + PortalServiceUtils.converToJson(sapResponse));
		 */
		SimpleLogger.trace(Severity.INFO, loc, "Response from callInitEmailVerification to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;

	}

	/**
	 * convert response from java connector object to web service response
	 * 
	 * @param sapResponse
	 * @return RegResponse
	 */
	private Response convertJcoResponse(SAPConnectorResponse sapResponse) {
		Response RegResponse = new Response();
		RegResponse = (Response) PortalServiceUtils.copyProperties(sapResponse, RegResponse);
		return RegResponse;
	}

	@Override
	public Response callInitAllowEmail(Request registrationReq) throws ConnectorException {
		// TODO Auto-generated method stub
		SAPConnector sapConnector = new SAPConnectorImpl();
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) PortalServiceUtils.copyProperties(registrationReq, sapReq);
		SimpleLogger.trace(Severity.INFO, loc, "Call callInitAllowEmail from PortalJCOImpl with Json request"
				+ PortalServiceUtils.converToJson(sapReq));
		SAPConnectorResponse sapResponse = sapConnector.callInitAllowEmail(sapReq);
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc, "Response from callInitAllowEmail to PortalJCOImpl with Json data"
				+ PortalServiceUtils.converToJson(sapResponse));
		Response RegResponse = convertJcoResponse(sapResponse);
		return RegResponse;
	}

	@Override
	public Response callCustomerValidation(Request paramRegistration)
			throws ConnectorException {
		return null;
	}

}