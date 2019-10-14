package com.lp.BOBService.selfRegistration.impl;

import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;
import com.lp.BOBService.selfRegistration.PortalJCO;
import com.lp.BOBService.selfRegistration.PortalUME;
import com.lp.BOBService.utils.AppConstants;
import com.lp.BOBService.utils.ServiceUtils;
import com.lp.connector.exception.ConnectorException;
import com.lp.connector.model.SAPConnectorRequest;
import com.lp.connector.model.SAPConnectorResponse;
import com.sap.security.api.AttributeValueAlreadyExistsException;
import com.sap.security.api.IGroup;
import com.sap.security.api.IGroupFactory;
import com.sap.security.api.IRole;
import com.sap.security.api.IRoleFactory;
import com.sap.security.api.IUser;
import com.sap.security.api.IUserAccount;
import com.sap.security.api.IUserAccountFactory;
import com.sap.security.api.IUserFactory;
import com.sap.security.api.IUserMaint;
import com.sap.security.api.NoSuchGroupException;
import com.sap.security.api.NoSuchRoleException;
import com.sap.security.api.NoSuchUserAccountException;
import com.sap.security.api.NoSuchUserException;
import com.sap.security.api.UMException;
import com.sap.security.api.UMFactory;
import com.sap.security.api.UMRuntimeException;
import com.sap.security.api.UserAccountAlreadyExistsException;
import com.sap.security.api.UserAlreadyExistsException;
import com.sap.security.api.logon.ILoginConstants;
import com.sap.tc.logging.Category;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component("portalUMEImpl")
public class PortalUMEImpl implements PortalUME {

	private static final Location loc = Location.getLocation(PortalUMEImpl.class);

	@Autowired
	@Qualifier("portalJCOImpl")
	private PortalJCO portalJCO;

	@Override
	public Response initPortalUser(Request registrationReq) throws ConnectorException {
		// TODO Auto-generated method stub
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) ServiceUtils.copyProperties(registrationReq, sapReq);
		Request requestLog = new Request();
		requestLog = (Request) ServiceUtils.copyProperties(registrationReq, requestLog);
		requestLog.setPassword("####");
		SAPConnectorResponse sapResponse = null;
		Response regResponse = null;
		// check if this user account is allowed by CRM
		regResponse = portalJCO.callInitAllowEmail(registrationReq);
		if (!"true".equalsIgnoreCase(regResponse.getReturnCRM())) {
			SimpleLogger.trace(Severity.INFO, loc, "Call callInitAllowEmail from PortalUMEImpl with response "
					+ ServiceUtils.converToJson(regResponse));
			return regResponse; // call CRM falied
		}

		SimpleLogger.trace(Severity.INFO, loc, "Call initPortalUser from PortalUMEImpl with Json request "
				+ ServiceUtils.converToJson(requestLog));
		try {

			sapResponse = this.initPortalUser(sapReq);
			sapResponse.setReturnCRM("X");

		} catch (UMException | UMRuntimeException umrex) {
			// TODO: Handle UMException. //// Error type 2 Call LP ...
			sapResponse = new SAPConnectorResponse();
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
			sapResponse.setReturnCRM("X");
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.initialiseUser",
					"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(requestLog));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umrex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;
		}
		SimpleLogger.trace(Severity.INFO, loc, "Response from PortalUMEImpl.initPortalUser  with Json data"
				+ ServiceUtils.converToJson(sapResponse));

		if (AppConstants.RETURN_UME_CREATED.equalsIgnoreCase(sapResponse.getReturnUME())) {
			if (sapResponse.getCustomerUID() != null) {
				try {
					/*
					 * assignPortalRole(sapResponse.getCustomerUID(), AppConstants.SELF_INIT_ROLE);
					 */
					this.assignPortalGroupRole(sapResponse.getCustomerUID(), AppConstants.SELF_INIT_GROUP, false);
				} catch (NoSuchUserException | NoSuchUserAccountException nsuex) {
					// TODO: Handle NoSuchUserException.
					// sapResponse = new SAPConnectorResponse(); //// Error type 2 Call LP ...
					sapResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
					sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserException);
					sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
					deleteUser(sapResponse.getCustomerUID());
					SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.initialiseUser",
							"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(requestLog));
					SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsuex);
					regResponse = convertJcoResponse(sapResponse);
					return regResponse;

				} catch (NoSuchGroupException nsrex) {
					// TODO: Handle NoSuchRoleException.
					// sapResponse = new SAPConnectorResponse(); //// Error type 2 Call LP ...
					sapResponse.setErrReason(AppConstants.MSG_NO_GROUP_FOUND);
					sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchRoleException);
					sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
					deleteUser(sapResponse.getCustomerUID());
					SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.initialiseUser",
							"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(requestLog));
					SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsrex);
					regResponse = convertJcoResponse(sapResponse);
					return regResponse;

				} catch (AttributeValueAlreadyExistsException avaeex) {
					// TODO: Handle AttributeValueAlreadyExistsException. // Error type 2 Call LP
					// ...
					// sapResponse = new SAPConnectorResponse();
					sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
					sapResponse.setErrCode(AppConstants.ERROR_CODE_AttributeValueAlreadyExistsException);
					sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
					deleteUser(sapResponse.getCustomerUID());
					SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.initialiseUser",
							"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(requestLog));
					SimpleLogger.traceThrowable(Severity.ERROR, loc, "", avaeex);
					regResponse = convertJcoResponse(sapResponse);
					return regResponse;
				} catch (UMException | UMRuntimeException umex) {
					// TODO: Handle UMException.
					// sapResponse = new SAPConnectorResponse(); // Error type 2 Call LP ...
					sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
					sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
					sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
					deleteUser(sapResponse.getCustomerUID());
					SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.initialiseUser",
							"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(requestLog));
					SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
					regResponse = convertJcoResponse(sapResponse);
					return regResponse;
				}

				/*
				 * System.out.
				 * println("Call callInitEmailVerification from PortalUMEImpl with Json request "
				 * + ServiceUtils.converToJson(sapReq));
				 */

				SimpleLogger.trace(Severity.INFO, loc,
						"Call callInitEmailVerification from PortalUMEImpl with Json request"
								+ ServiceUtils.converToJson(requestLog));
				if (AppConstants.RETURN_UME_CREATED.equalsIgnoreCase(sapResponse.getReturnUME())) {
					PortalJCO portalJCO = new PortalJCOImpl();
					try {
						registrationReq.setVerIndicator(""); // no verification need
						regResponse = portalJCO.callInitEmailVerification(registrationReq);
						if (AppConstants.RETURN_CRM_FALSE.equalsIgnoreCase(regResponse.getReturnCRM())) {
							regResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
							deleteUser(sapResponse.getCustomerUID());
						} else {
							regResponse.setReturnUME(AppConstants.RETURN_UME_CREATED);
						}

					} catch (ConnectorException ex) { // Error type 2 Call LP ...
						// TODO Auto-generated catch block
						// sapResponse = new SAPConnectorResponse();
						sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
						sapResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
						sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
						deleteUser(sapResponse.getCustomerUID());
						SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.initialiseUser",
								"Call callInitEmailVerification with request of "
										+ ServiceUtils.converToJson(requestLog));
						SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ex);
						regResponse = convertJcoResponse(sapResponse);
						return regResponse;
					}
					// /convert sapResponse to RegisterResponse
					/*
					 * System.out.
					 * println("Response from callInitEmailVerification to PortalUMEImpl with Json data "
					 * + ServiceUtils.converToJson(regResponse));
					 */
					SimpleLogger.trace(Severity.INFO, loc,
							"Response from callInitEmailVerification to PortalUMEImpl.initPortalUser with Json data"
									+ ServiceUtils.converToJson(sapResponse));
					return regResponse;
				}
			} else {
				// sapResponse.setReturnCRM(AppConstants.RETURN_CRM_FALSE); // Error type 1 Call
				// LP ...
				sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_LoginIDNotFound);
				sapResponse.setErrReason(AppConstants.MSG_NO_LOGON_ID);
			}
		}
		// /convert sapResponse to RegisterResponse
		regResponse = convertJcoResponse(sapResponse);
		return regResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalUME#VerifyInitPortalUser(com.lp.
	 * BOBService.model.Request)
	 */
	public Response VerifyInitPortalUser(Request registrationReq) throws ConnectorException {
		// TODO Auto-generated method stub
		SAPConnectorRequest sapReq = new SAPConnectorRequest();
		sapReq = (SAPConnectorRequest) ServiceUtils.copyProperties(registrationReq, sapReq);
		SAPConnectorResponse sapResponse = null;
		Response regResponse = null;
		boolean verifycation = false;
		PortalJCO portalJCO = new PortalJCOImpl();
		try {
			if ("true".equalsIgnoreCase(registrationReq.getVerIndicator())) {
				registrationReq.setVerIndicator("X"); // value of X as true for data imported to FM of CRM
				verifycation = true;
			} else {
				registrationReq.setVerIndicator(""); // value of "" as false for data imported to FM of CRM
			}
			regResponse = portalJCO.callInitEmailVerification(registrationReq);
		} catch (ConnectorException ex) { // Error type 2 Call LP ...
			// TODO Auto-generated catch block
			sapResponse = new SAPConnectorResponse();
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.VerifyInitPortalUser",
					"Call callInitEmailVerification with request of "
							+ ServiceUtils.converToJson(registrationReq));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;
		}

		if ("true".equalsIgnoreCase(regResponse.getReturnCRM()) && verifycation) {
			// assign role of continuing completed
			String customerLogonId = registrationReq.getEmailAddress();
			SimpleLogger.trace(Severity.INFO, loc, "VerifyInitPortalUser willl assignProtaRole to " + customerLogonId);

			try {
				/*
				 * assignPortalRole(customerLogonId,AppConstants.SELF_CONTINUING_COMPLETE_ROLE);
				 */
				this.assignPortalGroupRole(customerLogonId, AppConstants.SELF_CONTINUING_COMPLETE_GROUP, true);
			} catch (NoSuchUserException | NoSuchUserAccountException nsuex) {
				// TODO: Handle NoSuchUserException.
				sapResponse = new SAPConnectorResponse(); //// Error type 1 Call LP ...
				sapResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserException);
				sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				sapResponse.setReturnCRM(AppConstants.RETURN_CRM_TRUE);
				SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.VerifyInitPortalUser",
						"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(registrationReq));
				SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsuex);
				regResponse = convertJcoResponse(sapResponse);
				return regResponse;

			} catch (NoSuchGroupException nsrex) {
				// TODO: Handle NoSuchRoleException.
				sapResponse = new SAPConnectorResponse(); //// Error type 2 Call LP ...
				sapResponse.setErrReason(AppConstants.MSG_NO_ROLE_FOUND);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchRoleException);
				sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				sapResponse.setReturnCRM(AppConstants.RETURN_CRM_TRUE);
				SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.VerifyInitPortalUser",
						"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(registrationReq));
				SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsrex);
				regResponse = convertJcoResponse(sapResponse);
				return regResponse;

			} catch (AttributeValueAlreadyExistsException avaeex) {
				// TODO: Handle AttributeValueAlreadyExistsException. // Error type 2 Call LP
				// ...
				sapResponse = new SAPConnectorResponse();
				sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_AttributeValueAlreadyExistsException);
				sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				sapResponse.setReturnCRM(AppConstants.RETURN_CRM_TRUE);
				SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.VerifyInitPortalUser",
						"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(registrationReq));
				SimpleLogger.traceThrowable(Severity.ERROR, loc, "", avaeex);
				regResponse = convertJcoResponse(sapResponse);
				return regResponse;
			} catch (UMException | UMRuntimeException umex) {
				// TODO: Handle UMException.
				sapResponse = new SAPConnectorResponse(); // Error type 2 Call LP ...
				sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
				sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				sapResponse.setReturnCRM(AppConstants.RETURN_CRM_TRUE);
				SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.VerifyInitPortalUser",
						"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(registrationReq));
				SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
				regResponse = convertJcoResponse(sapResponse);
				return regResponse;
			}
			// assign portal role successfully
			regResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
		}
		// /convert sapResponse to RegisterResponse
		SimpleLogger.trace(Severity.INFO, loc,
				"Response from callInitEmailVerification to PortalUMEImpl.VerifyInitPortalUser with Json data"
						+ ServiceUtils.converToJson(regResponse));
		return regResponse;

	}

	/**
	 * convert response from java connector object to web service response
	 * 
	 * @param sapResponse
	 * @return regResponse
	 */
	private Response convertJcoResponse(SAPConnectorResponse sapResponse) {
		Response regResponse = new Response();
		regResponse = (Response) ServiceUtils.copyProperties(sapResponse, regResponse);
		return regResponse;
	}

	/**
	 * @param cConReq
	 * @return
	 * @throws UserAlreadyExistsException
	 * @throws UserAccountAlreadyExistsException
	 * @throws UMException
	 * @throws UMRuntimeException
	 */
	private SAPConnectorResponse initPortalUser(SAPConnectorRequest cConReq) throws UMException, UMRuntimeException {
		String method = "PortalUMEImpl-initPortalUser";
		// SimpleLogger.trace(Severity.INFO,loc,method + " - with request parameters :"+
		// ConnectorUtils.converToJson(cConReq));
		SAPConnectorResponse cConResponse = new SAPConnectorResponse();
		IUserFactory userFactory = UMFactory.getUserFactory();
		IUserAccountFactory userAccountFactory = UMFactory.getUserAccountFactory();
		IUser user = null;
		try {
			user = userFactory.getUserByLogonID(cConReq.getEmailAddress());
		} catch (NoSuchUserException ex) {
			user = null;
			SimpleLogger.trace(Severity.INFO, loc, "User " + cConReq.getEmailAddress() + " not existed");
		}

		if (user == null) {
			IUserMaint mutableUser = userFactory.newUser(cConReq.getEmailAddress());

			/*
			 * Set the LastName attribute of the user. Note: The user attribute last name is
			 * mandatory for AS ABAP data source.
			 */
			mutableUser.setLastName(cConReq.getLastName());
			mutableUser.setFirstName(cConReq.getFirstName());
			mutableUser.setEmail(cConReq.getEmailAddress());
			// Create a new user account with userName.
			IUserAccount userAccount = userAccountFactory.newUserAccount(cConReq.getEmailAddress());

			// Get the initial password of the user account.
			userAccount.setPassword(cConReq.getPassword());
			userAccount.setPasswordChangeRequired(false);

			// Write the changes to the user store in a single
			// transaction.
			userFactory.commitUser(mutableUser, userAccount);
			// TODO Object clean up task
			loc.exiting(method);

			// Assemble Response Object
			cConResponse.setCustomerUID(userAccount.getLogonUid());
			cConResponse.setReturnUME(AppConstants.RETURN_UME_CREATED);
		} else {
			cConResponse.setCustomerUID(user.getUniqueID());
			cConResponse.setReturnUME(AppConstants.RETURN_UME_REGISTERD); // already registered
			cConResponse.setErrCode(AppConstants.ERROR_CODE_DUPLIATED_USER);
			cConResponse.setErrReason(AppConstants.MSG_LOGON_DUPLICATED_USER);
			// cConResponse.setReturnUME("existing");

		}
		// End of Assembling
		return cConResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.selfRegistration.PortalUME#updateUserDepartment(java.
	 * lang.String)
	 */
	public void updateUserDepartment(String logonId) {
		String method = "PortalUMEImpl-updateUserDepartment";
		SimpleLogger.trace(Severity.INFO, loc, method + " - with request parameters :" + logonId);
		IUserFactory userFactory = UMFactory.getUserFactory();
		IUser user;
		try {
			user = userFactory.getUserByLogonID(logonId);
			// Get the unique ID.
			String uniqueID = user.getUniqueID();
			// Get a modifiable user object.
			IUserMaint userMaint = userFactory.getMutableUser(uniqueID);
			if (userMaint != null && userMaint.getDepartment() == null) {
				userMaint.setDepartment(AppConstants.DRIVER);
				// Write the changes to the user store.
				userMaint.save();
				userMaint.commit();
			}

		} catch (UMException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.updateUserDepartment",
					"Call PortalUMEImpl with request of " + logonId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalUME#getUserInfo(java.lang.String)
	 */
	public IUserMaint getUserInfo(String logonId) {
		String method = "PortalUMEImpl-getUserInfo";
		SimpleLogger.trace(Severity.INFO, loc, method + " - with request parameters :" + logonId);
		IUserFactory userFactory = UMFactory.getUserFactory();
		IUser user;
		IUserMaint userMaint = null;
		try {
			user = userFactory.getUserByLogonID(logonId);
			// Get a modifiable user object.
			userMaint = userFactory.getMutableUser(user.getUniqueID());
			// userMaint.getUserAccounts().
		} catch (UMException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.getUserInfo",
					"Call PortalUMEImpl with request of " + logonId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
		}

		return userMaint;

	}

	/**
	 * @param logonId
	 * @param assignRole
	 * @throws NoSuchUserException
	 * @throws NoSuchUserAccountException
	 * @throws NoSuchRoleException
	 * @throws AttributeValueAlreadyExistsException
	 * @throws UMException
	 * @throws UMRuntimeException
	 */
	private void assignPortalRole(String logonId, String assignRole)
			throws NoSuchUserException, NoSuchUserAccountException, NoSuchRoleException,
			AttributeValueAlreadyExistsException, UMException, UMRuntimeException {
		IUserFactory userFactory = UMFactory.getUserFactory();
		IRoleFactory roleFactory = UMFactory.getRoleFactory();
		IUser user = userFactory.getUserByLogonID(logonId);
		IRole role = roleFactory.getRoleByUniqueName(assignRole);

		// Check if the user is already assigned directly to the role.
		if (!role.isMember(user.getUniqueID(), false)) {
			// Get a modifiable role object from the UME.
			role = roleFactory.getMutableRole(role.getUniqueID());
			// Make the user as direct member of the role.
			role.addMember(user.getUniqueID());

			/*
			 * Write the changes to the database. Note: If the user was already added to
			 * this role on another cluster node while between the check in the 'if'
			 * statement and now, an AttributeValueAlreadyExistsException occurs.
			 */
			role.save();
			role.commit();
		}
	}

	private void assignPortalGroupRole(String logonId, String groupUniqueName, boolean removePreGroup)
			throws NoSuchUserException, NoSuchUserAccountException, NoSuchGroupException,
			AttributeValueAlreadyExistsException, UMException, UMRuntimeException {

		IUserFactory userFactory = UMFactory.getUserFactory();
		IGroupFactory groupFactory = UMFactory.getGroupFactory();

		IUser user = userFactory.getUserByLogonID(logonId);
		IGroup group = groupFactory.getGroupByUniqueName(groupUniqueName);

		// Check if the user is already assigned directly to this group.
		if (!group.isMember(user.getUniqueID(), false)) {

			// Get a modifiable group object from the UME.
			group = groupFactory.getMutableGroup(group.getUniqueID());

			// Make the user a direct member of the group.
			if (removePreGroup) {
				IGroup preGroup = null;
				if (AppConstants.SELF_CONTINUING_COMPLETE_GROUP.equalsIgnoreCase(groupUniqueName)) {
					preGroup = groupFactory.getGroupByUniqueName(AppConstants.SELF_INIT_GROUP);
				} else if (AppConstants.SELF_PENDING_GROUP.equalsIgnoreCase(groupUniqueName)
						|| AppConstants.SELF_APPROVAL_GROUP.equalsIgnoreCase(groupUniqueName)) {
					preGroup = groupFactory.getGroupByUniqueName(AppConstants.SELF_CONTINUING_COMPLETE_GROUP);
				}

				if (preGroup != null && preGroup.isMember(user.getUniqueID(), false)) {
					SimpleLogger.trace(Severity.INFO, loc,
							" - take previous group " + preGroup + " away from :" + user.getUniqueID());
					preGroup = groupFactory.getMutableGroup(preGroup.getUniqueID());
					preGroup.removeMember(user.getUniqueID());
					preGroup.save();
					preGroup.commit();
				}
			}
			group.addMember(user.getUniqueID());
			/*
			 * Write the changes to the database. Note: If the user was already added to
			 * this group on another cluster node while between the check in the 'if'
			 * statement and now, an AttributeValueAlreadyExistsException occurs.
			 */
			group.save();
			group.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.selfRegistration.PortalUME#checkDriverGroup(java.lang.
	 * String)
	 */
	public boolean checkDriverGroup(String logonId) {

		IUserFactory userFactory = UMFactory.getUserFactory();
		IGroupFactory groupFactory = UMFactory.getGroupFactory();
		SimpleLogger.trace(Severity.INFO, loc, "checkDriverGroup- with request parameters :" + logonId);
		boolean rtn = false;
		try {
			IUser user = userFactory.getUserByLogonID(logonId);
			IGroup groupGeneral = null;
			IGroup groupQuote = null;
			groupGeneral = groupFactory.getGroupByUniqueName(AppConstants.DRIVER_GENERAL);
			groupQuote = groupFactory.getGroupByUniqueName(AppConstants.DRIVER_QUOTE);

			SimpleLogger.trace(Severity.INFO, loc,
					" checkDriverGroup- with 2 groups  groupGeneral : " + groupGeneral + ",groupQuote " + groupQuote);
			if (groupGeneral != null && groupQuote != null) {
				// Check if the user is already assigned directly to the role.
				if (groupGeneral.isMember(user.getUniqueID(), false)
						&& groupQuote.isMember(user.getUniqueID(), false)) {
					rtn = true;
				} else {
					rtn = false;
				}
			}

		} catch (UMException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.checkDriverGroup",
					"Call PortalUMEImpl with request of " + logonId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			e.printStackTrace();
		}

		SimpleLogger.trace(Severity.INFO, loc, " checkDriverGroup- rtn : " + rtn);

		return rtn;

	}

	/**
	 * @param logonId
	 * @param groupUniqueName
	 */
	public void removeGroup(String logonId, String groupUniqueName) {
		IUserFactory userFactory = UMFactory.getUserFactory();
		IGroupFactory groupFactory = UMFactory.getGroupFactory();
		try {
			IUser user = userFactory.getUserByLogonID(logonId);
			// IGroup group = groupFactory.getGroupByUniqueName(groupUniqueName);
			IGroup preGroup = groupFactory.getGroupByUniqueName(groupUniqueName);

			preGroup = groupFactory.getMutableGroup(preGroup.getUniqueID());
			preGroup.removeMember(user.getUniqueID());
			preGroup.save();
			preGroup.commit();
		} catch (UMException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.removeGroup",
					"Call PortalUMEImpl with request of " + logonId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			e.printStackTrace();
		}

	}

	public Response assignPendingGroup(String LoginId) {
		SAPConnectorResponse sapResponse = null;
		Response regResponse = new Response();
		try {
			this.assignPortalGroupRole(LoginId, AppConstants.SELF_PENDING_GROUP, true);
		} catch (NoSuchUserException | NoSuchUserAccountException nsuex) {
			// TODO: Handle NoSuchUserException.
			//// Error type 1 Call LP ...
			regResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
			regResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserException);
			regResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsuex);
			return regResponse;

		} catch (NoSuchGroupException nsrex) {
			// TODO: Handle NoSuchRoleException.
			//// Error type 2 Call LP ...
			regResponse.setErrReason(AppConstants.MSG_NO_ROLE_FOUND);
			regResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchRoleException);
			regResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsrex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;

		} catch (AttributeValueAlreadyExistsException avaeex) {
			// TODO: Handle AttributeValueAlreadyExistsException. // Error type 2 Call LP
			// ...
			sapResponse = new SAPConnectorResponse();
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_AttributeValueAlreadyExistsException);
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", avaeex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;
		} catch (UMException | UMRuntimeException umex) {
			// TODO: Handle UMException.
			sapResponse = new SAPConnectorResponse(); // Error type 2 Call LP ...
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;
		}
		regResponse = new Response();
		regResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
		return regResponse;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.selfRegistration.PortalUME#assignApprovalGroup(java.lang
	 * .String)
	 */
	public Response assignApprovalGroup(String LoginId) {
		SAPConnectorResponse sapResponse = null;
		Response regResponse = new Response();
		try {
			this.assignPortalGroupRole(LoginId, AppConstants.SELF_APPROVAL_GROUP, true);
		} catch (NoSuchUserException | NoSuchUserAccountException nsuex) {
			// TODO: Handle NoSuchUserException.
			//// Error type 1 Call LP ...
			regResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
			regResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserException);
			regResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsuex);
			return regResponse;

		} catch (NoSuchGroupException nsrex) {
			// TODO: Handle NoSuchRoleException.
			//// Error type 2 Call LP ...
			regResponse.setErrReason(AppConstants.MSG_NO_ROLE_FOUND);
			regResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchRoleException);
			regResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsrex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;

		} catch (AttributeValueAlreadyExistsException avaeex) {
			// TODO: Handle AttributeValueAlreadyExistsException. // Error type 2 Call LP
			// ...
			sapResponse = new SAPConnectorResponse();
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_AttributeValueAlreadyExistsException);
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", avaeex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;
		} catch (UMException | UMRuntimeException umex) {
			// TODO: Handle UMException.
			sapResponse = new SAPConnectorResponse(); // Error type 2 Call LP ...
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.assignPendingGroup",
					"Call PortalUMEImpl with request of " + LoginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
			regResponse = convertJcoResponse(sapResponse);
			return regResponse;
		}
		regResponse = new Response();
		regResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
		return regResponse;

	}

	/**
	 * @param uniqueId
	 * @throws UMException
	 * @throws UMRuntimeException
	 */
	private String checkRole(String uniqueId) throws UMException, UMRuntimeException {
		IRoleFactory roleFactory = UMFactory.getRoleFactory();
		IRole initRole = roleFactory.getRoleByUniqueName(AppConstants.SELF_INIT_ROLE);
		IRole completedRole = roleFactory.getRoleByUniqueName(AppConstants.SELF_CONTINUING_COMPLETE_ROLE);
		// Check if the user is already assigned directly to the role.
		String roleType = "";
		if (completedRole.isMember(uniqueId, false)) {
			roleType = AppConstants.SELF_CONTINUING_COMPLETE_ROLE;
		} else if (initRole.isMember(uniqueId, false)) {
			roleType = AppConstants.SELF_INIT_ROLE_TYPE;
		}
		return roleType;
	}

	/**
	 * @param uniqueId
	 * @throws UMException
	 * @throws UMRuntimeException
	 */
	public String checkGroup(String logonId) {
		String groupType = AppConstants.SELF_OTHER_GROUP_TYPE;
		try {
			IUserFactory userFactory = UMFactory.getUserFactory();
			IGroupFactory groupFactory = UMFactory.getGroupFactory();
			IUser user = userFactory.getUserByLogonID(logonId);

			IGroup groupInitial = groupFactory.getGroupByUniqueName(AppConstants.SELF_INIT_GROUP);

			IGroup groupComplete = groupFactory.getGroupByUniqueName(AppConstants.SELF_CONTINUING_COMPLETE_GROUP);

			IGroup groupPending = groupFactory.getGroupByUniqueName(AppConstants.SELF_PENDING_GROUP);

			IGroup groupApproval = groupFactory.getGroupByUniqueName(AppConstants.SELF_APPROVAL_GROUP);

			// Check if the user is already assigned directly to this group.

			if (groupApproval.isMember(user.getUniqueID(), false)) {
				groupType = AppConstants.SELF_APPROVAL_GROUP_TYPE;
			} else if (groupPending.isMember(user.getUniqueID(), false)) {
				groupType = AppConstants.SELF_PENDING_GROUP_TYPE;
			} else if (groupComplete.isMember(user.getUniqueID(), false)) {
				groupType = AppConstants.SELF_COMPLETED_GROUP_TYPE;
			} else if (groupInitial.isMember(user.getUniqueID(), false)) {
				groupType = AppConstants.SELF_INIT_GROUP_TYPE;
			}

		} catch (UMException ux) {
			SimpleLogger.log(Severity.INFO, Category.SYS_SERVER, loc,
					"PortalUMEImpl.checkGroup with logonId" + logonId + "~" + ux, "");
			SimpleLogger.traceThrowable(Severity.INFO, loc, "", ux);
		}

		return groupType;
	}

	/**
	 * @param logonID
	 */
	public void deleteUser(String logonID) {
		try {
			IUserFactory userFactory = UMFactory.getUserFactory();
			// Get the user.
			IUser user = userFactory.getUserByLogonID(logonID);
			// Delete the user.
			userFactory.deleteUser(user.getUniqueID());

		} catch (NoSuchUserException | NoSuchUserAccountException nsuex) {
			// TODO: Handle NoSuchUserException.
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.deleteUser",
					"User id :: " + logonID);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", nsuex);
		} catch (UMException | UMRuntimeException umex) {
			// TODO: Handle UMException.
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.deleteUser",
					"User id :: " + logonID);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
		}
	}

	/**
	 * @param loginId
	 * @param pwd
	 * @return
	 */
	public Response validateAccount(String loginId, String pwd) {
		String method = "PortalUMEImpl-validateAccount";
		SimpleLogger.trace(Severity.INFO, loc, method + " - with request parameters :" + loginId);
		SAPConnectorResponse sapResponse = new SAPConnectorResponse();
		Response regResponse = null;
		IUserAccountFactory userAccountFactory = UMFactory.getUserAccountFactory();
		IUserFactory userFactory = UMFactory.getUserFactory();
		try {
			IUserAccount userAccount = userAccountFactory.getUserAccountByLogonId(loginId);
			IUser user = userFactory.getUserByLogonID(loginId);
			int iCheckPwd = userAccount.checkPasswordExtended(pwd);
			if (iCheckPwd == ILoginConstants.CHECKPWD_OK) {
				sapResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
				sapResponse.setFirstName(user.getFirstName());
				sapResponse.setLastName(user.getLastName());
				sapResponse.setEmailAddress(loginId);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_PWDOK);
				// sapResponse.setEmailAddress(user.getEmail());
				// sapResponse.setToken(userAccount.getHashedPassword());
			} else {
				sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				sapResponse.setErrCode(AppConstants.ERROR_CODE_INCORRECT_PWD);
				if (iCheckPwd == ILoginConstants.CHECKPWD_PWDLOCKED) {
					sapResponse.setErrReason(AppConstants.MSG_LOGON_PWD_LOCKED);
				} else if (iCheckPwd == ILoginConstants.CHECKPWD_PWDEXPIRED) {
					sapResponse.setErrReason(AppConstants.MSG_LOGON_PWD_EXPIRED);
				} else {
					sapResponse.setErrReason(AppConstants.MSG_LOGON_PWD_INCORRECT);
				}
				SimpleLogger.trace(Severity.INFO, loc, method + " - with request parameters User id :" + loginId
						+ " password failed as " + sapResponse.getErrReason());
			}
			;

		} catch (NoSuchUserAccountException ex) {
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserAccountException);
			sapResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
			SimpleLogger.trace(Severity.INFO, loc, "User " + loginId + " doesn't not exist.");
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ex);
		} catch (UMException umex) {
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.validateAccount",
					"User id : " + loginId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
		}

		regResponse = convertJcoResponse(sapResponse);
		return regResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.selfRegistration.PortalUME#getEncryptPwd(java.lang.
	 * String)
	 */
	public Response getEncryptPwd(String logonId) throws ConnectorException {
		String method = "PortalUMEImpl-getEncrptPwd";
		SimpleLogger.trace(Severity.INFO, loc, method + " - with request parameters :" + logonId);
		SAPConnectorResponse sapResponse = new SAPConnectorResponse();
		Response regResponse = null;
		IUserAccountFactory userAccountFactory = UMFactory.getUserAccountFactory();
		String encryptPwd = "";
		try {
			IUserAccount userAccount = userAccountFactory.getUserAccountByLogonId(logonId);
			encryptPwd = userAccount.getHashedPassword();
			sapResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
			sapResponse.setToken(encryptPwd);
			SimpleLogger.trace(Severity.INFO, loc, method + " - with encryptPwd :" + encryptPwd);

		} catch (NoSuchUserException ex) {
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserException);
			sapResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
			SimpleLogger.trace(Severity.INFO, loc, "User " + logonId + " doesn't not exist.");
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ex);

		} catch (NoSuchUserAccountException exx) {
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_NoSuchUserAccountException);
			sapResponse.setErrReason(AppConstants.MSG_NO_USER_EXISTS);
			SimpleLogger.trace(Severity.INFO, loc, "User " + logonId + " doesn't not exist.");
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", exx);
		} catch (UMException umex) {
			sapResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			sapResponse.setErrCode(AppConstants.ERROR_CODE_UMException);
			sapResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.getEncryptPwd",
					"User id : " + logonId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umex);
		}
		regResponse = convertJcoResponse(sapResponse);
		return regResponse;

	}

	@Override
	public Response resetPassword(Request registrationReq) throws ConnectorException {
		// TODO Auto-generated method stub

		Request requestLog = new Request();
		requestLog = (Request) ServiceUtils.copyProperties(registrationReq, requestLog);
		requestLog.setPassword("####");
		requestLog.setNewPassword("####");

		Response regResponse = null;
		SimpleLogger.trace(Severity.INFO, loc, "Call resetPassword from PortalUMEImpl with Json request "
				+ ServiceUtils.converToJson(requestLog));

		try {
			regResponse = this.updatePassword(registrationReq.getLogonId(), registrationReq.getNewPassword());
		} catch (UMException | UMRuntimeException umrex) {
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalUMEImpl.resetPassword",
					"Call PortalUMEImpl with request of " + ServiceUtils.converToJson(requestLog));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", umrex);
			regResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			regResponse.setAppStatus("RESET FALIED");
			// return regResponse;
		}

		SimpleLogger.trace(Severity.INFO, loc, "Response from PortalUMEImpl.resetPassword  with Json data"
				+ ServiceUtils.converToJson(regResponse));
		// /convert sapResponse to RegisterResponse
		return regResponse;

	}

	private Response updatePassword(String logonId, String newPassword) throws UMException, UMRuntimeException {
		String method = "PortalUMEImpl-resetPassword";
		Response regResponse = new Response();
		IUserAccountFactory userAccountFactory = UMFactory.getUserAccountFactory();
		IUserAccount userAccount = userAccountFactory.getUserAccountByLogonId(logonId);
		IUserAccount mAccnt = userAccountFactory.getMutableUserAccount(userAccount.getUniqueID());
		// Set the new password in the mutable user account object
		mAccnt.setPassword(newPassword);
		mAccnt.setPasswordChangeRequired(false);
		// Save and commit the mutable user account object
		mAccnt.save();
		mAccnt.commit();
		regResponse.setReturnUME(AppConstants.RETURN_TRUE);
		regResponse.setAppStatus("RESET PASSWORD");
		return regResponse;
	}

	@Override
	public boolean checkUserExist(String logonId) {
		IUserFactory userFactory = UMFactory.getUserFactory();
		IUser user = null;
		try {
			user = userFactory.getUserByLogonID(logonId);
		} catch (NoSuchUserException e) {
			user = null;
		} catch (UMException ex) {
			user = null;
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ex);
		}
		return user != null;
	}

	@Override
	public Response createUMEIdentity(Request paramRegistration) throws ConnectorException {
		// TODO Auto-generated method stub
		return null;
	}

}
