package com.workhub.mt4j;

import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class TextElementView extends AbstractElementView {
	
	protected MTTextArea content;
	
	public TextElementView(float x, float y, float width,
			float height, PApplet applet) {
		super(x, y, Constants.Z_POSITION_DEFAULT_ELEMENT, width, height, applet);
		content = new MTTextArea(applet, FontManager.getInstance().createFont(
				applet, "arial.ttf", 18, new MTColor(50, 50, 50, 255),
				new MTColor(0, 0, 0, 255)));
		content.setNoFill(true);
		content.setText("Ajoutez votre texte ici");
		content.setPickable(false);
		content.setNoStroke(true);
		content.setAnchor(PositionAnchor.UPPER_LEFT);
		content.setPositionGlobal(new Vector3D(this.getPosition(TransformSpace.GLOBAL).getX(), (float) (this.getPosition(TransformSpace.GLOBAL).getY()+40)));
		addChild(content);
	}

	public void editElementContent(){
		MTKeyboard keyb = new MTKeyboard(mtApplication);
        keyb.setFillColor(new MTColor(30, 30, 30, 210));
        keyb.setStrokeColor(new MTColor(0,0,0,255));
        getParent().addChild(keyb);
		Vector3D position = this.getPosition(TransformSpace.GLOBAL);
		Vector3D offset = new Vector3D(this.getWidthXYGlobal() / 2, this.getHeightXYGlobal() + keyb.getHeightXY(TransformSpace.GLOBAL) / 2);
		position = position.addLocal(offset);
		keyb.setPositionGlobal(position);
		Utils.fixPosition(keyb, (int)position.x, (int)position.y, this.mtApplication, PositionAnchor.CENTER);
		
		content.setEnableCaret(true);
		keyb.addTextInputListener(content);
		keyb.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
			@Override
			public void stateChanged(StateChangeEvent evt) {
				content.setEnableCaret(false);
			}
		});
	}

	public MTTextArea getContent() {
		return content;
	}

	public void setContent(MTTextArea content) {
		this.content = content;
	}
}
