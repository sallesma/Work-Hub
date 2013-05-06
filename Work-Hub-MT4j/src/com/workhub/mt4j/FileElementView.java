package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class FileElementView extends AbstractElementView {

	private MTImage content;
/*
 * TODO : Mettre le bon icone
 */
	public FileElementView(float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		content = new MTImage(applet.loadImage("Image/iconeDefault.jpg"), applet);
		content.setName("Default Picture");
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setPositionRelativeToParent(new Vector3D(300, 260));
		addChild(content);
	}

	public MTImage getContent() {
		return content;
	}
	
	public void setContent(MTImage content) {
		this.content = content;
	}
}