package com.local.heartrunning;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * Draws a path between two points to be displayed on a map
 *
 */

public class OverlayPath extends Overlay {
	
	private MapDataPoint from;
	private MapDataPoint to;
	
	/**
	 * Create a new path between a and b
	 * @param a is the point from
	 * @param b is the point to
	 */
	public OverlayPath(MapDataPoint a, MapDataPoint b) {
		from = a;
		to = b;
	}
	
//	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
//		
//	}
	
	/**
	 * Draw a path between the two points
	 */
	public boolean draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow, long when) {
		Projection projection = mapView.getProjection();
		    if (!shadow && from != null && to != null) {
		    	Paint p = new Paint();
		    	p.setColor(chooseColor());
		        p.setAntiAlias(true);
		        Point ptFrom = new Point();
		        projection.toPixels(from.getLocationAsGeoPoint(), ptFrom);
		        Point ptTo = new Point();
		        projection.toPixels(to.getLocationAsGeoPoint(), ptTo);
		        p.setStrokeWidth(5);
		        p.setAlpha(120);
		        canvas.drawLine(ptFrom.x, ptFrom.y, ptTo.x, ptTo.y, p);
		    }

		    return super.draw(canvas, mapView, shadow, when);
	}
	
	/**
	 * Choose the colour depending on BPM to indicate heart zones
	 * TODO: Currently uses a max heart rate of 220, meaning the
	 * person is 0 years old
	 * @return the colour as an int 
	 */
	private int chooseColor() {
		MapDataPoint m;
		//Check whether any bpm data exists
		if (to.hasBPM()) {
			m = to;
		}
		else if (from.hasBPM()) {
			m = from;
		}
		//If we don't find any, go for depressing black for not using our app
		// as intended
		else {
			return Color.BLACK;
		}
		float bpm = m.getBPM();
		//The zone which does nowt
		if (bpm < 110) {
			return Color.BLUE;
		}
		//Fat burning zone
		else if (bpm < 132) {
			return Color.GREEN;
		}
		//Aerobic zone
		else if (bpm < 176) {
			return Color.YELLOW;
		}
		//Anaerobic/Technically dead zone
		else {
			return Color.RED;
		}
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
