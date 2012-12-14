package com.local.heartrunning;

/**
 * Draw the graph of the heart rate
 * @author Ashley Griffiths
 *
 */
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	
	ArrayList<DataPoint> data;	
	
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(Color.GREEN);
	}
	
	public void linkData(ArrayList<DataPoint> data) {
		this.data = data;
	}
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);
         mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
         mCanvas = new Canvas(mBitmap);
         mCanvas.drawRect(new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()), new Paint(Color.TRANSPARENT));
         
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
			int sY = getGraphHeight(data.get(data.size()-2).getBrightness());
			int eX = (int) (currentPoint*widthPerPoint);
			int eY = getGraphHeight(data.get(data.size()-1).getBrightness());
			Log.d("G","HPP: "+heightPerPoint+ " sY: "+sY+" eY: "+eY);
			mCanvas.drawLine(sX,sY,eX,eY,mPaint);
		}
		currentPoint++;
		if(currentPoint > pointsToDraw) {
			currentPoint = 0;
			mCanvas.drawRect(new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()), new Paint(Color.TRANSPARENT));
			updateDrawingStats();
		}
	}
	
	public void drawPeaks(ArrayList<DataPoint> peaks) {
		for(DataPoint dp : peaks) {
			mCanvas.drawLine(0, getGraphHeight(dp.getBrightness()), mCanvas.getWidth(), getGraphHeight(dp.getBrightness()), mPaint );
		}
	}
	
	private void updateDrawingStats() {
		// This stops things breaking if we rotate
		if(data.size() > pointsToDraw) {
			max = 0;
			min = 1000000000;
			
			for(int i=data.size()-(pointsToDraw);i<data.size();i++) {
				max = Math.max(max, data.get(i).getBrightness());
				min = Math.min(min,data.get(i).getBrightness());
			}
			delta = max-min;
	        heightPerPoint = (float)mBitmap.getHeight()/(float)delta;
		}
	}
	
	private int getNewMidpoint() {
		int total = 0;
		for(int i=data.size()-(pointsToDraw);i<data.size();i++) {
			total += data.get(i).getBrightness();
		}
		return total/pointsToDraw;
	}

     @Override
     protected void onDraw(Canvas canvas) {
    	 updateGraph();
    	 
         canvas.drawColor(0xFFFFFFFF);
         canvas.drawBitmap(mBitmap, 0, 0, mPaint);
     }

}
