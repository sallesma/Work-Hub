package com.workhub.android.element;

import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.HorizontalAlign;

import android.view.View;
import android.widget.EditText;

import com.workhub.android.R;
import com.workhub.android.utils.Ressources;
import com.workhub.model.TextElementModel;

public class TextElement extends BaseElement{

	private Text mTextContent;

	public TextElement(TextElementModel model, float centerX, float centerY, Ressources res) {
		super(model, centerX, centerY, res , true);
		mTextContent = new Text(0, 0, res.getFont(), getModel().getContent(), 1000, new TextOptions(AutoWrap.WORDS, getScaledAutoWrapMargin(),  HorizontalAlign.LEFT),
				res.getContext().getVertexBufferObjectManager());
		mTextContent.setColor(1, 0, 1);
		
		body.attachChild(mTextContent);
		this.updateView();
	}
	@Override
	public float updateView() {
		float posY = super.updateView()+MARGIN/getScaleY();
		mTextContent.getTextOptions().setAutoWrapWidth(getScaledAutoWrapMargin());
		mTextContent.setScale(1/getScaleX(), 1/getScaleY());
		String content = getModel().getContent();
		mTextContent.setText(content);
		while(posY+ mTextContent.getHeightScaled()>body.getHeightScaled()&&mTextContent.getLines().size()>1){
			
			content = content.substring(0, content.length()-3-mTextContent.getLines().get(mTextContent.getLines().size()-1).toString().length())+"...";
			
			mTextContent.setText(content);
		}
		
		mTextContent.setScaleCenter(0, 0);
		
		mTextContent.setPosition(getMarginX(), posY);
		return posY+ mTextContent.getHeightScaled();
		
	};
	
	

	public TextElementModel getModel(){
		return (TextElementModel) model;
	}
	
	@Override
	protected void iniEditDialog() {
		editDialog.findViewById(R.id.content).setVisibility(View.VISIBLE);
		((EditText)editDialog.findViewById(R.id.content)).setText(getModel().getContent());
	}
	@Override
	protected void saveModel() {
		getModel().setContent(((EditText)editDialog.findViewById(R.id.content)).getText().toString());
	}
	
	
}