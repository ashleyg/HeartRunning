package com.local.heartrunning;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	// GUI Components
	Button loginButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Load GUI Components
        loginButton = (Button)findViewById(R.id.login_button);
        
        loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadMenu();			
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void loadMenu() {
		Intent intentMainMenu = new Intent(this,MainMenu.class);
        startActivity(intentMainMenu);	
    }
}