package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
/**
 * This class represents the buttons displayed in the corners
 * We use a MTRoundRectangle which has its width = height and arcHeight = arcWidth = width /2 to build a circle
 * This is necessary in order to have a round button
 *
 */
public class WorkHubButton extends MTRoundRectangle {
	private MTApplication mtApplication;
	private MTTextArea buttonText;

	public WorkHubButton(String text, int corner, int rayon, int segments,
			int textXPos, int textYPos, MTApplication mtApplication) {
		super(getXPositionFromCorner(corner, mtApplication, rayon),
				getYPositionFromCorner(corner, mtApplication, rayon), Constants.Z_POSITION_DEFAULT_BUTTON,
				rayon * 2, rayon * 2, rayon, rayon, segments, mtApplication);
		this.mtApplication = mtApplication;

		buttonText = new MTTextArea(getMtApplication(), FontManager
				.getInstance().createFont(getMtApplication(), "arial.ttf", 20,
						new MTColor(255, 255, 255, 255),
						new MTColor(255, 255, 255, 255)));
		buttonText.setNoFill(true);
		buttonText.setPickable(false);
		buttonText.setText(text);
		buttonText.setNoStroke(true);
		buttonText.setPositionRelativeToParent(new Vector3D(textXPos, textYPos));
		addChild(buttonText);
		
		setFillColor(new MTColor(150, 150, 100, 255));
		setNoStroke(true);		
		
		unregisterAllInputProcessors();
		removeAllGestureEventListeners();
		assignActions(mtApplication, text);
	}

	private void assignActions(MTApplication mtApplication, String text) {
		addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				System.out.println(buttonText.getText());
				if (inEvt instanceof AbstractCursorInputEvt) {
					AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
					switch (cursorInputEvt.getId()) {
					case AbstractCursorInputEvt.INPUT_DETECTED:
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

		switch (text) {
		case Constants.BUTTON_ID_MENU:
			break;
		case Constants.BUTTON_ID_ENVOYER:
			break;
		case Constants.BUTTON_ID_RECEVOIR:
			break;
		case Constants.BUTTON_ID_MASQUER:
			break;
		}
	}

	public static int getXPositionFromCorner(int corner, MTApplication mtApplication, int rayon) {
		int x = ((corner & 0x01) == 0) ? -rayon : mtApplication.getWidth()-rayon;
		return x;
	}
	public static int getYPositionFromCorner(int corner, MTApplication mtApplication, int rayon) {
		int y = ((corner & 0x10) == 0) ? -rayon : mtApplication.getHeight()-rayon;
		return y;
	}

	public MTApplication getMtApplication() {
		return mtApplication;
	}

	public void setMtApplication(MTApplication mtApplication) {
		this.mtApplication = mtApplication;
	}	
}
