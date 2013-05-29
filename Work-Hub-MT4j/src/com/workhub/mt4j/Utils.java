package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

// Modifie la position d'un élément pour qu'il apparaisse en entier à l'écran
public class Utils {
	public static void fixPosition(AbstractShape shape, int x, int y, PApplet applet, PositionAnchor positionAnchor) {
		int fixedX = x;
		int fixedY = y;
		switch(positionAnchor) {
		case UPPER_LEFT :
			if(fixedX + shape.getWidthXY(TransformSpace.GLOBAL) > applet.width) {
				fixedX = (int)(applet.width - shape.getWidthXY(TransformSpace.GLOBAL));
			}
			if(fixedY + shape.getHeightXY(TransformSpace.GLOBAL) > applet.height) {
				fixedY = (int)(applet.height - shape.getHeightXY(TransformSpace.GLOBAL));
			}
			break;
		case CENTER :
			if(fixedX + shape.getWidthXY(TransformSpace.GLOBAL) / 2 > applet.width) {
				fixedX = (int)(applet.width - shape.getWidthXY(TransformSpace.GLOBAL) / 2);
			}
			if(fixedY + shape.getHeightXY(TransformSpace.GLOBAL) / 2 > applet.height) {
				fixedY = (int)(applet.height - shape.getHeightXY(TransformSpace.GLOBAL) / 2);
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
