package com.workhub.android.activity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.workhub.android.R;
import com.workhub.android.scene.MainScene;
import com.workhub.android.utils.Constants;
import com.workhub.android.utils.Ressources;

public class HomeActivity extends SimpleLayoutGameActivity {

	private Camera mCamera;
	private Ressources res;

	@Override
	public EngineOptions onCreateEngineOptions() {
		int CAMERA_LARGEUR = getResources().getDisplayMetrics().widthPixels;
		int CAMERA_HAUTEUR = getResources().getDisplayMetrics().heightPixels;
		
		int resolutionX= (int) (Constants.SCREEN_WIDTH);
		int resolutionY= (int) (Constants.SCREEN_HEIGHT);
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
		
		MainScene scene = new MainScene(res); 
		scene.populate();
		return scene;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.game_activity;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.layout_rendersurfaceview;
	}
	
}
