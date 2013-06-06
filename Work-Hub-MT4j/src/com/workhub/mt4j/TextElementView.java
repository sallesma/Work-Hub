package com.workhub.mt4j;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;

import com.workhub.model.TextElementModel;

import processing.core.PApplet;

public class TextElementView extends AbstractElementView {
	
	protected MTTextArea content;
	
	public TextElementView(float x, float y, float width,
			float height, PApplet applet, WorkHubScene scene) {
		super(x, y, MT4JConstants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet, scene);
		content = new MTTextArea(x,
				y + 40,
				width,
				height - 40,
				FontManager.getInstance().createFont(applet, "arial.ttf", 18, new MTColor(50, 50, 50, 255), new MTColor(0, 0, 0, 255)),
				applet
				);
		content.setNoFill(true);
		content.setText("Ajoutez votre texte ici");
		content.setPickable(false);
		content.setNoStroke(true);
		addChild(content);
	}
	
	public TextElementView(TextElementModel model, float x, float y, float width,
			float height, PApplet applet, WorkHubScene scene) {
		super(x, y, MT4JConstants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet, scene);
		setTitle(model.getTitle(), applet);
		content = new MTTextArea(x,
				y + 40,
				width,
				height - 40,
				FontManager.getInstance().createFont(applet, "arial.ttf", 18, new MTColor(50, 50, 50, 255), new MTColor(0, 0, 0, 255)),
				applet
				);
		content.setNoFill(true);
		content.setText(model.getContent());
		content.setPickable(false);
		content.setNoStroke(true);
		addChild(content);
	}

	public void editElementContent(){
		createEditionKeyboard(content);
	}

	public MTTextArea getContent() {
		return content;
	}

	public void setContent(MTTextArea content) {
		this.content = content;
	}

	@Override
	public void saveContent() {
		((TextElementModel)getModel()).setContent(content.getText());
	}
}
