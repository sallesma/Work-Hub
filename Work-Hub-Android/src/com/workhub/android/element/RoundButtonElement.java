package com.workhub.android.element;

import java.util.List;

import jade.core.AID;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.HorizontalAlign;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;

public class RoundButtonElement extends AbstractElement{


	public final float RAYON;

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
	private LoopEntityModifier entityModifier;
	private Sprite cercleBody;

	public RoundButtonElement(float centerX, float centerY,int type, Ressources res, Object arg) {
		super(centerX, centerY, res);
		RAYON = res.toPixel(90);
		this.setType(type);
		this.centerScene = res.getScreenCenter();
		initShape(res);
		rotateToCenter();
		this.arg = arg;

	}

	protected void initShape(Ressources res){

		cercleBody = new Sprite(-RAYON, -RAYON ,RAYON*2, RAYON*2,
				res.getTR_Rond(), res.getContext().getVertexBufferObjectManager());
		cercleBody.setColor(110/255f, 200/255f, 240/255f);
		this.attachChild(cercleBody);


		centerText = new Text(0, 0, res.getFont(), getTitle(getType()), new TextOptions(HorizontalAlign.CENTER), res.getContext().getVertexBufferObjectManager());
		System.out.println(res.toPixel(100));		
		centerText.setPosition(-centerText.getWidth()/2, RAYON/4);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_recevoir:
			res.getContext().receive();
			break;	
		}
		super.onClick(v);
	}

	@Override
	protected void iniDialogView() {
		super.iniDialogView();
		if(type==R.id.bt_raccourci_recevoir){
			Button bt_recevoir = (Button) menuDialog.findViewById(R.id.bt_recevoir);
			bt_recevoir.setVisibility(View.VISIBLE);
			bt_recevoir.setOnClickListener(this);
		}
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
		switch (getType()) {
		case R.id.bt_raccourci_editer:
			if(abstractElement instanceof BaseElement){
				res.getContext().askEdition(((BaseElement) abstractElement).getModel().getAgent());
			}
			break;
		case R.id.bt_raccourci_envoyer:
			if(abstractElement instanceof GroupElement){
				List<BaseElement> es = ((GroupElement)abstractElement).getBaseElements();
				for(int i = 0 ; i <es.size() ; i++ ){
					res.getContext().sendElement((AID) arg, ((BaseElement)es.get(i)).getModel().getAgent());
				}
			}
			else if(abstractElement instanceof BaseElement){
				res.getContext().sendElement((AID) arg, ((BaseElement)abstractElement).getModel().getAgent());
			}
			abstractElement.masquer();
			break;
		case R.id.bt_raccourci_exporter:
			if(abstractElement instanceof GroupElement){
				((GroupElement)abstractElement).export();
			}else if (abstractElement instanceof BaseElement){
				((BaseElement)abstractElement).export();
			}
			break;
		case R.id.bt_raccourci_masquer:
			if(abstractElement instanceof GroupElement){
				((GroupElement)abstractElement).masquer();
			}else if(abstractElement instanceof BaseElement){
				((BaseElement)abstractElement).masquer();
			}
			break;
		case R.id.bt_raccourci_recevoir:
			res.getContext().receive();
			break;
		case R.id.bt_raccourci_supprimer:
			abstractElement.remove();
			break;


		}

	}

	@Override
	protected void onShortClick() 
	{
		if(type==R.id.bt_raccourci_recevoir){
			res.getContext().receive();
		}
	};

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void animate(int size) {


		if(size==0&&entityModifier!=null){
			res.getContext().runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					cercleBody.unregisterEntityModifier(entityModifier);
					cercleBody.setScale(1);
					entityModifier = null;
				}
			});
		}else if(size>0&&entityModifier==null){
			entityModifier = new LoopEntityModifier( new SequenceEntityModifier(
					new ScaleModifier(0.5f, 1, 1.1f),
					new ScaleModifier(0.5f, 1.1f, 1)
					));
			cercleBody.registerEntityModifier(entityModifier);


		}
	}






}
