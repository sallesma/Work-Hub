package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.IdragClusterable;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;

import processing.core.PApplet;

public abstract class AbstractElementView extends MTClipRectangle implements IdragClusterable {
	protected MTTextArea title;
	protected MTApplication mtApplication;
	protected WorkHubScene scene;
	protected ElementModel model;
	protected MTKeyboard keyboard;
	protected MTTextArea keyboardTarget;

	public AbstractElementView(float x, float y, float z, float width,
			float height, PApplet applet, final WorkHubScene scene) {
		super(x, y, z, width, height, applet);
		this.mtApplication = (MTApplication) applet;
		this.scene = scene;
		this.model = null;
		this.keyboard = null;
		this.keyboardTarget = null;
		setAnchor(PositionAnchor.UPPER_LEFT);

		title = new MTTextArea(applet, FontManager.getInstance().createFont(
				applet, "arial.ttf", 20, new MTColor(0, 0, 0, 255),
				new MTColor(0, 0, 0, 255)));
		title.setNoFill(true);
		title.setPickable(false);
		title.setNoStroke(true);
		title.setAnchor(PositionAnchor.UPPER_LEFT);
		title.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX(), (float) (this.getPosition(TransformSpace.GLOBAL).getY())));
		addChild(title);

		MTLine ligne = new MTLine(getRenderer(), new Vertex(210, 240), new Vertex(390, 240));
		ligne.setFillColor(new MTColor(0, 0, 0, 255));
		ligne.setPickable(false);
		ligne.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX()+100, (float) (this.getPosition(TransformSpace.GLOBAL).getY()+32.0)));
		addChild(ligne);

		setFillColor(new MTColor(250, 230, 100, 255));

		registerInputProcessor(new TapAndHoldProcessor(applet, 700));
		addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer((MTApplication) applet, scene.getCanvas()));
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
		
		addGestureListener(DragProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent)ge;
				if(de.getId() == DragEvent.GESTURE_ENDED) {
					String intersection = scene.testShortCutIntersection(de.getTo());
					if(intersection != null) {
						switch(intersection) {
						case MT4JConstants.BUTTON_ID_MENU :
							// Pas d'action
							break;
						case MT4JConstants.BUTTON_ID_ENVOYER :
							// TODO
							break;
						case MT4JConstants.BUTTON_ID_RECEVOIR :
							// Pas d'action
							break;
						case MT4JConstants.BUTTON_ID_MASQUER :
							AbstractElementView.this.destroy();
							break;
						default :
							// Raccourci inconnu
							break;
						}
					}
				}
				return false;
			}
		});

		scene.getCanvas().addChild(this);
	}

	@Override
	final public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
	}

	@Override
	final public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	public abstract void editElementContent();

	public void addLassoProcessor(){
		AbstractComponentProcessor[] processors = getParent().getInputProcessors();
		for (int i = 0 ; i < processors.length ; i++ ) {
			if ( processors[i].getClass() == LassoProcessor.class )
				((LassoProcessor) processors[i]).addClusterable((IdragClusterable) this);
		}
	}

	public void editElementTitle(){
		createEditionKeyboard(title);
	}

	protected void updateTitleWithElementPath(String path) {
		String[] splittedPath = path.split("[\\\\/]");
		System.out.println(splittedPath.toString());
		title.setText(splittedPath[splittedPath.length-1]);
	}

	protected void openContextualMenu(Vector3D locationOnScreen) {
		ContextMenu contextMenu = new ContextMenu(this, (int)locationOnScreen.x, (int)locationOnScreen.y, mtApplication, scene, MT4JConstants.CONTEXT_ELEMENT_MENU);
		this.getParent().addChild(contextMenu);
	}

	public MTTextArea getTitle() {
		return title;
	}

	public void setTitle(MTTextArea title) {
		this.title = title;
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public void createEditionKeyboard(final MTTextArea target) {
		MTKeyboard keyb = new MTKeyboard(mtApplication);
		keyb.setFillColor(new MTColor(30, 30, 30, 210));
		keyb.setStrokeColor(new MTColor(0,0,0,255));

		//permet de supprimer l'inertie
		keyb.removeAllGestureEventListeners();
		keyb.addGestureListener(DragProcessor.class, new DefaultDragAction());
		keyb.addGestureListener(RotateProcessor.class, new DefaultRotateAction());
		keyb.addGestureListener(ScaleProcessor.class, new DefaultScaleAction());

		getParent().addChild(keyb);
		Vector3D position = this.getPosition(TransformSpace.GLOBAL);
		Vector3D offset = new Vector3D(this.getWidthXYGlobal() / 2, this.getHeightXYGlobal() + keyb.getHeightXY(TransformSpace.GLOBAL) / 2);
		position = position.addLocal(offset);
		keyb.setPositionGlobal(position);
		MT4JUtils.fixPosition(keyb, (int)position.x, (int)position.y, this.mtApplication, PositionAnchor.CENTER);

		keyb.addTextInputListener(target);
		keyb.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
			@Override
			public void stateChanged(StateChangeEvent evt) {
				target.setEnableCaret(false);
				saveModel();
				JadeInterface.getInstance().finishEdition(model.getAgent());
				keyboard = null;
				keyboardTarget = null;
			}
		});
		
		keyboard = keyb;
		keyboardTarget = target;
		setEnableKeyboard(false);
	}

	public ElementModel getModel() {
		return model;
	}

	public void setModel(ElementModel model) {
		this.model = model;
		updateView();
	}

	public void tryEditElementTitle() {
		editElementTitle();
		JadeInterface.getInstance().askEdition(model.getAgent());
	}

	public void tryEditElementContent() {
		editElementContent();
		JadeInterface.getInstance().askEdition(model.getAgent());
	}

	public void saveModel() {
		model.setTitle(title.getText());
		model.setColor(MT4JUtils.MTColorToInt(getFillColor()));
		saveContent();
		JadeInterface.getInstance().saveElement(model);
	}

	public abstract void saveContent();

	public abstract int getType();
	
	public MTKeyboard getKeyboard() {
		return keyboard;
	}
	
	public void setEnableKeyboard(boolean enableKeyboard) {
		keyboard.setVisible(enableKeyboard);
		keyboard.setEnabled(enableKeyboard);
		keyboardTarget.setEnableCaret(enableKeyboard);
	}
	
	public void updateView() {
		setTitle(model.getTitle());
		setFillColor(MT4JUtils.intToMTColor(model.getColor()));
		updateContent();
	}
	
	public abstract void updateContent();
	
	public static AbstractElementView createEmptyElement(int type, Vector3D location, PApplet applet, WorkHubScene scene) {
		float x = location.x;
		float y = location.y;
		switch(type) {
		case Constants.TYPE_ELEMENT_TEXT :
			return new TextElementView(x, y, MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
		case Constants.TYPE_ELEMENT_LINK :
			return new LinkElementView(x, y, MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
		case Constants.TYPE_ELEMENT_PICTURE :
			return new ImageElementView(null, x, y, MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
		case Constants.TYPE_ELEMENT_FILE :
			return new FileElementView(null, x, y, MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
		default :
			return null;
		}
	}
}
