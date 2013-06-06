package com.workhub.android.utils;

import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseCubicInOut;
import org.andengine.util.modifier.ease.EaseExponentialOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

public class ConstantsAndroid {

	//public final static int SCREEN_WIDTH  = 624;
	//public final static int SCREEN_HEIGHT = 1040;
	public static int ZINDEX = 0;
//	public static final String EXT_PATH_TEMP = Environment.getExternalStorageDirectory() + "/Android/data/com.piviandco.fatbooth/temp/";
//	public static final String EXT_PATH_FILES = Environment.getExternalStorageDirectory() + "/Android/data/com.piviandco.fatbooth/files/";
//	public static final String EXT_PATH_THUMBS = Environment.getExternalStorageDirectory() + "/Android/data/com.piviandco.fatbooth/thumbs/";
//	public static final String EXT_PATH_SAVED = Environment.getExternalStorageDirectory() + "/FatBooth/";
	
	public static final String EXT_PATH_FILES = Environment.getExternalStorageDirectory() + "/Android/data/com.workhub.android/export/";
	
	
	public static final String LOG_TAG = "WorkHub";
	public static final float ANIMATION_DURATION = 0.25f;
	public static final IEaseFunction[] EASEFUNCTIONS = new IEaseFunction[] {
        EaseExponentialOut.getInstance(),
        EaseBackOut.getInstance(),
        EaseCubicInOut.getInstance()
        };
	
	
	

}
