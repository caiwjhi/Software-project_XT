
package com.example.xiaotianweather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.ui.widget.StatusButton.StatusButtonChild;
import com.example.xiaotianweather.R;
import com.example.xiaotianweather.bean.CityManagerBean;
import com.example.xiaotianweather.bean.MHttpEntity;
import com.example.xiaotianweather.bean.ResponseWrapper;
import com.example.xiaotianweather.bean.SendDataBean;
import com.google.gson.GsonBuilder;

public class HomePagerActivity extends FragmentActivity implements
        OnClickListener, FragmentAndActivity {

	private NotificationManager notificationManager;   
	
    private long nowtime;// 保存当前时间
    public static TextView bottom_weathertext;// 底部天气预报
    public static ResponseWrapper response = new ResponseWrapper();// 数据结构的对象
    public static ResponseWrapper response2;
    public static TextView bottom_citymanager;// 底部
    public static TextView bottom_todaycan;// 底部
    private DrawerLayout drawerlayout_main;// drawerlayout_main
    private View left_drawer;
    private EditText inputcity;//
    // private String inputcitytext;
    public static final int succeed = 1;
    public static final int fail = 2;
    public static final int nonet = 3;
    private static int tag = 0;
    public static String TAG_H = null;
    public String wetherdata=null;
    private ProgressDialog pDialog;
    public static FragmentHomeContent homecontent = new FragmentHomeContent();
    public FragmentCityManager citymanager = new FragmentCityManager();
    public static CityManagerBean cmb2 = new CityManagerBean();
    
    private static int backgroundSrc = R.drawable.background4;//xml文件里的背景
    //private int temSrc;//每次getIntent时得到的背景图片，

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepager_main_activity);
        
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
        
        Intent intent = getIntent();
        if(intent != null)
             Log.i("主界面", "intent is null ? " + intent);
        wetherdata = intent.getStringExtra("weather_data");// 得到启动页传递过来的数据
       // temSrc = intent.getIntExtra("background_src", backgroundSrc);//得到修改背景时传来的图片，没有时默认为xml里的背景
        if(wetherdata != null)
        {	
              Log.i("主界面1", "weather_data : " + wetherdata);
              Log.i("主界面显示的城市是  ", "");//????
        }
        GsonBuilder gson = new GsonBuilder();//
        response2 = gson.create().fromJson(wetherdata, ResponseWrapper.class);
        if (response2.getError() == 0) {
            response = response2;
        }
        showNotification();
        initview();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentlayout, homecontent, FragmentHomeContent.TAG);
        ft.commit();

        bottom_weathertext.setTextColor(Color
                .parseColor(getString(R.string.color_bottombg)));
    }

    private void initview() {
        bottom_weathertext = (TextView) findViewById(R.id.bottom_weathertext);
        bottom_todaycan = (TextView) findViewById(R.id.bottom_todaycan);
        bottom_citymanager = (TextView) findViewById(R.id.bottom_citymanager);
        drawerlayout_main = (DrawerLayout) findViewById(R.id.drawerlayout_main);
        left_drawer = findViewById(R.id.left_drawer);
        drawerlayout_main.setScrimColor(0x00000000);// 设置底部页面背景透明度
    }

   
    public void showNotification(){
    	//2步创建一个Notification  
        Notification notification = new Notification();  
        //设置通知 消息  图标  
        notification.icon=R.drawable.xiaotian_icon;  
        //设置发出消息的内容  
        notification.tickerText="天气提醒";  
        //设置发出通知的时间  
        notification.when=System.currentTimeMillis();  
          
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE; //发起Notification后，铃声和震动均只执行一次
        //设置显示通知时的默认的发声、振动、Light效果  
        notification.defaults = Notification.DEFAULT_VIBRATE;//振动  
        notification.flags = Notification.FLAG_AUTO_CANCEL;     //通知被点击后，自动消失
        Intent intent = new Intent();//空Intent，使得点击通知时，不跳转
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);  
        //天气信息确定
        String todaydata = HomePagerActivity.response.getResults().get(0)
                .getWeather_data().get(0).getDate();
        String temperature = HomePagerActivity.response.getResults().get(0)
                .getWeather_data().get(0).getTemperature();
        String subs = null;
        String tempToNote = null;//将要被通知的温度
        if (todaydata.length() > 14) {
            subs = todaydata.substring(14, todaydata.length() - 1);
            tempToNote = subs;
        } else if (temperature.length() > 5) {
            String[] str = temperature.split("~ ", 2);
            subs = str[1];
            tempToNote = subs;
        } else {
            tempToNote = temperature;
        }
        
        String textToShow = null;
        textToShow = response.getResults().get(0).getCurrentCity() +"  温度:" + tempToNote + "  pm2.5 " + response.getResults().get(0).getPm25();
        Log.i("消息提示", textToShow);
        //4步：设置更加详细的信息  
        notification.setLatestEventInfo(this, "", textToShow, pendingIntent);  
          
          
          
        //5步：使用notificationManager对象的notify方法 显示Notification消息   需要制定 Notification的标识  
        notificationManager.notify(R.drawable.xiaotian_icon, notification); 
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homep_menu:
                openleftlayout();
                break;
            case R.id.share:
            	//showToast("分享。。。");
            	
            	shareMsg(getTitle().toString(), "小天天气", "分享", GetandSaveCurrentImage());
            	/*Intent intent=new Intent(Intent.ACTION_SEND);   
                intent.setType("image/*");   
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");   
                intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");    
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
                startActivity(Intent.createChooser(intent, getTitle()));*/   
            	break;
            case R.id.homep_refresh:
                showDialog();
                bottom_weathertext.setTextSize(18);
                bottom_citymanager.setTextSize(16);
                bottom_todaycan.setTextSize(16);

                bottom_todaycan.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                bottom_citymanager.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                bottom_weathertext.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombg)));
                chagepage(homecontent, FragmentHomeContent.TAG);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        sendRequest(FragmentHomeContent.currentcity.getText()
                                .toString());
                    }
                }).start();
                showNotification();
                break;
            case R.id.bottom_todaycan:
                bottom_todaycan.setTextSize(18);
                bottom_citymanager.setTextSize(16);
                bottom_weathertext.setTextSize(16);

                bottom_todaycan.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombg)));
                bottom_citymanager.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                bottom_weathertext.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                chagepage(new FragmentTodayCan(), FragmentTodayCan.TAG);
                break;
            case R.id.bottom_citymanager:
                bottom_citymanager.setTextSize(18);
                bottom_weathertext.setTextSize(16);
                bottom_todaycan.setTextSize(16);

                bottom_citymanager.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombg)));
                bottom_weathertext.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                bottom_todaycan.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                chagepage(citymanager, FragmentCityManager.TAG);
                
                break;
            case R.id.bottom_weathertext:
                bottom_weathertext.setTextSize(18);
                bottom_citymanager.setTextSize(16);
                bottom_todaycan.setTextSize(16);

                bottom_weathertext.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombg)));
                bottom_citymanager.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                bottom_todaycan.setTextColor(Color
                        .parseColor(getString(R.string.color_bottombgn)));
                chagepage(homecontent, FragmentHomeContent.TAG);
               
                break;
            case R.id.button1:
            	Intent intent1=new Intent(HomePagerActivity.this,WeatherChartActivity.class);
                intent1.putExtra("weather_data",wetherdata );
                startActivityForResult(intent1, 0);
                break;   
    		case R.id.button3:
    			//TODO..
    			Intent intent3 = new Intent(HomePagerActivity.this, BackgroundChange.class);
    			startActivity(intent3);
    			break;
    		case R.id.button4:
    			//TODO..
    			Intent intent4=new Intent(HomePagerActivity.this,LifeIndexActivity.class);
    			startActivity(intent4);
    			break;
    		case R.id.button5:
    			Intent intent5=new Intent(HomePagerActivity.this,AboutApp.class);
    			startActivity(intent5);
    			
    			break;
            
            case R.id.exitapp:
                final Dialog dialog = new Dialog(this,
                        android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                View exitappview = getLayoutInflater().inflate(
                        R.layout.exitapp_dialog, null);
                TextView exitapp_text = (TextView) exitappview.findViewById(R.id.exitapp_text);
                Button leftbutton = (Button) exitappview.findViewById(R.id.leftbutton);
                Button rightbutton = (Button) exitappview.findViewById(R.id.rightbutton);
                exitapp_text.setText("退出程序");
                leftbutton.setText("确定");
                rightbutton.setText("取消");
                leftbutton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                rightbutton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(exitappview);
                dialog.show();
                break;
        }
    }

    private void fromJson(String wetherdata) {
        GsonBuilder gson = new GsonBuilder();//
        response2 = gson.create().fromJson(wetherdata, ResponseWrapper.class);
        if (response2.getError() == 0) {
            response = response2;
            homecontent.setpagedata();
            if (tag == 4 && inputcity != null) {
                closeinput(inputcity);
            }
        } else if (response2.getError() == -3 || response2.getError() == -2) {
            showToast(getString(R.string.input_truename));
        } else {
            showToast(getString(R.string.getdata_fail));
        }
        if (FragmentHomeContent.pDialog != null) {
            FragmentHomeContent.pDialog.dismiss();
        }
    }

    /**
     * 点击多次bt，Toast只显示一次的解决方案
     */
    public Toast toast = null;

    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 向服务器发送数据请求
     */
    public void sendRequest(String cityname) {
        String getData = null;
        MHttpEntity mhe = null;
        try {
            SendDataBean.setCity(cityname);// 获取用户输入的城市名
            mhe = MHttpEntity.sendhttpclient(SendDataBean.getData());
            if (mhe.getHentity() != null) {
                getData = EntityUtils.toString(mhe.getHentity());
                mhe.getMessage().obj = getData;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.sendMessage(mhe.getMessage());// 使用Handler对网络状态做处理
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (pDialog != null)
                pDialog.dismiss();
            if (msg != null)
                switch (msg.arg1) {
                    case succeed:// 与服务器连接成功
                        if (msg.obj != null) {
                            fromJson(msg.obj.toString());
                            wetherdata=msg.obj.toString();
                            
                        }
                        break;
                    case fail:// 与服务器连接失败
                        showToast(getString(R.string.net_fail));
                        break;
                }
        }
    };

    /**
     * 关联menu键
     */
    private void openleftlayout() {
        if (drawerlayout_main.isDrawerOpen(left_drawer)) {
            drawerlayout_main.closeDrawer(left_drawer);
        } else {
            drawerlayout_main.openDrawer(left_drawer);
        }
    }

    public void chagepage(Fragment fragment, String str) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();// 开启一个事物，得到事物对象ft
        ft.replace(R.id.fragmentlayout, fragment, str);
        ft.commit();
    }

    /**
     * 关闭输入法键盘
     */
    public void closeinput(EditText editText) {
        editText.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(
                HomePagerActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 连续按两次返回则退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - nowtime > 2000) {
                Toast.makeText(this, R.string.click_exit, Toast.LENGTH_SHORT)
                        .show();
                nowtime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onResume() {
    	super.onResume();
    	int temSrc = BackgroundChange.backgroundSelected;
    	if(temSrc != backgroundSrc){
    		RelativeLayout tem = (RelativeLayout)findViewById(R.id.include_content);
			tem.setBackgroundResource(temSrc);
			backgroundSrc = temSrc;
    	}
    	else{
    		RelativeLayout tem = (RelativeLayout)findViewById(R.id.include_content);
			tem.setBackgroundResource(temSrc);
    	}
    		
    };
    
    
    @Override
    public void senddata(EditText inputcity) {
        this.inputcity = inputcity;
    }

    @Override
    public void sendcitytext(final String inputcitytext) {
        // this.inputcitytext = inputcitytext;
        tag = 4;
        if ("".equals(inputcitytext)) {
            showToast(getString(R.string.edittext_hint));
            if (FragmentHomeContent.pDialog != null) {
                FragmentHomeContent.pDialog.dismiss();
            }
        } else {
            SendDataBean.setCity(inputcitytext);// 获取用户输入城市
            new Thread(new Runnable() {

                @Override
                public void run() {
                    sendRequest(inputcitytext);
                }
            }).start();
        }
    }

    @Override
    public void showDialog() {
        pDialog = new ProgressDialog(HomePagerActivity.this);
        pDialog.setCancelable(true);// 点击可以取消Dialog的展现
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("正在更新...");
        pDialog.show();
    }
    
    /**
     * 分享功能
     * 
     * 
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
            String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
    /**
     * 从Assets中读取图片
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
 
        return image;
 
    }
    
    /**
    * 获取和保存当前屏幕的截图
    */
    private String GetandSaveCurrentImage()  
    { 
    	String filepath = null;
	    //构建Bitmap  
	    WindowManager windowManager = getWindowManager();  
	    Display display = windowManager.getDefaultDisplay();  
	    int w = display.getWidth();  
	    int h = display.getHeight();  
	    Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );      
	    //获取屏幕  
	    View decorview = this.getWindow().getDecorView();   
	    decorview.setDrawingCacheEnabled(true);   
	    Bmp = decorview.getDrawingCache();   
	    //图片存储路径
	    String SavePath = getSDCardPath()+"/Demo/ScreenImages";
	    //保存Bitmap   
	    try {  
	    File path = new File(SavePath);  
	    //文件  
	    filepath = SavePath + "/Screen_1.png";  
	    File file = new File(filepath);  
	    if(!path.exists()){  
	    path.mkdirs();  
	    }  
	    
	    if (!file.exists()) {  
	         file.createNewFile();  
	    }  
	    
	   // showToast(filepath);
	    Log.i("截图", filepath);
	    FileOutputStream fos = null;  
	    fos = new FileOutputStream(file);  
	    if (null != fos) {  
		    Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);  
		    fos.flush();  
		    fos.close();    
	   // Toast.makeText(mContext, "截屏文件已保存至SDCard/ScreenImages/目录下",Toast.LENGTH_LONG).show();  
	       // showToast("截屏文件已保存至" + filepath);
	    }  
	    } catch (Exception e) {  
	    	e.printStackTrace();  
	    }  
	    
	    return filepath;
	 }  
	    /**
	    * 获取SDCard的目录路径功能
	    * @return
	    */
	private String getSDCardPath(){
	    File sdcardDir = null;
	    //判断SDCard是否存在
	    boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	    if(sdcardExist){
	    sdcardDir = Environment.getExternalStorageDirectory();
	    }
	    return sdcardDir.toString();
	}
}
