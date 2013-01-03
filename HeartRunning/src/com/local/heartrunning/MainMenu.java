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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        // Load GUI Components
        startRunningButton = (Button)findViewById(R.id.menu_start_running_button);
        startRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadStartRunning();			
			}
		});
        
        if (MainActivity.facebookUser != null) {
        	TextView welcome = (TextView) findViewById(R.id.textView1);
        	welcome.setText("Let's get going, " + MainActivity.facebookUser.getName() + "!");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    public void loadStartRunning() {
    	Intent intentStartRunning = new Intent(this,RunningView.class);
  		startActivity(intentStartRunning);	
    }

}
