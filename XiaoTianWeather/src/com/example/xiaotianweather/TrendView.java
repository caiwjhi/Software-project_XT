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

public class TrendView extends View {

	private Paint mPointPaint;
	private Paint mTextPaint;
	private Paint mTimePaint;
	private Paint mLinePaint1;
	private Paint mLinePaint2;
	private Paint mNamePaint;

	private int x[] = new int[4];
	private float radius = 8;
	private int h;
	private List<Integer> topTem;
	private List<Integer> lowTem;
	private List<String> timeWeek;
	private Bitmap[] topBmps;
	
	private Context c;

	public TrendView(Context context) {
		super(context);
		this.c = context;
		init();
	}
	public TrendView(Context context, AttributeSet attr) {
		super(context, attr);
		this.c = context;
		init();
	}
	private void init(){
		topBmps = new Bitmap[4];
		
		topTem = new ArrayList<Integer>();
		lowTem = new ArrayList<Integer>();
		timeWeek = new ArrayList<String>();

		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.GRAY);

		mLinePaint1 = new Paint();
		mLinePaint1.setColor(Color.YELLOW);
		mLinePaint1.setAntiAlias(true);
		mLinePaint1.setStrokeWidth(4);
		mLinePaint1.setStyle(Style.FILL);
		
		mLinePaint2 = new Paint();
		mLinePaint2.setColor(Color.BLUE);
		mLinePaint2.setAntiAlias(true);
		mLinePaint2.setStrokeWidth(4);
		mLinePaint2.setStyle(Style.FILL);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(20F);
		mTextPaint.setTextAlign(Align.CENTER); 
		
		mTimePaint = new Paint();
		mTimePaint.setAntiAlias(true);
		mTimePaint.setColor(Color.BLACK);
		mTimePaint.setTextSize(27F);
		mTimePaint.setTextAlign(Align.CENTER);
		
		mNamePaint = new Paint();
		mNamePaint.setAntiAlias(true);
		mNamePaint.setColor(Color.RED);
		mNamePaint.setTextSize(50F);
		mNamePaint.setTextAlign(Align.CENTER);
	}
	public void setWidthHeight(int w, int h){
		x[0] = w/8;
		x[1] = w*3/8;
		x[2] = w*5/8;
		x[3] = w*7/8;
		
		this.h = h;
	}

	public void setTemperature(List<Integer> top, List<Integer> low){
		this.topTem = top;
		this.lowTem = low;
		
		postInvalidate();
	}

	public void setTime(List<String> timeWeek2){
		this.timeWeek = timeWeek2;
		
		postInvalidate();
	}

	public void setBitmap(List<Integer> topList){
		for(int i=0;i<topList.size();i++){
			topBmps[i] = WeatherPic.getSmallPic(c, topList.get(i), 0);
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float space = 0f;
		float space1 = 0f;
		int temspace = 12;
		int picSize = 100;	
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();  

		float fontHeight = fontMetrics.bottom - fontMetrics.top;  
		
		int h = this.h/2;
		int h2 = (int) (h - fontHeight/2);
		int h3 = (int) (h - fontHeight - picSize);
		 
		int h4 = (int) (h + fontHeight);
		int topY = h3;
		canvas.drawText("一周天气", (x[1]+x[2])/2, h-350, mNamePaint);
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
		int lowY = h4 ;
		for (int i = 0; i < lowTem.size(); i++) {
			space = (-lowTem.get(i)) * temspace;
			if (i != lowTem.size() - 1) {
				space1 = ( - lowTem.get(i+1)) * temspace;
				canvas.drawLine(x[i], h + space, x[i+1], h + space1, mLinePaint2);
			} 
			canvas.drawText(lowTem.get(i) + "°", x[i], h4 + space, mTextPaint);
			canvas.drawCircle(x[i], h + space, radius, mPointPaint);
			if(lowY < (int) (h4 + space + fontHeight)){
				lowY = (int) (h4 + space + fontHeight);
			}
		}
		topY = (int) (topY + space);
		lowY = (int) (lowY - space);
		for (int i = 0; i < timeWeek.size(); i++) {
			canvas.drawText(timeWeek.get(i), x[i], lowY , mTimePaint);
		}
	}

}
