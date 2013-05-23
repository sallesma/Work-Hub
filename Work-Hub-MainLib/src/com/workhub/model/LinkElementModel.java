package com.workhub.model;

import jade.core.AID;

import java.net.URL;

import com.workhub.utils.Constants;

public class LinkElementModel extends ElementModel{
	private String content;
	
	public LinkElementModel(int color, String title, AID agent, String url) {
		super(color, title, agent);
		this.content = url;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_LINK;
	}
}
