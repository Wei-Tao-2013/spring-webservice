package com.lp.BOBService.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.lp.connector.exception.ConnectorException;
import com.lp.BOBService.model.AuthUser;
import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;
import com.lp.BOBService.selfRegistration.PortalJCO;
import com.lp.BOBService.selfRegistration.PortalUME;
import com.lp.BOBService.service.PortalDetailsResponse;
import com.lp.BOBService.utils.AppConstants;
import com.lp.BOBService.utils.AppData;
import com.lp.BOBService.utils.PortalServiceUtils;
import com.sap.security.api.IUserMaint;
import com.sap.security.api.UMException;
import com.sap.tc.logging.Category;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PortalDetailsResponseImpl implements PortalDetailsResponse {

	private static final Location loc = Location.getLocation(PortalDetailsResponseImpl.class);

	@Autowired
	private PortalUME portalUMEImpl;
	@Autowired
	private PortalJCO portalJCOImpl;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	private HttpServletResponse httpResponse;
	@Autowired
	private HttpServletRequest httpRequest;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#checkEmail(com.lp.
	 * BOBService.model.Request)
	 */
	public Response checkEmail(final Request Request) {

		// secure registerCustomer service on portal
		Response RegResponseSecure = checkEncryptSession(Request);
		if (AppConstants.RETURN_UME_FALSE.equals(RegResponseSecure.getReturnUME())) {
			RegResponseSecure.setReturnCRM(AppConstants.RETURN_CRM_FALSE);
			return RegResponseSecure;
		}
		// secure end this solution would be changed accordingly if web service moves
		// out from portal
		Response RegResponse;
		try {
			RegResponse = portalJCOImpl.callCustomerValidation(Request);
			RegResponse.setGoogleAPI(AppData.googleAPI);
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.checkEmail",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#validateRecaptcha()
	 */
	public Response validateRecaptcha(final Request Request) {
		Response RegResponse = new Response();
		JSONObject jsonObject;
		try {
			jsonObject = PortalServiceUtils.performRecaptchaSiteVerify(Request.getGoogleRecaptchaToken());

			SimpleLogger.log(Severity.INFO, Category.SYS_LOGGING, loc, "validateRecaptcha.validateRecaptcha",
					"your remote addr IP is " + httpRequest.getRemoteAddr());

			RegResponse.setGoogleRecaptchaSuccess(String.valueOf(jsonObject.getBoolean("success")));

			SimpleLogger.log(Severity.INFO, Category.SYS_LOGGING, loc, "validateRecaptcha.validateRecaptcha",
					"setGoogleRecaptchaSuccess is  " + String.valueOf(jsonObject.getBoolean("success")));

			if (!"true".equalsIgnoreCase(RegResponse.getGoogleRecaptchaSuccess())) {
				SimpleLogger.log(Severity.INFO, Category.SYS_LOGGING, loc,
						"PortalDetailsResponseImpl.validateRecaptcha",
						"With false from google server " + jsonObject.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.validateRecaptcha",
					"With exception from google server " + e);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			return RegResponse;
		}
		return RegResponse;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#sendVerifyEmail()
	 */
	public Response sendVerifyEmail() {
		/*
		 * if (!PortalServiceUtils.checkIpBlock(httpRequest.getRemoteAddr())){ Response
		 * RegResponseBlock = new Response();
		 * RegResponseBlock.setReturnUME(AppConstants.RETURN_UME_FALSE);
		 * RegResponseBlock.
		 * setErrReason("This Ip is blocked for a while ,please try again, IP is  " +
		 * httpRequest.getRemoteAddr()); return RegResponseBlock; };
		 */

		Response RegResponse = new Response();
		Request registrationReq = new Request();

		String emailAddress = null;
		IUserMaint userMaint = null;
		int sentEmailNumber = 0;
		try {
			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("selfRegLogonId")) {
						// do something
						emailAddress = cookie.getValue();
					}
				}
			}
			if (emailAddress != null) {
				registrationReq.setEmailAddress(emailAddress);
				Response RegResponseEncrypt = portalUMEImpl.getEncryptPwd(emailAddress);
				String initialSession = (httpSession.getAttribute("encryptSessionInitial") != null)
						? httpSession.getAttribute("encryptSessionInitial").toString()
						: "";
				if (initialSession.equals(RegResponseEncrypt.getToken())) { // verify Token
					// retrieve info as per emailAddress
					userMaint = portalUMEImpl.getUserInfo(emailAddress);
					if (userMaint != null) {
						sentEmailNumber = userMaint.getAttribute("com.lp.selfReg", "numEmailSent") == null ? 0
								: Integer.parseInt(userMaint.getAttribute("com.lp.selfReg", "numEmailSent")[0]);
						if (sentEmailNumber < AppData.maxNumberEmailSent) {
							registrationReq.setFirstName(userMaint.getFirstName());
							registrationReq.setLastName(userMaint.getLastName());
							registrationReq.setVerIndicator(""); // no verification need
							RegResponse = portalJCOImpl.callInitEmailVerification(registrationReq);
							if (AppConstants.RETURN_CRM_TRUE.equalsIgnoreCase(RegResponse.getReturnCRM())) {
								userMaint.removeAttributeValue("com.lp.selfReg", "numEmailSent",
										String.valueOf(sentEmailNumber));
								userMaint.addAttributeValue("com.lp.selfReg", "numEmailSent",
										String.valueOf(sentEmailNumber + 1));
								try {
									userMaint.save();
									userMaint.commit();
								} catch (UMException e) {
									// TODO Auto-generated catch block
									SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
											"PortalDetailsResponseImpl.sendVerifyEmail",
											"With request of logon id " + emailAddress);
									SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
								}

								httpSession.setAttribute("encryptSessionInitial", "done"); // clear session in case user
																							// re send email multiple
																							// times unless user sign in
																							// portal again
								RegResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
								RegResponse.setErrReason("The verification email has been sent out.");
							}
						} else {
							// send verify email exceed maximum
							SimpleLogger.trace(Severity.ERROR, loc,
									"sendVerifyEmail:: can not find user info as request of logon id " + emailAddress);
							RegResponse.setErrReason("The number of verify email sent has been exceed the maximum ");
							RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
							return RegResponse;
						}
					} else {
						// can't find user info on portal
						SimpleLogger.trace(Severity.ERROR, loc,
								"sendVerifyEmail:: can not find user info as request of logon id " + emailAddress);
						RegResponse.setErrReason("Can not find user info as request of logon id " + emailAddress);
						RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
						return RegResponse;
					}
				} else {
					// user session is clear
					if ("done".equalsIgnoreCase(initialSession)) {
						SimpleLogger.trace(Severity.INFO, loc,
								"sendVerifyEmail:: user's request session could be remove as email has been sent already as request of logon id "
										+ emailAddress + " Token is" + RegResponseEncrypt.getToken()
										+ "initial session value is " + initialSession);
						RegResponse
								.setErrReason("The email has been sent already as request of logon id " + emailAddress);
					} else {
						// user account is not correct as token is wrong
						SimpleLogger.trace(Severity.ERROR, loc,
								"sendVerifyEmail:: user's request session could be wrong as token is incorrect as request of logon id "
										+ emailAddress + " Token is" + RegResponseEncrypt.getToken()
										+ "initial session value is " + initialSession);
						RegResponse.setErrReason("User's request session could be wrong as logon id " + emailAddress
								+ " you may sign in again to try.");
					}

					RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
					return RegResponse;

				}
			} else {
				// can't find email info in cookie as issuse of setting cookie or service be
				// called without sign in.
				SimpleLogger.trace(Severity.ERROR, loc,
						"sendVerifyEmail:: can't find email info in cookie as issuse of setting cookie or service be called without sign in.");
				RegResponse.setErrReason(" Can't find email info,please sign in first.");
				RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				return RegResponse;
			}
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.sendVerifyEmail",
					"With request of logon id " + emailAddress);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			// RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.service.PortalDetailsResponse#checkCustomerNumber(com.lp
	 * .BOBService.model.Request)
	 */
	public Response checkCustomerNumber(final Request Request) {

		// Secure registerCustomer service on portal
		Response RegResponseSecure = checkEncryptSession(Request);
		if (AppConstants.RETURN_UME_FALSE.equals(RegResponseSecure.getReturnUME())) {
			RegResponseSecure.setReturnCRM(AppConstants.RETURN_CRM_FALSE);
			return RegResponseSecure;
		}
		// Secure end this solution would be changed accordingly if web service moves
		// out from portal

		Response RegResponse;
		Response RegResponseMandatory;
		try {
			RegResponse = portalJCOImpl.callCustomerValidation(Request);
			RegResponseMandatory = portalJCOImpl.callMandatoryIndicator(Request);
			RegResponse.setMandatory(RegResponseMandatory.getMandatory());
			// RegResponse.setMandatory("true");
			RegResponse.setGoogleAPI(AppData.googleAPI);
			SimpleLogger.trace(Severity.INFO, loc, "Call checkCustomerNumber retrives googleAPI "
					+ PortalServiceUtils.converToJson(AppData.googleAPI));

		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.checkCustomerNumber",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.service.PortalDetailsResponse#validateWorkEmail(com.lp.
	 * BOBService.model.Request)
	 */
	public Response validateWorkEmail(final Request Request) {

		// secure registerCustomer service on portal
		Response RegResponseSecure = checkEncryptSession(Request);
		if (AppConstants.RETURN_UME_FALSE.equals(RegResponseSecure.getReturnUME())) {
			RegResponseSecure.setReturnCRM(AppConstants.RETURN_CRM_FALSE);
			return RegResponseSecure;
		}
		// secure end this solution would be changed accordingly if web service moves
		// out from portal
		Response RegResponse;
		Response RegResponseMandatory;
		try {
			RegResponse = portalJCOImpl.callWorkEamilValiation(Request);
			RegResponseMandatory = portalJCOImpl.callMandatoryIndicator(Request);
			RegResponse.setMandatory(RegResponseMandatory.getMandatory());
			// RegResponse.setMandatory("true");
			RegResponse.setGoogleAPI(AppData.googleAPI);

			SimpleLogger.trace(Severity.INFO, loc,
					"Call validateWorkEmail retrives googleAPI " + PortalServiceUtils.converToJson(AppData.googleAPI));

		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.validateWorkEmail",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#registerCustomer(com.lp.
	 * BOBService.model.Request)
	 */
	public Response registerCustomer(final Request Request) {
		Response RegResponse, RegResponseSecure;
		try {
			// secure registerCustomer service on portal
			RegResponseSecure = checkEncryptSession(Request);
			if (AppConstants.RETURN_UME_FALSE.equals(RegResponseSecure.getReturnUME())) {
				RegResponseSecure.setReturnCRM(AppConstants.RETURN_CRM_FALSE);
				return RegResponseSecure;
			}
			// secure end this solution would be changed accordingly if web service moves
			// out from portal
			RegResponse = portalJCOImpl.callCustomerRegistration(Request);
			// call UME to assign pending role for this user
			if (AppConstants.CRM_STATUS_PENDING.equalsIgnoreCase(RegResponse.getAppStatus())) {
				Response RegResponseUME = portalUMEImpl.assignPendingGroup(Request.getEmailAddress());
				if (AppConstants.RETURN_UME_TRUE.equalsIgnoreCase(RegResponseUME.getReturnUME())) {
					RegResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
				} else {
					return RegResponseUME;
				}
			} else if (AppConstants.CRM_STATUS_APPROVAL.equalsIgnoreCase(RegResponse.getAppStatus())) { // //call UME to
																										// for this user
				Response RegResponseUME = portalUMEImpl.assignApprovalGroup(Request.getEmailAddress());
				if (AppConstants.RETURN_UME_TRUE.equalsIgnoreCase(RegResponseUME.getReturnUME())) {
					RegResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
				} else {
					return RegResponseUME;
				}
			} else {
				RegResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
			}

		} catch (ConnectorException ec) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.registerCustomer",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ec);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#registerCustomer(com.lp.
	 * BOBService.model.Request)
	 */
	public Response verifyToken(final Request Request) {
		Response RegResponse;
		try {
			RegResponse = portalJCOImpl.callVerifyToken(Request);
		} catch (ConnectorException ec) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.registerCustomer",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ec);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	public Response verifyAddress(final Request Request) {
		// secure registerCustomer service on portal
		Response RegResponseSecure = checkEncryptSession(Request);
		if (AppConstants.RETURN_UME_FALSE.equals(RegResponseSecure.getReturnUME())) {
			RegResponseSecure.setReturnCRM(AppConstants.RETURN_CRM_FALSE);
			return RegResponseSecure;
		}
		// secure end this solution would be changed accordingly if web service moves
		// out from portal
		Response RegResponse;
		try {
			RegResponse = portalJCOImpl.callAddressCheck(Request);
		} catch (ConnectorException ec) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.verifyAddress",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ec);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#updatePassword(com.lp.
	 * BOBService.model.Request)
	 */
	public Response updatePassword(final Request Request) {
		Response RegResponse;
		try {
			RegResponse = portalJCOImpl.callUpdatePassword(Request);
		} catch (ConnectorException ec) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.updatePassword",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ec);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.service.PortalDetailsResponse#verifyEmailActivation(com.
	 * lp.BOBService.model.Request)
	 */
	public Response verifyEmailActivation(final Request Request) {
		Response RegResponse;
		try {
			RegResponse = portalJCOImpl.callVerifyEmailActivation(Request);
		} catch (ConnectorException ec) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
					"PortalDetailsResponseImpl.verifyEmailActivation",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ec);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#initialiseUser(com.lp.
	 * BOBService.model.Request)
	 */
	public Response initialiseUser(final Request Request) {

		/*
		 * String ip = httpRequest.getHeader("x-forwarded-for"); if ( null == ip ||
		 * ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip
		 * =httpRequest.getHeader("Proxy-Client-IP"); } if ( null == ip || ip.length()
		 * == 0 || "unknown".equalsIgnoreCase(ip)) { ip =
		 * httpRequest.getHeader("WL-Proxy-Client-IP"); } if ( null == ip || ip.length()
		 * == 0 || "unknown".equalsIgnoreCase(ip)) { ip = httpRequest.getRemoteAddr(); }
		 * 
		 * SimpleLogger.trace(Severity.INFO,loc,"initialiseUser request :"+ ip );
		 * 
		 * if (!PortalServiceUtils.checkIpBlock(ip)){ Response RegResponseBlock = new
		 * Response(); RegResponseBlock.setReturnUME(AppConstants.RETURN_UME_FALSE);
		 * RegResponseBlock.
		 * setErrReason("This Ip is blocked for a while ,please try again, IP is  " +
		 * ip); return RegResponseBlock; };
		 */

		Response RegResponse;
		try {
			RegResponse = portalUMEImpl.initPortalUser(Request);

		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.service.PortalDetailsResponse#verifyInitialUser(com.lp.
	 * BOBService.model.Request)
	 */
	public Response verifyInitialUser(final Request Request) {
		Response RegResponse;
		try {
			RegResponse = portalUMEImpl.VerifyInitPortalUser(Request);
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.initialiseUser",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.service.PortalDetailsResponse#testJcoconnection(com.lp.
	 * BOBService.model.Request)
	 */
	public Response testJcoconnection(final Request Request) {
		Response RegResponse;
		try {
			RegResponse = portalUMEImpl.initPortalUser(Request);
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc, "PortalDetailsResponseImpl.initialiseUser",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lp.BOBService.service.PortalDetailsResponse#RegisterEncryptSession(com
	 * .lp.BOBService.model.Request)
	 */
	public Response registerEncryptSession(final Request Request) {

		Response RegResponse;
		try {
			RegResponse = portalUMEImpl.getEncryptPwd(Request.getEmailAddress());
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
					"PortalDetailsResponseImpl.registerEncryptSession",
					"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
			return RegResponse;
		}
		httpSession.setAttribute("encryptSession", RegResponse.getToken());
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#getEncryptSession()
	 */
	public String getEncryptSession() {
		return (httpSession.getAttribute("encryptSession") != null)
				? httpSession.getAttribute("encryptSession").toString()
				: null;
	}

	public void killSession() {
		httpSession.setAttribute("encryptSession", null);
	}

	public void switchGoogleAPI(String indicator) {
		AppData.googleAPI = indicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#checkEncryptSession
	 * (com.lp.BOBService.model.Request)
	 */
	public Response checkEncryptSession(Request Request) {
		Response RegResponse = new Response();
		String sessionValue = getEncryptSession();
		if (sessionValue == null) {
			RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			RegResponse.setErrCode(AppConstants.ERROR_CODE_SESSIONLOST);
			RegResponse.setErrReason(AppConstants.MSG_SESSION_LOST);
		} else {
			try {
				RegResponse = portalUMEImpl.getEncryptPwd(Request.getEmailAddress());
				RegResponse.setGroupType(portalUMEImpl.checkGroup(Request.getEmailAddress()));

			} catch (ConnectorException e) {
				// TODO Auto-generated catch block
				SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
						"PortalDetailsResponseImpl.checkEncryptSession",
						"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
				SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
				RegResponse = new Response();
				RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
				RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
				RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				return RegResponse;
			}

			if (sessionValue.equals(RegResponse.getToken())) {
				SimpleLogger.trace(Severity.INFO, loc,
						" sessionValue is  :" + sessionValue + " Token is " + RegResponse.getToken());
				RegResponse.setReturnUME(AppConstants.RETURN_UME_TRUE);
			} else {
				SimpleLogger.trace(Severity.INFO, loc,
						" failed sessionValue is  :" + sessionValue + " Token is " + RegResponse.getToken());
				RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
				RegResponse.setErrCode(AppConstants.ERROR_CODE_INCORRECT_ENCRYPTPWD);
				RegResponse.setErrReason(AppConstants.MSG_PWD_INCORRECT);
			}
		}
		return RegResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.BOBService.service.PortalDetailsResponse#
	 * checkCompleteRegisterPermission (com.lp.BOBService.model.Request)
	 */
	public Response checkCompleteRegisterPermission(Request Request) {
		Response RegResponse = new Response();
		String logonId = Request.getEmailAddress();
		String groupType = portalUMEImpl.checkGroup(logonId);
		SimpleLogger.trace(Severity.INFO, loc, "checkCompleteRegisterPermission groupType is  :" + groupType);
		if (groupType.equalsIgnoreCase(AppConstants.SELF_COMPLETED_GROUP_TYPE)
				|| groupType.equalsIgnoreCase(AppConstants.SELF_PENDING_GROUP_TYPE)) {
			// additional check for pending group
			if (groupType.equalsIgnoreCase(AppConstants.SELF_PENDING_GROUP_TYPE)) {
				if (portalUMEImpl.checkDriverGroup(logonId)) {
					Response RegResponseUME;
					try {
						RegResponseUME = portalUMEImpl.assignApprovalGroup(logonId);
						if (AppConstants.RETURN_UME_TRUE.equalsIgnoreCase(RegResponseUME.getReturnUME())) {
							portalUMEImpl.removeGroup(logonId, AppConstants.SELF_PENDING_GROUP);
							groupType = AppConstants.SELF_APPROVAL_GROUP_TYPE;
						}
					} catch (ConnectorException ec) {
						SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
								"PortalDetailsResponseImpl.checkCompleteRegisterPermission",
								"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
						SimpleLogger.traceThrowable(Severity.ERROR, loc, "", ec);

					}
				}
			}
			// end additional check
		}
		// login validation

		if (groupType.equalsIgnoreCase(AppConstants.SELF_COMPLETED_GROUP_TYPE)
				|| groupType.equalsIgnoreCase(AppConstants.SELF_PENDING_GROUP_TYPE)) {
			try {
				RegResponse = portalUMEImpl.validateAccount(Request.getEmailAddress(), Request.getPassword());
				if (AppConstants.RETURN_UME_TRUE.equals(RegResponse.getReturnUME())) { // if pass validation ,set
																						// encryptSession with token
					httpSession.setAttribute("encryptSession", RegResponse.getToken());
				}

			} catch (ConnectorException e) {
				// TODO Auto-generated catch block
				SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
						"PortalDetailsResponseImpl.checkCompleteRegisterPermission",
						"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
				SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
				RegResponse = new Response();
				RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
				RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
				return RegResponse;
			}

		} else {
			RegResponse.setErrCode(AppConstants.ERROR_CODE_PERMISSION_DENY);
			RegResponse.setErrReason(AppConstants.MSG_DENY);
			RegResponse.setReturnUME(AppConstants.RETURN_UME_FALSE);
			RegResponse.setGroupType(groupType);
			// as initial group will set up cookie for providing possibility recording email
			// sending information
			if (groupType.equalsIgnoreCase(AppConstants.SELF_INIT_GROUP_TYPE)) {
				// set logon id cookie
				Cookie logonIdCookie = new Cookie("selfRegLogonId", logonId);
				// setting max age to be 30 mins
				logonIdCookie.setMaxAge(30 * 60);
				logonIdCookie.setPath("/");
				httpResponse.addCookie(logonIdCookie);

				// set session for security
				Response RegResponseValidateAccount = new Response();
				try {
					RegResponseValidateAccount = portalUMEImpl.validateAccount(logonId, Request.getPassword());
					if (AppConstants.RETURN_UME_TRUE.equals(RegResponseValidateAccount.getReturnUME())) { // if pass
																											// validation
																											// ,set
																											// encryptSession
																											// with
																											// token
						httpSession.setAttribute("encryptSessionInitial", RegResponseValidateAccount.getToken());
					}

				} catch (ConnectorException e) {
					SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
							"PortalDetailsResponseImpl.checkCompleteRegisterPermission.checkAccountValidation-initial",
							"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
					SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
					;
				}

			}
			// ending
			// as approval group first login set department as "Driver" for indicator as
			// user login
			if (groupType.equalsIgnoreCase(AppConstants.SELF_APPROVAL_GROUP_TYPE)) {
				Response RegResponseValidateAccount = new Response();
				try {

					RegResponseValidateAccount = portalUMEImpl.validateAccount(logonId, Request.getPassword());
				} catch (ConnectorException e) {
					SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
							"PortalDetailsResponseImpl.checkCompleteRegisterPermission.checkAccountValidation-Approval",
							"Call PortalJcoImpl with request of " + PortalServiceUtils.converToJson(Request));
					SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
				}
				if (AppConstants.RETURN_UME_TRUE.equals(RegResponseValidateAccount.getReturnUME())) { // if pass
																										// validation,
																										// update
																										// Department
																										// value as
																										// indicator
					// httpSession.setAttribute("encryptSession", RegResponse.getToken());
					portalUMEImpl.updateUserDepartment(logonId);
				}

			}
		}

		RegResponse.setGroupType(groupType);
		return RegResponse;
	}

	public String getGoogleGeoTest(String request) {

		SocketAddress addr = new InetSocketAddress("lpautmg0001.ap.leaseplancorp.net", 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
		GeoApiContext context = new GeoApiContext.Builder().proxy(proxy)
				.apiKey("AIzaSyB-W0s81dBYgPLFIhPLrI7wv-vmJJPYTbg").build();
		InetAddress Ip = null;

		try {
			Ip = InetAddress.getLocalHost();
			SimpleLogger.trace(Severity.INFO, loc, "getGoogleGeoTest ip is  :" + Ip);

		} catch (UnknownHostException e1) {

			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e1);
		}
		GeocodingResult[] results;

		String trn = "";
		try {
			results = GeocodingApi.geocode(context, request).await();

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// System.out.println(gson.toJson(results[0].addressComponents));

			trn = gson.toJson(results[0].addressComponents);
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// ApiException.from(status, errorMessage)
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
		}
		return trn;
	}

	@Override
	// services/data/v20.0/sobjects/Account/
	public Response callSalesforceNZ(Request Request) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpHost target = new HttpHost(AppConstants.SALESFORCE_SITE, 443, "https");
		HttpHost proxy = new HttpHost("10.19.21.1", 8080, "http");
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

		Response regResponse = new Response();

		try {
			String queryURL = Request.getSalesforceInstanceUrl() + "/services/data/v20.0/sobjects/Account/";
			SimpleLogger.trace(Severity.INFO, loc,
					"queryURL  is :" + queryURL + "with token is " + Request.getSalesforceAccessToken());
			HttpGet httpGet = new HttpGet(queryURL);

			SimpleLogger.trace(Severity.INFO, loc,
					"Executing request " + httpGet.getRequestLine() + " to " + target + " via " + proxy);
			httpGet.setConfig(config);

			Header oauthHeader = new BasicHeader("Authorization", "Bearer " + Request.getSalesforceAccessToken());
			Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
			httpGet.addHeader(oauthHeader);
			httpGet.addHeader(prettyPrintHeader);

			HttpResponse response = client.execute(httpGet);

			final int statusCode = response.getStatusLine().getStatusCode();
			SimpleLogger.trace(Severity.INFO, loc, "get response status is  :" + statusCode);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();

			if (statusCode != HttpStatus.SC_OK) {
				// System.out.println("Error authenticating to salesforce: " + statusCode);
				SimpleLogger.trace(Severity.INFO, loc, "response result from salesforce  :" + result.toString());
				regResponse.setAppStatus(AppConstants.RETURN_FALSE);
				return regResponse;
			}

			String response_string = EntityUtils.toString(response.getEntity());
			SimpleLogger.trace(Severity.INFO, loc, "results is  :" + response_string);

			JSONObject jObject = new JSONObject(response_string);
			JSONArray recentItems = jObject.getJSONArray("recentItems");
			/*
			 * Iterator<?> keys = jObject.keys();
			 * 
			 * while (keys.hasNext()) { String key = (String) keys.next(); String value =
			 * jObject.getString(key); map.put(key, value); }
			 * 
			 * 
			 */
			Iterator<Object> iteratorRecentItems = recentItems.iterator();
			HashMap<String, List<?>> map = new HashMap<String, List<?>>();
			List<String> accountObjlist = new ArrayList<String>();
			while (iteratorRecentItems.hasNext()) {
				JSONObject next = (JSONObject) iteratorRecentItems.next();
				Iterator<?> keys = next.keys();
				accountObjlist.add(next.toString());
				/*
				 * while (keys.hasNext()) { String key = (String) keys.next(); if
				 * (!"attributes".equalsIgnoreCase(key)) { String value = next.getString(key);
				 * accountObjlist.add(new AccountObj(key,value)); map.put(key, value); } }
				 */

			}
			map.put("recentItems", accountObjlist);
			regResponse.setStrSalseforceResults(response_string);
			regResponse.setSalseforceResults(map);
			regResponse.setAppStatus(AppConstants.RETURN_TRUE);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "ClientProtocolException {}  :" + e.getMessage());
			// logger.error("ClientProtocolException {} ", e.getMessage());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "UnsupportedOperationException {}  :" + e.getMessage());
			// logger.error("UnsupportedOperationException {} ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "IOException {}  :" + e.getMessage());
			// logger.error("IOException {} ", e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "Exception {}  :" + e.getMessage());
		}
		return regResponse;

	}

	@Override
	public Response getAccessToken(String code) {
		// System.setProperty("https.proxyHost", "10.19.21.1");
		// System.setProperty("https.proxyPort", "8080");

		HttpClient client = HttpClientBuilder.create().build();
		HttpHost target = new HttpHost(AppConstants.SALESFORCE_SITE, 443, "https");
		HttpHost proxy = new HttpHost("10.19.21.1", 8080, "http");
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		Response regResponse = new Response();
		try {
			String requireTokenURL = AppConstants.SALESFORCE_SITE + AppConstants.SALESFORCE_OAUTH2
					+ "?grant_type=authorization_code&code=" + code + "&client_id=" + AppConstants.CUST_KEY
					+ "&client_secret=" + AppConstants.CUST_SECRET
					+ "&redirect_uri=https://portaldva.leaseplan.com.au/nz-poc/index.html";
			SimpleLogger.trace(Severity.INFO, loc, "require Token URL  :" + requireTokenURL);

			HttpPost httpPost = new HttpPost(requireTokenURL);
			SimpleLogger.trace(Severity.INFO, loc,
					"Executing request " + httpPost.getRequestLine() + " to " + target + " via " + proxy);

			httpPost.setConfig(config);
			HttpResponse response = client.execute(httpPost);

			final int statusCode = response.getStatusLine().getStatusCode();
			SimpleLogger.trace(Severity.INFO, loc, "get response status is  :" + statusCode);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("Error authenticating to salesforce: " + statusCode);
				regResponse.setAppStatus(AppConstants.RETURN_FALSE);
				return regResponse;
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			JSONObject jObject = new JSONObject(result.toString());
			Iterator<?> keys = jObject.keys();
			HashMap<String, String> map = new HashMap<String, String>();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = jObject.getString(key);
				map.put(key, value);
			}

			String salesforceAccessToken = map.get("access_token");
			String salesforceInstanceUrl = map.get("instance_url");
			String salesforceRefreshToken = map.get("refresh_token");
			String salesforceSignature = map.get("signature");

			regResponse.setSalesforceAccessToken(salesforceAccessToken);
			regResponse.setSalesforceInstanceUrl(salesforceInstanceUrl);
			regResponse.setSalesforceRefreshToken(salesforceRefreshToken);
			regResponse.setSalesforceSignature(salesforceSignature);
			regResponse.setAppStatus(AppConstants.RETURN_TRUE);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "ClientProtocolException {}  :" + e.getMessage());
			// logger.error("ClientProtocolException {} ", e.getMessage());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "UnsupportedOperationException {}  :" + e.getMessage());
			// logger.error("UnsupportedOperationException {} ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "IOException {}  :" + e.getMessage());
			// logger.error("IOException {} ", e.getMessage());

		}
		return regResponse;
	}

	@Override
	public Response createSalesforceNZ(Request Request) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpHost target = new HttpHost(AppConstants.SALESFORCE_SITE, 443, "https");
		HttpHost proxy = new HttpHost("10.19.21.1", 8080, "http");
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		Response regResponse = new Response();
		try {
			String createAccountURL = Request.getSalesforceInstanceUrl() + "/services/data/v20.0/sobjects/Account/";
			SimpleLogger.trace(Severity.INFO, loc, "createAccountURL  :" + createAccountURL);
			JSONObject lead = new JSONObject();
			lead.put("Name", Request.getFirstName());
			HttpPost httpPost = new HttpPost(createAccountURL);
			SimpleLogger.trace(Severity.INFO, loc,
					"Executing request " + httpPost.getRequestLine() + " to " + target + " via " + proxy);

			httpPost.setConfig(config);
			Header oauthHeader = new BasicHeader("Authorization", "Bearer " + Request.getSalesforceAccessToken());
			Header contentType = new BasicHeader("Content-Type", "application/json");

			StringEntity body = new StringEntity(lead.toString(1));
			body.setContentType("application/json");
			httpPost.setEntity(body);

			httpPost.addHeader(oauthHeader);
			httpPost.addHeader(contentType);
			httpPost.setEntity(body);

			HttpResponse response = client.execute(httpPost);

			final int statusCode = response.getStatusLine().getStatusCode();
			SimpleLogger.trace(Severity.INFO, loc, "get response status is  :" + statusCode);

			if (statusCode == 201) {
				String response_string = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(response_string);

				SimpleLogger.trace(Severity.INFO, loc, "sucessfull response is " + response_string);
			} else {
				SimpleLogger.trace(Severity.INFO, loc, "faled code is " + statusCode + "");
			}

			regResponse.setAppStatus(AppConstants.RETURN_TRUE);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "ClientProtocolException {}  :" + e.getMessage());
			// logger.error("ClientProtocolException {} ", e.getMessage());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "UnsupportedOperationException {}  :" + e.getMessage());
			// logger.error("UnsupportedOperationException {} ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "IOException {}  :" + e.getMessage());
			// logger.error("IOException {} ", e.getMessage());

		}
		return regResponse;
	}

	@Override
	public Response getDrivers(Request Request) {

		Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
		HttpClient client = HttpClientBuilder.create().build();
		HttpHost target = new HttpHost(AppConstants.SALESFORCE_SITE, 443, "https");
		HttpHost proxy = new HttpHost("10.19.21.1", 8080, "http");
		/// RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		Response regResponse = new Response();
		try {

			String fetchDriversURL = "http://10.19.6.12:8184/lpnzsrxs/rxsrtr/vehicles?clientNumber="
					+ Request.getCustomerNumber() + "&limit=10";
			SimpleLogger.trace(Severity.INFO, loc, "fetchDriversURL  :" + fetchDriversURL);

			HttpGet httpGet = new HttpGet(fetchDriversURL);
			SimpleLogger.trace(Severity.INFO, loc,
					"Executing request " + httpGet.getRequestLine() + " via proxy " + proxy);
			// httpGet.setConfig(config);

			httpGet.addHeader(prettyPrintHeader);

			HttpResponse response = client.execute(httpGet);

			final int statusCode = response.getStatusLine().getStatusCode();
			SimpleLogger.trace(Severity.INFO, loc, "get response status is  :" + statusCode);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();

			if (statusCode != HttpStatus.SC_OK) {
				// System.out.println("Error authenticating to salesforce: " + statusCode);
				SimpleLogger.trace(Severity.INFO, loc, "response result from salesforce  :" + result.toString());
				regResponse.setAppStatus(AppConstants.RETURN_FALSE);
				return regResponse;
			}

			String response_string = EntityUtils.toString(response.getEntity());
			SimpleLogger.trace(Severity.INFO, loc, "results is  :" + response_string);

			JSONObject jObject = new JSONObject(response_string);
			JSONArray recentItems = jObject.getJSONArray("vehicles");
			/*
			 * Iterator<?> keys = jObject.keys();
			 * 
			 * while (keys.hasNext()) { String key = (String) keys.next(); String value =
			 * jObject.getString(key); map.put(key, value); }
			 * 
			 * 
			 */
			Iterator<Object> iteratorRecentItems = recentItems.iterator();
			HashMap<String, List<?>> map = new HashMap<String, List<?>>();
			List<String> accountObjlist = new ArrayList<String>();
			while (iteratorRecentItems.hasNext()) {
				JSONObject next = (JSONObject) iteratorRecentItems.next();
				Iterator<?> keys = next.keys();
				accountObjlist.add(next.toString());
				/*
				 * while (keys.hasNext()) { String key = (String) keys.next(); if
				 * (!"attributes".equalsIgnoreCase(key)) { String value = next.getString(key);
				 * accountObjlist.add(new AccountObj(key,value)); map.put(key, value); } }
				 */

			}
			map.put("vehicles", accountObjlist);
			regResponse.setStrSalseforceResults(response_string);
			regResponse.setSalseforceResults(map);
			regResponse.setAppStatus(AppConstants.RETURN_TRUE);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "ClientProtocolException {}  :" + e.getMessage());
			// logger.error("ClientProtocolException {} ", e.getMessage());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "UnsupportedOperationException {}  :" + e.getMessage());
			// logger.error("UnsupportedOperationException {} ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "IOException {}  :" + e.getMessage());
			// logger.error("IOException {} ", e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			regResponse.setAppStatus(AppConstants.RETURN_FALSE);
			SimpleLogger.trace(Severity.ERROR, loc, "Exception {}  :" + e.getMessage());
		}
		return regResponse;
	}

	@Override
	public Response resetPassword(Request Request) {
		Response RegResponse;
		try {
			RegResponse = portalUMEImpl.resetPassword(Request);
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			RegResponse = new Response();
			RegResponse.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			RegResponse.setErrReason(AppConstants.CALL_LEASEPLAN);
		}
		return RegResponse;
	}

	@Override
	public Response validateAccount(String logonId, String password) {
		// TODO Auto-generated method stub
		Response response = new Response();
		AuthUser authUser = new AuthUser();
		try {
			Response res = portalUMEImpl.validateAccount(logonId, password);
			authUser.setAuthentication(res.getReturnUME());
			authUser.setLogonId(logonId);
			authUser.setEmailAddress(res.getEmailAddress());
			authUser.setStatus(res.getErrCode());
			response.setAuthUser(authUser);
		} catch (ConnectorException e) {
			SimpleLogger.log(Severity.ERROR, Category.SYS_SERVER, loc,
					"PortalDetailsResponseImpl.checkAccountValidation",
					"Call PortalJcoImpl with request of " + logonId);
			SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			authUser.setAuthentication(AppConstants.RETURN_UME_FALSE);
			authUser.setStatus(AppConstants.ERROR_CODE_JCO_EXCETPION);
			authUser.setLogonId(logonId);
			response.setErrCode(AppConstants.ERROR_CODE_JCO_EXCETPION);
			response.setErrReason(AppConstants.CALL_LEASEPLAN);
			response.setAuthUser(authUser);
			
		}
		return response;

	}

}
