package com.workhub.mt4j;

import java.io.IOException;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import com.workhub.model.LinkElementModel;
import com.workhub.utils.Constants;

import processing.core.PApplet;

public class LinkElementView extends TextElementView{
	public LinkElementView(float x, float y, float width,
			float height, PApplet applet, WorkHubScene scene) {
		super(x, y, width, height, applet, scene);
		content.setFont(FontManager.getInstance().createFont(
				applet, "arial.ttf", 18, new MTColor(50, 50, 50, 255),
				MTColor.BLUE));
		content.setText("http://www.google.com");
		
		registerInputProcessor(new TapProcessor(applet, 25, true, 350));
		addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isDoubleTap()){
					try {
						if (content.getText().startsWith("http://")){
							java.awt.Desktop.getDesktop().browse(java.net.URI.create(content.getText()));
						} else {
							java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://"+content.getText()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
		});
	}

	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_LINK;
	}
	
	@Override
	public void updateContent() {
		LinkElementModel linkModel = (LinkElementModel)model;
		content.setText(linkModel.getContent());
	}
}
