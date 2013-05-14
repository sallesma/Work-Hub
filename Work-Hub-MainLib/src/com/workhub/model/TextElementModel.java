package com.workhub.model;

import jade.core.AID;

import com.workhub.utils.Constants;

public class TextElementModel extends ElementModel{

	public TextElementModel(int color, String title, AID agent, String txt) {
		super(color, title, agent);
		this.content = txt;
	}

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	} 
	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_TEXT;
	}
}
