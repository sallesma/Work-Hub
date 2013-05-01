package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public class TextElementView extends AbstractElementView {
	private MTTextArea title;
	private MTTextArea content;
/*
 * TODO : empêcher que le titre ou le contenu dépasse du post it
 */
	public TextElementView(PApplet pApplet, Vertex[] vertices) {
		super(pApplet, vertices);
		title = new MTTextArea(pApplet, FontManager.getInstance().createFont(
				pApplet, "arial.ttf", 20, new MTColor(0, 0, 0, 255),
				new MTColor(0, 0, 0, 255)));
		title.setNoFill(true);
		title.setText("Mon titre");
		title.setPickable(false);
		title.setNoStroke(true);
		title.setPositionRelativeToParent(new Vector3D(250, 220));
		addChild(title);
		
		content = new MTTextArea(pApplet, FontManager.getInstance().createFont(
				pApplet, "arial.ttf", 18, new MTColor(50, 50, 50, 255),
				new MTColor(0, 0, 0, 255)));
		content.setNoFill(true);
		content.setText("Ajoutez votre texte ici");
		content.setPickable(false);
		content.setNoStroke(true);
		content.setPositionRelativeToParent(new Vector3D(300, 260));
		addChild(content);

		setFillColor(new MTColor(250, 230, 100, 255));
	}

	public MTTextArea getTitle() {
		return title;
	}

	public void setTitle(MTTextArea title) {
		this.title = title;
	}

	public MTTextArea getContent() {
		return content;
	}

	public void setContent(MTTextArea content) {
		this.content = content;
	}
}
