package com.workhub.mt4j;

import org.mt4j.components.interfaces.IclickableButton;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public abstract class AbstractElementView extends MTPolygon implements IclickableButton, IGestureEventListener, IMTInputEventListener{
	/*
	 * TODO : empêcher que le titre ou le contenu dépassent du post it
	 * TODO : Mettre une taille par défaut
	 */
	private MTTextArea title;
	
	public AbstractElementView(PApplet pApplet, Vertex[] vertices) {
		super(pApplet, vertices);
		
		title = new MTTextArea(pApplet, FontManager.getInstance().createFont(
				pApplet, "arial.ttf", 20, new MTColor(0, 0, 0, 255),
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
	
	public MTTextArea getTitle() {
		return title;
	}

	public void setTitle(MTTextArea title) {
		this.title = title;
	}
}
