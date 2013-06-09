package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.transition.FlipTransition;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class WorkHubMenu extends AbstractScene {
	private MenuButton m_host;
	private MenuButton m_client;
	private boolean m_isHost = false;
	private String m_IP = null;
	private String m_nickname = null;
	
	public WorkHubMenu(MTApplication mtApplication) {
		super(mtApplication, "WorkHub - Main menu");
		this.setClearColor(new MTColor(198, 200, 200, 255));
		
		int rightX = mtApplication.getWidth() - 50 - 400;
		float y = (mtApplication.getHeight() - 150) / 2;
		m_host = new MenuButton(50, y, "Hôte", mtApplication);
		m_client = new MenuButton(rightX, y, "Client", mtApplication);
		getCanvas().addChild(m_host);
		getCanvas().addChild(m_client);
		
		m_host.addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				if (inEvt instanceof AbstractCursorInputEvt) {
					if(((AbstractCursorInputEvt)inEvt).getId() == AbstractCursorInputEvt.INPUT_DETECTED) {
						m_isHost = true;
						removeButtons();
						m_IP = "localhost";
						askInput("Nickname");
					}
				}
				return false;
			}
		});
		
		m_client.addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				if (inEvt instanceof AbstractCursorInputEvt) {
					if(((AbstractCursorInputEvt)inEvt).getId() == AbstractCursorInputEvt.INPUT_DETECTED) {
						m_isHost = false;
						removeButtons();
						askInput("IP");
					}
				}
				return false;
			}
		});
		
		setTransition(new FlipTransition(mtApplication));
	}

	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}

	public class MenuButton extends MTRectangle {
		public MenuButton(float x, float y, String text, PApplet applet) {
			super(x, y, 400, 150, applet);
			
			setFillColor(new MTColor(110, 200, 240, 255));
			setStrokeColor(new MTColor(110, 170, 200, 255));
			
			MTTextArea buttonText = new MTTextArea(applet, FontManager
					.getInstance().createFont(applet, "arial.ttf", 50,
							new MTColor(0, 0, 0, 255),
							new MTColor(0, 0, 0, 255)));
			buttonText.setNoFill(true);
			buttonText.setPickable(false);
			buttonText.setText(text);
			buttonText.setNoStroke(true);
			
			buttonText.setAnchor(PositionAnchor.UPPER_LEFT);
			float newX = x + (400 - buttonText.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)) / 2;
			float newY = y + (150 - buttonText.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)) / 2;
			buttonText.setPositionGlobal(new Vector3D(newX, newY));
			addChild(buttonText);
			
			unregisterAllInputProcessors();
			removeAllGestureEventListeners();
		}
	}
	
	public void removeButtons() {
		m_host.destroy();
		m_client.destroy();
	}
	
	public void askInput(final String target) {
		MTApplication app = getMTApplication();
		final MTKeyboard keyb = new MTKeyboard(app);
		keyb.setFillColor(new MTColor(30, 30, 30, 210));
		keyb.setStrokeColor(new MTColor(0,0,0,255));
		keyb.setPositionGlobal(new Vector3D(app.getWidth() / 2, app.getHeight() / 2));
		
		final MTTextArea t = new MTTextArea(app, FontManager.getInstance().createFont(app, "arial.ttf", 50, 
        		new MTColor(0,0,0,255), //Fill color 
				new MTColor(0,0,0,255))); //Stroke color
        t.setExpandDirection(ExpandDirection.UP);
		t.setStrokeColor(new MTColor(0,0 , 0, 255));
		t.setFillColor(new MTColor(205,200,177, 255));
		t.setText(target + " ?");
		t.unregisterAllInputProcessors();
		t.setEnableCaret(true);
		t.snapToKeyboard(keyb);
		keyb.addTextInputListener(t);
		
		getCanvas().addChild(keyb);
		
		keyb.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
			@Override
			public void stateChanged(StateChangeEvent evt) {
				switch(target) {
				case "IP" :
					m_IP = t.getText();
					askInput("Nickname");
					break;
				case "Nickname" :
					m_nickname = t.getText();
					try {
						goToNextScene();
					} catch (WorkHubException e) {
						e.printStackTrace();
					}
					break;
				default :
					break;
				}
			}
		});
	}
	
	public void goToNextScene() throws WorkHubException {
		MTApplication app = getMTApplication();
		WorkHubScene scene = new WorkHubScene(app, "WorkHub");
		app.addScene(scene);
		app.changeScene(scene);
		
		JadeInterface.getInstance().setScene(scene);
		JadeInterface.getInstance().startJade(m_IP, null, m_isHost, m_nickname);
	}
}
