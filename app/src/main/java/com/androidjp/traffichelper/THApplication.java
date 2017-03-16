package com.androidjp.traffichelper;

import android.app.Application;
import android.content.Context;


/**
 * 自定义Application基类
 * Created by androidjp on 2016/12/1.
 */

public class THApplication extends Application {

    private static THApplication sInstance;

    public static Context getContext(){
        return getInstance().getApplicationContext();
    }

    public static THApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
