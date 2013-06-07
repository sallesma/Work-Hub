package com.workhub.android.activity;

import jade.android.AndroidHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.workhub.android.R;
import com.workhub.android.utils.SettingsManager;

public class LauncherActivity extends Activity implements OnClickListener {


	private EditText name_field;
	private EditText host_field;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.home);

		

		Button bt_connexion = (Button) findViewById(R.id.bt_connexion);
		bt_connexion.setOnClickListener(this);
		Button bt_heberger = (Button) findViewById(R.id.bt_heberger);
		bt_heberger.setOnClickListener(this);
		
		
		SettingsManager settings = SettingsManager.getInstance(getApplicationContext());
		
		name_field = (EditText) findViewById(R.id.name);
		host_field = (EditText) findViewById(R.id.ipaddress);
		
		if(settings.getHost()!=null){
			host_field.setText(settings.getHost());
		}
		
		if(settings.getNickname()!=null){
			name_field.setText(settings.getNickname());
		}
		
	}

	@Override
	public void onClick(View v) {
		
		String nickName = name_field.getText().toString();
		if(nickName.equals("")){
			Toast.makeText(getApplicationContext(), "Vous avez besoin d'un nom pour rejoindre le hub", Toast.LENGTH_LONG).show();
			return;
		}
		String iphost = host_field.getText().toString();
		SettingsManager settings = SettingsManager.getInstance(getApplicationContext());
		switch (v.getId()) {
		 
		case R.id.bt_connexion:
			settings = SettingsManager.getInstance(getApplicationContext());
			settings.setHost(iphost);
			settings.setNickname(nickName);
			
			break;
		case R.id.bt_heberger:
			
			settings.setHost(AndroidHelper.getLocalIPAddress());
			settings.setNickname(nickName);
			
			break;
		}
		
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.alpha_in, R.anim.no_anim);
		
	}
	

}
