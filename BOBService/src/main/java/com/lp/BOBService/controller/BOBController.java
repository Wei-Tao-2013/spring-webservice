/**

 * @author taowe
 * @version 2.0
 * @since  FEB-2019 
 * 
 */

package com.lp.BOBService.controller;

import com.lp.BOBService.model.RegistrationRequest;
import com.lp.BOBService.model.RegistrationResponse;
import com.lp.BOBService.service.PortalDetailsResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value(value = "${auth0.apiAudience}")
	private String apiAudience;
	@Value(value = "${auth0.issuer}")
	private String issuer;
	

	@Autowired
	private PortalDetailsResponse portalDetailsResponse;

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody RegistrationResponse resetPassword(@RequestBody RegistrationRequest registrationRequest)
			throws Throwable {
		RegistrationResponse regResponse = portalDetailsResponse.resetPassword(registrationRequest);
		return regResponse;
	}

	@RequestMapping(value = "/passwordstate/password", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody RegistrationResponse changePassword(@RequestBody RegistrationRequest registrationRequest)
			throws Throwable {
		RegistrationResponse regResponse = portalDetailsResponse.resetPassword(registrationRequest);
		return regResponse;

	}

	// services/data/v20.0/sobjects/Account/
	@RequestMapping(value = "/callSalesforceNZ", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody RegistrationResponse callSalesforceNZ(@RequestBody RegistrationRequest registrationRequest)
			throws Throwable {
		RegistrationResponse regResponse = portalDetailsResponse.callSalesforceNZ(registrationRequest);
		return regResponse;
	}

	// services/data/v20.0/sobjects/Account/
	@RequestMapping(value = "/createSalesforceNZ", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody RegistrationResponse createSalesforceNZ(@RequestBody RegistrationRequest registrationRequest)
			throws Throwable {
		RegistrationResponse regResponse = portalDetailsResponse.createSalesforceNZ(registrationRequest);
		return regResponse;
	}

	// access nz data
	@RequestMapping(value = "/drivers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody RegistrationResponse getDrivers(@RequestBody RegistrationRequest registrationRequest)
			throws Throwable {
		RegistrationResponse regResponse = portalDetailsResponse.getDrivers(registrationRequest);
		
		return regResponse;
	}

	@RequestMapping(value = "/getAccessToken", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody RegistrationResponse getAccessToken(@RequestBody RegistrationRequest registrationRequest)
			throws Throwable {
		RegistrationResponse regResponse = portalDetailsResponse
				.getAccessToken(registrationRequest.getSalesforceAuthCode());
		return regResponse;
	}
	

	/* test only */
	@RequestMapping(value = "/ume/{callName}", method = RequestMethod.GET, headers = "Accept=text/plain")
	public @ResponseBody String getSayHello(@PathVariable("callName") String callName) throws Throwable {
		System.out.println("----------------call-----UME ");
		return "Welcome BOBServices I am " + callName + ", How are we ?";
	}

	@RequestMapping(value = "/ume/public/test", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public String publicEndpoint() {
            return new JSONObject()
                    .put("message", "All good. You DO NOT need to be authenticated to call /api/public.")
                    .toString();
        }

        @RequestMapping(value = "/ume/api/test", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public String privateEndpoint() {
            return new JSONObject()
                    .put("message", "All good. You can see this because you are Authenticated.")
                    .toString();
        }


		@RequestMapping(value = "/ume/api/test", method = RequestMethod.PUT, produces = "application/json")
        @ResponseBody
        public String privateEndpointPUT() {
            return new JSONObject()
                    .put("message", "All good for PUT. You can see this because you are Authenticated.")
                    .toString();
		}

		
		@RequestMapping(value = "/ume/api/test", method = RequestMethod.PATCH, produces = "application/json")
        @ResponseBody
        public String privateEndpointPATCH() {
            return new JSONObject()
                    .put("message", "All good for PATCH. You can see this because you are Authenticated.")
                    .toString();
		}

		
		@RequestMapping(value = "/ume/api/test", method = RequestMethod.POST, produces = "application/json")
        @ResponseBody
        public String privateEndpointPOST() {
            return new JSONObject()
                    .put("message", "All good for POST. You can see this because you are Authenticated.")
                    .toString();
		}
		

        @RequestMapping(value = "/ume/api-scoped/test", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public String privateScopedEndpoint() {
            return new JSONObject()
                    .put("message", "All good. You can see this because you are Authenticated with a Token granted the 'read:messages' scope")
                    .toString();
        }
	

}
