package com.workhub.mt4j;

import java.io.IOException;

import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class LinkElementView extends AbstractElementView{
	private MTTextArea content;

	public LinkElementView(float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		content = new MTTextArea(applet, FontManager.getInstance().createFont(
				applet, "arial.ttf", 18, new MTColor(50, 50, 50, 255),
				MTColor.BLUE));
		content.setNoFill(true);
		content.setText("http://www.google.com");
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX(), (float) (this.getPosition(TransformSpace.GLOBAL).getY()+40)));
		
		registerInputProcessor(new TapProcessor(applet, 25, true, 350));
		addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isDoubleTap()){
					try {
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(content.getText()));
					} catch (IOException e) {
						try {
							java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://"+content.getText()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
				return false;
			}
		});
		addChild(content);
	}
	
	public void editElementContent() {
		MTKeyboard keyb = new MTKeyboard(mtApplication);
        keyb.setFillColor(new MTColor(30, 30, 30, 210));
        keyb.setStrokeColor(new MTColor(0,0,0,255));
        getParent().addChild(keyb);
		keyb.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f,0));
		
		content.setEnableCaret(true);
		keyb.addTextInputListener(content);
		keyb.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
			@Override
			public void stateChanged(StateChangeEvent evt) {
				content.setEnableCaret(false);
			}
		});
	}
	
	public MTTextArea getContent() {
		return content;
	}

	public void setContent(MTTextArea content) {
		this.content = content;
	}
}
