package com.yangwei.airindexpro.util;


import com.yangwei.airindexpro.BuildConfig;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class LocationToAddressConverter {
	public static final String TAG = "LocationToAddressConverter";
	
	public static void LocationToAddress(final Context context, final Location location, final CallBack callback) {
		((Activity)context).getLoaderManager().initLoader(Constant.coordinate_reverse_loader_id, null, new LoaderCallbacks<Address>() {

			@Override
			public Loader<Address> onCreateLoader(int arg0,
					Bundle arg1) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onCreateLoader");
				}
				return new CoordinateReverseLoader(context, location);
			}

			@Override
			public void onLoadFinished(Loader<Address> arg0,
					Address arg1) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onLoadFinished");
				}
				((Activity)context).getLoaderManager().destroyLoader(Constant.coordinate_reverse_loader_id);
				callback.finished(arg1);
				
			}

			@Override
			public void onLoaderReset(Loader<Address> arg0) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onLoaderReset");
				}
			}
			
		});
		
		return;
	}
	
	public static interface CallBack{
		public void finished(Address address);
	}
	
	
}
