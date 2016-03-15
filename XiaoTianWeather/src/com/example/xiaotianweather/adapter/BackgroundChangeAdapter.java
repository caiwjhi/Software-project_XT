package com.example.xiaotianweather.adapter;

import com.example.xiaotianweather.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class BackgroundChangeAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private int res[] = {R.drawable.background, R.drawable.background1, R.drawable.background2,
			R.drawable.background3, R.drawable.background4, R.drawable.background5,
			R.drawable.background6};
	 private int[] resours = {
	            R.drawable.ic_todaycan_date,
	            R.drawable.ic_todaycan_jingdian, R.drawable.ic_todaycan_dress,
	            R.drawable.ic_todaycan_carwash, R.drawable.ic_todaycan_tour,
	            R.drawable.ic_todaycan_coldl, R.drawable.ic_todaycan_sport,
	            R.drawable.ic_todaycan_ultravioletrays
	    };

	public BackgroundChangeAdapter(Context c) {
		this.mContext = c;
		this.mInflater = LayoutInflater.from(c);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return res.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_gridview_backgroundchange, parent, false);
			//convertView.setLayoutParams(new GridView.LayoutParams(85, 85));
			//convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			//convertView.setPadding(8,8,8,8);
		}
		ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_background);
		//imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setPadding(12,12,12,12);
		imageView.setBackgroundResource(res[position]);
		
		return convertView;
	}

}
