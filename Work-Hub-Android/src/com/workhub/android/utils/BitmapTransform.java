package com.workhub.android.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class BitmapTransform {
	

	public static Bitmap crop(Bitmap b, int x, int y, int width, int height) {
		
		if (b != null) {
			
			try {
				Bitmap b2 = Bitmap.createBitmap(b, x, y, width, height);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				
				Log.e(ConstantsAndroid.LOG_TAG, "We have no memory to crop. Return the original bitmap.", ex);
			}
		}
		return b;
	}
	
	public static Bitmap resize(Bitmap b, int width, int height) {
		
		if (b != null) {
			
			try {
				Bitmap b2 = Bitmap.createScaledBitmap(b, width, height, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				
				Log.e(ConstantsAndroid.LOG_TAG, "We have no memory to resize. Return the original bitmap.", ex);
			}
		}
		return b;
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
			
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				
				Log.e(ConstantsAndroid.LOG_TAG, "We have no memory to rotate. Return the original bitmap.", ex);
			}
		}
		return b;
	}

}
