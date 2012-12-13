package com.local.heartrunning;

import java.util.ArrayList;

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
	int pointsToDraw = 100;
	int currentPoint = 0;
	
	int min = 14000000;
	int max = 20000000;
	int delta = max-min;

	float widthPerPoint;
	float heightPerPoint;
	
	ArrayList<Integer> data;	
	
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(Color.GREEN);
	}
	
	public void linkData(ArrayList<Integer> data) {
		this.data = data;
	}
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);
         mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
         mCanvas = new Canvas(mBitmap);
         
         widthPerPoint = w/pointsToDraw;
         heightPerPoint = (float)h/(float)delta;
         
         //mCanvas.drawLine(0, 0, w, h, mPaint);
     }
	
	private int getGraphHeight(int value) {
		
		return (int) ((value-min)*heightPerPoint);
	}
	
	private void updateGraph() {
		if(currentPoint > 0 && data.size() >= 2) {
			int sX = (int) ((currentPoint-1)*widthPerPoint);
			int sY = getGraphHeight(data.get(data.size()-2));
			int eX = (int) (currentPoint*widthPerPoint);
			int eY = getGraphHeight(data.get(data.size()-1));
			Log.d("G","HPP: "+heightPerPoint+ " sY: "+sY+" eY: "+eY);
			mCanvas.drawLine(sX,sY,eX,eY,mPaint);
		}
		currentPoint++;
		if(currentPoint > pointsToDraw)
			currentPoint = 0;
	}

     @Override
     protected void onDraw(Canvas canvas) {
    	 updateGraph();
    	 
         canvas.drawColor(0xFFFFFFFF);
         canvas.drawBitmap(mBitmap, 0, 0, mPaint);
     }

}
