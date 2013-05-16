package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextField;

import processing.core.PApplet;

public class ContextButton extends MTListCell {
	private MTTextField m_text;
	
	public ContextButton(PApplet applet, String text) {
		super(Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, applet);
		//m_text = new MTTextField(0, 0, width, height, font, applet)
	}

}
