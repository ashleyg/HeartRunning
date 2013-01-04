package com.local.heartrunning;

import java.util.ArrayList;

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import android.os.Bundle;
import android.view.Menu;

public class PostRunView extends MapActivity {
	
	private MapView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_run_view);
        
        mView = (MapView) findViewById(R.id.mapview);
        mView.getOverlays().clear();
        mView.setBuiltInZoomControls(true);
        
        drawPath();        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_run_view, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void drawPath() {
		ArrayList<MapDataPoint> dataPoints = RunningView.gps.getMapDataPoints();
		GeoPoint[] points = new GeoPoint[dataPoints.size()];
		
		int i = 0;
		for (MapDataPoint m : dataPoints) {
			int latitude = (int) (m.getLocation().getLatitude() * 1E6);
			int longitude = (int) (m.getLocation().getLongitude() * 1E6);
			points[i] = new GeoPoint(latitude, longitude);
			i++;
		}
		mView.getController().setCenter(points[0]);
		mView.getController().setZoom(15);
		
		for (i = 1; i < points.length; i++) {
			mView.getOverlays().add(new OverlayPath(points[i-1], points[i]));
		}
		mView.invalidate();
	}
}
