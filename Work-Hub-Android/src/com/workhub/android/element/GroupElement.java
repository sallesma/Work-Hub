package com.workhub.android.element;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.util.algorithm.collision.ShapeCollisionChecker;
import org.andengine.util.algorithm.hull.JarvisMarch;
import org.andengine.util.color.Color;

import android.view.View;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.scene.MainScene;
import com.workhub.android.utils.GPoint;
import com.workhub.android.utils.Ressources;
import com.workhub.model.ElementModel;

public class GroupElement extends AbstractElement  {

	private List<BaseElement> baseElements = new ArrayList<BaseElement>();
	private Mesh mHull;
	private float[] mHullVertices = new float[0];
	private int mHullVertexCount = 0;
	private float[] mMeshVertices = new float[0];
	private int mMeshVertexCount = 0;
	private Float[] iniRotation;
	private BaseElement selectedElement;

	public GroupElement(Ressources res) {
		super(res.getScreenCenter().x,res.getScreenCenter().y, res);



	}
	@Override
	protected void iniDialogView() {
		super.iniDialogView();
		
		Button bt_exporter = (Button) menuDialog.findViewById(R.id.bt_exporter);
		bt_exporter.setVisibility(View.VISIBLE);
		bt_exporter.setOnClickListener(this);
		
		
		Button bt_masquer = (Button) menuDialog.findViewById(R.id.bt_masquer);
		bt_masquer.setVisibility(View.VISIBLE);
		bt_masquer.setOnClickListener(this);
		
		Button bt_edit = (Button) menuDialog.findViewById(R.id.bt_editer);
		bt_edit.setVisibility(View.VISIBLE);
		bt_edit.setOnClickListener(this);

		Button bt_envoyer_a = (Button) menuDialog.findViewById(R.id.bt_envoyer_a);
		bt_envoyer_a.setVisibility(View.VISIBLE);
		bt_envoyer_a.setOnClickListener(this);
		
		Button bt_degroupe = (Button) menuDialog.findViewById(R.id.bt_degroupe);
		bt_degroupe.setVisibility(View.VISIBLE);
		bt_degroupe.setOnClickListener(this);
	}
	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		super.onPinchZoomStarted(pPinchZoomDetector, pSceneTouchEvent);
		group();
		
	}
	
	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		super.onPinchZoom(pPinchZoomDetector, pTouchEvent, pZoomFactor);
		
		GPoint vec = GPoint.Sub(pt1, pt2);
		//vec = GPoint.Normalize(vec);
		//vec = GPoint.Mu(vec);
		
		
		
		float size = getBaseElements().size();
		float x = getBaseElements().get(0).getX();
		float y = getBaseElements().get(0).getY();
		for (int i = 0; i < getBaseElements().size(); i++) {
			GPoint pos = GPoint.Mult(vec, i/size);
			getBaseElements().get(i).setPosition(x+pos.x, y+pos.y);
		}
	}
	
	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) 
	{
		super.onPinchZoomFinished(pPinchZoomDetector, pTouchEvent, pZoomFactor);
		iniRotation = null;
	};
	
	@Override
	public void setScale(float pScaleX, float pScaleY) {};

	@Override
	public void setRotation(float pRotation) {
		
		
		float rotation =  GPoint.AngleBetween(pt1, pt2);
		if(iniRotation==null){
			iniRotation = new Float[getBaseElements().size()];
			for (int i = 0; i < getBaseElements().size(); i++) {
				iniRotation[i]= getBaseElements().get(i).getRotation()-rotation;
			}
		}
		
		for (int i = 0; i < getBaseElements().size(); i++) {
			getBaseElements().get(i).setRotation(iniRotation[i]+rotation);
		}
		
	};
	public void initialize(List<BaseElement> list){
		setBaseElements(list);
		if(mHull!=null){
			mHull.detachSelf();
		}
		group();

	}
	
	public void group(){
		for (BaseElement baseElement : getBaseElements()) {
			baseElement.moveTo(getBaseElements().get(0));
		}
	}

	@Override
	public boolean contains(float pX, float pY) {
		for (int i = 0; i < getBaseElements().size(); i++) {
			if(getBaseElements().get(i).contains(pX, pY)){
				selectedElement = getBaseElements().get(i);
				return true;
			}
		}
		return false;
	}


	@Override
	public void onHoldStarted(HoldDetector pHoldDetector, int pPointerID,
			float pHoldX, float pHoldY) {
		super.onHoldStarted(pHoldDetector, pPointerID, pHoldX, pHoldY);
		res.getContext().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				menuDialog.show();

			}
		});

	}
	@Override
	public void remove() { 
		for (int i = 0; i < getBaseElements().size(); i++) {
			getBaseElements().get(i).remove();
		}
		clearGroup();
		super.remove();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_editer:
			if(selectedElement!=null){
				res.getContext().askEdition(selectedElement.getModel().getAgent());
			}
			break;
		case R.id.bt_envoyer_a:
			((MainScene)getParent()).iniNeighboursList(this);
			res.getContext().getNeightbourgList();
			break;
		case R.id.bt_exporter:
			export();
			break;
		case R.id.bt_masquer:
			remove();
			break;
		case R.id.bt_degroupe:
			super.remove();
			break;
		}
		super.onClick(v);
	}
	
	public void export() {
		List<ElementModel> list = new ArrayList<ElementModel>();
		for (int i = 0; i < getBaseElements().size(); i++) {
			list.add(getBaseElements().get(i).getModel());
		}
		res.getContext().export(list);	
	}
	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		for (int i = 0; i < getBaseElements().size(); i++) {
			getBaseElements().get(i).onScroll(pScollDetector, pPointerID, pDistanceX, pDistanceY);
		}
	}
	
	
	
	
	public void addMeshVertex(float pX, float pY) {
		final float[] newMeshVertices = new float[this.mMeshVertices.length + 3];

		System.arraycopy(this.mMeshVertices, 0, newMeshVertices, 0, this.mMeshVertices.length);
		newMeshVertices[newMeshVertices.length - 3] = pX;
		newMeshVertices[newMeshVertices.length - 2] = pY;
		newMeshVertices[newMeshVertices.length - 1] = Color.WHITE_ABGR_PACKED_FLOAT;

		this.mMeshVertices = newMeshVertices;
		this.mMeshVertexCount ++;
	}
	public void refreshVertexDraw() {

		if(mHull!=null){
			mHull.detachSelf();
		}

		mHullVertices = new float[mMeshVertices.length];
		System.arraycopy(mMeshVertices, 0, mHullVertices, 0, mMeshVertices.length);
		mHullVertexCount = new JarvisMarch().computeHull(mHullVertices, mMeshVertexCount, 0, 1, 3);

		mHull = new Mesh(-res.getScreenCenter().x,-res.getScreenCenter().y, mHullVertices, mHullVertexCount, DrawMode.TRIANGLE_FAN, res.getContext().getVertexBufferObjectManager(), DrawType.STATIC);
		//this.mHull.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(1, 0.95f, 1.05f), new ScaleModifier(1, 1.05f, 0.95f))));
		mHull.setColor(Color.RED);
		mHull.setAlpha(0.5f);
		attachChild(mHull);




	}
	
	
	
	public boolean collideWith(BaseElement e) {
		if(mHullVertexCount<3)
			return false;
		
		float[] vertices = new float[mHullVertexCount*2];
		for (int i = 0; i < mHullVertexCount; i++) {
			vertices[i*2]= mHullVertices[i*3];
			vertices[i*2+1]= mHullVertices[i*3+1];
		}
		return ShapeCollisionChecker.checkCollision(vertices, mHullVertexCount, e.getVerticles(), 4);
	}
	public boolean isInitialize() {
		return getBaseElements().size()>0;
	}
	public boolean containsOneOf(ArrayList<BaseElement> list) {
		for (int i = 0; i < getBaseElements().size(); i++) {
			if(list.contains(getBaseElements().get(i))){
				return true;
			}
		}
		return false;
	}
	public void clearGroup() {
		getBaseElements().clear();
		selectedElement = null;
	}
	public List<BaseElement> getBaseElements() {
		return baseElements;
	}
	public void setBaseElements(List<BaseElement> baseElements) {
		this.baseElements = baseElements;
	}
}
