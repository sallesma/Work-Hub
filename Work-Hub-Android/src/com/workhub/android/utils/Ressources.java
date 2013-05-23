package com.workhub.android.utils;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.workhub.android.activity.HomeActivity;

public class Ressources {


	private BitmapTextureAtlas mainTextureAtlas;
	private HomeActivity context;
	private TextureRegion TR_Rond;
	private Font mFont;
	private GPoint screenCenter;
	private float sceneWidth;
	private float sceneHeight;
	private TextureRegion TR_No_Image;
	private ITextureRegion TR_Logo;

	public Ressources(HomeActivity context, Camera mCamera) {
		this.context = context;
		this.screenCenter = new GPoint(mCamera.getCenterX(), mCamera.getCenterY());
		sceneHeight = mCamera.getHeight();
		sceneWidth = mCamera.getWidth();
		this.mFont = FontFactory.create(context.getFontManager(), context.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mainTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
		context.getTextureManager().loadTexture(this.mainTextureAtlas);

		BuildableBitmapTextureAtlas mBuildableTexture = new BuildableBitmapTextureAtlas(context.getTextureManager(), 1024, 2048, TextureOptions.BILINEAR);

		TR_Rond=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTexture, context, "rond.png");
		TR_No_Image=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTexture, context, "default.png");
		TR_Logo=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTexture, context, "logoWH.png");

		try {
			mBuildableTexture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(2, 2, 2));
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		mBuildableTexture.load();
	}

	
	
	
	 
	public TextureRegion getTR_No_Image() {
		return TR_No_Image;
	}
	
	public TextureRegion getTR_Rond() {
		return TR_Rond;
	}

	public HomeActivity getContext() {
		return context;
	}
	public Font getFont() {
		return mFont;

	}


	public GPoint getScreenCenter() {
		return screenCenter;
	}

	public float getSceneWidth() {
		return sceneWidth;
	}
	public float getSceneHeight() {
		return sceneHeight;
	}





	public ITextureRegion getTR_Logo() {
		return TR_Logo;
	}

	
}
