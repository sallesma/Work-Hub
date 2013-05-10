package com.workhub.android.element;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.view.View;

import com.workhub.android.R;
import com.workhub.android.utils.Constants;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;

public class RoundButtonElement extends AbstractElement{

	public final static int TYPE_ENVOYER = 0;
	public final static int TYPE_SUPPRIMER = 1;
	public final static int TYPE_RECEVOIR = 2;

	public final static int RAYON = Constants.SCREEN_WIDTH/4;

	private final static String[] TITRE = {"Envoyer", "Supprimer", "Recevoir"};


	private int type;
	private Text centerText;
	private GPoint centerScene;

	public RoundButtonElement(float centerX, float centerY,int type, Ressources res) {
		super(centerX, centerY, res);
		this.type = type;
		this.centerScene = res.getScreenCenter();
		initShape(res);
		rotateToCenter();

	}

	protected void initShape(Ressources res){

		Sprite s = new Sprite(-RAYON, -RAYON ,RAYON*2, RAYON*2,
				res.getTR_Rond(), res.getContext().getVertexBufferObjectManager());
		s.setColor(0, 0, 0.8f);
		this.attachChild(s);


		centerText = new Text(0, 0, res.getFont(), TITRE[type], new TextOptions(HorizontalAlign.CENTER), res.getContext().getVertexBufferObjectManager());
		centerText.setPosition(-centerText.getWidth()/2, RAYON/3);
		centerText.setColor(Color.WHITE);
		this.attachChild(centerText);
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		super.onScroll(pScollDetector, pPointerID, pDistanceX, pDistanceY);
		rotateToCenter();
	};

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {}

	private void rotateToCenter(){
		GPoint pos = new GPoint(this.getX(),  this.getY());


		this.setRotation(GPoint.AngleBetween(pos, centerScene)+90);
		centerText.setRotation(-this.getRotation());

	}

	@Override
	public boolean contains(float pX, float pY) {
		float distance = GPoint.Distance(new GPoint(pX, pY), new GPoint(getX(), getY()));
		float rayon = RAYON*this.getScaleX();
		return (distance<rayon);
	}

	public void setActionOn(AbstractElement abstractElement) {
		switch (type) {
		case TYPE_ENVOYER:
			//TODO
			break;
		case TYPE_RECEVOIR:
			//TODO
			break;
		case TYPE_SUPPRIMER:
		abstractElement.remove();
		break;


		}

	}






}
