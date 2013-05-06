package com.workhub.android.element;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ContinuousHoldDetector;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.utils.Constants;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;

public abstract class AbstractElement extends Entity  implements ITouchArea, IHoldDetectorListener, IScrollDetectorListener, IPinchZoomDetectorListener, OnClickListener{


	private PinchZoomDetector mPinchZoomDetector;
	private ScrollDetector mScrollDetector;
	private ContinuousHoldDetector mHoldDetector;
	protected Dialog menuDialog;
	protected Ressources res;

	protected AbstractElement(float centerX, float centerY, final Ressources res){
		super(centerX, centerY);
		this.res=res;
		mPinchZoomDetector = new PinchZoomDetector(this);
		mScrollDetector = new ScrollDetector(this);
		mHoldDetector = new ContinuousHoldDetector(this);
		mHoldDetector.setTriggerHoldMinimumMilliseconds(600);
		res.getContext().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				menuDialog = new Dialog(res.getContext());//,android.R.style.Theme_Dialog);
				//choiceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				menuDialog.setContentView(R.layout.dialog);
				
				
				iniDialogView();
				
			}

			
		});
		
		
		this.registerUpdateHandler(mHoldDetector);
//		this.registerUpdateHandler(new IUpdateHandler() {
//
//			@Override
//			public void reset() {				
//			}
//
//			@Override
//			public void onUpdate(float pSecondsElapsed) {
//				onEntityGroupUpdate(pSecondsElapsed);
//
//			}
//
//
//		});

	}


	protected void iniDialogView(){
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


	protected void remove() {
		res.getContext().runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				((Scene) getParent()).unregisterTouchArea(AbstractElement.this);
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
				pointerCount=Math.min(pointerCount+1, 2);
				setZIndex(Constants.ZINDEX++);
				getParent().sortChildren(false);
				break;
			case TouchEvent.ACTION_MOVE: {


				break;}
			case TouchEvent.ACTION_UP:
			case TouchEvent.ACTION_CANCEL:
			case TouchEvent.ACTION_OUTSIDE:
				pointerCount=Math.max(pointerCount-1, 0);
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
		private GPoint pt1 = new GPoint(-1, -1), pt2=new GPoint(-1, -1);
		private int iniFingerID = -1;
		private float iniAngle;
		private boolean isRot = false;



		@Override
		public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
				TouchEvent pSceneTouchEvent) {
			
			iniScaleX = this.getScaleX();
			iniScaleY = this.getScaleY();
			iniAngle = this.getRotation();
			iniFingerID = pSceneTouchEvent.getPointerID();
			pt1.update(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		}


		@Override
		public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
				TouchEvent pTouchEvent, float pZoomFactor) {
			if(pointerCount>1){
				this.setScale(iniScaleX*pZoomFactor, iniScaleY*pZoomFactor);
				if(pTouchEvent.getPointerID()==iniFingerID){
					pt1.x = (int) pTouchEvent.getX();
					pt1.y = (int) pTouchEvent.getY();

				}else{
					pt2.x = (int) pTouchEvent.getX();
					pt2.y = (int) pTouchEvent.getY();
					if(!isRot ){
						iniAngle = getRotation()-GPoint.AngleBetween(pt1, pt2);
						isRot=true;
					} 
				}
				if(isRot){
					setRotation(iniAngle + GPoint.AngleBetween(pt1, pt2));
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
		}



	}
