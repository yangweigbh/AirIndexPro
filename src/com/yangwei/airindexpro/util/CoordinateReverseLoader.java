package com.yangwei.airindexpro.util;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.util.Log;

import com.yangwei.airindexpro.BuildConfig;

public class CoordinateReverseLoader extends AsyncTaskLoader<Address> {
	private static final String TAG = "CoordinateReverseLoader";
	private Location location;

	public CoordinateReverseLoader(Context context, Location location) {
		super(context);
		this.location = location;
	}
	
	@Override protected void onStartLoading() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onStartLoading");
		}
		
		forceLoad();
	};

	@Override
	public Address loadInBackground() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "loadInBackground");
		}
		double longtitude = location.getLongitude();
		double latitude = location.getLatitude();
		
		HttpClient client = null;
		Address result = null;
		try {
			URI uri = new URI(TextUtils.replace(Constant.sogouReverseCodeURL, new String[]{"#"}, new String[]{longtitude+","+latitude}).toString());
			client = AndroidHttpClient.newInstance("android");
			HttpGet httpRequest = new HttpGet(uri);
			String jsonContent = client.execute(httpRequest, new BasicResponseHandler());
			try {
				System.out.println("content: " + jsonContent);
				JSONObject object = new JSONObject(jsonContent);
				if (object.getString("status").equals("ok")) {
					JSONObject data = object.getJSONObject("response").getJSONArray("data").getJSONObject(0);
					result = new Address(data.getString("address"), data.getString("province"), data.getString("city"), longtitude, latitude);
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
