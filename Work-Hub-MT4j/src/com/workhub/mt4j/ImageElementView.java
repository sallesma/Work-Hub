package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageElementView extends AbstractElementView {

	private MTImage content;
	
	public ImageElementView(String imagePath, float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		updateTitleWithElementPath(imagePath);
		PImage image = applet.loadImage(imagePath);
		if ( image.height < image.width )
			image.resize((int) (width-10), 0);
		else image.resize(0, (int) (height-40));
		content = new MTImage(image, applet);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX(), (float) (this.getPosition(TransformSpace.GLOBAL).getY()+40)));
		addChild(content);
	}

	public void editElementContent(){
		String imagePath = mtApplication.selectInput();
		PImage image = mtApplication.loadImage(imagePath);
		if ( image.height < image.width )
			image.resize((int) (this.getWidthXYGlobal()-10), 0);
		else image.resize(0, (int) (this.getHeightXYGlobal()-40));
		content.removeFromParent();
		content = new MTImage(image, mtApplication);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+40)));
		addChild(content);
	}

	public MTImage getContent() {
		return content;
	}
	
	public void setContent(MTImage content) {
		this.content = content;
	}
}
