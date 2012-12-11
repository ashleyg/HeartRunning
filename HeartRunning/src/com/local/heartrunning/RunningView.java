package com.local.heartrunning;

import java.io.OutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class RunningView extends Activity {

	// GUI Components
	Button finishRunningButton;
	
	// Other stuff
	GPSManager gps;
	
	private Camera mCamera;
    private CameraPreview mPreview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_view);
        
        // Initialize the GPS view
        gps = new GPSManager(this);
        
        // Load GUI Components
        finishRunningButton = (Button)findViewById(R.id.button_finish_run);
        finishRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopRuning();
			}
		});
        
        // Create an instance of Camera
        mCamera = getCameraInstance();
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mPreview.linkParent(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }
    
    public void processImage(Bitmap bitmap) {
    	/*
    	for(int x = 0; x < bitmap.getWidth(); x++) {
    		for( int y = 0; y < bitmap.getHeight(); y++) {
    			int p = bitmap.getPixel(x, y);
    			Log.d("HR-IP", "P: "+p);
    		}
    	}
    	*/
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
    
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            /*
            b.setMessage("You have a camera! Don't you look nice today.");
            AlertDialog a = b.create();
            a.show();
            */
            return true;
        } else {
        	/*
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            b.setMessage("Sorry this has no camera");
            AlertDialog a = b.create();
            a.show();
            */
            return false;
        }
    }
    
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
