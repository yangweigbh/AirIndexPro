package com.yangwei.airindexpro.ui;

import com.yangwei.airindexpro.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SidebarMenuFragment extends PreferenceFragment {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.sidebar_menu);
    }
}
