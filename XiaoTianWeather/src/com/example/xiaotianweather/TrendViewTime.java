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

public class TrendViewTime extends View {

	private Paint mPointPaint;
	private Paint mTextPaint;
	private Paint mTitlePaint;
	private Paint mTimePaint;
	private Paint mLinePaint1;
	private Paint mNamePaint;

	private int x[] = new int[10];
	private float radius = 8;
	private int h;
	private List<Integer> topTem;
	private List<Integer> time;
	private Bitmap[] topBmps;
	
	private Context c;

	public TrendViewTime(Context context) {
		super(context);
		this.c = context;
		init();
	}
	public TrendViewTime(Context context, AttributeSet attr) {
		super(context, attr);
		this.c = context;
		init();
	}
	private void init(){
		topBmps = new Bitmap[10];
		
		topTem = new ArrayList<Integer>();
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
		mTitlePaint.setTextSize(30F);
		mTitlePaint.setTextAlign(Align.CENTER); 
		
		mTimePaint = new Paint();
		mTimePaint.setAntiAlias(true);
		mTimePaint.setColor(Color.BLACK);
		mTimePaint.setTextSize(20F);
		mTimePaint.setTextAlign(Align.CENTER);
		
		mNamePaint = new Paint();
		mNamePaint.setAntiAlias(true);
		mNamePaint.setColor(Color.BLACK);
		mNamePaint.setTextSize(50F);
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

	public void setTemperature(List<Integer> top){
		this.topTem = top;
		postInvalidate();
	}

	public void setTime(List<Integer> time){
		this.time = time;
		postInvalidate();
	}

	public void setBitmap(List<Integer> topList){
		for(int i=0;i<topList.size();i++){
			topBmps[i] = WeatherPicTime.getSmallPic(c, topList.get(i), 0);
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float space = 0f;
		float space1 = 0f;
		int temspace = 12;
		int picSize = 60;	
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();  
		float fontHeight = fontMetrics.bottom - fontMetrics.top;  
		
		int h = this.h-900;
		int h2 = (int) (h - fontHeight/2);	
		int h3 = (int) (h - fontHeight - picSize);
		 
		int topY = h3;
		canvas.drawText("小时天气", (x[4]+x[5])/2, h-300, mNamePaint);
		for (int i = 0; i < topTem.size(); i++) {
			space = ( - topTem.get(i)) * temspace;
			if(topTem.get(i) != 100){
				if (i != topTem.size() - 1) {
					
					space1 = ( - topTem.get(i+1)) * temspace;
					canvas.drawLine(x[i], h + space, x[i+1], h + space1, mLinePaint1);
				}
				canvas.drawText(topTem.get(i) + "°", x[i], h2 + space, mTextPaint);
				canvas.drawCircle(x[i], h + space, radius, mPointPaint);
				canvas.drawBitmap(topBmps[i], x[i]-topBmps[i].getWidth()/2, h3 + space, null);
			}
			if(topY > h3 + space){
				topY = (int) (h3 + space);
			}
		}
		
		topY = (int) (topY + space);
		for (int i = 0; i < time.size(); i++) {
			canvas.drawText(time.get(i).toString()+"时", x[i], topY+400 , mTimePaint);

		}
	}

}
