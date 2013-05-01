package com.workhub.mt4j;

import org.mt4j.components.interfaces.IclickableButton;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public abstract class WorkHubAbstractElement extends MTPolygon implements IclickableButton, IGestureEventListener, IMTInputEventListener{

	public WorkHubAbstractElement(PApplet pApplet, Vertex[] vertices) {
		super(pApplet, vertices);
		MTLine ligne = new MTLine(getRenderer(), new Vertex(210, 240), new Vertex(390, 240));
		ligne.setFillColor(new MTColor(0, 0, 0, 255));
		ligne.setPickable(false);
		addChild(ligne);
		// TODO Auto-generated constructor stub
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

}
