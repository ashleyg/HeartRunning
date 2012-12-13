package com.local.heartrunning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {

	

	Bitmap mBitmap;
	Canvas mCanvas;
	Paint 	mPaint = new Paint(Paint.DITHER_FLAG);
	
	
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(Color.WHITE);
	}
	/*
	public GraphView(Context context) {
		super(context);
				
	}
	*/
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);
         mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
         mCanvas = new Canvas(mBitmap);
         
         mCanvas.drawLine(0, 0, w, h, mPaint);
     }

     @Override
     protected void onDraw(Canvas canvas) {
    	 Log.d("DRAWING", "DRAWING");
    	 
         canvas.drawColor(0xFFAAAAAA);
         // canvas.drawLine(mX, mY, Mx1, My1, mPaint);
         // canvas.drawLine(mX, mY, x, y, mPaint);
         canvas.drawBitmap(mBitmap, 0, 0, mPaint);
     }

}
