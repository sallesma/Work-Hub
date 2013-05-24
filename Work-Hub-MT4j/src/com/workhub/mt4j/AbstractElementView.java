package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public abstract class AbstractElementView extends MTClipRectangle {

	/*
	 * TODO : empêcher que le titre ou le contenu dépassent du post it
	 * TODO : Mettre une taille par défaut
	 */
	private MTTextArea title;
	private MTApplication mtApplication;
	
	public AbstractElementView(float x, float y, float z, float width,
			float height, PApplet applet) {
		super(x, y, z, width, height, applet);
		this.mtApplication = (MTApplication) applet;
		title = new MTTextArea(applet, FontManager.getInstance().createFont(
				applet, "arial.ttf", 20, new MTColor(0, 0, 0, 255),
				new MTColor(0, 0, 0, 255)));
		title.setNoFill(true);
		title.setText("Mon titre");
		title.setPickable(false);
		title.setNoStroke(true);
		title.setPositionRelativeToParent(new Vector3D(250, 220));
		addChild(title);
		
		MTLine ligne = new MTLine(getRenderer(), new Vertex(210, 240), new Vertex(390, 240));
		ligne.setFillColor(new MTColor(0, 0, 0, 255));
		ligne.setPickable(false);
		addChild(ligne);
		
		setFillColor(new MTColor(250, 230, 100, 255));
		
		registerInputProcessor(new TapAndHoldProcessor(applet, 1000));
		addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer((MTApplication) applet, this));
		addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent tahe = (TapAndHoldEvent)ge;
				switch (tahe.getId()) {
				case TapAndHoldEvent.GESTURE_DETECTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (tahe.isHoldComplete()){
						openContextualMenu(tahe.getLocationOnScreen());
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	protected void openContextualMenu(Vector3D locationOnScreen) {
		ContextMenu contextMenu = new ContextMenu(this, (int)locationOnScreen.x, (int)locationOnScreen.y, mtApplication, Constants.CONTEXT_ELEMENT_MENU);
		this.getParent().addChild(contextMenu);
	}
	
	public MTTextArea getTitle() {
		return title;
	}

	public void setTitle(MTTextArea title) {
		this.title = title;
	}
}
