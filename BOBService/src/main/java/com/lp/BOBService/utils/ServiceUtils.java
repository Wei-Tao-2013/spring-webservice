package com.lp.BOBService.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ServiceUtils {
	private static final Location loc = Location.getLocation(ServiceUtils.class);

	public static String converToJson(Object obj) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(obj);
		return jsonStr;
	}

	public static Object copyProperties(Object fromObj, Object toObj) {
		if (fromObj != null) {
			BeanWrapper bw = new BeanWrapperImpl(fromObj);
			BeanUtils.copyProperties(bw.getWrappedInstance(), toObj);
		} else {
			toObj = null;
		}
		return toObj;
	}

	public static Properties readProperties(String filePath) throws Exception {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(filePath);
		properties.load(fis);
		fis.close();
		return properties;
	}

	public static Boolean checkIpBlock(String Ip) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Iterator<Map.Entry<String, Timestamp>> iterator = AppData.ipBlockList.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Timestamp> entry = iterator.next();
			if ((timestamp.getTime() - entry.getValue().getTime()) > AppData.requestInterval) {
				iterator.remove();
			}
		}

		if (AppData.ipBlockList.containsKey(Ip)) {
			SimpleLogger.trace(Severity.ERROR, loc, "checkIpBlock:: block Ip is " + Ip);
			return false;
		} else {
			AppData.ipBlockList.put(Ip, timestamp);
		}
		return true;
	}

	public static JSONObject performRecaptchaSiteVerify(String recaptchaResponseToken) throws IOException {
		URL url = new URL(AppConstants.SITE_VERIFY_URL);
		StringBuilder postData = new StringBuilder();
		addParam(postData, AppConstants.SECRET_PARAM, AppConstants.SITE_SECRET);
		addParam(postData, AppConstants.RESPONSE_PARAM, recaptchaResponseToken);
		return postAndParseJSON(url, postData.toString());
	}

	private static JSONObject postAndParseJSON(URL url, String postData) throws IOException {

		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("charset", StandardCharsets.UTF_8.displayName());
		urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length()));
		urlConnection.setUseCaches(false);
		urlConnection.getOutputStream().write(postData.getBytes(StandardCharsets.UTF_8));
		JSONTokener jsonTokener = new JSONTokener(urlConnection.getInputStream());
		return new JSONObject(jsonTokener);
	}

	private static StringBuilder addParam(StringBuilder postData, String param, String value)
			throws UnsupportedEncodingException {
		if (postData.length() != 0) {
			postData.append("&");
		}
		return postData.append(String.format("%s=%s", URLEncoder.encode(param, StandardCharsets.UTF_8.displayName()),
				URLEncoder.encode(value, StandardCharsets.UTF_8.displayName())));
	}

}
