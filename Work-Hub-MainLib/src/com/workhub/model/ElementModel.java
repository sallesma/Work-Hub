package com.workhub.model;

import jade.core.AID;

public abstract class  ElementModel {
	private int color;
	private String title = "";
	private AID agent;
	
	
	public ElementModel(int color, String title, AID agent) {
		this.color = color;
		this.title = title;
		this.agent = agent;
	}
	
	
	public abstract int getType();
	
	public int getColor() {
		return color; 
	}
	public void setColor(int color) {
		this.color = color;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
