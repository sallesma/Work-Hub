package com.workhub.android.element;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.algorithm.collision.RectangularShapeCollisionChecker;

import android.view.View;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.utils.Constants;
import com.workhub.android.utils.Ressources;

public abstract class BaseElement extends AbstractElement  {
	private static final int WIDTH = Constants.SCREEN_WIDTH*2/3;
	private static final int MARGIN = WIDTH/20;
	protected static final int AUTOWRAP_WIDTH = WIDTH - MARGIN*2;

	private Text mTextTitre;
	private Rectangle body;

	private List<RectangularShape> componentList = new ArrayList<RectangularShape>();

	public BaseElement(float centerX, float centerY, Ressources res) {
		super(centerX, centerY, res);
		initShape(res);

		

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

	protected void initShape(Ressources res){

		body = new Rectangle(0, 0, WIDTH, 0, res.getContext().getVertexBufferObjectManager());
		body.setColor(0.5f, 0.5f, 0);//TODO

		updateElementPosition();
		this.attachChild(body);
		this.mTextTitre = new Text(0, 0, res.getFont(), "Titre test", 1000, new TextOptions(AutoWrap.WORDS, AUTOWRAP_WIDTH,  HorizontalAlign.CENTER),
				res.getContext().getVertexBufferObjectManager());
		addComponent(mTextTitre);
	}

	protected void addComponent(RectangularShape component){
		this.componentList.add(component);
		body.attachChild(component);
	}

	public void updateElementPosition(){
		float posY=MARGIN;
		for (int i = 0; i < componentList.size(); i++) {
			componentList.get(i).setPosition(MARGIN, posY);
			posY = posY+ componentList.get(i).getHeight()+MARGIN;
		}
		body.setPosition(-WIDTH/2, -posY/2);
		body.setHeight(posY+MARGIN);
	}


	@Override
	public boolean contains(float pX, float pY) {
		return body.contains(pX, pY);
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
	public float[] getVerticles() {
		float[] v = new float[4*2];
		RectangularShapeCollisionChecker.fillVertices(body, v);
		return v;
	}
}
