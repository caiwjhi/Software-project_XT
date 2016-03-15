package com.example.xiaotianweather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.example.xiaotianweather.adapter.BackgroundChangeAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BackgroundChange extends Activity{
	
	private GridView backgroundGridView;
	private Button buttonBack;
	
	/*
	 * 被选中的背景图片，用来和HomePagerActivity里的原有的背景图片比较，不同时则变换背景
	 */
	public static int backgroundSelected = R.drawable.background4;
	
	private int res[] = {R.drawable.background, R.drawable.background1, R.drawable.background2,
			R.drawable.background3, R.drawable.background4, R.drawable.background5,
			R.drawable.background6};
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background_change);
		
		buttonBack = (Button)findViewById(R.id.buttonBack);
		
		buttonBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		backgroundGridView = (GridView)findViewById(R.id.gridview_background);
		
		backgroundGridView.setAdapter(new BackgroundChangeAdapter(this));
		
		backgroundGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), ""+position,Toast.LENGTH_SHORT).show();//显示信息;
			    //变换背景
				Log.i("choose 背景", String.valueOf(position));
				backgroundSelected = res[position];
				
				finish();
			}
			
		});
		
	}

}
