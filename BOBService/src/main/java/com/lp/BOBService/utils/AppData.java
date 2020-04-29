package com.lp.BOBService.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AppData implements Serializable {

	/** For serialization. */
	private static final long serialVersionUID = 630864659034596778L;

	/** System Exceptions **/
	public static String googleAPI = "false";
	public static String Mandatory = "true";
	//public static String auth0Token ="";
	public static HashMap<String,String> auth0Token = new HashMap<String,String>();


	public static int maxNumberEmailSent = 3;
	public static int requestInterval = 5000;
	public static Map<String, Timestamp> ipBlockList = new HashMap<String, Timestamp>();

}