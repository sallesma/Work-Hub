package com.workhub.android.utils;

import org.andengine.util.math.MathUtils;



public class GPoint {
	public float x; 
	public float y;
	
	public GPoint(float f, float g){
		this.x = f;
		this.y = g;
		
	}
	
	public void update(float x2, float y2) {
		this.x = x2;
		this.y = y2;
	}
	public static GPoint milieu(GPoint pt1, GPoint pt2){
		return new GPoint((pt1.x+pt2.x)/2, (pt1.y+pt2.y)/2);
		
	}
	
	public void Maximum(float i) {
		float puissance = (float) Math.pow(Math.pow(this.x, 2.0f)+Math.pow(this.y,2.0f), 0.5f);
		if(puissance>i){
			float x = i/puissance;
			this.update(this.x*x, this.y*x);
		}
	}
	
	public void Minimum(float i) {
		float puissance = (float) Math.pow(Math.pow(this.x, 2.0f)+Math.pow(this.y,2.0f), 0.5f);
		if(puissance<i){
			float x = i/puissance;
			this.update(this.x*x, this.y*x);
		}
	}
	
    public static GPoint Add(final GPoint v1, final GPoint v2) {
    	return new GPoint(v1.x + v2.x,v1.y + v2.y);
    }
    
   

    public static GPoint Sub(final GPoint v1, final GPoint v2) {
    	return new GPoint(v1.x - v2.x,v1.y - v2.y);
    }
   
    public static GPoint Mult(final GPoint v, final float f) {
    	return new GPoint(v.x * f, v.y * f);
    }
    
    public static GPoint ConvertCoord(GPoint coordIni, GPoint posScene, float angle) {
    	GPoint coord = GPoint.Sub(coordIni, posScene);
    	float[] p = {coord.x, coord.y};
    	p = MathUtils.rotateAroundCenter(p, -angle, 0, 0);
    	coord.update(p[0], p[1]);
    	return coord;
    }

   
	public static GPoint Perp(final GPoint v) {
        return new GPoint(-v.y, v.x);
    }

    public static float Length(final GPoint v) {
        return (float) Math.sqrt(v.x*v.x+v.y*v.y);
    }

    public static float Distance(final GPoint v1, final GPoint v2) {
        return Length(Sub(v1, v2));
    }
    
    public static GPoint Normalize(final GPoint v) {
        return Mult(v, 1.0f / Length(v));
    }

    public static GPoint VectRadian(final float a) {
        return new GPoint((float)Math.cos(a), (float)Math.sin(a));
    }

    public static GPoint VectDegre(final float a) {
        return new GPoint((float)Math.cos(Math.toRadians(a)), (float)Math.sin(Math.toRadians(a)));
    }
    
    public static float AngleBetween(final GPoint v1, final GPoint v2) {
        return ToDegre(Sub(v1, v2));
    }
    
   public static float ToRadian(final GPoint v) {
        return (float) Math.atan2(v.y, v.x);
    }
   
   public static float ToDegre(final GPoint v) {
       return (float) Math.toDegrees(ToRadian(v));
   }

public static GPoint getBaryCentre(GPoint pt1, GPoint pt2, float val1) {
	return new GPoint(pt1.x*val1+pt2.x*(1.0f-val1), pt1.y*val1+pt2.y*(1.0f-val1));
}




    
}
