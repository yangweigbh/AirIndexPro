package com.yangwei.airindexpro.datasource;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocalDataSource implements IDataSource<CityAirQualityIndex> {

	String source = "["
			+ "{\"aqi\": 82, "
			+ "\"area\": \"北京\","
			+ " \"pm2_5\": 31,"
			+ "\"pm2_5_24h\": 60, "
			+ "\"position_name\": \"吉大\", "
			+ "\"primary_pollutant\": \"颗粒物(PM2.5)\","
			+ "\"quality\": \"良\","
			+ "\"station_code\": \"1367A\","
			+ "\"time_point\": \"2013-03-07T19:00:00Z\"}, "
			+ "{\"aqi\": 50,\"area\": \"北京\",\"pm2_5\": 0,\"pm2_5_24h\": 53,\"position_name\": \"斗门\",\"primary_pollutant\": \"臭氧8小时\",\"quality\": \"轻度污染\",\"station_code\": \"1370A\",\"time_point\": \"2013-03-07T19:00:00Z\"}]";

	public LinkedList<CityAirQualityIndex> getData() {
		LinkedList<CityAirQualityIndex> result = new LinkedList<CityAirQualityIndex>();
		try {
			JSONArray array = new JSONArray(source);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void getData(
			DataReadyListener<LinkedList<CityAirQualityIndex>> listener) {
		listener.dataReady(getData());
		
	}

	@Override
	public void setCity(String city) {
		// TODO Auto-generated method stub
		
	}

}
