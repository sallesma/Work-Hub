package com.workhub.model;

import jade.core.AID;

import java.net.MalformedURLException;
import java.net.URL;

import com.workhub.utils.Constants;

public class LinkElementModel extends TextElementModel{
	
	
	public LinkElementModel(int color, String title, AID agent, String txt) {
		super(color, title, agent, txt);
		this.type = Constants.TYPE_ELEMENT_LINK;
	}

	public boolean isValide(){
		try {
		    URL url = new URL(getContent());
		    //URLConnection conn = url.openConnection();
		    //conn.connect();
		} catch (MalformedURLException e) {
		    return false;
		}
		return true;
	}
}
