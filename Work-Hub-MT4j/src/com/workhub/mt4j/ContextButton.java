package com.workhub.mt4j;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class ContextButton extends MTListCell {
	private MTTextField m_text;
	private final MTComponent m_source;
	
	public ContextButton(final PApplet applet, MTComponent source, final String text) {
		super(Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, applet);
		
		IFont font = FontManager.getInstance().createFont(applet, "arial.ttf", 18);
		m_source = source;
		m_text = new MTTextField(0, 0, Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, font, applet);
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
							//just close the menu (instruction below)
							break;
						case Constants.CONTEXT_BUTTON_DELETE:
							m_source.removeFromParent();
							break;
						case Constants.CONTEXT_BUTTON_CREATE_TEXT:
							TextElementView textElement = new TextElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(textElement);
							break;
						case Constants.CONTEXT_BUTTON_CREATE_IMAGE:
							ImageElementView imageElement = new ImageElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(imageElement);
							break;
						case Constants.CONTEXT_BUTTON_CREATE_LINK:
							LinkElementView linkElement = new LinkElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(linkElement);
							break;
						case Constants.CONTEXT_BUTTON_CREATE_FILE:
							FileElementView fileElement = new FileElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(fileElement);
							break;
						case Constants.CONTEXT_BUTTON_VISUALIZE_ELEMENTS:
							break;
						case Constants.CONTEXT_BUTTON_EDIT:
							break;
						case Constants.CONTEXT_BUTTON_SHARE:
							break;
						case Constants.CONTEXT_BUTTON_CHANGE_COLOR:
							break;
						case Constants.CONTEXT_BUTTON_EXPORT_PDF:
							break;
						case Constants.CONTEXT_BUTTON_HIDE:
							break;
						case Constants.CONTEXT_BUTTON_SPLIT_GROUP:
							break;
						}
						getParent().getParent().removeFromParent();
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
