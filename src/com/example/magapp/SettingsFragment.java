package com.example.magapp;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment implements  OnPreferenceChangeListener   {

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.fragment_settings);
	    
	    
	    CheckBoxPreference notification_pref = (CheckBoxPreference) findPreference("notification");

	    notification_pref.setOnPreferenceChangeListener(this);
	    if (IsServiceActive()) {
	    	 notification_pref.setChecked(true);
	    }else {
	    	 notification_pref.setChecked(false);
	    }
	    
	  }
	  
	  public boolean onPreferenceChange(Preference preference, Object newValue) {
		 
          if (newValue.equals(true)){
        	  getActivity().startService(new Intent(getActivity(), NewOrderService.class));
          }else {        	 
        	 getActivity().stopService(new Intent(getActivity(), NewOrderService.class));
          }
          return true;
      }
	  
	  public boolean IsServiceActive(){
		    ActivityManager manager = (ActivityManager)getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {		    	 
		      if ("com.example.magapp.NewOrderService".equals(service.service.getClassName())) {
		    	  return true;
		    	 
		      }
		    }
		    return false;
	  }
	}