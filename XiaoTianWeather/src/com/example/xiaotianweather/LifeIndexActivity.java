package com.example.xiaotianweather;

import java.util.Calendar;
import java.util.List;

import com.example.xiaotianweather.bean.SportIndexBean;
import com.example.xiaotianweather.bean.port;
import com.example.xiaotianweather.FragmentHomeContent;
import com.example.xiaotianweather.HomePagerActivity;
import com.example.xiaotianweather.adapter.GridTodayCAdapter;
import com.example.xiaotianweather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 生活指数
 * @author ACER
 *
 */
public class LifeIndexActivity extends Activity{
	
	public Button buttonBack;
	public TextView index_show;
	public SportIndexBean sib, sib1, sib2, sib3, sib4, sib5, sib6, sib7;//8个指数项
	public List<SportIndexBean> listsib;//用来显示的指数
	
	private int[] resours = {
            R.drawable.ic_todaycan_date,
            R.drawable.ic_todaycan_jingdian, R.drawable.ic_todaycan_dress,
            R.drawable.ic_todaycan_carwash, R.drawable.ic_todaycan_tour,
            R.drawable.ic_todaycan_coldl, R.drawable.ic_todaycan_sport,
            R.drawable.ic_todaycan_ultravioletrays
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.life_index);
	
		buttonBack = (Button)findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}//关闭该界面
        	
        });
		
		GridView todayinfo_grid = (GridView) 
				findViewById(R.id.gridview);
		//描述text
		index_show = (TextView) findViewById(R.id.index_show);

		listsib = HomePagerActivity.response.getResults().get(0).getIndex();
		Log.i("Index activity ", listsib.toString()+"==>>listsib.toString()");
		Log.i("Index activity ", String.valueOf(listsib.size()));
		//因为成都的index数据为空
		if (HomePagerActivity.response.getResults().get(0).getIndex()//getIndex()是一个List
				.toString() == "[]") {
//			sib = new SportIndexBean();
			Log.i("TAG", index_show.getText()+"==>>todaycan_dec");
			
			listsib.add(sib);
			listsib.add(sib1);
			listsib.add(sib2);
			listsib.add(sib3);
			listsib.add(sib4);
			listsib.add(sib5);
			listsib.add(sib6);
			listsib.add(sib7);
			try{
			todayinfo_grid.setAdapter(new GridTodayCAdapter(this, 
					HomePagerActivity.response.getResults().get(0).getIndex()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else {
			sib = new SportIndexBean();
			sib1 = new SportIndexBean();
			Calendar calendar = Calendar.getInstance();
			calendar.get(Calendar.YEAR);
			calendar.get(Calendar.MONTH +1);
			calendar.get(Calendar.DAY_OF_MONTH);
//			calendar.get(Calendar.YEAR)+"年"+
//			calendar.get(Calendar.MONTH +1)+"月"+
//			calendar.get(Calendar.DAY_OF_MONTH)+"日"
			sib.setZs("点击查看");
			sib1.setZs("点击查看");
			try{
			sib.setDes("公(阳)历："+calendar.get(Calendar.YEAR)+"年"+
				String.valueOf(calendar.get(Calendar.MONTH) + 1) +"月"+
				calendar.get(Calendar.DAY_OF_MONTH)+"日"+"\n"+
				"农(阴)历：" + FragmentHomeContent.lunarStr);
			}
			catch(Exception e){
				e.printStackTrace();
			}//STring int转换会抛出异常
			sib1.setDes(getVisitPlaces(HomePagerActivity.response.getResults().get(0).getCurrentCity()));
			if(listsib.size() < 7){
			   listsib.add(sib);
			   listsib.add(sib1);
			}
			Log.i("Index activity2 ", String.valueOf(listsib.size()));
			index_show.setText(listsib.get(6).getDes());
			
			todayinfo_grid.setAdapter(new GridTodayCAdapter(this,
					listsib));
			
			//listsib = HomePagerActivity.response.getResults().get(0).getIndex();
		}
		todayinfo_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(index_show.getText().equals("")){
					
				}else {
					Log.i("position show ", String.valueOf(position));
					if(position == 0)
						index_show.setText(listsib.get(6).getDes());
					else
					{
						if(position == 1)
							index_show.setText(listsib.get(7).getDes());
						else
							index_show.setText(listsib.get(position - 2).getDes());
					}
					
				}
			}
		});
		
	
	}
	/*
	 * 根据参数城市名，得到一串景点地名
	 */
	public String getVisitPlaces(String cityName){
		String places = "抱歉，未查到";
		/*if(cityName.equals("北京"))
			places = "故宫，天安门，长城，\n 北海公园，鸟巢，颐和园，清华大学，等";
		if(cityName.equals("上海"))
			places = "东方明珠，上海博物馆，朱家角，\n 龙华寺，豫园，外滩万国建筑博览";
		if(cityName.equals("天津"))
			places = "";
		if(cityName.equals("广州"))
			places = "";
		if(cityName.equals("南京"))
			places = "";
		if(cityName.equals("成都"))
			places = "";*/
		String cityNames[] = {"北京", "上海", "广州", 
				"南京", "成都", "武汉",
				"杭州", "西安", "济南", 
				"长春", "东莞", "沈阳", 
				"天津", "哈尔滨", "长沙", 
				"呼和浩特", "石家庄", "重庆", 
				"无锡", "包头", "大连", 
				"深圳", "福州", "海口", 
				"乌鲁木齐", "兰州", "银川", 
				"太原", "郑州", "合肥",
				"南昌", "南宁", "贵阳", 
				"昆明", "拉萨", "西宁", 
				"台北", "香港", "澳门"
				};
		String visitPlacesNames[] = {"故宫，天安门，长城，\n 北海公园，鸟巢，颐和园，清华大学，等", "东方明珠，上海博物馆，朱家角，\n 龙华寺，豫园，外滩万国建筑博览，等", "白云山，莲花山，等", 
				"夫子庙，明孝陵，\n 中山陵，海底世界，等", "都江堰，武侯祠，金沙遗址博物馆，等", "东湖，黄鹤楼公园，九宫山风景区，等", 
				"杭州西湖，飞来峰，灵隐寺，等", "兵马俑，碑林博物馆， 西安古城墙，等", "大明湖，千佛山风景区，等", 
				"净月潭，双阳湖，长影世纪城，等", "可园，鸦片战争纪念馆，威远炮台，等", "棋盘山，清福陵，清沈阳故宫，等", 
				"天后宫，石家大院，等", "圣索菲亚大教堂，冰灯游园会，冰雪大世界，等", "花明楼，岳麓书院，橘子洲，等", //5
				"大召，清公主府，昭君墓，等", "隆兴寺，赵州桥，等", "金佛山，大足石刻艺术群，等", 
				"灵山大佛，太湖欢乐园，等", "美岱召，五当召，南海公园，等", "森林动物园，劳动公园，等", 
				"欢乐谷，世界之窗，等", "涌泉寺，鼓山，西禅寺，等", "热带野生动植物园，五公祠，假日海滩，等", 
				"五彩湾，天池风景区，等", "黄河母亲雕塑，石佛沟国家森林公园，等", "贺兰山风景区，西夏王陵，等",
				"常家庄园，天龙山石窟，等", "河南博物馆，黄帝故里，等", "包河秀色，逍遥古津， 明教寺，等", //10
				"滕王阁，明月山，等", "青秀山，大明山，等", "花溪公园， 百花湖，等", 
				"九乡，西山森林公园，大观楼，等", "布达拉宫，大昭寺，等", "孟达天池，东关清真大寺，等", 
				"阳明山公园，台北101大楼，等", "香港海洋公园，维多利亚港，等", "渔人码头，澳门葡京赌场，等",
				};
		int i = 0;
		for(i = 0; i < 39; i++)
		{
			if(cityName.equals(cityNames[i]))
				break;
		}
		if(i < 39)
			places = visitPlacesNames[i];
		return places;
		
	}

}
