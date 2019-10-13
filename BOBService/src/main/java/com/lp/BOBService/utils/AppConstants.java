package com.lp.BOBService.utils;

import java.io.Serializable;

public class AppConstants implements Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -3378732900839934764L;

	/** System Exceptions **/
	public static final String ERROR_CODE_JCO_EXCETPION = "JCO_EXCETPION";
	public static final String ERROR_CODE_UserAlreadyExistsException = "UserAlreadyExistsException";
	public static final String ERROR_CODE_UserAccountAlreadyExistsException = "UserAccountAlreadyExistsException";
	public static final String ERROR_CODE_UMException = "UMException";
	public static final String ERROR_CODE_UMRuntimeException = "UMRuntimeException";
	public static final String ERROR_CODE_NoSuchRoleException = "NoSuchRoleException";
	public static final String ERROR_CODE_NoSuchUserException = "NoSuchUserException";
	public static final String ERROR_CODE_NoSuchUserAccountException = "NoSuchUserAccountException";
	public static final String ERROR_CODE_AttributeValueAlreadyExistsException = "AttributeValueAlreadyExistsException";

	/** Business errors **/
	public static final String ERROR_CODE_LoginIDNotFound = "LoginIDNotFound";
	public static final String ERROR_CODE_SESSIONLOST = "SESSIONLOST";
	public static final String ERROR_CODE_INCORRECT_ENCRYPTPWD = "INCORRECT_ENCRYPTPWD";
	public static final String ERROR_CODE_PERMISSION_DENY = "PERMISSION_DENY";
	public static final String ERROR_CODE_INCORRECT_PWD = "INCORRECT_PWD";
	public static final String ERROR_CODE_DUPLIATED_USER = "DUPLIATED_USER";
	public static final String ERROR_CODE_PWDOK = "PASSWORD_OK";
	

	/** Application Status Setting **/
	public static final String RETURN_UME_TRUE = "true";
	public static final String RETURN_UME_FALSE = "false";
	public static final String RETURN_CRM_TRUE = "true";
	public static final String RETURN_CRM_FALSE = "false";
	public static final String RETURN_TRUE = "true";
	public static final String RETURN_FALSE = "false";
	public static final String RETURN_UME_REGISTERD = "registered"; //
	public static final String RETURN_UME_CREATED = "created";

	/** Common explanation **/
	public static final String CALL_LEASEPLAN = "LeasePlan Online has encountered an internal error, please try again later or call 132 572.";
	public static final String MSG_USER_EXISTS = "User already exists ,please try again later or call 132 572.";
	public static final String MSG_SESSION_LOST = "Session lost or expired.";
	public static final String MSG_PWD_INCORRECT = "Encrypt password in session is incorrect";
	public static final String MSG_DENY = "Permission deny.";
	public static final String MSG_NO_USER_EXISTS = "User account can't be found on portal, please try again later or call 132 572.";
	public static final String MSG_NO_GROUP_FOUND = "User group can't be found on portal, please try again later or call 132 572.";
	public static final String MSG_NO_LOGON_ID = "UME Logon id can't be found in portal.";
	public static final String MSG_NO_ROLE_FOUND = "User role can't be found on portal, please try again later or call 132 572.";
	public static final String MSG_LOGON_PWD_INCORRECT = "Password failed.";
	public static final String MSG_LOGON_PWD_LOCKED = "Password is locked";
	public static final String MSG_LOGON_PWD_EXPIRED = "Password expired";
	public static final String MSG_LOGON_DUPLICATED_USER = "This user has been already registered.";

	/** status list **/
	public static final String REJECTED = "Rejected";
	public static final String COMPLETED = "Completed";

	/** roles&role type list in portal **/
	public static final String SELF_INIT_ROLE = "pcd:portal_content/leaseplan_online/anonymous/roles/self_init_role"; // initialise
																														// register
	public static final String SELF_CONTINUING_COMPLETE_ROLE = "pcd:portal_content/leaseplan_online/anonymous/roles/self_completed_role"; // completed
																																			// register
	public static final String SELF_INIT_ROLE_TYPE = "initialiseRole"; // initialise register role type
	public static final String SELF_COMPLETED_ROLE_TYPE = "completedRole"; // completed register role type
	public static final String DRIVER_QUOTE = "DriverQuote"; // driver quote;
	public static final String DRIVER_GENERAL = "DriverGeneral"; // driver general;
	public static final String DRIVER = "Driver";

	/** groups&group type list in portal **/
	public static final String SELF_INIT_GROUP = "SelfRegistrationInitial"; // initialise register
	public static final String SELF_CONTINUING_COMPLETE_GROUP = "SelfRegistrationVerified"; // completed register
	public static final String SELF_PENDING_GROUP = "SelfRegistrationPending"; // manual approval pending stage
	public static final String SELF_APPROVAL_GROUP = "SelfRegistrationApproval"; // manual approval pending stage

	public static final String SELF_INIT_GROUP_TYPE = "initialiseGroup"; // initialise register group type
	public static final String SELF_COMPLETED_GROUP_TYPE = "verifiedGroup"; // completed register group type
	public static final String SELF_PENDING_GROUP_TYPE = "pendingGroup"; // manual approval pending stage group type
	public static final String SELF_APPROVAL_GROUP_TYPE = "approvalGroup"; // Approval stage group type
	public static final String SELF_OTHER_GROUP_TYPE = "otherGroup"; // manual approval pending stage group type

	/** CRM Return status ***/
	public static final String CRM_STATUS_APPROVAL = "COMPLETE"; // creating driver approval
	public static final String CRM_STATUS_PENDING = "PENDING"; // creating driver pending
	public static final String CRM_STATUS_REJECJ = "ERROR"; // creating driver rejected

	/** redirect link list **/
	public static final String INITIAL_LINK = "initial link"; // initial link for initialise register
	public static final String COMPLETED_LINK = "completed link"; // completed link for completed register

	/** google reCaptcha **/
	public static final String SITE_SECRET = "6LebGTMUAAAAAHyjyukKIxTF1byFg9wXtenkEgc3";
	public static final String SECRET_PARAM = "secret";
	public static final String RESPONSE_PARAM = "response";
	public static final String G_RECAPTCHA_RESPONSE = "g-recaptcha-response";
	public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	/* salesforce key */
	public static final String CUST_SECRET = "8213259018947198423";
	public static final String CUST_KEY = "3MVG9YDQS5WtC11qaMS10FhX_KBu5v_UM5N7ImbPjoLZFU26wfh3DR8GINmXTJpZpU3TUT_00up30EzyAEALq";
	public static final String INSTANCE_SITE = "https://ap4.salesforce.com";
	public static final String SALESFORCE_SITE = "https://login.salesforce.com";
	public static final String SALESFORCE_OAUTH2 = "/services/oauth2/token";



}