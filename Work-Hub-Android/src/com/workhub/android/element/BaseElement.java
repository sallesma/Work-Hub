package com.workhub.android.element;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.algorithm.collision.RectangularShapeCollisionChecker;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.workhub.android.R;
import com.workhub.android.utils.ConstantsAndroid;
import com.workhub.android.utils.Ressources;
import com.workhub.model.ElementModel;

public abstract class BaseElement extends AbstractElement  {
	protected static final int WIDTH = ConstantsAndroid.SCREEN_WIDTH*2/3;
	protected static final int HEIGHT = ConstantsAndroid.SCREEN_WIDTH*4/5;
	protected static final float MARGIN = 25;

	private Text mTextTitre;
	protected Rectangle body;
	protected ElementModel model;
	protected Dialog editDialog;
	private Rectangle contour;

	public BaseElement(ElementModel model, float centerX, float centerY, Ressources res) {
		this(model, centerX, centerY, res, false);
	}
	public BaseElement(ElementModel model, float centerX, float centerY, Ressources res, boolean isResizable) {
		super(centerX, centerY, res, isResizable);
		this.model = model;
		initShape(res);
	}


	protected abstract void iniEditDialog();
	protected abstract void saveModel();

	@Override
	public void setScale(float pScaleX, float pScaleY) {
		super.setScale(pScaleX, pScaleY);
			this.updateView();
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

		Button bt_editer = (Button) menuDialog.findViewById(R.id.bt_editer);
		bt_editer.setVisibility(View.VISIBLE);
		bt_editer.setOnClickListener(this);
	}

	protected float getScaledAutoWrapMargin(){
		return WIDTH*getScaleX()-getMarginX()*2;
	}
	protected float getMarginX(){
		return MARGIN/getScaleX();
	}
	protected void initShape(Ressources res){

		body = new Rectangle(0, 0, WIDTH, HEIGHT, res.getContext().getVertexBufferObjectManager());
		contour = new Rectangle(-2, -2, WIDTH+4, HEIGHT+4, res.getContext().getVertexBufferObjectManager());
		contour.setZIndex(-20);
		contour.setColor(0, 0, 0);
		body.attachChild(contour);
		body.setColor(246/255f, 234/255f, 111/255f);//TODO
		body.setPosition(-WIDTH/2, -HEIGHT/2);

		this.attachChild(body);
		this.mTextTitre = new Text(0, 0, res.getFont(), model.getTitle(), 1000, new TextOptions(AutoWrap.WORDS, getScaledAutoWrapMargin(),  HorizontalAlign.CENTER),
				res.getContext().getVertexBufferObjectManager());
		body.attachChild(mTextTitre);

	}

	//	protected void addComponent(RectangularShape component){
	//		this.componentList.add(component);
	//		body.attachChild(component);
	//	}

	public void setBodyHeight(float pHeight){
		body.setHeight(pHeight);
		contour.setHeight(body.getHeight()+4);
	}
	public float updateView(){




		float posY=MARGIN/getScaleY();
		mTextTitre.getTextOptions().setAutoWrapWidth(getScaledAutoWrapMargin());
		mTextTitre.setText(model.getTitle());
		mTextTitre.setScaleCenter(0, 0);
		mTextTitre.setScale(1/getScaleX(), 1/getScaleY());
		mTextTitre.setPosition(getMarginX(), posY);
		
		return posY+ mTextTitre.getHeightScaled();




	}


	@Override
	public boolean contains(float pX, float pY) {
		return body.contains(pX, pY);
	}


	@Override
	public void onHoldFinished(HoldDetector pHoldDetector, long pHoldTimeMilliseconds, int pPointerID, float pHoldX, float pHoldY) {
		res.getContext().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				menuDialog.show();

			}
		});
	};
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_envoyer_a:
			//TODO
			break;
		case R.id.bt_editer:
			edit();
			break;
		case R.id.bt_masquer:
			masquer();
			break;
		case R.id.bt_ok:
			model.setTitle(((EditText)editDialog.findViewById(R.id.title)).getText().toString());
			saveModel();
			editDialog.dismiss();
			editDialog=null;
			updateView();
			break;	
		case R.id.bt_cancel:
			editDialog.dismiss();
			break;	
		}
		super.onClick(v);
	}

	public void masquer() {
		super.remove();
	}

	public void edit() {
		res.getContext().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				editDialog = new Dialog(res.getContext(), R.style.dialog_app_theme);
				editDialog.setContentView(R.layout.element_dialog);
				((EditText)editDialog.findViewById(R.id.title)).setText(model.getTitle());
				((Button)editDialog.findViewById(R.id.bt_ok)).setOnClickListener(BaseElement.this);
				((Button)editDialog.findViewById(R.id.bt_cancel)).setOnClickListener(BaseElement.this);
				iniEditDialog();
				editDialog.show();
			}
		});

	}



	public float[] getVerticles() {
		float[] v = new float[4*2];
		RectangularShapeCollisionChecker.fillVertices(body, v);
		return v;
	}
	public void moveTo(BaseElement baseElement) {
		this.registerEntityModifier(new MoveModifier(ConstantsAndroid.ANIMATION_DURATION, getX(), 	baseElement.getX(),
				getY(), baseElement.getY(), ConstantsAndroid.EASEFUNCTIONS[0]));
		this.registerEntityModifier(new ScaleModifier(ConstantsAndroid.ANIMATION_DURATION,
				getScaleX(), 	1, getScaleY(), 1, ConstantsAndroid.EASEFUNCTIONS[0]));
	}
}
