package com.workhub.mt4j;

import java.util.List;

import org.mt4j.components.visibleComponents.widgets.MTList;

import processing.core.PApplet;

public class ContextMenu extends MTList {

	public ContextMenu(int x, int y, PApplet applet, List<ContextButton> buttons) {
		super(x, y, Constants.CONTEXT_BUTTON_WIDTH, buttons.size() * Constants.CONTEXT_BUTTON_HEIGHT, applet);
		for(ContextButton button : buttons) {
			addChild(button);
		}
		
		setVisible(true);
	}	
}
