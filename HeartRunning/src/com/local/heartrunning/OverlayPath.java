package com.local.heartrunning;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class OverlayPath extends Overlay {
	
	private GeoPoint from;
	private GeoPoint to;
	
	public OverlayPath(GeoPoint a, GeoPoint b) {
		from = a;
		to = b;
	}
	
//	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
//		
//	}
	
	public boolean draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow, long when) {
		 Projection projection = mapView.getProjection();
		    if (!shadow && from != null && to != null) {
		    	Paint p = new Paint();
		    	p.setColor(Color.BLUE);
		        p.setAntiAlias(true);
		        Point ptFrom = new Point();
		        projection.toPixels(from, ptFrom);
		        Point ptTo = new Point();
		        projection.toPixels(to, ptTo);
		        p.setStrokeWidth(5);
		        p.setAlpha(120);
		        canvas.drawLine(ptFrom.x, ptFrom.y, ptTo.x, ptTo.y, p);
		    }

		    return super.draw(canvas, mapView, shadow, when);
	}
	
//	
//	public boolean onKeyDown(int keyCode, android.view.KeyEvent event, MapView mapView)  {
//		return false;
//	}
//	
//	public boolean onKeyUp(int keyCode, android.view.KeyEvent event, MapView mapView) {
//		return false;
//	}
//	
//	public boolean onTap(GeoPoint p, MapView mapView)  {
//		return false;
//	}
//	
//	public boolean onTouchEvent(android.view.MotionEvent e, MapView mapView) {
//		return false;
//	}
//	
//	public boolean onTrackballEvent(android.view.MotionEvent event, MapView mapView) {
//		return false;
//	}

}
