package com.yangwei.airindexpro.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yangwei.airindexpro.R;
import com.yangwei.airindexpro.quadtree.QuadTree;
import com.yangwei.airindexpro.ui.PreferenceListFragment.OnPreferenceAttachedListener;
import com.yangwei.airindexpro.util.Constant;

public class MainActivity extends FragmentActivity implements OnPreferenceAttachedListener{

	private FragmentTabPager mFragmentTabPager;
	private SlidingMenu menu;
	public static final String TAG = "MainActivity";
	private List<String> validCity;
	private SharedPreferences preferences;
	private PreferenceListFragment sidebar_menu;
	private QuadTree tree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences
				.edit()
				.putStringSet(
						Constant.VALID_CITY_KEY,
						new HashSet<String>(Arrays
								.asList(Constant.valid_city_array))).commit();
		
		setValidCity(Arrays.asList(Constant.valid_city_array));
		
		setupView();
	}

	private void setupView() {
		ViewPager vp = (ViewPager) findViewById(R.id.pager);
		mFragmentTabPager = new FragmentTabPager(this, vp);
		
		//setup actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mFragmentTabPager.addTab(actionBar.newTab(), AirQuaIndexFragmentForLocation.class, new Bundle());
		Set<String> user_cities = preferences.getStringSet(Constant.USER_CITIES_KEY, null);
		if (user_cities != null && user_cities.size() != 0) {
			Iterator<String> iterator = user_cities.iterator();
			while (iterator.hasNext()) {
				String city = (String) iterator.next();
				Bundle b =  new Bundle();
				b.putString("city", city);
				mFragmentTabPager.addTab(actionBar.newTab(), AirQuaIndexFragmentForFixed.class, b);
			}
		}
		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindScrollScale(0);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeEnabled(false);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu_frame);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		sidebar_menu = PreferenceListFragment.newInstance(R.xml.sidebar_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, sidebar_menu).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public List<String> getValidCity() {
		return validCity;
	}

	public void setValidCity(List<String> validCity) {
		this.validCity = validCity;
	}
	
	public QuadTree getQuadTree() {
		if (tree == null) {
			tree = new QuadTree(20, 50, 80, 150);
			for (int i = 0; i < Constant.valid_city_cor_array.length; i++) {
				tree.set(Constant.valid_city_cor_array[i].getX(), Constant.valid_city_cor_array[i].getY(), Constant.valid_city_cor_array[i].getValue());
			}
		}
		return tree; 
	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		MultiSelectListPreference mslistpreference = (MultiSelectListPreference) root.findPreference("user_cities");
		Set<String> user_cities = preferences.getStringSet(Constant.USER_CITIES_KEY, null);
		if (user_cities != null && user_cities.size() != 0) {
			mslistpreference.setSummary(user_cities.toString());
		}
		mslistpreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Set<String> newValueSet = (Set<String>)newValue;
				if (newValueSet.size() == 0) {
					preference.setSummary(R.string.no_city_select);
					int tabCount = getActionBar().getTabCount();
					if (tabCount > 1) {
						for (int i = tabCount - 1; i > 0; i--) {
							mFragmentTabPager.removeTab(i);
						}
						mFragmentTabPager.setUpdateNeeded(true);
						mFragmentTabPager.notifyDataSetChanged();
					}
				} else {
					preference.setSummary(newValueSet.toString());
					int tabCount = getActionBar().getTabCount();
					if (tabCount > 1) {
						for (int i = tabCount - 1; i > 0; i--) {
							mFragmentTabPager.removeTab(i);
						}
					}
					Iterator<String> iterator = newValueSet.iterator();
					while (iterator.hasNext()) {
						String city = (String) iterator.next();
						Bundle b =  new Bundle();
						b.putString("city", city);
						mFragmentTabPager.addTab(getActionBar().newTab(), AirQuaIndexFragmentForFixed.class, b);
					}
					mFragmentTabPager.setUpdateNeeded(true);
					mFragmentTabPager.notifyDataSetChanged();
				}
				return true;
			}
		});
	}

}
