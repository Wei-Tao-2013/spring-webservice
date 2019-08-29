package com.lp.connector.impl;

import java.util.List;
import java.util.ArrayList;
import com.lp.connector.SAPConnector;
import com.lp.connector.exception.ConnectorException;
import com.lp.connector.model.CustomerData;
import com.lp.connector.model.SAPConnectorRequest;
import com.lp.connector.model.SAPConnectorResponse;
import com.lp.connector.model.CustomerNumber;
import com.lp.connector.utils.AppConstants;
import com.lp.connector.utils.ConnectorUtils;
import com.lp.connector.utils.ServiceLocator;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

public class SAPConnectorImpl implements SAPConnector {
	private static final Location location = Location.getLocation(SAPConnectorImpl.class);
	private static String ABAP_Z_SELFREG_CUSTNO_CHECK = "Z_SELFREG_CUSTNO_CHECK";
	private static String ABAP_Z_SELFREG_COMPANY_EMAIL_VERIFY = "Z_SELFREG_COMPANY_EMAIL_VERIFY";
	private static String ABAP_Z_SELFREG_DRIVERCREATE = "Z_SELFREG_DRIVERCREATE";
	private static String ABAP_Z_SELFREG_INIT_EMAIL_VERIFY = "Z_SELFREG_INIT_EMAIL_VERIFY";
	private static String ABAP_ZUI_CHANGEPASS_SEND_EMAIL = "ZUI_CHANGEPASS_SEND_EMAIL";
	private static String ABAP_Z_SELFREG_PASSWORD_RESET = "Z_SELFREG_PASSWORD_RESET";
	private static String ABAP_ZUI_CHANGEPASS_VERIFY_EMAIL = "ZUI_CHANGEPASS_VERIFY_EMAIL";
	private static String ABAP_Z_SELFREG_MANDATORY_INDICATOR = "Z_SELFREG_FIELD_CHECK";
	private static String ABAP_Z_SELFREG_ADDRESS_CHECK = "Z_SELFREG_COMPLETE_CHECK";
	private static String ABAP_Z_SELFREG_INIT_ALLOW_EMAIL = "Z_SELFREG_INIT_ALLOW_EMAIL";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.CrmConnector#callCustomerValidation(com.lp.connector.model.
	 * CrmConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callCustomerValidation(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "selfreg-callCustomerValidation";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_CUSTNO_CHECK);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException(
						"Technical issue, " + "Function module " + ABAP_Z_SELFREG_CUSTNO_CHECK + " not found in SAP");
			}

			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_CUST_NO", cConReq.getCustomerNumber());
			jCoFunction.getImportParameterList().setValue("IV_EMAIL", cConReq.getEmailAddress());

			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location, method + "Executing FM[" + ABAP_Z_SELFREG_CUSTNO_CHECK
					+ "] with parameters[" + cConReq.getCustomerNumber() + ", " + cConReq.getEmailAddress() + "]");
			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM
			String rvResult = jCoFunction.getExportParameterList().getString("RV_RESULT");
			String rvCode = jCoFunction.getExportParameterList().getString("RV_RESULT_CODE");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");
			String evSelfregStatus = jCoFunction.getExportParameterList().getString("EV_SELFREG_STATUS");
			String evCustNo = jCoFunction.getExportParameterList().getString("EV_CUSTNO");
			String evCustnoDesc = jCoFunction.getExportParameterList().getString("EV_CUSTNO_DESC");
			String evCustnoDescName = jCoFunction.getExportParameterList().getString("EV_CUSTNO_DESC_NAME");
			String evCustnoDescLong = jCoFunction.getExportParameterList().getString("EV_CUSTNO_DESC_LONG");

			// Fetch multiple customer numbers from CRM
			cConResponse.setMultCNFound("false"); // as defalut
			cConResponse.setHideMultCNMatch("true"); // as default
			String evMultCNFound = jCoFunction.getExportParameterList().getString("EV_MULT_CN_FOUND");
			String evHideMultCNMatch = jCoFunction.getExportParameterList().getString("EV_HIDE_MULT_CN_MATCH");

			SimpleLogger.trace(Severity.DEBUG, location,
					method + " - with Retrieve data Multi CustomerNumber : evMultCNFound:: " + evMultCNFound
							+ "evHideMultCNMatch:: " + evHideMultCNMatch);

			SimpleLogger.trace(Severity.DEBUG, location,
					method + " - with Retrieve data  jCoFunction.getTableParameterList() :: "
							+ jCoFunction.getTableParameterList());

			if ("X".equalsIgnoreCase(evMultCNFound)) {
				cConResponse.setMultCNFound("true");
				if (!"X".equalsIgnoreCase(evHideMultCNMatch)) {
					cConResponse.setHideMultCNMatch("false");
					List<CustomerNumber> customerNumberList = new ArrayList<CustomerNumber>();

					JCoTable multiCustomerNumberMatches = jCoFunction.getTableParameterList()
							.getTable("ET_MULT_CN_MATCHES");
					for (int i = 0; i < multiCustomerNumberMatches.getNumRows(); i++) {
						multiCustomerNumberMatches.setRow(i);
						SimpleLogger.trace(Severity.DEBUG, location,
								method + " - BP_ID:" + multiCustomerNumberMatches.getString("BP_ID"));
						SimpleLogger.trace(Severity.DEBUG, location,
								method + " - DESCRIPTION:" + multiCustomerNumberMatches.getString("DESCRIPTION"));
						SimpleLogger.trace(Severity.DEBUG, location, method + " - DESCRIPTION_NAME ::"
								+ multiCustomerNumberMatches.getString("DESCRIPTION_NAME"));
						SimpleLogger.trace(Severity.DEBUG, location, method + " - DESCRIPTION_LONG ::"
								+ multiCustomerNumberMatches.getString("DESCRIPTION_LONG"));
						CustomerNumber customerNumberObj = new CustomerNumber();
						customerNumberObj.setBpId(multiCustomerNumberMatches.getString("BP_ID"));
						customerNumberObj.setDescription(multiCustomerNumberMatches.getString("DESCRIPTION"));
						customerNumberObj.setDescriptionName(multiCustomerNumberMatches.getString("DESCRIPTION_NAME"));
						customerNumberObj.setDescriptionLong(multiCustomerNumberMatches.getString("DESCRIPTION_LONG"));
						customerNumberList.add(customerNumberObj);
					}
					cConResponse.setCustomerNumberList(customerNumberList);
				}
			}

			// multiCustomerNumberMatches.
			CustomerData customerData = new CustomerData();
			customerData.setDriverLicence(jCoFunction.getExportParameterList().getString("EV_LICENSENO"));
			customerData.setLicenceRegion(jCoFunction.getExportParameterList().getString("EV_LICENSEREGO"));
			customerData.setDateofBirth(jCoFunction.getExportParameterList().getString("EV_DOB"));
			customerData.setEmployeeNumber(jCoFunction.getExportParameterList().getString("EV_EMPLNO"));
			customerData.setTitle(jCoFunction.getExportParameterList().getString("EV_TITLE"));
			customerData.setFullAddress(jCoFunction.getExportParameterList().getString("EV_ADDRESS"));
			customerData.setTelephoneNumber(jCoFunction.getExportParameterList().getString("EV_TELEPHONE"));
			customerData.setMobileNumber(jCoFunction.getExportParameterList().getString("EV_MOBIL"));
			// customer address
			customerData.setStreetName(jCoFunction.getExportParameterList().getString("EV_STREET"));
			customerData.setStreetNumber(jCoFunction.getExportParameterList().getString("EV_HOUSE_NO"));
			customerData.setSuburb(jCoFunction.getExportParameterList().getString("EV_CITY"));
			customerData.setPostalCode(jCoFunction.getExportParameterList().getString("EV_POSTL_COD1"));
			customerData.setState(jCoFunction.getExportParameterList().getString("EV_REGION"));

			cConResponse.setCustomerData(customerData);

			strRetrieve.append("CRM retrieved following values, RV_RESULT[ ").append(rvResult).append(" ], EV_REASON[ ")
					.append(evReason).append(" ], EV_SELFREG_STATUS[ ").append(evSelfregStatus)
					.append(" ], RV_RESULT_CODE[ ").append(rvCode).append(" ], EV_CUSTNO[ ").append(evCustNo)
					.append(" ], EV_CUSTNO_DESC[ ").append(evCustnoDesc).append(" ], EV_CUSTNO_DESC_NAME[ ")
					.append(evCustnoDescName).append(" ], EV_LICENSENO[ ").append(customerData.getDriverLicence())
					.append(" ], EV_LICENSEREGO[ ").append(customerData.getLicenceRegion()).append(" ], EV_DOB[ ")
					.append(customerData.getDateofBirth()).append(" ], EV_EMPLNO[ ")
					.append(customerData.getEmployeeNumber()).append(" ], EV_TITLE[ ").append(customerData.getTitle())
					.append(" ], EV_ADDRESS[ ").append(customerData.getFullAddress()).append(" ], EV_CUSTOMER_DATA[ ")
					.append(customerData.toString()).append(customerData.getFullAddress())
					.append(" ], EV_CUSTNO_DESC_LONG[ ").append(evCustnoDescLong).append(" ]");
			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM

			// Assemble Response Object
			cConResponse.setReturnCRM(rvResult);
			cConResponse.setErrCode(rvCode);
			cConResponse.setErrReason(evReason);
			cConResponse.setAppStatus(evSelfregStatus);
			cConResponse.setCustomerNumber(evCustNo);
			cConResponse.setCustomerDesc(evCustnoDesc);
			cConResponse.setCustomerDescLong(evCustnoDescLong);
			cConResponse.setCustomerDescName(evCustnoDescName);
			cConResponse.setCustomerData(customerData);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.SAPConnector#callMandatoryIndicator(com.lp.connector.model.
	 * SAPConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callMandatoryIndicator(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "selfreg-callMandatoryIndicator";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_MANDATORY_INDICATOR);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException("Technical issue, " + "Function module "
						+ ABAP_Z_SELFREG_MANDATORY_INDICATOR + " not found in SAP");
			}

			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location,
					method + "Executing FM[" + ABAP_Z_SELFREG_MANDATORY_INDICATOR + "] ");

			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM

			String rvMandatory = jCoFunction.getExportParameterList().getString("EV_MANDATORY");

			JCoTable resultTable = jCoFunction.getExportParameterList().getTable("ET_MANDATORY_CHECK");

			// getTableParameterList().getTable("ET_MANDATORY_CHECK");

			for (int i = 0; i < resultTable.getNumRows(); i++, resultTable.nextRow()) {
				String result = resultTable.getString("ZZ_FIELD");
				SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data from table is  field Name :"
						+ resultTable.getString("ZZ_FIELD") + " value is " + resultTable.getString("MANDATORY"));
			}

			strRetrieve.append("CRM retrieved following values, RV_MANDATORY [ ").append(rvMandatory).append(" ] ");
			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM
			// Assemble Response Object
			cConResponse.setMandatory(rvMandatory);

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.connector.SAPConnector#callAddressCheck(com.lp.connector.model.
	 * SAPConnectorRequest)
	 */
	public SAPConnectorResponse callAddressCheck(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "selfreg-callAddressCheck";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_ADDRESS_CHECK);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException(
						"Technical issue, " + "Function module " + ABAP_Z_SELFREG_ADDRESS_CHECK + " not found in SAP");
			}

			// Set parameters for calling FM

			jCoFunction.getImportParameterList().setValue("IV_GOOGLE_IND", cConReq.getGoogleIndicator());

			if (cConReq.getResiAddress() != null) {
				SimpleLogger.trace(Severity.INFO, location, method + " - Assemble ResiAddress to CRM");
				JCoStructure jcoStruIsAddress = jCoFunction.getImportParameterList().getStructure("IS_DRIVER_ADDRESS");
				// long descriptions
				jcoStruIsAddress.setValue("STREET_ADDRESS_LONG", cConReq.getResiAddress().getStreetaddressLong());
				jcoStruIsAddress.setValue("ROUTE_LONG", cConReq.getResiAddress().getRouteLong());
				jcoStruIsAddress.setValue("INTERSECTION_LONG", cConReq.getResiAddress().getIntersectionLong());
				jcoStruIsAddress.setValue("POLITICAL_LONG", cConReq.getResiAddress().getPoliticalLong());
				jcoStruIsAddress.setValue("COUNTRY_LONG", cConReq.getResiAddress().getCountryLong());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_1_LO",
						cConReq.getResiAddress().getAdministrativearealevel1Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_2_LO",
						cConReq.getResiAddress().getAdministrativearealevel2Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_3_LO",
						cConReq.getResiAddress().getAdministrativearealevel3Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_4_LO",
						cConReq.getResiAddress().getAdministrativearealevel4Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_5_LO",
						cConReq.getResiAddress().getAdministrativearealevel5Long());
				jcoStruIsAddress.setValue("COLLOQUIAL_AREA_LONG", cConReq.getResiAddress().getColloquialareaLong());
				jcoStruIsAddress.setValue("LOCALITY_LONG", cConReq.getResiAddress().getLocalityLong());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_1_LONG",
						cConReq.getResiAddress().getSublocalitylevel1Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_2_LONG",
						cConReq.getResiAddress().getSublocalitylevel2Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_3_LONG",
						cConReq.getResiAddress().getSublocalitylevel3Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_4_LONG",
						cConReq.getResiAddress().getSublocalitylevel4Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_5_LONG",
						cConReq.getResiAddress().getSublocalitylevel5Long());
				jcoStruIsAddress.setValue("NEIGHBORHOOD_LONG", cConReq.getResiAddress().getNeighborhoodLong());
				jcoStruIsAddress.setValue("PREMISE_LONG", cConReq.getResiAddress().getPremiseLong());
				jcoStruIsAddress.setValue("SUBPREMISE_LONG", cConReq.getResiAddress().getSubpremiseLong());
				jcoStruIsAddress.setValue("POSTAL_CODE_LONG", cConReq.getResiAddress().getPostalcodeLong());
				jcoStruIsAddress.setValue("NATURAL_FEATURE_LONG", cConReq.getResiAddress().getNaturalfeatureLong());
				jcoStruIsAddress.setValue("AIRPORT_LONG", cConReq.getResiAddress().getAirportLong());
				jcoStruIsAddress.setValue("PARK_LONG", cConReq.getResiAddress().getParkLong());
				jcoStruIsAddress.setValue("POST_BOX_LONG", cConReq.getResiAddress().getPostboxLong());
				jcoStruIsAddress.setValue("STREET_NUMBER_LONG", cConReq.getResiAddress().getStreetnumberLong());
				jcoStruIsAddress.setValue("FLOOR_LONG", cConReq.getResiAddress().getFloorLong());
				jcoStruIsAddress.setValue("ROOM_LONG", cConReq.getResiAddress().getRoomLong());
				// short descriptions
				jcoStruIsAddress.setValue("STREET_ADDRESS_SHORT", cConReq.getResiAddress().getStreetaddressShort());
				jcoStruIsAddress.setValue("ROUTE_SHORT", cConReq.getResiAddress().getRouteShort());
				jcoStruIsAddress.setValue("INTERSECTION_SHORT", cConReq.getResiAddress().getIntersectionShort());
				jcoStruIsAddress.setValue("POLITICAL_SHORT", cConReq.getResiAddress().getPoliticalShort());
				jcoStruIsAddress.setValue("COUNTRY_SHORT", cConReq.getResiAddress().getCountryShort());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_1_SH",
						cConReq.getResiAddress().getAdministrativearealevel1Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_2_SH",
						cConReq.getResiAddress().getAdministrativearealevel2Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_3_SH",
						cConReq.getResiAddress().getAdministrativearealevel3Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_4_SH",
						cConReq.getResiAddress().getAdministrativearealevel4Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_5_SH",
						cConReq.getResiAddress().getAdministrativearealevel5Short());
				jcoStruIsAddress.setValue("COLLOQUIAL_AREA_SHORT", cConReq.getResiAddress().getColloquialareaShort());
				jcoStruIsAddress.setValue("LOCALITY_SHORT", cConReq.getResiAddress().getLocalityShort());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_1_SHORT",
						cConReq.getResiAddress().getSublocalitylevel1Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_2_SHORT",
						cConReq.getResiAddress().getSublocalitylevel2Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_3_SHORT",
						cConReq.getResiAddress().getSublocalitylevel3Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_4_SHORT",
						cConReq.getResiAddress().getSublocalitylevel4Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_5_SHORT",
						cConReq.getResiAddress().getSublocalitylevel5Short());
				jcoStruIsAddress.setValue("NEIGHBORHOOD_SHORT", cConReq.getResiAddress().getNeighborhoodShort());
				jcoStruIsAddress.setValue("PREMISE_SHORT", cConReq.getResiAddress().getPremiseShort());
				jcoStruIsAddress.setValue("SUBPREMISE_SHORT", cConReq.getResiAddress().getSubpremiseShort());
				jcoStruIsAddress.setValue("POSTAL_CODE_SHORT", cConReq.getResiAddress().getPostalcodeShort());
				jcoStruIsAddress.setValue("NATURAL_FEATURE_SHORT", cConReq.getResiAddress().getNaturalfeatureShort());
				jcoStruIsAddress.setValue("AIRPORT_SHORT", cConReq.getResiAddress().getAirportShort());
				jcoStruIsAddress.setValue("PARK_SHORT", cConReq.getResiAddress().getParkShort());
				jcoStruIsAddress.setValue("POST_BOX_SHORT", cConReq.getResiAddress().getPostboxShort());
				jcoStruIsAddress.setValue("STREET_NUMBER_SHORT", cConReq.getResiAddress().getStreetnumberShort());
				jcoStruIsAddress.setValue("FLOOR_SHORT", cConReq.getResiAddress().getFloorShort());
				jcoStruIsAddress.setValue("ROOM_SHORT", cConReq.getResiAddress().getRoomShort());
				jCoFunction.getImportParameterList().setValue("IS_DRIVER_ADDRESS", jcoStruIsAddress);
			}

			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location, method + "Executing FM[" + ABAP_Z_SELFREG_ADDRESS_CHECK + "] ");
			jCoFunction.execute(jCoDestination);
			// Retrieve the return values from CRM
			String rvResult = jCoFunction.getExportParameterList().getString("RV_SUCCESS");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");

			strRetrieve.append("CRM retrieved following values, rvResult [ ").append(rvResult).append(" ] ");

			strRetrieve.append("CRM retrieved following values, RV_SUCCESS[ ").append(rvResult)
					.append(" ], EV_REASON[ ").append(evReason).append(" ]");

			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM
			// Assemble Response Object
			// Assemble Response Object
			cConResponse.setReturnCRM(rvResult);
			cConResponse.setErrReason(evReason);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.SAPConnector#callCustomerValidation(com.lp.connector.model.
	 * SAPConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callVerifyToken(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "password-callVerifyToken";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));
		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_ZUI_CHANGEPASS_VERIFY_EMAIL); 

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException(
						"Technical issue, " + "Function module " + ABAP_ZUI_CHANGEPASS_VERIFY_EMAIL + " not found in SAP");
			}

			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_INIT_EMAIL", cConReq.getEmailAddress());
			jCoFunction.getImportParameterList().setValue("IV_USERID",cConReq.getUserId());
			jCoFunction.getImportParameterList().setValue("IV_VERIFICATION_CODE", cConReq.getRefNumber());
			
		
			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location, method + "Executing FM[" + ABAP_ZUI_CHANGEPASS_VERIFY_EMAIL
					+ "] with parameters[" + cConReq.getEmailAddress() + ", " + cConReq.getUserId() + ", " + cConReq.getRefNumber() + "]");

			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM
			String rvSuccess = jCoFunction.getExportParameterList().getString("RV_SUCCESS");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");
		

			strRetrieve.append("CRM retrieved following values, RV_SUCCESS[ ").append(rvSuccess).append(" ], EV_REASON[ ")
					.append(evReason).append(" ] ");
			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM

			// Assemble Response Object
			cConResponse.setReturnCRM(rvSuccess);
			cConResponse.setErrReason(evReason);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);
			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.connector.SAPConnector#callUpdatePassword(com.lp.connector.model.
	 * SAPConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callUpdatePassword(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "selfreg-callVerifyToken";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_CUSTNO_CHECK); // FM
																												// Module
																												// need
																												// to
																												// udpated

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException(
						"Technical issue, " + "Function module " + ABAP_Z_SELFREG_CUSTNO_CHECK + " not found in SAP");
			}

			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_CUST_NO", cConReq.getCustomerNumber());
			jCoFunction.getImportParameterList().setValue("IV_EMAIL", cConReq.getEmailAddress());

			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location, method + "Executing FM[" + ABAP_Z_SELFREG_CUSTNO_CHECK
					+ "] with parameters[" + cConReq.getCustomerNumber() + ", " + cConReq.getEmailAddress() + "]");

			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM
			String rvResult = jCoFunction.getExportParameterList().getString("RV_RESULT");
			String rvCode = jCoFunction.getExportParameterList().getString("RV_RESULT_CODE");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");
			String evSelfregStatus = jCoFunction.getExportParameterList().getString("EV_SELFREG_STATUS");
			String evCustNo = jCoFunction.getExportParameterList().getString("EV_CUSTNO");
			String evCustnoDesc = jCoFunction.getExportParameterList().getString("EV_CUSTNO_DESC");
			String evCustnoDescName = jCoFunction.getExportParameterList().getString("EV_CUSTNO_DESC_NAME");
			String evCustnoDescLong = jCoFunction.getExportParameterList().getString("EV_CUSTNO_DESC_LONG");

			strRetrieve.append("CRM retrieved following values, RV_RESULT[ ").append(rvResult).append(" ], EV_REASON[ ")
					.append(evReason).append(" ], EV_SELFREG_STATUS[ ").append(evSelfregStatus)
					.append(" ], RV_RESULT_CODE[ ").append(rvCode).append(" ], EV_CUSTNO[ ").append(evCustNo)
					.append(" ], EV_CUSTNO_DESC[ ").append(evCustnoDesc).append(" ], EV_CUSTNO_DESC_NAME[ ")
					.append(evCustnoDescName).append(" ], EV_CUSTNO_DESC_LONG[ ").append(evCustnoDescLong).append(" ]");
			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM

			// Assemble Response Object
			cConResponse.setReturnCRM(rvResult);
			cConResponse.setErrCode(rvCode);
			cConResponse.setErrReason(evReason);
			cConResponse.setAppStatus(evSelfregStatus);
			cConResponse.setCustomerNumber(evCustNo);
			cConResponse.setCustomerDesc(evCustnoDesc);
			cConResponse.setCustomerDescLong(evCustnoDescLong);
			cConResponse.setCustomerDescName(evCustnoDescName);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.SAPConnector#callVerifyEmailActivation(com.lp.connector.
	 * model.SAPConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callVerifyEmailActivation(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "password-callVerifyEmailActivation";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_ZUI_CHANGEPASS_SEND_EMAIL);
			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException("Technical issue, " + "Function module " + ABAP_ZUI_CHANGEPASS_SEND_EMAIL
						+ " not found in SAP");
			}
			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_USERID", cConReq.getEmailAddress());
			jCoFunction.getImportParameterList().setValue("IV_SIMPLE_FLAG", cConReq.getSimpleFlag());
			jCoFunction.getImportParameterList().setValue("IV_FIRSTNAME", cConReq.getFirstName());
			jCoFunction.getImportParameterList().setValue("IV_LASTNAME", cConReq.getLastName());
			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location,
					method + "Executing FM[" + ABAP_ZUI_CHANGEPASS_SEND_EMAIL + "] with parameters["
							+ cConReq.getEmailAddress()+", " + cConReq.getSimpleFlag() + "," + cConReq.getFirstName() + ","
							+ cConReq.getLastName());

			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM
			String rvResult = jCoFunction.getExportParameterList().getString("RV_SUCCESS");
			String rvMsg = jCoFunction.getExportParameterList().getString("EV_RETURN_MSG");

			strRetrieve.append("CRM retrieved following values, RV_SUCCESS[ ").append(rvResult)
					.append(" ], RV_RETURN_MSG[ ").append(rvMsg).append(" ]");

			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM
			// Assemble Response Object
			cConResponse.setReturnCRM(rvResult);
			cConResponse.setErrReason(rvMsg);
		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}

		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.CrmConnector#callCustomerRegistration(com.lp.connector.model
	 * .CrmConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callCustomerRegistration(SAPConnectorRequest cConReq) throws ConnectorException {

		// TODO Auto-generated method stub
		String method = "selfreg-callCustomerRegistration";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_DRIVERCREATE);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException(
						"Technical issue, " + "Function module " + ABAP_Z_SELFREG_DRIVERCREATE + " not found in SAP");
			}

			// Set parameters for calling FM
			JCoStructure jcoStruIsDriverData = jCoFunction.getImportParameterList().getStructure("IS_DRIVER_DATA");
			jcoStruIsDriverData.setValue("CUST_NO", cConReq.getCustomerNumber());
			jcoStruIsDriverData.setValue("PERNO", cConReq.getEmployeeNumber());
			jcoStruIsDriverData.setValue("NAME_F", cConReq.getFirstName());
			jcoStruIsDriverData.setValue("NAME_L", cConReq.getLastName());
			jcoStruIsDriverData.setValue("NAME_M", cConReq.getMiddleName());
			jcoStruIsDriverData.setValue("LICENCE_NO", cConReq.getDriverLicence());
			jcoStruIsDriverData.setValue("LICENCE_TYP", cConReq.getLicenceType());
			jcoStruIsDriverData.setValue("TITLE", cConReq.getTitle());
			jcoStruIsDriverData.setValue("WORK_EMAIL", cConReq.getWorkEmail());
			jcoStruIsDriverData.setValue("TELEPHONE", cConReq.getTelephoneNumber());
			jcoStruIsDriverData.setValue("MOBIL", cConReq.getMobileNumber());
			jcoStruIsDriverData.setValue("USER_ID", cConReq.getUserId());
			jcoStruIsDriverData.setValue("STREET", cConReq.getStreetName());
			jcoStruIsDriverData.setValue("STR_ABBR", cConReq.getStrAbbr());
			jcoStruIsDriverData.setValue("HOUSE_NO", cConReq.getHouseNumber());
			jcoStruIsDriverData.setValue("HOUSE_NO2", cConReq.getHouseNumber2());
			jcoStruIsDriverData.setValue("HOUSE_NO3", cConReq.getHouseNumber3());
			jcoStruIsDriverData.setValue("STR_SUPPL1", cConReq.getStrSuppl1());
			jcoStruIsDriverData.setValue("STR_SUPPL2", cConReq.getStrSuppl2());
			jcoStruIsDriverData.setValue("STR_SUPPL3", cConReq.getStrSuppl3());
			jcoStruIsDriverData.setValue("LOCATION", cConReq.getLocation());
			jcoStruIsDriverData.setValue("BUILDING", cConReq.getBuilding());
			jcoStruIsDriverData.setValue("ROOM_NO", cConReq.getRoomNumber());
			jcoStruIsDriverData.setValue("POSTL_COD1", cConReq.getPostalCode());
			jcoStruIsDriverData.setValue("CITY", cConReq.getCity());
			jcoStruIsDriverData.setValue("COUNTRY", cConReq.getCountry());
			jcoStruIsDriverData.setValue("COUNTRYISO", cConReq.getCountryISO());
			jcoStruIsDriverData.setValue("REGION", cConReq.getRegion());
			jcoStruIsDriverData.setValue("BIRTHDT", cConReq.getDateofBirth());
			jcoStruIsDriverData.setValue("GENDER", cConReq.getGender());
			jcoStruIsDriverData.setValue("EMAIL", cConReq.getEmailAddress());
			jcoStruIsDriverData.setValue("ZZQUESTION1", cConReq.getZZQuesetion1());
			jcoStruIsDriverData.setValue("ZZANSWER1", cConReq.getZZAnswer1());
			jcoStruIsDriverData.setValue("ZZQUESTION2", cConReq.getZZQuesetion2());
			jcoStruIsDriverData.setValue("ZZANSWER2", cConReq.getZZAnswer2());
			jcoStruIsDriverData.setValue("LICENCE_REGO", cConReq.getLicenceRegion());
			// jcoStruIsDriverData.setValue("PERNO",cConReq.getZZAnswer2());
			jCoFunction.getImportParameterList().setValue("IS_DRIVER_DATA", jcoStruIsDriverData);

			if (cConReq.getResiAddress() != null) {
				SimpleLogger.trace(Severity.INFO, location, method + " - Assemble ResiAddress to CRM");
				JCoStructure jcoStruIsAddress = jCoFunction.getImportParameterList().getStructure("IS_DRIVER_ADDRESS");
				// long descriptions
				jcoStruIsAddress.setValue("STREET_ADDRESS_LONG", cConReq.getResiAddress().getStreetaddressLong());
				jcoStruIsAddress.setValue("ROUTE_LONG", cConReq.getResiAddress().getRouteLong());
				jcoStruIsAddress.setValue("INTERSECTION_LONG", cConReq.getResiAddress().getIntersectionLong());
				jcoStruIsAddress.setValue("POLITICAL_LONG", cConReq.getResiAddress().getPoliticalLong());
				jcoStruIsAddress.setValue("COUNTRY_LONG", cConReq.getResiAddress().getCountryLong());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_1_LO",
						cConReq.getResiAddress().getAdministrativearealevel1Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_2_LO",
						cConReq.getResiAddress().getAdministrativearealevel2Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_3_LO",
						cConReq.getResiAddress().getAdministrativearealevel3Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_4_LO",
						cConReq.getResiAddress().getAdministrativearealevel4Long());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_5_LO",
						cConReq.getResiAddress().getAdministrativearealevel5Long());
				jcoStruIsAddress.setValue("COLLOQUIAL_AREA_LONG", cConReq.getResiAddress().getColloquialareaLong());
				jcoStruIsAddress.setValue("LOCALITY_LONG", cConReq.getResiAddress().getLocalityLong());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_1_LONG",
						cConReq.getResiAddress().getSublocalitylevel1Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_2_LONG",
						cConReq.getResiAddress().getSublocalitylevel2Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_3_LONG",
						cConReq.getResiAddress().getSublocalitylevel3Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_4_LONG",
						cConReq.getResiAddress().getSublocalitylevel4Long());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_5_LONG",
						cConReq.getResiAddress().getSublocalitylevel5Long());
				jcoStruIsAddress.setValue("NEIGHBORHOOD_LONG", cConReq.getResiAddress().getNeighborhoodLong());
				jcoStruIsAddress.setValue("PREMISE_LONG", cConReq.getResiAddress().getPremiseLong());
				jcoStruIsAddress.setValue("SUBPREMISE_LONG", cConReq.getResiAddress().getSubpremiseLong());
				jcoStruIsAddress.setValue("POSTAL_CODE_LONG", cConReq.getResiAddress().getPostalcodeLong());
				jcoStruIsAddress.setValue("NATURAL_FEATURE_LONG", cConReq.getResiAddress().getNaturalfeatureLong());
				jcoStruIsAddress.setValue("AIRPORT_LONG", cConReq.getResiAddress().getAirportLong());
				jcoStruIsAddress.setValue("PARK_LONG", cConReq.getResiAddress().getParkLong());
				jcoStruIsAddress.setValue("POST_BOX_LONG", cConReq.getResiAddress().getPostboxLong());
				jcoStruIsAddress.setValue("STREET_NUMBER_LONG", cConReq.getResiAddress().getStreetnumberLong());
				jcoStruIsAddress.setValue("FLOOR_LONG", cConReq.getResiAddress().getFloorLong());
				jcoStruIsAddress.setValue("ROOM_LONG", cConReq.getResiAddress().getRoomLong());
				// short descriptions
				jcoStruIsAddress.setValue("STREET_ADDRESS_SHORT", cConReq.getResiAddress().getStreetaddressShort());
				jcoStruIsAddress.setValue("ROUTE_SHORT", cConReq.getResiAddress().getRouteShort());
				jcoStruIsAddress.setValue("INTERSECTION_SHORT", cConReq.getResiAddress().getIntersectionShort());
				jcoStruIsAddress.setValue("POLITICAL_SHORT", cConReq.getResiAddress().getPoliticalShort());
				jcoStruIsAddress.setValue("COUNTRY_SHORT", cConReq.getResiAddress().getCountryShort());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_1_SH",
						cConReq.getResiAddress().getAdministrativearealevel1Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_2_SH",
						cConReq.getResiAddress().getAdministrativearealevel2Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_3_SH",
						cConReq.getResiAddress().getAdministrativearealevel3Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_4_SH",
						cConReq.getResiAddress().getAdministrativearealevel4Short());
				jcoStruIsAddress.setValue("ADMINISTRATIVE_AREA_LEVEL_5_SH",
						cConReq.getResiAddress().getAdministrativearealevel5Short());
				jcoStruIsAddress.setValue("COLLOQUIAL_AREA_SHORT", cConReq.getResiAddress().getColloquialareaShort());
				jcoStruIsAddress.setValue("LOCALITY_SHORT", cConReq.getResiAddress().getLocalityShort());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_1_SHORT",
						cConReq.getResiAddress().getSublocalitylevel1Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_2_SHORT",
						cConReq.getResiAddress().getSublocalitylevel2Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_3_SHORT",
						cConReq.getResiAddress().getSublocalitylevel3Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_4_SHORT",
						cConReq.getResiAddress().getSublocalitylevel4Short());
				jcoStruIsAddress.setValue("SUBLOCALITY_LEVEL_5_SHORT",
						cConReq.getResiAddress().getSublocalitylevel5Short());
				jcoStruIsAddress.setValue("NEIGHBORHOOD_SHORT", cConReq.getResiAddress().getNeighborhoodShort());
				jcoStruIsAddress.setValue("PREMISE_SHORT", cConReq.getResiAddress().getPremiseShort());
				jcoStruIsAddress.setValue("SUBPREMISE_SHORT", cConReq.getResiAddress().getSubpremiseShort());
				jcoStruIsAddress.setValue("POSTAL_CODE_SHORT", cConReq.getResiAddress().getPostalcodeShort());
				jcoStruIsAddress.setValue("NATURAL_FEATURE_SHORT", cConReq.getResiAddress().getNaturalfeatureShort());
				jcoStruIsAddress.setValue("AIRPORT_SHORT", cConReq.getResiAddress().getAirportShort());
				jcoStruIsAddress.setValue("PARK_SHORT", cConReq.getResiAddress().getParkShort());
				jcoStruIsAddress.setValue("POST_BOX_SHORT", cConReq.getResiAddress().getPostboxShort());
				jcoStruIsAddress.setValue("STREET_NUMBER_SHORT", cConReq.getResiAddress().getStreetnumberShort());
				jcoStruIsAddress.setValue("FLOOR_SHORT", cConReq.getResiAddress().getFloorShort());
				jcoStruIsAddress.setValue("ROOM_SHORT", cConReq.getResiAddress().getRoomShort());
				jCoFunction.getImportParameterList().setValue("IS_DRIVER_ADDRESS", jcoStruIsAddress);
			}

			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location, method + " - Executing FM[" + ABAP_Z_SELFREG_DRIVERCREATE
					+ "] with parameters[" + ConnectorUtils.converToJson(cConReq));
			jCoFunction.execute(jCoDestination);
			// Retrieve the return values from CRM

			String evSucces = jCoFunction.getExportParameterList().getString("EV_SUCCESS");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");
			String evStatus = jCoFunction.getExportParameterList().getString("EV_STATUS");

			strRetrieve.append("CRM retrieved following values, RV_SUCCESS[ ").append(evSucces)
					.append(" ], EV_REASON[ ").append(evReason).append(" ]").append(" , EV_STATUS[ ").append(evStatus)
					.append(" ]");
			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM
			// Assemble Response Object
			cConResponse.setReturnCRM(evSucces);
			cConResponse.setErrReason(evReason);
			cConResponse.setAppStatus(evStatus);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " data retrieve with "
							+ strRetrieve.toString() + " response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.CrmConnector#callWorkEamilValiation(com.lp.connector.model.
	 * CrmConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callWorkEamilValiation(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "selfreg-callWorkEamilValiation";
		location.entering(method);
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuilder strRetrieve = new StringBuilder();
		StringBuilder strParas = new StringBuilder();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();
		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_COMPANY_EMAIL_VERIFY);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException("Technical issue, " + "Function module "
						+ ABAP_Z_SELFREG_COMPANY_EMAIL_VERIFY + " not found in SAP");
			}

			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_WORK_EMAIL", cConReq.getWorkEmail());
			jCoFunction.getImportParameterList().setValue("IV_REF_NUMBER", cConReq.getRefNumber());
			jCoFunction.getImportParameterList().setValue("IV_REGISTRATION_URL", cConReq.getResgistrationURL());
			jCoFunction.getImportParameterList().setValue("IV_FIRSTNAME", cConReq.getFirstName());
			jCoFunction.getImportParameterList().setValue("IV_LASTNAME", cConReq.getLastName());
			jCoFunction.getImportParameterList().setValue("IV_VERIF_INDICATOR", cConReq.getVerIndicator());
			jCoFunction.getImportParameterList().setValue("IV_CUSTOMERNUMBER", cConReq.getCustomerNumber());
			jCoFunction.getImportParameterList().setValue("IV_PERS_EMAIL", cConReq.getEmailAddress());

			// Execute the FM
			strParas.append("Executing FM [ ").append(ABAP_Z_SELFREG_COMPANY_EMAIL_VERIFY).append(" ] with parameters ")
					.append("IV_WORK_EMAIL[ ").append(cConReq.getEmailAddress()).append(" ], IV_REF_NUMBER [ ")
					.append(cConReq.getRefNumber()).append(" ],IV_REGISTRATION_URL [ ")
					.append(cConReq.getResgistrationURL()).append(" ],IV_VERIF_INDICATOR [ ")
					.append(cConReq.getVerIndicator()).append(" ],IV_FIRSTNAME [ ").append(cConReq.getCustomerNumber())
					.append(" ],IV_FIRSTNAME [ ").append(cConReq.getFirstName()).append(" ],IV_LASTNAME [ ")
					.append(cConReq.getLastName()).append(" ] ").append(" ,IV_PERS_EMAIL [ ")
					.append(cConReq.getEmailAddress()).append(" ] ");

			SimpleLogger.trace(Severity.INFO, location, method + strParas.toString());
			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM

			String rvSucces = jCoFunction.getExportParameterList().getString("RV_SUCCESS");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");
			String evCode = jCoFunction.getExportParameterList().getString("RV_RESULT_CODE");
			CustomerData customerData = new CustomerData();
			customerData.setDriverLicence(jCoFunction.getExportParameterList().getString("EV_LICENSENO"));
			customerData.setLicenceRegion(jCoFunction.getExportParameterList().getString("EV_LICENSEREGO"));
			customerData.setDateofBirth(jCoFunction.getExportParameterList().getString("EV_DOB"));
			customerData.setEmployeeNumber(jCoFunction.getExportParameterList().getString("EV_EMPLNO"));
			customerData.setTitle(jCoFunction.getExportParameterList().getString("EV_TITLE"));
			customerData.setFullAddress(jCoFunction.getExportParameterList().getString("EV_ADDRESS"));
			customerData.setTelephoneNumber(jCoFunction.getExportParameterList().getString("EV_TELEPHONE"));
			customerData.setMobileNumber(jCoFunction.getExportParameterList().getString("EV_MOBIL"));

			customerData.setStreetName(jCoFunction.getExportParameterList().getString("EV_STREET"));
			customerData.setStreetNumber(jCoFunction.getExportParameterList().getString("EV_HOUSE_NO"));
			customerData.setSuburb(jCoFunction.getExportParameterList().getString("EV_CITY"));
			customerData.setPostalCode(jCoFunction.getExportParameterList().getString("EV_POSTL_COD1"));
			customerData.setState(jCoFunction.getExportParameterList().getString("EV_REGION"));

			cConResponse.setCustomerData(customerData);

			strRetrieve.append("CRM retrieved following values, RV_SUCCESS[ ").append(rvSucces)
					.append(" ], EV_LICENSENO[ ").append(customerData.getDriverLicence()).append(" ], EV_LICENSEREGO[ ")
					.append(customerData.getLicenceRegion()).append(" ], EV_DOB[ ")
					.append(customerData.getDateofBirth()).append(" ], EV_EMPLNO[ ")
					.append(customerData.getEmployeeNumber()).append(" ], EV_TITLE[ ").append(customerData.getTitle())
					.append(" ], EV_ADDRESS[ ").append(customerData.getFullAddress()).append(" ], EV_REASON[ ")
					.append(evReason).append(" ]");

			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM

			// Assemble Response Object
			cConResponse.setReturnCRM(rvSucces);
			cConResponse.setErrReason(evReason);
			cConResponse.setErrCode(evCode);
			cConResponse.setCustomerData(customerData);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + "request with " + ConnectorUtils.converToJson(cConReq) + "Data retrieve with "
							+ strRetrieve.toString() + "response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);

			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.CrmConnector#callInitEmailVerification(com.lp.connector.
	 * model.CrmConnectorRequest)
	 */
	@Override
	public SAPConnectorResponse callInitEmailVerification(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub

		String method = "selfreg-callInitEmailVerification";
		location.entering(method);
		cConReq.setPassword("####");
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));

		// Call Function Module
		StringBuilder strRetrieve = new StringBuilder();
		StringBuilder strParas = new StringBuilder();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();
		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_INIT_EMAIL_VERIFY);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException("Technical issue, " + "Function module " + ABAP_Z_SELFREG_INIT_EMAIL_VERIFY
						+ " not found in SAP");
			}

			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_INIT_EMAIL", cConReq.getEmailAddress());
			jCoFunction.getImportParameterList().setValue("IV_VERIFICATION_CODE", cConReq.getRefNumber());
			jCoFunction.getImportParameterList().setValue("IV_FIRSTNAME", cConReq.getFirstName());
			jCoFunction.getImportParameterList().setValue("IV_LASTNAME", cConReq.getLastName());
			jCoFunction.getImportParameterList().setValue("IV_VERIF_INDICATOR", cConReq.getVerIndicator());
			jCoFunction.getImportParameterList().setValue("IV_CUSTOMERNUMBER", cConReq.getCustomerNumber());

			// jCoFunction.getImportParameterList().getStructure("").s
			// Execute the FM
			strParas.append("Executing FM [ ").append(ABAP_Z_SELFREG_INIT_EMAIL_VERIFY).append(" ] with parameters ")
					.append("IV_INIT_EMAIL[ ").append(cConReq.getEmailAddress()).append(" ], IV_VERIFICATION_CODE [ ")
					.append(cConReq.getRefNumber()).append(" ], IV_VERIF_INDICATOR [ ")
					.append(cConReq.getVerIndicator()).append(" ], IV_CUSTOMERNUMBER [ ")
					.append(cConReq.getCustomerNumber()).append(" ],IV_FIRSTNAME [ ").append(cConReq.getFirstName())
					.append(" ],IV_LASTNAME [ ").append(cConReq.getLastName()).append(" ] ");

			SimpleLogger.trace(Severity.INFO, location, method + strParas.toString());
			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM

			String rvSucces = jCoFunction.getExportParameterList().getString("RV_SUCCESS");
			String evReason = jCoFunction.getExportParameterList().getString("EV_REASON");

			strRetrieve.append("CRM retrieved following values, RV_SUCCESS[ ").append(rvSucces)
					.append(" ], EV_REASON[ ").append(evReason).append(" ]");

			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM

			// Assemble Response Object
			cConResponse.setReturnCRM(rvSucces);
			cConResponse.setErrReason(evReason);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + "request with " + ConnectorUtils.converToJson(cConReq) + "Data retrieve with "
							+ strRetrieve.toString() + "response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);
			cConResponse.setReturnCRM("false"); //// Error type 1 Call LP ...
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

	/* test only */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.connector.CrmConnector#callCustomerValidationTest(com.lp.connector.
	 * model.CrmConnectorRequest)
	 */
	public SAPConnectorResponse callCustomerValidationTest(SAPConnectorRequest cConReq) throws ConnectorException {
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		cConResponse.setReturnCRM("false");
		cConResponse.setErrReason("not found");
		cConResponse.setAppStatus("completed");
		cConResponse.setCustomerDesc("Jenney S");
		cConResponse.setCustomerDescLong("Jeneey sss");
		cConResponse.setCustomerDescName("Jenney DeseName");
		cConResponse.setCustomerNumber(cConReq.getCustomerNumber());
		cConResponse.setCustomerUID(cConReq.getCustomerUID());
		cConResponse.setCustomerTxt("customer txt: ");
		System.out.print(ConnectorUtils.converToJson(cConReq));

		/*
		 * if (true) { throw new ConnectorException("Technical issue, " +
		 * "Function module " + ABAP_Z_SELFREG_CUSTNO_CHECK + "not found in SAP"); }
		 */

		return cConResponse;
	}

	@Override
	public SAPConnectorResponse callInitAllowEmail(SAPConnectorRequest cConReq) throws ConnectorException {
		// TODO Auto-generated method stub
		String method = "selfreg-callInitAllowEmail";
		location.entering(method);
		cConReq.setPassword("####");
		SimpleLogger.trace(Severity.INFO, location,
				method + " - with request parameters :" + ConnectorUtils.converToJson(cConReq));
		// Call Function Module
		StringBuffer strRetrieve = new StringBuffer();
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		JCoDestination jCoDestination = serviceLocator.getJavaConnectorObject();

		try {
			// Call Function Module
			JCoFunction jCoFunction = jCoDestination.getRepository().getFunction(ABAP_Z_SELFREG_INIT_ALLOW_EMAIL);

			// If Call Function module is null, then throw error or return
			if (jCoFunction == null) {
				// TODO ("Function module not found in SAP.");
				throw new ConnectorException("Technical issue, " + "Function module " + ABAP_Z_SELFREG_INIT_ALLOW_EMAIL
						+ " not found in SAP");
			}
			// Set parameters for calling FM
			jCoFunction.getImportParameterList().setValue("IV_INIT_EMAIL", cConReq.getEmailAddress());

			// Execute the FM
			SimpleLogger.trace(Severity.INFO, location, method + " Executing FM[" + ABAP_Z_SELFREG_INIT_ALLOW_EMAIL
					+ "] with parameters[" + cConReq.getEmailAddress() + "]");

			jCoFunction.execute(jCoDestination);

			// Retrieve the return values from CRM
			String rvResult = jCoFunction.getExportParameterList().getString("RV_SUCCESS");
			String rvCode = jCoFunction.getExportParameterList().getString("EV_REASON_CODE");

			strRetrieve.append("CRM retrieved following values, RV_RESULT[ ").append(rvResult)
					.append(" ], EV_REASON_CODE[ ").append(rvCode).append(" ]");
			SimpleLogger.trace(Severity.INFO, location, method + " - with Retrieve data :" + strRetrieve.toString());
			// End Call FM
			// Assemble Response Object
			cConResponse.setReturnCRM(rvResult);
			cConResponse.setErrCode(rvCode);
			// End of Assembling

		} catch (JCoException jCoException) {
			SimpleLogger.trace(Severity.ERROR, location,
					method + " - request with " + ConnectorUtils.converToJson(cConReq) + " - data retrieve with "
							+ strRetrieve.toString() + " - response data with "
							+ ConnectorUtils.converToJson(cConResponse));
			SimpleLogger.traceThrowable(Severity.ERROR, location, "", jCoException);
			cConResponse.setReturnCRM("false");
			cConResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			cConResponse.setErrReason(AppConstants.CALL_LEASEPLAN);

		} finally {
			// TODO Object clean up task
			location.exiting(method);
		}
		return cConResponse;
	}

}
