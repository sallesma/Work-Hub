package com.workhub.android.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;



public class FileManager {

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} 
		return false;
	}

	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		} 
		return false;
	}
	
	
	
	public static Bitmap readBitmapFile(String path) throws Exception {
		return readBitmapFile(path, 0);
	}

	
	public static Bitmap readBitmapFile(String path, int sampleSize) throws Exception {
		
		if (isExternalStorageReadable()) {

			Bitmap bMap = null;
			File file = new File(path);

			try {
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream buf = new BufferedInputStream(fis, 1024);
				BitmapFactory.Options options = new BitmapFactory.Options();
				
				options.inSampleSize = sampleSize;
				

				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				
				bMap = BitmapFactory.decodeStream(buf, null, options);
				
				fis.close();
				buf.close();
				

				//Rotate picture
				ExifInterface exif = new ExifInterface(path);
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				switch(orientation){
				case 3: 
					bMap = BitmapTransform.rotate(bMap, 180);
					break;
				case 6:
					bMap = BitmapTransform.rotate(bMap, 90); 
					break;
				case 8:
					bMap = BitmapTransform.rotate(bMap, -90);
					break;
				}
				
				
				return bMap;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		throw new Exception();

	}

	
	public static Size getBitmapSize(String path) throws Exception {
		
		if (isExternalStorageReadable()) {
			try{
				File file = new File(path);
				FileInputStream fis = new FileInputStream(file);

				//Decode image size
			    BitmapFactory.Options o = new BitmapFactory.Options();
			    o.inJustDecodeBounds = true;
			    BitmapFactory.decodeStream(fis,null,o);

			    //Find the correct scale value. It should be the power of 2.
			    int imgWidth = o.outWidth;
			    int imgHeight = o.outHeight;

				fis.close();
				
	            return new Size(imgWidth, imgHeight);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			

			

		}
		
		throw new Exception("ExternalStorageReaderException");
		
	}
	
	public static boolean shouldSampleBitmapSize(String path, int screenWidth, int screenHeight) throws Exception {
		
		Size size = getBitmapSize(path);
		
		if(size.width > 2*screenWidth) {
			return true;
		}
		
		return false;
	}
	
	
	public static Bitmap loadBitmap(Drawable sprite, Config bitmapConfig) {
		int width = sprite.getIntrinsicWidth();
		int height = sprite.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);
		Canvas canvas = new Canvas(bitmap);
		sprite.setBounds(0, 0, width, height);
		sprite.draw(canvas);
		return bitmap;
	}
	
	public static Bitmap loadBitmap(Drawable sprite) {
		return loadBitmap(sprite, Bitmap.Config.RGB_565);
	}
	
	public static boolean fileExist(String fileName) {

	    if(isExternalStorageReadable()) {
			File file = new File(fileName);
			return file.exists();
	    }
	    
	    return false;
	}

	public static void addAntiScanFile(String folder) {

		try {

			File nomediaFile = new File(folder, ".nomedia");
			FileWriter writer = new FileWriter(nomediaFile);
			writer.flush();
			writer.close();

		} catch (IOException e) {

		}
	}
	
	public static boolean deleteFile(String filename) {
		
		File file = new File(filename);
		return file.delete();
	}

	public static void copy(String name, String from, String to) {
		copy( name,name,   from,  to) ;
	}
	public static void copy(String name,String toName,  String from, String to) {

		try {
			File fromfile = new File(from, name);
			FileInputStream in = new FileInputStream(fromfile);

			File tofile = new File(to, toName);
			FileOutputStream out = new FileOutputStream(tofile);

			byte[] b = new byte[2048];
			int read;
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
			}
			
		} catch (IOException e) {

		}
	}
}
