
package com.example.xiaotianweather.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaotianweather.R;
import com.example.xiaotianweather.bean.SportIndexBean;

public class GridTodayCAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<SportIndexBean> sportIndex;
    private int[] resours = {
            R.drawable.ic_todaycan_date,
            R.drawable.ic_todaycan_jingdian, R.drawable.ic_todaycan_dress,
            R.drawable.ic_todaycan_carwash, R.drawable.ic_todaycan_tour,
            R.drawable.ic_todaycan_coldl, R.drawable.ic_todaycan_sport,
            R.drawable.ic_todaycan_ultravioletrays
    };

    public GridTodayCAdapter(Context context, List<SportIndexBean> sportIndex) {
        this.mInflater = LayoutInflater.from(context);
        this.sportIndex = sportIndex;
    }

    @Override
    public int getCount() {
        return sportIndex == null ? 0 : sportIndex.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	Log.i("in getView ", "position : " + position);
    	Log.i("in getView ", "getCount : " + getCount());
        if (convertView == null) {
            // 此处需要加上第二个参数parent，否则item中的设置无效。如item高度设置。
            convertView = mInflater.inflate(R.layout.item_gridview_todaycan, parent, false);
        }

        TextView dothing = (TextView) convertView.findViewById(R.id.dothing);
        TextView index = (TextView) convertView.findViewById(R.id.index);
        ImageView image_index = (ImageView) convertView
                .findViewById(R.id.image_index);
        ImageView image_click = (ImageView) convertView
                .findViewById(R.id.image_click);

        // 设置数据
        if (position%8 == 0) {
            dothing.setText("今天是");
        } else if (position%8 == 1) {
            dothing.setText("城市景点");
        } else if (position%8 == 2) {
            dothing.setText("穿衣指数");
        } else if (position%8 == 3) {
            dothing.setText("洗车指数");
        } else if (position%8 == 4) {
            dothing.setText("旅游指数");
        } else if (position%8 == 5) {
            dothing.setText("感冒指数");
        } else if (position%8 == 6) {
            dothing.setText("运动指数");
        } else if (position%8 == 7) {
            dothing.setText("紫外线指数");
        } else {
            dothing.setText(sportIndex.get(position).getTipt());
        }

        try {
            if (sportIndex.get(position).getZs() != null) {
                if (position == 0) {
                    index.setText(sportIndex.get(position + 6).getZs());
                } else if (position == 1) {
                    index.setText(sportIndex.get(position + 6).getZs());
                } else {
                    index.setText(sportIndex.get(position - 2).getZs());
                }
            } else {
            	//if(position < 8)
                    index.setText("暂无");
            }
        } catch (NullPointerException e) {
            index.setText("暂无");
        }
        Log.i("TAG", sportIndex.size() + "sportIndex.size()");
        Log.i("adapter position ", String.valueOf(position));
        //if(position < 8){
             image_index.setBackgroundResource(resours[position%8]);
             image_click.setBackgroundResource(R.drawable.ic_todaycan_clickbt);
        //}
        return convertView;
    }
}
