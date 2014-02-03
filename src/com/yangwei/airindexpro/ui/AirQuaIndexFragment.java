package com.yangwei.airindexpro.ui;

import java.util.LinkedList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.yangwei.airindexpro.BuildConfig;
import com.yangwei.airindexpro.R;
import com.yangwei.airindexpro.datasource.CityAirQualityIndex;
import com.yangwei.airindexpro.datasource.DataReadyListener;
import com.yangwei.airindexpro.datasource.IDataSource;
import com.yangwei.airindexpro.datasource.LocalDataSource;
import com.yangwei.airindexpro.datasource.RemoteDataSource;
import com.yangwei.airindexpro.util.AQIImageManager;

public abstract class AirQuaIndexFragment extends Fragment implements ViewFactory, OnRefreshListener{
	protected static final String TAG = "AirQuaIndexFragment";
	TextSwitcher mSwitcher;
	Context mContext;
	protected PullToRefreshLayout mPullToRefreshLayout;
	IDataSource<CityAirQualityIndex> dataSource;
	protected TextView mCityName;
	protected ImageView mEmotionView;
	protected TextView mCategoryView;
	protected int tab_position;
	final Handler handler = new Handler();
	CityAirQualityIndex averageAQI;
	
	protected DataReadyListener<LinkedList<CityAirQualityIndex>> mDataReadyListener = new DataReadyListener<LinkedList<CityAirQualityIndex>>() {
		@Override
		public void dataReady(LinkedList<CityAirQualityIndex> data) {
			if (data == null) {
				mSwitcher.setText(getResources().getText(R.string.nonetwork));
				mPullToRefreshLayout.setRefreshComplete();
				return;
			}
			if (data.size() == 0) {
				mSwitcher.setText(getResources().getText(R.string.networkerror));
				mPullToRefreshLayout.setRefreshComplete();
				return;
			}
			averageAQI = data.peekLast();
			final int limit = averageAQI.getAqi();
			updateUI(limit, averageAQI.getArea());
			
			mPullToRefreshLayout.setRefreshComplete();
		}
	};
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, ">>>>>>>>onCreate, position:" + tab_position);
		}
		dataSource = new RemoteDataSource(getActivity());
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, ">>>>>>>>onCreateView, position:" + tab_position);
		}
		Bundle arg = getArguments();
        if(arg != null)
        	tab_position = arg.getInt("tab_position");
		View root = inflater.inflate(R.layout.aqi_fragment, container, false);
		mCityName = (TextView) root.findViewById(R.id.city);
		mEmotionView = (ImageView) root.findViewById(R.id.emotion);
		mCategoryView = (TextView) root.findViewById(R.id.catagory);
		
		mSwitcher = (TextSwitcher) root.findViewById(R.id.aqi);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
		mPullToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.ptr_layout);

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // Here we mark just the ListView and it's Empty View as pullable
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);
        
        
		return root;
	}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
			Log.d(TAG, ">>>>>>>>onActivityCreated, position:" + tab_position + " bundle: " + savedInstanceState);
		}
        inActivityCreated(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public View makeView() {
		TextView view = new TextView(getActivity());
		view.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
		view.setTextSize(getResources().getDimension(R.dimen.aqi_textsize));
		return view;
	}

	@Override
	public void onRefreshStarted(View view) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, ">>>>>onRefreshStarted");
		}
		getData();
	}
	
	abstract void inActivityCreated(Bundle savedInstanceState);
	abstract void getData();

	protected void updateUI(final int limit, String city) {
		mCityName.setText(city);
		
		Thread t = new Thread(){
			
			public void run() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						mSwitcher.setText(String.valueOf(limit));
					}
				});
					
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						mCategoryView.setBackgroundColor(AQIImageManager.getInstance(getActivity()).atAQI(limit).getCategoryColor());
						mCategoryView.setText(AQIImageManager.getInstance(getActivity()).atAQI(limit).getCategoryText());
						Animation animation = new AlphaAnimation(0, 1);
						animation.setDuration(1000);
						mCategoryView.startAnimation(animation);
						mCategoryView.setVisibility(View.VISIBLE);
					}
				});
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Animation animation = new AlphaAnimation(0, 1);
						animation.setDuration(1000);
						mEmotionView.setImageResource(AQIImageManager.getInstance(getActivity()).atAQI(limit).getBaoImage());
						mEmotionView.startAnimation(animation);
						mEmotionView.setVisibility(View.VISIBLE);
					}
				});
				
			}
		};
		t.start();
	}
}
