package com.yangwei.airindexpro.datasource;

import java.util.LinkedList;
import com.yangwei.airindexpro.BuildConfig;
import com.yangwei.airindexpro.util.Constant;
import com.yangwei.airindexpro.util.RemoteDataLoader;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

public class RemoteDataSource implements IDataSource<CityAirQualityIndex>{
	private static final String TAG = "RemoteDataSource";
	String allStationPM2_5 = "http://www.pm25.in/api/querys/pm2_5.json?city=";
	String city = "北京";
	Context mContext;
	DataReadyListener<LinkedList<CityAirQualityIndex>> mListener;
	public RemoteDataSource(Context context) {
		super();
		mContext = context;
	}
	
	@Override
	public void getData(DataReadyListener<LinkedList<CityAirQualityIndex>> listener) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getData");
		}
		mListener = listener;
		LoaderManager loaderManager = ((Activity)mContext).getLoaderManager();
		
//		if (loaderManager.getLoader(Constant.remote_data_loader_id) == null) {
//			loaderManager.initLoader(Constant.remote_data_loader_id, null, new LoaderCallbacks<LinkedList<CityAirQualityIndex>>() {
//
//				@Override
//				public Loader<LinkedList<CityAirQualityIndex>> onCreateLoader(int arg0,
//						Bundle arg1) {
//					if (BuildConfig.DEBUG) {
//						Log.d(TAG, "onCreateLoader");
//					}
//					return new RemoteDataLoader(mContext, city);
//				}
//
//				@Override
//				public void onLoadFinished(Loader<LinkedList<CityAirQualityIndex>> arg0,
//						LinkedList<CityAirQualityIndex> arg1) {
//					if (BuildConfig.DEBUG) {
//						Log.d(TAG, "onLoadFinished");
//					}
//					mListener.dataReady(arg1);
//					
//				}
//
//				@Override
//				public void onLoaderReset(Loader<LinkedList<CityAirQualityIndex>> arg0) {
//					if (BuildConfig.DEBUG) {
//						Log.d(TAG, "onLoaderReset");
//					}
//				}
//				
//			});
//		} else {
//			
//		}
		loaderManager.restartLoader(Constant.remote_data_loader_id, null, new LoaderCallbacks<LinkedList<CityAirQualityIndex>>() {

			@Override
			public Loader<LinkedList<CityAirQualityIndex>> onCreateLoader(int arg0,
					Bundle arg1) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onCreateLoader");
				}
				return new RemoteDataLoader(mContext, city);
			}

			@Override
			public void onLoadFinished(Loader<LinkedList<CityAirQualityIndex>> arg0,
					LinkedList<CityAirQualityIndex> arg1) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onLoadFinished");
				}
				mListener.dataReady(arg1);
				
			}

			@Override
			public void onLoaderReset(Loader<LinkedList<CityAirQualityIndex>> arg0) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onLoaderReset");
				}
			}
			
		});
		
		return;
	}
	
	@Override
	public void setCity(String city) {
		this.city = city;
	}
	
	
	

}