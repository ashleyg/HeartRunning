package com.local.heartrunning;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity {

	// GUI Components
	Button startRunningButton;
	Button startSlow;
	Button startMedium;
	Button startFast;
	
	public int Speed;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        // Load GUI Components
        //TODO add GUI components for slow medium fast and get the buttons up and passing data
        startRunningButton = (Button)findViewById(R.id.menu_start_running_button);
        startSlow = (Button)findViewById(R.id.menu_start_running_button);
        startMedium = (Button)findViewById(R.id.menu_start_running_button);
        startFast = (Button)findViewById(R.id.menu_start_running_button);
        
        startRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadStartRunning(4);			
			}
		});
        startSlow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadStartRunning(0);			
			}
		});
        startMedium.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadStartRunning(1);			
			}
		});
        startFast.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadStartRunning(2);			
			}
		});
        
        if (User.getInstance().getFacebook() != null) {
        	TextView welcome = (TextView) findViewById(R.id.textView1);
        	welcome.setText("Let's get going, " + User.getInstance().getFacebook().getFirstName() + "!");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    //TODO make speed play audio
    public void loadStartRunning(int speed) {
    	Intent intentStartRunning = new Intent(this,RunningView.class);
  		startActivity(intentStartRunning);	
    }

}
