package com.mojodigi.khulasaNewsLite.AddsUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	

	


	public static String getkeyValue_Str(JSONObject jo, String tag) {
		String key_value = null;
		if (jo.has(tag)) {
			try {
				key_value = jo.getString(tag);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		return key_value;
	}

	public static Double getkeyValue_Double(JSONObject jo, String tag) {
		Double key_value = 0d;
		if (jo.has(tag)) {
			try {
				key_value = jo.getDouble(tag);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		return key_value;
	}
}
