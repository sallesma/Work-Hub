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


	public AID getAgent() {
		return agent;
	}


	public int[] getColorRGB() {
		int hex = color;
	    int r = (hex & 0xFF0000) >> 16;
	    int g = (hex & 0xFF00) >> 8;
	    int b = (hex & 0xFF);
		return new int[] {r, g, b};
	}

	public void setColorRGB(int r, int g, int b){
		r = (r << 16);
		g = (g << 8);
	    color = (0xFF000000 | r | g | b) ;		
		
	}

	
	
	
	
}
