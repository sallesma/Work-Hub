package com.workhub.android.utils;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.widget.ArrayAdapter;

import com.workhub.android.activity.HomeActivity;
import com.workhub.android.element.BaseElement;

public class MyListAdapter extends ArrayAdapter<String> {

	private List<String> mIdMap ;
	private List<AID> mlistAID = new ArrayList<AID>();

	public MyListAdapter(HomeActivity homeActivity, int textViewResourceId,
			List<String> objects) {
		super(homeActivity, textViewResourceId, objects);
		mIdMap = objects;
	}



	@Override
	public long getItemId(int position) {
		//String item = getItem(position);
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}



	public List<AID> getListAID() {
		return mlistAID;
	}
	
	public void addEntry(Entry<AID, String> entry) {
		mlistAID.add(entry.getKey());
		mIdMap.add(entry.getValue());
		
	}



}
