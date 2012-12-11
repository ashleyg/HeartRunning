package com.local.heartrunning;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static String TAG = "HR-CAM";
    private RunningView rv;

    @SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    
    // I apologies for the horrible self reference here, feel free to 
    // do this in a better way if you can think of one - AG
    public void linkParent(RunningView rv) {
    	this.rv = rv;
    }

	public void onPreviewFrame(byte[] imageData, Camera arg1) {
		Size ps = mCamera.getParameters().getPreviewSize();
		YuvImage image = new YuvImage(imageData,ImageFormat.YV12,ps.width,ps.height, null);
		// TODO - Look into getting the camera to give a native BMP format, available on some phones, will need code
		// to do both.
		
		// Explains the following - http://stackoverflow.com/questions/7794307/getting-image-from-surfaceview-to-imageview
		// Some tricks to speed things up = http://kfb-android.blogspot.co.uk/2009/04/image-processing-in-android.html
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compressToJpeg(new Rect(0,0,ps.width,ps.height), 50, out);
		Bitmap bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
		rv.processImage(bitmap); // Pass it up to the parent processing method
	}
}