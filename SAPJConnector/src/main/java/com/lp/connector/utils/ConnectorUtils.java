package com.lp.connector.utils;

import com.google.gson.Gson;

public class ConnectorUtils {

	public static String converToJson(Object obj) {
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(obj);
		
		return jsonStr;
	}

}
