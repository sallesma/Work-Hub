package com.workhub.mt4j;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import com.workhub.model.FileElementModel;
import com.workhub.utils.Constants;

import processing.core.PApplet;
import processing.core.PImage;

public class FileElementView extends AbstractElementView {

	private MTImage content;
	private String filePath;
	
	public FileElementView(String filePath, float x, float y, float width,
			float height, PApplet applet, WorkHubScene scene) {
		super(x, y, MT4JConstants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet, scene);
		this.filePath = new String(filePath);
		updateTitleWithElementPath(filePath);
		Vector3D position = new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+height*0.2f));
		PImage image = applet.loadImage(chooseIcon());
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
		Vector3D position = new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+height*0.2f));
		filePath = mtApplication.selectInput();
		updateTitleWithElementPath(filePath);
		PImage image = mtApplication.loadImage(chooseIcon());
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

	@Override
	public void saveContent() {
		((FileElementModel)getModel()).setContent(null); // TODO
	}

	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_FILE;
	}
	
	@Override
	public void updateContent() {
		// TODO
	}
}