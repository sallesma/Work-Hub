package com.workhub.android.element;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.algorithm.collision.RectangularShapeCollisionChecker;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.workhub.android.R;
import com.workhub.android.scene.MainScene;
import com.workhub.android.utils.ConstantsAndroid;
import com.workhub.android.utils.Ressources;
import com.workhub.model.ElementModel;

public abstract class BaseElement extends AbstractElement  {
	protected final float WIDTH;
	protected final float HEIGHT;
	protected final float MARGIN;

	private Text mTextTitre;
	protected Rectangle body;
	protected ElementModel model;
	protected Dialog editDialog;
	private Rectangle contour;
	private int selectColor;
	private boolean isSaving = false;

	public BaseElement(ElementModel model, float centerX, float centerY, Ressources res) {
		this(model, centerX, centerY, res, false);
	}
	public BaseElement(ElementModel model, float centerX, float centerY, Ressources res, boolean isResizable) {
		super(centerX, centerY, res, isResizable);
		WIDTH = res.toPixel(250);
		HEIGHT = getBodyHeight();
		MARGIN = res.toPixel(10);
		this.model = model;
		initShape(res);
	}


	protected float getBodyHeight() {
		return res.toPixel(250);
	}
	protected abstract void iniEditDialog();
	private void saveModel(){
		isSaving  = true;
		model.setColor(selectColor);

		model.setTitle(((EditText)editDialog.findViewById(R.id.title)).getText().toString());
		saveContent();
		res.getContext().saveElement(model);
	}

	protected abstract void saveContent();

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

		Button bt_exporter = (Button) menuDialog.findViewById(R.id.bt_exporter);
		bt_exporter.setVisibility(View.VISIBLE);
		bt_exporter.setOnClickListener(this);

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

		contour = new Rectangle(res.toPixel(-2),res.toPixel(-2), WIDTH+res.toPixel(4), HEIGHT+res.toPixel(4), res.getContext().getVertexBufferObjectManager());
		contour.setZIndex(-20);
		contour.setColor(0, 0, 0);
		body.attachChild(contour);

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
		contour.setHeight(body.getHeight()+res.toPixel(4));
	}
	public float updateView(){

		int[] colors  = model.getColorRGB();
		body.setColor(colors[0]/255f, colors[1]/255f, colors[2]/255f);
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
			((MainScene)getParent()).iniNeighboursList(this);
			res.getContext().getNeightbourgList();
			break;
		case R.id.bt_editer:
			res.getContext().askEdition(model.getAgent());
			break;
		case R.id.bt_masquer:
			masquer();
			break;
		case R.id.bt_ok:

			saveModel();
			editDialog.dismiss();
			editDialog=null;
			updateView();
			break;	
		case R.id.bt_color:
			AmbilWarnaDialog dialog = new AmbilWarnaDialog(res.getContext(), model.getColor(), new OnAmbilWarnaListener() {
				@Override
				public void onOk(AmbilWarnaDialog dialog, int color) {
					((Button)editDialog.findViewById(R.id.bt_color)).setBackgroundColor(color);
					selectColor = color;
				}

				@Override
				public void onCancel(AmbilWarnaDialog dialog) {
				}
			});

			dialog.show();



			break;
		case R.id.bt_cancel:
			editDialog.dismiss();
			break;	
		case R.id.bt_exporter:
			export();
			break;
		}

		super.onClick(v);
	}

	public void export() {
		List<ElementModel> list = new ArrayList<ElementModel>();
		list.add(getModel());
		res.getContext().export(list);		
	}

	@Override
	public void masquer() {
		super.masquer();
	}


	public void remove() {
		res.getContext().deleteElement(model.getAgent());
		super.masquer();
	};

	public void edit() {
		res.getContext().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				selectColor = model.getColor();
				editDialog = new Dialog(res.getContext(), R.style.dialog_app_theme);
				editDialog.setContentView(R.layout.element_dialog);
				((EditText)editDialog.findViewById(R.id.title)).setText(model.getTitle());
				((Button)editDialog.findViewById(R.id.bt_ok)).setOnClickListener(BaseElement.this);
				((Button)editDialog.findViewById(R.id.bt_cancel)).setOnClickListener(BaseElement.this);

				((Button)editDialog.findViewById(R.id.bt_color)).setOnClickListener(BaseElement.this);

				((Button)editDialog.findViewById(R.id.bt_color)).setBackgroundColor(model.getColor());
				iniEditDialog();

				editDialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						if(!isSaving)
							res.getContext().stopEditing(getModel().getAgent());
						isSaving = false;
					}
				});

				editDialog.show();
			}
		});

	}



	public float[] getVerticles() {
		float[] v = new float[4*2];
		RectangularShapeCollisionChecker.fillVertices(body, v);
		return v;
	}
	public void moveTo(float x, float y) {
		this.registerEntityModifier(new MoveModifier(ConstantsAndroid.ANIMATION_DURATION, getX(), 	x,
				getY(), y, ConstantsAndroid.EASEFUNCTIONS[0]));
		this.registerEntityModifier(new ScaleModifier(ConstantsAndroid.ANIMATION_DURATION,
				getScaleX(), 	1, getScaleY(), 1, ConstantsAndroid.EASEFUNCTIONS[0]));
	}

	public void moveTo(BaseElement baseElement) {
		moveTo(baseElement.getX(), baseElement.getY());
	}
	public ElementModel getModel() {
		return model;
	}
	public void setModel(ElementModel model) {
		this.model = model;
		updateView();
	}
}
