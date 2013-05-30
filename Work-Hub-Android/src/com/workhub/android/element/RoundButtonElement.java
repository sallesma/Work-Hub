package com.workhub.android.element;

import jade.core.AID;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.HorizontalAlign;

import android.app.Dialog;

import com.workhub.android.R;
import com.workhub.android.utils.ConstantsAndroid;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;

public class RoundButtonElement extends AbstractElement{


	public final static int RAYON = ConstantsAndroid.SCREEN_WIDTH/4;

	private String getTitle(int type){
		switch (type) {
		case R.id.bt_raccourci_editer:
			return res.getContext().getString(R.string.raccourci_editer);
		case R.id.bt_raccourci_envoyer:
			return res.getContext().getString(R.string.raccourci_envoyer);
		case R.id.bt_raccourci_exporter:
			return res.getContext().getString(R.string.raccourci_exporter);
		case R.id.bt_raccourci_masquer:
			return res.getContext().getString(R.string.raccourci_masquer);
		case R.id.bt_raccourci_recevoir:
			return res.getContext().getString(R.string.raccourci_recevoir);
		case R.id.bt_raccourci_supprimer:
			return res.getContext().getString(R.string.raccourci_supprimer);
		}

		return "Erreur";
	}

	protected Dialog editDialog;
	private int type;
	private Text centerText;
	private GPoint centerScene;
	private Object arg;

	public RoundButtonElement(float centerX, float centerY,int type, Ressources res, Object arg) {
		super(centerX, centerY, res);
		this.type = type;
		this.centerScene = res.getScreenCenter();
		initShape(res);
		rotateToCenter();
		this.arg = arg;

	}

	protected void initShape(Ressources res){

		Sprite s = new Sprite(-RAYON, -RAYON ,RAYON*2, RAYON*2,
				res.getTR_Rond(), res.getContext().getVertexBufferObjectManager());
		s.setColor(110/255f, 200/255f, 240/255f);
		this.attachChild(s);


		centerText = new Text(0, 0, res.getFont(), getTitle(type), new TextOptions(HorizontalAlign.CENTER), res.getContext().getVertexBufferObjectManager());
		centerText.setPosition(-centerText.getWidth()/2, RAYON/3);
		this.attachChild(centerText);
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		super.onScroll(pScollDetector, pPointerID, pDistanceX, pDistanceY);
		rotateToCenter();
	};

	@Override
	public void onHoldFinished(HoldDetector pHoldDetector,
			long pHoldTimeMilliseconds, int pPointerID, float pHoldX,
			float pHoldY) {
		super.onHoldFinished(pHoldDetector, pHoldTimeMilliseconds, pPointerID, pHoldX,
				pHoldY);
		res.getContext().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				menuDialog.show();
			}
		});
	}



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
		case R.id.bt_raccourci_editer:
			if(abstractElement instanceof BaseElement){
				((BaseElement)abstractElement).edit();
			}
			break;
		case R.id.bt_raccourci_envoyer:
			if(abstractElement instanceof BaseElement){
				res.getContext().sendElement((AID) arg, ((BaseElement)abstractElement).getModel().getAgent());
			}
			
			break;
		case R.id.bt_raccourci_exporter:
			//TODO
			break;
		case R.id.bt_raccourci_masquer:
			if(abstractElement instanceof BaseElement){
				((BaseElement)abstractElement).masquer();
			}
			break;
		case R.id.bt_raccourci_recevoir:
			//TODO
			break;
		case R.id.bt_raccourci_supprimer:
			abstractElement.remove();
			break;


		}

	}






}
