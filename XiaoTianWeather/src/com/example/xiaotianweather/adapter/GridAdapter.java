
package com.example.xiaotianweather.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xiaotianweather.R;
import com.example.xiaotianweather.bean.SportIndexBean;

public class GridAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<SportIndexBean> sportIndex;

    public GridAdapter(Context context, List<SportIndexBean> sportIndex) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview_todaycan,
                    parent, false);// 此处需要加上第二个参数parent，否则item中的设置无效。如item高度设置。
        }
        TextView dothing = (TextView) convertView.findViewById(R.id.dothing);
        TextView index = (TextView) convertView.findViewById(R.id.index);
        // 设置数据
        dothing.setText(sportIndex.get(position).getTipt());
        index.setText(sportIndex.get(position).getZs());
        return convertView;
    }

    /**
     * 屏蔽item的点击事件
     */
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
