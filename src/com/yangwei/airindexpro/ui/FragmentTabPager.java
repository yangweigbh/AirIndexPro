package com.yangwei.airindexpro.ui;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

class FragmentTabPager extends FragmentStatePagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;

    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private HashMap<String, Fragment> framents = new HashMap<String, Fragment>();
    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }
    
    private boolean updateNeeded = false;

    FragmentTabPager(FragmentActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mActionBar = activity.getActionBar();

        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        tab.setTabListener(this);
        tab.setText(args.getString("city"));
        mActionBar.addTab(tab);
        mTabs.add(info);
    }
    
    void removeTab(int position) {
    	mActionBar.removeTabAt(position);
    	framents.remove(mTabs.get(position).args.getString("city"));
    	mTabs.remove(position);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        info.args.putInt("tab_position", position);
        if (framents.get(info.args.getString("city")) == null) {
        	framents.put(info.args.getString("city"), Fragment.instantiate(mContext, info.clss.getName(), info.args));
		} 
        
        return framents.get(info.args.getString("city"));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // NO-OP
    }

    @Override
    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // NO-OP
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // NO-OP
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // NO-OP
    }
    
    @Override
    public int getItemPosition(Object object) {
    	if (updateNeeded) {
    		updateNeeded = false;
    		return PagerAdapter.POSITION_NONE;
		} else {
			return PagerAdapter.POSITION_UNCHANGED;
		}
    	
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	super.destroyItem(container, position, object);
    }

	public void setUpdateNeeded(boolean updateNeeded) {
		this.updateNeeded = updateNeeded;
	}
}