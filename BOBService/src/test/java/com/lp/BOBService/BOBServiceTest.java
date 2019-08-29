package com.lp.BOBService;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;

import com.lp.BOBService.utils.AppConstants;
import com.sap.tc.logging.Category;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

public class BOBServiceTest {
	ApplicationContext appContext;
	StringBuffer checkEmailJsonStr = null;
	StringBuffer checkCustomerNumberJsonStr = null;
	StringBuffer checkWorkEmailJsonStr = null;
	StringBuffer checkRegisterCustomerJsonStr = null;
	StringBuffer checkinitialiseUserJsonStr = null;
	StringBuffer checkVerifyinitialiseUserJsonStr = null;
	JSONObject jsonObject =null;

	private final String DOMAIN_URL = "https://portaldva.leaseplan.com.au"; // http://lpausap1003.ap.leaseplancorp.net:58000
	
	
	private final String google_server = "https://www.google.com/recaptcha/api/siteverify";
	private final String CHECK_EMAIL = DOMAIN_URL
			+ "/BOBService/checkEmail/";
	private final String CHECK_CUSTOMER_NUMBER = DOMAIN_URL
			+ "/BOBService/checkCustomerNumber/";
	private final String VALIDATE_WORK_EMAIL = DOMAIN_URL
			+ "/BOBService/validateWorkEmail/";
	private final String REGISTER_CUSTOMER = DOMAIN_URL
			+ "/BOBService/registerCustomer/";
	private final String INITIALISE_USER = DOMAIN_URL
			+ "/BOBService/initialiseUser/";
	private final String VERIFY_INITIAL_USER = DOMAIN_URL
			+ "/BOBService/verifyInitialUser/";

	@Before
	public void setUp() throws Exception {
		checkEmailJsonStr = assembleJsonObject("checkEmail.json");
		checkCustomerNumberJsonStr = assembleJsonObject("checkCustomerNumber.json");
		checkWorkEmailJsonStr = assembleJsonObject("validateWorkEmail.json");
		checkRegisterCustomerJsonStr = assembleJsonObject("registerCustomer.json");
		checkinitialiseUserJsonStr = assembleJsonObject("initialiseUser.json");
		checkVerifyinitialiseUserJsonStr = assembleJsonObject("verifyInitialUser.json");
		jsonObject =  new JSONObject(checkinitialiseUserJsonStr.toString());
	}
	
	//@Test
	public void testDemo() {
		System.out.println(checkinitialiseUserJsonStr.toString());
		
		try {
			
		//	jsonObject =  new JSONObject(checkinitialiseUserJsonStr.toString());

			JSONArray initialiseUser = (JSONArray) jsonObject
					.get("initialiseUser");
			Iterator<Object> iteratorinitialiseUsers = initialiseUser
					.iterator();
			while (iteratorinitialiseUsers.hasNext()) {
				JSONObject next = (JSONObject) iteratorinitialiseUsers.next();
				System.out.println("object string is " + next.toString());

			}
		} catch (Exception e) {

		}

	}

    
	public void testInitialiseUser() {
		System.out.println("starting test initialiseUser....");
		HttpClient httpClient = new HttpClient();
		try {
			JSONArray initialiseUser = (JSONArray) jsonObject
					.get("initialiseUsers");
			Iterator<Object> iteratorinitialiseUsers = initialiseUser
					.iterator();
			try {
				while (iteratorinitialiseUsers.hasNext()) {
					JSONObject next = (JSONObject) iteratorinitialiseUsers.next();
					
				StringRequestEntity stringEntity = new StringRequestEntity(
						next.toString(), "application/json", "UTF-8");
				PostMethod postMethod = new PostMethod(INITIALISE_USER);
				postMethod.setRequestEntity(stringEntity);
				int status = httpClient.executeMethod(postMethod);
				HashMap<String, String> map = this.buildHashMap(postMethod);
				System.out.println("-->" + map.get("returnCRM"));
				postMethod.releaseConnection();
				assertEquals("testInitialiseUser", 200, status);
				assertEquals("testInitialiseUser", "true", map.get("returnCRM"));
				assertEquals("testInitialiseUser", "created", map.get("returnUME"));
				assertEquals("testInitialiseUser", "Validation email sent", map.get("errReason"));
				}
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
		
	}
	
	//@Test
	public void testGoogleServer() {
		System.out.println("starting test initialiseUser....");
		HttpClient httpClient = new HttpClient();
		String recaptchaResponseToken ="tstestest";
		try {
			
			System.setProperty("http.proxyHost", "10.19.21.3");
			System.setProperty("http.proxyPort", "8080");
			
			try {
				
			
					
				GetMethod getMethod = new GetMethod(AppConstants.SITE_VERIFY_URL+"?secret=6LebGTMUAAAAAHyjyukKIxTF1byFg9wXtenkEgc3&response="+recaptchaResponseToken);
				int statusCode = httpClient.executeMethod(getMethod);
			
				
		
				//System.out.println("-->" + map.get("returnCRM"));
				getMethod.releaseConnection();
			
				
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
		
	}
  
	
	
	
	//@Test
	public void  performRecaptchaSiteVerify()
	  	       throws IOException {
		
		 String recaptchaResponseToken = "03AO6mBfxhtJUtaDegHcvqtYGFw1rlz6K8psn7f1mDDHM8nxkRrNVgvhls7SJ_Dju78sgMaifwF1epM9qER4UMG-v3GJ8KfD4GicBIYGXvkRca_XEALiTCFEiBhlolz_zXqRMxRkFlBrINr9aU8yMeaX2qRuIwvZEC7zY6rDCkcDckYkh2MuEpYijnHHF-oCXq4_sUf2Xt_2VWtgH0V7Z1WDTPjsstzqexzdrc7PdJpDv_by_TJ8vjTTC2XJbWVf-ue8lUgZTBVyQ8Ta0N7pO1p3XKxCY6ZNPa7yoRb94EnJi4iKUp7lvsXAp5uXfWmwUHt22mK_ZGo_wfXE5Q1Ywqj1RzPe8JSJTO_3A7R4clU87jaeNfIX3VBOTegZBkipldOo5XtkIyevXB0TMDK4FWrHnrBq6cQCNC8tCLLaJGpTgnY9Z8vP-HOWR_fN2NnVBZt-w9tE6dr1Sz"; 
	  	// URL url = new URL("http","10.19.21.3", 8080, AppConstants.SITE_VERIFY_URL);
		 URL url = new URL(AppConstants.SITE_VERIFY_URL);
	  	        	
	  	 StringBuilder postData = new StringBuilder();
	  	 addParam(postData, AppConstants.SECRET_PARAM, AppConstants.SITE_SECRET);
	  	 addParam(postData, AppConstants.RESPONSE_PARAM, recaptchaResponseToken);
	  	 
	//	 SimpleLogger.trace(Severity.ERROR,loc," postData.toString() "+ postData.toString()); 
	  	  postAndParseJSON(url, postData.toString());
	  	}
	    
		private void  postAndParseJSON(URL url, String postData) throws IOException {
			
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.19.21.3", 8080));
			System.out.println("starting test return from google server is postDatais  ...." + postData.toString());
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(proxy);
	    	System.out.println("set up connection");
	        urlConnection.setDoOutput(true);
	      
	        urlConnection.setRequestMethod("POST");
	        urlConnection.setRequestProperty(
	                "Content-Type", "application/x-www-form-urlencoded");
	        urlConnection.setRequestProperty(
	                "charset", StandardCharsets.UTF_8.displayName());
	        urlConnection.setRequestProperty(
	                "Content-Length", Integer.toString(postData.length()));
	        urlConnection.setUseCaches(false);
	        urlConnection.getOutputStream()
	                .write(postData.getBytes(StandardCharsets.UTF_8));
	        System.out.println("finish up data sending connection");
	        
	     //   SimpleLogger.trace(Severity.ERROR,loc,"urlConnection.getOutputStream() "+ urlConnection.getOutputStream().toString()); 
	    
	     //   JSONTokener jsonTokener = new JSONTokener(urlConnection.getInputStream());
	        
	        String result = "";
	        StringBuffer sb = new StringBuffer();
	        InputStream is = null;

	        
	        try {
	            is = new BufferedInputStream(urlConnection.getInputStream());
	            
	            System.out.println("get data from connection");
	            BufferedReader br = new BufferedReader(new InputStreamReader(is));
	            String inputLine = "";
	            while ((inputLine = br.readLine()) != null) {
	            	System.out.println(" inputLine...." + inputLine);
	                sb.append(inputLine);
	            }
	            result = sb.toString();
	        }
	        catch (Exception e) {
	         //   Log.i(TAG, "Error reading InputStream");
	            result = null;
	        }
	        finally {
	            if (is != null) {
	                try { 
	                    is.close(); 
	                } 
	                catch (IOException e) {
	              //      Log.i(TAG, "Error closing InputStream");
	                }
	            }   
	        }
	        
	        
	        
	    	System.out.println("starting test return from google server is ...." + result);
	        //System.out new JSONObject(jsonTokener);
	      }
	      
		
		
		
		 private static StringBuilder addParam(
	             StringBuilder postData, String param, String value)
	             throws UnsupportedEncodingException {
	       if (postData.length() != 0) {
	         postData.append("&");
	       }
	       return postData.append(
	               String.format("%s=%s",
	                       URLEncoder.encode(param, StandardCharsets.UTF_8.displayName()),
	                       URLEncoder.encode(value, StandardCharsets.UTF_8.displayName())));
	     }
		 
		 
	
	 /*public static String getResponse(String recaptchaResponseToken){
		 
		 
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(AppConstants.SITE_VERIFY_URL+"?secret="+AppConstants.SITE_SECRET+"&response="+recaptchaResponseToken);
			   SimpleLogger.trace(Severity.ERROR,loc,"getResponse start"); 
         try {
				int statusCode = httpClient.executeMethod(getMethod);
				 SimpleLogger.trace(Severity.ERROR,loc,"getResponse end1"); 
				  byte[] responseBody = getMethod.getResponseBody();
				  SimpleLogger.trace(Severity.ERROR,loc,"getResponse end2"); 
			       SimpleLogger.log(Severity.INFO, Category.SYS_LOGGING, loc, "validateRecaptcha.responseBody",   " responseBody " + responseBody.toString() );
			       SimpleLogger.trace(Severity.ERROR,loc,"getResponse end"); 
			       
			} catch (Exception e) {
				// TODO Auto-generated catch block
			      SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);
			      SimpleLogger.trace(Severity.ERROR,loc,"getResponse "+ e.getStackTrace()); 
				//e.printStackTrace();
			}
			
        return "";
     			
     	
	 }*/
	
	
	 
	
  
	/**
	 * test case for failing to verification of initial user
	 */
	//@Test
	public void testVerifyInitialUser() {
		System.out.println("starting test verifyInitialiseUser....................................................................................");
		HttpClient httpClient = new HttpClient();
		try {
			JSONArray verifyInitialUser = (JSONArray) jsonObject
					.get("verifyInitialUsers");
			Iterator<Object> iteratorVerifyInitialUser = verifyInitialUser
					.iterator();
			try {
				while (iteratorVerifyInitialUser.hasNext()) {
					JSONObject next = (JSONObject) iteratorVerifyInitialUser.next();
				StringRequestEntity stringEntity = new StringRequestEntity(
						next.toString(), "application/json", "UTF-8");
				PostMethod postMethod = new PostMethod(VERIFY_INITIAL_USER);
				postMethod.setRequestEntity(stringEntity);
				int status = httpClient.executeMethod(postMethod);
				HashMap<String, String> map = this.buildHashMap(postMethod);
				postMethod.releaseConnection();
				assertEquals("testVerifyInitialUser", "true", map.get("returnCRM"));
				assertEquals("testVerifyInitialUser", 200, status);
				}
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
	}

  //  @Test
	public void testCheckEmail() {
		// System.out.println(checkEmailJsonStr);
    	System.out.println("starting test testCheckEmail.............................................................................");
		HttpClient httpClient = new HttpClient();
		
		try {
			JSONArray verifyCheckEmails = (JSONArray) jsonObject
					.get("checkEmails");
			Iterator<Object> iteratorVerifyCheckEmails = verifyCheckEmails
					.iterator();
			try {
				while (iteratorVerifyCheckEmails.hasNext()) {
					JSONObject next = (JSONObject) iteratorVerifyCheckEmails.next();
				
				StringRequestEntity stringEntity = new StringRequestEntity(
						next.toString(), "application/json", "UTF-8");
				PostMethod postMethod = new PostMethod(CHECK_EMAIL);
				postMethod.setRequestEntity(stringEntity);
				int status = httpClient.executeMethod(postMethod);
				HashMap<String, String> map = this.buildHashMap(postMethod);
				// System.out.println("-->" + map.get("returnCRM"));
				postMethod.releaseConnection();
				
				assertEquals("testCheckEmail", "0", map.get("errCode"));
				assertEquals("testCheckEmail", "true", map.get("returnCRM"));
				assertEquals("testCheckEmail", 200, status);
				}
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
	}

 //   @Test
	public void testCheckCustomerNumber() {
		System.out.println("starting test testCheckCustomerNumber.............................................................................");
		HttpClient httpClient = new HttpClient();
		try {
			JSONArray verifyCheckCustomerNumber = (JSONArray) jsonObject
					.get("checkCustomerNumbers");
			Iterator<Object> iteratorVerify = verifyCheckCustomerNumber
					.iterator();
			try {
				while (iteratorVerify.hasNext()) {
					JSONObject next = (JSONObject) iteratorVerify.next();
				StringRequestEntity stringEntity = new StringRequestEntity(
						next.toString(), "application/json", "UTF-8");
				PostMethod postMethod = new PostMethod(CHECK_CUSTOMER_NUMBER);
				postMethod.setRequestEntity(stringEntity);
				int status = httpClient.executeMethod(postMethod);
				HashMap<String, String> map = this.buildHashMap(postMethod);
				// System.out.println("-->" + map.get("returnCRM"));
				postMethod.releaseConnection();
				assertEquals("testCheckEmail", "3", map.get("errCode"));
				assertEquals("testCheckEmail", "false", map.get("returnCRM"));
				assertEquals("testCheckCustomerNumber", 200, status);
				}
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
	}
   
    /**
     * check verify workemail sending 
     */
  //  @Test
	public void testValidateWorkEmail() {
		System.out.println("starting test testValidateWorkEmail.............................................................................");
		System.out.println(checkWorkEmailJsonStr);
		HttpClient httpClient = new HttpClient();
		try {
			JSONArray verifyValidateWorkEmail = (JSONArray) jsonObject
					.get("validateWorkEmails");
			Iterator<Object> iteratorVerify = verifyValidateWorkEmail
					.iterator();
			try {
				while (iteratorVerify.hasNext()) {
					JSONObject next = (JSONObject) iteratorVerify.next();
				StringRequestEntity stringEntity = new StringRequestEntity(
						next.toString(), "application/json", "UTF-8");
				PostMethod postMethod = new PostMethod(VALIDATE_WORK_EMAIL);
				postMethod.setRequestEntity(stringEntity);
				int status = httpClient.executeMethod(postMethod);
				HashMap<String, String> map = this.buildHashMap(postMethod);
				// System.out.println("-->" + map.get("returnCRM"));
				postMethod.releaseConnection();
				
				assertEquals("testValidateWorkEmail", "Validation email sent", map.get("errReason"));
				assertEquals("testValidateWorkEmail", "true", map.get("returnCRM"));
				assertEquals("testValidateWorkEmail", 200, status);
				}
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
	}


	public void testRegisterCustomer() {
		System.out.println(checkRegisterCustomerJsonStr);
		HttpClient httpClient = new HttpClient();
		try {
			String json = this.checkRegisterCustomerJsonStr.toString();
			JSONObject jsonObject = new JSONObject(json);
			try {
				StringRequestEntity stringEntity = new StringRequestEntity(
						jsonObject.toString(), "application/json", "UTF-8");
				PostMethod postMethod = new PostMethod(REGISTER_CUSTOMER);
				postMethod.setRequestEntity(stringEntity);
				int status = httpClient.executeMethod(postMethod);
				HashMap<String, String> map = this.buildHashMap(postMethod);
				// System.out.println("-->" + map.get("returnCRM"));
				postMethod.releaseConnection();
				assertEquals("testRegisterCustomer", 200, status);
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpClient = null;
		}
	}


	public void testPerformace() {
		System.out.println("pefermance testing " + checkEmailJsonStr);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			// testCheckEmail();
		}
		long finishTime = System.currentTimeMillis();
		long timeSpan = finishTime - startTime;
		System.out.println("That took: " + timeSpan + " ms start from "
				+ startTime + " end by " + finishTime);
		// HttpClient httpClient =
	}

	private HashMap<String, String> buildHashMap(PostMethod postMethod)
			throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				postMethod.getResponseBodyAsStream());
		byte[] contents = new byte[1024];
		int bytesRead = 0;
		String strFileContents = "";
		while ((bytesRead = bufferedInputStream.read(contents)) != -1) {
			strFileContents = new String(contents, 0, bytesRead);
		}
		strFileContents = strFileContents.replaceAll("null", "\"\"");
		HashMap<String, String> map = new HashMap<String, String>();
		JSONObject jObject = new JSONObject(strFileContents);
		Iterator<?> keys = jObject.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = jObject.getString(key);
			if (StringUtils.isEmpty(value)) {
				value = null;
			}
			map.put(key, value);
		}
		return map;

	}

	private StringBuffer assembleJsonObject(String checkResource) {
		StringBuffer stringBuffer = new StringBuffer();
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(checkResource);
		BufferedReader r = new BufferedReader(
				new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		String jsonString = null;
		try {
			while ((line = r.readLine()) != null) {
				stringBuilder.append(line);
			}
			jsonString = stringBuilder.toString();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return stringBuffer.append(jsonString);
	}
	
	
}
