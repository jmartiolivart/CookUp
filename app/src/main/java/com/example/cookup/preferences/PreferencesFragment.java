package com.example.cookup.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.cookup.R;


public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_editprofile);
    }
}