package com.workhub.mt4j;

import java.util.Map;

import jade.core.AID;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
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

import com.workhub.model.ElementModel;
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

		PImage image = mtApplication.loadImage("Image/logoWH.png");
		imageFond = new MTImage(image, mtApplication);
		imageFond.setNoFill(true);
		imageFond.setNoStroke(true);
		imageFond.setPickable(false);
		imageFond.setAnchor(PositionAnchor.CENTER);
		imageFond.setPositionGlobal(new Vector3D(mtApplication.getWidth()/2f, mtApplication.getHeight()/2f));
		imageFond.getImage().setNoStroke(true);
		getCanvas().addChild(imageFond);

		menuButton = new WorkHubButton(MT4JConstants.BUTTON_ID_MENU, MT4JConstants.CORNER_TOP_LEFT, MT4JConstants.SHORTCUT_BUTTON_RADIUS, 1000, 40, 40, getMTApplication(), this);
		envoyerButton = new WorkHubButton(MT4JConstants.BUTTON_ID_ENVOYER, MT4JConstants.CORNER_BOTTOM_RIGHT, MT4JConstants.SHORTCUT_BUTTON_RADIUS, 1000, 980, 700, getMTApplication(), this);
		recevoirButton = new WorkHubButton(MT4JConstants.BUTTON_ID_RECEVOIR, MT4JConstants.CORNER_BOTTOM_LEFT, MT4JConstants.SHORTCUT_BUTTON_RADIUS, 1000, 50, 700, getMTApplication(), this);
		masquerButton = new WorkHubButton(MT4JConstants.BUTTON_ID_MASQUER, MT4JConstants.CORNER_TOP_RIGHT, MT4JConstants.SHORTCUT_BUTTON_RADIUS, 1000, 980, 40, getMTApplication(), this);
		masquerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth()-20, -20));
		this.getCanvas().addChild(menuButton);
		this.getCanvas().addChild(masquerButton);
		this.getCanvas().addChild(envoyerButton);
		this.getCanvas().addChild(recevoirButton);

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
						openContextualMenu(tahe.getLocationOnScreen(), MT4JConstants.CONTEXT_BACKGROUND_MENU);
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

	public void openContextualMenu(Vector3D location, int menuType) {
		ContextMenu contextMenu = new ContextMenu(getCanvas(), (int)location.x, (int)location.y, getMTApplication(), this, menuType);
		this.getCanvas().addChild(contextMenu);
	}

	// Utilise pour les raccourcis
	public void openContextualMenu(Vector3D location, WorkHubButton source) {
		ContextMenu contextMenu = new ContextMenu(source, (int)location.x, (int)location.y, getMTApplication(), this, MT4JConstants.CONTEXT_SHORTCUT_MENU);
		this.getCanvas().addChild(contextMenu);
	}

	// Utilise pour traiter EVENT_TYPE_ELEMENTS et EVENT_TYPE_NEIGHBOURS
	public void openContextualMenu(Map<AID, String> map, int menuType) {
		Vector3D location = null;
		MTComponent source = null;
		switch(menuType) {
		case MT4JConstants.CONTEXT_IMPORT_MENU :
			location = MT4JUtils.removeBeginning(ContextMenu.elementViewLocation);
			source = getCanvas();
			break;
		case MT4JConstants.CONTEXT_EXPORT_MENU :
			ExportData data = MT4JUtils.removeBeginning(ContextMenu.exportLocation);
			location = data.getLocation();
			source = data.getComponent();
			break;
		default :
			break;
		}
		ContextMenu contextMenu = new ContextMenu(source, (int)location.x, (int)location.y,
				getMTApplication(), this, menuType, map);
		this.getCanvas().addChild(contextMenu);
	}

	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}

	public AbstractElementView getElement(AID aid) {
		for(MTComponent comp : getCanvas().getChildren()) {
			if(comp instanceof AbstractElementView) {
				AbstractElementView elt = (AbstractElementView)comp;
				if(elt.getModel() != null && elt.getModel().getAgent().equals(aid)) {
					return elt;
				}
			}
		}
		return null;
	}

	// Ajoute le modele au premier element qui correspond.
	public void attachModel(ElementModel model) {
		int type = model.getType();
		MTComponent[] children = getCanvas().getChildren();
		boolean found = false;
		for(int i = 0 ; i < children.length && !found ; i++) {
			if(children[i] instanceof AbstractElementView) {
				AbstractElementView element = (AbstractElementView)children[i];
				if(element.getType() == type && element.getModel() == null) {
					element.setModel(model);
					found = true;
				}
			}
		}
	}

	// Renvoie le bouton intersecte s'il y en a un, null sinon
	public String testShortCutIntersection(Vector3D position) {
		if(menuButton != null && Vector3D.distance(position, menuButton.getCenterPointGlobal()) <= MT4JConstants.SHORTCUT_BUTTON_RADIUS) {
			return MT4JConstants.BUTTON_ID_MENU; 
		}
		if(envoyerButton != null && Vector3D.distance(position, envoyerButton.getCenterPointGlobal()) <= MT4JConstants.SHORTCUT_BUTTON_RADIUS) {
			return MT4JConstants.BUTTON_ID_ENVOYER; 
		}
		if(recevoirButton != null && Vector3D.distance(position, recevoirButton.getCenterPointGlobal()) <= MT4JConstants.SHORTCUT_BUTTON_RADIUS) {
			return MT4JConstants.BUTTON_ID_RECEVOIR; 
		}
		if(masquerButton != null && Vector3D.distance(position, masquerButton.getCenterPointGlobal()) <= MT4JConstants.SHORTCUT_BUTTON_RADIUS) {
			return MT4JConstants.BUTTON_ID_MASQUER; 
		}
		return null;
	}

	public void removeShortcut(String ID) {
		switch(ID) {
		case MT4JConstants.BUTTON_ID_MENU :
			menuButton = null;
			break;
		case MT4JConstants.BUTTON_ID_ENVOYER :
			envoyerButton = null;
			break;
		case MT4JConstants.BUTTON_ID_RECEVOIR :
			recevoirButton = null;
			break;
		case MT4JConstants.BUTTON_ID_MASQUER :
			masquerButton = null;
			break;
		default :
			// Raccourci inconnu
			break;
		}
	}
}
