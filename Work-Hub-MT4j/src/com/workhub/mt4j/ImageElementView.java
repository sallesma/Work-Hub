package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageElementView extends AbstractElementView {

	private MTImage content;
	
	public ImageElementView(float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		PImage image = applet.loadImage("Image/defaultImage.jpg");
		image.resize(190, 0);
		content = new MTImage(image, applet);
		content.setName("Default Picture");
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX(), (float) (this.getPosition(TransformSpace.GLOBAL).getY()+40)));
		addChild(content);
	}

	public MTImage getContent() {
		return content;
	}
	
	public void setContent(MTImage content) {
		this.content = content;
	}
}
