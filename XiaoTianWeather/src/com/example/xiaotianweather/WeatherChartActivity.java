package com.example.xiaotianweather;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.xiaotianweather.bean.MHttpEntity;
import com.example.xiaotianweather.bean.SendDataBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SimpleExpandableListAdapter;

public class WeatherChartActivity extends Activity {
	private List<Integer> maxlist;
	private List<Integer> minlist;
	private List<Integer> hourlist;
	private List<Integer> hourPic;
	private List<Integer> topPic;
	private List<Integer> pmlist;
	public String Hour="Clear 0.0 Clear 0.0 Clear 0.0 Clear 0.0 Clear 0.0 Clear 0.0 Clear 0.0"
			+ " Clear 0.0 Clear 0.0 Clear 0.0";
	public String Pm="0 0 0 0 0 0 0 0 0 0";
	private List<String> timeWeek;
	private List<Integer> time;
	
	private int[] temTop={7,8,9,10};
	private int[] temLow={1,2,3,3};
	private int[] imgTop={2,2,3,5};
	private String[] WeatherImage={"晴","多云","阴","阵雨","晴转多云","雷雨","雨夹雪","多云转晴","雷阵雨","多云转阴"
			,"小雨转多云","","","阵雪","","中雪","","","","冰雹"
			,"","中雨","大雨","","","暴雨","小雪","大雪","暴雪",""
			,"","沙尘暴","雾"};
	private String[] hourWeatherImage={"Clear","Mostly Cloudy","Overcast","Chance of Rain","Partly Cloudy","Thunderstorm","Snow Showers","Partly Cloudy","Chance of a Thunderstorm","",
			"","","","Chance of Snow","","Snow","","","","",
			"","Rain","","","","","","","","",
			"","",""};
	private int temHour[] = {12,12,6,9,12,6,7,8,5,3};
	private int imgHour[] = {2,2,2,2,1,2,1,1,2,2};
	private int pmHour[] = {50,100,200,300,100,450,375,25,70,255};
	private String Cityname; 
	private String[] week = {"今天","明天","后天","大后天"};
	private int[] hour= {1,2,3,4,5,6,7,8,9,10};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_chart);
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();		// 屏幕宽（像素，如：480px）
	    int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
	    TrendView trend = (TrendView) findViewById(R.id.weather);
	    trend.setWidthHeight(screenWidth, screenHeight/2);
	    final TrendViewTime trend2 = (TrendViewTime) findViewById(R.id.weather_time);
	    trend2.setWidthHeight(screenWidth, screenHeight);
	    trend2.setVisibility(View.VISIBLE);
	    TrendViewPM25 trend3 = (TrendViewPM25) findViewById(R.id.pm_time);
	    trend3.setWidthHeight(screenWidth, screenHeight);
	    trend3.setVisibility(View.VISIBLE);
	    Intent intent=getIntent();
	    Cityname = HomePagerActivity.response.getResults().get(0).getCurrentCity();
	    new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3);// 注：异步线程中不能设置UI

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendRequest(Cityname);
                try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }).start();
	    new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3);// 注：异步线程中不能设置UI

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendRequest2(Cityname);
            }
        }).start();
        
	    String wetherdata = intent.getStringExtra("weather_data");
	    Log.i("界面1", "weather_data : " + wetherdata);
	    String regex="[0-9]{1,2} ~ [0-9]{1,2}";
	    Pattern pattern=Pattern.compile(regex);
	    Matcher matcher=pattern.matcher(wetherdata);
	    int j=0;
	    while(matcher.find())
	    {
	    	String[] str=matcher.group(0).split(" ~ ",2);
	    	temTop[j]=Integer.parseInt(str[0]);
	    	temLow[j]=Integer.parseInt(str[1]);
	    	j++;
	    }
	    
	    String regex2="\"weather\":\"([\u4e00-\u9fa5]+)";
	    Pattern pattern2=Pattern.compile(regex2);
	    Matcher matcher2=pattern2.matcher(wetherdata);
	    j=0;
	    while(matcher2.find())
	    {
	    	String str2=matcher2.group().substring(11);
	    	for(int k=0;k<WeatherImage.length;k++)
	    	{
	    		if(WeatherImage[k].equals(str2))
	    		{
	    			imgTop[j]=k;
	    			Log.i("debug", str2+' '+k);
	    			j++;
	    			break;
	    		}
	    	}
	    	
	    }
	    
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //得到未来十小时的天气
	    String everyHour[]=Hour.split("\\.",11);
        Log.i("lhg",everyHour[0]+everyHour[9]);
        for(int i=0;i<10;i++)
        {
        	int k=0;
        	for(int j1=everyHour[i].length()-1;j1>=0;j1--)
        	{
        		if(everyHour[i].charAt(j1)<48||everyHour[i].charAt(j1)>57)
        		{
        			k=j1;
        			break;
        		}
        	}
        	String hourTem=everyHour[i].substring(k+1);
        	String hourWeather;
        	if(i==0)hourWeather=everyHour[0].substring(0,k);
        	else hourWeather=everyHour[i].substring(2,k);
        	Log.i("llllllhg",hourWeather+' '+hourTem);
        	temHour[i]=Integer.parseInt(hourTem);
        	for(int h=0;h<hourWeatherImage.length;h++)
	    	{
	    		//Log.i("hhhhhhhh:", hourWeatherImage[h]);
	    		if(hourWeatherImage[h].equals(hourWeather))
	    		{
	    			//Log.i("gggggggg:","the picture is No."+h);
	    			imgHour[i]=h;
	    			break;
	    		}
	    	}
    	    Log.i("iiiiiii","the picture is No."+imgHour[i]);
        }
	    
        //得到十小时Pm2.5的预测值
        String everyPm[]=Pm.split(" ",10);
        for(int i=0;i<10;i++)
        {
        	Log.i("abcdef",everyPm[i]);
        	float aa=Float.parseFloat(everyPm[i]);
        	pmHour[i]=(int) aa;
        }
        
        //未来十小时的时间
        SimpleDateFormat formatter=new SimpleDateFormat("HH");
        String currentHour=formatter.format(new java.util.Date());
        Log.i("hourhour",currentHour+" o'clock");
        hour[0]=Integer.parseInt(currentHour)+1;
        for(int i=1;i<=9;i++)
        {
        	hour[i]=hour[i-1]+1;
        	if(hour[i]>=24)hour[i]-=24;
        }
        
	    maxlist = new ArrayList<Integer>();
	    minlist = new ArrayList<Integer>();
	    topPic = new ArrayList<Integer>();
	    hourlist = new ArrayList<Integer>();
	    hourPic = new ArrayList<Integer>();
	    time = new ArrayList<Integer>();
	    pmlist = new ArrayList<Integer>();
	    
	    //time = new ArrayList<String>();
	    timeWeek = new ArrayList<String>();

	    for(int i=0;i<temTop.length;i++){
	    	maxlist.add(temTop[i]);
	    	minlist.add(temLow[i]);
	    	topPic.add(imgTop[i]);

	    	timeWeek.add(week[i]);
	    }
	    for(int i=0;i<temHour.length;i++){
	    	hourlist.add(temHour[i]);
	    	hourPic.add(imgHour[i]);
	    	pmlist.add(pmHour[i]);
	    	time.add(hour[i]);
	    }
	    trend.setTemperature(maxlist, minlist);
	    trend.setBitmap(topPic);
	    trend.setTime(timeWeek);
	    
	    trend2.setTemperature(hourlist);
	    trend2.setBitmap(hourPic);
	    trend2.setTime(time);
	    
	    trend3.setPM25(pmlist);
	    trend3.setTime(time);
	}
	 @SuppressWarnings("deprecation")
	public void sendRequest(String cityname) {
	        String getData = null;
	        HttpClient client=new DefaultHttpClient();
	        HttpPost request;
	        Log.i("wwwwww", "wrong" + getData);
	        try {
	        	if(cityname.equals("海淀区"))
	        		cityname="北京";
	            request=new HttpPost("http://166.111.206.74:8080/TEST/sky/"+cityname);
	            Log.i("wwwwww", "wrong22" + getData);
	            HttpResponse response =client.execute(request);
	            Log.i("wwwwww", "wrong22" + getData);
	            HttpEntity entity =response.getEntity();
	            if (entity != null) {
	                getData = EntityUtils.toString(entity);
	                Hour=getData;
	                Log.i("wwwwww", "hour" + getData);
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 public void sendRequest2(String cityname) {
	        String getData = null;
	        HttpClient client=new DefaultHttpClient();
	        HttpPost request;
	        Log.i("wwwwww", "wrong" + getData);
	        try {
	        	if(cityname.equals("海淀区"))
	        		cityname="北京";
	            request=new HttpPost("http://166.111.206.74:8080/TEST/map/"+cityname);
	            Log.i("wwwwww", "wrong22" + getData);
	            HttpResponse response =client.execute(request);
	            Log.i("wwwwww", "wrong22" + getData);
	            HttpEntity entity =response.getEntity();
	            if (entity != null) {
	                getData = EntityUtils.toString(entity);
	                Pm=getData;
	                Log.i("wwwwww", "hour" + getData);
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


}
