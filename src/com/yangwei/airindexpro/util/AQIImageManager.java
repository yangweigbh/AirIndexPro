package com.yangwei.airindexpro.util;

import com.yangwei.airindexpro.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

public class AQIImageManager {
	private static AQIImageManager mInstance;
	private int mAQI;
	private Context mContext;

	public static AQIImageManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AQIImageManager(context);
		}
		return mInstance;
	}
	
	private AQIImageManager(Context context) {
		mContext = context;
	}
	
	public AQIImageManager atAQI(int aqi) {
		mAQI = aqi;
		return mInstance;
	}
	
	public int getCategoryText() {
		ensureValidAQI();
		int catStringID = 0;
		if (mAQI <= 50) {
			catStringID = R.string.good;
		} else if (mAQI > 50 && mAQI <= 100) {
			catStringID = R.string.moderate;
		} else if (mAQI > 101 && mAQI <= 150) {
			catStringID = R.string.unhealthyforsensitivegroups;
		} else if (mAQI > 151 && mAQI <= 200) {
			catStringID = R.string.unhealthy;
		} else if (mAQI > 201 && mAQI <= 300) {
			catStringID = R.string.veryunhealthy;
		} else if (mAQI > 301) {
			catStringID = R.string.hazardous;
		} 
		
		return catStringID;
	}
	
	public int getCategoryColor() {
		ensureValidAQI();
		int color = 0;
		if (mAQI <= 50) {
			color = Color.GREEN;
		} else if (mAQI > 50 && mAQI <= 100) {
			color = Color.YELLOW;
		} else if (mAQI > 101 && mAQI <= 150) {
			color = 0xFFFF7F27;
		} else if (mAQI > 151 && mAQI <= 200) {
			color = Color.RED;
		} else if (mAQI > 201 && mAQI <= 300) {
			color = 0xFF800000;
		} else if (mAQI > 301) {
			color = 0xFF400040;
		} 
		
		return color;
	}
	
	public int getBaoImage() {
		ensureValidAQI();
		int result = 0;
		if (mAQI <= 50) {
			result = R.drawable.baogood;
		} else if (mAQI > 50 && mAQI <= 100) {
			result = R.drawable.baomoderate;
		} else if (mAQI > 101 && mAQI <= 150) {
			result = R.drawable.baounhealthys;
		} else if (mAQI > 151 && mAQI <= 200) {
			result = R.drawable.baounhealthy;
		} else if (mAQI > 201 && mAQI <= 300) {
			result = R.drawable.baoveryunhealthy;
		} else if (mAQI > 301) {
			result = R.drawable.baohazard;
		}
		
		return result;
	}

	private void ensureValidAQI() {
		if (mAQI < 0 || mAQI > 500) {
			throw new IllegalArgumentException();
		}
		
	}
}
