package com.local.heartrunning;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PostRunView extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_run_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_run_view, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
