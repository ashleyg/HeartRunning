package com.local.heartrunning;

import android.os.Bundle;
import com.facebook.FacebookActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FacebookActivity {

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
        this.openSession();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onSessionStateChange(SessionState state, Exception e) {
    	//TODO: this is a copy paste from facebook
    	// user has either logged in or not ...
		  if (state.isOpened()) {
		    // make request to the /me API
		    Request request = Request.newMeRequest(this.getSession(), new Request.GraphUserCallback() {
		        // callback after Graph API response with user object
		        @Override
		        public void onCompleted(GraphUser user, Response response) {
		          if (user != null) {
	//    	            TextView welcome = (TextView) findViewById(R.id.welcome);
	//    	            welcome.setText("Hello " + user.getName() + "!");
		          }
		        }
		      }
		    );
		    Request.executeBatchAsync(request);
		  }
    }
    
    public void loadMenu() {
		Intent intentMainMenu = new Intent(this,MainMenu.class);
        startActivity(intentMainMenu);	
    }
}
