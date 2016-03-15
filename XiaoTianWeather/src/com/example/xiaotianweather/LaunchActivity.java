
package com.example.xiaotianweather;

import java.io.IOException;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.baidu.location.BDLocation;
import com.example.xiaotianweather.R;
import com.example.xiaotianweather.bean.MHttpEntity;
import com.example.xiaotianweather.bean.ResponseWrapper;
import com.example.xiaotianweather.bean.SendDataBean;
import com.google.gson.GsonBuilder;

public class LaunchActivity extends Activity {

    public static ResponseWrapper response;// 数据结构的对象
    public static final int succeed = 1;
    public static final int fail = 2;
    public static final int nonet = 3;
    public String normalDistrict= "海淀"; //默认地区，当定位失败时，也能显示该地
    public String normalCity = "北京";//默认地区，
    public LocationClient mLocationClient = null;
    public BDLocationListener mListener;
    private ProgressDialog pDialog;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        
        
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("定位中...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
        //-----------------------------------ok----------------
        
        mLocationClient = new LocationClient(this.getApplicationContext());
        mListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mListener);// 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");   //设置坐标类型
        option.setScanSpan(1000);
        //option.disableCache(true);//禁止启用缓存定位
        option.setAddrType("all");//
        option.setIsNeedAddress(true);
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setIgnoreKillProcess(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        if (mLocationClient != null && mLocationClient.isStarted())
		    mLocationClient.requestLocation();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);// 注：异步线程中不能设置UI，暂停3s，在这期间，Mylocationlistener一直在定位，得到的地址信息会在sendRequest()里组合成message，发送给handle

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendRequest();
            }
        }).start();
        Log.i("TAG", "baidu location client: is started: " + mLocationClient.isStarted());
    }

    
    private void sendRequest() {
        Log.i("TAG1", System.currentTimeMillis()
                + "System.currentTimeMillis()222");
        String getData = null;
        MHttpEntity mhe = null;
        try {
        	if(normalDistrict == null)
        		normalDistrict = "海淀区";
            SendDataBean.setCity(normalDistrict);
        	//SendDataBean.setCity(normalCity);
            Log.e("TAG1得到的地址", normalDistrict + "==>>normalDistrict");
            mhe = MHttpEntity.sendhttpclient(SendDataBean.getData());
            if (mhe.getHentity() != null) {
                getData = EntityUtils.toString(mhe.getHentity());
                GsonBuilder gson = new GsonBuilder();//
                response = gson.create().fromJson(getData,
                        ResponseWrapper.class);
                Log.i("TAG1", response.getError() + "-->response.getError()");
                if (response.getError() == -3) {
                    Log.e("TAG -3", normalDistrict + "==>>normalDistrict222");
                    SendDataBean.setCity(normalDistrict);
                    //SendDataBean.setCity(normalCity);
                    mhe = MHttpEntity.sendhttpclient(SendDataBean.getData());
                    if (mhe.getHentity() != null) {
                        getData = EntityUtils.toString(mhe.getHentity());
                        Log.i("TAG1", getData + "-->getData");
                    }
                    if (response.getError() == -3) {
                        SendDataBean.setCity(normalCity);
                        Log.e("TAG1", normalCity + "==>>normalCity");
                        mhe = MHttpEntity
                                .sendhttpclient(SendDataBean.getData());
                        if (mhe.getHentity() != null) {
                            Log.e("TAG1", mhe.getHentity() + "==>>mhe.getHentity()");
                            getData = EntityUtils.toString(mhe.getHentity());
                            Log.i("TAG1", getData + "-->getData");
                        }
                    }
                }
                mhe.getMessage().obj = getData;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler.sendMessage(mhe.getMessage());// 使用Handler对网络状态做处理
    }

    /**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class BaiduReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(LaunchActivity.this, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_SHORT).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(LaunchActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
			}
		}

	}
    /**
     * 对网络连接状态做处理
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (pDialog != null) {
                pDialog.dismiss();
            }
            if (msg != null)
                switch (msg.arg1) {
                    case succeed:// 与服务器连接成功，则传递数据并跳转
                        Toast.makeText(LaunchActivity.this, "服务器连接成功, 定位的地点是"+normalDistrict, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LaunchActivity.this,
                                HomePagerActivity.class);
                        if (msg.obj != null)
                            intent.putExtra("weather_data", (String) msg.obj);
                        Log.i("得到的地址数据： ", (String)msg.obj);
                        Log.i("得到的城市", normalCity);
                        
                        intent.putExtra("normal_city", normalCity);
                        // Toast.makeText(LaunchActivity.this, "before start HomepageActivity,the normalCity:" + normalCity, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                        break;
                    case fail:// 与服务器连接失败，弹出错误提示Toast
                        Toast.makeText(LaunchActivity.this,
                                getString(R.string.net_fail), Toast.LENGTH_SHORT)
                                .show();
                        Toast.makeText(LaunchActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();//
                        Message Mesg = Message.obtain();
                        Mesg.arg1 = nonet;// Handler机制，同抽奖类APP
                        handler.sendMessageDelayed(Mesg, 2000);// 延迟发送
                        break;
                    case nonet:
                        finish();// 2秒后关闭页面
                        break;
                    default:
                        Toast.makeText(LaunchActivity.this, "others ", Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    };

    /**
     * 拦截返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyLocationListener implements BDLocationListener {
//http://bbs.csdn.net/topics/390993420
        @Override
        public void onReceiveLocation(BDLocation location) {
        	Log.i("BDlocation", "listener");
        	if(location == null)
        		return;
        	
        	LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			String address = location.getAddrStr();
			if(address != null)
			{
				Log.i("定位地址address", address);

			}
			if(location.getDistrict() != null)
			{
			     normalDistrict = location.getDistrict();//如果定位失败，则，normaDistrict仍是默认值
			     Log.i("district ", normalDistrict);
			     normalCity = location.getCity();
			     Log.i("normalCity ", normalCity);
			     if(location.getDistrict().equals(normalDistrict))
			    	 mLocationClient.stop();
			    
			}
			else {
				Toast.makeText(LaunchActivity.this, "定位失败，默认为北京海淀区", Toast.LENGTH_SHORT).show();
			}
			Log.i("定位坐标1", ll.toString());
			if(address == null)
				Log.i("address is null", "yes");
			
        }
    }
}
