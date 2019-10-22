/**

 * @author taowe
 * @version 2.0
 * @since  FEB-2019 
 * 
 */

package com.lp.BOBService.controller;

import com.lp.BOBService.model.Request;
import com.lp.BOBService.model.Response;
import com.lp.BOBService.service.PortalService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class BOBController {

	@Autowired
	private PortalService portalService;

	@RequestMapping(value = "/ume/api/resetPassword", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Response resetPassword(@RequestBody Request Request) throws Throwable {
		Response res = portalService.resetPassword(Request);
		return res;
	}

	@RequestMapping(value = "/ume/api/passwordstate/password", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody Response changePassword(@RequestBody Request Request) throws Throwable {
		Response res = portalService.resetPassword(Request);
		return res;

	}

	// services/data/v20.0/sobjects/Account/
	@RequestMapping(value = "/ume/api/callSalesforceNZ", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Response callSalesforceNZ(@RequestBody Request Request) throws Throwable {
		Response res = portalService.callSalesforceNZ(Request);
		return res;
	}

	// services/data/v20.0/sobjects/Account/
	@RequestMapping(value = "/ume/api/createSalesforceNZ", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Response createSalesforceNZ(@RequestBody Request Request) throws Throwable {
		Response res = portalService.createSalesforceNZ(Request);
		return res;
	}

	// access nz data
	@RequestMapping(value = "/ume/api/drivers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Response getDrivers(@RequestBody Request Request) throws Throwable {
		Response res = portalService.getDrivers(Request);

		return res;
	}

	@RequestMapping(value = "/ume/api/getAccessToken", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Response getAccessToken(@RequestBody Request Request) throws Throwable {
		Response res = portalService.getAccessToken(Request.getSalesforceAuthCode());
		return res;
	}

	/* test only */
	@RequestMapping(value = "/ume/{callName}", method = RequestMethod.GET, headers = "Accept=text/plain")
	public @ResponseBody String getSayHello(@PathVariable("callName") String callName) throws Throwable {
		return "Welcome BOBServices I am " + callName + ", How are we ?";
	}

	@RequestMapping(value = "/ume/public/test", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String publicEndpoint() {
		return new JSONObject()
				.put("message",
						"All good! You DO NOT need to be authenticated to call /api/public via proxy and port --> "
								+ System.getProperty("https.proxyHost") + ":" + System.getProperty("https.proxyPort"))
				.toString();
	}

	//Portal UME unique check
	@RequestMapping(value = "/ume/public/authentication/{loginId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Response authenticationEndpoint(@PathVariable("loginId") String loginId
			) throws Throwable {
		Response res = portalService.checkPortalAccountUnique(loginId);
		return res;
	}

	@RequestMapping(value = "/ume/public/umeIdentity", method = RequestMethod.POST, produces = "application/json")

	public @ResponseBody Response createPortalIdentiy(@RequestBody Request Request) {

		Response res = portalService.createUMEIdentity(Request);
		return res;
	}

	//Portal UME authentication 
	@RequestMapping(value = "/ume/public/authentication/{loginId}/{password}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Response authenticationEndpoint(@PathVariable("loginId") String loginId,
			@PathVariable("password") String password) throws Throwable {
		Response res = portalService.validateAccount(loginId, password);
		return res;
	}

	@RequestMapping(value = "/ume/api/test", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String privateEndpoint() {
		//// System.out.println("system proxy" + System.getProperty("https.proxyHost"));
		return new JSONObject().put("message", "All good. You can see this because you are Authenticated.").toString();
	}

	@RequestMapping(value = "/ume/api/test", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public String privateEndpointPUT() {
		return new JSONObject().put("message", "All good for PUT. You can see this because you are Authenticated.")
				.toString();
	}

	@RequestMapping(value = "/ume/api/test", method = RequestMethod.PATCH, produces = "application/json")
	@ResponseBody
	public String privateEndpointPATCH() {
		return new JSONObject().put("message", "All good for PATCH. You can see this because you are Authenticated.")
				.toString();
	}

	@RequestMapping(value = "/ume/api/test", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String privateEndpointPOST() {
		return new JSONObject().put("message", "All good for POST. You can see this because you are Authenticated.")
				.toString();
	}

	@RequestMapping(value = "/ume/api-scoped/test", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String privateScopedEndpoint() {
		return new JSONObject().put("message",
				"All good. You can see this because you are Authenticated with a Token granted the 'read:messages' scope")
				.toString();
	}

}
