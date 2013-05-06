package com.workhub.android.element;

import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.HorizontalAlign;

import com.workhub.android.utils.Ressources;

public class TextElement extends BaseElement{

	private Text mTextContent;

	public TextElement(float centerX, float centerY, Ressources res) {
		super(centerX, centerY, res);
		
		mTextContent = new Text(0, 0, res.getFont(), "texte de test qui doit faire plus de la moitier de l'écran", 1000, new TextOptions(AutoWrap.WORDS, BaseElement.AUTOWRAP_WIDTH,  HorizontalAlign.LEFT),
				res.getContext().getVertexBufferObjectManager());
		mTextContent.setColor(1, 0, 1);
		
		this.addComponent(mTextContent);
		this.updateElementPosition();
	}
	
}