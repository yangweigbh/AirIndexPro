/*
 * Copyright 2013 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yangwei.airindexpro.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

class FragmentTabPager extends FragmentPagerAdapter
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
        notifyDataSetChanged();
    }
    
    void removeTab(int position) {
    	mActionBar.removeTabAt(position);
    	framents.remove(mTabs.get(position).args.getString("city"));
    	mTabs.remove(position);
    	notifyDataSetChanged();
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
    	System.out.println(">>>>>>>>tab selected: " + tab.getPosition());
        //Object tag = tab.getTag();
        mViewPager.setCurrentItem(tab.getPosition());
//        for (int i = 0; i < mTabs.size(); i++) {
//            if (mTabs.get(i) == tag) {
//                mViewPager.setCurrentItem(i);
//            }
//        }
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}