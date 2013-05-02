package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public class FileElementView extends AbstractElementView {
	private MTImage content;
/*
 * TODO : Mettre le bon icone
 */

	public FileElementView(PApplet pApplet, Vertex[] vertices) {
		super(pApplet, vertices);
		content = new MTImage(pApplet.loadImage("Image/iconeDefault.jpg"), pApplet);
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