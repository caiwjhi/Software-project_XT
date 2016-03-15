
package com.example.xiaotianweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.xiaotianweather.R;
import com.example.xiaotianweather.bean.MHttpEntity;
import com.example.xiaotianweather.bean.SendDataBean;
import com.example.xiaotianweather.bean.SportIndexBean;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTodayCan extends Fragment implements OnGetGeoCoderResultListener {

    public static final String TAG = "TodayCan";
    public SportIndexBean sib, sib1, sib2, sib3, sib4, sib5, sib6, sib7;
    public List<SportIndexBean> listsib;

    private MapView mapView = null;
    private LatLng cenpt = new LatLng(30.663791, 104.07281);// 默认为北京;//中心点坐标
    private MapStatus mMapStatus;// 地图状态
    private MapStatusUpdate mMapStatusUpdate;//
    private String cityName;// 百度地图上将要显示的地点
    private BaiduMap baiduMap;
    String pmdata[] = new String[30];
    GeoCoder mSearch = null;

    private Info pmInfo;// 记录该城市里的pm2.5观测点的pm2.5观测值

    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        HomePagerActivity.TAG_H = TAG;
        View homep_content = inflater.inflate(R.layout.gridview_todaycan, null);

        for (int i = 0; i < 30; i++) {
            pmdata[i] = "null";
        }

        cityName = HomePagerActivity.response.getResults().get(0).getCurrentCity();// 获取天气预报界面的城市
        Log.i("设置地点", "城市是： " + cityName);
        new Thread(new Runnable() {

            @Override
            public void run() {
                sendRequest(cityName);
                try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Log.i("new thread ", "after sendRequest");
                for(int i=0;i<10;i++)
                	System.out.print(pmdata[i]);
                pmInfo = new Info(cityName);
                pmInfo.setPms(cityName);
                addOverlay(pmInfo.getPms());
            }
            
        }).start();
        mapView = (MapView) homep_content.findViewById(R.id.map_View);
        baiduMap = mapView.getMap();

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.geocode(new GeoCodeOption()
                .city(cityName)
                .address(cityName));// http://developer.baidu.com/map/index.php?title=androidsdk/guide/retrieval#.E5.9C.B0.E7.90.86.E7.BC.96.E7.A0.81

        
        /**
         * 点击图标时，显示pm2.5值
         */
        baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                // 获得marker中的数据
                Info info = (Info) marker.getExtraInfo().get("info");

                InfoWindow mInfoWindow;
                // 生成一个TextView用户在地图中显示InfoWindow
                TextView location = new TextView(getActivity());
                // location.setBackgroundResource(R.drawable.location_tips);
                location.setPadding(30, 20, 30, 50);
                location.setText(info.getPm());
                Log.i("点击图标", location.getText().toString());
                // 将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = baiduMap.getProjection().toScreenLocation(ll);
                Log.e(TAG, "--!" + p.x + " , " + p.y);
                p.y -= 47;
                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
                return true;
            }
        });
        return homep_content;
    }

    /**
     * 初始化图层
     */
    @SuppressLint("NewApi")
    public void addOverlay(List<Info> infos) {
        baiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;

        for (Info info : infos) {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            Log.i("图层", latLng.toString());
            // 图标
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            if(info.getName() != null && !info.getName().equals("null")){
            	Log.i("info.getName is not ", info.getName());
            int tem = Integer.valueOf(info.getName()).intValue();
            if(tem < 75)
                 textView.setBackground(getResources().getDrawable(R.drawable.pm75));
            else{
            	if(tem < 100)
            		textView.setBackground(getResources().getDrawable(R.drawable.pm100));
            	else{
            		if(tem < 150)
            			textView.setBackground(getResources().getDrawable(R.drawable.pm150));
            		else{
            			if(tem < 200)
            				textView.setBackground(getResources().getDrawable(R.drawable.pm200));
            			else
            				textView.setBackground(getResources().getDrawable(R.drawable.pm400));
            		}
            	}
            }
            textView.setText(info.getName());
            }
            else//防止服务器没开程序崩溃
            {
            	textView.setBackground(getResources().getDrawable(R.drawable.pm75));
            	textView.setText("--");
            }
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(textView));
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(bd).zIndex(5);
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        // TODO Auto-generated method stub
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有检索到结果
            Toast.makeText(getActivity(), "出错了， 默认地图为北京", Toast.LENGTH_SHORT).show();//
            cenpt = new LatLng(30.663791, 104.07281);
            mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(12)
                    .build();
            baiduMap.setMaxAndMinZoomLevel(18, 7);
            Log.i("显示坐标ll", "cenpt :" + cenpt);
            // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            // 改变地图状态
            baiduMap.setMapStatus(mMapStatusUpdate);
            return;
        }
        Log.i("监听", result.getAddress());
        // 获取地理编码结果
        cenpt = result.getLocation();

        mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(12)
                .build();
        baiduMap.setMaxAndMinZoomLevel(18, 7);
        Log.i("显示坐标", "cenpt :" + cenpt);
        // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        // TODO Auto-generated method stub
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有找到检索结果

        }
        // 获取反向地理编码结果
    }

    /**
     * 这是一个私有类，用来显示pm2.5观测站点的相关信息
     * 
     * @author Administrator
     */
    private class Info implements Serializable {

        private static final long serialVersionUID = -758459502806858414L;
        /**
         * 经度。在百度地图上显示时会用到
         */
        private double latitude;
        /**
         * 纬度
         */
        private double longitude;

        /**
         * 观测点名称
         */
        private String name;
        /**
         * 距离,可能用不到
         */
        private String distance;

        /**
         * 该城市名字用来查找城市里的所有pm2.5观测站的观测值
         */
        private String cityName;

        /**
         * 观测点name处的pm2.5的值
         */
        private String pm25;

        /**
         * 该List用来测试，直接初始化一组数值用来显示，实际应该由查询得到
         */
        private List<Info> pms = new ArrayList<Info>();

        /**
         * 该函数应该用数据库来查找cityname里的所有观测点信息，若数据库得到的信息里没有经纬度的值，可以用上面的mSearch来得到经纬度
         * 
         * @param cityName
         */
        public void setPms(String cityName) {

            for (int i = 0; i < 5; i++)
                System.out.println(pmdata[i]);
            String fileName = "beijing.txt"; // 文件名字
            Log.i("todaycan", "cityName " + (cityName.equals("北京")));
            if (cityName.equals("北京"))
                fileName = "beijing.txt";
            if (cityName.equals("上海"))
                fileName = "shanghai.txt";
            if (cityName.equals("天津"))
                fileName = "tianjin.txt";
            if (cityName.equals("成都"))
                fileName = "chengdu.txt";
            if (cityName.equals("福州"))
                fileName = "fuzhou.txt";
            if (cityName.equals("海口"))
                fileName = "haikou.txt";
            if (cityName.equals("杭州"))
                fileName = "hangzhou.txt";
            if (cityName.equals("合肥"))
                fileName = "hefei.txt";
            if (cityName.equals("呼和浩特"))
                fileName = "huhehaote.txt";
            if (cityName.equals("昆明"))
                fileName = "kunming.txt";
            if (cityName.equals("拉萨"))
                fileName = "lasa.txt";
            if (cityName.equals("南昌"))
                fileName = "nanchang.txt";
            if (cityName.equals("南京"))
                fileName = "nanjing.txt";
            if (cityName.equals("上海"))
                fileName = "shanghai.txt";
            if (cityName.equals("沈阳"))
                fileName = "shenyang.txt";
            if (cityName.equals("天津"))
                fileName = "tianjin.txt";
            
            System.out.print(cityName);
            String res = "";
            try {
                InputStream in = getResources().getAssets().open(fileName);
                // Testassetsyan.txt这里有这样的文件存在
                int length = in.available();
                byte[] buffer = new byte[length];
                in.read(buffer);
                res = EncodingUtils.getString(buffer, "GBK");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // System.out.print(res);
            Scanner scanner = new Scanner(res);
            double x[][] = new double[20][2];
            int i = 0;
            int j = 0;
            while (scanner.hasNext()) {
                String world = scanner.next();
                System.out.println(world);
                if (i % 3 == 1)
                    x[j][0] = Double.valueOf(world);
                if (i % 3 == 2) {
                    x[j][1] = Double.valueOf(world);
                    j++;
                }
                i++;
            }
            for (int k = 0; k < j; k++) {
                pms.add(new Info(x[k][0], x[k][1], pmdata[k], cityName, "0"));
            }

        }

        public List<Info> getPms() {
            return pms;
        }

        public Info(String cityName) {
            this.cityName = cityName;
        }

        public Info(double latitude, double longitude, String name,
                String cityName, String pm) {
            super();
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
            this.cityName = cityName;
            this.pm25 = pm;
        }

        public String getPm() {
            return pm25;
        }

        public void setPm(String pm) {
            this.pm25 = pm;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String name) {
            this.cityName = name;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

    }

    public void sendRequest(String cityname) {
    	 String getData = null;
	     HttpClient client=new DefaultHttpClient();
	        HttpPost request;
        try {
        	if(cityname.equals("海淀区"))
        		cityname="北京";
        	request=new HttpPost("http://166.111.206.74:8080/TEST/pm/"+cityname);
        	//需要判断网址是否可以访问，防止程序崩溃
        	
        	HttpResponse response = client.execute(request);
            Log.i("wwwwww", "wrong22" + getData);
            HttpEntity entity =response.getEntity();

            
            if (entity != null) {
                getData = EntityUtils.toString(entity);
                Log.i("todaycan", "getData " + getData);           
                pmdata =getData.split(".0 ");
            }
        } catch (ParseException e) {
        	Log.i("catch1 ", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
        	Log.i("catch2 ", e.toString());
            e.printStackTrace();
        }
        finally {
        	Log.i("after catch", "");
		}
        
    }
}
