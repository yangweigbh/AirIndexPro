package com.yangwei.airindexpro.util;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yangwei.airindexpro.BuildConfig;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class ValidCityLoader extends AsyncTaskLoader<ArrayList<String>> {
	
	private static final String TAG = "ValidCityLoader";

	public ValidCityLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override protected void onStartLoading() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onStartLoading");
		}
		
		forceLoad();
	};

	@Override
	public ArrayList<String> loadInBackground() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "loadInBackground");
		}
		
		HttpClient client = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			URI uri = new URI(Constant.valid_city_url + "token=" + Constant.token);
			client = AndroidHttpClient.newInstance("android");
			HttpGet httpRequest = new HttpGet(uri);
			String jsonContent = client.execute(httpRequest, new BasicResponseHandler());
			try {
				System.out.println("content: " + jsonContent);
				JSONObject object = new JSONObject(jsonContent);
				JSONArray cityarray = object.getJSONArray("cities");
				for (int i = 0; i < cityarray.length(); i++) {
					result.add(cityarray.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				try {
					JSONObject error = new JSONObject(jsonContent);
					Log.d(TAG, error.getString("error"));
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				((AndroidHttpClient)client).close();
			}
		}
		return result;
	}

}
