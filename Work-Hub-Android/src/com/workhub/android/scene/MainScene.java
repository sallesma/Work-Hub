package com.workhub.android.scene;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ContinuousHoldDetector;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.element.AbstractElement;
import com.workhub.android.element.BaseElement;
import com.workhub.android.element.GroupElement;
import com.workhub.android.element.PictureElement;
import com.workhub.android.element.RoundButtonElement;
import com.workhub.android.element.TextElement;
import com.workhub.android.utils.Constants;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;
import com.workhub.model.PictureElementModel;
import com.workhub.model.TextElementModel;

public class MainScene extends Scene implements IOnSceneTouchListener, IHoldDetectorListener, IScrollDetectorListener, OnClickListener{

	private ContinuousHoldDetector mHoldDetector;
	private ScrollDetector mScrollDetector;
	private Ressources res;
	private GroupElement groupElement;
	private Runnable groupRunnable;

	private Dialog currentDialog;


	public MainScene(Ressources res) {
		this.res = res;
		mHoldDetector = new ContinuousHoldDetector(this);
		mHoldDetector.setTriggerHoldMinimumMilliseconds(600);
		this.registerUpdateHandler(mHoldDetector);
		mScrollDetector = new ScrollDetector(this);

		groupRunnable = new Runnable() {
			@Override
			public void run() {
				if(groupElement!=null){
					if(!groupElement.isInitialize()){


						ArrayList<BaseElement> list = new ArrayList<BaseElement>();
						for (int i = getChildCount()-1; i >=0; i--) {
							if(getChildByIndex(i) instanceof BaseElement){
								if(groupElement.collideWith((BaseElement) getChildByIndex(i))){
									list.add((BaseElement) getChildByIndex(i));
								}


							}
						}


						if(list.size()>1){
							groupElement.initialize(list);
							groupElement.setZIndex(Constants.ZINDEX++);
							registerTouchArea(groupElement);
							sortChildren(false);
						}else{
							groupElement.detachSelf();
						}

					}else{

						unregisterTouchArea(groupElement);
						groupElement.detachSelf();
						groupElement=null;
					}
				}}};


	}

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
		Boolean b = mChildrenSortPending;
		super.onManagedDraw(pGLState, pCamera);





		SmartList<ITouchArea> list = getTouchAreas();
		if(b){
			//IEntity entity = (IEntity) getTouchAreas().get(0);
			list.clear();
			for (int i = 0; i<getChildCount(); i ++) {
				if(getChildByIndex(i) instanceof AbstractElement){
					list.add((ITouchArea) getChildByIndex(i));
				}
			}
			//ZIndexSorter.getInstance().sort((SmartList<IEntity>) getTouchAreas());
		}

	}
	public void populate() {


		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setOnAreaTouchTraversalFrontToBack();
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.setOnSceneTouchListener(this);
		float width = res.getSceneWidth();
		float height = res.getSceneHeight();

		RoundButtonElement rb = new RoundButtonElement(width-width/12, -width/20, RoundButtonElement.TYPE_SUPPRIMER, res );
		this.attachChild(rb);
		this.registerTouchArea(rb);
		rb = new RoundButtonElement(width-width/12, height+width/20, RoundButtonElement.TYPE_ENVOYER, res );
		this.attachChild(rb);
		this.registerTouchArea(rb);
		rb = new RoundButtonElement(0+width/12, height+width/20, RoundButtonElement.TYPE_RECEVOIR, res );
		this.attachChild(rb);
		this.registerTouchArea(rb);  

		TextElementModel txtM = new TextElementModel(0,"", null, "" );
		txtM.setContent("tQu'est-ce que qsdf.org ? QSDF.ORG est un domaine � usage initialement personnel. Les services ou sites webs li�s � qsdf.");
		txtM.setTitle("titre de l'element txt");
		TextElement txt = new TextElement(txtM, 200, 200, res);
		this.registerTouchArea(txt);
		this.attachChild(txt);

		PictureElementModel pm = new PictureElementModel(0,"Image Element", null, null);

		PictureElement txt1 = new PictureElement(pm, 100, 200, res);
		this.registerTouchArea(txt1);
		this.attachChild(txt1);

		//		this.mHullVertices = new float[this.mMeshVertices.length];
		//		System.arraycopy(this.mMeshVertices, 0, this.mHullVertices, 0, this.mMeshVertices.length);
		//		this.mHullVertexCount = new JarvisMarch().computeHull(this.mHullVertices, this.mMeshVertexCount, 0, 1, 3);
		//
		//		this.mHull = new Mesh(centerX, centerY, this.mHullVertices, this.mHullVertexCount, DrawMode.LINE_LOOP, this.getVertexBufferObjectManager(), DrawType.STATIC);
		//		this.mHull.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(1, 0.95f, 1.05f), new ScaleModifier(1, 1.05f, 0.95f))));
		//		this.mHull.setColor(Color.RED);



	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent event) {
		int myEventAction = event.getAction(); 

		final float X = event.getX();
		final float Y = event.getY();

		switch (myEventAction) {
		case TouchEvent.ACTION_DOWN:

			break;
		case TouchEvent.ACTION_MOVE: {
			if(groupElement!=null&&!groupElement.isInitialize()){
				res.getContext().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						groupElement.addMeshVertex(X, Y);
						groupElement.refreshVertexDraw();
					}
				});

			}

			break;}
		case TouchEvent.ACTION_UP:
		case TouchEvent.ACTION_CANCEL:
		case TouchEvent.ACTION_OUTSIDE:

			if(groupElement!=null){
				res.getContext().runOnUpdateThread(groupRunnable);
			}
			break;
		}




		this.mHoldDetector.onTouchEvent(event);
		this.mScrollDetector.onTouchEvent(event);


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
		res.getContext().runOnUiThread(new Runnable() {



			@Override
			public void run() {
				currentDialog = new Dialog(res.getContext(), R.style.dialog_app_theme);
				currentDialog.setContentView(R.layout.dialog_scene_main);
				((Button)currentDialog.findViewById(R.id.bt_clear_all)).setOnClickListener(MainScene.this);
				((Button)currentDialog.findViewById(R.id.bt_nouveau)).setOnClickListener(MainScene.this);
				((Button)currentDialog.findViewById(R.id.bt_importer)).setOnClickListener(MainScene.this);
				currentDialog.show();

			}
		});

	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		groupElement = new GroupElement(res );
		attachChild(groupElement);
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {


	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {



	}

	public void verifyRoundButton(AbstractElement abstractElement) {
		if(abstractElement instanceof RoundButtonElement)
			return;

		for (int i = 0; i < getChildCount(); i++) {

			if(getChildByIndex(i) instanceof RoundButtonElement){
				RoundButtonElement rb = (RoundButtonElement) getChildByIndex(i);
				if(rb.contains(abstractElement.getX(), abstractElement.getY())){
					rb.setActionOn(abstractElement);
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		currentDialog.dismiss();
		switch (v.getId()) {
		case R.id.bt_clear_all:
			res.getContext().runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					MainScene.this.detachChildren();					
				}
			});
			break;

		case R.id.bt_nouveau:
			currentDialog = new Dialog(res.getContext(), R.style.dialog_app_theme);
			currentDialog.setContentView(R.layout.dialog_scene_new);
			((Button)currentDialog.findViewById(R.id.bt_element_texte)).setOnClickListener(MainScene.this);
			((Button)currentDialog.findViewById(R.id.bt_element_lien)).setOnClickListener(MainScene.this);
			((Button)currentDialog.findViewById(R.id.bt_element_fichier)).setOnClickListener(MainScene.this);
			((Button)currentDialog.findViewById(R.id.bt_element_image)).setOnClickListener(MainScene.this);

			currentDialog.show();

			break;

		case R.id.bt_importer:
			//TODO
			break;
		case R.id.bt_element_fichier:
			//TODO
			break;
		case R.id.bt_element_lien:
			//TODO
			break;
		case R.id.bt_element_image:
			//TODO
		{
			PictureElementModel model = new PictureElementModel(0,"", null, null );

			GPoint centre = res.getScreenCenter();
			PictureElement tx = new PictureElement(model, centre.x, centre.y, res);
			attachChild(tx);
			registerTouchArea(tx);
			tx.edit();
		}
		break;
		case R.id.bt_element_texte:
			//TODO la cr�ation doit creer un agent;
			TextElementModel model = new TextElementModel(0,"", null, "" );
			model.setTitle("");
			model.setContent("");
			GPoint centre = res.getScreenCenter();
			TextElement tx = new TextElement(model, centre.x, centre.y, res);
			attachChild(tx);
			registerTouchArea(tx);
			tx.edit();

			break;
		}

	}

}

