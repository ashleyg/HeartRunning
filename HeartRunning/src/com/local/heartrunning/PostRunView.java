package com.local.heartrunning;

import java.util.ArrayList;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import android.os.Bundle;
import android.view.Menu;

/**
 * Show the workout on a map
 *
 */

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
	
	/**
	 * Draw the route of the activity on the map
	 */
	private void drawPath() {
		ArrayList<MapDataPoint> dataPoints = RunningView.gps.getMapDataPoints();

		//Make sure we actually have data
		if (!dataPoints.isEmpty()) {
			//Set where the map is centred
			if (dataPoints.get(0).hasLocationData()) {
				mView.getController().setCenter(dataPoints.get(0).getLocationAsGeoPoint());
			}
		}
		//An acceptable zoom level
		mView.getController().setZoom(15);
		
		//Plot the path
		for (int i = 1; i < dataPoints.size(); i++) {
			mView.getOverlays().add(new OverlayPath(dataPoints.get(i-1), dataPoints.get(i)));
		}
		//Force a redraw
		mView.invalidate();
	}
}
