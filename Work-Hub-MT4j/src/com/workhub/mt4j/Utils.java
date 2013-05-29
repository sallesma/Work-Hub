package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

// Modifie la position d'un élément pour qu'il apparaisse en entier à l'écran
public class Utils {
	public static void fixPosition(AbstractShape shape, int x, int y, PApplet applet) {
		int fixedX = x;
		int fixedY = y;
		if(fixedX + Constants.CONTEXT_BUTTON_WIDTH > applet.width) {
			fixedX = applet.width - Constants.CONTEXT_BUTTON_WIDTH;
		}
		if(fixedY + shape.getHeightXY(TransformSpace.GLOBAL) > applet.height) {
			fixedY = (int)(applet.height - shape.getHeightXY(TransformSpace.GLOBAL));
		}
		shape.setPositionGlobal(new Vector3D(fixedX, fixedY));
	}
}
