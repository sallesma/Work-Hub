package com.workhub.model;

import jade.core.AID;

import java.net.URL;

import com.workhub.utils.Constants;

public class LinkElementModel extends ElementModel{
	
	public LinkElementModel(int color, String title, AID agent, URL url) {
		super(color, title, agent);
		this.content = url;
	}

	private URL content;

	public URL getContent() {
		return content;
	}

	public void setContent(URL content) {
		this.content = content;
	}
	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_LINK;
	}
}
