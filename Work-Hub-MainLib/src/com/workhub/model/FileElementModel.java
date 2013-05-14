package com.workhub.model;

import jade.core.AID;

import java.io.File;

import com.workhub.utils.Constants;


public class FileElementModel extends ElementModel {
	private File content;
	
	
	public FileElementModel(int color, String title, AID agent, File file) {
		super(color, title, agent);
		this.content = file;
	}


	public File getContent() {
		return content;
	}

	public void setContent(File content) {
		this.content = content;
	}


	@Override
	public int getType() {
		return Constants.TYPE_ELEMENT_FILE;
	}
	
	
}
