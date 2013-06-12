package com.workhub.mt4j;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

import com.sun.opengl.impl.mipmap.Image;
import com.workhub.model.PictureElementModel;
import com.workhub.utils.Constants;

public class ImageElementView extends AbstractElementView {

	private MTImage content;
	private PImage image;
	
	public ImageElementView(float x, float y, float width,
			float height, PApplet applet, WorkHubScene scene) {
		super(x, y, MT4JConstants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet, scene);
		Vector3D position = new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+height*0.2f));
		PImage image = applet.loadImage("Image/defaultImage.jpg");
		this.image = image;
		if(image.width > 500) {
			image.resize(500, 0);
		}
		if(image.height > 500) {
			image.resize(0, 500);
		}
		content = new MTImage(image, applet);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(position);
		float scale = image.height * 0.8 < image.width ? width / image.width : height * 0.8f / image.height;
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
		this.image = image;
		if(image.width > 500) {
			image.resize(500, 0);
		}
		if(image.height > 500) {
			image.resize(0, 500);
		}
		content.removeFromParent();
		content.destroy();
		content = new MTImage(image, mtApplication);
		content.setNoFill(true);
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(position);
		float scale = image.height * 0.8 < image.width ? width / image.width : height * 0.8f / image.height;
		content.scale(scale, scale, 0f, position);
		addChild(content);
		saveModel();
		JadeInterface.getInstance().finishEdition(model.getAgent());
	}

	public MTImage getContent() {
		return content;
	}
	
	public void setContent(MTImage content) {
		this.content = content;
	}

	@Override
	public void saveContent() {
		((PictureElementModel)getModel()).setContent(PNGEncoder.toPNG(image.width, image.height, image.pixels, true));
	}

	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_PICTURE;
	}
	
	@Override
	public void updateContent() {
		PictureElementModel pictureModel = (PictureElementModel)model;
		if(pictureModel.getContent() != null) {
			float width = this.getWidthXY(TransformSpace.LOCAL);
			float height = this.getHeightXY(TransformSpace.LOCAL);
			Vector3D position = new Vector3D(this.getPosition(TransformSpace.LOCAL).getX(), (float) (this.getPosition(TransformSpace.LOCAL).getY()+height*0.2f));
			
			
			BufferedImage bufferedImage = null;
			try {
				bufferedImage = ImageIO.read(new ByteArrayInputStream(pictureModel.getContent()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			byte[] bytePixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
			
			image = new PImage(bufferedImage.getWidth(), bufferedImage.getHeight());
			image.pixels = MT4JUtils.byteArrayToIntArray(bytePixels);
			image.loadPixels();
			image.updatePixels();
			content.removeFromParent();
			content.destroy();
			content = new MTImage(image, mtApplication);
			content.setNoFill(true);
			content.setPickable(false);
			content.setNoStroke(true);
			content.setAnchor(PositionAnchor.UPPER_LEFT);
			content.setPositionGlobal(position);
			float scale = image.height * 0.8 < image.width ? width / image.width : height * 0.8f / image.height;
			content.scale(scale, scale, 0f, position);
			addChild(content);
		}
	}
}
