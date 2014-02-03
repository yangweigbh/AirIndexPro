package com.yangwei.airindexpro.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yangwei.airindexpro.BuildConfig;
import com.yangwei.airindexpro.R;
import com.yangwei.airindexpro.quadtree.Point;
import com.yangwei.airindexpro.util.Address;
import com.yangwei.airindexpro.util.LocationToAddressConverter;

public class AirQuaIndexFragmentForLocation extends AirQuaIndexFragment {
	
	String gpsType = LocationManager.GPS_PROVIDER;

	private void makeUseOfNewLocation(final Location location) {
		if (location != null) {
			LocationToAddressConverter.LocationToAddress(getActivity(), location, new LocationToAddressConverter.CallBack() {
				
				@Override
				public void finished(Address address) {
					if (((MainActivity)getActivity()).getValidCity() != null) {
						if (((MainActivity)getActivity()).getValidCity().contains(address.getCity())) {
							dataSource.setCity(address.getCity());
							mPullToRefreshLayout.setRefreshing(true);
							dataSource.getData(mDataReadyListener);
						} else {
							Point nearestPoint = ((MainActivity)getActivity()).getQuadTree().findNearestPoint(location.getLatitude(), location.getLongitude());
							dataSource.setCity((String)nearestPoint.getValue());
							mPullToRefreshLayout.setRefreshing(true);
							dataSource.getData(mDataReadyListener);
						}
					} else {
						Toast.makeText(getActivity(), "valid city null", Toast.LENGTH_LONG).show();
					}
					
				}
			});
		} else {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "location can't be null");
			}
		}
		
	}

	@Override
	void getData() {
		getActivity().getActionBar().getTabAt(tab_position).setText(getResources().getString(R.string.city_get_by_gps));
		mCategoryView.setVisibility(View.INVISIBLE);
		mEmotionView.setVisibility(View.INVISIBLE);
		final LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				makeUseOfNewLocation(location);
				locationManager.removeUpdates(this);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		locationManager.requestLocationUpdates(gpsType, 0,
				0, locationListener);
		
	}
	
	@Override
	void inActivityCreated(Bundle savedInstanceState) {
		getData();
	}

}
