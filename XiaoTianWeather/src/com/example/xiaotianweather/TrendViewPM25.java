package com.example.xiaotianweather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class TrendViewPM25 extends View {

	private Paint mPointPaint;
	private Paint mTextPaint;
	private Paint mTitlePaint;
	private Paint mTimePaint;
	private Paint mLinePaint1;
	private Paint mNamePaint;

	private int x[] = new int[10];
	private float radius = 8;
	private int h;
	private List<Integer> pm25;
	private List<Integer> time;
	
	private Context c;

	public TrendViewPM25(Context context) {
		super(context);
		this.c = context;
		init();
	}
	public TrendViewPM25(Context context, AttributeSet attr) {
		super(context, attr);
		this.c = context;
		init();
	}
	private void init(){
		
		pm25 = new ArrayList<Integer>();
		time = new ArrayList<Integer>();
		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.GRAY);

		mLinePaint1 = new Paint();
		mLinePaint1.setColor(Color.YELLOW);
		mLinePaint1.setAntiAlias(true);
		mLinePaint1.setStrokeWidth(4);
		mLinePaint1.setStyle(Style.FILL);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(25F);
		mTextPaint.setTextAlign(Align.CENTER); 
		
		mTitlePaint = new Paint();
		mTitlePaint.setAntiAlias(true);
		mTitlePaint.setColor(Color.BLACK);
		mTitlePaint.setTextSize(20F);
		mTitlePaint.setTextAlign(Align.CENTER); 
		
		mTimePaint = new Paint();
		mTimePaint.setAntiAlias(true);
		mTimePaint.setColor(Color.BLACK);
		mTimePaint.setTextSize(20F);
		mTimePaint.setTextAlign(Align.CENTER);
		
		mNamePaint = new Paint();
		mNamePaint.setAntiAlias(true);
		mNamePaint.setColor(Color.BLACK);
		mNamePaint.setTextSize(30F);
		mNamePaint.setTextAlign(Align.CENTER);
	}
	public void setWidthHeight(int w, int h){
		x[0] = w/20;
		x[1] = w*3/20;
		x[2] = w*5/20;
		x[3] = w*7/20;
		x[4] = w*9/20;
		x[5] = w*11/20;
		x[6] = w*13/20;
		x[7] = w*15/20;
		x[8] = w*17/20;
		x[9] = w*19/20;
		
		this.h = h;
	}

	public void setPM25(List<Integer> pm25){
		this.pm25 = pm25;
		postInvalidate();
	}

	public void setTime(List<Integer> time){
		this.time = time;
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float space = 0f;
		float space1 = 0f;
		int temspace = 12;	
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();  
		float fontHeight = fontMetrics.bottom - fontMetrics.top;  
		
		int h = this.h-300;
		int h2 = (int) (h - fontHeight/2);	
		int h3 = (int) (h - fontHeight - 60);
		 
		int topY = h3;
		canvas.drawText("小时PM2.5", (x[4]+x[5])/2, h-350, mNamePaint);
		for (int i = 0; i < pm25.size(); i++) {
			space = ( - pm25.get(i)/20) * temspace;
			if(pm25.get(i) != 501){
				if (i != pm25.size() - 1) {
					
					space1 = ( - pm25.get(i+1)/20) * temspace;
					canvas.drawLine(x[i], h + space, x[i+1], h + space1, mLinePaint1);
				}
				canvas.drawText(pm25.get(i).toString(), x[i], h2 + space, mTextPaint);
				canvas.drawCircle(x[i], h + space, radius, mPointPaint);
			}
			if(topY > h3 + space){
				topY = (int) (h3 + space);
			}
		}
		
		topY = (int) (topY + space);
		for (int i = 0; i < time.size(); i++) {
			canvas.drawText(time.get(i).toString()+"时", x[i], topY+600 , mTimePaint);

		}
	}

}
