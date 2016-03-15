
package com.example.xiaotianweather;

import com.baidu.lbsapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.baidu.nplatform.comapi.map.MapController;
import com.example.xiaotianweather.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

/**
 * 该activity所实现的功能都在其相应的Fragment里实现了
 * 
 * @author Administrator
 */
public class TodayCanActivity extends Activity implements OnClickListener {

    private MapView mapView = null;
    private static Context mapContext;

    private TextView bottom_todaycan;//
    private Intent intent;
    private String cityName;// 百度地图上将要显示的地点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("TAGGG", "cityname : " + cityName);
        initView();
        setdata();
        Log.i("TAGGG", "cityname : " + cityName);
    }

    private void initView() {
        setContentView(R.layout.todaycan_activity);
        mapContext = getApplicationContext();
        mapView = (MapView) findViewById(R.id.map_View);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    private void setdata() {
        cityName = HomePagerActivity.response.getResults().get(0).getCurrentCity();// 获取天气预报界面的城市
        Log.i("设置地点", "城市是： " + cityName);
    }

    @Override
    public void onClick(View v) {

    }
}
