package com.workhub.android.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsManager {
	
		private static Context context;
		
		private static final String HOST = "HOST";
		private static final String NICKNAME = "NICKNAME";
		
		private static SettingsManager _instance;
		private SharedPreferences settings;

		
		
		private SettingsManager() {
			
		}

		public static SettingsManager getInstance(Context context) {
			SettingsManager.context = context;
			if(_instance==null) {
				_instance = new SettingsManager();
			}
			return _instance;
		}
		
		private SharedPreferences getSettings() {
			if(this.settings == null) {
				this.settings = context.getSharedPreferences(ConstantsAndroid.SettingsFileName, Context.MODE_PRIVATE);
			}
			return this.settings;
		}

		///////////////////////////////////////////
		// SAVER HELPER
		///////////////////////////////////////////
		
		private void saveString(String key, String value) {
			SharedPreferences.Editor editor = getSettings().edit();
			editor.putString(key, value);
			editor.commit();
		}

		private void saveInt(String key, int value) {
			SharedPreferences.Editor editor = getSettings().edit();
			editor.putInt(key, value);
			editor.commit();
		}
		
		private void saveLong(String key, long value) {
			SharedPreferences.Editor editor = getSettings().edit();
			editor.putLong(key, value);
			editor.commit();
		}
		
		private void saveBool(String key, boolean value) {
			SharedPreferences.Editor editor = getSettings().edit();
			editor.putBoolean(key, value);
			editor.commit();
		}
		
		///////////////////////////////////////////
		// GETTER SETTER
		///////////////////////////////////////////
	
		

		public String getHost() {
			return getSettings().getString(HOST, null);
		}
		
		public void setHost(String host) {
			saveString(HOST, host);
		}
		
		public String getNickname() {
			return getSettings().getString(NICKNAME, null);
		}
		
		public void setNickname(String nickname) {
			saveString(NICKNAME, nickname);
		}
}
