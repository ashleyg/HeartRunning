package com.local.heartrunning;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainMenu extends Activity {

	// GUI Components
	Button startRunningButton;
	RadioGroup radioGroup;

	
	private int speed;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        // Load GUI Components
        //TODO add GUI components for slow medium fast and get the buttons up and passing data
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        
        RadioButton r = (RadioButton) findViewById(R.id.menu_start_slow_button);
        r.setChecked(true);
        
        
        startRunningButton = (Button)findViewById(R.id.start_run);
        
        
        startRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int selectedId = radioGroup.getCheckedRadioButtonId();
				
				int runSpeed = 0;
				switch (selectedId) {
					case R.id.menu_start_slow_button:
						runSpeed = 0;
						break;
					case R.id.menu_start_medium_button:
						runSpeed = 1;
						break;
					default:
						runSpeed = 2;
						break;
				}
				loadStartRunning(runSpeed);			
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
    public void loadStartRunning(int s) {
    	speed = s;
    	Intent intentStartRunning = new Intent(this, RunningView.class);
    	intentStartRunning.putExtra("speed", speed);
    	startActivity(intentStartRunning);	
    }

}
