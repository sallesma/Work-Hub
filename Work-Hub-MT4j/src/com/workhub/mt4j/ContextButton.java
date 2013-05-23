package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class ContextButton extends MTListCell {
	private MTTextField m_text;
	
	public ContextButton(PApplet applet, final String text) {
		super(Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, applet);
		
		IFont font = FontManager.getInstance().createFont(applet, "arial.ttf", 18);
		
		m_text = new MTTextField(0, 0, 200, 40, font, applet);
		m_text.setFillColor(MTColor.AQUA);
		m_text.setText(text);
		addChild(m_text);

		addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				if (inEvt instanceof AbstractCursorInputEvt) {
					AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
					switch (cursorInputEvt.getId()) {
					case AbstractCursorInputEvt.INPUT_DETECTED:
						switch (text) {
						case Constants.CONTEXT_BUTTON_CLOSE:
							getParent().getParent().removeFromParent();
						}
						break;
					case AbstractCursorInputEvt.INPUT_ENDED:
						break;
					case AbstractCursorInputEvt.INPUT_UPDATED:
						break;
					default:
						break;
					}
				}
				return false;
			}
		});

	}

}
