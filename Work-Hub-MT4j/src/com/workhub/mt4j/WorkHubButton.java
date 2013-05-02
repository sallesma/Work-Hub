package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IclickableButton;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class WorkHubButton extends MTRoundRectangle implements IclickableButton, IGestureEventListener, IMTInputEventListener {
	private MTApplication mtApplication;
	private MTTextArea buttonText;

	public WorkHubButton(String texte, float x, float y, float z, float width,
			float height, float arcWidth, float arcHeight, int segments,
			PApplet pApplet) {
		super(x, y, z, width, height, arcWidth, arcHeight, segments, pApplet);
		mtApplication = (MTApplication) pApplet;
		
		buttonText = new MTTextArea(getMtApplication(), FontManager
				.getInstance().createFont(getMtApplication(), "arial.ttf", 20,
						new MTColor(255, 255, 255, 255),
						new MTColor(255, 255, 255, 255)));
		buttonText.setNoFill(true);
		buttonText.setPickable(false);
		buttonText.setText(texte);
		buttonText.setNoStroke(true);
		buttonText.setPositionRelativeToParent(new Vector3D(70, this.getHeightXY(TransformSpace.LOCAL)-20));
		addChild(buttonText);
		
		setTextPosition(new Vector3D(70, this.getHeightXY(TransformSpace.LOCAL)-20));
		setPositionGlobal(new Vector3D(mtApplication.getWidth(), -60));
		setFillColor(new MTColor(150, 150, 100, 255));
		setNoStroke(true);
		// Empêche le bouton d'être déplacé
		unregisterAllInputProcessors();
		removeAllGestureEventListeners();
		addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				System.out.println("pouet");
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
	}

	@Override
	public void fireActionPerformed(TapEvent ce) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setTextPosition (Vector3D position) {
		buttonText.setPositionRelativeToParent(position);
	}

	public MTApplication getMtApplication() {
		return mtApplication;
	}

	public void setMtApplication(MTApplication mtApplication) {
		this.mtApplication = mtApplication;
	}	
}
