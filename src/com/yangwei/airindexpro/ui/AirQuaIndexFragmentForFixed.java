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
		if (BuildConfig.DEBUG) {
			Log.d(TAG, ">>>>>>>>getData: " + tab_position + " for: " + arg.getString("city"));
		}
		if(arg != null) {
			dataSource.setCity(arg.getString("city"));
		}
		dataSource.getData(mDataReadyListener);	
		mCategoryView.setVisibility(View.INVISIBLE);
		mEmotionView.setVisibility(View.INVISIBLE);
	}

	@Override
	void inActivityCreated(Bundle savedInstanceState) {
		getData();
	}

}
