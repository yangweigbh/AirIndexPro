package com.yangwei.airindexpro.util;

import java.net.URI;
import java.util.LinkedList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.widget.Toast;

import com.yangwei.airindexpro.BuildConfig;
import com.yangwei.airindexpro.R;
import com.yangwei.airindexpro.datasource.CityAirQualityIndex;

public class RemoteDataLoader extends AsyncTaskLoader<LinkedList<CityAirQualityIndex>> {
	
	private static final String TAG = "RemoteDataLoader";
	String city = "";
	private Context context;

	public RemoteDataLoader(Context context, String city) {
		super(context);
		this.city = city;
		this.context = context;
	}
	
	@Override protected void onStartLoading() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onStartLoading");
		}
		
		forceLoad();
	};

	@Override
	public LinkedList<CityAirQualityIndex> loadInBackground() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "loadInBackground");
		}
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if (!isConnected) {
			return null;
		}
		HttpClient client = null;
		LinkedList<CityAirQualityIndex> result = new LinkedList<CityAirQualityIndex>();
		try {
			URI uri = new URI(Constant.allStationPM2_5 + city + "&token=" + Constant.token);
			client = AndroidHttpClient.newInstance("android");
			HttpGet httpRequest = new HttpGet(uri);
			String jsonContent = client.execute(httpRequest, new BasicResponseHandler());
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "jsoncontent: " + jsonContent);
			}
			try {
				JSONArray array = new JSONArray(jsonContent);
				int length = array.length();
				for (int i = 0; i < length; i++) {
					JSONObject object = array.getJSONObject(i);
					CityAirQualityIndex.Builder builder = new CityAirQualityIndex.Builder();
					result.add(builder.aqi(object.getInt("aqi"))
							.area(object.getString("area"))
							.pm2_5(object.getInt("pm2_5"))
							.pm2_5_24h(object.getInt("pm2_5_24h"))
							.position_name(object.getString("position_name"))
							.primary_pollutant(object.getString("primary_pollutant"))
							.quality(object.getString("quality"))
							.station_code(object.getString("station_code"))
							.time_point(object.getString("time_point"))
							.build());
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
			Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
		} finally {
			if (client != null) {
				((AndroidHttpClient)client).close();
			}
		}
		return result;
	}
	
}
