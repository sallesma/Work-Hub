package com.workhub.mt4j;

import org.mt4j.components.clusters.Cluster;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class ElementGroupView extends Cluster {

	public ElementGroupView(PApplet applet, MTPolygon selectionPolygon) {
		super(applet, selectionPolygon);
	}
	protected void openContextualMenu(Vector3D locationOnScreen) {
		ContextMenu contextMenu = new ContextMenu(this, (int)locationOnScreen.x, (int)locationOnScreen.y, getRenderer(), Constants.CONTEXT_GROUP_MENU);
		this.getParent().addChild(contextMenu);
	}
}
