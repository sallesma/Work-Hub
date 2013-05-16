package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class ContextButton extends MTListCell {
	private MTTextField m_text;
	
	public ContextButton(PApplet applet, String text) {
		super(Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, applet);
		
		IFont font = FontManager.getInstance().createFont(applet, "arial.ttf", 18);
		
		m_text = new MTTextField(0, 0, 200, 40, font, applet);
		m_text.setFillColor(MTColor.AQUA);
		m_text.setText(text);
		addChild(m_text);
	}

}
