package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class FileElementView extends AbstractElementView {

	private MTImage content;
	private String filePath;
	
	public FileElementView(String filePath, float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		this.filePath = new String(filePath);
		updateTitleWithElementPath(filePath);
		PImage image = applet.loadImage(chooseIcon());
		if ( image.height < image.width )
			image.resize((int) (width-10), 0);
		else image.resize(0, (int) (height-45));
		content = new MTImage(image, applet);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX(), (float) (this.getPosition(TransformSpace.GLOBAL).getY()+40)));
		addChild(content);
	}
	
	private String chooseIcon() {
		if (filePath.toLowerCase().endsWith(".doc") || filePath.toLowerCase().endsWith(".docx")
				|| filePath.toLowerCase().endsWith(".odt"))
			return "Image/iconeDocx.png";
		else if (filePath.toLowerCase().endsWith(".jpg") || filePath.toLowerCase().endsWith(".jpeg")
				|| filePath.toLowerCase().endsWith(".png") || filePath.toLowerCase().endsWith(".bmp"))
			return "Image/iconeImage.jpg";
		else if (filePath.toLowerCase().endsWith(".pdf"))
			return "Image/iconePDF.png";
		else
		return "Image/iconeDefault.png";
	}

	public void editElementContent() {
		float width = this.getWidthXY(TransformSpace.LOCAL);
		float height = this.getHeightXY(TransformSpace.LOCAL);
		filePath = mtApplication.selectInput();
		updateTitleWithElementPath(filePath);
		PImage image = mtApplication.loadImage(chooseIcon());
		if ( image.height < image.width )
			image.resize((int) (width - 10), 0);
		else image.resize(0, (int) (height - 40));
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