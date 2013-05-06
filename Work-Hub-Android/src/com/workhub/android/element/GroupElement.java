package com.workhub.android.element;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.util.algorithm.collision.ShapeCollisionChecker;
import org.andengine.util.algorithm.hull.JarvisMarch;
import org.andengine.util.color.Color;

import android.view.View;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.utils.Ressources;

public class GroupElement extends AbstractElement  {

	private List<BaseElement> baseElements = new ArrayList<BaseElement>();
	private Mesh mHull;
	private float[] mHullVertices = new float[0];
	private int mHullVertexCount = 0;
	private float[] mMeshVertices = new float[0];
	private int mMeshVertexCount = 0;

	public GroupElement(Ressources res) {
		super(res.getScreenCenter().x,res.getScreenCenter().y, res);



	}
	@Override
	protected void iniDialogView() {
		super.iniDialogView();
		Button bt_masquer = (Button) menuDialog.findViewById(R.id.bt_masquer);
		bt_masquer.setVisibility(View.VISIBLE);
		bt_masquer.setOnClickListener(this);

		Button bt_envoyer_a = (Button) menuDialog.findViewById(R.id.bt_envoyer_a);
		bt_envoyer_a.setVisibility(View.VISIBLE);
		bt_envoyer_a.setOnClickListener(this);
	}


	public void initialize(List<BaseElement> list){
		baseElements = list;
		if(mHull!=null){
			mHull.detachSelf();
		}

	}

	@Override
	public boolean contains(float pX, float pY) {
		for (int i = 0; i < baseElements.size(); i++) {
			if(baseElements.get(i).contains(pX, pY)){
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
	protected void remove() {
		for (int i = 0; i < baseElements.size(); i++) {
			baseElements.get(i).remove();
		}
		baseElements.clear();
		super.remove();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_envoyer_a:
			//TODO
			break;
		case R.id.bt_masquer:
			super.remove();
			break;
		}
		super.onClick(v);
	}
	
	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		for (int i = 0; i < baseElements.size(); i++) {
			baseElements.get(i).onScroll(pScollDetector, pPointerID, pDistanceX, pDistanceY);
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
		return baseElements.size()>0;
	}
}