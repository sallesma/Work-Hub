package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class WorkHubScene extends AbstractScene {
	private WorkHubButton menuButton;
	private WorkHubButton envoyerButton;
	private WorkHubButton recevoirButton;
	private WorkHubButton masquerButton;
	private MTImage imageFond;
	
	public WorkHubScene(MTApplication mtApplication, String name) throws WorkHubException{
		super(mtApplication, name);
		this.setClearColor(new MTColor(198, 200, 200, 255));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		menuButton = new WorkHubButton(Constants.BUTTON_ID_MENU, Constants.CORNER_TOP_LEFT, 130, 1000, 40, 40, getMTApplication(), this);
		envoyerButton = new WorkHubButton(Constants.BUTTON_ID_ENVOYER, Constants.CORNER_BOTTOM_RIGHT, 130, 1000, 980, 700, getMTApplication(), this);
		recevoirButton = new WorkHubButton(Constants.BUTTON_ID_RECEVOIR, Constants.CORNER_BOTTOM_LEFT, 130, 1000, 50, 700, getMTApplication(), this);
		masquerButton = new WorkHubButton(Constants.BUTTON_ID_MASQUER, Constants.CORNER_TOP_RIGHT, 130, 1000, 980, 40, getMTApplication(), this);
		masquerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth()-20, -20));
		this.getCanvas().addChild(menuButton);
		this.getCanvas().addChild(masquerButton);
		this.getCanvas().addChild(envoyerButton);
		this.getCanvas().addChild(recevoirButton);
		
		PImage image = mtApplication.loadImage("Image/logoWH.png");
		imageFond = new MTImage(image, mtApplication);
		imageFond.setNoFill(true);
		imageFond.setNoStroke(true);
		imageFond.setPickable(false);
		imageFond.setAnchor(PositionAnchor.CENTER);
		imageFond.setPositionGlobal(new Vector3D(mtApplication.getWidth()/2f, mtApplication.getHeight()/2f));
		imageFond.getImage().setNoStroke(true);
		getCanvas().addChild(imageFond);

		getCanvas().registerInputProcessor(new TapAndHoldProcessor(mtApplication, 700));
		getCanvas().addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(mtApplication, getCanvas()));
		getCanvas().addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
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
		
		LassoProcessor lassoProcessor = new LassoProcessor(mtApplication, getCanvas(), getSceneCam());
		getCanvas().registerInputProcessor(lassoProcessor);
		getCanvas().addGestureListener(LassoProcessor.class, new LassoAction(mtApplication, this, getCanvas().getClusterManager(), getCanvas()));
	}
	
	public void openContextualMenu(Vector3D location) {
		ContextMenu contextMenu = new ContextMenu(getCanvas(), (int)location.x, (int)location.y, getMTApplication(), this, Constants.CONTEXT_BACKGROUND_MENU);
		this.getCanvas().addChild(contextMenu);
	}

	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}
}
