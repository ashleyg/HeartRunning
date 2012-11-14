package com.local.heartrunning;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RunningView extends Activity {

	// GUI Components
	Button finishRunningButton;
	
	// Other stuff
	GPSManager gps;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_view);
        
        // Initialise the GPS view
        gps = new GPSManager(this);
        
        // Load GUI Components
        finishRunningButton = (Button)findViewById(R.id.button_finish_run);
        finishRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopRuning();
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.running_view, menu);
        return true;
    }
    
    public void stopRuning() {
    	Intent intentStopRunning = new Intent(this,PostRunView.class);
    	startActivity(intentStopRunning);
    }
}
