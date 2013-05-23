package com.workhub.android.element;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.widget.ImageView;

import com.workhub.android.R;
import com.workhub.android.utils.BitmapTransform;
import com.workhub.android.utils.FileManager;
import com.workhub.android.utils.Ressources;
import com.workhub.android.utils.Size;
import com.workhub.model.PictureElementModel;

public class PictureElement extends BaseElement{

	public static final int SELECT_PICTURE = 100;
	private static final int requiredSize = 512;
	private BitmapTextureAtlas texture;
	private BitmapTextureAtlasSourcePerso bitmapTextureAtlasSourcePerso;
	private Sprite imageSprite;

	public PictureElement(PictureElementModel model, float centerX, float centerY, Ressources res) {
		super(model, centerX, centerY, res , false);

		TextureRegion tr = null;
		if(model.getContent()==null){
			tr = res.getTR_No_Image();

		}else{
			iniTexture();
			Bitmap b=null;
			//model.getContent()//TODO
			tr = getTextureRegion(b);
		}
		;
		imageSprite = new Sprite(0, 0, tr, res.getContext().getVertexBufferObjectManager());
		body.attachChild(imageSprite);
		this.updateView();
	}
	private TextureRegion getTextureRegion(Bitmap b) {
		bitmapTextureAtlasSourcePerso = new BitmapTextureAtlasSourcePerso(b);
		return TextureRegionFactory.createFromSource(texture, bitmapTextureAtlasSourcePerso , 0, 0, false);
	}
	
	private void iniTexture(){
		if(texture!=null){
			texture.clearTextureAtlasSources();
			bitmapTextureAtlasSourcePerso.recycle();
			bitmapTextureAtlasSourcePerso= null;
		}else{
			texture = new BitmapTextureAtlas(res.getContext().getTextureManager(), requiredSize, requiredSize, TextureOptions.BILINEAR);
			res.getContext().getEngine().getTextureManager().loadTexture(texture);
		}
		
			
		
			
	}
	
	public void changeImage(final Bitmap b){
		res.getContext().runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				imageSprite.dispose();
				imageSprite.detachSelf();
				
				
				iniTexture();
				TextureRegion tr = getTextureRegion(b);
				texture.load();
				imageSprite = new Sprite(0, 0,b.getWidth(), b.getHeight(), tr, res.getContext().getVertexBufferObjectManager());
				body.attachChild(imageSprite);
				updateView();
			}
		});
		res.getContext().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				((ImageView)editDialog.findViewById(R.id.img_content)).setImageBitmap(b);
				
			}
		});
	}
	
	
	@Override
	public float updateView() {
		float posY = super.updateView()+MARGIN/getScaleY();
		
		
		float rap = imageSprite.getWidth()/imageSprite.getHeight();
		
		float height = (body.getWidth()-2*MARGIN)/rap;
		imageSprite.setHeight(height);
		imageSprite.setWidth((body.getWidth()-2*MARGIN));
		//imageSprite.setScaleCenter(0, 0);
		//imageSprite.setScale(rap);
		
		imageSprite.setPosition(MARGIN, posY);
		posY =posY+ imageSprite.getHeight();
		setBodyHeight(posY+MARGIN);
		//body.setWidth(imageSprite.getWidth());
		return posY;

	};



	public PictureElementModel getModel(){
		return (PictureElementModel) model;
	}

	@Override
	protected void iniEditDialog() {
		editDialog.findViewById(R.id.img_content).setVisibility(View.VISIBLE);
		editDialog.findViewById(R.id.img_content).setOnClickListener(this);
		if(bitmapTextureAtlasSourcePerso!=null){
			((ImageView)editDialog.findViewById(R.id.img_content)).setImageBitmap(bitmapTextureAtlasSourcePerso.getBitmap());
		}

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_content:

			res.getContext().loadImage(this, SELECT_PICTURE);
			break;
		case R.id.bt_cancel:
			editDialog.dismiss();
			break;	
		}

		super.onClick(v);



	}
	public void onActivityResult(String imgPath) {
		try {
			Size bitmapSize = FileManager.getBitmapSize(imgPath);


			float sampleScale=1;
			float size=bitmapSize.height;
			if(bitmapSize.width>bitmapSize.height)
				size = bitmapSize.width;
			
			if(size > requiredSize){


				while(true){
					sampleScale++;
					if(size/sampleScale<requiredSize)
						break;

				}
			}
			
			Bitmap bitmap;
			
			bitmap = FileManager.readBitmapFile(imgPath, (int) sampleScale);
			
			float rap =((float) bitmap.getWidth())/bitmap.getHeight();
			
			int height = requiredSize;
			int weight = requiredSize;
			if(rap>1){
				height = (int) (requiredSize/rap);
			}else{
				weight = (int) (requiredSize*rap);
			}
			
			
			
			bitmap = BitmapTransform.resize(bitmap, weight, height);
			size = bitmap.getWidth();
			changeImage(bitmap);
			


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void saveModel() {
//TODO
	}
	
	@Override
	public void remove() {
		super.remove();
		if(texture!=null){
			texture.unload();
		}
		if(bitmapTextureAtlasSourcePerso!=null){
			bitmapTextureAtlasSourcePerso.recycle();
		}
	}
	
	class BitmapTextureAtlasSourcePerso extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource
	{
		private Bitmap mBitmap;

		public BitmapTextureAtlasSourcePerso(Bitmap pBitmap) {
			super(0,0, pBitmap.getWidth(), pBitmap.getHeight());
			this.mBitmap = pBitmap;
		}




		public void recycle() {
			mBitmap.recycle();

		}




		public Bitmap getBitmap() {
			return mBitmap;
		}




		@Override
		public Bitmap onLoadBitmap(Config pBitmapConfig)
		{
			return mBitmap.copy(Bitmap.Config.ARGB_8888, false);
		}

		@Override
		public IBitmapTextureAtlasSource deepCopy() {
			return new BitmapTextureAtlasSourcePerso(Bitmap.createBitmap(mBitmap));
		}
	}


}