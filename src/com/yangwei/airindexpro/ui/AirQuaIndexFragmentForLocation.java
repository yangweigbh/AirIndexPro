package com.yangwei.airindexpro.ui;

import android.R.integer;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.yangwei.airindexpro.R;
import com.yangwei.airindexpro.util.Address;
import com.yangwei.airindexpro.util.LocationToAddressConverter;

public class AirQuaIndexFragmentForLocation extends AirQuaIndexFragment {
	
	String gpsType = LocationManager.NETWORK_PROVIDER;

	void getDataForTheFirstTime() {
		getActivity().getActionBar().getTabAt(tab_position).setText(getResources().getString(R.string.city_get_by_gps));
		// dataSource.getData(mDataReadyListener);
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network
				// location provider.
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

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(gpsType, 0,
				0, locationListener);

		Location lastKnownLocation = locationManager
				.getLastKnownLocation(gpsType);
		// System.out.println( "last known location get: " +
		// lastKnownLocation.getLatitude() + " : " +
		// lastKnownLocation.getLongitude());

		// getActivity().getLoaderManager().initLoader(Constant.valid_city_loader_id,
		// null, new LoaderCallbacks<ArrayList<String>>() {
		//
		// @Override
		// public Loader<ArrayList<String>> onCreateLoader(int id,
		// Bundle args) {
		// return new ValidCityLoader(getActivity());
		// }
		//
		// @Override
		// public void onLoadFinished(Loader<ArrayList<String>> arg0,
		// ArrayList<String> arg1) {
		// if(arg1.size() == 0) {
		// Toast.makeText(getActivity(), "Valid City fetch failed",
		// Toast.LENGTH_LONG).show();
		// } else {
		// ((MainActivity)getActivity()).setValidCity(arg1);
		// }
		//
		// }
		//
		// @Override
		// public void onLoaderReset(Loader<ArrayList<String>> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });

	}
	
	private void makeUseOfNewLocation(Location location) {
		if (location != null) {
			Toast.makeText(getActivity(), "location get: " + location.getLatitude() + " : " + location.getLongitude(), Toast.LENGTH_LONG).show();
			LocationToAddressConverter.LocationToAddress(getActivity(), location, new LocationToAddressConverter.CallBack() {
				
				@Override
				public void finished(Address address) {
					System.out.println("city: " + address.getCity());
					if (((MainActivity)getActivity()).getValidCity() != null) {
						if (((MainActivity)getActivity()).getValidCity().contains(address.getCity())) {
							dataSource.setCity(address.getCity());
							mPullToRefreshLayout.setRefreshing(true);
							dataSource.getData(mDataReadyListener);
						} else {
							Toast.makeText(getActivity(), getResources().getText(R.string.location_not_support), Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getActivity(), "valid city null", Toast.LENGTH_LONG).show();
					}
					
				}
			});
		} else {
			System.out.println(">>>>>>>>>null!!!!!!!");
		}
		
	}

	@Override
	void getData() {
		getActivity().getActionBar().getTabAt(tab_position).setText(getResources().getString(R.string.city_get_by_gps));
		// dataSource.getData(mDataReadyListener);
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network
				// location provider.
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

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(gpsType, 0,
				0, locationListener);
		
	}
	
	@Override
	void inActivityCreated() {
		getDataForTheFirstTime();
	}

}
