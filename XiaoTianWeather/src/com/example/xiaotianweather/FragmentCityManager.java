
package com.example.xiaotianweather;

import com.example.xiaotianweather.R;
import com.example.xiaotianweather.adapter.GridCityMAdapter;
import com.example.xiaotianweather.bean.CityManagerBean;
import com.example.xiaotianweather.bean.SQLiteCityManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FragmentCityManager extends Fragment {

    public static final String TAG = "CityManager";
    private GridView mgridview;
    private String cityname;
    private String imageurl;
    private String weather;
    private String temp;
    public CityManagerBean cmb;
    public GridCityMAdapter cmAdapter;
    private FragmentAndActivity mActivity;
    public Intent intent;
    private SQLiteCityManager sqlite;
    private SQLiteDatabase db;

    @Override
    public void onResume() {
        getdatabase();
        for (int i = 0; i < FragmentHomeContent.mcmb.size(); i++) {
            if (FragmentHomeContent.mcmb.get(i).getCity()
                    .equals(HomePagerActivity.cmb2.getCity())) {
                FragmentHomeContent.mcmb.remove(HomePagerActivity.cmb2);
            }
        }
        // 标记，为每次打开城市管理页都会加载一个item问题的解决方案
        HomePagerActivity.cmb2.setCity("添加");
        FragmentHomeContent.mcmb.add(FragmentHomeContent.mcmb.size(),
                HomePagerActivity.cmb2);
        cmAdapter.setCitymanager(FragmentHomeContent.mcmb);
        for (int i = 0; i < FragmentHomeContent.mcmb.size(); i++) {
            Log.i("TAG", FragmentHomeContent.mcmb.get(i).getCity());
        }
        Log.i("TAG", FragmentHomeContent.mcmb.size() + "<<<<==>>>>FragmentHomeContent.mcmb");
        cmAdapter.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentAndActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        HomePagerActivity.TAG_H = TAG;
        View homep_content = inflater.inflate(R.layout.gridview_citymanager,
                null);
        mgridview = (GridView) homep_content.findViewById(R.id.gridview);
        intent = new Intent(getActivity(), AddCityActivity.class);
        mgridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position == FragmentHomeContent.mcmb.size() - 1) {
                    startActivity(intent);
                } else {
                    // showDialog
                    mActivity.showDialog();

                    HomePagerActivity.bottom_citymanager
                            .setBackgroundColor(Color.TRANSPARENT);
                    HomePagerActivity.bottom_weathertext.setTextSize(18);
                    HomePagerActivity.bottom_citymanager.setTextSize(16);

                    HomePagerActivity.bottom_weathertext.setTextColor(Color
                            .parseColor(getString(R.string.color_bottombg)));
                    HomePagerActivity.bottom_citymanager.setTextColor(Color
                            .parseColor(getString(R.string.color_bottombgn)));

                    HomePagerActivity ff = (HomePagerActivity) getActivity();
                    ff.chagepage(HomePagerActivity.homecontent,
                            FragmentHomeContent.TAG);
                    // 得到城市，发起网络请求。
                    mActivity.sendcitytext(FragmentHomeContent.mcmb.get(
                            position).getCity());
                }
            }
        });
        // mgridview.setOnItemLongClickListener(new OnItemLongClickListener() {
        //
        // @Override
        // public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
        // int arg2, long arg3) {
        //
        // //返回false则为调用longclick后继续调用click方法。true则为不继续调用。
        // return true;
        // }
        // });
        cmAdapter = new GridCityMAdapter(getActivity(),
                FragmentHomeContent.mcmb);
        mgridview.setAdapter(cmAdapter);

        return homep_content;
    }

    public void getdatabase() {
        // 创建SQLite对象并不会创建数据库
        sqlite = new SQLiteCityManager(getActivity(),
                "testdb", null, 1);
        // 读写数据库
        db = sqlite.getWritableDatabase();
        // 查询的条件
        Cursor cursor = db.query("guanoweather", null, null, null, null, null,
                null);
        FragmentHomeContent.mcmb.clear();
        // 循环的从数据库中取出
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            cityname = cursor.getString(cursor.getColumnIndex("cityname"));
            imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
            weather = cursor.getString(cursor.getColumnIndex("weather"));
            temp = cursor.getString(cursor.getColumnIndex("temp"));
            Log.i("TAG", _id + "  @@@@@@@@_id-" + " cityname-" + cityname + " imageurl-"
                    + imageurl + " weather-" + weather + " temp-" + temp);
            setCityManagerBean();
        }
    }

    /**
     * 设置到CityManagerBean中
     */
    public void setCityManagerBean() {
        cmb = new CityManagerBean();
        cmb.setCity(cityname);
        cmb.setWeather(weather);
        cmb.setTemp(temp);
        cmb.setWeatherimage(imageurl);
        for (int i = 0; i < FragmentHomeContent.mcmb.size(); i++) {
            Log.i("TAG", FragmentHomeContent.mcmb.size() + "==>FragmentHomeContent.mcmb.size()");
            Log.i("TAG", FragmentHomeContent.mcmb.get(i).getCity() + "==>"
                    + "FragmentHomeContent.mcmb.get(i).getCity()");
            if (FragmentHomeContent.mcmb.get(i).getCity().equals(cmb.getCity())) {
                FragmentHomeContent.mcmb.set(i, cmb);
                return;
            }
        }
        FragmentHomeContent.mcmb.add(cmb);
    }

    /**
     * 删除数据库
     */
    public void deletedata() {
        sqlite = new SQLiteCityManager(getActivity(),
                "testdb", null, 1);
        db = sqlite.getWritableDatabase();
        db.delete("guanoweather", "_id = " + 2, null);
    }
}
