package com.workhub.android.element;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ContinuousHoldDetector;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.util.math.MathUtils;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.scene.MainScene;
import com.workhub.android.utils.ConstantsAndroid;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;

public abstract class AbstractElement extends Entity  implements ITouchArea, IHoldDetectorListener, IScrollDetectorListener, IPinchZoomDetectorListener, OnClickListener{


	private PinchZoomDetector mPinchZoomDetector;
	private ScrollDetector mScrollDetector;
	private ContinuousHoldDetector mHoldDetector;
	protected Dialog menuDialog;
	protected Ressources res;
	private Sprite touchRound;

	private final long SHORT_CLICK_TIMEOUT = 200;
	private boolean shortClick = false;
	
	protected AbstractElement(float centerX, float centerY, final Ressources res, boolean isResizable){
		super(centerX, centerY);
		this.isResizable = isResizable;
		this.res=res;
		mPinchZoomDetector = new PinchZoomDetector(this);
		mScrollDetector = new ScrollDetector(this);
		mHoldDetector = new ContinuousHoldDetector(this){
			

			

			@Override
			public void onUpdate(float pSecondsElapsed) {
				boolean visible = false;
			
				if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
					final long holdTimeMilliseconds = System.currentTimeMillis() - this.mDownTimeMilliseconds;
					if(!this.mMaximumDistanceExceeded){
						shortClick = (holdTimeMilliseconds<SHORT_CLICK_TIMEOUT);
						
						if(holdTimeMilliseconds <= this.mTriggerHoldMinimumMilliseconds) {
							visible = true;
							float r = -0.25f+((float)(this.mTriggerHoldMinimumMilliseconds-holdTimeMilliseconds))/this.mTriggerHoldMinimumMilliseconds;
							touchRound.setScale(1/getScaleX()-r, 1/getScaleY()-r);
						}
						
					}						
				}
				touchRound.setVisible(visible);
				super.onUpdate(pSecondsElapsed);
			}
		};
		mHoldDetector.setTriggerHoldMinimumMilliseconds(600);
		res.getContext().runOnUiThread(new Runnable() {
			@Override
			public void run() {

				iniDialogView();
			}
		});

		
		this.registerUpdateHandler(mHoldDetector);

		setZIndex(ConstantsAndroid.ZINDEX++);
		touchRound = new Sprite(-res.toPixel(50), -res.toPixel(50), res.toPixel(50)*2,res.toPixel(50)*2, res.getTR_Rond(), res.getContext().getVertexBufferObjectManager());
		touchRound.setColor(0.2f, 0.2f, 0.2f);
		
		this.attachChild(touchRound);
		touchRound.setZIndex(400);
		sortChildren(false);
	} 
	
	
	
	
	

	protected AbstractElement(float centerX, float centerY, Ressources res2) {
		this(centerX, centerY, res2, false);
	}


	protected void iniDialogView(){
		menuDialog = new Dialog(res.getContext(), R.style.dialog_app_theme);
		menuDialog.setContentView(R.layout.dialog);
		Button bt_supprimer = (Button) menuDialog.findViewById(R.id.bt_supprimer);
		bt_supprimer.setVisibility(View.VISIBLE);
		bt_supprimer.setOnClickListener(AbstractElement.this);
	
	}

	private int pointerCount = 0;

	@Override
	public void onClick(View v) {
		menuDialog.dismiss();
		switch (v.getId()) {

		case R.id.bt_supprimer:
			remove();
			break;
		}
	}


	public void remove() {
		res.getContext().runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				((Scene) getParent()).unregisterTouchArea(AbstractElement.this);
				for (int i =0; i<getChildCount(); i++){
					getChildByIndex(i).dispose() ;
				}
				detachSelf();

			}
		});

	}


	@Override
	public boolean onAreaTouched(TouchEvent event,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {


		int myEventAction = event.getAction(); 

		//	float X = event.getX();
		//	float Y = event.getY();


		switch (myEventAction) {
		case TouchEvent.ACTION_DOWN:
			touchRound.setPosition(pTouchAreaLocalX-touchRound.getWidth()/2, pTouchAreaLocalY-touchRound.getHeight()/2);
			
			pointerCount=Math.min(pointerCount+1, 2);
			setZIndex(ConstantsAndroid.ZINDEX++);
			getParent().sortChildren(false);
			break;
		case TouchEvent.ACTION_MOVE: {


			break;}
		case TouchEvent.ACTION_UP:
		case TouchEvent.ACTION_CANCEL:
		case TouchEvent.ACTION_OUTSIDE:
			pointerCount=Math.max(pointerCount-1, 0);
			if(shortClick&&pointerCount==0){
				onShortClick();
			}
			break;
		}




		this.mPinchZoomDetector.onTouchEvent(event);
		if(pointerCount>1) {
			mHoldDetector.reset();
			//this.mHoldDetector.setEnabled(false);
			this.mScrollDetector.setEnabled(false);
			if(event.getAction()== TouchEvent.ACTION_DOWN) {
				this.mScrollDetector.setEnabled(true);
				//this.mHoldDetector.setEnabled(true);
			}

			this.mScrollDetector.onTouchEvent(event);
			//this.mHoldDetector.onTouchEvent(event);

		} else {
			if(event.getAction()== TouchEvent.ACTION_DOWN) {
				this.mScrollDetector.setEnabled(true);
				this.mHoldDetector.setEnabled(true);
			}

			this.mScrollDetector.onTouchEvent(event);
			this.mHoldDetector.onTouchEvent(event);
		}



		return true;

	}



	protected void onShortClick() {
		
	}






	@Override
	public void onHoldStarted(HoldDetector pHoldDetector, int pPointerID,
			float pHoldX, float pHoldY) {
		mHoldDetector.reset();

	}


	@Override
	public void onHold(HoldDetector pHoldDetector, long pHoldTimeMilliseconds,
			int pPointerID, float pHoldX, float pHoldY) {
	}


	@Override
	public void onHoldFinished(HoldDetector pHoldDetector,
			long pHoldTimeMilliseconds, int pPointerID, float pHoldX,
			float pHoldY) {
	}

	protected float iniScaleX, iniScaleY;
	protected GPoint pt1 = new GPoint(-1, -1), pt2=new GPoint(-1, -1);
	private int iniFingerID = -1;
	protected float iniAngle;
	private boolean isRot = false;
	private float[] iniScaleLocalCoord1;
	private float[] iniScaleLocalCoord2;
	private float iniDistX;
	private float iniDistY;
	protected boolean isResizable = false;



	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {

		iniScaleX = this.getScaleX();
		iniScaleY = this.getScaleY();
		iniAngle = this.getRotation();
		iniFingerID = pSceneTouchEvent.getPointerID();
		pt1.update(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		iniScaleLocalCoord1 = MathUtils.revertRotateAroundCenter(pt1.toArray(), this.getRotation(), 
				getX(), getY());
	}


	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {



		if(pointerCount>1){
			//this.setScale(iniScaleX*pZoomFactor, iniScaleY*pZoomFactor);
			if(pTouchEvent.getPointerID()==iniFingerID){
				pt1.x = (int) pTouchEvent.getX();
				pt1.y = (int) pTouchEvent.getY();

			}else{
				pt2.x = (int) pTouchEvent.getX();
				pt2.y = (int) pTouchEvent.getY();
				if(!isRot ){
					iniAngle = getRotation()-GPoint.AngleBetween(pt1, pt2);
					isRot=true;
					iniScaleLocalCoord2 = MathUtils.revertRotateAroundCenter(new float[]{pt2.x, pt2.y}, this.getRotation(), 
							getX(), getY());
					iniDistX=Math.abs(iniScaleLocalCoord1[0]-iniScaleLocalCoord2[0]);
					iniDistY=Math.abs(iniScaleLocalCoord1[1]-iniScaleLocalCoord2[1]);
				} 		
			}
			if(isRot){
				setRotation(iniAngle + GPoint.AngleBetween(pt1, pt2));

				if(isResizable ){
					float[] scaleLocalCoord1 = MathUtils.revertRotateAroundCenter(new float[]{pt1.x, pt1.y}, this.getRotation(), 
							getX(), getY());
					float[] scaleLocalCoord2 = MathUtils.revertRotateAroundCenter(new float[]{pt2.x, pt2.y}, this.getRotation(), 
							getX(), getY());

					//this.convertSceneToLocalCoordinates(pt1.x, pt1.y).clone();
					//float[] scaleLocalCoord2 = this.convertSceneToLocalCoordinates(pt2.x, pt2.y).clone();
					float distX=Math.abs(scaleLocalCoord1[0]-scaleLocalCoord2[0]);
					float distY=Math.abs(scaleLocalCoord1[1]-scaleLocalCoord2[1]);
					if(iniDistY<50)
						distY=iniDistY;
					else if(iniDistX<50)
						distX=iniDistX;
					this.setScale(iniScaleX*Math.abs(distX/iniDistX),
							iniScaleY*Math.abs(distY/iniDistY)); 
				}else{
					this.setScale(iniScaleX*pZoomFactor,
							iniScaleY*pZoomFactor); 
				}
			}
		}
	}


	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		isRot=false;
	}







	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		onScroll(pScollDetector, pPointerID, pDistanceX, pDistanceY);
	}


	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {

		this.setPosition(this.getX()+pDistanceX, this.getY()+pDistanceY);
	}


	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		onScroll(pScollDetector, pPointerID, pDistanceX, pDistanceY);

		if(pointerCount==0){
			((MainScene) getParent()).verifyRoundButton(this);
		}
	}



}
