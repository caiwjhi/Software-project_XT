
package com.example.xiaotianweather.base;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class Appliction extends Application {

    @Override
    public void onCreate() {
        SDKInitializer.initialize(getApplicationContext());// 百度地图可能会用到
        initImageLoader(this);
        // SDKInitializer.initialize(getApplicationContext());//百度地图可能会用到
    }

    private void initImageLoader(Context ctx) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                ctx).threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .discCacheSize(32 * 1024 * 1024)
                        .memoryCacheSize(4 * 1024 * 1024).enableLogging().build();
        ImageLoader.getInstance().init(config);
    }
}
