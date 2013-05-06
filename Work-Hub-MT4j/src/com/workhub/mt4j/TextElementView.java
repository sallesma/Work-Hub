package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class TextElementView extends AbstractElementView {
	public TextElementView(float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		content = new MTTextArea(applet, FontManager.getInstance().createFont(
				applet, "arial.ttf", 18, new MTColor(50, 50, 50, 255),
				new MTColor(0, 0, 0, 255)));
		content.setNoFill(true);
		content.setText("Ajoutez votre texte ici");
		content.setPickable(false);
		content.setNoStroke(true);
		content.setPositionRelativeToParent(new Vector3D(300, 260));
		addChild(content);
	}

	private MTTextArea content;

	public MTTextArea getContent() {
		return content;
	}

	public void setContent(MTTextArea content) {
		this.content = content;
	}
}
