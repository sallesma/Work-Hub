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
		Vector3D position = new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+height*0.2f));
		PImage image = applet.loadImage(imagePath);
		content = new MTImage(image, applet);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(position);
		float scale = image.height < 0.2 * image.width ? width / image.width : height * 0.8f / image.height;
		content.scale(scale, scale, 0f, position);
		addChild(content);
	}

	public void editElementContent(){
		float width = this.getWidthXY(TransformSpace.LOCAL);
		float height = this.getHeightXY(TransformSpace.LOCAL);
		Vector3D position = new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+height*0.2f));
		String imagePath = mtApplication.selectInput();
		updateTitleWithElementPath(imagePath);
		PImage image = mtApplication.loadImage(imagePath);
		content.removeFromParent();
		content = new MTImage(image, mtApplication);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(position);
		float scale = image.height < image.width ? width / image.width : height * 0.8f / image.height;
		content.scale(scale, scale, 0f, position);
		addChild(content);
	}

	public MTImage getContent() {
		return content;
	}
	
	public void setContent(MTImage content) {
		this.content = content;
	}
}
