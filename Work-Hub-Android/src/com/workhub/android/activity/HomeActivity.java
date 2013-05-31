package com.workhub.android.activity;

import jade.android.AgentContainerHandler;
import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.android.RuntimeService;
import jade.core.AID;
import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.gui.GuiEvent;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;

import com.workhub.android.R;
import com.workhub.android.element.AbstractElement;
import com.workhub.android.element.BaseElement;
import com.workhub.android.element.PictureElement;
import com.workhub.android.scene.MainScene;
import com.workhub.android.utils.ConstantsAndroid;
import com.workhub.android.utils.Ressources;
import com.workhub.jade.agent.ClientAgent;
import com.workhub.jade.agent.ClientAgentInterface;
import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;

public class HomeActivity extends SimpleLayoutGameActivity implements PropertyChangeListener{

	private Camera mCamera;
	private Ressources res;
	private ServiceConnection serviceConnection;
	private MicroRuntimeServiceBinder microRuntimeServiceBinder;

	private Logger logger = Logger.getJADELogger(this.getClass().getName());
	private String agentName;

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {


		super.onCreate(pSavedInstanceState);
		//startJade("Florian","192.168.43.238", "1099",agentStartupCallback );
		agentName = "Florian";
		startJade(agentName, AndroidHelper.getLocalIPAddress(), "1099",agentStartupCallback );

	}

	public ClientAgentInterface getAgentInterface(){
		try {
			AgentController agentC = MicroRuntime.getAgent(agentName);
			return (ClientAgentInterface) agentC.getO2AInterface(ClientAgentInterface.class);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		return null;
	} 

	private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
		@Override
		public void onSuccess(AgentController agent) {
		}

		@Override
		public void onFailure(Throwable throwable) {
			System.out.println("Nickname already in use!");
		}
	};
	private AbstractElement askElement;
	private MainScene scene;

	public void startJade(final String nickname, final String host,
			final String port,
			final RuntimeCallback<AgentController> agentStartupCallback) {

		final Properties profile = new Properties();
		profile.setProperty(Profile.MAIN_HOST, host);
		profile.setProperty(Profile.MAIN_PORT, port);
		profile.setProperty(Profile.MAIN, Boolean.TRUE.toString());
		profile.setProperty(Profile.JVM, Profile.ANDROID);

		if (AndroidHelper.isEmulator()) {
			// Emulator: this is needed to work with emulated devices
			profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
		} else {
			profile.setProperty(Profile.LOCAL_HOST,
					AndroidHelper.getLocalIPAddress());
		}
		// Emulator: this is not really needed on a real device
		profile.setProperty(Profile.LOCAL_PORT, "1099");

		if (microRuntimeServiceBinder == null) {
			serviceConnection = new ServiceConnection() {
				public void onServiceConnected(ComponentName className,
						IBinder service) {
					microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
					System.out.println("Gateway successfully bound to MicroRuntimeService");
					startContainer(nickname, profile, agentStartupCallback);
				};

				public void onServiceDisconnected(ComponentName className) {
					microRuntimeServiceBinder = null;
					System.out.println("Gateway unbound from MicroRuntimeService");
					finish();
				}
			};
			System.out.println( "Binding Gateway to MicroRuntimeService...");
			bindService(new Intent(getApplicationContext(),
					MicroRuntimeService.class), serviceConnection,
					Context.BIND_AUTO_CREATE);
		} else {
			System.out.println( "MicroRumtimeGateway already binded to service");
			startContainer(nickname, profile, agentStartupCallback);
		}
	}

	private void startContainer(final String nickname, final Properties profile,
			final RuntimeCallback<AgentController> agentStartupCallback) {

		if (!MicroRuntime.isRunning()) {
			RuntimeService runtimeService = new RuntimeService();
			runtimeService.createMainAgentContainer(new RuntimeCallback<AgentContainerHandler>() {

				@Override
				public void onSuccess(AgentContainerHandler arg0) {
					System.out.println("Successfully start of the container...");
					//startAgent(nickname, agentStartupCallback);
					microRuntimeServiceBinder.startAgentContainer(profile, 
							new RuntimeCallback<Void>() {
						@Override
						public void onSuccess(Void thisIsNull) {
							System.out.println("Successfully start of the container...");
							startAgent(nickname, agentStartupCallback);
						}

						@Override
						public void onFailure(Throwable throwable) {
							System.out.println( "Failed to start the container...");
							finish();
						}
					});

				}

				@Override
				public void onFailure(Throwable arg0) {
					finish();

				}
			});

		} else {
			startAgent(nickname, agentStartupCallback);
		}
	}

	private void startAgent(final String nickname,
			final RuntimeCallback<AgentController> agentStartupCallback) {
		microRuntimeServiceBinder.startAgent(nickname,
				ClientAgent.class.getName(),
				new Object[] { this },
				new RuntimeCallback<Void>() {
			@Override
			public void onSuccess(Void thisIsNull) {
				System.out.println("Successfully start of the "
						+ ClientAgent.class.getName() + "...");

			}

			@Override
			public void onFailure(Throwable throwable) {
				System.out.println("Failed to start the "
						+ ClientAgent.class.getName() + "...");
				agentStartupCallback.onFailure(throwable);
				finish();
			}
		});
	}





	@Override
	public EngineOptions onCreateEngineOptions() {
		int CAMERA_LARGEUR = getResources().getDisplayMetrics().widthPixels;
		int CAMERA_HAUTEUR = getResources().getDisplayMetrics().heightPixels;

		int resolutionX= (int) (ConstantsAndroid.SCREEN_WIDTH);
		int resolutionY= (int) (ConstantsAndroid.SCREEN_HEIGHT);
		float x = Math.min(((float)resolutionX/CAMERA_LARGEUR), ((float)resolutionY/CAMERA_HAUTEUR));
		resolutionX = (int) (CAMERA_LARGEUR*x);
		resolutionY = (int) (CAMERA_HAUTEUR*x);
		mCamera = new Camera(0, 0, resolutionX, resolutionY);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(CAMERA_LARGEUR, CAMERA_HAUTEUR), mCamera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {

		res = new Ressources(this, mCamera);
	}

	@Override
	protected Scene onCreateScene() {

		scene = new MainScene(res); 
		scene.populate();
		return scene;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.main_activity;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.layout_rendersurfaceview;
	}

	public void loadImage(AbstractElement e, int requestCode) {
		this.askElement = e;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, requestCode);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == PictureElement.SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				String imgPath;
				if(selectedImageUri.toString().startsWith("file://")) {
					imgPath = selectedImageUri.toString().replaceAll("file://", "");
				} else {
					imgPath = getPath(selectedImageUri);
				}
				((PictureElement)askElement).onActivityResult(imgPath);

			}
		}
		if(askElement!=null){
			askElement = null;
		}
	}

	public String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	public void sendElement(AID dest, AID elementAgent){
		Object[] param = {dest , elementAgent};

		GuiEvent event = new GuiEvent(param,Constants.EVENT_TYPE_SEND);
		getAgentInterface().fireOnGuiEvent(event);
	}

	public void createElement(int elementType){
		GuiEvent event = new GuiEvent(elementType,Constants.EVENT_TYPE_CREATE_ELEMENT);
		getAgentInterface().fireOnGuiEvent(event);
	}

	public void deleteElement(AID elementAgent){
		GuiEvent event = new GuiEvent(elementAgent,Constants.EVENT_TYPE_DELETE);
		getAgentInterface().fireOnGuiEvent(event);
	}

	public void saveElement(ElementModel model){
		GuiEvent event = new GuiEvent(model, Constants.EVENT_TYPE_SAVE);
		getAgentInterface().fireOnGuiEvent(event);
	}

	public void getElement(AID agentAID ){
		GuiEvent event = new GuiEvent(agentAID, Constants.EVENT_TYPE_CHARGE);
		getAgentInterface().fireOnGuiEvent(event);
	}

	public void getElementList(){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_GET_ELEMENTS);
		getAgentInterface().fireOnGuiEvent(event);
	}

	public void getNeightbourgList(){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_GET_NEIGHBOURGS);
		getAgentInterface().fireOnGuiEvent(event);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		switch (((Integer)event.getPropagationId())) {
		case Constants.EVENT_TYPE_CHANGE:
		{
			AID aidModel = (AID)event.getNewValue();
			BaseElement element = scene.getElement(aidModel);
			if(element!=null){
				getElement(aidModel);
			}
			break;
		}
		case Constants.EVENT_TYPE_CONTENU:
		{
			ElementModel model = (ElementModel)event.getNewValue();
			BaseElement element = scene.getElement(model.getAgent());
			if(element!=null){
				element.setModel(model);
			}else{
				try {
					scene.addElement(model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
		case Constants.EVENT_TYPE_DIED:
		{
			ElementModel model = (ElementModel)event.getNewValue();
			BaseElement element = scene.getElement(model.getAgent());
			if(element!=null){
				element.remove();
			}
			break;
		}
		case Constants.EVENT_TYPE_ELEMENTS:
		{
			Map<AID, String> map = (Map<AID, String>)event.getNewValue();
			//TODO

			break;
		}
		case Constants.EVENT_TYPE_NEIGHBOURS:
		{
			Map<AID, String> map = (Map<AID, String>)event.getNewValue();
			//TODO

			break;
		}
		}

	}

}
