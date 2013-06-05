package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
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
}
