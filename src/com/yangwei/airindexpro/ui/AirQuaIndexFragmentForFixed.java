package com.yangwei.airindexpro.ui;

import com.yangwei.airindexpro.BuildConfig;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AirQuaIndexFragmentForFixed extends AirQuaIndexFragment {

	@Override
	void getData() {
		Bundle arg = getArguments();
		if(arg != null) {
			dataSource.setCity(arg.getString("city"));
			if (BuildConfig.DEBUG) {
				Log.d(TAG, ">>>>>>>>tab_position: " + tab_position + " for: " + arg.getString("city"));
			}
		}
		dataSource.getData(mDataReadyListener);	
		Toast.makeText(getActivity(), "refresh has started", Toast.LENGTH_LONG).show();
		mCategoryView.setVisibility(View.INVISIBLE);
		mEmotionView.setVisibility(View.INVISIBLE);
	}

	@Override
	void inActivityCreated(Bundle savedInstanceState) {
		getData();
//		Bundle arg = getArguments();
//		if (arg != null) {
//			dataSource.setCity(arg.getString("city"));
//			if (BuildConfig.DEBUG) {
//				Log.d(TAG, ">>>>>>>>tab_position: " + tab_position
//						+ " for: " + arg.getString("city"));
//			}
//			Toast.makeText(getActivity(), "refresh has started",
//					Toast.LENGTH_LONG).show();
//			//mPullToRefreshLayout.setRefreshing(true);
//			dataSource.getData(mDataReadyListener);
//		}	
		
	}

}
