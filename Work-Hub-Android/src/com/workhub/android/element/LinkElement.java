package com.workhub.android.element;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.workhub.android.R;
import com.workhub.android.utils.Ressources;
import com.workhub.model.LinkElementModel;

public class LinkElement extends TextElement{


	public LinkElement(LinkElementModel model, float centerX, float centerY, Ressources res) {
		super(model, centerX, centerY, res);
		
	}
	
	

	
	@Override
	protected void iniDialogView() {
		super.iniDialogView();
		Button bt_ouvrir = (Button) menuDialog.findViewById(R.id.bt_ouvrir);
		bt_ouvrir.setVisibility(View.VISIBLE);
		bt_ouvrir.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_ouvrir:
			Intent intent1 = new Intent(Intent.ACTION_VIEW);
	    	intent1.setData(Uri.parse(getModel().getContent()));
	    	res.getContext().startActivity(intent1);
			break;	
		}
		super.onClick(v);
	}
	
}