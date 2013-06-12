package com.workhub.mt4j;

import java.util.ArrayList;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

// Modifie la position d'un élément pour qu'il apparaisse en entier à l'écran
public class MT4JUtils {
	public static void fixPosition(AbstractShape shape, int x, int y, PApplet applet, PositionAnchor positionAnchor) {
		int fixedX = x;
		int fixedY = y;
		int width = (int)shape.getWidthXY(TransformSpace.GLOBAL);
		int height = (int)shape.getHeightXY(TransformSpace.GLOBAL);
		switch(positionAnchor) {
		case UPPER_LEFT :
			if(fixedX + width > applet.width) {
				fixedX = applet.width - width;
			}
			if(fixedY + height > applet.height) {
				fixedY = applet.height - height;
			}
			break;
		case CENTER :
			if(fixedX < width / 2) {
				fixedX = width / 2;
			}
			else if(fixedX + width / 2 > applet.width) {
				fixedX = applet.width - width / 2;
			}
			if(fixedY < height / 2) {
				fixedY = height / 2;
			}
			else if(fixedY + height / 2 > applet.height) {
				fixedY = applet.height - height / 2;
			}
			break;
		case LOWER_LEFT :	
		case LOWER_RIGHT :
			// Non implémenté
		default :
			break;
		}
		shape.setPositionGlobal(new Vector3D(fixedX, fixedY));
	}

	public static <T> T removeBeginning(ArrayList<T> list) {
		if(list.isEmpty()) {
			return null;
		}
		T location = list.get(0);
		for(int i = 1 ; i < list.size() ; i++) {
			list.set(i - 1, list.get(i));
		}
		list.remove(list.size() - 1);
		return location;
	}

	public static MTColor intToMTColor(int argb) {
		int a = (argb & 0xFF000000) >>> 24;
		int r = (argb & 0x00FF0000) >>> 16;
		int g = (argb & 0x0000FF00) >>> 8;
		int b = (argb & 0x000000FF);
		return new MTColor(r, g, b, a);
	}

	public static int MTColorToInt(MTColor color) {
		int a = (int)color.getAlpha();
		int r = (int)color.getR();
		int g = (int)color.getG();
		int b = (int)color.getB();
		return (a << 24) + (r << 16) + (g << 8) + b;
	}

	public static int[] byteArrayToIntArray(byte[] array) {
		int size = array.length / 4;
		int[] buf = new int[size];
		for(int i = 0 ; i < size ; i++) {
			buf[i] = array[4 * i];
			for(int j = 1 ; j <= 3 ; j++) {
				buf[i] <<= 8;
				buf[i] += (array[4 * i + j] & 0x000000FF);
			}
		}
		return buf;
	}

	public static byte[] intArrayToByteArray(int[] array) {
		int size = array.length * 4;
		byte[] buf = new byte[size];
		for(int i = 0 ; i < array.length ; i++) {
			int intValue = array[i];
			buf[4 * i + 3] = (byte)(intValue & 0x000000FF);
			for(int j = 2 ; j >= 0 ; j--) {
				intValue >>>= 8;
				buf[4 * i + j] = (byte)(intValue & 0x000000FF);
			}
		}
		return buf;
	}
}
