package com.workhub.model;

import jade.core.AID;

import com.workhub.utils.Constants;

public class PictureElementModel extends ElementModel{
	
	public PictureElementModel(int color, String title, AID agent, byte[] imageByte) {
		super(color, title, agent);
		this.content = imageByte;
		this.type = Constants.TYPE_ELEMENT_PICTURE;
	}

	private byte[] content;
	private int type;
	
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	@Override
	public int getType() {
		return this.type;
	}
	
	
}
